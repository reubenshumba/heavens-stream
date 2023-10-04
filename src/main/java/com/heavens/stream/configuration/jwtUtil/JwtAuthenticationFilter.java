package com.heavens.stream.configuration.jwtUtil;

import com.heavens.stream.helpers.UserHelper;
import com.heavens.stream.models.MyUserDetails;
import com.heavens.stream.services.MyUserDetailService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final MyUserDetailService myUserDetailService;

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response,
                                    @NotNull FilterChain filterChain) throws ServletException, IOException {

        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            log.info(response.toString());
            return;
        }

        String token = header.replace("Bearer ", "");
        String username = JwtUtil.extractUsername(token);

        if(username !=null && SecurityContextHolder.getContext().getAuthentication() ==null) {
            MyUserDetails myUserDetails = myUserDetailService.loadUserByUsername(username);
            if (JwtUtil.validateToken(token, myUserDetails)) {
                UserHelper.loginUser(request,myUserDetails);
            }
        }
        filterChain.doFilter(request, response);
    }

}
