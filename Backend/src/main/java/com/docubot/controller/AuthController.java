package com.docubot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.docubot.dto.AuthenticationRequest;
import com.docubot.entity.User;
import com.docubot.service.AuthenticationService;

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
    public ResponseEntity<String> login(@RequestBody AuthenticationRequest request){
        try {
            String token = authservice.login(request).getToken();
            return ResponseEntity.ok(token);
        } catch(Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }
}
