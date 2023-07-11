package com.example.springbootsecurity.Repository;

import com.example.springbootsecurity.Entity.UserVerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserVerficationTokenRepository extends JpaRepository<UserVerificationToken,Long> {
       UserVerificationToken findByToken(String token);
}
