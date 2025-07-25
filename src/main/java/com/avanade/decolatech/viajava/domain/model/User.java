package com.avanade.decolatech.viajava.domain.model;

import com.avanade.decolatech.viajava.domain.model.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "TB_USERS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "EMAIL", nullable = false, unique = true)
    private String email;

    @Column(name = "PASSWORD", nullable = false)
    private String password;

    @Column(name = "PHONE", nullable = false, unique = true)
    private String phone;

    @Column(name = "ACTIVE", nullable = false)
    private boolean active;

    @Column(name = "BIRTHDATE", nullable = false)
    private LocalDate birthdate;

    @Column(name = "IMAGE_URL")
    private String imageUrl;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Role role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Document> documents;

    @CreationTimestamp
    @Column(name = "CREATED_AT", nullable = false)
    private LocalDateTime createdAt;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if(this.role.getUserRole() == UserRole.ADMIN) return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"));

        return List.of(new SimpleGrantedAuthority("ROLE_CLIENT"));
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isEnabled() {
        return this.active;
    }
}
