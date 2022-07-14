package application.service;

import application.model.User;

import java.util.Collection;
import java.util.List;

public interface UserService extends IService<User> {
    void addFriend(User user, User friend);

    void removeFriend(User user, User friend);

    Collection<User> getUserFriends(int userId);

    List<User> getCommonFriends(int userId, int friendId);
}
