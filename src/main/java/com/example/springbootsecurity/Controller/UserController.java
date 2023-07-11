package com.example.springbootsecurity.Controller;

import com.example.springbootsecurity.DTO.PasswordDto;
import com.example.springbootsecurity.DTO.UserDto;
import com.example.springbootsecurity.Entity.PasswordResetToken;
import com.example.springbootsecurity.Entity.User;
import com.example.springbootsecurity.Entity.UserVerificationToken;
import com.example.springbootsecurity.Event.UserRegToken;
import com.example.springbootsecurity.Service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.*;


import java.util.Optional;
import java.util.UUID;

@RestController
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;



    @Autowired
    private ApplicationEventPublisher publisher;

    @PostMapping("/register")
    public String userRegistration(@RequestBody UserDto userDto, HttpServletRequest request){
        User user = userService.saveUser(userDto);
        publisher.publishEvent(new UserRegToken(user,applicationUrl(request)));

        return "User Registration Success";

    }
    @GetMapping("/verifyToken")
    public String useRegistrationConfirm(@RequestParam("token") String token){
        String result = userService.validateVerificationToken(token);
        if (result.equalsIgnoreCase("valid")){
            return "User Verification Success";
        }
        return "User Bad";

    }
    @GetMapping("/resendVerifyToken")
    public String resendVerifyToken(@RequestParam("token") String oldToken,HttpServletRequest request){
        UserVerificationToken userVerificationToken = userService.generateNewToken(oldToken);
        User user =userVerificationToken.getUser();
        resendVerifyTokenEmail( user,applicationUrl(request),userVerificationToken);
        return "Verification link send";
    }

    @PostMapping("/resetPassword")
    public  String resetPassword (@RequestBody PasswordDto passwordDto,HttpServletRequest request){
        User user = userService.findByEmailId(passwordDto.getEmail());
        String url = "";
        if (user!= null){
            String token = UUID.randomUUID().toString();
            userService.createResetPasswordToken(user,token);
             url = resetPasswordTokenEmail(user,applicationUrl(request),token);
        }
        return url;
    }
    @PostMapping("/savePassword")
    public String saveResetPassword(@RequestParam("token") String token,PasswordDto passwordDto){
        String result = userService.validateResetVerifyToken(token);
        if (!result.equalsIgnoreCase("valid")){
            return "Invalid Token";
        }
        Optional<User> user = userService.getUserByPasswordToken(token);
        if (user.isPresent()){
            userService.saveNewPassword(user.get(),passwordDto.getNewPassword());
            return "Password reset Successfully ";
        }
        return "invalid";

    }
    @PostMapping("/changePassword")
    private String changePassword(@RequestBody PasswordDto passwordDto){
        User user = userService.findByEmailId(passwordDto.getEmail());
        if (!userService.checkOldPasswordValid(user,passwordDto.getOldPassword())){
            return "Invalid Old Password";
        }
        userService.saveNewPassword(user,passwordDto.getNewPassword());
        return "Password Changed";

    }


    private String resetPasswordTokenEmail(User user, String applicationUrl, String token) {
        String url = applicationUrl+"/resetPassword?token="+token;
        //sendVerificationEmail()
        log.info("Click the link to reset Password your account: {}",
                url);
        return url;
    }

    public void resendVerifyTokenEmail(User user, String applicationUrl, UserVerificationToken userVerificationToken){
        String url = applicationUrl+"/verifyToken?token="+userVerificationToken.getToken();
        //sendVerificationEmail()
        log.info("Click the link to verify your account: {}",
                url);
    }

    private String applicationUrl(HttpServletRequest request) {
        return "HTTP:"+request.getServerName()+":"+request.getServerPort()+request.getContextPath();
    }
}
