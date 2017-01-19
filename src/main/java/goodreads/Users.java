package goodreads;



import org.springframework.data.repository.CrudRepository;

/**
 * Created by Patrick on 1/18/17.
 */
public interface Users extends CrudRepository<User, Long> {
    public User findByUsername(String username);
}