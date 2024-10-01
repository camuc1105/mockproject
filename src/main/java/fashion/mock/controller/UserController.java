	/**
	 * Author: Ngô Văn Quốc Thắng 11/05/1996
	 */
package fashion.mock.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import fashion.mock.model.User;
import fashion.mock.service.RoleService;
import fashion.mock.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/users")
public class UserController {


    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;
    
    private boolean checkAdminAccess(HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("user");
        if (user == null || !userService.isAdmin(user.getId())) {
            redirectAttributes.addFlashAttribute("errorMessage", "Bạn không có quyền truy cập trang này.");
            return false;
        }
        model.addAttribute("user", user);
        model.addAttribute("isAdmin", true);
        return true;
    }
   
	/**
	 * Author: Ngô Văn Quốc Thắng 11/05/1996
	 */
    @GetMapping
    public String listUsers(Model model, HttpSession session, RedirectAttributes redirectAttributes,  
                            @RequestParam(defaultValue = "0") int page, 
                            @RequestParam(defaultValue = "5") int size) {
    	 if (!checkAdminAccess(session, model, redirectAttributes)) {
    		 return "403";
         }
        
        Page<User> userPage = userService.getAllUsers(PageRequest.of(page, size));
        model.addAttribute("users", userPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", userPage.getTotalPages());
        model.addAttribute("totalItems", userPage.getTotalElements());
        return "adminlistuser";
    }

	/**
	 * Author: Ngô Văn Quốc Thắng 11/05/1996
	 */
    @GetMapping("/new")
    public String showAddUserForm(Model model, HttpSession session,RedirectAttributes redirectAttributes) {
        if (!checkAdminAccess(session, model, redirectAttributes)) {
        	 return "403";
        }        
        model.addAttribute("user1", new User());
        model.addAttribute("allRoles", roleService.getAllRoles());
        return "adminformuser";
    }

	/**
	 * Author: Ngô Văn Quốc Thắng 11/05/1996
	 */
    @GetMapping("/edit/{id}")
    public String showUpdateUserForm(@PathVariable Long id, Model model, HttpSession session,RedirectAttributes redirectAttributes) {
        if (!checkAdminAccess(session, model, redirectAttributes)) {
        	 return "403";
        }        
        
        User users = userService.getUserById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));
        model.addAttribute("user1", users);
        model.addAttribute("allRoles", roleService.getAllRoles());
        
        // Lấy danh sách các roleId đã chọn của user
        List<Long> selectedRoleIds = users.getUserRoles().stream()
                .map(userRole -> userRole.getRole().getId())
                .collect(Collectors.toList());
        model.addAttribute("selectedRoleIds", selectedRoleIds);
        
        model.addAttribute("selectedStatus", users.getStatus());
        return "adminformuser";
    }

	/**
	 * Author: Ngô Văn Quốc Thắng 11/05/1996
	 */
    @PostMapping
    public String addUser(@Valid @ModelAttribute User user, BindingResult bindingResult, 
                          @RequestParam(required = false) List<Long> roleIds, 
                          RedirectAttributes redirectAttributes,
                          Model model) {
        if (bindingResult.hasErrors() || roleIds == null || roleIds.isEmpty()) {
            if (roleIds == null || roleIds.isEmpty()) {
                model.addAttribute("roleError", "Vui lòng chọn ít nhất một vai trò.");
            }
            model.addAttribute("allRoles", roleService.getAllRoles());
            return "adminformuser";
        }

        try {
            userService.addUserWithRoles(user, roleIds);
            redirectAttributes.addFlashAttribute("successMessage", "Người dùng đã được thêm thành công!");
            return "redirect:/users";
        } catch (IllegalArgumentException e) {
            bindingResult.rejectValue("email", "error.user", e.getMessage());
            model.addAttribute("allRoles", roleService.getAllRoles());
            return "adminformuser";
        }
    }

    @PostMapping("/update")
    public String updateUser(@Valid @ModelAttribute User user, BindingResult bindingResult,
                             @RequestParam(required = false) List<Long> roleIds, 
                             RedirectAttributes redirectAttributes,
                             Model model) {
        // Lấy người dùng hiện có từ cơ sở dữ liệu
        User existingUser = userService.getUserById(user.getId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

        // Đặt email và mật khẩu về giá trị hiện có
        user.setEmail(existingUser.getEmail());
//        user.setPassword(existingUser.getPassword());
//
//        // Loại bỏ lỗi email và mật khẩu khỏi BindingResult
//        List<FieldError> errorsToKeep = bindingResult.getFieldErrors().stream()
//                .filter(fe -> !fe.getField().equals("email") && !fe.getField().equals("password"))
//                .collect(Collectors.toList());
//        bindingResult = new BeanPropertyBindingResult(user, "user");
//        for (FieldError error : errorsToKeep) {
//            bindingResult.addError(error);
//        }

        if (bindingResult.hasErrors() || roleIds == null || roleIds.isEmpty()) {
            if (roleIds == null || roleIds.isEmpty()) {
                model.addAttribute("roleError", "Vui lòng chọn ít nhất một vai trò.");
            }
            model.addAttribute("allRoles", roleService.getAllRoles());
            return "adminformuser";
        }

        try {
            userService.updateUserWithRoles(user, roleIds);
            redirectAttributes.addFlashAttribute("successMessage", "Cập nhật người dùng thành công!");
            return "redirect:/users";
        } catch (IllegalArgumentException e) {
            bindingResult.rejectValue("email", "error.user", e.getMessage());
            model.addAttribute("allRoles", roleService.getAllRoles());
            return "adminformuser";
        }
    }
    
	/**
	 * Author: Ngô Văn Quốc Thắng 11/05/1996
	 */
    // Xóa category
    @GetMapping("/delete/{id}")
    public String deleteCategory(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        boolean deleted = userService.deleteUser(id);
        if (!deleted) {
            redirectAttributes.addFlashAttribute("message", "Người dùng không tồn tại");
            redirectAttributes.addFlashAttribute("messageType", "error");
        } else {
            redirectAttributes.addFlashAttribute("message", "Người dùng đã được xóa thành công");
            redirectAttributes.addFlashAttribute("messageType", "success");
        }
        return "redirect:/users";
    } 
    
	/**
	 * Author: Ngô Văn Quốc Thắng 11/05/1996
	 */ 
    @GetMapping("/search")
    public String searchUsers(@RequestParam(value = "searchTerm", required = false) String searchTerm, 
                              @RequestParam(defaultValue = "0") int page, 
                              @RequestParam(defaultValue = "5") int size,
                              Model model, HttpSession session,RedirectAttributes redirectAttributes) {
        if (!checkAdminAccess(session, model, redirectAttributes)) {
        	 return "403";
        }        
        Page<User> userPage = userService.searchUsers(searchTerm, PageRequest.of(page, size));
        model.addAttribute("users", userPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", userPage.getTotalPages());
        model.addAttribute("totalItems", userPage.getTotalElements());
        model.addAttribute("searchTerm", searchTerm);
        return "adminlistuser";
    }
}