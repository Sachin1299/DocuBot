//
//	package com.docubot.service;
//
//	import io.jsonwebtoken.*;
//	import io.jsonwebtoken.security.Keys;
//import jakarta.servlet.http.Cookie;
//import jakarta.servlet.http.HttpServletRequest;
//
//import org.springframework.stereotype.Service;
//
//	import java.security.Key;
//	import java.util.Date;
//	import java.util.function.Function;
//
//	@Service
//	public class JwtService {
//
//	    private static final String SECRET_KEY = "your-secret-key-must-be-256-bits-long-minimum-32-char"; // use env/config later
//
//	    private Key getSigningKey() {
//	        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
//	    }
//
//	    public String extractUsername(String token) {
//	        return extractClaim(token, Claims::getSubject);
//	    }
//
//	    public Date extractExpiration(String token) {
//	        return extractClaim(token, Claims::getExpiration);
//	    }
//
//	    public <T> T extractClaim(String token, Function<Claims, T> resolver) {
//	        final Claims claims = extractAllClaims(token);
//	        return resolver.apply(claims);
//	    }
//
//	    private Claims extractAllClaims(String token) {
//	        return Jwts.parserBuilder()
//	            .setSigningKey(getSigningKey())
//	            .build()
//	            .parseClaimsJws(token)
//	            .getBody();
//	    }
//
//	    private boolean isTokenExpired(String token) {
//	        return extractExpiration(token).before(new Date());
//	    }
//
//	    public String generateToken(String username) {
//	        return Jwts.builder()
//	            .setSubject(username)
//	            .setIssuedAt(new Date(System.currentTimeMillis()))
//	            .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) // 24 hrs
//	            .signWith(getSigningKey(), SignatureAlgorithm.HS256)
//	            .compact();
//	    }
//
//	    public boolean isTokenValid(String token, String userEmail) {
//	        final String username = extractUsername(token);
//	        return (username.equals(userEmail) && !isTokenExpired(token));
//	    }
//	    
//	    public String getJwtFromCookies(HttpServletRequest request) {
//	    	try {
//	    	if(request.getCookies() != null) {
//	    		for(Cookie cookie : request.getCookies()) {
//	    			if(cookie.getName().equals("jwt")) {
//	    				return cookie.getValue();
//	    			}
//	    		}
//	    	}
//	    	return "";
//	    	}catch(Exception e) {
//			return "";
//	    	}
//	    }
//	}
//

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
    
    private static final String SECRET_KEY = "your-secret-key-must-be-256-bits-long-minimum-32-char";
    
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }
    
    public String extractUsername(String token) {
        try {
            return extractClaim(token, Claims::getSubject);
        } catch (ExpiredJwtException e) {
            throw new ExpiredJwtException(e.getHeader(), e.getClaims(), "JWT token has expired");
        } catch (MalformedJwtException e) {
            throw new MalformedJwtException("JWT token is malformed or invalid format");
        } catch (SignatureException e) {
            throw new SignatureException("JWT signature validation failed");
        } catch (UnsupportedJwtException e) {
            throw new UnsupportedJwtException("JWT token is unsupported");
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("JWT token is null, empty or only whitespace");
        }
    }
    
    public Date extractExpiration(String token) {
        try {
            return extractClaim(token, Claims::getExpiration);
        } catch (ExpiredJwtException e) {
            throw new ExpiredJwtException(e.getHeader(), e.getClaims(), "JWT token has expired");
        } catch (MalformedJwtException e) {
            throw new MalformedJwtException("JWT token is malformed or invalid format");
        } catch (SignatureException e) {
            throw new SignatureException("JWT signature validation failed");
        } catch (UnsupportedJwtException e) {
            throw new UnsupportedJwtException("JWT token is unsupported");
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("JWT token is null, empty or only whitespace");
        }
    }
    
    public <T> T extractClaim(String token, Function<Claims, T> resolver) {
        try {
            final Claims claims = extractAllClaims(token);
            return resolver.apply(claims);
        } catch (ExpiredJwtException e) {
            throw new ExpiredJwtException(e.getHeader(), e.getClaims(), "JWT token has expired");
        } catch (MalformedJwtException e) {
            throw new MalformedJwtException("JWT token is malformed or invalid format");
        } catch (SignatureException e) {
            throw new SignatureException("JWT signature validation failed");
        } catch (UnsupportedJwtException e) {
            throw new UnsupportedJwtException("JWT token is unsupported");
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("JWT token is null, empty or only whitespace");
        }
    }
    
    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        } catch (ExpiredJwtException e) {
            throw new ExpiredJwtException(e.getHeader(), e.getClaims(), "JWT token has expired");
        } catch (MalformedJwtException e) {
            throw new MalformedJwtException("JWT token is malformed or invalid format");
        } catch (SignatureException e) {
            throw new SignatureException("JWT signature validation failed");
        } catch (UnsupportedJwtException e) {
            throw new UnsupportedJwtException("JWT token is unsupported");
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("JWT token is null, empty or only whitespace");
        }
    }
    
    private boolean isTokenExpired(String token) {
        try {
            return extractExpiration(token).before(new Date());
        } catch (ExpiredJwtException e) {
            // If token is expired, we can still extract the expiration claim
            return true;
        } catch (Exception e) {
            // For any other exception, consider token as invalid/expired
            throw new IllegalArgumentException("Cannot determine token expiration status", e);
        }
    }
    
    public String generateToken(String username) {
        try {
            if (username == null || username.trim().isEmpty()) {
                throw new IllegalArgumentException("Username cannot be null or empty");
            }
            
            return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) // 24 hrs
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Token generation failed: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error during token generation", e);
        }
    }
    
    public boolean isTokenValid(String token, String userEmail) {
        try {
            if (token == null || token.trim().isEmpty()) {
                return false;
            }
            if (userEmail == null || userEmail.trim().isEmpty()) {
                return false;
            }
            
            final String username = extractUsername(token);
            return (username.equals(userEmail) && !isTokenExpired(token));
            
        } catch (ExpiredJwtException e) {
            return false; // Token is expired, so invalid
        } catch (MalformedJwtException | SignatureException | UnsupportedJwtException | IllegalArgumentException e) {
            return false; // Any JWT parsing error means invalid token
        } catch (Exception e) {
            return false; // Any unexpected error means invalid token
        }
    }
    
    public String getJwtFromCookies(HttpServletRequest request) {
        try {
            if (request == null) {
                throw new IllegalArgumentException("HttpServletRequest cannot be null");
            }
            
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (cookie != null && "jwt".equals(cookie.getName())) {
                        String tokenValue = cookie.getValue();
                        if (tokenValue != null && !tokenValue.trim().isEmpty()) {
                            return tokenValue;
                        }
                    }
                }
            }
            return null; // Return null instead of empty string for clearer semantics
            
        } catch (IllegalArgumentException e) {
            throw e; // Re-throw validation exceptions
        } catch (Exception e) {
            throw new RuntimeException("Error extracting JWT from cookies", e);
        }
    }
}

