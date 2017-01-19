package goodreads;



import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by Patrick on 1/5/17.
 */
public interface Posts extends CrudRepository<Post, Long>{

    List<Post> findByUserId(long id);
}
