package com.oneenterprise.userservice.service;

import com.oneenterprise.userservice.dto.UserResponse;
import com.oneenterprise.userservice.exception.UserNotFoundException;
import com.oneenterprise.userservice.model.User;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Service
public class UserService {

    private final Map<Long, User> users = new ConcurrentHashMap<>();

    public UserService() {
        users.put(1001L, new User(1001L, "John Carter", "john@example.com"));
        users.put(1002L, new User(1002L, "Priya Sharma", "priya@example.com"));
        users.put(1003L, new User(1003L, "Wei Zhang", "wei@example.com"));
    }

    public UserResponse getUser(Long id) {
        User user = users.get(id);
        if (user == null) {
            throw new UserNotFoundException(id);
        }
        return new UserResponse(user.getId(), user.getName(), user.getEmail());
    }
}
