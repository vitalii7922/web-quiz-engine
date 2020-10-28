package com.engine.mapper;

import com.engine.dto.UserDto;
import com.engine.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    /**
     * map User object to UserDto object
     *
     * @param user User object
     * @return UserDto object
     */
    public UserDto toUserDto(User user) {
        return UserDto.builder()
                .email(user.getEmail())
                .password(user.getPassword())
                .build();
    }

    /**
     * map UserDto object to User object
     *
     * @param userDto UserDto object
     * @return User object
     */
    public User toUser(UserDto userDto) {
        return User.builder()
                .email(userDto.getEmail())
                .password(userDto.getPassword())
                .build();
    }
}
