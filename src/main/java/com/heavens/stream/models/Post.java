package com.heavens.stream.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.heavens.stream.enums.FeaturePost;
import com.heavens.stream.enums.PostType;
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
@Entity(name = "posts")
@Table(name = "posts")
@Builder
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class Post extends Actionable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "post_title", nullable = false, unique = true)
    private String postTitle;

    @Column(name = "short_description" )
    private String shortDescription;

    @Column(name = "post_message", columnDefinition = "TEXT")
    private String postMessage;

    @Column(name = "post_type")
    private PostType postType = PostType.MESSAGE;

    @Column(name = "feature_post")
    private FeaturePost featurePost = FeaturePost.NEW;

    @Column(name = "string_id", nullable = false, unique = true)
    private String stringId;

    @Column(name = "image_url")
    private String imageUrl;


    @JsonIgnore
    @JsonManagedReference
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "post_heaven",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "heaven_id"))
    private List<Heaven> heavens = new ArrayList<>();


}
