package com.shopme.admin.user;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;


public class EncodePasswordTest {

    @Test
    public void encodePasswordTest() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPassword = "helloworld";

        String encodedPassword = encoder.encode(rawPassword);

        System.out.println(encodedPassword);
        assertThat(encoder.matches(rawPassword, encodedPassword)).isTrue();
    }
}
