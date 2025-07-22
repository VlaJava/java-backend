package com.avanade.decolatech.viajava.domain.model;

import com.avanade.decolatech.viajava.domain.model.enums.UsuarioRole;
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
    private Usuario usuario;

    @Enumerated(EnumType.STRING)
    @Column(name = "ROLE")
    private UsuarioRole usuarioRole;
}
