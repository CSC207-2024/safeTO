package user;

import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Implementation of the UserService interface.
 * This service manages user data using an in-memory database.
 */
@Service
public class UserServiceImpl implements UserService {

//    private final Map<Integer, User> userDatabase = new HashMap<>();
    private final Map<String, User> emailDatabase = new HashMap<>();

    /**
     * Saves a user to the in-memory database.
     *
     * @param user the user to be saved
     */
    @Override
    public void saveUser(User user) {
        emailDatabase.put(user.getEmail(), user);
        printEmailDatabase();
    }

    /**
     * Retrieves a user by their email address.
     *
     * @param email the email address of the user
     * @return an Optional containing the user if found, or an empty Optional if not found
     */
    @Override
    public Optional<User> getUserByEmail(String email) {
        return Optional.ofNullable(emailDatabase.get(email));
    }

    /**
     * Prints the current state of the email database to the console.
     */
    public void printEmailDatabase() {
        System.out.println(emailDatabase);
    }

}
