package application.service;

import application.model.User;
import application.storage.Storage;
import org.springframework.stereotype.Service;
import util.exception.NotFoundException;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService extends CommonService<User> {
    public UserService(Storage<User> storage) {
        super(storage);
    }

    public void addFriend(User user, User friend) {
        user.addFriend(friend);
    }

    public void removeFriend(User user, User friend) {
        user.removeFriend(friend);
    }

    public Collection<User> getUserFriends(int userId) {
        User user = storage.findItem(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        Map<Integer, User> userFriends = new HashMap<>();
        for (Integer id : user.getFriends()) {
            User friend = storage.findItem(id).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
            userFriends.put(id, friend);
        }
        return userFriends.values();
    }

    public List<User> getCommonFriends(int userId, int friendId) {
        Set<Integer> userFriends = storage.findItem(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"))
                .getFriends();

        Set<Integer> friendFriends = storage.findItem(friendId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"))
                .getFriends();

        Set<Integer> commonIds = new HashSet<>(userFriends);
        commonIds.retainAll(friendFriends);

        return commonIds
                .stream()
                .map((id) -> storage.findItem(id).orElseThrow(() -> new NotFoundException("Пользователь не найден")))
                .collect(Collectors.toList());
    }
}
