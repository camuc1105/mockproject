/**
 * @author Duong Van Luc 01/07/2000
 */
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
import fashion.mock.service.UserService;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/information")
public class CustomerInformationController {

    private final CustomerInformationService customerInformationService;
    private final UserService userService;

    public CustomerInformationController(CustomerInformationService customerInformationService, UserService userService) {
        this.customerInformationService = customerInformationService;
		this.userService = userService;
    }

    @GetMapping("")
    public String userProfile(HttpSession session, Model model) {
    	User user = (User) session.getAttribute("user");
		boolean isAdmin = false; // Initialize isAdmin

		if (user != null) {
			isAdmin = userService.isAdmin(user.getId());
			model.addAttribute("user", user);
		} else {
			return "redirect:/login/loginform";
		}
		model.addAttribute("isAdmin", isAdmin);
		
        Long userId = user.getId(); // Lấy userId từ đối tượng User trong session
        User user1 = customerInformationService.getUserById(userId);
        model.addAttribute("user", user1);
        return "customer-information";
    }

    @PostMapping("/change-password")
    @ResponseBody
    public String changePassword(HttpSession session, @RequestParam("oldPassword") String oldPassword,
            @RequestParam("newPassword") String newPassword,
            @RequestParam("confirmNewPassword") String confirmNewPassword) {
        // Lấy user từ session
        User sessionUser = (User) session.getAttribute("user");
        if (sessionUser == null) {
            return "Bạn cần đăng nhập để thực hiện chức năng này!";
        }

        Long userId = sessionUser.getId();

        if (!newPassword.equals(confirmNewPassword)) {
            return "Mật khẩu mới không khớp!";
        }

        boolean isPasswordChanged = customerInformationService.changePassword(userId, oldPassword, newPassword);
        if (!isPasswordChanged) {
            return "Mật khẩu cũ không đúng!";
        }

        return "Mật khẩu đã được thay đổi thành công!";
    }

    @PostMapping("/update-info")
    @ResponseBody
    public String updateUserInfo(HttpSession session, @RequestParam("field") String field,
            @RequestParam("value") String value) {
        // Retrieve the user from the session
        User sessionUser = (User) session.getAttribute("user");
        if (sessionUser == null) {
            return "Bạn cần đăng nhập để thực hiện chức năng này!";
        }

        Long userId = sessionUser.getId();
        User user = customerInformationService.getUserById(userId);

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

        // Update the user information in the database
        customerInformationService.updateUserInfo(user);

        // Update the user information in the session to reflect the changes
        session.setAttribute("user", user);

        return "Cập nhật thành công!";
    }
}