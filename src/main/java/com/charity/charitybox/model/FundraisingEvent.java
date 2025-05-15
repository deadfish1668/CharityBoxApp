package com.charity.charitybox.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
public class FundraisingEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private CurrencyType currency;

    private BigDecimal balance = BigDecimal.ZERO;

    public FundraisingEvent() {}

    public FundraisingEvent(String name, CurrencyType currency) {
        this.name = name;
        this.currency = currency;
        this.balance = BigDecimal.ZERO;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public CurrencyType getCurrency() { return currency; }
    public void setCurrency(CurrencyType currency) { this.currency = currency; }

    public BigDecimal getBalance() { return balance; }
    public void setBalance(BigDecimal balance) { this.balance = balance; }

    public void addToBalance(BigDecimal amount) {
        this.balance = this.balance.add(amount);
    }
}

