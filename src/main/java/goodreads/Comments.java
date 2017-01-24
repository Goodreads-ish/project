package goodreads;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by raylinares on 1/23/17.
 */
public interface Comments extends CrudRepository<Comment, Long> {
    List<Comment> findByUserId(long id);
    List<Comment> findByPostId(long id);
}
