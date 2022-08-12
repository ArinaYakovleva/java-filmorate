package application.service;

import application.model.User;
import application.storage.dao.IUserDbStorage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@Slf4j
public class UserService extends CommonService<User, IUserDbStorage> implements IUserService {
    @Autowired
    public UserService(IUserDbStorage storage) {
        super(storage);
    }

    @Override
    public User createItem(User item) {
        log.debug(String.format("Создание пользователя: %s", item.toString()));
        if (item.getName() == null || item.getName().isEmpty()) {
            item.setName(item.getLogin());
        }
        return super.createItem(item);
    }

    @Override
    public void addFriend(int userId, int friendId) {
        log.debug(String.format("Добавление в друзья пользователя с ID %d у пользователя %d", friendId, userId));
        storage.addFriend(userId, friendId);
    }

    @Override
    public void removeFriend(int userId, int friendId) {
        log.debug(String.format("Удаление друга с ID %d у пользователя %d", friendId, userId));
        log.debug(String.format("Добавление в друзья пользователя с ID %d у пользователя %d", friendId, userId));
        storage.removeFriend(userId, friendId);
    }

    @Override
    public Collection<User> getUserFriends(int userId) {
        return storage.getUserFriends(userId);
    }

    @Override
    public Collection<User> getCommonFriends(int userId, int friendId) {
        return storage.getCommonFriends(userId, friendId);
    }

    @Override
    public Collection<User> getFilmLikes(int filmId) {
        return storage.getFilmLikes(filmId);
    }
}
