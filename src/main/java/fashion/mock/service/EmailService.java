package fashion.mock.service;

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
    
    /**
     * @author Tran Thien Thanh 09/04/1996
     */
    public void sendOrderConfirmation(String to, Order order) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

            // Create a Thymeleaf context and set the variables
            Context context = new Context();
            context.setVariable("orderId", order.getId());
            context.setVariable("totalPrice", String.format("%,.0f", order.getTotalPrice()));
            context.setVariable("userName", order.getUser().getUserName());
            context.setVariable("orderDate", order.getOrderDate());

            // Process the Thymeleaf template and get the result
            String htmlContent = templateEngine.process("order-confirmation", context);

            // Set the email properties
            helper.setTo(to);
            helper.setSubject("Order Confirmation - F-Shop Fashion");
            helper.setText(htmlContent, true); // true indicates this is HTML

            // Send the email
            mailSender.send(mimeMessage);

        } catch (MessagingException e) {
            // Log the exception or handle it appropriately
            e.printStackTrace();
        }
    }
}