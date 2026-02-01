package org.example.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.example.entities.Currencies;
import org.example.entities.ExchangeRates;
import org.example.services.CurrenciesService;
import org.example.services.ExchangeRatesService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;

@WebServlet("/exchangeRates")
public class ExchangeRatesServlet extends HttpServlet {
    ObjectWriter objectMapper = new ObjectMapper().writer().withDefaultPrettyPrinter();
    ExchangeRatesService exchangeRatesService = new ExchangeRatesService();
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        ExchangeRates reqExchangeRates = new ExchangeRates();
        CurrenciesService currenciesService = new CurrenciesService();
        resp.setContentType("text/html");
        if(req.getParameter("baseCurrencyCode") == null|| req.getParameter("targetCurrencyCode") == null || req.getParameter("rate") == null){
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        Currencies baseCurrency = currenciesService.getCurrencyByCode(req.getParameter("baseCurrencyCode"));
        Currencies targetCurrency = currenciesService.getCurrencyByCode(req.getParameter("targetCurrencyCode"));
        BigDecimal rate = new BigDecimal(req.getParameter("rate"));

        reqExchangeRates.setBaseCurrency(baseCurrency);
        reqExchangeRates.setTargetCurrency(targetCurrency);
        reqExchangeRates.setRate(rate);
        String codes = baseCurrency.getCode()+targetCurrency.getCode();
        if(exchangeRatesService.validateExchangeRatesExists(codes)){
            resp.setStatus(HttpServletResponse.SC_CONFLICT);
            return;
        }
        exchangeRatesService.saveExchangeRate(reqExchangeRates);
        resp.setStatus(HttpServletResponse.SC_CREATED);
        resp.getWriter().write(objectMapper.writeValueAsString(exchangeRatesService.getExchangeRateResponse(codes)));
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.getWriter().write(objectMapper.writeValueAsString(exchangeRatesService.getAllExchangeRates()));
    }
}
