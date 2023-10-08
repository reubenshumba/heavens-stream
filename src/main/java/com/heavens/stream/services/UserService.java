package com.heavens.stream.services;

import com.heavens.stream.configuration.jwtUtil.JwtUtil;
import com.heavens.stream.dtos.MyUserDto;
import com.heavens.stream.helpers.AuthorityHelper;
import com.heavens.stream.helpers.UserHelper;
import com.heavens.stream.models.Authority;
import com.heavens.stream.models.MyUser;
import com.heavens.stream.models.MyUserDetails;
import com.heavens.stream.repositories.AuthorityRepository;
import com.heavens.stream.repositories.MyUserRepository;
import com.heavens.stream.response.AuthenticationResponse;
import com.heavens.stream.response.Response;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Data
@Slf4j
public class UserService {

    private final MyUserRepository userRequestRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final AuthorityRepository authorityRepository;

    private final MyUserDetailService myUserDetailService;

    private HttpServletRequest request;


    public Response<Page<MyUserDto>> getAllUserRequests(int pageSize, int page) {
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<MyUser> userPage = userRequestRepository.findAll(pageable);
        List<MyUserDto> myUserDto = MyUserDto.fromMyUsers(userPage.getContent());
        Page<MyUserDto> authUserPage = new PageImpl<>(myUserDto, pageable, userPage.getTotalElements());
        return Response.successfulResponse("Successful", authUserPage);
    }

    public Response<MyUserDto> getUserRequestById(Long id) {
        Optional<MyUser> byId = userRequestRepository.findById(id);
        if (byId.isEmpty()){
            return  Response.failedResponse(HttpStatus.NOT_FOUND.value(),"Not found");
        }
        MyUserDto myUserDto = MyUserDto.fromMyUser(byId.get());
        return Response.successfulResponse("Successful", myUserDto);
    }

    @Transactional
    public Response<AuthenticationResponse> saveUserRequest(MyUser userRequest) {
        log.info("received this request {}", userRequest);
        Optional<MyUser> checkIfUserExist = userRequestRepository.findFirstByUsernameIgnoreCaseOrEmailIgnoreCaseAndActiveTrue(userRequest.getUsername(), userRequest.getEmail());
        if(checkIfUserExist.isPresent()){
            return Response.failedResponse(HttpStatus.CONFLICT.value(), "User with provided details already exist");
        }
        log.info("about to encrypt password  ");
        userRequest.setPassword(bCryptPasswordEncoder.encode(userRequest.getPassword()));
        if(userRequest.getAuthorities().isEmpty()){
            log.info("no authorities where passed , try to find a default role User ");
            Optional<Authority> role_user = authorityRepository.findFirstByRoleNameOrAuthorityCodeAndActiveTrue("ROLE_USER", "14029049");

            if(role_user.isPresent()){
                log.info("default role found ");
                userRequest.setAuthorities(List.of(role_user.get()));
                //return Response.failedResponse(HttpStatus.NOT_FOUND.value(), "Could not assign role");
            }else {

                log.info("no default  role found, create one ");
                Authority authority =AuthorityHelper.createDefaultUserRole(authorityRepository);
                log.info("no default  role found, Admin,super admin, and user role has been being created : {}",authority);
                userRequest.setAuthorities(List.of(authority));
            }

        }else{
            log.info("user has authorities therefore validate");
            List<Long> collect = userRequest.getAuthorities().stream().map(Authority::getId).collect(Collectors.toList());
            List<Authority> authorities = authorityRepository.findAllByIdIn(collect);
            userRequest.setAuthorities(authorities);
            log.info("Authorities are found {}", authorities);
        }


        MyUserDto myUserDto = MyUserDto.fromMyUser(userRequestRepository.save(userRequest));
        log.info("create a session for {}", myUserDto);

        //Get token
        MyUserDetails myUserDetails = myUserDetailService.loadUserByUsername(myUserDto.getUsername());
        String token = JwtUtil.generateToken(myUserDetails);
        log.info("Session token created {}", token );
        //Login new user
        AuthenticationResponse authenticationResponse = new AuthenticationResponse(token, myUserDto);
        UserHelper.loginUser(request,myUserDetails);
        log.info("Session created {}", authenticationResponse );
        return Response.successfulResponse("Successful", authenticationResponse);
    }

    public void deleteUserRequest(Long id) {
        userRequestRepository.deleteById(id);
    }

    public Optional<MyUser> findById(Long id) {
        return userRequestRepository.findById(id);
    }

    public MyUser save(MyUser user) {
        return userRequestRepository.save(user);
    }
}

