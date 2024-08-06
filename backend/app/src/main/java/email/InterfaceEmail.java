package email;



/**
 * An interface for email alerts.
 */
public interface InterfaceEmail {
    void sendEmail(String from, String to, String subject, String body);
}
