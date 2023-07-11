package com.example.springbootsecurity.Event;

import com.example.springbootsecurity.Entity.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;
import org.springframework.stereotype.Service;

import java.time.Clock;
@Getter
@Setter
public class UserRegToken extends ApplicationEvent {
    private User user;
    private String url;
    public UserRegToken(User user,String url) {
        super(user);
        this.user=user;
        this.url=url;
    }


}
