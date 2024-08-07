package user;

import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

//    private final Map<Integer, User> userDatabase = new HashMap<>();
    private final Map<String, User> emailDatabase = new HashMap<>();

    @Override
    public void saveUser(User user) {
        emailDatabase.put(user.getEmail(), user);
        printEmailDatabase();
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return Optional.ofNullable(emailDatabase.get(email));
    }

    public void printEmailDatabase() {
        System.out.println(emailDatabase);
    }

}
