package fashion.mock.controller;

import fashion.mock.model.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/home")
public class HomeController {


    @GetMapping
    public String home(Model model, HttpSession session){
       User user =(User) session.getAttribute("user");
       model.addAttribute("user", user);
      // this.setUser(user);
        return "home";
    }

}
