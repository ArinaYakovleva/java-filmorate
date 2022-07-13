package application.storage;

import application.model.User;
import org.springframework.stereotype.Component;

@Component
public class InMemoryUserStorage extends CommonStorage<User> {

}
