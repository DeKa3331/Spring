package com.umcsuser.car_rent.service;

import com.umcsuser.car_rent.dto.UserRequest;
import com.umcsuser.car_rent.models.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    void register(UserRequest req);
    Optional<User> findByLogin(String login);
    void softDeleteUser(String id);
    List<User> getAllUsers();
    void addRoleToUser(String id, String roleName);
    void removeRoleFromUser(String id, String roleName);
}
