package goodreads;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

/**
 * Created by raylinares on 1/24/17.
 */
@Controller
public class CommentsController extends BaseController {

    @Autowired
    private Posts postsDao;

    @Autowired
    private Comments commentsDao;

    @GetMapping("/posts/{id}/comment")
    public String showCommentForm(@PathVariable long id, Model model) {
        Post post = postsDao.findOne(id);
        model.addAttribute("post", post);
        model.addAttribute("loggedInUser", loggedInUser());
        model.addAttribute("postBelongsToUser", postBelongsToUser(post));
        model.addAttribute("comment", new Comment());
        return "posts/comment";
    }

    @PostMapping("/posts/{id}/comment")
    public String creatComment(@Valid Comment comment, Errors validation, Model model, @PathVariable long id){
        if (validation.hasErrors()){
            System.out.println(validation.getAllErrors().get(0));
            Post post = postsDao.findOne(id);
            model.addAttribute("post", post);
            model.addAttribute("errors", validation);
            model.addAttribute("comment", comment);
            model.addAttribute("loggedInUser", loggedInUser());
            return "posts/comment";
        }

        //System.out.println(comment.getId());
        Post post = postsDao.findOne(id);
        Comment newComment = new Comment();
        newComment.setBody(comment.getBody());
        newComment.setPost(post);
        newComment.setUser(loggedInUser());
        commentsDao.save(newComment);
        return "redirect:/posts/" + id;
    }
    @GetMapping("/comments/{id}/delete")
    public String deleteComment(@PathVariable long id){
        commentsDao.delete(id);
        return "redirect:/posts/";
    }
}
