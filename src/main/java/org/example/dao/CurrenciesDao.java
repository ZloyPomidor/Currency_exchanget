package org.example.dao;

import org.example.entities.Currencies;
import org.example.exceptions.CurrencyNotFoundException;
import org.example.utils.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.joining;

public class CurrenciesDao implements Dao<Long, Currencies> {

    private static final CurrenciesDao INSTANCE = new CurrenciesDao();
    private static final String DELETE_SQL = """
            DELETE FROM currency_exchange.public.currencies
            WHERE id = ?
            """;
    private static final String SAVE_SQL = """
            INSERT INTO currency_exchange.public.currencies(code, name, sign)
            VALUES (?, ?, ?)
            """;
    public static final String UPDATE_SQL = """
            UPDATE currency_exchange.public.currencies
            SET code = ?,
            name = ?,
            sign = ?
            WHERE id = ?
            """;
    public static final String FIND_ALL_SQL = """
            SELECT id,
                   code,
                   name,
                   sign
            FROM  currency_exchange.public.currencies
            """;
    public static final String FIND_BY_ID_SQL = FIND_ALL_SQL + """
            WHERE id = ?
            """;
    public static final String FIND_BY_CODE_SQL = FIND_ALL_SQL + """
            WHERE code = ?
            """;


    private CurrenciesDao(){
    }

    public static CurrenciesDao getInstance(){
        return INSTANCE;
    }

    public List<Currencies> findAll(){
        try (Connection connection = ConnectionManager.open();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_SQL)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Currencies> currencies = new ArrayList<>();
            while (resultSet.next()){
                currencies.add(buildCurrencies(resultSet));
            }
            return currencies;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<Currencies> findById(Long id){
        try (Connection connection = ConnectionManager.open();){
            return findById(id, connection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<Currencies> findByCode(String code){
        try (Connection connection = ConnectionManager.open();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_CODE_SQL)) {
            preparedStatement.setString(1, code);

            ResultSet resultSet = preparedStatement.executeQuery();
            Currencies currencies = null;
            if(resultSet.next()){
                currencies = buildCurrencies(resultSet);
            }
            return Optional.ofNullable(currencies);
        } catch (SQLException e) {
            throw new CurrencyNotFoundException(e.getMessage());
        }

    }

    public Optional<Currencies> findById(Long id, Connection connection){
        try (PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID_SQL)) {
            preparedStatement.setLong(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            Currencies currencies = null;
            if(resultSet.next()){
                currencies = buildCurrencies(resultSet);
            }
            return Optional.ofNullable(currencies);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }


    public void update(Currencies currencies){
        try (Connection connection = ConnectionManager.open();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_SQL)) {
            preparedStatement.setString(1, currencies.getCode());
            preparedStatement.setString(2, currencies.getName());
            preparedStatement.setString(3, currencies.getSign());
            preparedStatement.setLong(4, currencies.getId());

            preparedStatement.executeUpdate();
            
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Currencies save(Currencies currencies){
        try (Connection connection = ConnectionManager.open();
        var prepareStatement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)) {
            prepareStatement.setString(1, currencies.getCode());
            prepareStatement.setString(2, currencies.getName());
            prepareStatement.setString(3, currencies.getSign());

            prepareStatement.executeUpdate();

            ResultSet generatedKeys = prepareStatement.getGeneratedKeys();
            if(generatedKeys.next()){
                currencies.setId(generatedKeys.getLong("id"));
            }

            return currencies;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean delete(Long id){
        try (Connection connection = ConnectionManager.open();
             var prepareStatement = connection.prepareStatement(DELETE_SQL)) {
            prepareStatement.setLong(1, id);

            return prepareStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static Currencies buildCurrencies(ResultSet resultSet) throws SQLException {
        return new Currencies(
                resultSet.getLong("id"),
                resultSet.getString("code"),
                resultSet.getString("name"),
                resultSet.getString("sign")
        );
    }

}
