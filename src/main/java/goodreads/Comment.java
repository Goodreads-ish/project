package goodreads;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.sql.Timestamp;

/**
 * Created by raylinares on 1/23/17.
 */
@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "Your reply can't be blank")
    @Size(min = 2, message = "Replies must be at least two characters")
    @Column(nullable = false, length = 2000)
    private String body;

    private boolean belongsTo;

    @ManyToOne
    private Post post;

    @ManyToOne
    private User user;

    @CreationTimestamp
    @Column(name = "created_date", nullable = false, updatable = false)
    private Timestamp createDate;

    public Comment(long id, String body){
        this.id = id;
        this.body = body;
    }
    public Comment(){

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Timestamp getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    public boolean isBelongsTo() {
        return belongsTo;
    }

    public void setBelongsTo(boolean belongsTo) {
        this.belongsTo = belongsTo;
    }
}
