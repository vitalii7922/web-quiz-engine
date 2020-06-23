package engine.service;

import engine.model.User;
import engine.model.UserDetailImpl;
import engine.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String userEmail) {
        Optional<User> userOptional = Optional.ofNullable(userRepository.findByEmail(userEmail));
//        System.out.println("userDetailService" + userOptional.get());
        return userOptional.map(UserDetailImpl::new)
                .orElseThrow(() -> new UsernameNotFoundException("Not found: " + userEmail));
    }
}
