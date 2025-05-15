package com.charity.charitybox.controller;

import com.charity.charitybox.model.CollectionBox;
import com.charity.charitybox.model.CurrencyType;
import com.charity.charitybox.service.CollectionBoxService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/boxes")
public class CollectionBoxController {

    private final CollectionBoxService boxService;

    public CollectionBoxController(CollectionBoxService boxService) {
        this.boxService = boxService;
    }

    // 1. Register a new box
    @PostMapping
    public ResponseEntity<CollectionBox> registerBox() {
        return ResponseEntity.ok(boxService.registerBox());
    }

    // 2. List all boxes
    @GetMapping
    public List<CollectionBox> getAllBoxes() {
        return boxService.getAllBoxes();
    }

    // 3. Assign to event
    @PostMapping("/{boxId}/assign/{eventId}")
    public ResponseEntity<String> assignToEvent(@PathVariable Long boxId, @PathVariable Long eventId) {
        boxService.assignBoxToEvent(boxId, eventId);
        return ResponseEntity.ok("Box assigned successfully");
    }

    // 4. Add money
    @PostMapping("/{boxId}/add-money")
    public ResponseEntity<String> addMoney(@PathVariable Long boxId, @RequestBody Map<String, String> request) {
        CurrencyType currency = CurrencyType.valueOf(request.get("currency"));
        BigDecimal amount = new BigDecimal(request.get("amount"));
        boxService.addMoney(boxId, currency, amount);
        return ResponseEntity.ok("Money added successfully");
    }

    // 5. Empty the box
    @PostMapping("/{boxId}/empty")
    public ResponseEntity<String> emptyBox(@PathVariable Long boxId) {
        boxService.emptyBox(boxId);
        return ResponseEntity.ok("Box emptied and funds transferred.");
    }

    // 6. Unregister a box
    @DeleteMapping("/{boxId}")
    public ResponseEntity<String> unregister(@PathVariable Long boxId) {
        boxService.unregisterBox(boxId);
        return ResponseEntity.ok("Box unregistered.");
    }

}
