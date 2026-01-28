package org.example.dto.response;

import org.example.entities.Currencies;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;

public record ConversionResponse(
                                 Currencies fromCurrency,
                                 Currencies toCurrency,
                                 BigDecimal convertedRate,
                                 BigDecimal originalAmount,
                                 BigDecimal convertedAmount) {
}
