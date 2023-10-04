package com.heavens.stream.controllers;

import com.heavens.stream.models.Authority;
import com.heavens.stream.response.Response;
import com.heavens.stream.services.AuthorityService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/authorities")
@RequiredArgsConstructor
public class AuthorityController {

    private final AuthorityService authorityService;

    @GetMapping("/get")
    public ResponseEntity<Page<Authority>> getAllAuthorities(@RequestParam(defaultValue = "10") int pageSize, @RequestParam int page) {
        Response<Page<Authority>> response = authorityService.getAllAuthorities(pageSize, page);
        return ResponseEntity.status(response.getStatusCode()).body(response.getData());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Authority> getAuthorityById(@PathVariable Long id) {
        Response<Authority> response = authorityService.getAuthorityById(id);
        return ResponseEntity.status(response.getStatusCode()).body(response.getData());
    }

    @PostMapping("/add")
    public ResponseEntity<Authority> saveAuthority(@RequestBody Authority authority) {
        Response<Authority> response = authorityService.saveAuthority(authority);
        return ResponseEntity.status(response.getStatusCode()).body(response.getData());
    }

    @DeleteMapping("/{id}/{userId}")
    public ResponseEntity<Response<String>> deleteAuthority(@PathVariable Long id, @PathVariable Long userId) {
        Response<String> response = authorityService.deactivateAuthority(id, userId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
