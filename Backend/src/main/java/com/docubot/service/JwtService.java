
	package com.docubot.service;

	import io.jsonwebtoken.*;
	import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

	import java.security.Key;
	import java.util.Date;
	import java.util.function.Function;

	@Service
	public class JwtService {

	    private static final String SECRET_KEY = "your-secret-key-must-be-256-bits-long-minimum-32-char"; // use env/config later

	    private Key getSigningKey() {
	        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
	    }

	    public String extractUsername(String token) {
	        return extractClaim(token, Claims::getSubject);
	    }

	    public Date extractExpiration(String token) {
	        return extractClaim(token, Claims::getExpiration);
	    }

	    public <T> T extractClaim(String token, Function<Claims, T> resolver) {
	        final Claims claims = extractAllClaims(token);
	        return resolver.apply(claims);
	    }

	    private Claims extractAllClaims(String token) {
	        return Jwts.parserBuilder()
	            .setSigningKey(getSigningKey())
	            .build()
	            .parseClaimsJws(token)
	            .getBody();
	    }

	    private boolean isTokenExpired(String token) {
	        return extractExpiration(token).before(new Date());
	    }

	    public String generateToken(String username) {
	        return Jwts.builder()
	            .setSubject(username)
	            .setIssuedAt(new Date(System.currentTimeMillis()))
	            .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) // 24 hrs
	            .signWith(getSigningKey(), SignatureAlgorithm.HS256)
	            .compact();
	    }

	    public boolean isTokenValid(String token, String userEmail) {
	        final String username = extractUsername(token);
	        return (username.equals(userEmail) && !isTokenExpired(token));
	    }
	    
	    public String getJwtFromCookies(HttpServletRequest request) {
	    	if(request.getCookies() != null) {
	    		for(Cookie cookie : request.getCookies()) {
	    			if(cookie.getName().equals("jwt")) {
	    				return cookie.getValue();
	    			}
	    		}
	    	}
			return "";
	    }
	}

