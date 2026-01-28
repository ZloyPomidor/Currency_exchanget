package org.example.entities;

import java.math.BigDecimal;

public class ExchangeRates {
    private Long id;
    private Currencies baseCurrency;
    private Currencies targetCurrency;
    private BigDecimal rate;


    public ExchangeRates() {}

    public ExchangeRates(Long id, Currencies baseCurrencyId, Currencies targetCurrencyId, BigDecimal rate) {
        this.id = id;
        this.baseCurrency = baseCurrencyId;
        this.targetCurrency = targetCurrencyId;
        this.rate = rate;

    }

    @Override
    public String toString() {
        return "ExchangeRates{" +
                "baseCurrencyId=" + baseCurrency +
                ", id=" + id +
                ", targetCurrencyId=" + targetCurrency +
                ", rate=" + rate +
                '}';
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
