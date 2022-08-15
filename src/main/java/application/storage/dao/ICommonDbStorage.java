package application.storage.dao;

import util.exception.NotFoundException;

public interface ICommonDbStorage {
    NotFoundException getNotFoundError(int recordID);
}
