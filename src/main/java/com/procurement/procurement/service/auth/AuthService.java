package com.procurement.procurement.service.auth;

import com.procurement.procurement.dto.auth.AuthResponseDTO;
import com.procurement.procurement.dto.auth.LoginRequestDTO;
import com.procurement.procurement.entity.user.Role;
import com.procurement.procurement.entity.user.User;
import com.procurement.procurement.repository.user.UserRepository;
import com.procurement.procurement.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // ===================== REGISTER USER =====================
    public String register(LoginRequestDTO request) {

        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("User already exists");
        }

        User user = new User();

        user.setUsername(request.getUsername());
        user.setEmail(request.getUsername()); // reuse username as email
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        // 🔥 IMPORTANT FIXES (PREVENT NULL ROWS)
        user.setFullName(request.getUsername());
        user.setEnabled(true);
        user.setActive(true);

        Role role = new Role();
        role.setName("ADMIN"); // or USER
        user.setRoles(Set.of(role));

        userRepository.save(user);

        return "User registered successfully";
    }




    // ===================== LOGIN USER =====================
    public AuthResponseDTO login(LoginRequestDTO loginRequest) throws AuthenticationException {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        User user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String token = jwtTokenProvider.generateToken(user.getUsername());

        AuthResponseDTO response = new AuthResponseDTO();
        response.setToken(token);
        response.setType("Bearer");
        response.setUsername(user.getUsername());

        Set<String> roles = user.getRoles()
                .stream()
                .map(Role::getName)
                .collect(Collectors.toSet());

        response.setRoles(roles);

        return response;
    }
}
