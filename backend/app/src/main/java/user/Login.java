package user;

public class Login{


    // Method to authenticate the user
    public boolean authenticate(String username, String password) {
        if (username == null || password == null) {
            return false;
        }
        return username.equals(validUsername) && password.equals(validPassword);
    }

    

    public static void main (String args[]) {
        Scanner scanner = new Scanner(System.in);
        LoginService loginService = new LoginService();

        System.out.println("Enter username:");
        String username = scanner.nextLine();

        System.out.println("Enter password:");
        String password = scanner.nextLine();

        if (loginService.authenticate(username, password)) {
            System.out.println("Login successful!");
        } else {
            System.out.println("Login failed. Invalid username or password.");
        }
        
        scanner.close();
    }
}
