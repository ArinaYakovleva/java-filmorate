package application.service;

import application.model.CommonDataModel;
import application.storage.Storage;
import lombok.extern.slf4j.Slf4j;
import util.exception.CreateException;
import util.exception.NotFoundException;

import java.util.Collection;
import java.util.Optional;

@Slf4j
public abstract class CommonService<T extends CommonDataModel, K extends Storage<T>> implements IService<T> {
    protected final K storage;

    public CommonService(K storage) {
        this.storage = storage;
    }

    public Collection<T> findAll() {
        return storage.findAll();
    }

    public T findItem(int id) {
        return storage
                .findItem(id)
                .orElseThrow(() ->
                        new NotFoundException(String.format("Объект с ID %d не найден", id)));
    }

    public T createItem(T item) {
        return storage
                .createItem(item)
                .orElseThrow(() -> new CreateException("Ошибка создания"));
    }

    @Override
    public T updateItem(T item) {
        Optional<T> user = storage.updateItem(item);
        if (user.isEmpty()) {
            throw new NotFoundException(String.format("Пользователь с ID %d не найден", item.getId()));
        }
        log.debug(String.format("Обновление фильма: %s", item.toString()));
        return user.get();
    }

    public void deleteItem(int id) {
        log.debug(String.format("Удаления фильма с ID %d", id));
        storage.delete(id);
    }
}

