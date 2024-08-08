package email;

import com.resend.Resend;
import com.resend.services.emails.model.CreateEmailOptions;
import com.resend.services.emails.model.CreateEmailResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.HashMap;

public class EmailAlert implements InterfaceEmail {

    private final String template;
    private final String apiKey;
    private final EmailBuilder emailBuilder;
    private static final Logger logger = LoggerFactory.getLogger(EmailAlert.class);

    /**
     * A public constructor that initializes the email template and builder.
     * 
     * @param template     The email template in HTML format.
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
     * 
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

    private String[] runPythonScript(String neighbourhood, String year) {
        String[] result = new String[2];
        try {
            ProcessBuilder pb = new ProcessBuilder("python3", "generate_annual_report.py", neighbourhood, year);
            pb.redirectErrorStream(true);
            Process process = pb.start();

            try (BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                int index = 0;
                while ((line = in.readLine()) != null) {
                    if (index < result.length) {
                        result[index] = line;
                        index++;
                    }
                }
            }

            int exitCode = process.waitFor();
            if (exitCode != 0) {
                throw new RuntimeException("Python script failed with exit code " + exitCode);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Sends an email using the Resend service.
     * 
     * @param from    The sender's email address.
     * @param to      The recipient's email address.
     * @param subject The email subject.
     * @param body    The email body.
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

    private static String callPythonScriptForChartBase64() {
        // ???
        // who defined this function
        return "";
    }

    private static String callPythonScriptForChartBase64Prev() {
        return "";
    }

    public static void main(String[] args) {

        String template = "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <title>Annual Crime Report</title>\n" +
                "    <style>\n" +
                "        body { font-family: Arial, sans-serif; line-height: 1.6; }\n" +
                "        .container { max-width: 800px; margin: 0 auto; padding: 20px; border: 1px solid #ddd; border-radius: 5px; }\n"
                +
                "        .header { text-align: center; padding-bottom: 20px; }\n" +
                "        .header h1 { margin: 0; font-size: 24px; }\n" +
                "        .header h2 { margin: 0; font-size: 20px; color: #555; }\n" +
                "        .content { margin-bottom: 20px; }\n" +
                "        .content h3 { margin-bottom: 10px; }\n" +
                "        .content p { margin: 10px 0; }\n" +
                "        .footer { text-align: center; padding-top: 20px; font-size: 14px; color: #777; }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <div class=\"container\">\n" +
                "        <div class=\"header\">\n" +
                "            <h1>Annual Crime Report</h1>\n" +
                "            <h2>{{neighborhood}} - {{year}}</h2>\n" +
                "        </div>\n" +
                "        <div class=\"content\">\n" +
                "            <h3>Dear {{recipientName}},</h3>\n" +
                "            <p>We hope this message finds you well. Please find below the annual crime report for {{neighborhood}} for the year of {{year}}.</p>\n"
                +
                "            <p>This report includes a comparison of crime rates and two bar plots illustrating different categories of crimes.</p>\n"
                +
                "            <div class=\"chart\">\n" +
                "                <h3>Crime Categories Bar Plot</h3>\n" +
                "                <img src=\"data:image/png;base64,{{chartBase64}}\" alt=\"Bar Plot of Crime Categories\" style=\"width: 100%; height: auto;\">\n"
                +
                "            </div>\n" +
                "            <div class=\"chart\">\n" +
                "                <h3>Crime Categories Bar Plot - Previous Year</h3>\n" +
                "                <img src=\"data:image/png;base64,{{chartBase64Prev}}\" alt=\"Bar Plot of Crime Categories - Previous Year\" style=\"width: 100%; height: auto;\">\n"
                +
                "            </div>\n" +
                "        </div>\n" +
                "        <div class=\"footer\">\n" +
                "            <p>If you have any questions or need further information, please feel free to contact us.</p>\n"
                +
                "            <p>Thank you for your attention.</p>\n" +
                "            <p>Best regards,<br>The Safety Map Team</p>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>";

        String chartBase64 = callPythonScriptForChartBase64();
        String chartBase64Prev = callPythonScriptForChartBase64Prev();

        // instantiate a new email builder and build parameters from map
        EmailBuilder builder = new EmailBuilder();
        builder.fromMap(new HashMap<>())
                .setParameter("neighborhood", "Clairlea-Birchmount")
                .setParameter("year", "2023")
                .setParameter("recipientName", "Joe")
                .setParameter("chartUrl", chartBase64)
                .setParameter("chartUrl2", chartBase64Prev);

        // Map<String, Object> params = builder.build();

        // instantiate an email by putting in an html template (String), and parameters
        // (Map)
        // we used double curly brackets for placeholders
        EmailAlert emailAlert = new EmailAlert(template, builder);
        // Method for formatting email body: replacing values into placeholders in
        // template
        String body = emailAlert.formatEmailBody();
        // Method for sending email
        emailAlert.sendEmail("safeTO <developers@csc207.joefang.org>",
                "bilin.nong@mai.utoronto.ca",
                "Test Email", body);

    }
}
