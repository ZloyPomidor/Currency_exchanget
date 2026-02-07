package org.example.servlets;

import org.example.models.Currencies;
import org.example.models.ExchangeRates;
import org.example.services.CurrenciesService;
import org.example.services.ExchangeRatesService;
import org.example.utils.BigDecimalUtil;
import org.example.utils.Writer;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@WebServlet("/exchangeRates")
public class ExchangeRatesServlet extends HttpServlet {

    private static final String PARAM_BASE_CURRENCY_CODE = "baseCurrencyCode";
    private static final String PARAM_TARGET_CURRENCY_CODE = "targetCurrencyCode";
    private static final String PARAM_RATE = "rate";
    private static final String INCORRECT_DATA_MESSAGE = "Не правильно введены данные. Пример: baseCurrency = 'USD', targetCurrency = 'RUB', rate = '1.09'";
    private static final String CURRENCY_HAS_BEEN_NOT_FOUND_MESSAGE = "Валюта не найдена";
    private static final String EXCHANGE_RATE_IS_ALREADY_EXISTS = "Валютная пара с таким кодом уже существует";
    private static final String DB_ERROR = "Ошибка базы данных";
    private final ExchangeRatesService exchangeRatesService = new ExchangeRatesService();
    private final CurrenciesService currenciesService = new CurrenciesService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        ExchangeRates reqExchangeRate = new ExchangeRates();

        if(req.getParameter(PARAM_BASE_CURRENCY_CODE) == null|| req.getParameter(PARAM_TARGET_CURRENCY_CODE) == null || !BigDecimalUtil.isValidRateValue(req.getParameter(PARAM_RATE))){
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, INCORRECT_DATA_MESSAGE);
            return;
        }

        Currencies baseCurrency = currenciesService.getCurrencyByCode(req.getParameter(PARAM_BASE_CURRENCY_CODE));
        Currencies targetCurrency = currenciesService.getCurrencyByCode(req.getParameter(PARAM_TARGET_CURRENCY_CODE));
        BigDecimal rate = new BigDecimal(req.getParameter(PARAM_RATE));

        if(!currenciesService.validateCurrencyExists(baseCurrency.getCode()) || !currenciesService.validateCurrencyExists(targetCurrency.getCode())){
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, CURRENCY_HAS_BEEN_NOT_FOUND_MESSAGE);
        }
        reqExchangeRate.setBaseCurrency(baseCurrency);
        reqExchangeRate.setTargetCurrency(targetCurrency);
        reqExchangeRate.setRate(rate);
        String codes = baseCurrency.getCode()+targetCurrency.getCode();

        if(exchangeRatesService.validateExchangeRatesExists(codes)){
            resp.sendError(HttpServletResponse.SC_CONFLICT,EXCHANGE_RATE_IS_ALREADY_EXISTS);
            return;
        }
        ExchangeRates exchangeRate = exchangeRatesService.saveExchangeRate(reqExchangeRate);
        if(exchangeRate == null){
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, DB_ERROR);
            return;
        }
        resp.setStatus(HttpServletResponse.SC_CREATED);
        resp.getWriter().write(Writer.write(exchangeRate));
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        List<ExchangeRates> allExchangeRates = exchangeRatesService.getAllExchangeRates();
        resp.getWriter().write(Writer.write(allExchangeRates));

    }
}
