package com.heavens.stream.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity(name = "users")
@Table(name = "users")
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class MyUser extends Actionable implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "username", unique = true)
    private String username;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "account_non_expired", columnDefinition = "boolean default true") // Set default value to false
    public boolean accountNonExpired = true;

    @Column(name = "account_non_locked", columnDefinition = "boolean default true") // Set default value to false
    public boolean accountNonLocked = true;

    @Column(name = "credentials_non_expired", columnDefinition = "boolean default true") // Set default value to false
    public boolean credentialsNonExpired = true;

    @Column(name = "can_delete")
    public boolean delete;

    @Column(name = "can_edit")
    public boolean edit;

    @Column(name = "can_create")
    public boolean create;

    @JsonIgnore
    @JsonManagedReference
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.DETACH,targetEntity = Authority.class)
    @JoinTable(name = "user_authority",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "authority_id"))
    private List<Authority> authorities = new ArrayList<>();

    @Override
    public String toString() {
        return "MyUser{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", accountNonExpired=" + accountNonExpired +
                ", accountNonLocked=" + accountNonLocked +
                ", credentialsNonExpired=" + credentialsNonExpired +
                ", delete=" + delete +
                ", edit=" + edit +
                ", create=" + create +
                '}';
    }
}
