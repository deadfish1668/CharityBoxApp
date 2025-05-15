package com.charity.charitybox.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Entity
public class CollectionBox {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean empty = true;

    @ManyToOne
    private FundraisingEvent assignedEvent;

    @ElementCollection
    @CollectionTable(name = "box_money")
    @MapKeyEnumerated(EnumType.STRING)
    private Map<CurrencyType, BigDecimal> money = new HashMap<>();

    public CollectionBox() {}

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public boolean isEmpty() { return empty; }

    public void setEmpty(boolean empty) { this.empty = empty; }

    public FundraisingEvent getAssignedEvent() { return assignedEvent; }

    public void setAssignedEvent(FundraisingEvent assignedEvent) {
        this.assignedEvent = assignedEvent;
    }

    public Map<CurrencyType, BigDecimal> getMoney() {
        return money;
    }

    public void setMoney(Map<CurrencyType, BigDecimal> money) {
        this.money = money;
    }

    // Helper method to add money
    public void addMoney(CurrencyType currency, BigDecimal amount) {
        this.money.put(currency, this.money.getOrDefault(currency, BigDecimal.ZERO).add(amount));
        this.empty = false;
    }

    // Helper method to clear money
    public void clearMoney() {
        this.money.clear();
        this.empty = true;
    }
}
