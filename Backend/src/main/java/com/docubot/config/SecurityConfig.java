package com.docubot.config;

import com.docubot.security.JwtAuthenticationFilter;
import com.docubot.security.CustomOAuth2AuthenticationSuccessHandler;   // ADD
import com.docubot.security.CustomOAuth2AuthenticationFailureHandler;   // ADD

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthFilter;

    @Autowired
    private AuthenticationProvider authenticationProvider;

    // ADD: OAuth handlers (you’ll create these classes in step 3.7)
    @Autowired
    private CustomOAuth2AuthenticationSuccessHandler oauthSuccessHandler;

    @Autowired
    private CustomOAuth2AuthenticationFailureHandler oauthFailureHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // CORS
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            // CSRF off for stateless APIs with JWT
            .csrf(csrf -> csrf.disable())
            // Authorization rules
            .authorizeHttpRequests(auth -> auth
                // Permit all preflight (OPTIONS) requests
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()  // FIX: "/**"
                // Public auth endpoints
                .requestMatchers("/api/auth/login","/api/auth/signup", "/api/auth/logout").permitAll()
                // OAuth2 endpoints must be public
                .requestMatchers("/oauth2/**", "/login/oauth2/**").permitAll()
                // Everything else requires authentication
                .anyRequest().authenticated()
            )
            // Stateless (JWT)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            // OAuth2 login flow
            .oauth2Login(oauth2 -> oauth2
                // Optional but explicit base URIs (match Spring defaults you’re using)
                .authorizationEndpoint(e -> e.baseUri("/oauth2/authorization"))
                .redirectionEndpoint(e -> e.baseUri("/login/oauth2/code/*"))
                .successHandler((AuthenticationSuccessHandler) oauthSuccessHandler)
                .failureHandler((AuthenticationFailureHandler) oauthFailureHandler)
            )
            // Your DAO auth provider (for email/password)
            .authenticationProvider(authenticationProvider)
            // JWT filter before username/password auth filter
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("https://localhost:3000"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*")); // FIX: "*"
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // FIX: "/**"
        return source;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
