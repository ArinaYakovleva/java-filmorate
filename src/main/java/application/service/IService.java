package application.service;

import java.util.Collection;
import java.util.Optional;

public interface IService<T> {
    Collection<T> findAll();

    T findItem(int id);

    T createItem(T item);

    T updateItem(T item);

    T deleteItem(int id);

}
