package com.ueg.eventplataform.domain.infra_security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;

@Configuration
@EnableWebSecurity
public class SecurityConf {
    @Autowired 
    SecurityFilter securityFilter;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
       return httpSecurity
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                    .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                    .requestMatchers(HttpMethod.POST, "/auth/register").permitAll()
                    .requestMatchers(HttpMethod.GET, "/users").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.PUT, "/users/{id}/role").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.DELETE, "/users/{id}").hasRole("ADMIN")

                    // 2. Rotas de Eventos - Leitura (Abertas para todos)
                    .requestMatchers(HttpMethod.GET, "/events").permitAll()
                    .requestMatchers(HttpMethod.GET, "/events/{id}").permitAll()

                    // 3. Rotas de Eventos - Modificação (Restritas ao Organizador/Admin)
                    // Substitua "ADMIN" ou "HOST" pelo nome exato que está no seu Enum UserRole
                    .requestMatchers(HttpMethod.POST, "/events").hasRole("Host") 
                    .requestMatchers(HttpMethod.PUT, "/events/{id}").hasRole("Admin")
                    .requestMatchers(HttpMethod.DELETE, "/events/{id}").hasRole("Admin")
                    .anyRequest().authenticated())
                    .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
        }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    } 
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
