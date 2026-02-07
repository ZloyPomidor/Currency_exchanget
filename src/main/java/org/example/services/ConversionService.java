package org.example.services;

import org.example.dto.request.ConversionRequest;
import org.example.dto.response.ConversionResponse;
import org.example.models.Currencies;
import org.example.exceptions.ConversionHasBeenFailedException;
import org.example.exceptions.ExchangeRateNotFoundException;
import org.example.utils.BigDecimalUtil;

import javax.swing.text.html.Option;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class  ConversionService {
    private final CurrenciesService  currenciesService;
    private final ExchangeRatesService exchangeRatesService;
    private static final String USD = "USD";

    public ConversionService(CurrenciesService currenciesService, ExchangeRatesService exchangeRatesService) {
        this.currenciesService = currenciesService;
        this.exchangeRatesService = exchangeRatesService;
    }

    public Optional<ConversionResponse> getConversionResponse(ConversionRequest conversionRequest) {
        Optional<BigDecimal> convertedRate = getConversionRate(conversionRequest.fromCurrency(), conversionRequest.toCurrency());
        if (convertedRate.isEmpty()) {
            return Optional.empty();
        }
        BigDecimal convertedAmount = convertedRate.get().multiply(conversionRequest.amount())
                .setScale(2, RoundingMode.HALF_UP);
        Currencies fromCurrency = currenciesService.getCurrencyByCode(conversionRequest.fromCurrency());
        Currencies toCurrency = currenciesService.getCurrencyByCode(conversionRequest.toCurrency());
        return Optional.of(new ConversionResponse(
                fromCurrency,
                toCurrency,
                convertedRate.get(),
                conversionRequest.amount(),
                convertedAmount));
    }


    public Optional<BigDecimal> getConversionRate(String from, String to) {
        List<String> defultCombinations = getCodesVariation(from,to);

        if(!defultCombinations.isEmpty()){
           return Optional.ofNullable(getRateTo(from, to, defultCombinations));
        }

        List<String> toCombinations = getCodesVariation(USD,to);
        List<String> fromCombinations = getCodesVariation(USD,from);

        if(!toCombinations.isEmpty() && !fromCombinations.isEmpty()){
            BigDecimal toRate = getRateTo(USD, to,toCombinations);
            BigDecimal fromRate = getRateTo(USD, from, fromCombinations);
            return Optional.of(BigDecimalUtil.divide(toRate, fromRate));
        }
        return Optional.empty();
    }

    private BigDecimal getRateTo(String from, String to,List<String> codes){
        String defultExchangeCode = from+to;
        String turnedExchangeCode = to+from;

        if(!codes.isEmpty()){
            if(exchangeRatesService.validateExchangeRatesExists(defultExchangeCode)){
                return exchangeRatesService.getExchangeRateByCodes(defultExchangeCode).getRate();
            }
            else {
                BigDecimal rate = exchangeRatesService.getExchangeRateByCodes(turnedExchangeCode).getRate();
                return BigDecimalUtil.divideWithDefaultDividend(rate);
            }
        }
        throw new ExchangeRateNotFoundException();
    }

    private List<String> getCodesVariation(String from, String to){
        List<String> combinationsCode =  List.of(from+to, to+from);
        List<String> resultCodesCombination = new ArrayList<>();
        for(String code : combinationsCode){
            if(exchangeRatesService.validateExchangeRatesExists(code)){
                resultCodesCombination.add(code);
            }
        }
        return resultCodesCombination;
    }
}
