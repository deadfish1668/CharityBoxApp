package com.charity.charitybox.service;

import com.charity.charitybox.model.CurrencyType;
import com.charity.charitybox.model.FundraisingEvent;
import com.charity.charitybox.repository.FundraisingEventRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FundraisingEventServiceTest {

    private final FundraisingEventRepository repo = mock(FundraisingEventRepository.class);
    private final FundraisingEventService service = new FundraisingEventService(repo);

    @Test
    void testCreateEvent() {
        FundraisingEvent event = new FundraisingEvent("Test Event", CurrencyType.EUR);
        when(repo.save(any())).thenReturn(event);

        FundraisingEvent result = service.createEvent("Test Event", CurrencyType.EUR);

        assertEquals("Test Event", result.getName());
        assertEquals(CurrencyType.EUR, result.getCurrency());
        assertEquals(BigDecimal.ZERO, result.getBalance());
    }

    @Test
    void testAddToBalance() {
        FundraisingEvent event = new FundraisingEvent("Test", CurrencyType.EUR);
        event.setId(1L);
        when(repo.findById(1L)).thenReturn(Optional.of(event));

        service.addToBalance(1L, BigDecimal.valueOf(50));
        assertEquals(BigDecimal.valueOf(50), event.getBalance());
        verify(repo).save(event);
    }
}
