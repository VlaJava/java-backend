package com.avanade.decolatech.viajava.domain.model;

import com.avanade.decolatech.viajava.domain.model.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "TB_ROLES")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne
    @JoinColumn(name = "USUARIO_ID", referencedColumnName = "ID")
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "ROLE")
    private UserRole userRole;
}
