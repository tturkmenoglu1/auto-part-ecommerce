package com.ape.controller;

import com.ape.dto.request.RegisterRequest;
import com.ape.security.jwt.JwtUtils;
import com.ape.service.UserService;
import com.ape.utility.APEResponse;
import com.ape.utility.ResponseMessage;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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

//    @PostMapping("/login")
//    public ResponseEntity<LoginResponse> authenticate(@Valid @RequestBody LoginRequest loginRequest) {
//
//        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
//                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword());
//
//        Authentication authentication = authenticationManager.
//                authenticate(usernamePasswordAuthenticationToken);
//
//        UserDetails userDetails = (UserDetails) authentication.getPrincipal();// mevcut giriş yapan kullanıcıyı getirir
//
//        String jwtToken = jwtUtils.generateJwtToken(userDetails);
//
//        LoginResponse loginResponse = new LoginResponse(jwtToken);
//
//        return new ResponseEntity<>(loginResponse, HttpStatus.OK);
//
//    }
}