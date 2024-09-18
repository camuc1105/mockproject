/**
* Author: Nguyễn Viết Hoàng Phúc 22/11/1997
*/
package fashion.mock.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import fashion.mock.model.User;
import fashion.mock.service.EmailService;
import fashion.mock.service.UserService;
import fashion.mock.service.VerificationService;

@Controller
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

	@PostMapping("/resendCodeForget")
	public String resendCode(@ModelAttribute User user) {
		String code = verificationService.generateAndStoreCode(user.getEmail());
		emailService.sendVerificationCode(user.getEmail(), code);
		return "redirect:/reset-password?email=" + user.getEmail();
	}

	// Xử lý cập nhật mật khẩu
	@PostMapping("/reset-password")
	public String resetPassword(@RequestParam("email") String email, @RequestParam("password") String password) {
		userService.updatePassword(userService.getByEmail(email), password);
		return "index";
	}
}
