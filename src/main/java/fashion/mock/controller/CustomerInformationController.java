package fashion.mock.controller;

import java.time.LocalDate;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import fashion.mock.model.User;
import fashion.mock.service.CustomerInformationService;

/**
 * @author Duong Van Luc 01/07/2000
 */

@Controller
@RequestMapping("/information")
public class CustomerInformationController {

	private final CustomerInformationService customerInformationService;

	public CustomerInformationController(CustomerInformationService customerInformationService) {
		this.customerInformationService = customerInformationService;
	}

	@GetMapping("")
	public String userProfile(Model model) {
		// Giả sử lấy thông tin người dùng với ID cố định (hoặc có thể từ session)
		User user = customerInformationService.getUserById(1L);
		model.addAttribute("user", user);
		System.out.println(user);
		return "customer-information";
	}

	@PostMapping("/change-password")
	@ResponseBody
	public String changePassword(@RequestParam("oldPassword") String oldPassword,
			@RequestParam("newPassword") String newPassword,
			@RequestParam("confirmNewPassword") String confirmNewPassword) {
		if (!newPassword.equals(confirmNewPassword)) {
			return "Mật khẩu mới không khớp!";
		}

		boolean isPasswordChanged = customerInformationService.changePassword(1L, oldPassword, newPassword);
		if (!isPasswordChanged) {
			return "Mật khẩu cũ không đúng!";
		}

		return "Mật khẩu đã được thay đổi thành công!";
	}

	// Cập nhật thông tin người dùng (tên, địa chỉ, số điện thoại)
	@PostMapping("/update-info")
	@ResponseBody
	public String updateUserInfo(@RequestParam("field") String field, @RequestParam("value") String value) {
		// Xử lý cập nhật theo từng trường (field)
		User user = customerInformationService.getUserById(1L); // Giả sử lấy người dùng với ID cố định

		switch (field) {
		case "name":
			user.setUserName(value);
			break;
		case "phone":
			user.setPhone(value);
			break;
		case "address":
			user.setAddress(value);
			break;
		default:
			return "Trường thông tin không hợp lệ!";
		}

		user.setUpdatedDate(LocalDate.now());
		// Cập nhật thông tin người dùng
		customerInformationService.updateUserInfo(user);

		return "Cập nhật thành công!";
	}

}