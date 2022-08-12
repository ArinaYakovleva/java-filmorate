package application.storage.dao;

import application.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import util.exception.NotFoundException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Component
@Slf4j
public class UserDbStorage implements IUserDbStorage {
    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Collection<User> findAll() {
        return jdbcTemplate
                .query("select * from users;", (rs, rowNum) -> makeUser(rs));
    }

    @Override
    public Optional<User> findItem(int id) {
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet("select * from USERS where USER_ID = ?;", id);

        if (sqlRowSet.next()) {
            User user = new User(sqlRowSet.getString("email"),
                    sqlRowSet.getString("login"),
                    sqlRowSet.getDate("birthday").toLocalDate());
            user.setName(sqlRowSet.getString("name"));
            user.setId(sqlRowSet.getInt("user_id"));
            log.info(String.format("Найден пользователь: %s", user.toString()));
            return Optional.of(user);
        } else {
            throw getNotFoundError(id);
        }
    }

    @Override
    public Optional<User> createItem(User item) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("user_id");

        int generatedId = jdbcInsert.executeAndReturnKey(item.toMap()).intValue();
        item.setId(generatedId);
        return Optional.of(item);
    }

    @Override
    public Optional<User> updateItem(User item) {
        String sqlQuery = "update users set email=?, login=?, name=?, birthday=? where user_id=?";
        try {
            jdbcTemplate.update(sqlQuery,
                    item.getEmail(),
                    item.getLogin(),
                    item.getName(),
                    item.getBirthday(),
                    item.getId()
            );
            return Optional.of(item);
        } catch (DataAccessException e) {
            throw getNotFoundError(item.getId());
        }
    }

    @Override
    public void delete(int id) {
        String sqlQuery = "delete from users where user_id=?";
        try {
            jdbcTemplate.update(sqlQuery, id);
        } catch (DataAccessException e) {
            throw getNotFoundError(id);
        }
    }

    public Collection<User> getFilmLikes(int filmId) {
        String likesQuery = "select * from users as u join likes as l on l.user_id = u.user_id where l.film_id=?";
        return jdbcTemplate.query(likesQuery, (rs, rowNum) -> makeUser(rs), filmId);
    }

    public void addFriend(int userId, int friendId) {
        String sqlQuery = "insert into friends values(?, ?)";
        try {
            jdbcTemplate.update(sqlQuery, userId, friendId);
        } catch (DataAccessException e) {
            log.error(String.format("Ошибка добавления в " +
                    "друзья к пользователю %d пользователя с ID %d", userId, friendId));
            throw new NotFoundException(String.format("Ошибка добавления в " +
                    "друзья к пользователю %d пользователя с ID %d", userId, friendId));
        }

    }

    public void removeFriend(int userId, int friendId) {
        String sqlQuery = "delete from friends where user_id=? and friend_id=?";
        try {
            jdbcTemplate.update(sqlQuery, userId, friendId);
        } catch (DataAccessException e) {
            String errorMessage = String.format("Ошибка удаления из " +
                    "друзей у пользователя %d пользователя с ID %d", userId, friendId);
            log.error(errorMessage);
            throw new NotFoundException(errorMessage);

        }
    }

    public Collection<User> getUserFriends(int userId) {
        String sqlQuery = "select * from USERS inner join FRIENDS F on USERS.USER_ID = F.USER_ID where F.USER_ID=?;";
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> makeUser(rs), userId);
    }

    public Collection<User> getCommonFriends(int userId, int friendId) {
        String sqlQuery = "select * from FRIENDS " +
                "join USERS U on U.USER_ID = FRIENDS.FRIEND_ID " +
                "where FRIENDS.USER_ID=? and FRIENDS.FRIEND_ID in (select FRIEND_ID from friends where USER_ID = ?)";
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> makeUser(rs), userId, friendId);
    }

    private User makeUser(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("user_id");
        String email = resultSet.getString("email");
        String login = resultSet.getString("login");
        String name = resultSet.getString("name");
        LocalDate birthday = resultSet.getDate("birthday").toLocalDate();

        Set<Integer> friends = getUserFriendsById(id);
        User user = new User(email, login, birthday);
        user.setName(name);
        user.setId(id);
        user.setFriends(friends);
        return user;
    }

    private Set<Integer> getUserFriendsById(int id) {
        Set<Integer> friendsIds = new HashSet<>();
        SqlRowSet friendsRowSet = jdbcTemplate.queryForRowSet("select f.friend_id from users as u " +
                "join friends as f on f.user_id = u.user_id where u.user_id=?;", id);
        while (friendsRowSet.next()) {
            friendsIds.add(friendsRowSet.getInt("friend_id"));
        }
        return friendsIds;
    }

    private NotFoundException getNotFoundError(int recordID) {
        String errorMessage = String.format("Ошибка доступа к записи, запись с ID %d не найдена", recordID);
        log.error(errorMessage);
        return new NotFoundException(errorMessage);
    }
}
