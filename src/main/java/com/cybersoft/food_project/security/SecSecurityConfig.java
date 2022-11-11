package com.cybersoft.food_project.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@ComponentScan("com.cybersoft.food_project.security")
public class SecSecurityConfig {

    @Autowired
    CustomAuthenticationProvider customAuthenticationProvider;

    // Khởi tạo danh sách user cứng trên RAM (database giả)
//    @Bean
//    public InMemoryUserDetailsManager userDetailsService(){
//        UserDetails user1 = User.withUsername("cybersoft")
//                .password(passwordEncoder().encode("123"))
//                .roles("USER")
//                .build();
//        UserDetails user2 = User.withUsername("admin")
//                .password(passwordEncoder().encode("123"))
//                .roles("ADMIN")
//                .build();
//        return new InMemoryUserDetailsManager(user1,user2);
//    }

    // Kiểu mã hóa dữ liệu
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);    // tạo Authent builder
        authenticationManagerBuilder.authenticationProvider(customAuthenticationProvider); // dùng Authent builer để chọn Provider
        return authenticationManagerBuilder.build();
    }

    // Quy định rule Authencication và Authorization
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        /*
        antMatcher: dinh nghia link can xac thuc
        authenticated:  bat buoc phai chung thuc (dang nhap)
        permitAll: cho phep truy cap full quyen vao link chi dinh
        anyRequest: all link deu phai dang nhap
         */
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/signin").permitAll()
                .antMatchers("/signin/test").authenticated()
                .anyRequest().authenticated();
        return http.build();
    }

}
