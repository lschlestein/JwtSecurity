package com.lucas.security.auth;

import com.lucas.security.config.JwtService;
import com.lucas.security.user.Role;
import com.lucas.security.user.User;
import com.lucas.security.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

public class AuthenticationService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    public AuthenticationResponse register(RegisterRequest request){// create a new user save to database and send a new token
        var user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))//encode the password to save
                .role(Role.USER)//static role
                .build();
        repository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
    public AuthenticationResponse authenticate(AuthenticationRequest request){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()//if is wrong one exception is created
                )
        );
        var user = repository.findByEmail(request.getEmail())//here the user is authenticated
                .orElseThrow();
        var jwtToken = jwtService.generateToken(user);//generate a token
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}
