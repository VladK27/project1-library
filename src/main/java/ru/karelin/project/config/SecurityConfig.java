package ru.karelin.project.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import ru.karelin.project.services.EmployeeDetailsService;

@Configuration
public class SecurityConfig {
    private final EmployeeDetailsService employeeDetailsService;

    @Autowired
    public SecurityConfig(EmployeeDetailsService employeeDetailsService) {
        this.employeeDetailsService = employeeDetailsService;
    }

    @Bean
    public PasswordEncoder getPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);

        authenticationManagerBuilder.userDetailsService(employeeDetailsService)
                .passwordEncoder(getPasswordEncoder());

        return authenticationManagerBuilder.build();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http.
                formLogin((formLogin) ->
                        formLogin
                                .loginPage("/auth/login")
                                .loginProcessingUrl("/auth/perform_login")
                                .defaultSuccessUrl("/employee/profile", true)
                                .failureUrl("/auth/login?error")
                )
                .authorizeHttpRequests((authorizeRequests) ->
                        authorizeRequests
                                .requestMatchers("/", "/auth/**", "/static/css/**").permitAll()
                                .requestMatchers("/admin/**").hasRole("ADMIN")
                                .anyRequest().hasAnyRole("ADMIN", "MANAGER")
                )
                .logout((logout) ->
                        logout
                                .logoutUrl("/auth/logout")
                                .logoutSuccessUrl("/auth/login")
                );

        return http.build();
    }
}
