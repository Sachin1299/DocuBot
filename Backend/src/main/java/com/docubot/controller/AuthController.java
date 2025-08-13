package com.docubot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.docubot.dto.AuthenticationRequest;
import com.docubot.entity.User;
import com.docubot.service.AuthenticationService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
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
    
    @GetMapping("/logout")
    public ResponseEntity<String> logout(HttpServletResponse response){
    	try {
    	ResponseCookie cookie = ResponseCookie.from("jwt",null)
    			.httpOnly(true)
    			.secure(true)
    			.sameSite("None")
    			.path("/")
    			.maxAge(0)
    			.build();
    	
    	response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    	return ResponseEntity.ok("Logout Successfull");
    	}
    	catch(Exception e) {
    		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    	}
    }
    
    
//    @GetMapping("/check")
//    public ResponseEntity<String> loginCheck(){
////    	Cookie[] cookies = request.getCookies();
////    	System.out.println("Cookie: " + cookies[0].getValue());
////    	if (cookies != null) {
////    	    for (Cookie c : cookies) {
////    	        System.out.println("Cookie: " + c.getName() + "=" + c.getValue());
////    	    }
////    	} else {
////    	    System.out.println("No cookies");
////    	}
//    	try {
//    		return ResponseEntity.status(HttpStatus.OK).build();
//    	}catch(Exception e) {
//    	return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//    	}
//    }
    
    @GetMapping("/check")
    public ResponseEntity<String> loginCheck(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            return ResponseEntity.ok("Authenticated as: " + authentication.getName());
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not authenticated");
    }

}
