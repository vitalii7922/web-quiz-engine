package engine.service;
import engine.model.User;
import engine.model.UserImpl;
import engine.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;


@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String userEmail) {
        Optional<User> userOptional = Optional.ofNullable(userRepository.findByEmail(userEmail));
        return userOptional.map(UserImpl::new)
                .orElseThrow(() -> new UsernameNotFoundException("Not found: " + userEmail));
    }

    public User registerUser(User user) {
        Optional<User> userOptional = Optional.ofNullable(userRepository.findByEmail(user.getEmail()));
        if (userOptional.isEmpty()) {
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            return userRepository.save(user);
        }
        return null;
    }

    public UserImpl getUserImpl() {
        return (UserImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }


}
