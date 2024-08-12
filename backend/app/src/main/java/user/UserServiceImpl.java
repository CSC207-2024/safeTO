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

    private final Map<Integer, User> UserDatabase = new HashMap<>();

    /**
     * Saves a user to the in-memory database.
     *
     * @param user the user to be saved
     */
    @Override
    public void saveUser(User user) {
        UserDatabase.put(user.getUserID(), user);
        printUserDatabase();
    }

    /**
     * Retrieves a user by their user ID.
     *
     * @param userID the email address of the user
     * @return an Optional containing the user if found, or an empty Optional if not found
     */
    @Override
    public Optional<User> getUserByUserID(String userID) {
        Optional.ofNullable(UserDatabase.get(userID));
    }

    /**
     * Retrieves a user by their user ID and then gets the email.
     *
     * @param userID the user ID of the user
     * @return an Optional containing the user's email if found, or an empty Optional if not found
     */
    @Override
    public Optional<String> getEmailByUserID(String userID) {
        User user = UserDatabase.get(userID);
        if (user != null) {
            return Optional.of(user.getEmail());
        } else {
            return Optional.empty();
        }
    }

}
