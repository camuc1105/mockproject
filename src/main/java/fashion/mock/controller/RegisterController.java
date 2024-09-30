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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
	public String verifyCode(@RequestParam("email") String email, @RequestParam("code") String code, Model model,
							 RedirectAttributes redirectAttributes) {
		boolean isValid = verificationService.validateCode(email, code);
		if (isValid) {
			User user = userService.getByEmail(email);
			userService.activeUser(user);
			return "VerifySuccess"; // Redirect to login page if verification is successful
		} else {
			model.addAttribute("codeError", "Mã xác minh không đúng hoặc đã hết hạn!");
			model.addAttribute("email", email);
			return "inputCodeVerify";
		}
	}
	@GetMapping("/login/loginform")
	public String getLoginPage(Model model, @ModelAttribute("successMessage") String successMessage) {
		if (!successMessage.isEmpty()) {
			model.addAttribute("successMessage", successMessage);
		}
		return "login";
	}

	@PostMapping("/reVeryfi")
	public String getInputCode(@RequestParam String email, Model model) {
		// Lấy thông tin người dùng qua email
		User user = userService.getByEmail(email);

		// Kiểm tra nếu user tồn tại
		if (user != null) {
			model.addAttribute("email", user.getEmail());
			String code = verificationService.generateAndStoreCode(user.getEmail());
			emailService.sendVerificationCode(user.getEmail(), code);
			return "redirect:/register/inputCode?email=" + user.getEmail();
		}

		// Nếu email không tồn tại, quay lại trang đăng nhập với thông báo lỗi
		model.addAttribute("errorStatus", "Email không tồn tại");
		return "login";  // Trả về trang login kèm lỗi
	}
}
