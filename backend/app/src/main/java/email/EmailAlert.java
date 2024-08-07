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


//      instantiate a new email builder and build parameters from map
        EmailBuilder builder = new EmailBuilder();
        builder.fromMap(new HashMap<>())
                .setParameter("title", "Test Email")
                .setParameter("content", "This is a new test email");

        Map<String, Object> params = builder.build();

//      instantiate an email by putting in an html template (String), and parameters (Map)
//      we used double curly brackets for placeholders
        EmailAlert emailAlert = new EmailAlert("<html><body><h1>{{title}}</h1><p>{{content}}</p></body></html>", params);
//      Method for formatting email body: replacing values into placeholders in template
        String body = emailAlert.formatEmailBody();
//      Method for sending email
        emailAlert.sendEmail("safeTO <developers@csc207.joefang.org>",
                "bilin.nong@mai.utoronto.ca",
                "Test Email", body);

    }
}
