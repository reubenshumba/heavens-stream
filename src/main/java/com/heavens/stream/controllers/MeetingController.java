package com.heavens.stream.controllers;

import com.heavens.stream.dtos.MyUserDto;
import com.heavens.stream.models.Meeting;
import com.heavens.stream.response.Response;
import com.heavens.stream.services.MeetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/meetings")
public class MeetingController {

    private final MeetingService meetingService;

    @Autowired
    public MeetingController(MeetingService meetingService) {
        this.meetingService = meetingService;
    }

    @PostMapping
    public ResponseEntity<Response<Meeting>> createMeeting(@RequestBody Meeting meeting, @AuthenticationPrincipal MyUserDto myUserDto) {
        Long createdByUserId = myUserDto.getId();
        Response<Meeting> response = meetingService.createMeeting(meeting, createdByUserId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PutMapping("/{meetingId}")
    public ResponseEntity<Response<Meeting>> updateMeeting(@PathVariable Long meetingId, @RequestBody Meeting updatedMeeting) {
        updatedMeeting.setId(meetingId);
        Response<Meeting> response = meetingService.updateMeeting(updatedMeeting);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/{meetingId}")
    public ResponseEntity<Response<Meeting>> getMeeting(@PathVariable Long meetingId) {
        Response<Meeting> response = meetingService.getMeetingById(meetingId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping("/{meetingId}")
    public ResponseEntity<Response<String>> deleteMeeting(@PathVariable Long meetingId) {
        Response<String> response = meetingService.deleteMeetingById(meetingId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
