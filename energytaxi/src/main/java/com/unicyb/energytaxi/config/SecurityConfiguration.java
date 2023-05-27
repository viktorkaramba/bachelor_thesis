package com.unicyb.energytaxi.config;

import com.unicyb.energytaxi.entities.documents.ROLE;
import com.unicyb.energytaxi.services.other.LogoutService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    private final LogoutService logoutService;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.cors();
        httpSecurity.csrf().disable();
        httpSecurity.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        httpSecurity.authorizeHttpRequests().requestMatchers("/api/v1/auth/**").permitAll();
        httpSecurity.authorizeHttpRequests().requestMatchers("/ws/**").permitAll();
        httpSecurity.authorizeHttpRequests().requestMatchers("/api/v1/documents/**")
                .hasAnyAuthority(ROLE.SUPER_ADMIN.name(), ROLE.ADMIN.name());
        httpSecurity.authorizeHttpRequests().requestMatchers("/api/v1/indicators/**")
                .hasAnyAuthority(ROLE.SUPER_ADMIN.name(), ROLE.ADMIN.name());
        httpSecurity.authorizeHttpRequests().requestMatchers("/api/v1/user-app/**")
                .hasAnyAuthority(ROLE.SUPER_ADMIN.name(), ROLE.ADMIN.name(), ROLE.USER.name());
        httpSecurity.authorizeHttpRequests().requestMatchers("/api/v1/driver-app/**")
                .hasAnyAuthority(ROLE.SUPER_ADMIN.name(), ROLE.ADMIN.name(), ROLE.DRIVER.name());
        httpSecurity.authorizeHttpRequests().requestMatchers("/api/v1/bonuses/**")
                .hasAnyAuthority(ROLE.SUPER_ADMIN.name(), ROLE.ADMIN.name(), ROLE.USER.name());
        httpSecurity.authorizeHttpRequests().anyRequest().authenticated();
        httpSecurity.authenticationProvider(authenticationProvider);
        httpSecurity.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        httpSecurity.logout().logoutUrl("/api/v1/auth/logout").addLogoutHandler(logoutService)
                .logoutSuccessHandler(((request, response, authentication) -> {
                    SecurityContextHolder.clearContext();
                }));
        return httpSecurity.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*"));
        configuration.setAllowedMethods(List.of("*"));
        configuration.setAllowedHeaders(List.of("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
