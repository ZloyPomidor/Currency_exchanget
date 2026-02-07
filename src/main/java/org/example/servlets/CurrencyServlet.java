package org.example.servlets;


import org.example.models.Currencies;
import org.example.services.CurrenciesService;
import org.example.utils.Writer;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/currency/*")
public class CurrencyServlet extends HttpServlet {

    private final CurrenciesService currenciesService = new CurrenciesService();
    private static final String DB_ERROR = "Ошибка базы данных. Валюта отсутсвует";
    private static final String INCORRECT_DATA_MESSAGE = "Код валюты отсутствует в адресе.";
    private static final String CURRENCY_HAS_BEEN_NOT_FOUND_MESSAGE = "Валюта не найдена.";


    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();
        if(pathInfo==null||pathInfo.equals("/")){
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, INCORRECT_DATA_MESSAGE);
            return;
        }

        String currencyCode = req.getPathInfo().replaceFirst("/", "").toUpperCase();

        if(!currenciesService.validateCurrencyExists(currencyCode)){
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, CURRENCY_HAS_BEEN_NOT_FOUND_MESSAGE);
            return;
        }

        Currencies currency = currenciesService.getCurrencyByCode(currencyCode);

        if(currency == null){
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, DB_ERROR);
            return;
        }

        resp.getWriter().write(Writer.write(currency));
    }
}
