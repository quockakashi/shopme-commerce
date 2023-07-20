package com.shopme.admin.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;

@Configuration
public class WebSecurityConfig {

    @Bean
    @Primary
    public UserDetailsService userDetailsService() {
        return new ShopmeUserDetailService();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());

        return authenticationProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, RememberMeServices rememberMeServices) throws Exception {
        http
                .authenticationProvider(authenticationProvider())
                .authorizeHttpRequests(configurer->
                        configurer
                                .requestMatchers("/webjars/**").permitAll()
                                .requestMatchers("/images/**").permitAll()
                                .anyRequest().authenticated())
                .formLogin(login ->
                        login
                                .loginPage("/login")
                                .loginProcessingUrl("/authenticateTheUser")
                                .permitAll())
                                /* ban không phải làm gì cả,
                                  chỉ cần đứa "username" và "password" vào "/authenticateTheUser"
                                  bằng phương thức post từ form, spring boot will process behind the seance*/
                .logout(LogoutConfigurer::permitAll)
                        .rememberMe(remember ->
                                remember.rememberMeServices(rememberMeServices));

        http.httpBasic(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public RememberMeServices rememberMeServices(ShopmeUserDetailService userDetailService) {
        TokenBasedRememberMeServices.RememberMeTokenAlgorithm encodingAlgorithm = TokenBasedRememberMeServices.RememberMeTokenAlgorithm.SHA256;
        TokenBasedRememberMeServices rememberMe = new TokenBasedRememberMeServices("abCdEfghJklMnOPKrstyvwZ_123456789", userDetailService, encodingAlgorithm);
        rememberMe.setMatchingAlgorithm(TokenBasedRememberMeServices.RememberMeTokenAlgorithm.MD5);
        rememberMe.setTokenValiditySeconds(7 * 24 * 60 * 60);
        return rememberMe;
    }

}
