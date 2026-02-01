package org.example.dto.response;

import org.example.entities.Currencies;

import java.math.BigDecimal;

public record ConversionResponse(
                                 Currencies baseCurrency,
                                 Currencies targetCurrency,
                                 BigDecimal rate,
                                 BigDecimal amount,
                                 BigDecimal convertedAmount) {
    @Override
    public String toString() {
        return  "{\"baseCurrency\":" + baseCurrency + "," + "\n" +
                "\"targetCurrency\":" + targetCurrency + "," + "\n" +
                "\"rate\": " + rate + "," + "\n" +
                "\"amount\": " + amount + "," + "\n" +
                "\"convertedAmount\": " + convertedAmount + "," + "\n" +
                "}";
    }

}
