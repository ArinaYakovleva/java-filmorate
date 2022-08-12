package application.storage.dao;

import application.model.User;
import application.storage.Storage;

import java.util.Collection;

public interface IUserDbStorage extends Storage<User> {
    void addFriend(int userId, int friendId);
    void removeFriend(int userId, int friendId);
    Collection<User> getUserFriends(int userId);
    Collection <User> getCommonFriends(int userId, int friendId);
    Collection<User> getFilmLikes(int filmId);
}
