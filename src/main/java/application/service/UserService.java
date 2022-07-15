package application.service;

import application.model.User;

import java.util.Collection;
import java.util.List;

public interface UserService extends IService<User> {
    void addFriend(int userId, int friendId);

    void removeFriend(int userId, int friendId);

    Collection<User> getUserFriends(int userId);

    List<User> getCommonFriends(int userId, int friendId);
}
