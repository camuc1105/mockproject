/**
 * Trần Thảo
 */
package fashion.mock.controller;

import fashion.mock.model.User;
import fashion.mock.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/login")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/loginform")
    public String login(Model model){
        System.out.println("===homebuoi");
        model.addAttribute("user", new User());
        return "login";
    }

    @PostMapping("/loginsuccess")
    public String loginSuccess(@Valid @ModelAttribute User user, BindingResult bindingResult, Model model, HttpSession session) throws AuthenticationException {
        System.out.println(user.getEmail());
        System.out.println("===");
        if(bindingResult.hasErrors()) {
            return "login";
        }
        if(!userService.getEmail(user.getEmail())){
            System.out.println("===1");
            model.addAttribute("errorEmail", "Email chưa được đăng ký!");
            return "login";
        }
        if(!userService.getPassword(user.getPassword())){
            System.out.println("===2");
            model.addAttribute("errorPassword", "Mật khẩu không chính xác!");
            return "login";
        }
        User user1 = userService.getStatus(user.getEmail());
        if(user1.getStatus() == null || !user1.getStatus().equalsIgnoreCase("ACTIVE")){
            System.out.println("===3");
            model.addAttribute("errorStatus", "Tài khoản này chưa được active!");
            return "login";
        }
        session.setAttribute("user", user1);
        return "redirect:/home";
    }

    @PostMapping("/logout")
    public String logout(HttpSession session){
        session.removeAttribute("user");
        return "redirect:/home";
    }

}
