package org.example.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.example.models.Currencies;
import org.example.services.CurrenciesService;
import org.example.utils.Writer;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@WebServlet("/currencies")
public class CurrenciesServlet extends HttpServlet {
    private final CurrenciesService currenciesService = new CurrenciesService();
    private final static String CURRENCY_EXISTS_MESSAGE= "Эта валюта уже существует";
    private static final String INCORRECT_DATA_MESSAGE = "Данные были введены неверно. Пример code = 'USD', name = 'US Dollar', sign = '$'";
    private static final String PARAM_CODE = "code";
    private static final String PARAM_NAME = "name";
    private static final String PARAM_SIGN = "sign";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Currencies reqCurrency = new Currencies();
        if(req.getParameter(PARAM_CODE)== null|| req.getParameter(PARAM_NAME) == null|| req.getParameter(PARAM_SIGN) == null){
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, INCORRECT_DATA_MESSAGE);
            return;
        }
        reqCurrency.setCode(req.getParameter(PARAM_CODE));
        reqCurrency.setName(req.getParameter(PARAM_NAME));
        reqCurrency.setSign(req.getParameter(PARAM_SIGN));
        if(currenciesService.validateCurrencyExists(reqCurrency.getCode())){
            resp.sendError(HttpServletResponse.SC_CONFLICT, CURRENCY_EXISTS_MESSAGE);
            return;
        }
        Currencies savedCurrency = currenciesService.save(reqCurrency);
        resp.setStatus(HttpServletResponse.SC_CREATED);
        resp.getWriter().write(Writer.write(savedCurrency));
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.getWriter().write(Writer.write(currenciesService.getAllCurrencies()));
    }
}