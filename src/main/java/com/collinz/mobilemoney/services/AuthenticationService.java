package com.collinz.mobilemoney.services;

import com.collinz.mobilemoney.exceptions.ObjectAlreadyExistsException;
import com.collinz.mobilemoney.exceptions.ObjectNotFoundException;
import com.collinz.mobilemoney.models.Role;
import com.collinz.mobilemoney.models.User;
import com.collinz.mobilemoney.models.enums.Status;
import com.collinz.mobilemoney.repositories.UserRepository;
import com.collinz.mobilemoney.requests.LoginRequest;
import com.collinz.mobilemoney.requests.RegisterRequest;
import com.collinz.mobilemoney.responses.AuthenticationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthenticationService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository repository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthenticationService(PasswordEncoder encoder, UserRepository repo, JwtService jwtService, AuthenticationManager authenticationManager){
        this.passwordEncoder = encoder;
        this.repository = repo;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public AuthenticationResponse register(RegisterRequest request) throws ObjectAlreadyExistsException{
        User user = new User(
                null,
                request.getFirstName(),
                request.getLastName(),
                request.getUsername(),
                passwordEncoder.encode(request.getPassword()),
                Status.ACTIVE,
                LocalDateTime.now(),
                Role.USER
        );

        if (repository.existsByUsername(user.getUsername())){
            throw new ObjectAlreadyExistsException("Username already in use!");
        }

        repository.save(user);

        return getAuthenticationResponse(user);
    }

    private AuthenticationResponse getAuthenticationResponse(User user) {
        String jwt = jwtService.generateToken(user);

        return new AuthenticationResponse(jwt);
    }

    public AuthenticationResponse login(LoginRequest request) throws ObjectNotFoundException {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        var user = repository.findUserByUsername(request.getUsername()).orElseThrow(() -> new ObjectNotFoundException("User not found!"));

        return getAuthenticationResponse(user);
    }
}
