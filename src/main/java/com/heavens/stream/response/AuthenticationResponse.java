package com.heavens.stream.response;

import com.heavens.stream.dtos.MyUserDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationResponse {
    private String token;
    private MyUserDto user;
}
