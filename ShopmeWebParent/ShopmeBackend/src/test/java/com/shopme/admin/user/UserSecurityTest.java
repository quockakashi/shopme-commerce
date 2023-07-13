package com.shopme.admin.user;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import static org.assertj.core.api.Assertions.assertThat;

public class UserSecurityTest {

    @Test
    public void testSimpleGrantAuthority() {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("admin");
        System.out.println(authority.toString());
        assertThat(authority.toString()).isNotNull();
    }
}
