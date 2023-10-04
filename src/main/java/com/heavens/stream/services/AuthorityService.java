package com.heavens.stream.services;

import com.heavens.stream.enums.CodeType;
import com.heavens.stream.helpers.AuthorityHelper;
import com.heavens.stream.helpers.Helper;
import com.heavens.stream.models.Authority;
import com.heavens.stream.models.MyUser;
import com.heavens.stream.repositories.AuthorityRepository;
import com.heavens.stream.repositories.MyUserRepository;
import com.heavens.stream.response.Response;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Data
public class AuthorityService {

    private final AuthorityRepository authorityRepository;
    private final MyUserRepository myUserRepository;

    public Response<Page<Authority>> getAllAuthorities(int pageSize, int page) {
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Authority> authorityPage = authorityRepository.findAll(pageable);
        return Response.successfulResponse("Successful", authorityPage);
    }

    public Response<Authority> getAuthorityById(Long id) {
        Optional<Authority> authority = authorityRepository.findById(id);
        if (authority.isPresent()) {
            return Response.successfulResponse("Successful", authority.get());
        } else {
            return Response.failedResponse(HttpStatus.NOT_FOUND.value(), "Authority not found");
        }
    }

    public Response<Authority> saveAuthority(Authority authority) {
        authority.setAuthorityCode(Helper.generateRandomCode(6, CodeType.NUMBERS_CHARACTER_UPPER_CASE));
        Authority savedAuthority = authorityRepository.save(authority);
        return Response.successfulResponse("Successful", savedAuthority);
    }

    public Response<String> deactivateAuthority(Long id, Long userId) {
        Optional<Authority> byId = authorityRepository.findById(id);
        Optional<MyUser> userOptional = myUserRepository.findById(userId);
        if (byId.isEmpty() || userOptional.isEmpty()) {
            return Response.failedResponse(HttpStatus.NOT_FOUND.value(), "Not found");
        }

        if (byId.get().getMyUsers().contains(userOptional.get()) || AuthorityHelper.hasSuperAdminAuthority(userOptional.get().getAuthorities())) {
            Authority authority = byId.get();
            authority.setActive(false);
            authorityRepository.save(authority);
            return Response.successfulResponse("Successful");
        }
        return Response.failedResponse(HttpStatus.UNAUTHORIZED.value(), "Unauthorized");
    }

    public Response<Authority> updateAuthority(Long ID, Authority authority, boolean updateStringId) {
        Optional<Authority> existingAuthority = authorityRepository.findById(ID);
        if (existingAuthority.isEmpty()) {
            return Response.failedResponse(HttpStatus.NOT_FOUND.value(), "Authority not found");
        }
        Authority updatedAuthority = existingAuthority.get();
        if (authority.getRoleName() != null) {
            updatedAuthority.setRoleName(authority.getRoleName());
        }
        if (updateStringId) {
            updatedAuthority.setStringId(authority.getStringId());
        }
        if (authority.getRoleDescription() != null) {
            updatedAuthority.setRoleDescription(authority.getRoleDescription());
        }
        if (authority.isVisible() != existingAuthority.get().isVisible()) {
            updatedAuthority.setVisible(authority.isVisible());
        }
        if (authority.isActive() != existingAuthority.get().isActive()) {
            updatedAuthority.setVisible(authority.isActive());
        }
        // Save the updated authority
        updatedAuthority = authorityRepository.save(updatedAuthority);
        return Response.successfulResponse("Successful", updatedAuthority);
    }

    public Response<List<Authority>> searchAuthoritiesByName(String name) {
        List<Authority> authorities = authorityRepository.findByRoleNameContainingIgnoreCase(name);
        if (authorities.isEmpty()) {
            return Response.failedResponse(HttpStatus.NOT_FOUND.value(), "No authorities found with the given name");
        }
        return Response.successfulResponse("Successful", authorities);
    }

    public List<Authority> getAuthorityById(List<Long> id) {
        return authorityRepository.findAllById(id);
    }

    public Optional<Authority> findByAuthorityCode(String authorityCode) {
        return authorityRepository.findByAuthorityCode(authorityCode);
    }

    public Optional<Authority> getAuthorityByStringIDOrRoleName(String stringId, String roleName) {
        return authorityRepository.findFirstByStringIdIgnoreCaseOrRoleNameIgnoreCase(stringId, roleName);
    }
}
