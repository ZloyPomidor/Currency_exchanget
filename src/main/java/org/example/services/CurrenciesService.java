package org.example.services;

import org.example.dao.CurrenciesDao;
import org.example.entities.Currencies;
import org.example.exceptions.CurrencyIsAlreadyExistsException;
import org.example.exceptions.CurrencyNotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

public class CurrenciesService {

    CurrenciesDao currenciesDao = CurrenciesDao.getInstance();

    public Currencies save(Currencies currency){
        if(!isCodeUnique(currency.getCode())){
            throw new CurrencyIsAlreadyExistsException();
        }
        return currenciesDao.save(currency);
    }

    public List<Currencies> getAllCurrencies(){
        return currenciesDao.findAll();
    }
    private boolean isCodeUnique(String code) {
        if(codeFormatIsValid(code)){
            return !validateCurrencyExists(code);
        }
        return false;
    }

    public static boolean validateCurrencyExists(String code){
       if(codeFormatIsValid(code)){
           Optional<Currencies> maybeCurrency = CurrenciesDao.getInstance().findByCode(code);
           return maybeCurrency.isPresent();
       }
        return false;
    }

    private static boolean codeFormatIsValid(String code) {
        String regex = "[a-zA-Z]{3}";
        if(!code.isEmpty()){
            code = code.toUpperCase();
            return Pattern.compile(regex).matcher(code).matches();
        }
        return false;
    }

    public Currencies getCurrencyByCode(String code){
        return CurrenciesDao.getInstance().findByCode(code).orElseThrow(CurrencyNotFoundException::new);
    }

    public void update(Currencies currency){
        currenciesDao.update(currency);
    }
}
