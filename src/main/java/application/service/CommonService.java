package application.service;

import application.model.CommonDataModel;
import application.storage.Storage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import util.exception.NotFoundException;

import java.util.Collection;
import java.util.Optional;

@Service
public abstract class CommonService<T extends CommonDataModel> {
    protected final Storage<T> storage;

    @Autowired
    public CommonService(Storage<T> storage) {
        this.storage = storage;
    }

    public Collection<T> findAll() {
        return storage.findAll();
    }

    public Optional<T> findItem(int id) {
        return storage.findItem(id);
    }

    public Optional<T> createItem(T item) {
        return storage.createItem(item);
    }

    public Optional<T> updateItem(T item) {
        if (!storage.findItem(item.getId()).isPresent()) {
            throw new NotFoundException(String.format("Пользователь с ID %d не найден", item.getId()));
        }
        return storage.updateItem(item);
    }

    public Optional<T> deleteItem(int id) {
        return storage.delete(id);
    }
}
