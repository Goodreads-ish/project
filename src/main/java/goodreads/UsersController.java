package goodreads;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.*;
import java.util.Collections;
import java.util.List;

/**
 * Created by Patrick on 1/18/17.
 */

@Controller
public class UsersController extends BaseController{

    @Autowired
    Users usersDao;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private Posts postsDao;

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("loggedInUser", loggedInUser());
        return "posts/register";
    }

    @PostMapping("/register")
    public String createUser(@Valid User user, Errors error, Model model, @RequestParam(name = "profile") String profile) {

        if(error.hasErrors()) {
            model.addAttribute("errors", error);
            model.addAttribute("user", user);
            model.addAttribute("loggedInUser", loggedInUser());
            return "posts/register";
        }
        user.setProfile(profile);
        String plaintextPassword = user.getPassword();
        String encryptedPassword = passwordEncoder.encode(plaintextPassword);
        user.setPassword(encryptedPassword);
        usersDao.save(user);
        return "redirect:/login";
    }

    @GetMapping("/users/{id}")
    public String showPostsByUser(@PathVariable long id, Model model) {
        List<Post> posts = postsDao.findByUserId(id);
        Collections.reverse(posts);
        User user = usersDao.findOne(id);
        model.addAttribute("posts", posts);
        model.addAttribute("user", user);
        model.addAttribute("loggedInUser", loggedInUser());
        return "posts/profile";
    }
}
