package email;

import com.resend.Resend;
import com.resend.services.emails.model.CreateEmailOptions;
import com.resend.services.emails.model.CreateEmailResponse;
import com.resend.services.emails.model.Email;

import java.util.HashMap;
import java.util.Map;



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

