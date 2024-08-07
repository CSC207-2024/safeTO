package user;

import java.util.Optional;

public interface UserService {
    void saveUser(User user);
    Optional<User> getUserByEmail(String email);
}
