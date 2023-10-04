package com.heavens.stream.services;

import com.heavens.stream.models.MyUser;
import com.heavens.stream.models.MyUserDetails;
import com.heavens.stream.repositories.AuthorityRepository;
import com.heavens.stream.repositories.MyUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j
public class MyUserDetailService implements UserDetailsService {

    private final MyUserRepository myUserRepository;
    private final AuthorityRepository authorityRepository;

    @Override
    public MyUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<MyUser> myUserOptional = myUserRepository.findFirstByUsernameIgnoreCaseOrEmailIgnoreCaseAndActiveTrue(username, username);
        MyUser myUser = myUserOptional.orElseThrow(() -> new UsernameNotFoundException("MyUser not found with username or email " + username));
        return new MyUserDetails(myUser);
    }




}
