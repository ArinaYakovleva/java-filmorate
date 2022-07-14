package application.service;

import application.model.User;
import application.storage.Storage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import util.exception.CreateException;
import util.exception.NotFoundException;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class InMemoryUserService extends CommonService<User> implements UserService {
    public InMemoryUserService(Storage<User> storage) {
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
    public void addFriend(User user, User friend) {
        user.addFriend(friend);
    }

    @Override
    public void removeFriend(User user, User friend) {
        user.removeFriend(friend);
    }

    @Override
    public Collection<User> getUserFriends(int userId) {
        User user = storage.findItem(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        Map<Integer, User> userFriends = new HashMap<>();
        for (Integer id : user.getFriends()) {
            User friend = storage.findItem(id).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
            userFriends.put(id, friend);
        }
        return userFriends.values();
    }

    @Override
    public List<User> getCommonFriends(int userId, int friendId) {
        Set<Integer> userFriends = storage.findItem(userId)
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь с ID %d не найден", userId)))
                .getFriends();

        Set<Integer> friendFriends = storage.findItem(friendId)
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь с ID %d не найден", friendId)))
                .getFriends();

        Set<Integer> commonIds = new HashSet<>(userFriends);
        commonIds.retainAll(friendFriends);

        return commonIds
                .stream()
                .map((id) -> storage.findItem(id).orElseThrow(() ->
                        new NotFoundException(String.format("Пользователь с ID %d не найден", id))))
                .collect(Collectors.toList());
    }
}
