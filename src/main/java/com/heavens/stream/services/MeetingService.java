package com.heavens.stream.services;

import com.heavens.stream.models.Meeting;
import com.heavens.stream.repositories.MeetingRepository;
import com.heavens.stream.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MeetingService {

    private final MeetingRepository meetingRepository;

    @Autowired
    public MeetingService(MeetingRepository meetingRepository) {
        this.meetingRepository = meetingRepository;
    }

    public Response<Page<Meeting>> getAllMeetings(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("id").descending());
        Page<Meeting> meetingPage = meetingRepository.findAll(pageRequest);
        return Response.successfulResponse("Successful", meetingPage);
    }

    public Response<Meeting> getMeetingById(Long id) {
        Optional<Meeting> optionalMeeting = meetingRepository.findById(id);
        return optionalMeeting.map(meeting -> Response.successfulResponse("Successful", meeting))
                .orElse(Response.failedResponse(HttpStatus.NOT_FOUND.value(), "Not found"));
    }

    public Response<Meeting> createMeeting(Meeting meeting, Long createdByUserId) {
        meeting.setCreatedBy(createdByUserId);
        meeting.setActive(true);
        Meeting savedMeeting = meetingRepository.save(meeting);
        return Response.successfulResponse("Meeting created successfully", savedMeeting);
    }

    public Response<Meeting> updateMeeting(Meeting updatedMeeting) {
        Optional<Meeting> optionalMeeting = meetingRepository.findById(updatedMeeting.getId());
        if (optionalMeeting.isEmpty()) {
            return Response.failedResponse(HttpStatus.NOT_FOUND.value(), "Meeting not found");
        }

        Meeting existingMeeting = optionalMeeting.get();
        existingMeeting.setMeetingTitle(updatedMeeting.getMeetingTitle());
        existingMeeting.setHost(updatedMeeting.getHost());
        existingMeeting.setShortDescription(updatedMeeting.getShortDescription());
        existingMeeting.setMeetingMessage(updatedMeeting.getMeetingMessage());
        existingMeeting.setMeetingType(updatedMeeting.getMeetingType());
        existingMeeting.setFeaturePost(updatedMeeting.getFeaturePost());
        existingMeeting.setStringId(updatedMeeting.getStringId());
        existingMeeting.setMeetingUrl(updatedMeeting.getMeetingUrl());
        existingMeeting.setEnableChat(updatedMeeting.isEnableChat());

        Meeting updatedMeetingEntity = meetingRepository.save(existingMeeting);
        return Response.successfulResponse("Meeting updated successfully", updatedMeetingEntity);
    }

    public Response<String> deleteMeetingById(Long id) {
        Optional<Meeting> optionalMeeting = meetingRepository.findById(id);
        if (optionalMeeting.isEmpty()) {
            return Response.failedResponse(HttpStatus.NOT_FOUND.value(), "Meeting not found");
        }

        Meeting meeting = optionalMeeting.get();
        meeting.setActive(false);
        meetingRepository.save(meeting);
        return Response.successfulResponse("Meeting deleted successfully");
    }
}
