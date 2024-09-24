/**
 * Author: Nguyễn Viết Hoàng Phúc 22/11/1997
 */
package fashion.mock.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import fashion.mock.model.User;
import fashion.mock.service.EmailService;
import fashion.mock.service.UserService;
import fashion.mock.service.VerificationService;

@Controller
@RequestMapping("/forget-password")
public class ForgetPasswordController {
    private final UserService userService;
    private final EmailService emailService;
    private final VerificationService verificationService;

    public ForgetPasswordController(UserService userService, EmailService emailService,
                                    VerificationService verificationService) {
        this.userService = userService;
        this.emailService = emailService;
        this.verificationService = verificationService;
    }

    // Hiển thị form nhập email để quên mật khẩu
    @GetMapping
    public String showForgotPasswordForm() {
        return "forgotPasswordForm";
    }

    // Xử lý khi người dùng nhập email
    @PostMapping
    public String handleForgotPassword(@RequestParam("email") String email, Model model) {
        if (userService.getByEmail(email) == null) {
            model.addAttribute("error", "Email không tồn tại!");
            return "forgotPasswordForm";
        }
        String code = verificationService.generateAndStoreCode(email);
        emailService.sendVerificationCode(email, code);
        return "redirect:/forget-password/verify-code?email=" + email;
    }

    // Hiển thị form nhập mã xác nhận
    @GetMapping("/verify-code")
    public String showVerifyCodeForm(@RequestParam("email") String email, Model model) {
        model.addAttribute("email", email);
        return "verifyCodeForm";
    }

    // Xử lý khi người dùng nhập mã xác nhận
    @PostMapping("/verify-code")
    public String verifyCode(@RequestParam("email") String email, @RequestParam("code") String code, Model model) {
        boolean isValid = verificationService.validateCode(email, code);
        if (!isValid) {
            model.addAttribute("error", "Mã xác minh không đúng hoặc đã hết hạn!");
            model.addAttribute("email", email);
            return "verifyCodeForm";
        }
        System.out.println("Mã xác nhận hợp lệ. Chuyển hướng đến trang đặt lại mật khẩu.");
        return "redirect:/forget-password/reset-password?email=" + email;
    }

    // Hiển thị form cập nhật mật khẩu mới
    @GetMapping("/reset-password")
    public String showResetPasswordForm(@RequestParam("email") String email, Model model) {
        model.addAttribute("email", email);
        model.addAttribute("user", new User());
        return "resetPasswordForm";
    }

    @PostMapping("/resend-code-forget")
    public String resendCode(@RequestParam("email") String email, Model model) {
        try {
            String code = verificationService.generateAndStoreCode(email);
            emailService.sendVerificationCode(email, code);
        } catch (IllegalStateException e) {
            // Nếu vượt quá số lần gửi, hiển thị lỗi
            model.addAttribute("error", e.getMessage());
            model.addAttribute("email", email); // Truyền lại email để hiện trong form
            return "verifyCodeForm"; // Trả về trang xác nhận mã và hiển thị lỗi
        }
        return "redirect:/forget-password/verify-code?email=" + email;
    }


    // Xử lý cập nhật mật khẩu
    @PostMapping("/reset-password")
    public String resetPassword(@RequestParam("email") String email, @RequestParam("password") String password) {
        User user = userService.getByEmail(email);
        userService.updatePassword(user, password);
        System.out.println("changed password success!");
        return "login";
    }
}