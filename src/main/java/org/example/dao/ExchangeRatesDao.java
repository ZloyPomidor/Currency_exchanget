package org.example.dao;

import org.example.entities.ExchangeRates;
import org.example.utils.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ExchangeRatesDao implements Dao<Long, ExchangeRates> {

    private static final ExchangeRatesDao INSTANCE = new ExchangeRatesDao();

    public static final String FIND_ALL_SQL = """
            SELECT id,
                   base_currency_id,
                   target_Currency_Id,
                   rate
            FROM  currency_exchange.public.exchange_rates
            """;

    public static final String UPDATE_SQL = """
            UPDATE currency_exchange.public.exchange_rates
            SET base_currency_id = ?,
                target_Currency_Id = ?,
                rate = ?
            WHERE id = ?
            """;

    private static final String DELETE_SQL = """
            DELETE FROM currency_exchange.public.exchange_rates
            WHERE id = ?
            """;

    private static final String SAVE_SQL = """
            INSERT INTO currency_exchange.public.exchange_rates(base_currency_id, target_currency_id, rate)
            VALUES (?, ?, ?)
            """;
    public static final String FIND_BY_ID_SQL = FIND_ALL_SQL + """
            WHERE id = ?
            """;
    public static final String FIND_BY_BASE_AND_TARGET_ID_SQL = FIND_ALL_SQL + """
            WHERE base_currency_id = ? and  target_currency_id = ?
            """;

    private final CurrenciesDao currenciesDao = CurrenciesDao.getInstance();

    private ExchangeRatesDao(){}

    public static ExchangeRatesDao getInstance(){
        return INSTANCE;
    }

    @Override
    public boolean delete(Long id) {
        try (Connection connection = ConnectionManager.open();
             var prepareStatement = connection.prepareStatement(DELETE_SQL)) {
            prepareStatement.setLong(1, id);

            return prepareStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(ExchangeRates exchangeRate) {
        try (Connection connection = ConnectionManager.open();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_SQL)) {
             preparedStatement.setLong(1, exchangeRate.getBaseCurrency().getId());
             preparedStatement.setLong(2, exchangeRate.getTargetCurrency().getId());
             preparedStatement.setBigDecimal(3, exchangeRate.getRate());
             preparedStatement.setLong(4, exchangeRate.getId());
             preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ExchangeRates save(ExchangeRates exchangeRate) {
        try (Connection connection = ConnectionManager.open();
             PreparedStatement preparedStatement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setLong(1, exchangeRate.getBaseCurrency().getId());
            preparedStatement.setLong(2, exchangeRate.getTargetCurrency().getId());
            preparedStatement.setBigDecimal(3, exchangeRate.getRate());

            preparedStatement.executeUpdate();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();

            if(generatedKeys.next()){
                exchangeRate.setId(generatedKeys.getLong("id"));
            }

            return exchangeRate;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<ExchangeRates> findById(Long id) {
        try (Connection connection = ConnectionManager.open();) {
            return findById(id,connection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }



    public Optional<ExchangeRates> findById(Long id, Connection connection){
        try (PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID_SQL)) {
            preparedStatement.setLong(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            ExchangeRates result = null;
            if(resultSet.next()){
                result = buildExchangeRates(resultSet);
            }
            return Optional.ofNullable(result);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<ExchangeRates> findByBaseAndTargetId(Long baseCurrencyId, Long targetCurrencyId) {
        try (Connection connection = ConnectionManager.open();) {
            return findByBaseAndTargetId(baseCurrencyId, targetCurrencyId, connection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }



    public Optional<ExchangeRates> findByBaseAndTargetId(Long baseCurrencyId, Long targetCurrencyId, Connection connection){
        try (PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_BASE_AND_TARGET_ID_SQL)) {
            preparedStatement.setLong(1, baseCurrencyId);
            preparedStatement.setLong(2, targetCurrencyId);

            ResultSet resultSet = preparedStatement.executeQuery();
            ExchangeRates result = null;
            if(resultSet.next()){
                result = buildExchangeRates(resultSet);
            }
            return Optional.ofNullable(result);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<ExchangeRates> findAll(){
        try (Connection connection = ConnectionManager.open();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_SQL)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            List<ExchangeRates> resultExchangeRates = new ArrayList<>();
            while (resultSet.next()){
                resultExchangeRates.add(buildExchangeRates(resultSet));
            }
            return resultExchangeRates;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private ExchangeRates buildExchangeRates(ResultSet resultSet) throws SQLException {
        return new ExchangeRates(resultSet.getLong("id"),
                currenciesDao.findById(resultSet.getLong("base_currency_id"), resultSet.getStatement().getConnection()).orElse(null),
                currenciesDao.findById(resultSet.getLong("target_currency_id"),  resultSet.getStatement().getConnection()).orElse(null),
                resultSet.getBigDecimal("rate"));
    }


}
