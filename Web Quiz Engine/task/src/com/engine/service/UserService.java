package com.engine.service;

import com.engine.mapper.UserMapper;
import com.engine.model.User;
import com.engine.repository.UserRepository;
import com.engine.dto.UserDto;
import com.engine.model.UserImpl;
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
    private final UserMapper userMapper;

    /**
     * UserService constructor
     *
     *
     * @param userRepository requests to DB
     * @param bCryptPasswordEncoder Implementation of PasswordEncoder that uses the BCrypt strong hashing function
     * @param userMapper maps UserDto object to User and vice versa
     */
    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userMapper = userMapper;
    }

    /**
     * @param userEmail user email
     * @return UserImpl object if user has been found by specified email or throw UsernameNotFoundException if user is
     * in DB with specified id
     */
    @Override
    public UserDetails loadUserByUsername(String userEmail) {
        Optional<User> userOptional = Optional.ofNullable(userRepository.findByEmail(userEmail));
        return userOptional.map(UserImpl::new)
                .orElseThrow(() -> new UsernameNotFoundException("Not found: " + userEmail));
    }

    /**
     * @param userDto  UserDto object
     * @return saved User object or null if a user in DB with specified id
     */
    public User registerUser(UserDto userDto) {
        Optional<User> userOptional = Optional.ofNullable(userRepository.findByEmail(userDto.getEmail()));
        if (userOptional.isEmpty()) {
            userDto.setPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
            User user = userMapper.toUser(userDto);
            return userRepository.save(user);
        }
        return null;
    }

    /**
     * @return a user object who sends requests
     */
    public User getCurrentUser() {
        UserImpl userImpl = (UserImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return User.builder()
                .id(userImpl.getId())
                .email(userImpl.getUsername())
                .password(userImpl.getPassword())
                .build();
    }
}