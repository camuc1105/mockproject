/**
 * Author: Nguyễn Viết Hoàng Phúc 22/11/1997
 */
package fashion.mock.service;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendVerificationCode(String to, String code) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

            String htmlMsg = "<!DOCTYPE html>\n" +
                    "<html lang=\"vi\">\n" +
                    "<head>\n" +
                    "    <meta charset=\"UTF-8\">\n" +
                    "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                    "    <title>Mã xác minh của bạn</title>\n" +
                    "    <link href=\"https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css\" rel=\"stylesheet\">\n" +
                    "    <style>\n" +
                    "        body {\n" +
                    "            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;\n" +
                    "            background-color: #f8f9fa;\n" +
                    "        }\n" +
                    "        .email-container {\n" +
                    "            max-width: 600px;\n" +
                    "            margin: 20px auto;\n" +
                    "            background-color: #ffffff;\n" +
                    "            border-radius: 8px;\n" +
                    "            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);\n" +
                    "            overflow: hidden;\n" +
                    "        }\n" +
                    "        .header {\n" +
                    "            background-color: #0066cc;\n" +
                    "            color: #ffffff;\n" +
                    "            padding: 20px;\n" +
                    "            text-align: center;\n" +
                    "        }\n" +
                    "        .content {\n" +
                    "            padding: 30px;\n" +
                    "        }\n" +
                    "        .code {\n" +
                    "            font-size: 32px;\n" +
                    "            font-weight: bold;\n" +
                    "            color: #0066cc;\n" +
                    "            text-align: center;\n" +
                    "            padding: 15px;\n" +
                    "            background-color: #e6f2ff;\n" +
                    "            border-radius: 5px;\n" +
                    "            margin: 20px 0;\n" +
                    "            letter-spacing: 5px;\n" +
                    "        }\n" +
                    "        .footer {\n" +
                    "            background-color: #f1f3f5;\n" +
                    "            padding: 15px;\n" +
                    "            text-align: center;\n" +
                    "            font-size: 14px;\n" +
                    "            color: #6c757d;\n" +
                    "        }\n" +
                    "    </style>\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "    <div class=\"email-container\">\n" +
                    "        <div class=\"header\">\n" +
                    "            <h1 class=\"mb-0\">F-Shop Fashion</h1>\n" +
                    "        </div>\n" +
                    "        <div class=\"content\">\n" +
                    "            <h2 class=\"text-center mb-4\">Mã xác minh của bạn</h2>\n" +
                    "            <p>Kính gửi quý khách,</p>\n" +
                    "            <p>Cảm ơn bạn đã sử dụng dịch vụ của F-Shop Fashion. Mã xác minh của bạn là:</p>\n" +
                    "            <div class=\"code\">" +code + "</div>\n" +
                    "            <p class=\"text-danger\"><strong>Lưu ý:</strong> Mã này sẽ hết hạn sau 5 phút. Vui lòng không chia sẻ mã này với bất kỳ ai.</p>\n" +
                    "            <p>Nếu bạn không yêu cầu mã này, vui lòng bỏ qua email này hoặc liên hệ với chúng tôi ngay lập tức.</p>\n" +
                    "            <p>Trân trọng,<br>Đội ngũ hỗ trợ F-Shop Fashion</p>\n" +
                    "        </div>\n" +
                    "        <div class=\"footer\">\n" +
                    "            <p>&copy; 2023 F-Shop Fashion. All rights reserved.</p>\n" +
                    "        </div>\n" +
                    "    </div>\n" +
                    "</body>\n" +
                    "</html>";

            helper.setTo(to);
            helper.setSubject("Mã xác minh của bạn");
            helper.setText(htmlMsg, true); // true indicates this is HTML

            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            // Log the exception or handle it appropriately
            e.printStackTrace();
        }
    }
}