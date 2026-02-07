package org.example.services;

import org.example.dao.ExchangeRatesDao;
import org.example.models.ExchangeRates;
import org.example.exceptions.ExchangeRateNotFoundException;
import org.example.utils.BigDecimalUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ExchangeRatesService {
    private final CurrenciesService currenciesService = new CurrenciesService();
    private final ExchangeRatesDao exchangeRatesDao = ExchangeRatesDao.getInstance();

    public List<ExchangeRates> getAllExchangeRates(){
        return exchangeRatesDao.findAll();
    }

    public ExchangeRates getExchangeRateByCodes(String code){
       return maybeGetExchangeRatesByCodes(code).orElseThrow(ExchangeRateNotFoundException::new);
    }

    public boolean validateExchangeRatesExists(String code) {
        Optional<ExchangeRates> maybeExchangeRate = maybeGetExchangeRatesByCodes(code);
        return maybeExchangeRate.isPresent();
    }

    public ExchangeRates saveExchangeRate(ExchangeRates exchangeRates){
        return exchangeRatesDao.save(exchangeRates);
    }

    public ExchangeRates updateExchangeRate(String codes, BigDecimal rate){

        ExchangeRates exchangeRatesByCodes = getExchangeRateByCodes(codes);
        exchangeRatesByCodes.setRate(rate);
        ExchangeRatesDao.getInstance().update(exchangeRatesByCodes);

        return getExchangeRateByCodes(codes);
    }

    public void updateCrossRate(String codes, BigDecimal rate){
        String turnedCodes = getTurnCode(codes);

        if(validateExchangeRatesExists(turnedCodes)){

            BigDecimal crossRate = BigDecimalUtil.divideWithDefaultDividend(rate);
            updateExchangeRate(turnedCodes, crossRate);

        }
    }


    public boolean codesIsValid(String code){
        if (code.length() == 6){
            for (String part : getCodeParts(code)) {
                if (!currenciesService.validateCurrencyExists(part)) {
                    return false;
                }
            }
        }
        return true;
    }
    private List<String> getCodeParts(String code){
        return List.of(code.substring(0,3), code.substring(3,6));
    }

    private Optional<ExchangeRates> maybeGetExchangeRatesByCodes(String codes){
        List<Long> currencyPairIds = new ArrayList<>();
        if (codesIsValid(codes)){
            List<String> codeParts = getCodeParts(codes);
            for (String code : codeParts) {
                currencyPairIds.add(currenciesService.getCurrencyByCode(code).getId());
            }
        }
        return exchangeRatesDao.findByBaseAndTargetId(currencyPairIds.getFirst(), currencyPairIds.getLast());
    }

    private String getTurnCode(String code){
        List<String> codeParts = getCodeParts(code);
        return codeParts.getLast()+codeParts.getFirst();
    }
}
