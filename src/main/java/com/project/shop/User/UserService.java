package com.project.shop.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Optional<User> getUserById(String id) {
        return userRepository.findById(id);
    }

    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public List<User> getUserByNickname(String nickname) {
        return userRepository.findByNickname(nickname);
    }

    public User registerUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public Optional<User> updateUser(String id, User user) {
        return userRepository.findById(id).map(existingUser -> {
            if (user.getUsername() != null) existingUser.setUsername(user.getUsername());
            if (user.getEmail() != null) existingUser.setEmail(user.getEmail());
            if (user.getNickname() != null) existingUser.setNickname(user.getNickname());
            if (user.getAge() != 0) existingUser.setAge(user.getAge());
            if (user.getPhoneNumber() != null) existingUser.setPhoneNumber(user.getPhoneNumber());
            if (user.getAddress() != null) existingUser.setAddress(user.getAddress());
            if (user.getGender() != null) existingUser.setGender(user.getGender());
            if (user.getBirthDate() != null) existingUser.setBirthDate(user.getBirthDate());
            if (user.getSkinType() != null) existingUser.setSkinType(user.getSkinType());
            if (user.getScalpType() != null) existingUser.setScalpType(user.getScalpType());
            if (user.getPassword() != null && !user.getPassword().isEmpty()) {
                existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
            }
            return userRepository.save(existingUser);
        });
    }

    public boolean deleteUser(String id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
}
