package backend;

import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import user.User;
import user.UserService;
import location.SimpleLocation;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class UserResourceTest {

    private UserServiceMock userService;
    private UserResource userResource;

    @BeforeEach
    void setUp() {
        userService = new UserServiceMock();
        userResource = new UserResource(userService);
    }

    @Test
    void createUser_success() {
        // Given
        SimpleLocation location = new SimpleLocation(40.7128, -74.0060); // Example location
        User user = new User("John", "Doe", "test@example.com", "password123", "user", true, location);

        // When
        Response response = userResource.createUser(user);

        // Then
        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        assertNotNull(userService.getUserByEmail(user.getEmail()));
    }

    @Test
    void getUserByEmail_success() {
        // Given
        SimpleLocation location = new SimpleLocation(40.7128, -74.0060); // Example location
        User user = new User("Jane", "Smith", "jane.smith@example.com", "password456", "admin", true, location);
        userService.addUser(user);

        // When
        Response response = userResource.getUserByEmail("jane.smith@example.com");

        // Then
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(user, response.getEntity());
    }

    @Test
    void getUserByEmail_notFound() {
        // Given
        String email = "unknown@example.com";

        // When
        Response response = userResource.getUserByEmail(email);

        // Then
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
    }

    // Simplified mock implementation of the UserService interface
    static class UserServiceMock implements UserService {

        private final Map<String, User> users = new HashMap<>();

        @Override
        public void saveUser(User user) {
            users.put(user.getEmail(), user);
        }

        @Override
        public Optional<User> getUserByEmail(String email) {
            return Optional.ofNullable(users.get(email));
        }

        public void addUser(User user) {
            users.put(user.getEmail(), user);
        }
    }
}
