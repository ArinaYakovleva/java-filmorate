package application.storage;

import java.util.Collection;
import java.util.Optional;

public interface Storage<T> {
    Collection<T> findAll();

    Optional<T> findItem(int id);

    Optional<T> createItem(T item);

    Optional<T> updateItem(T item);

    void delete(int id);
}
