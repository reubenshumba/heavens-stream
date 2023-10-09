package com.heavens.stream.helpers;

import com.heavens.stream.dtos.MyUserDto;
import com.heavens.stream.models.MyUser;
import com.heavens.stream.models.MyUserDetails;
import com.heavens.stream.repositories.MyUserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;



@RequiredArgsConstructor
@Slf4j
public class UserHelper {
    public static void loginUser(HttpServletRequest request,MyUserDetails myUserDetails){
        log.info("request ===> {}", request);
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(MyUserDto.fromMyUser(myUserDetails.getMyUser()), null, myUserDetails.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }


}
