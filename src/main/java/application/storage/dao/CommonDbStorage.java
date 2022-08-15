package application.storage.dao;

import lombok.extern.slf4j.Slf4j;
import util.exception.NotFoundException;

@Slf4j
public abstract class CommonDbStorage {
    public NotFoundException getNotFoundError(int recordID) {
        String errorMessage = String.format("Ошибка доступа к записи, запись с ID %d не найдена", recordID);
        log.error(errorMessage);
        return new NotFoundException(errorMessage);
    }
}
