package engine.repository;

import engine.model.CompletedQuiz;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import java.util.List;

public interface CompletedQuizRepository extends PagingAndSortingRepository<CompletedQuiz, Long> {
    List<CompletedQuiz> findAllByUserId(long id);
    Page<CompletedQuiz> findAllByUserId(long userId, Pageable pageable);
}
