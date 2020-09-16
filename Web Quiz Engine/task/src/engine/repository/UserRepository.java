package engine.repository;

import engine.model.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByEmail(String email);
    @Query("select u from User u where u.email = ?1 and u.password = ?2")
    User find(String email, String password);
}
