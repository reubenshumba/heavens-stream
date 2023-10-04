package com.heavens.stream.services;

import com.heavens.stream.dtos.HeavenDto;
import com.heavens.stream.dtos.MyUserDto;
import com.heavens.stream.helpers.AuthorityHelper;
import com.heavens.stream.helpers.Helper;
import com.heavens.stream.models.Authority;
import com.heavens.stream.models.Heaven;
import com.heavens.stream.models.MyUser;
import com.heavens.stream.repositories.HeavenRepository;
import com.heavens.stream.request.HeavenRequest;
import com.heavens.stream.response.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Slf4j
public class HeavenService {

    private static final Sort DATE_CREATED = Sort.by(Sort.Direction.DESC, "dateCreated");
    private final HeavenRepository heavenRepository;
    private final AuthorityService authorityService;
    private final UserService userService;


    public Response<Page<HeavenDto>> getAllHeavens(int pageSize, int page, MyUserDto authUser,boolean filterByUser) {
        Pageable pageable = PageRequest.of(page, pageSize, DATE_CREATED);
        Page<Heaven> allByActiveTrue = filterByUser ? heavenRepository.findDistinctByAuthoritiesInOrHeavenOwnAndActiveTrue(authUser.getAuthorities(), authUser.getId(), pageable) :
        heavenRepository.findAllByActiveTrue(pageable);

        List<HeavenDto> heavenDto = HeavenDto.toDTO(allByActiveTrue.getContent());
        Page<HeavenDto> newPage = new PageImpl<>(heavenDto, pageable, allByActiveTrue.getTotalElements());
        return Response.successfulResponse("Successful", newPage);
    }


    public Response<HeavenDto> getHeavenById(Long id) {
        Optional<Heaven> optionalHeaven = heavenRepository.findById(id);
        return optionalHeaven
                .map(heaven -> Response.successfulResponse("Successful", HeavenDto.toDTO(heaven)))
                .orElse(Response.failedResponse(HttpStatus.NOT_FOUND.value(), "Heaven not found"));
    }

    @Transactional
    public Response<HeavenDto> saveHeaven(HeavenRequest heavenRequest, Long id) {
        String s = Helper.randomUUIDStringID();
        heavenRequest.setHeavenName(heavenRequest.getHeavenName().trim());
        Optional<Heaven> optionalHeaven = heavenRepository.findFirstByHeavenNameOrStringId(heavenRequest.getHeavenName(), s);
        if (optionalHeaven.isPresent()) {
            return Response.failedResponse(HttpStatus.CONFLICT.value(), "Heaven already exists with the name: " +
                    heavenRequest.getHeavenName());
        }
        List<Authority> authorities = new ArrayList<>();
        if (!heavenRequest.getAuthorities().isEmpty()) {
            List<Authority> authorityList = authorityService.getAuthorityById(heavenRequest.getAuthorities());
            authorities.addAll(authorityList);
        }

        Heaven heaven = Heaven.builder()
                .heavenName(heavenRequest.getHeavenName())
                .heavenOwn(id)
                .imageUrl(heavenRequest.getImageUrl())
                .heavenDescription(heavenRequest.getHeavenDescription())
                .build();

        heaven.actionedByWithStringID(id,s);
        heaven.setHeavenOwn(id);
        // Create a new active authority
        Authority authority = new Authority();
        String roleName = "ROLE_" + heaven.getHeavenName().toUpperCase().replaceAll("\\s+", "_")
                .replaceAll("(_)+", "_");
        authority.setRoleName(roleName);
        authority.setStringId(s);
        authority.setRoleDescription(" " + heaven.getHeavenName().toUpperCase());
        authority.setVisible(true); // Assuming "visible" should be a boolean, not a string
        authority.actionedBy(id);
        // Save the authority first
        Response<Authority> authorityResponse = authorityService.saveAuthority(authority);
        if(authorityResponse.isSuccess()){
            authorities.add(authorityResponse.getData());
        }
        heaven.setAuthorities(authorities);
        // Save the heaven
        Heaven savedHeaven = heavenRepository.save(heaven);
        return Response.successfulResponse("Successful", HeavenDto.toDTO(savedHeaven));
    }

    public Response<String> deleteHeavenById(MyUserDto myUserDto, String stringId) {
        Optional<Heaven> optionalHeaven = heavenRepository.findFirstByHeavenNameOrStringId(stringId, stringId);
        if (optionalHeaven.isEmpty()) {
            return Response.failedResponse(HttpStatus.NOT_FOUND.value(), "Heaven not found");
        }
        var userId = myUserDto.getId();
        Heaven heaven = optionalHeaven.get();
        if (heaven.getHeavenOwn().equals(userId) || heaven.getCreatedBy().equals(userId) ||
                AuthorityHelper.hasSuperAdminAuthority(myUserDto.getAuthorities()) ||
                AuthorityHelper.hasAdminAuthority(myUserDto.getAuthorities())) {
            heaven.setActive(false);
            heavenRepository.save(heaven);
            return Response.successfulResponse("Successful");
        }

        return Response.failedResponse(HttpStatus.UNAUTHORIZED.value(), "Unauthorized");
    }

    @Transactional
    public Response<HeavenDto> updateHeaven(Long heavenId, HeavenRequest heavenRequest,MyUserDto userDto) {
        Optional<Heaven> optionalHeaven = heavenRepository.findById(heavenId);
        if (optionalHeaven.isEmpty()) {
            return Response.failedResponse(HttpStatus.NOT_FOUND.value(), "Heaven not found");
        }
        Heaven existingHeaven = optionalHeaven.get();

        //Set Heaven
        Heaven updatedHeaven = Heaven
                .builder()
                .imageUrl(heavenRequest.getImageUrl())
                .heavenName(heavenRequest.getHeavenName())
                .heavenDescription(heavenRequest.getHeavenDescription())
                .build();

        var userId = userDto.getId();
        if (existingHeaven.getHeavenOwn().equals(userId) || existingHeaven.getCreatedBy().equals(userId) ||
                AuthorityHelper.hasSuperAdminAuthority(userDto.getAuthorities()) ||
                AuthorityHelper.hasAdminAuthority(userDto.getAuthorities())) {
            var StringID = existingHeaven.getStringId();
            List<Authority> authorities = getAuthorities(userId,heavenRequest, existingHeaven);

            // Use Spring's BeanUtils to copy non-null properties from updatedHeaven to existingHeaven
            BeanUtils.copyProperties(updatedHeaven, existingHeaven, Helper.getNullPropertyNames(updatedHeaven));
            existingHeaven.setCreatedBy(existingHeaven.getCreatedBy());
            existingHeaven.setDateUpdated(existingHeaven.getDateUpdated());
            existingHeaven.setDateCreated(existingHeaven.getDateCreated());
            existingHeaven.setUpdatedBy(userId);
            existingHeaven.setStringId(StringID);
            existingHeaven.setAuthorities(authorities);
            Heaven updatedHeavenEntity = heavenRepository.save(existingHeaven);
            return Response.successfulResponse("Heaven updated successfully", HeavenDto.toDTO(updatedHeavenEntity));
        }
        return Response.failedResponse(HttpStatus.UNAUTHORIZED.value(), "Unauthorized");

    }

    private List<Authority> getAuthorities(Long userID, HeavenRequest heavenRequest, Heaven existingHeaven) {
        //get authority by id
        List<Authority> authorities = new ArrayList<>();
        if (!heavenRequest.getAuthorities().isEmpty()) {
            List<Authority> authorityList = authorityService.getAuthorityById(heavenRequest.getAuthorities());
            authorities.addAll(authorityList);
        }

        //Update the Authority that was created by this heaven
        //update the related authority
        var authorityName = "ROLE_" + existingHeaven.getHeavenName().toUpperCase().replaceAll("\\s+", "_")
                .replaceAll("(_)+", "_");
        Optional<Authority> authorityResponse = authorityService.getAuthorityByStringIDOrRoleName(existingHeaven.getStringId(), authorityName);
        if(authorityResponse.isPresent()){
            Authority authority = authorityResponse.get();
            String roleName = "ROLE_" + heavenRequest.getHeavenName().toUpperCase().replaceAll("\\s+", "_")
                    .replaceAll("(_)+", "_");
            authority.setRoleName(roleName);
            authority.setStringId(existingHeaven.getStringId());
            authority.setRoleDescription(" " + heavenRequest.getHeavenName().toUpperCase());
            authority.setVisible(true);
            authority.actionedBy(userID);
            Response<Authority> UpdatedAuthorityResponse = authorityService.updateAuthority(authority.getId(), authority, true);
           //Add updated
            if (UpdatedAuthorityResponse.isSuccess()){
                authorities.add(UpdatedAuthorityResponse.getData());
            }

        }
        return authorities;
    }

    public Response<Page<HeavenDto>> searchHeavens(String keyword, int pageSize, int page,MyUserDto authUser,boolean filterByUser) {
        Pageable pageable = PageRequest.of(page, pageSize, DATE_CREATED);
        Page<Heaven> searchResults = filterByUser ? heavenRepository.findDistinctByHeavenNameContainingIgnoreCaseAndAuthoritiesInOrHeavenOwnAndActiveTrue(keyword,authUser.getAuthorities(),
                        authUser.getId(), pageable) : heavenRepository.findByHeavenNameContainingIgnoreCaseAndActiveTrue(keyword, pageable);
                Page<HeavenDto> newPage = Helper.convertPageToDto(searchResults, HeavenDto::toDTO);
        return Response.successfulResponse("Successful", newPage);
    }

    public Response<MyUserDto> addUserAuthority(String authorityCode, MyUserDto myUserDto) {
        Optional<Authority> optionalAuthority = authorityService.findByAuthorityCode(authorityCode);
        Optional<MyUser> optionalUser = userService.findById(myUserDto.getId());

        if (optionalAuthority.isEmpty() || optionalUser.isEmpty()) {
            return Response.failedResponse(HttpStatus.NOT_FOUND.value(), "Authority or User not found");
        }

        MyUser user = optionalUser.get();
        Authority authority = optionalAuthority.get();

        if (!user.getAuthorities().contains(authority)) {
            user.getAuthorities().add(authority);
            MyUser savedUser = userService.save(user);
            MyUserDto updatedUserDto = MyUserDto.fromMyUser(savedUser);
            return Response.successfulResponse("Authority added to user", updatedUserDto);
        } else {
            return Response.failedResponse(HttpStatus.CONFLICT.value(), "User already has the authority");
        }
    }

    public Response<MyUserDto> removeUserAuthority(String authorityCode, MyUserDto myUserDto) {
        Optional<Authority> optionalAuthority = authorityService.findByAuthorityCode(authorityCode);
        Optional<MyUser> optionalUser = userService.findById(myUserDto.getId());

        if (optionalAuthority.isEmpty() || optionalUser.isEmpty()) {
            return Response.failedResponse(HttpStatus.NOT_FOUND.value(), "Authority or User not found");
        }

        MyUser user = optionalUser.get();
        Authority authority = optionalAuthority.get();

        if (user.getAuthorities().contains(authority)) {
            user.getAuthorities().remove(authority);
            MyUser savedUser = userService.save(user);
            MyUserDto updatedUserDto = MyUserDto.fromMyUser(savedUser);
            return Response.successfulResponse("Authority removed from user", updatedUserDto);
        } else {
            return Response.failedResponse(HttpStatus.NOT_FOUND.value(), "User does not have the authority");
        }
    }
}

