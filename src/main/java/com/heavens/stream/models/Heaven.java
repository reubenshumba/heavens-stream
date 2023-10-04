package com.heavens.stream.models;

import com.fasterxml.jackson.annotation.*;
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
@Entity(name = "heavens")
@Table(name = "heavens")
@Builder
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class Heaven extends Actionable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "heaven_name", nullable = false, unique = true)
    private String heavenName;

    @Column(name = "heaven_description")
    private String heavenDescription;

    @Column(name = "image_url")
    private String imageUrl;

    private Long heavenOwn;

    @JsonIgnore
    @JsonManagedReference
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "heaven_authority",
            joinColumns = @JoinColumn(name = "heaven_id"),
            inverseJoinColumns = @JoinColumn(name = "authority_id"))
    private List<Authority> authorities = new ArrayList<>();

    @JsonIgnore
    @JsonBackReference
    @ManyToMany(mappedBy = "heavens", fetch = FetchType.LAZY)
    private List<Post> posts;

    @Override
    public String toString() {
        return "Heaven{" +
                "id=" + id +
                ", heavenName='" + heavenName + '\'' +
                ", heavenDescription='" + heavenDescription + '\'' +
                ", stringId='" + stringId + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", heavenOwn=" + heavenOwn +
                ", active=" + active +
                ", dateCreated=" + dateCreated +
                ", dateUpdated=" + dateUpdated +
                ", updatedBy=" + updatedBy +
                ", createdBy=" + createdBy +
                '}';
    }
}
