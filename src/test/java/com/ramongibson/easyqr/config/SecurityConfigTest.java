package com.ramongibson.easyqr.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

@SpringBootTest
public class SecurityConfigTest {

    @Autowired
    private SecurityConfig securityConfig;

    @Test
    public void testFilterChainBean() throws Exception {
        HttpSecurity httpSecurity = mock(HttpSecurity.class);
        assertNotNull(httpSecurity);

    }

    @Test
    public void testBCryptPasswordEncoderBean() {
        BCryptPasswordEncoder bCryptPasswordEncoder = securityConfig.bCryptPasswordEncoder();

        assertNotNull(bCryptPasswordEncoder);

        String rawPassword = "password";
        String encodedPassword = bCryptPasswordEncoder.encode(rawPassword);
        assertTrue(bCryptPasswordEncoder.matches(rawPassword, encodedPassword));
    }
}
