package engine.model;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.util.Collections;

public class UserImpl extends org.springframework.security.core.userdetails.User {

    private final long id;

    public UserImpl(User user) {
        super(user.getEmail(), user.getPassword(), Collections.singletonList(new SimpleGrantedAuthority("USER")));
        this.id = user.getId();
    }

    public long getId() {
        return id;
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
