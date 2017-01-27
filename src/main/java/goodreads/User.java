package goodreads;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import java.util.List;

/**
 * Created by Patrick on 1/18/17.
 */
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Email(message = "Please enter a valid email!")
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank(message = "Please create a username!")
    @Column(nullable = false, unique = true)
    private String username;

    @NotBlank(message = "Please create a password!")
    @Column(nullable = false)
    private String password;

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    @Column(nullable = false)
    private String about;

    @Column(nullable = true)
    private String profile;

    @OneToMany(mappedBy = "user")
    private List<Post> posts;

    @OneToMany(mappedBy = "user")
    private List<Comment> comments;


    public User() {
    }

    public User(User user) {
        id = user.id;
        email = user.email;
        username = user.username;
        password = user.password;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAbout() { return about; }

    public void setAbout(String about) { this.about = about; }

    public String getProfile() { return profile; }

    public void setProfile(String profile) { this.profile = profile; }
}