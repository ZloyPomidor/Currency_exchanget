package org.example.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.example.entities.Currencies;
import org.example.services.CurrenciesService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@WebServlet("/currencies")
public class CurrenciesServlet extends HttpServlet {
    private final ObjectWriter objectMapper = new ObjectMapper().writer().withDefaultPrettyPrinter();
    private final CurrenciesService currenciesService = new CurrenciesService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Currencies reqCurrency = new Currencies();
        resp.setContentType("text/html");
        if(req.getParameter("code")== null|| req.getParameter("fullName") == null|| req.getParameter("sign") == null){
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        reqCurrency.setCode(req.getParameter("code"));
        reqCurrency.setFullName(req.getParameter("fullName"));
        reqCurrency.setSign(req.getParameter("sign"));
        if(CurrenciesService.validateCurrencyExists(reqCurrency.getCode())){
            resp.setStatus(HttpServletResponse.SC_CONFLICT);
            return;
        }
        Currencies savedCurrency = currenciesService.save(reqCurrency);
        resp.setStatus(HttpServletResponse.SC_CREATED);
        resp.getWriter().write(objectMapper.writeValueAsString(savedCurrency));
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        resp.getWriter().write(objectMapper.writeValueAsString(currenciesService.getAllCurrencies()));
    }
}