package com.charity.charitybox.util;

import com.charity.charitybox.model.CurrencyType;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

public class CurrencyConverter {

    private static final Map<CurrencyType, Map<CurrencyType, BigDecimal>> rates = Map.of(
            CurrencyType.EUR, Map.of(
                    CurrencyType.EUR, BigDecimal.valueOf(1),
                    CurrencyType.USD, BigDecimal.valueOf(1.1),
                    CurrencyType.GBP, BigDecimal.valueOf(0.85)
            ),
            CurrencyType.USD, Map.of(
                    CurrencyType.EUR, BigDecimal.valueOf(0.91),
                    CurrencyType.USD, BigDecimal.valueOf(1),
                    CurrencyType.GBP, BigDecimal.valueOf(0.77)
            ),
            CurrencyType.GBP, Map.of(
                    CurrencyType.EUR, BigDecimal.valueOf(1.18),
                    CurrencyType.USD, BigDecimal.valueOf(1.3),
                    CurrencyType.GBP, BigDecimal.valueOf(1)
            )
    );

    public static BigDecimal convert(CurrencyType from, CurrencyType to, BigDecimal amount) {
        BigDecimal rate = rates.get(from).get(to);
        return amount.multiply(rate).setScale(2, RoundingMode.HALF_UP);
    }
}
