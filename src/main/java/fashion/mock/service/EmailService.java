package fashion.mock.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import fashion.mock.model.Order;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Autowired
    public EmailService(JavaMailSender mailSender, TemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    public void sendVerificationCode(String to, String code) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

            Context context = new Context();
            context.setVariable("code", code);

            String htmlContent = templateEngine.process("EmailVerify", context);

            helper.setTo(to);
            helper.setSubject("Mã xác minh của bạn");
            helper.setText(htmlContent, true); // true indicates this is HTML

            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            // Log the exception or handle it appropriately
            e.printStackTrace();
        }
    }
    
    public void sendOrderConfirmation(String to, Order order) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

            String htmlMsg = "<!DOCTYPE html>" +
                    "<html lang=\"vi\">" +
                    "<head>" +
                    "    <meta charset=\"UTF-8\">" +
                    "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">" +
                    "    <title>Order Confirmation</title>" +
                    "    <style>" +
                    "        body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background-color: #f8f9fa; }" +
                    "        .email-container { max-width: 600px; margin: 20px auto; background-color: #ffffff; border-radius: 8px; }" +
                    "        .header { background-color: #0066cc; color: #ffffff; padding: 20px; text-align: center; }" +
                    "        .content { padding: 30px; }" +
                    "        .footer { background-color: #f1f3f5; padding: 15px; text-align: center; font-size: 14px; color: #6c757d; }" +
                    "    </style>" +
                    "</head>" +
                    "<body>" +
                    "    <div class=\"email-container\">" +
                    "        <div class=\"header\">" +
                    "            <h1 class=\"mb-0\">F-Shop Fashion - Order Confirmation</h1>" +
                    "        </div>" +
                    "        <div class=\"content\">" +
                    "            <p>Thank you for your order! Here are your order details:</p>" +
                    "            <p><strong>Order ID:</strong> " + order.getId() + "</p>" +
                    "            <p><strong>Total Price:</strong> " + order.getTotalPrice() + " VND</p>" +
                    "            <p>We will notify you when your order is shipped.</p>" +
                    "        </div>" +
                    "        <div class=\"footer\">" +
                    "            <p>&copy; 2023 F-Shop Fashion. All rights reserved.</p>" +
                    "        </div>" +
                    "    </div>" +
                    "</body>" +
                    "</html>";

            helper.setTo(to);
            helper.setSubject("Order Confirmation - F-Shop Fashion");
            helper.setText(htmlMsg, true); // true indicates this is HTML

            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace(); // Log the exception or handle it appropriately
        }
    }
}