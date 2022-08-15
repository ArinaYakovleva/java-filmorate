package application.service;

import application.model.User;

import java.util.Collection;

public interface IUserService extends IService<User> {
    void addFriend(int userId, int friendId);

    void removeFriend(int userId, int friendId);

    Collection<User> getUserFriends(int userId);

    Collection<User> getCommonFriends(int userId, int friendId);
}
