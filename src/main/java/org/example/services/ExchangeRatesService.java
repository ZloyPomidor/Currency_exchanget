package org.example.services;

import org.example.dao.ExchangeRatesDao;
import org.example.dto.response.ExchangeRateResponse;
import org.example.entities.Currencies;
import org.example.entities.ExchangeRates;
import org.example.exceptions.ExchangeRateNotFoundException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ExchangeRatesService {
    CurrenciesService currenciesService = new CurrenciesService();
    ExchangeRatesDao exchangeRatesDao = ExchangeRatesDao.getInstance();

    public List<ExchangeRates> getAllExchangeRates(){
        return exchangeRatesDao.findAll();
    }

    public List<ExchangeRates> getExchangesWith(String currencyCode){
        return exchangeRatesDao.findAll().stream().filter(e->e.getBaseCurrency().getCode().equals(currencyCode)
                ||e.getTargetCurrency().getCode().equals(currencyCode)).toList();
    }

    public ExchangeRateResponse getExchangeRateResponse(String codes){
        ExchangeRates result = getExchangeRatesByCodes(codes);
        Currencies baseCurrency = currenciesService.getCurrencyByCode(result.getBaseCurrency().getCode());
        Currencies targetCurrency = currenciesService.getCurrencyByCode(result.getTargetCurrency().getCode());

        return new ExchangeRateResponse(baseCurrency.getCode(), baseCurrency.getFullName(), baseCurrency.getSign(),
                targetCurrency.getCode(), targetCurrency.getFullName(), targetCurrency.getSign(), result.getRate());
    }

    public ExchangeRates getExchangeRatesByCodes(String code){
       return maybeGetExchangeRatesByCodes(code).orElseThrow(ExchangeRateNotFoundException::new);
    }

    private Optional<ExchangeRates> maybeGetExchangeRatesByCodes(String code){
        List<Long> currencyPairIds = new ArrayList<>();
        if (codeFormatIsValid(code)){
            List<String> codeParts = getCodeParts(code);
            for (String part : codeParts) {
                currencyPairIds.add(currenciesService.getCurrencyByCode(part).getId());
            }
        }
        return exchangeRatesDao.findByBaseAndTargetId(currencyPairIds.getFirst(), currencyPairIds.getLast());
    }

    public boolean validateExchangeRatesExists(String code) {
        Optional<ExchangeRates> maybeExchangeRates = maybeGetExchangeRatesByCodes(code);
        return maybeExchangeRates.isPresent();
    }

    public void saveExchangeRate(ExchangeRates exchangeRates){
        exchangeRatesDao.save(exchangeRates);
    }

    public ExchangeRateResponse updateExchangeRate(String codes, BigDecimal rate){
        ExchangeRates exchangeRatesByCodes = getExchangeRatesByCodes(codes);
        exchangeRatesByCodes.setRate(rate);
        ExchangeRatesDao.getInstance().update(exchangeRatesByCodes);
        return getExchangeRateResponse(codes);
    }

    public boolean codeFormatIsValid(String code){
        if (code.length() == 6){
            for (String part : getCodeParts(code)) {
                if(!CurrenciesService.validateCurrencyExists(part)) {
                    return false;
                }
            }
        }
        return true;
    }

    private List<String> getCodeParts(String code){
        return List.of(code.substring(0,3), code.substring(3,6));
    }

}
