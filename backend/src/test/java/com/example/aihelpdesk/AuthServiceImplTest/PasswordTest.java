package com.example.aihelpdesk.AuthServiceImplTest;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @Author wzh
 * @Date 2026/6/4 02:19
 */
public class PasswordTest {

    @Test
    void passwordHash (){
        System.out.println(new BCryptPasswordEncoder().encode("123456"));
    }

}
