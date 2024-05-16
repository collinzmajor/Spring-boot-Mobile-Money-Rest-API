package com.collinz.mobilemoney.controllers;

import com.collinz.mobilemoney.exceptions.ObjectAlreadyExistsException;
import com.collinz.mobilemoney.exceptions.ObjectNotFoundException;
import com.collinz.mobilemoney.requests.LoginRequest;
import com.collinz.mobilemoney.requests.RegisterRequest;
import com.collinz.mobilemoney.responses.AuthenticationResponse;
import com.collinz.mobilemoney.services.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {
    private final AuthenticationService authService;

    @Autowired
    public AuthenticationController(AuthenticationService service){
        this.authService = service;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody @Valid RegisterRequest request) throws ObjectAlreadyExistsException {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody @Valid LoginRequest request) throws ObjectNotFoundException {
        return ResponseEntity.ok(authService.login(request));
    }
}
