// File: JwtAuthenticationFilter.java
package com.docubot.security;

import com.docubot.service.CustomUserDetailsService;
import com.docubot.service.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {


    @Autowired
    private JwtService jwtUtil;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    // The ObjectMapper is used to write a JSON response
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
                                    throws ServletException, IOException {
    	
        try {   
            final String jwtToken = jwtUtil.getJwtFromCookies(request);
            System.out.println("Cookies : "+request.getCookies());
            System.out.println("JwtToken: "+jwtToken);
            final String userEmail = jwtUtil.extractUsername(jwtToken);

            // If token is valid, configure Spring Security to manually set authentication
            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);

                // Note: The original isTokenValid re-parsed the token.
                // Assuming your JwtService confirms the token hasn't expired and matches the user.
                if (jwtUtil.isTokenValid(jwtToken, userDetails.getUsername())) {
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
            // Continue the filter chain
            filterChain.doFilter(request, response);

        } catch (JwtException ex) {
            // This block is executed when the token is invalid (e.g., tampered, expired)
            // We directly create and send the 401 Unauthorized response

            // Set the response status to 401 Unauthorized
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);

            // Create a clear error message body
            Map<String, Object> errorDetails = new HashMap<>();
            errorDetails.put("message", "Invalid or expired JWT token.");
            errorDetails.put("error", ex.getMessage());
            
            // Write the error message to the response body
            response.getWriter().write(objectMapper.writeValueAsString(errorDetails));
            
            // By not calling filterChain.doFilter(), we stop the request processing here.
            // The 'return' is implicit as this is the end of the method execution path.
        }
        catch (UsernameNotFoundException ex) {
        	response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);

            // Create a clear error message body
            Map<String, Object> errorDetails = new HashMap<>();
            errorDetails.put("message", "User doesn't exit.");
            errorDetails.put("error", ex.getMessage());
            
            // Write the error message to the response body
            response.getWriter().write(objectMapper.writeValueAsString(errorDetails));
            
            // By not calling filterChain.doFilter(), we stop the request processing here.
            // The 'return' is implicit as this is the end of the method execution path.
        }
        catch (Exception ex) {
            // This block is executed when the token is invalid (e.g., tampered, expired)
            // We directly create and send the 401 Unauthorized response

            // Set the response status to 401 Unauthorized
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);

            // Create a clear error message body
            Map<String, Object> errorDetails = new HashMap<>();
            errorDetails.put("message", "Invalid or expired JWT token.");
            errorDetails.put("error", ex.getMessage());
            
            // Write the error message to the response body
            response.getWriter().write(objectMapper.writeValueAsString(errorDetails));
            
            // By not calling filterChain.doFilter(), we stop the request processing here.
            // The 'return' is implicit as this is the end of the method execution path.
        }
        
    }
    
    
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        return path.equals("/api/auth/login") ||
               path.equals("/api/auth/signup") ||
               path.equals("/api/auth/logout");
    }

}