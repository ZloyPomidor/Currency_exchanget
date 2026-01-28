package org.example.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.example.dto.request.ConversionRequest;
import org.example.dto.response.ConversionResponse;
import org.example.services.ConversionService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;

@WebServlet("/exchange")
public class ExchangeServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws  IOException {
        ConversionService conversionService = new ConversionService();
        ObjectWriter objectMapper = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String from =  req.getParameter("from");
        String to =  req.getParameter("to");
        String amount = req.getParameter("amount");
        if(from == null || to == null || amount == null) {// переделать валидацию
            resp.setStatus(400);
            return;
        }
        ConversionRequest conversionRequest = new ConversionRequest(from, to, new BigDecimal(amount));
        ConversionResponse conversionResponse = conversionService.getConversionResponse(conversionRequest);
        resp.getWriter().write(objectMapper.writeValueAsString(conversionResponse));
    }
}
