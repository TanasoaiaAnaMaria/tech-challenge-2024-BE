package com.usv.Team.Finder.App.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Set;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID idUser;

    private String firstName;

    private String lastName;

    @Column(unique = true)
    private String eMailAdress;

    private String password;

    private UUID idOrganisation;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_role",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id")}
    )
    private Set<Role> authorities;

    private Boolean isDepartmentManager = false;

    private UUID idDepartmentManager;

    private UUID idDepartment;

    @OneToMany(
            targetEntity = Skill.class,
            fetch = FetchType.LAZY,
            cascade = CascadeType.PERSIST
    )
    @JoinColumn(name="createdBy", referencedColumnName = "idUser")
    private Set<Skill> skilsCreated;

    @OneToMany(
            targetEntity = Notifications.class,
            fetch = FetchType.LAZY,
            cascade = CascadeType.PERSIST
    )
    @JoinColumn(name="addressedTo", referencedColumnName = "idUser")
    private Set<Skill> notifications;

    public User(String eMailAdress, String password, Set<Role> authorities) {
        this.eMailAdress = eMailAdress;
        this.password = password;
        this.authorities = authorities;
    }

    @JsonIgnore
    @Override
    public Set<Role> getAuthorities() {
        return authorities;
    }

    @JsonIgnore
    @Override
    public String getPassword() {
        return this.password;
    }

    @JsonIgnore
    @Override
    public String getUsername() {
        return this.eMailAdress;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return true;
    }
}
