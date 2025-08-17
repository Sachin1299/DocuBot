package com.docubot.security;

import com.docubot.entity.User;
import com.docubot.entity.Provider; // create this enum in your entity package
import com.docubot.repository.UserRepository;
import com.docubot.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomOAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Value("${app.frontend-url:https://localhost:3000}")
    private String frontendUrl;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        // Only email and name (as per your requirement)
        String email = oAuth2User.getAttribute("email");
        String name  = oAuth2User.getAttribute("name");

        if (email == null || email.isBlank()) {
            // Hard stop if email is somehow missing (shouldn't happen with proper scopes)
            response.sendRedirect(frontendUrl + "/login?error=missing_email");
            return;
        }

        // Find existing user by email
        User user = userRepository.findByEmail(email).orElse(null);

        if (user == null) {
            // First-time OAuth user (JIT provisioning)
            user = User.builder()
                    .email(email)
                    .name(name != null ? name : "")  // fallback if name is null
                    .password(null)                  // OAuth users have no local password
                    .provider(Provider.GOOGLE)
                    .enabled(true)
                    .build();
            userRepository.save(user);
        } else {
            // Existing user: enforce provider policy
            // If you want strict separation, block when provider is LOCAL
            // and the user tries to sign in with Google.
            // If you implement linking later, change behavior here.
            if (user.getProvider() == Provider.LOCAL) {
                response.sendRedirect(frontendUrl + "/login?error=email_in_use_with_password");
                return;
            }
            // Optional: update the display name if it changed upstream
            if (name != null && !name.equals(user.getName())) {
                user.setName(name);
                userRepository.save(user);
            }
        }

        // Mint your JWT (subject = email) using your existing service
        String token = jwtService.generateToken(user.getEmail());

        // Set HttpOnly cookie (aligned with your login implementation)
        ResponseCookie cookie = ResponseCookie.from("jwt", token)
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .path("/")
                .maxAge(24 * 60 * 60) // 1 day
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        // Redirect to frontend landing page for logged-in users
        response.sendRedirect(frontendUrl + "/home");
    }
}
