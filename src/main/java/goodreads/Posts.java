package goodreads;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * Created by Patrick on 1/5/17.
 */
public interface Posts extends PagingAndSortingRepository<Post, Long> {

    List<Post> findByUserId(long id);

    Page<Post> findAll(Pageable pageable);
}
