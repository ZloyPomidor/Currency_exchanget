package org.example.servlets;


import org.example.dto.request.ConversionRequest;
import org.example.dto.response.ConversionResponse;
import org.example.services.ConversionService;
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
import java.util.Optional;

@WebServlet("/exchange")
public class ExchangeServlet extends HttpServlet {
    private final ExchangeRatesService exchangeRatesService = new ExchangeRatesService();
    private final CurrenciesService currenciesService = new CurrenciesService();
    private static final String INCORRECT_DATA_MESSAGE = "Неправильно введен запрос.";
    private static final String NOT_FOUND_DATA_MESSAGE = "Конвертация не удалась. Курс конвертации не найден";
    private static final String PARAM_FROM = "from";
    private static final String PARAM_TO = "to";
    private static final String PARAM_AMOUNT = "amount";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws  IOException {
        ConversionService conversionService = new ConversionService(currenciesService,exchangeRatesService);
        String from = req.getParameter(PARAM_FROM);
        String to = req.getParameter(PARAM_TO);
        String amount = req.getParameter(PARAM_AMOUNT);
        if(!currenciesService.codeFormatIsValid(from) || !currenciesService.codeFormatIsValid(to) || !BigDecimalUtil.isValidRateValue(amount)) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, INCORRECT_DATA_MESSAGE);
            return;
        }
        ConversionRequest conversionRequest = new ConversionRequest(from, to, new BigDecimal(amount));
        Optional<ConversionResponse> conversionResponse = conversionService.getConversionResponse(conversionRequest);
        if(conversionResponse.isEmpty()) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, NOT_FOUND_DATA_MESSAGE);
        }
        resp.getWriter().write(Writer.write(conversionResponse.get()));
    }
}
