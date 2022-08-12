package application.storage.dao;

import application.model.User;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import util.exception.NotFoundException;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Transactional
class UserDbStorageTest {
    private final IUserDbStorage userDbStorage;
    private final IFilmDbStorage filmDbStorage;
    private static User testUser;

    @BeforeAll
    public static void init() {
        testUser = new User("ivan@mail.ru", "ivan", LocalDate.of(1997, 5, 29));
        testUser.setName("Ivan");
    }

    @Test
    public void findAll() {
        Collection<User> users = userDbStorage.findAll();
        Assertions.assertEquals(2, users.size());
    }

    @Test
    public void findFilm() {
        Optional<User> user = userDbStorage.findItem(1);
        Assertions.assertEquals(1, user.get().getId());
        Assertions.assertEquals(testUser, user.get());
    }

    @Test
    public void findNotFoundItem() {
        NotFoundException exception = Assertions.assertThrows(NotFoundException.class, () -> {
            userDbStorage.findItem(3);
        });
        Assertions.assertEquals("Ошибка доступа к записи, запись с ID 3 не найдена", exception.getMessage());
    }

    @Test
    public void createItem() {
        User testCreateUser = new User("test1@mail.ru", "test1", LocalDate.of(1997, 5, 29));
        testUser.setName("Test");
        Optional<User> user = userDbStorage.createItem(testCreateUser);
        Assertions.assertEquals(testCreateUser, user.get());
    }

    @Test
    public void updateItem() {
        User updateUser = new User("ivan@mail.ru", "ivan", LocalDate.of(1997, 5, 29));
        updateUser.setName("Ivan");
        updateUser.setId(1);
        Optional<User> user = userDbStorage.updateItem(updateUser);
        Assertions.assertEquals(updateUser, user.get());
    }

    @Test
    public void delete() {
        userDbStorage.delete(1);
        Collection<User> users = userDbStorage.findAll();
        Assertions.assertEquals(1, users.size());
    }

    @Test
    public void getFilmLikes() {
        filmDbStorage.likeFilm(1, 1);
        Collection<User> users = userDbStorage.getFilmLikes(1);
        Assertions.assertEquals(1, users.size());
    }

    @Test
    public void addFriend() {
        userDbStorage.addFriend(1, 2);
        Collection<User> friends = userDbStorage.getUserFriends(1);
        Assertions.assertEquals(1, friends.size());
    }

    @Test
    public void addFriendIfNotFound() {
        NotFoundException exception = Assertions.assertThrows(NotFoundException.class, () -> {
            userDbStorage.addFriend(3, 2);
        });
        Assertions.assertEquals("Ошибка добавления в друзья к пользователю 3 пользователя с ID 2",
                exception.getMessage());
    }

    @Test
    public void removeFriend() {
        userDbStorage.addFriend(1, 2);
        userDbStorage.removeFriend(1,2);
        Collection<User> friends = userDbStorage.getUserFriends(1);
        Assertions.assertEquals(0, friends.size());
    }

    @Test
    public void getCommonFriends() {
        User commonFriend = new User("test1@mail.ru", "test1", LocalDate.of(1997, 5, 29));
        commonFriend.setName("Test");
        userDbStorage.createItem(commonFriend);
        userDbStorage.addFriend(1, 3);
        userDbStorage.addFriend(2, 3);
        Collection<User> commonFriends = userDbStorage.getCommonFriends(1,2);
        Assertions.assertEquals(1, commonFriends.size());
    }
}
