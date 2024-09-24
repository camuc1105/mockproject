/**
* Author: Nguyễn Viết Hoàng Phúc 22/11/1997
*/
package fashion.mock.controller;

import fashion.mock.service.EmailService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import fashion.mock.model.User;
import fashion.mock.service.UserService;
import fashion.mock.service.VerificationService;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/register")
public class RegisterController {
	private final UserService userService;
	private final EmailService emailService;
	private final VerificationService verificationService;

	public RegisterController(UserService userService, EmailService emailService,
			VerificationService verificationService) {
		this.userService = userService;
		this.emailService = emailService;
		this.verificationService = verificationService;
	}

	@GetMapping("/register")
	public String showRegisterForm(Model model) {
		model.addAttribute("user", new User());
		return "RegisterForm";
	}

	@PostMapping("/save")
	public String saveUser(@Valid @ModelAttribute User user, BindingResult bindingResult, Model model) {
		if (bindingResult.hasErrors()) {
			model.addAttribute("user", user); // Truyền lại đối tượng user để hiển thị
			return "RegisterForm";
		}
		// Kiểm tra email đã tồn tại
		if (userService.checkEmail(user.getEmail())) {
			model.addAttribute("emailError", "Email đã tồn tại trong hệ thống. Vui lòng nhập email khác.");
			return "RegisterForm";
		} else {
			userService.createUser(user);
			String code = verificationService.generateAndStoreCode(user.getEmail());
			emailService.sendVerificationCode(user.getEmail(), code);
			return "redirect:/register/inputCode?email=" + user.getEmail();
		}
	}

	@PostMapping("/resendCode")
	public String resendCode(@ModelAttribute User user) {
		String code = verificationService.generateAndStoreCode(user.getEmail());
		emailService.sendVerificationCode(user.getEmail(), code);
		return "redirect:/register/inputCode";
	}

	@GetMapping("/inputCode")
	public String formInputCode(@RequestParam("email") String email, Model model) {
		model.addAttribute("email", email);
		return "inputCodeVerify";
	}

	@PostMapping("/verify")
	public String verifyCode(@RequestParam("email") String email, @RequestParam("code") String code, Model model) {
		boolean isValid = verificationService.validateCode(email, code);
		if (isValid) {
			User user = userService.getByEmail(email);
			userService.activeUser(user);
			return "redirect:/login"; // Redirect to login page if verification is successful
		} else {
			model.addAttribute("codeError", "Mã xác minh không đúng hoặc đã hết hạn!");
			model.addAttribute("email", email);
			return "inputCodeVerify";
		}
	}
}
