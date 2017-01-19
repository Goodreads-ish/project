package goodreads;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by Patrick on 1/5/17.
 */
@Controller
@RequestMapping("/posts")
public class PostsController extends BaseController {

    @Autowired
    private Posts postsDao;

    @GetMapping
    public String index(Model model) {
        List<Post> posts = new ArrayList((Collection) postsDao.findAll());
        Collections.reverse(posts);
        model.addAttribute("posts", posts);
        return "posts/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable long id, Model model) {
        Post post = postsDao.findOne(id);
        model.addAttribute("post", post);
        return "posts/view";
    }

    @GetMapping("/create")
    public String showForm(Model model) {
        model.addAttribute("post", new Post());
        return "posts/create";
    }

    @PostMapping("/create")
    public String create(@Valid Post post, Errors validation, Model model) {
        if (validation.hasErrors()) {
            System.out.println(validation.getAllErrors().get(0));
            model.addAttribute("errors", validation);
            model.addAttribute("post", post);
            return "posts/create";
        }
        post.setUser(loggedInUser());
        postsDao.save(post);
        return "redirect:/posts";
    }

    @GetMapping("/{id}/edit")
    public String showEdit(@PathVariable long id, Model model) {
        Post post = postsDao.findOne(id);
        model.addAttribute("post", post);
        return "posts/edit";
    }

    @PostMapping("/{id}/edit")
    public String update(@Valid Post editedPost, Errors validation, Model model) {
        if (validation.hasErrors()) {
            model.addAttribute("errors", validation);
            model.addAttribute("post", editedPost);
            return "posts/edit";
        }
        Post exisitingPost = postsDao.findOne(editedPost.getId());
        String newTitle = editedPost.getTitle();
        String newBody = editedPost.getBody();
        exisitingPost.setTitle(newTitle);
        exisitingPost.setBody(newBody);
        exisitingPost.setUser(loggedInUser());
        postsDao.save(exisitingPost);
        return "redirect:/posts/" + exisitingPost.getId();
    }

    @GetMapping("/{id}/delete")
    public String delete(@PathVariable long id) {
        postsDao.delete(id);
        return "redirect:/posts";
    }
}
