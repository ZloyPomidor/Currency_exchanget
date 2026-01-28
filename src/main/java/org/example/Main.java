package org.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.example.dao.ExchangeRatesDao;
import org.example.entities.ExchangeRates;
import org.example.services.ConversionService;
import org.example.utils.ConnectionManager;
import org.postgresql.Driver;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws SQLException, JsonProcessingException {
       Class<Driver> driverClass = Driver.class;

        ConversionService  conversionService = new ConversionService();
        System.out.printf(conversionService.getConversionRate("EUR","RUB")+"");



//       String sql = """
//               CREATE TABLE gameds();
//               """;
//        try (Connection connection = ConnectionManager.open();
//             var statement = connection.createStatement()) {
//            System.out.println(connection.getTransactionIsolation());
//            var executeResult = statement.execute(sql);
//            System.out.printf(executeResult+"");
//        }

    }
}