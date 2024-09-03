package com.user_management_service.model;

import com.user_management_service.enums.*;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.sql.Date;
import java.util.Collection;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "user")
public class User implements UserDetails, Serializable {

    @Id
    @Column(name = "id", nullable = false, unique = true)
    private String id;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "password", nullable = false, unique = true)
    private String password;

    @Column(name = "email", nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

//    @OneToOne
//    @JoinColumn(name = "address_id")
//    @Nullable
//    private Address address;
//
//    @OneToMany(mappedBy = "user")
//    @Nullable
//    private List<Bank> banks;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(name = "profile_picture")
    private String profilePicture;

    @Column(name = "date_of_birth", nullable = false)
    private Date dateOfBirth;

    @Column(name = "last_login", nullable = false)
    private Date lastLogin;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    @Enumerated(EnumType.STRING)
    @Column(name = "language_preference", nullable = false)
    private LanguagePreference languagePreference;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    @Column(name = "notification_preference", nullable = false)
    private NotificationPreference notificationPreference;

    @Column(name = "email_verified", nullable = false, columnDefinition="boolean default false")
    private Boolean emailVerified;

    @Column(name = "phone_verified", nullable = false, columnDefinition="boolean default false")
    private Boolean phoneVerified;

    @Column(name = "o_auth_2", nullable = false, columnDefinition="boolean default false")
    private Boolean OAuth2;

    @Column(name = "is_2f_auth", nullable = false, columnDefinition="boolean default false")
    private Boolean is2FAuth;

    @Column(name = "has_fingerprint", nullable = false, columnDefinition="boolean default false")
    private Boolean hasFingerprint;

    @Column(name = "has_face_id", nullable = false, columnDefinition="boolean default false")
    private Boolean hasFaceId;

    @Column(name = "is_verified", nullable = false, columnDefinition="boolean default false")
    private Boolean isVerified;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(this.role.name()));
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}
