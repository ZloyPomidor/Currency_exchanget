package org.example.services;

import org.example.dto.request.ConversionRequest;
import org.example.dto.response.ConversionResponse;
import org.example.entities.Currencies;
import org.example.exceptions.ExchangeRateNotFoundException;
import org.example.utils.BigDecimalUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class  ConversionService {
    CurrenciesService  currenciesService = new CurrenciesService();
    ExchangeRatesService exchangeRatesService = new ExchangeRatesService();
    String USD = "USD";

    public ConversionResponse getConversionResponse(ConversionRequest conversionRequest) {
        BigDecimal convertedRate= getConvertedRate(conversionRequest.fromCurrency(), conversionRequest.toCurrency());
        BigDecimal convertedAmount = convertedRate.multiply(conversionRequest.amount());
        Currencies fromCurrency = currenciesService.getCurrencyByCode(conversionRequest.fromCurrency());
        Currencies toCurrency = currenciesService.getCurrencyByCode(conversionRequest.toCurrency());
        return new ConversionResponse(
                fromCurrency,
                toCurrency,
                convertedRate,
                conversionRequest.amount(),
                convertedAmount);
    }

    public static void main(String[] args) {
        ConversionService  conversionService = new ConversionService();
        System.out.printf(conversionService.getConversionRate("RUB","SAR")+"");
    }

    public BigDecimal getConversionRate(String from, String to) {
        List<String> defultCombinations = getCodesVariation(from,to);
        if(!defultCombinations.isEmpty()){
           return getRateTo(from,to,defultCombinations);
        }
        List<String> toCombinations = getCodesVariation(USD,to);
        List<String> fromCombinations = getCodesVariation(USD,from);
        if(!toCombinations.isEmpty() && !fromCombinations.isEmpty()){
            BigDecimal toRate = getRateTo(USD, to,toCombinations);
            BigDecimal fromRate = getRateTo(USD, from, fromCombinations);
            return BigDecimalUtil.divide(toRate, fromRate);
        }
        throw new ExchangeRateNotFoundException();//гений ты ты гений!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    }

    private BigDecimal getRateTo(String from, String to,List<String> codes){
        BigDecimal devisor = new BigDecimal("1");
        String defultExchangeCode = from+to;
        String reversExchangeCode = to+from;
        if(!codes.isEmpty()){
            if(exchangeRatesService.validateExchangeRatesExists(defultExchangeCode)){
                return exchangeRatesService.getExchangeRatesByCodes(defultExchangeCode).getRate();
            }
            else {
                BigDecimal rate = exchangeRatesService.getExchangeRatesByCodes(reversExchangeCode).getRate();
                return BigDecimalUtil.divide(devisor,rate);
            }
        }
        throw new ExchangeRateNotFoundException();
    }

    public BigDecimal getConvertedRate(String from, String to){
        BigDecimal devisor = new BigDecimal("1");
        if(isExistsCodeCombination(from, to)){
           String code = getValidCodeCombination(from, to);
            if(!code.equals(from+to)){
                BigDecimal rate = exchangeRatesService.getExchangeRatesByCodes(code).getRate();
                return BigDecimalUtil.divide(devisor, rate);
            }else
                return exchangeRatesService.getExchangeRatesByCodes(code).getRate();
        }else if ( isExistsCodeCombination(from, USD)&&
                isExistsCodeCombination(to, USD)){
            String baseCode = getValidCodeCombination(from, USD);
            String targetCode = getValidCodeCombination(to, USD);
            BigDecimal baseRate = exchangeRatesService.getExchangeRatesByCodes(baseCode).getRate();
            BigDecimal targetRate = exchangeRatesService.getExchangeRatesByCodes(targetCode).getRate();
            String fromUSD = from+ USD;
            String toUSD = to+ USD;

            if(baseCode.equals(fromUSD) && targetCode.equals(toUSD)){
                return BigDecimalUtil.divide(baseRate, targetRate);

            }else if(!baseCode.equals(fromUSD) && !targetCode.equals(toUSD)){
                BigDecimal resultConvertBaseRate =  BigDecimalUtil.divide(devisor, baseRate);
                BigDecimal resultConvertTargetRate = BigDecimalUtil.divide(devisor, targetRate);
                return BigDecimalUtil.divide(resultConvertBaseRate, resultConvertTargetRate);

            }else if(baseCode.equals(fromUSD)){
                BigDecimal resultConvertTargetRate = BigDecimalUtil.divide(devisor, targetRate);
                return BigDecimalUtil.divide(baseRate, resultConvertTargetRate);
            }else {
                BigDecimal resultConvertBaseRate = BigDecimalUtil.divide(devisor, baseRate );
                return BigDecimalUtil.divide(resultConvertBaseRate, targetRate);
            }
        }else {
            throw new RuntimeException("не  нашлось");
        }
    }



    private String getValidCodeCombination(String from, String to){
        return getCodesVariation(from,to).getFirst();
    }

    private boolean isExistsCodeCombination(String from, String to){
        return !getCodesVariation(from,to).isEmpty();
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
