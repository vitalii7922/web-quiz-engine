package com.engine.model;

import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;

@Getter
public class UserImpl extends org.springframework.security.core.userdetails.User {

    private final long id;

    public UserImpl(User user) {
        super(user.getEmail(), user.getPassword(), Collections.singletonList(new SimpleGrantedAuthority("USER")));
        this.id = user.getId();
    }

    @Override
    public boolean equals(Object rhs) {
        return super.equals(rhs);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
