package org.example.models;

import org.example.services.ExchangeRatesService;

import java.math.BigDecimal;

public class ExchangeRates {
    private Long id;
    private Currencies baseCurrency;
    private Currencies targetCurrency;
    private BigDecimal rate;


    public ExchangeRates() {}

    public ExchangeRates(Long id, Currencies baseCurrency, Currencies targetCurrency, BigDecimal rate) {
        this.id = id;
        this.baseCurrency = baseCurrency;
        this.targetCurrency = targetCurrency;
        this.rate = rate;

    }

    @Override
    public String toString() {
        return String.format("{\n" +
                "\"id\": %d,\n" +
                "\"baseCurrency\": %s,\n" +
                "\"targetCurrency\": %s,\n" +
                "\"rate\": %f\n" +
                "}", id, baseCurrency, targetCurrency, rate);
    }

    public Currencies getBaseCurrency() {
        return baseCurrency;
    }

    public void setBaseCurrency(Currencies baseCurrency) {
        this.baseCurrency = baseCurrency;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public Currencies getTargetCurrency() {
        return targetCurrency;
    }

    public void setTargetCurrency(Currencies targetCurrency) {
        this.targetCurrency = targetCurrency;
    }
}
