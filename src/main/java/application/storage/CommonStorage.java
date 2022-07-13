package application.storage;

import application.model.CommonDataModel;
import util.exception.NotFoundException;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

public abstract class CommonStorage<T extends CommonDataModel> implements Storage<T> {
    private final Map<Integer, T> list = new TreeMap<>();
    private int commonSize = 0;

    @Override
    public Collection<T> findAll() {
        return list.values();
    }

    @Override
    public Optional<T> findItem(int id) {
        if (list.containsKey(id)) {
            return Optional.ofNullable(list.get(id));
        }
        return Optional.empty();
    }

    @Override
    public Optional<T> createItem(T item) {
        commonSize++;
        item.setId(commonSize);
        list.put(item.getId(), item);
        return Optional.of(item);
    }

    @Override
    public Optional<T> updateItem(T item) {
        if (findItem(item.getId()).isEmpty()) {
            throw new NotFoundException("Не найден");
        }
        list.put(item.getId(), item);
        return Optional.of(item);
    }

    @Override
    public Optional<T> delete(int id) {
        commonSize--;
        return Optional.ofNullable(list.remove(id));
    }
}
