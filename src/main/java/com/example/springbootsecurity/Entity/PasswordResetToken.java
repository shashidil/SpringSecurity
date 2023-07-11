package com.example.springbootsecurity.Entity;

import jakarta.persistence.*;
import lombok.*;


import java.util.Calendar;
import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PasswordResetToken {
    private static final int expiration_Time= 10;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String token;
    private Date ExpirationTime;

    @OneToOne(
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id",foreignKey = @ForeignKey(name = "FK_password_reset_token"),nullable = false)
    private User user;

    public PasswordResetToken(String token, User user) {
        super();
        this.token = token;
        ExpirationTime = CalculateExpirationTime(expiration_Time);
        this.user = user;
    }
    public PasswordResetToken(String token){
        super();
        this.token= token;
        ExpirationTime = CalculateExpirationTime(expiration_Time);
    }

    public Date CalculateExpirationTime(int expirationTime){
        Calendar calendar =Calendar.getInstance();
        calendar.setTimeInMillis(new Date().getTime());
        calendar.add(calendar.MINUTE ,expirationTime);
        return new Date(calendar.getTime().getTime());

    }
}
