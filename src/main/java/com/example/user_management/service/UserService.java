package com.example.user_management.service;

import com.example.user_management.dto.UserDTO;
import com.example.user_management.entity.User;
import com.example.user_management.mapper.UserMapper;
import com.example.user_management.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Autowired
    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Transactional
    public User registerUser(UserDTO userDTO) {
        // Map UserDTO to User entity, including password
        User user = userMapper.toUser(userDTO);

        // Save user to MongoDB (assuming password handling happens in the service or repo layer)
        return userRepository.save(user);
    }
}
