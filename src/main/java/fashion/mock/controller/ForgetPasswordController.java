package fashion.mock.controller;

import fashion.mock.entities.User;
import fashion.mock.service.EmailService;
import fashion.mock.service.UserService;
import fashion.mock.service.VerificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ForgetPasswordController {
    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private VerificationService verificationService;

    // Hiển thị form nhập email để quên mật khẩu
    @GetMapping("/forgot-password")
    public String showForgotPasswordForm() {
        return "forgotPasswordForm";
    }

    // Xử lý khi người dùng nhập email
    @PostMapping("/forgot-password")
    public String handleForgotPassword(@RequestParam("email") String email, Model model) {
        if (userService.getByEmail(email) == null) {
            model.addAttribute("error", "Email không tồn tại!");
            return "forgotPasswordForm";
        }
        String code = verificationService.generateAndStoreCode(email);
        emailService.sendVerificationCode(email, code);
        return "redirect:/verify-code?email=" + email;
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
            return "verifyCodeForm";
        }
        return "redirect:/reset-password?email=" + email;
    }

    // Hiển thị form cập nhật mật khẩu mới
    @GetMapping("/reset-password")
    public String showResetPasswordForm(@RequestParam("email") String email, Model model, User user) {
        model.addAttribute("email", email);
        model.addAttribute("user", user);
        return "resetPasswordForm";
    }

    // Xử lý cập nhật mật khẩu
    @PostMapping("/reset-password")
    public String resetPassword(@RequestParam("email") String email,
                                @RequestParam("password") String password) {
        userService.updatePassword(email, password);
        return "index";
    }
}
