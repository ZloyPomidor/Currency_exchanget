package org.example.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.example.dao.ExchangeRatesDao;
import org.example.entities.ExchangeRates;
import org.example.services.CurrenciesService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/first")
public class FirstServlets extends HttpServlet {

    @Override
    public void init() throws ServletException {
        super.init();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ObjectWriter objectMapper = new ObjectMapper().writer().withDefaultPrettyPrinter();

        CurrenciesService currenciesService = new CurrenciesService();
        resp.setContentType("text/html");
        resp.getWriter().write(objectMapper.writeValueAsString(currenciesService.getAllCurrencies()));

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
//      List<ExchangeRates> exchangeRatesDao = ExchangeRatesDao.getInstance().findAll();
//        StringBuilder respParam = new StringBuilder();
//        try( PrintWriter w = resp.getWriter()){
//            for(ExchangeRates e : exchangeRatesDao){
//                respParam.append(e.toString());
//            }
//            w.write(String.valueOf(respParam));
//        }
        ObjectWriter objectMapper = new ObjectMapper().writer().withDefaultPrettyPrinter();

        List<ExchangeRates> exchangeRatesDao = ExchangeRatesDao.getInstance().findAll();
        String json = objectMapper.writeValueAsString(exchangeRatesDao);
        try( PrintWriter w = resp.getWriter()){
            w.write(String.valueOf(json));
        }

    }

    @Override
    public void destroy() {
        super.destroy();
    }

}
