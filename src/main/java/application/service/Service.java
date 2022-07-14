package application.service;

import java.util.Collection;

public interface Service<T> {
    Collection<T> findAll();

    T findItem(int id);

    T createItem(T item);

    T updateItem(T item);

    T deleteItem(int id);

}
