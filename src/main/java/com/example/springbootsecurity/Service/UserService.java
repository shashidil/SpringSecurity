package com.example.springbootsecurity.Service;

import com.example.springbootsecurity.DTO.UserDto;
import com.example.springbootsecurity.Entity.User;
import com.example.springbootsecurity.Entity.UserVerificationToken;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.Optional;

public interface UserService {
    User saveUser(UserDto userDto);

    UserVerificationToken SaveVerificationTokenUser(User user, String token);

    String validateVerificationToken(String token);

    UserVerificationToken generateNewToken(String oldToken);

    User findByEmailId(String email);

    void createResetPasswordToken(User user, String token);

    String validateResetVerifyToken(String token);

    Optional<User> getUserByPasswordToken(String token);

    void saveNewPassword(User user, String newPassword);

    boolean checkOldPasswordValid(User user, String oldPassword);
}
