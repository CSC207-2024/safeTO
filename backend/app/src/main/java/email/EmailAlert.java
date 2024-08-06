package email;


import com.resend.Resend;
import com.resend.services.emails.model.CreateEmailOptions;
import com.resend.services.emails.model.CreateEmailResponse;


import java.util.HashMap;
import java.util.Map;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;


/**
 * A public class that formats and sends email alerts.
 */
public class EmailAlert implements InterfaceEmail {

    private final String template;
    private final String apiKey;

    /**
     * A public constructor that initializes the email template.
     * @param template The email template in HTML format.
     */
    public EmailAlert(String template) {
        this.template = template;
        this.apiKey = System.getenv("RESEND_API_KEY");
        if (this.apiKey == null || this.apiKey.isEmpty()) {
            throw new IllegalArgumentException("API key is not set.");
        }
    }

    /**
     * A public method that reads contents from a file.
     * @param input the string contains the key-value pairs of email content.
     * @return a map of key-value pairs.
     */
    public Map<String, String> parseInput(String input) {
        Map<String, String> map = new HashMap<>();

        String cleanedInput = input.replaceAll("[{}]", "");
        String[] pairs = cleanedInput.split(",");

        for (String pair : pairs) {
            String[] keyValue = pair.split(":");
            map.put(keyValue[0].trim(), keyValue[1].trim());
        }

        return map;
    }

    /**
     * A public method that reads contents from a file.
     * @param path the path to the file.
     * @return a map of key-value pairs.
     */
    public Map<String, String> parseInputFromFile(String path) {
        Map<String, String> map = new HashMap<>();

        try {
//            read the file as a String
            String input = new String(Files.readAllBytes(Paths.get(path)));
            map = parseInput(input);
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }

        return map;
    }



    /**
     * A public method that formats the email body.
     * @param map a map of key-value pairs.
     * @return a String contains the formatted email body.
     */
    public String formatEmailBody(Map<String, String> map) {
        String body = template;

//        replace the placeholders in template with the values from the map
        for (Map.Entry<String, String> entry : map.entrySet()) {
            body = body.replace("{{" + entry.getKey() + "}}", entry.getValue());
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

    public static void main(String[] args) {
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
                    user.getEmail(), "Monthly Crime Report", emailBody);
        }
    }
    private static String readTemplate(String path) {
        try {
            return new String(Files.readAllBytes(Paths.get(path)));
        } catch (IOException e) {
            throw new RuntimeException("Failed to read template", e);
        }
    }

    private static List<User> fetchUsers() { // TODO: implement this method
        // Fetch and return a list of users from your database
        return List.of(); // Placeholder
    }

    private static Map<String, String> generateReportData(User user) {
        // Generate and return the report data for the user
        Map<String, String> data = new HashMap<>();
        data.put("title", "Monthly Crime Report");
        data.put("content", "Your customized report content here.");
        return data;
    }
}

