package com.heavens.stream.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity(name = "authorities")
@Table(name = "authorities")
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
@Builder
public class Authority extends Actionable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "role_name", nullable = false, unique = true)
    private String roleName;

    @Column(name = "role_description")
    private String roleDescription;

    @Column(name = "authority_code", nullable = false, unique = true)
    private String authorityCode;

    @Column(name = "visible", columnDefinition = "boolean default false")
    private boolean visible;


    @JsonIgnore
    @ManyToMany(mappedBy = "authorities", fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
    @JsonBackReference
    private List<MyUser> myUsers;

    @JsonIgnore
    @JsonBackReference
    @ManyToMany(mappedBy = "authorities", fetch = FetchType.LAZY)
    private List<Heaven> heavens;

    @JsonIgnore
    @JsonBackReference
    @ManyToMany(mappedBy = "authorities", fetch = FetchType.LAZY)
    private List<Meeting> meetings;

    @Override
    public String toString() {
        return "Authority{" +
                "id=" + id +
                ", roleName='" + roleName + '\'' +
                ", roleDescription='" + roleDescription + '\'' +
                ", authorityCode='" + authorityCode + '\'' +
                ", visible='" + visible + '\'' +
                '}';
    }
}
