package com.example.aihelpdesk.AuthServiceImplTest;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @Author wzh
 * @Date 2026/6/4 02:19
 */
public class PasswordTest {

    @Test
    void encodePassword() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encoded = encoder.encode("123456");

        System.out.println(encoded);
        System.out.println(encoder.matches("123456", encoded));
    }

}
