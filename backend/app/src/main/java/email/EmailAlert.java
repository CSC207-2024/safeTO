package email;

import com.resend.Resend;
import com.resend.services.emails.model.CreateEmailOptions;
import com.resend.services.emails.model.CreateEmailResponse;
import com.resend.services.emails.model.Email;
import user.User;

import java.nio.file.Files;
import java.nio.file.Paths;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * A public class that formats and sends email alerts.
 */
public class EmailAlert implements InterfaceEmail {


    private final String template;
    private final String apiKey;
    private final Map<String, Object> parameters;

    /**
     * A public constructor that initializes the email template.
     * @param template The email template in HTML format.
     */
    public EmailAlert(String template, Map<String, Object> parameters) {
        this.template = template;
        this.parameters = parameters;
        this.apiKey = System.getenv("RESEND_API_KEY");
        if (this.apiKey == null || this.apiKey.isEmpty()) {
            throw new IllegalArgumentException("API key is not set.");
        }
    }



    /**
     * A public method that formats the email body.
     * @return a String contains the formatted email body.
     */
    public String formatEmailBody() {
        String body = template;
        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            body = body.replace("{{" + entry.getKey() + "}}", (CharSequence) entry.getValue());
        }
        return body;
    }

    /**
     * A public method that sends an email.
     * @param from the sender's email address.
     * @param to the recipient's email address.
     * @param subject the email subject.
     * @param body the email body.
     */
    @Override
    public void sendEmail(String from, String to, String subject, String body){
        Resend resend = new Resend(apiKey);

        CreateEmailOptions request = CreateEmailOptions.builder()
                .from(from)
                .to(to)
                .subject(subject)
                .html(body)
                .build();

        try {
            CreateEmailResponse data = resend.emails().send(request);
            System.out.println("Email sent successfully: " + data);
        } catch (Exception e) {
            System.err.println("Failed to send email: " + e.getMessage());
        }
    }

    private static final Logger logger = LoggerFactory.getLogger(EmailAlert.class);

    public static void main(String[] args) {


        EmailBuilder builder = new EmailBuilder();
        builder.fromMap(new HashMap<>())
                .setParameter("title", "Test Email")
                .setParameter("content", "This is a new test email");

        Map<String, Object> params = builder.build();


        EmailAlert emailAlert = new EmailAlert("<html><body><h1>{{title}}</h1><p>{{content}}</p></body></html>",
                params);


        String body = emailAlert.formatEmailBody();

        String templatePath = "safeTO/frontend/public/templates/monthlyReportEmail.html";
        EmailAlert emailAlert = new EmailAlert("<html><body><h1>{{title}}</h1><p>{{content}}</p></body></html>");

        // Fetch users
        List<User> users = fetchUsers(); //TODO: Implement this method to get users


        for (User user : users) {
            // Generate email content
            Map<String, String> data = generateReportData(user); // Implement this method
            String emailBody = emailAlert.formatEmailBody(data);

            // Send email
            emailAlert.sendEmail("safeTO <developers@csc207.joefang.org>",
                    user.getEmail(), "Annual Crime Report", emailBody);
        }

        // Log received user info (just an example, actual implementation might vary)
        logger.info("Received user info: {}", userInfo);
    }
    private static String readTemplate(String path) {
        try {
            return new String(Files.readAllBytes(Paths.get(path)));
        } catch (IOException e) {
            throw new RuntimeException("Failed to read template", e);
        }
    }

//    private static List<User> fetchUsers() {
//        // Placeholder implementation
//        List<User> users = new ArrayList<>();
//        try (Connection connection = DriverManager.getConnection("jdbc:yourdatabaseurl", "username", "password");
//             Statement statement = connection.createStatement();
//             ResultSet resultSet = statement.executeQuery("SELECT * FROM users")) {
//
//            while (resultSet.next()) {
//                User user = new User();
//                user.setEmail(resultSet.getString("email"));
//                user.setAddress(resultSet.getString("address"));
//                // Set other properties as needed
//                users.add(user);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return users;
//    }

    private static Map<String, String> generateReportData(User user) {
        Map<String, String> data = new HashMap<>();
        // Generate the report content based on the user's data
        String reportContent = generateReportContentForUser(user);
        data.put("title", "Monthly Crime Report");
        data.put("content", reportContent);
        return data;
    }

    private static String generateReportContentForUser(User user) {
        // Implement this method to generate content based on user info
        // For example, you might use a Python script or another method to generate the content
        return "Customized report content.";
    }
}
