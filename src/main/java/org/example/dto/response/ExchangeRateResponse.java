package org.example.dto.response;

import java.math.BigDecimal;
import java.util.function.BiConsumer;

public record ExchangeRateResponse(String baseCurrencyCode,
                                   String baseCurrencyName,
                                   String bseCurrencySign,
                                   String targetCurrencyCode,
                                   String targetCurrencyName,
                                   String targetCurrencySign,
                                   BigDecimal rate) {
}
