package email;

import com.resend.Resend;
import com.resend.services.emails.model.CreateEmailOptions;
import com.resend.services.emails.model.CreateEmailResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class EmailAlert implements InterfaceEmail {

    private final String template;
    private final String apiKey;
    private final EmailBuilder emailBuilder;
    private static final Logger logger = LoggerFactory.getLogger(EmailAlert.class);

    /**
     * A public constructor that initializes the email template and builder.
     * @param template The email template in HTML format.
     * @param emailBuilder An EmailBuilder instance to build email parameters.
     */
    public EmailAlert(String template, EmailBuilder emailBuilder) {
        this.template = template;
        this.emailBuilder = emailBuilder;
        this.apiKey = System.getenv("RESEND_API_KEY");
        if (this.apiKey == null || this.apiKey.isEmpty()) {
            throw new IllegalArgumentException("API key is not set.");
        }
    }

    /**
     * Formats the email body using the provided template and parameters.
     * @return The formatted email body.
     */
    public String formatEmailBody() {
        String body = template;
        Map<String, Object> parameters = emailBuilder.getParameters();
        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            body = body.replace("{{" + entry.getKey() + "}}", entry.getValue().toString());
        }
        return body;
    }

    /**
     * Sends an email using the Resend service.
     * @param from The sender's email address.
     * @param to The recipient's email address.
     * @param subject The email subject.
     * @param body The email body.
     */
    @Override
    public void sendEmail(String from, String to, String subject, String body) {
        Resend resend = new Resend(apiKey);

        CreateEmailOptions request = CreateEmailOptions.builder()
                .from(from)
                .to(to)
                .subject(subject)
                .html(body)
                .build();

        try {
            CreateEmailResponse response = resend.emails().send(request);
            logger.info("Email sent successfully: {}", response);
        } catch (Exception e) {
            logger.error("Failed to send email: {}", e.getMessage());
        }
    }

    public static void main(String[] args) {
        String template = "<html><body><h1>{{title}}</h1><p>{{content}}</p></body></html>";
        EmailBuilder builder = new EmailBuilder();
        builder.setParameter("title", "Test Email")
                .setParameter("content", "This is a new test email");

        EmailAlert emailAlert = new EmailAlert(template, builder);

        // Example user list and email sending logic
        // List<User> users = fetchUsers(); // Implement user fetching if needed

        // For demonstration, using a static email list
        String[] emailAddresses = {"zhangyiyun178@gmail.com", "yiyunzhang957@gmail.com"};
        for (String email : emailAddresses) {
            String emailBody = emailAlert.formatEmailBody();
            emailAlert.sendEmail("safeTO <developers@csc207.joefang.org>", email, "Annual Crime Report", emailBody);
        }
    }
}
