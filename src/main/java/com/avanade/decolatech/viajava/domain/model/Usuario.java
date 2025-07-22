package com.avanade.decolatech.viajava.domain.model;

import com.avanade.decolatech.viajava.domain.model.enums.UsuarioRole;
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
@Table(name = "TB_USUARIOS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "NOME", nullable = false)
    private String nome;

    @Column(name = "EMAIL", nullable = false, unique = true)
    private String email;

    @Column(name = "SENHA", nullable = false)
    private String senha;

    @Column(name = "TELEFONE", nullable = false, unique = true)
    private String telefone;

    @Column(name = "ATIVO", nullable = false)
    private boolean ativo;

    @Column(name = "DATA_NASC", nullable = false)
    private LocalDate dataNasc;

    @Column(name = "IMAGEM_PERFIL")
    private String imagemPerfil;

    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL)
    private Role role;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private List<Documento> documentos;

    @CreationTimestamp
    @Column(name = "DATA_CADASTRO", nullable = false)
    private LocalDateTime dataCadastro;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if(this.role.getUsuarioRole() == UsuarioRole.ADMIN) return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"));

        return List.of(new SimpleGrantedAuthority("ROLE_CLIENTE"));
    }

    @Override
    public String getPassword() {
        return this.senha;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isEnabled() {
        return this.ativo;
    }
}
