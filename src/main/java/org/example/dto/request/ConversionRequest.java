package org.example.dto.request;

import java.math.BigDecimal;

public record ConversionRequest(
        String fromCurrency,
        String toCurrency,
        BigDecimal amount){
}
