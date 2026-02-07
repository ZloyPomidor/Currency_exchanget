package org.example.dao;

import java.util.List;
import java.util.Optional;

public interface Dao<K, E> {

    void update(E currencies);

    E save(E currencies);

    boolean delete(K id);

    Optional<E> findById(K id);

    List<E> findAll();
}
