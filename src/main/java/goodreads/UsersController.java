package goodreads;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.validation.Errors;

import javax.validation.*;

/**
 * Created by Patrick on 1/18/17.
 */

@Controller
public class UsersController extends BaseController{

    @Autowired
    Users usersDao;

    @Autowired
    PasswordEncoder passwordEncoder;

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User());
        return "posts/register";
    }

    @PostMapping("/register")
    public String createUser(@Valid User user, Errors error, Model model) {

        if(error.hasErrors()) {
            model.addAttribute("errors", error);
            model.addAttribute("user", user);
            return "posts/register";
        }
        String plaintextPassword = user.getPassword();
        String encryptedPassword = passwordEncoder.encode(plaintextPassword);
        user.setPassword(encryptedPassword);
        usersDao.save(user);
        return "redirect:/login";
    }
}
