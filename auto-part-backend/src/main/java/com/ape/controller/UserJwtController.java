package com.ape.controller;

import com.ape.dto.UserDTO;
import com.ape.dto.request.LoginRequest;
import com.ape.dto.request.RegisterRequest;
import com.ape.security.jwt.JwtUtils;
import com.ape.service.UserService;
import com.ape.utility.APEResponse;
import com.ape.utility.LoginResponse;
import com.ape.utility.ResponseMessage;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class UserJwtController {

    private final UserService userService;

    private final AuthenticationManager authenticationManager;

    private final JwtUtils jwtUtils;

    @PostMapping("/register")
    public ResponseEntity<APEResponse> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        userService.saveUser(registerRequest);

        APEResponse response = new APEResponse(ResponseMessage.REGISTER_RESPONSE_MESSAGE, true);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping(path = "/confirm")
    public ResponseEntity<APEResponse> confirm(@RequestParam("token") String token) {
        userService.confirmToken(token);
        APEResponse response = new APEResponse(ResponseMessage.ACCOUNT_CONFIRMED_RESPONSE,true);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestHeader(value = "basketUUID", required = false)String basketUUID,@Valid @RequestBody LoginRequest loginRequest) {

        LoginResponse loginResponse = userService.loginUser(basketUUID,loginRequest);

        return new ResponseEntity<>(loginResponse, HttpStatus.OK);

    }
}