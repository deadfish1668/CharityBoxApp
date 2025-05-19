package com.charity.charitybox.service;

import com.charity.charitybox.model.*;
import com.charity.charitybox.repository.CollectionBoxRepository;
import com.charity.charitybox.repository.FundraisingEventRepository;
import com.charity.charitybox.util.CurrencyConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CollectionBoxServiceTest {

    private CollectionBoxRepository boxRepo;
    private FundraisingEventRepository eventRepo;
    private CollectionBoxService service;

    @BeforeEach
    void setUp() {
        boxRepo = mock(CollectionBoxRepository.class);
        eventRepo = mock(FundraisingEventRepository.class);
        service = new CollectionBoxService(boxRepo, eventRepo);
    }

    @Test
    void testRegisterBox() {
        CollectionBox box = new CollectionBox();
        when(boxRepo.save(any())).thenReturn(box);

        CollectionBox result = service.registerBox();
        assertTrue(result.isEmpty());
    }

    @Test
    void testAssignBoxToEvent() {
        FundraisingEvent event = new FundraisingEvent("Event", CurrencyType.EUR);
        event.setId(1L);
        CollectionBox box = new CollectionBox();
        box.setId(1L);
        box.setEmpty(true);

        when(boxRepo.findById(1L)).thenReturn(Optional.of(box));
        when(eventRepo.findById(1L)).thenReturn(Optional.of(event));

        service.assignBoxToEvent(1L, 1L);

        assertEquals(event, box.getAssignedEvent());
        verify(boxRepo).save(box);
    }

    @Test
    void testAddMoney() {
        CollectionBox box = new CollectionBox();
        box.setId(1L);
        when(boxRepo.findById(1L)).thenReturn(Optional.of(box));

        service.addMoney(1L, CurrencyType.USD, BigDecimal.valueOf(5));
        assertFalse(box.isEmpty());
        assertEquals(BigDecimal.valueOf(5), box.getMoney().get(CurrencyType.USD));
    }
    @Test
    void testEmptyBoxTransfersFundsToEvent() {
        // Given
        FundraisingEvent event = new FundraisingEvent("Hope Event", CurrencyType.EUR);
        event.setId(1L);

        CollectionBox box = new CollectionBox();
        box.setId(1L);
        box.setEmpty(false);
        box.setAssignedEvent(event);

        // Box contains 10 EUR and 5 USD (converted to EUR at 0.91 = 4.55)
        Map<CurrencyType, BigDecimal> money = new HashMap<>();
        money.put(CurrencyType.EUR, BigDecimal.valueOf(10));
        money.put(CurrencyType.USD, BigDecimal.valueOf(5));
        box.setMoney(money);

        when(boxRepo.findById(1L)).thenReturn(Optional.of(box));
        when(eventRepo.findById(1L)).thenReturn(Optional.of(event));

        // When
        service.emptyBox(1L);

        // Then
        assertEquals(BigDecimal.valueOf(14.55), event.getBalance());
        assertTrue(box.isEmpty());
        assertTrue(box.getMoney().isEmpty());

        verify(eventRepo).save(event);
        verify(boxRepo).save(box);
    }

}
