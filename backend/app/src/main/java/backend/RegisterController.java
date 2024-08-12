package com.demo.controller;

import com.demo.pojo.RegisterUser;
import com.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class RegisterController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<RegisterUser> registerUser(@RequestBody RegisterUser user) {
        // Call the service layer method to save user information
        RegisterUser registeredUser = userService.registerUser(user);

        // Return a response containing the user object with the generated random password
        return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestParam String email, @RequestParam String password) {
        // Call the service layer method to validate login information
        boolean isAuthenticated = userService.loginUser(email, password);

        if (isAuthenticated) {
            return new ResponseEntity<>("Login successful!", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Invalid email or password", HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/info")
    public ResponseEntity<RegisterUser> getUserInfo(@RequestParam String email) {
        // Call the service layer method to get user information
        RegisterUser user = userService.getUserByEmail(email);

        if (user != null) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
