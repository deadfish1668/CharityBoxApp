package com.charity.charitybox.service;

import com.charity.charitybox.model.CollectionBox;
import com.charity.charitybox.model.CurrencyType;
import com.charity.charitybox.model.FundraisingEvent;
import com.charity.charitybox.repository.CollectionBoxRepository;
import com.charity.charitybox.repository.FundraisingEventRepository;
import com.charity.charitybox.util.CurrencyConverter;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
public class CollectionBoxService {

    private final CollectionBoxRepository boxRepository;
    private final FundraisingEventRepository eventRepository;

    public CollectionBoxService(CollectionBoxRepository boxRepository, FundraisingEventRepository eventRepository) {
        this.boxRepository = boxRepository;
        this.eventRepository = eventRepository;
    }

    public CollectionBox registerBox() {
        return boxRepository.save(new CollectionBox());
    }

    public List<CollectionBox> getAllBoxes() {
        return boxRepository.findAll();
    }

    public void unregisterBox(Long boxId) {
        CollectionBox box = getBox(boxId);
        box.setEmpty(true);
        box.getMoney().clear();
        box.setAssignedEvent(null);
        boxRepository.save(box);
        boxRepository.delete(box);
    }

    public void assignBoxToEvent(Long boxId, Long eventId) {
        CollectionBox box = getBox(boxId);
        if (!box.isEmpty()) {
            throw new RuntimeException("Cannot assign: box is not empty.");
        }

        FundraisingEvent event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        box.setAssignedEvent(event);
        boxRepository.save(box);
    }

    public void addMoney(Long boxId, CurrencyType currency, BigDecimal amount) {
        CollectionBox box = getBox(boxId);

        if (box.isEmpty()) {
            box.setEmpty(false);
        }

        box.getMoney().merge(currency, amount, BigDecimal::add);
        boxRepository.save(box);
    }

    public void emptyBox(Long boxId) {
        CollectionBox box = getBox(boxId);

        if (box.isEmpty() || box.getAssignedEvent() == null) {
            throw new RuntimeException("Box is empty or not assigned to an event.");
        }

        FundraisingEvent event = box.getAssignedEvent();
        CurrencyType targetCurrency = event.getCurrency();
        BigDecimal totalConverted = BigDecimal.ZERO;

        for (Map.Entry<CurrencyType, BigDecimal> entry : box.getMoney().entrySet()) {
            BigDecimal converted = CurrencyConverter.convert(entry.getKey(), targetCurrency, entry.getValue());
            totalConverted = totalConverted.add(converted);
        }

        event.addToBalance(totalConverted);
        eventRepository.save(event);

        box.setEmpty(true);
        box.getMoney().clear();
        boxRepository.save(box);
    }

    private CollectionBox getBox(Long id) {
        return boxRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Box not found"));
    }
}
