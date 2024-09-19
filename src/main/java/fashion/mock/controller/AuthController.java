/**
 * Trần Thảo
 */
package fashion.mock.controller;

import fashion.mock.model.User;
import fashion.mock.service.UserService;
import jakarta.validation.Valid;
import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String login(Model model){
        model.addAttribute("user", new User());
        return "login";
    }

    @PostMapping("/loginsuccess")
    public String loginSuccess(@Valid @ModelAttribute User user, BindingResult bindingResult, Model model) throws AuthenticationException {
        if(bindingResult.hasErrors()) {
            return "/login";
        }
        if(!userService.getEmail(user.getEmail())){
            model.addAttribute("error", "Email chưa được đăng ký!");
            return "/login";
        }
        if(!userService.getPassword(user.getPassword())){
            model.addAttribute("error", "Mật khẩu không chính xác!");
            return "/login";
        }
        return "home";
    }
    @GetMapping("/home")
    public String home(){
        return "home";
    }

}
