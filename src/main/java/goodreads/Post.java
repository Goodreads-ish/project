package goodreads;



import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.sql.Timestamp;
import java.util.List;

@Entity
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    private User user;

    @NotBlank(message = "Title can't be empty")
    @Size(min = 3, message = "Title must be at least 3 characters")
    @Column(nullable = false)
    private String title;

    @NotBlank(message = "Body can't be empty")
    @Column(nullable = false, length = 1000)
    private String body;

    @Column
    private String isbn;


    @CreationTimestamp
    @Column(name = "create_date", nullable = false, updatable = false)
    private Timestamp createDate;

    @OneToMany(mappedBy = "post")
    private List<Comment> comments;


    public Post(int id, String title, String body, String isbn) {
        this.id = id;
        this.title = title;
        this.body = body;
        this.isbn = isbn;
    }

    public Post() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getUser() { return user;}

    public void setUser(User user) { this.user = user;}

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Timestamp getCreateDate() { return createDate; }

    public void setCreateDate(Timestamp createDate) { this.createDate = createDate; }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }


}
