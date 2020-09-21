package engine.repository;

import engine.model.Quiz;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface QuizRepository extends PagingAndSortingRepository<Quiz, Long> {
    Quiz findById(long id);

    List<Quiz> findAll();

    @Query("SELECT COUNT(q) FROM Quiz q")
    long countAll();

    Page<Quiz> findAll(Pageable pageable);

    @Modifying
    @Transactional
    @Query("update Quiz q set q.options = :options, q.answer = :answer where q.id = :id")
    void updateById(@Param(value = "id") long id, @Param(value = "options") List<String> options,
                    @Param(value = "answer") List<Integer> answer);
}
