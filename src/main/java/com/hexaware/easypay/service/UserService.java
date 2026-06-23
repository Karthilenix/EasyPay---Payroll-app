package com.hexaware.easypay.service;

import java.util.List;

import com.hexaware.easypay.dto.UserDTO;
import com.hexaware.easypay.entity.User;

public interface UserService {

    User addUser(UserDTO dto);

    User getUserById(Long userId);

    List<User> getAllUsers();

    User updateUser(Long userId,
                    UserDTO dto);

    void disableUser(Long userId);
}