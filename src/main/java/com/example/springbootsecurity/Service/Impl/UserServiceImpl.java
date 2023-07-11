package com.example.springbootsecurity.Service.Impl;

import com.example.springbootsecurity.DTO.UserDto;
import com.example.springbootsecurity.Entity.PasswordResetToken;
import com.example.springbootsecurity.Entity.User;
import com.example.springbootsecurity.Entity.UserVerificationToken;
import com.example.springbootsecurity.Repository.PasswordResetRepository;
import com.example.springbootsecurity.Repository.UserRepository;
import com.example.springbootsecurity.Repository.UserVerficationTokenRepository;
import com.example.springbootsecurity.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.Calendar;
import java.util.Optional;
import java.util.UUID;
@Service

public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserVerficationTokenRepository userVerficationTokenRepository;

    @Autowired
    private PasswordResetRepository passwordResetRepository;
    @Override
    public User saveUser(UserDto userDto) {
        User user= new User();
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setRole("USER");
        userRepository.save(user);
        return user;
    }

    @Override
    public UserVerificationToken SaveVerificationTokenUser(User user, String token) {
        UserVerificationToken userVerificationToken = new UserVerificationToken(token,user);
        userVerficationTokenRepository.save(userVerificationToken);
        return userVerificationToken;

    }

    @Override
    public String validateVerificationToken(String token) {
        UserVerificationToken userVerificationToken = userVerficationTokenRepository.findByToken(token);
        if (userVerificationToken==null){
            return "Invalid";
        }
        User user = userVerificationToken.getUser();
        Calendar calendar = Calendar.getInstance();
        if (userVerificationToken.getExpirationTime().getTime()-calendar.getTime().getTime() <=0) {
            userVerficationTokenRepository.delete(userVerificationToken);
            return "Expired";
        }
        user.setEnable(true);
        userVerficationTokenRepository.save(userVerificationToken);
        return "Valid";
    }

    @Override
    public UserVerificationToken generateNewToken(String oldToken) {
        UserVerificationToken userVerificationToken = userVerficationTokenRepository.findByToken(oldToken);
        userVerificationToken.setToken(UUID.randomUUID().toString());
        userVerficationTokenRepository.save(userVerificationToken);
        return userVerificationToken;
    }

    @Override
    public User findByEmailId(String email) {
       User user = userRepository.findByEmail(email);
       return user;
    }

    @Override
    public void createResetPasswordToken(User user, String token) {
        PasswordResetToken passwordResetToken = new PasswordResetToken(token,user);
        passwordResetRepository.save(passwordResetToken);

    }

    @Override
    public String validateResetVerifyToken(String token) {
        PasswordResetToken passwordResetToken = passwordResetRepository.findByToken(token);
        if (passwordResetToken==null){
            return "Invalid";
        }
        User user = passwordResetToken.getUser();
        Calendar calendar = Calendar.getInstance();
        if (passwordResetToken.getExpirationTime().getTime()-calendar.getTime().getTime() <=0) {
            passwordResetRepository.delete(passwordResetToken);
            return "Expired";
        }

        return "Valid";
    }

    @Override
    public Optional<User> getUserByPasswordToken(String token) {
        return Optional.ofNullable(passwordResetRepository.findByToken(token).getUser());
    }

    @Override
    public void saveNewPassword(User user, String newPassword) {
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Override
    public boolean checkOldPasswordValid(User user, String oldPassword) {
        return passwordEncoder.matches(oldPassword, user.getPassword());
    }


}

