package com.docubot.service;

import com.docubot.repository.UserRepository;
import com.docubot.security.UserDetailsImpl;  // Import your UserDetailsImpl
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        var user = userRepository.findByEmail(email)
               .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
        
        return new UserDetailsImpl(user);  // ✅ Wrap User in UserDetailsImpl
    }
}
