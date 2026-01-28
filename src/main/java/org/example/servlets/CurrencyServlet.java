package org.example.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.example.services.CurrenciesService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
@WebServlet("/currency/*")
public class CurrencyServlet extends HttpServlet {

    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();
        if(pathInfo==null||pathInfo.equals("/")){
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        ObjectWriter objectMapper = new ObjectMapper().writer().withDefaultPrettyPrinter();
        CurrenciesService currenciesService = new CurrenciesService();
        String currencyCode = pathInfo.substring(pathInfo.lastIndexOf("/")+1);
        if(CurrenciesService.validateCurrencyExists(currencyCode)){
            resp.getWriter().write(objectMapper.writeValueAsString(currenciesService.getCurrencyByCode(currencyCode)));
        }else {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }

    }
}
