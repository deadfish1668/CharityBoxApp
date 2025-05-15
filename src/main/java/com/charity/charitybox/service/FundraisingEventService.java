package com.charity.charitybox.service;

import com.charity.charitybox.model.CurrencyType;
import com.charity.charitybox.model.FundraisingEvent;
import com.charity.charitybox.repository.FundraisingEventRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class FundraisingEventService {

    private final FundraisingEventRepository eventRepository;

    public FundraisingEventService(FundraisingEventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public FundraisingEvent createEvent(String name, CurrencyType currency) {
        FundraisingEvent event = new FundraisingEvent(name, currency);
        return eventRepository.save(event);
    }

    public List<FundraisingEvent> getAllEvents() {
        return eventRepository.findAll();
    }

    public FundraisingEvent getEventById(Long id) {
        return eventRepository.findById(id).orElseThrow(() -> new RuntimeException("Event not found"));
    }

    public void addToBalance(Long eventId, BigDecimal amount) {
        FundraisingEvent event = getEventById(eventId);
        event.addToBalance(amount);
        eventRepository.save(event);
    }
}
