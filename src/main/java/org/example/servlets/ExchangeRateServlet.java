package org.example.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.example.services.ExchangeRatesService;
import org.example.utils.BigDecimalUtil;

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

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getMethod();
        if(method.equalsIgnoreCase("PATCH")){
            doPatch(req, resp);
        }else {
            super.service(req, resp);
        }
    }

    public void doPatch(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ObjectWriter objectMapper = new ObjectMapper().writer().withDefaultPrettyPrinter();
        BufferedReader reader = req.getReader();
        StringBuilder body = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            body.append(line);
        }
        String pathInfo = req.getPathInfo();
        String rate = body.substring(body.lastIndexOf("=")+1);
        if(pathInfo == null || pathInfo.equals("/") || body.isEmpty() || !BigDecimalUtil.isValidRateValue(rate)){
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        BigDecimal newRate = new BigDecimal(rate);
        String codes = pathInfo.substring(pathInfo.lastIndexOf("/")+1);
        if(!exchangeRatesService.codeFormatIsValid(codes)){
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        resp.getWriter().write(objectMapper.writeValueAsString(exchangeRatesService.updateExchangeRate(codes, newRate)));

        String targetCurrencyCode = codes.substring(3,6).toUpperCase();
        String baseCurrencyCode = codes.substring(0,3).toUpperCase();
        String turnCodes = targetCurrencyCode+baseCurrencyCode;
        if(exchangeRatesService.validateExchangeRatesExists(turnCodes)){
            updateCrossRate(turnCodes,newRate);
        }
    }

    private void updateCrossRate(String turnCodes, BigDecimal newRate){
        BigDecimal updatedRate = BigDecimalUtil.divide(new BigDecimal(1), newRate);
        exchangeRatesService.updateExchangeRate(turnCodes, updatedRate);
    }


    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();
        if(pathInfo==null||pathInfo.equals("/")){
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        ObjectWriter objectMapper = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String codes = pathInfo.substring(pathInfo.lastIndexOf("/")+1);
        if(!exchangeRatesService.codeFormatIsValid(codes)){
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        resp.getWriter().write(objectMapper.writeValueAsString(exchangeRatesService.getExchangeRateResponse(codes)));
    }
}
