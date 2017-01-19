package goodreads;



import org.springframework.data.repository.CrudRepository;

/**
 * Created by Patrick on 1/5/17.
 */
public interface Posts extends CrudRepository<Post, Long>{
}
