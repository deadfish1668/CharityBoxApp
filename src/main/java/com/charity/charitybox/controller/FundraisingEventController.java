package com.charity.charitybox.controller;

import com.charity.charitybox.model.CurrencyType;
import com.charity.charitybox.model.FundraisingEvent;
import com.charity.charitybox.service.FundraisingEventService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.stream.Collectors;
import java.util.HashMap;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/events")
public class FundraisingEventController {

    private final FundraisingEventService eventService;

    public FundraisingEventController(FundraisingEventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping
    public ResponseEntity<FundraisingEvent> createEvent(@RequestBody Map<String, String> request) {
        String name = request.get("name");
        CurrencyType currency = CurrencyType.valueOf(request.get("currency"));
        FundraisingEvent event = eventService.createEvent(name, currency);
        return ResponseEntity.ok(event);
    }

    @GetMapping
    public List<FundraisingEvent> getAllEvents() {
        return eventService.getAllEvents();
    }



    @GetMapping("/report")
    public ResponseEntity<List<Map<String, Object>>> financialReport() {
        List<FundraisingEvent> events = eventService.getAllEvents();

        List<Map<String, Object>> report = events.stream()
                .map(event -> {
                    Map<String, Object> row = new HashMap<>();
                    row.put("name", event.getName());
                    row.put("amount", event.getBalance());
                    row.put("currency", event.getCurrency());
                    return row;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(report);
    }
}
