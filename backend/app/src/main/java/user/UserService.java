package user;

import java.util.Optional;

public interface UserService {
    void saveUser(User user);
    Optional<User> getUserByUserID(String userID)
    Optional<User> getEmailByUserID(String userID);
}
