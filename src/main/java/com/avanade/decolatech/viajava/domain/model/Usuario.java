package com.avanade.decolatech.viajava.domain.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "TB_USUARIOS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class Usuario {

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

    @CreationTimestamp
    @Column(name = "DATA_CADASTRO", nullable = false)
    private LocalDateTime dataCadastro;
}
