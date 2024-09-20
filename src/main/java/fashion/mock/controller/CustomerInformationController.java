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

@Controller
@RequestMapping("/information")
public class CustomerInformationController {

    private final CustomerInformationService customerInformationService;

    public CustomerInformationController(CustomerInformationService customerInformationService) {
        this.customerInformationService = customerInformationService;
    }

    @GetMapping("")
    public String userProfile(Model model) {
        User user = customerInformationService.getUserById(2L); 
        model.addAttribute("user", user);
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

        boolean isPasswordChanged = customerInformationService.changePassword(2L, oldPassword, newPassword);
        if (!isPasswordChanged) {
            return "Mật khẩu cũ không đúng!";
        }

        return "Mật khẩu đã được thay đổi thành công!";
    }

    @PostMapping("/update-info")
    @ResponseBody
    public String updateUserInfo(@RequestParam("field") String field,
                                 @RequestParam("value") String value) {

        User user = customerInformationService.getUserById(2L);

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
        customerInformationService.updateUserInfo(user);

        return "Cập nhật thành công!";
    }

}