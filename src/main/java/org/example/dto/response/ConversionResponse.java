package org.example.dto.response;

import org.example.models.Currencies;

import java.math.BigDecimal;

public record ConversionResponse(
                                 Currencies baseCurrency,
                                 Currencies targetCurrency,
                                 BigDecimal rate,
                                 BigDecimal amount,
                                 BigDecimal convertedAmount) {
    @Override
    public String toString() {
        return  String.format("{\n" +
                "\"baseCurrency\": %s, \n" +
                "\"targetCurrency\":  %s, \n" +
                "\"rate\":  %s, \n" +
                "\"amount\":  %s, \n" +
                "\"convertedAmount\":  %s \n" +
                "}", baseCurrency, targetCurrency, rate, amount, convertedAmount);
    }
}
