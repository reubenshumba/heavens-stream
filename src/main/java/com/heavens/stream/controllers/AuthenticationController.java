package com.heavens.stream.controllers;


import com.heavens.stream.models.MyUser;
import com.heavens.stream.request.AuthRequest;
import com.heavens.stream.response.AuthenticationResponse;
import com.heavens.stream.response.Response;
import com.heavens.stream.services.AuthenticateService;
import com.heavens.stream.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/authenticate")
@RequiredArgsConstructor
@Slf4j
public class AuthenticationController {

    private final AuthenticateService authenticateService;
    private final UserService userService;


    @PostMapping({"/user","/login"})
    public ResponseEntity<AuthenticationResponse> authenticateByUsernameAndPassword(@RequestBody @Valid AuthRequest authRequest){
        return new ResponseEntity<>(authenticateService.generateToken(authRequest), HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<Response<AuthenticationResponse>> saveUserRequest(@RequestBody MyUser userRequest) {
        Response<AuthenticationResponse> savedUserRequest = userService.saveUserRequest(userRequest);
        return new ResponseEntity<>(savedUserRequest, HttpStatusCode.valueOf(savedUserRequest.getStatusCode()));
    }

}
