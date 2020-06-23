package engine.service;
import engine.model.User;
import engine.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public User authorizeUser(User user) {
        System.out.println("UserService");
        Optional<User> userOptional = Optional.ofNullable(userRepository.findByEmail(user.getEmail()));
        if (userOptional.isEmpty()) {
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            user.setRole("ROLE_USER");
            return userRepository.save(user);
        }
        return null;
    }
}
