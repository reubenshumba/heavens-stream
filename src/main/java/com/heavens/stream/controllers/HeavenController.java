package com.heavens.stream.controllers;

import com.heavens.stream.dtos.HeavenDto;
import com.heavens.stream.dtos.MyUserDto;
import com.heavens.stream.models.Heaven;
import com.heavens.stream.models.MyUser;
import com.heavens.stream.request.HeavenRequest;
import com.heavens.stream.response.AuthenticationResponse;
import com.heavens.stream.response.Response;
import com.heavens.stream.services.HeavenService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/heavens")
@RequiredArgsConstructor
@Slf4j
public class HeavenController {

    private final HeavenService heavenService;

    @GetMapping("/all")
    public ResponseEntity<Response<Page<HeavenDto>>> getAllHeavens(@RequestParam(defaultValue = "0") int page,
                                                                   @RequestParam(defaultValue = "10") int pageSize,
                                                                   @RequestParam(required = false,defaultValue = "false") boolean byUser,
                                                                   @NotNull @AuthenticationPrincipal MyUserDto user) {

        Response<Page<HeavenDto>> heavensPage = heavenService.getAllHeavens(pageSize, page,user,byUser );
        return ResponseEntity.ok(heavensPage);
    }

    @GetMapping("/search-heavens")
    public ResponseEntity<Response<Page<HeavenDto>>> searchHeavens(@RequestParam(defaultValue = "0") int page,
                                                                   @RequestParam(defaultValue = "50") int pageSize,
                                                                   @RequestParam String search,
                                                                   @RequestParam(required = false, defaultValue = "false") boolean byUser,
                                                                   @NotNull @AuthenticationPrincipal MyUserDto user) {

        Response<Page<HeavenDto>> heavensPage = heavenService.searchHeavens(search,pageSize, page, user, byUser);
        return ResponseEntity.ok(heavensPage);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Response<HeavenDto>> getHeavenById(@PathVariable Long id) {
        Response<HeavenDto> heaven = heavenService.getHeavenById(id);
        return ResponseEntity.status(heaven.getStatusCode()).body(heaven);
    }

    @PostMapping("/save")
    public ResponseEntity<Response<HeavenDto>> createHeaven(@RequestBody HeavenRequest heaven, @AuthenticationPrincipal MyUserDto auth) {
        Response<HeavenDto> savedHeaven = heavenService.saveHeaven(heaven, auth.getId());
        return ResponseEntity.status(savedHeaven.getStatusCode()).body(savedHeaven);
    }

    @PutMapping("/edit/{heavenId}")
    public ResponseEntity<Response<HeavenDto>> updateHeaven(@PathVariable Long heavenId,
                                                            @RequestBody HeavenRequest heaven, @AuthenticationPrincipal MyUserDto auth) {
        Response<HeavenDto> savedHeaven = heavenService.updateHeaven(heavenId, heaven, auth);
        return ResponseEntity.status(savedHeaven.getStatusCode()).body(savedHeaven);
    }

    @DeleteMapping("/{stringId}")
    public ResponseEntity<Response<String>> deleteHeavenById(@PathVariable String stringId,
                                                             @AuthenticationPrincipal MyUserDto myUserDto) {
        Response<String> response = heavenService.deleteHeavenById(myUserDto, stringId);
        return ResponseEntity.status(response.getStatusCode()).body(response);    }

    @PutMapping("/join-heaven/{authorityCode}")
    public ResponseEntity<Response<MyUserDto>> addUserAuthority(@PathVariable String authorityCode,
                                                                       @NotNull @AuthenticationPrincipal MyUserDto myUserDto) {
        Response<MyUserDto> savedUserRequest = heavenService.addUserAuthority(authorityCode, myUserDto);
        return ResponseEntity.status(savedUserRequest.getStatusCode()).body(savedUserRequest);
    }

    @PutMapping("/exit-heaven/{authorityCode}")
    public ResponseEntity<Response<MyUserDto>> removeUserAuthority(@PathVariable String authorityCode,
                                                          @NotNull @AuthenticationPrincipal MyUserDto myUserDto) {
        Response<MyUserDto> savedUserRequest = heavenService.removeUserAuthority(authorityCode, myUserDto);
        return ResponseEntity.status(savedUserRequest.getStatusCode()).body(savedUserRequest);
    }
}
