package engine.repository;

import engine.model.Quiz;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface QuizRepository extends PagingAndSortingRepository<Quiz, Long> {
    Quiz findById(long id);
    List<Quiz> findAll();
    Page<Quiz> findAll(Pageable pageable);
}
