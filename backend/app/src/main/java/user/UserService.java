package com.demo.service;

import com.demo.mapper.UserMapper;
import com.demo.pojo.RegisterUser;
import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.CreateEmailOptions;
import com.resend.services.emails.model.CreateEmailResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;
    private Resend resend;


    public RegisterUser registerUser(RegisterUser user) {
        // generate a random password
        String randomPassword = generateRandomPassword(8);
        user.setPassword(randomPassword);

        // Call the MyBatis Mapper interface method to insert user data
        userMapper.insertUser(user);

        // send registration email to user
        sendRegistrationEmail(user);

        return user; // return user with generated password
    }

    // login method
    public boolean loginUser(String email, String password) {
        // find user by email address
        RegisterUser user = userMapper.findByEmail(email);

        // return true if user exists and password matches, false otherwise
        if (user != null && user.getPassword().equals(password)) {
            return true;
        }
        return false;
    }

    // get user info via user's email address
    public RegisterUser getUserByEmail(String email) {
        return userMapper.findByEmail(email);
    }


    // function that generates a random password
    private String generateRandomPassword(int length) {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[length];
        random.nextBytes(bytes);
        return Base64.getEncoder().encodeToString(bytes).substring(0, length);
    }

    private void sendRegistrationEmail(RegisterUser user) {
        CreateEmailOptions params = CreateEmailOptions.builder()
                .from("safeTO Team <contact@csc207.joefang.org>") // Replace with
                .to(user.getEmail())
                .subject("Welcome to safeTO!")
                .html("<strong>Welcome, " + user.getFirstName() + "!</strong><br/>Your account has been successfully created.<br/>Your password is: <strong>" + user.getPassword() + "</strong>")
                .build();

        try {
            CreateEmailResponse data = resend.emails().send(params);
            System.out.println("Registration email sent successfully: " + data.getId());
        } catch (ResendException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String apiKey = System.getenv("RESEND_API_KEY");

        if (apiKey == null || apiKey.isEmpty()) {
            throw new IllegalArgumentException("API key is not set in the environment variables.");
        }

        Resend resend = new Resend(apiKey);
    }

}
