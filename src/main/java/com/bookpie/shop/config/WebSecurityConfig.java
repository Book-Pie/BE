package com.bookpie.shop.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtTokenProvider jwtTokenProvider;
    //private final CustomOAuth2UserService customOAuth2UserService;
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .httpBasic().disable()
                .formLogin().disable()
                .authorizeRequests().antMatchers("/api/user/signup").permitAll()
                .antMatchers("/swagger-ui.html").permitAll()
                .antMatchers("/api/oauth/**").permitAll()
                .antMatchers("/api/user/me").hasRole("USER")
                .antMatchers("/api/user/nickname").hasRole("USER")
                .antMatchers("/api/user/password").hasRole("USER")
                .antMatchers("/swagger-resources/**").permitAll()
                .anyRequest().permitAll()
                .and()
                .addFilterBefore(new JwtFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);


       // http.oauth2Login()
         //       .userInfoEndpoint().userService(customOAuth2UserService);

    }

    @Bean
    public PasswordEncoder passwordEncoder(){return new BCryptPasswordEncoder();}


}