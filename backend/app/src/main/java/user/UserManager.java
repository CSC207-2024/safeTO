package user;

import java.util.Scanner;

public class UserManager{

    //TODO read from the username and password database

    //Demo
    private String validUsername = "admin";
    private String validPassword = "pswd";

    // Method to authenticate the user
    public boolean authenticate(String username, String password) {
        if (username == null || password == null) {
            return false;
        }
        return username.equals(validUsername) && password.equals(validPassword);
    }

    //TODO Method to sign up the user
    public boolean register(String username, String password) {

        return false;
    }


    
        

    //Demo to run login/authenticate process
    public static void main (String args[]) {
        Scanner scanner = new Scanner(System.in);
        UserManager um = new UserManager();

        System.out.println("Enter username:");
        String username = scanner.nextLine();

        System.out.println("Enter password:");
        String password = scanner.nextLine();

        if (um.authenticate(username, password)) {
            System.out.println("Login successful!");
        } else {
            System.out.println("Login failed. Invalid username or password.");
        }
        
        scanner.close();
    }
}
