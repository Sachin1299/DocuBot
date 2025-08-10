package com.docubot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.docubot.dto.AuthenticationRequest;
import com.docubot.entity.User;
import com.docubot.service.AuthenticationService;

import jakarta.servlet.http.HttpServletResponse;

@RestController  // ✅ Fixed annotation
@RequestMapping("/api/auth")  // ✅ Use @RequestMapping instead
public class AuthController {

    @Autowired
    private AuthenticationService authservice;

    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@RequestBody User user) {
        try {
            String token = authservice.register(user).getToken();
            return ResponseEntity.ok(token);
        } catch(Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody AuthenticationRequest request, HttpServletResponse response){
        try {
            String token = authservice.login(request).getToken();
//    		ResponseCookie cookie = ResponseCookie.from("jwt", token)
//    				.httpOnly(true)
//    				.secure(false)
//    				.sameSite("none")
//    				.path("/")
//    				.build();
//    		response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
            ResponseCookie cookie = ResponseCookie.from("jwt", token)
                    .httpOnly(true)    // JS can't access
                    .secure(true)     // false for local HTTP, true in prod HTTPS
                    .sameSite("None")  // ✅ Capital N
                    .path("/")         // Send for all backend paths
                    .maxAge(24 * 60 * 60) // Optional: 1 day
                    .build();

            response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

            return ResponseEntity.ok(token);
        } catch(Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }
}
