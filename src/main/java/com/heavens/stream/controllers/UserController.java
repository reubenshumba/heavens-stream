package com.heavens.stream.controllers;

import com.heavens.stream.dtos.MyUserDto;
import com.heavens.stream.models.MyUser;
import com.heavens.stream.response.AuthenticationResponse;
import com.heavens.stream.response.Response;
import com.heavens.stream.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;



    @GetMapping("/all")
    public ResponseEntity<Response<Page<MyUserDto>>> getAllUserRequests(@RequestParam(defaultValue = "0") int page,
                                                                        @RequestParam(defaultValue = "10") int pageSize) {
        Response<Page<MyUserDto>> userRequests = userService.getAllUserRequests(pageSize,page);
        return new ResponseEntity<>(userRequests, HttpStatus.OK);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Response<MyUserDto>> getUserRequestById(@PathVariable Long id) {
        Response<MyUserDto> userResponse = userService.getUserRequestById(id);
        return new ResponseEntity<>(userResponse, HttpStatusCode.valueOf(userResponse.getStatusCode()));
    }

    @PostMapping("/save")
    public ResponseEntity<Response<AuthenticationResponse>> saveUserRequest(@RequestBody MyUser userRequest) {
        Response<AuthenticationResponse> savedUserRequest = userService.saveUserRequest(userRequest);
        return new ResponseEntity<>(savedUserRequest, HttpStatusCode.valueOf(savedUserRequest.getStatusCode()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserRequest(@PathVariable Long id) {
        userService.deleteUserRequest(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}

