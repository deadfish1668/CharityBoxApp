package com.charity.charitybox;

import com.charity.charitybox.model.CurrencyType;
import com.charity.charitybox.model.FundraisingEvent;
import com.charity.charitybox.service.CollectionBoxService;
import com.charity.charitybox.service.FundraisingEventService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CharityBoxIntegrationTest {

    @Autowired
    private FundraisingEventService eventService;

    @Autowired
    private CollectionBoxService boxService;

    @Test
    void testFullIntegration_flow() {
        // 1. Create fundraising event in EUR
        FundraisingEvent event = eventService.createEvent("Integration Test Event", CurrencyType.EUR);

        // 2. Register and assign a box
        var box = boxService.registerBox();
        boxService.assignBoxToEvent(box.getId(), event.getId());

        // 3. Add 10 EUR + 5 USD
        boxService.addMoney(box.getId(), CurrencyType.EUR, BigDecimal.valueOf(10));
        boxService.addMoney(box.getId(), CurrencyType.USD, BigDecimal.valueOf(5)); // ~4.55 EUR

        // 4. Empty the box
        boxService.emptyBox(box.getId());

        // 5. Assert balance updated correctly (10 + 4.55)
        FundraisingEvent updatedEvent = eventService.getEventById(event.getId());
        assertEquals(BigDecimal.valueOf(14.55), updatedEvent.getBalance());
    }
    @Test
    void testAssignBoxFailsIfNotEmpty() {
        // Create event
        FundraisingEvent event = eventService.createEvent("Reject Assignment", CurrencyType.EUR);

        // Register and manually set up box
        var box = boxService.registerBox();

        // Add money (so it's not empty)
        boxService.addMoney(box.getId(), CurrencyType.EUR, BigDecimal.valueOf(10));

        // Try to assign — should throw
        Exception exception = assertThrows(RuntimeException.class, () -> {
            boxService.assignBoxToEvent(box.getId(), event.getId());
        });

        String message = exception.getMessage();
        assertTrue(message.contains("not empty") || message.contains("Cannot assign"), "Expected message about box being not empty");
    }
}
