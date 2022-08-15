package application.service;

import java.util.Collection;

public interface IService<T> {
    Collection<T> findAll();

    T findItem(int id);

    T createItem(T item);

    T updateItem(T item);

    void deleteItem(int id);

}
