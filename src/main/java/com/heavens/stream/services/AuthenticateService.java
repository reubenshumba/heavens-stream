package com.heavens.stream.services;

import com.heavens.stream.configuration.jwtUtil.JwtUtil;
import com.heavens.stream.dtos.MyUserDto;
import com.heavens.stream.models.MyUserDetails;
import com.heavens.stream.request.AuthRequest;
import com.heavens.stream.response.AuthenticationResponse;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
@Data
public class AuthenticateService {
    private final MyUserDetailService myUserDetailService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse generateToken(AuthRequest authRequest){

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
        );
        MyUserDetails myUserDetails = myUserDetailService.loadUserByUsername(authRequest.getUsername());
        // Create an MyUserDto object
        MyUserDto myUserDto = new MyUserDto();
        // Set the properties of the MyUserDto object based on the MyUserDetails object
        myUserDto.setId(myUserDetails.getMyUser().getId());
        myUserDto.setUsername(myUserDetails.getUsername());
        myUserDto.setEmail(myUserDetails.getMyUser().getEmail());
        myUserDto.setActive(myUserDetails.isEnabled());
        myUserDto.setAccountNonExpired(myUserDetails.isAccountNonExpired());
        myUserDto.setAccountNonLocked(myUserDetails.isAccountNonLocked());
        myUserDto.setCredentialsNonExpired(myUserDetails.isCredentialsNonExpired());
        myUserDto.setAuthorities(myUserDetails.getMyUser().getAuthorities());
        myUserDto.setDelete(myUserDetails.getMyUser().isDelete());
        myUserDto.setEdit(myUserDetails.getMyUser().isEdit());
        myUserDto.setCreate(myUserDetails.getMyUser().isCreate());
        return new AuthenticationResponse(JwtUtil.generateToken(myUserDetails), myUserDto);

    }
}
