package com.example.springbootsecurity.Event.listener;

import com.example.springbootsecurity.Entity.User;
import com.example.springbootsecurity.Event.UserRegToken;
import com.example.springbootsecurity.Service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.UUID;
@Component
@Slf4j
public class UserRegTokenListener implements ApplicationListener<UserRegToken> {

    @Autowired
    private UserService userService;
    @Override
    public void onApplicationEvent(UserRegToken event) {
        //create verification token and link
        User user= event.getUser();
        String token = UUID.randomUUID().toString();
        userService.SaveVerificationTokenUser( user, token );

        //send email

        String url = event.getUrl()+"/verifyToken?token="+token;
        //sendVerificationEmail()
        log.info("Click the link to verify your account: {}",
                url);
    }
}
