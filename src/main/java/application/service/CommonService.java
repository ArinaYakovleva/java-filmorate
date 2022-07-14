package application.service;

import application.model.CommonDataModel;
import application.storage.Storage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import util.exception.CreateException;
import util.exception.NotFoundException;

import java.util.Collection;

@org.springframework.stereotype.Service
@Slf4j
public abstract class CommonService<T extends CommonDataModel> implements Service<T> {
    protected final Storage<T> storage;

    @Autowired
    public CommonService(Storage<T> storage) {
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
        log.debug(String.format("Обновление фильма: %s", item.toString()));
        if (!storage.findItem(item.getId()).isPresent()) {
            throw new NotFoundException(String.format("Пользователь с ID %d не найден", item.getId()));
        }
        return storage
                .updateItem(item)
                .orElseThrow(() -> new CreateException(String.format("Ошибка при обновлении %s", item)));
    }

    public T deleteItem(int id) {
        log.debug(String.format("Удаления фильма с ID %d", id));
        return storage
                .delete(id)
                .orElseThrow(() -> new NotFoundException(String.format("Фильм с ID %d не найден", id)));
    }
}

