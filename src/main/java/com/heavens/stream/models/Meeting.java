package com.heavens.stream.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.heavens.stream.enums.FeaturePost;
import com.heavens.stream.enums.MeetingType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity(name = "meetings")
@Table(name = "meetings")
@Builder
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class Meeting extends Actionable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "meeting_title", nullable = false)
    private String meetingTitle;

    @Column(name = "meeting_host", nullable = false)
    private String host;

    @Column(name = "short_description")
    private String shortDescription;

    @Column(name = "meeting_message", columnDefinition = "TEXT")
    private String meetingMessage;

    @Column(name = "meeting_type")
    private MeetingType meetingType = MeetingType.URL;

    @Column(name = "feature_meeting")
    private FeaturePost featurePost = FeaturePost.NEW;

    @Column(name = "string_id", nullable = false, unique = true)
    private String stringId;

    @Column(name = "meeting_url", columnDefinition = "TEXT")
    private String meetingUrl;

    @Column(name = "enable_chat", columnDefinition = "boolean default true")
    private boolean enableChat = false;

    @JsonIgnore
    @JsonManagedReference
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "meeting_authority",
            joinColumns = @JoinColumn(name = "meeting_id"),
            inverseJoinColumns = @JoinColumn(name = "authority_id"))
    private List<Authority> authorities = new ArrayList<>();


}
