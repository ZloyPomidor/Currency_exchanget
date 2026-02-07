package org.example.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.example.models.ExchangeRates;
import org.example.services.ExchangeRatesService;
import org.example.utils.BigDecimalUtil;
import org.example.utils.Writer;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;

@WebServlet("/exchangeRate/*")
public class ExchangeRateServlet extends HttpServlet {

    private final ExchangeRatesService exchangeRatesService = new ExchangeRatesService();
    private static final String INCORRECT_DATA_MESSAGE = "Неправильно введен запрос.";
    private static final String EXCHANGE_RATE_HAS_BEEN_NOT_FOUND_MESSAGE = "Валютная пара не найдена";
    private static final String DB_ERROR = "Ошибка базы данных. Обновление провалено";


    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getMethod();
        if(method.equalsIgnoreCase("PATCH")){
            doPatch(req, resp);
        }else {
            super.service(req, resp);
        }
    }

    public void doPatch(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();
        String rate = getRate(req);

        if(pathInfo == null || pathInfo.equals("/") || !BigDecimalUtil.isValidRateValue(rate)){
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, INCORRECT_DATA_MESSAGE);
            return;
        }

        BigDecimal newRate = new BigDecimal(rate);
        String codes = req.getPathInfo().replaceFirst("/", "").toUpperCase();

        if(!exchangeRatesService.validateExchangeRatesExists(codes)){
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, EXCHANGE_RATE_HAS_BEEN_NOT_FOUND_MESSAGE);
            return;
        }

        ExchangeRates exchangeRate = exchangeRatesService.updateExchangeRate(codes, newRate);

        if (exchangeRate == null) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, DB_ERROR);
            return;
        }

        resp.getWriter().write(Writer.write(exchangeRate));

        exchangeRatesService.updateCrossRate(codes,newRate);
    }


    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();

        if(pathInfo==null||pathInfo.equals("/")){
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, INCORRECT_DATA_MESSAGE);
            return;
        }

        String codes = req.getPathInfo().replaceFirst("/", "").toUpperCase();

        if(!exchangeRatesService.codesIsValid(codes)){
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, EXCHANGE_RATE_HAS_BEEN_NOT_FOUND_MESSAGE);
            return;
        }

        resp.getWriter().write(Writer.write(exchangeRatesService.getExchangeRateByCodes(codes)));
    }


    private String getRate(HttpServletRequest req) throws IOException {
        BufferedReader reader = req.getReader();
        StringBuilder body = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            body.append(line);
        }
        return body.substring(body.lastIndexOf("=")+1);
    }

}
