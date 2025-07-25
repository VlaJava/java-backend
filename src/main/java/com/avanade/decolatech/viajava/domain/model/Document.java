package com.avanade.decolatech.viajava.domain.model;

import com.avanade.decolatech.viajava.domain.model.enums.TipoDocumento;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "TB_DOCUMENTOS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class Documento {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "ID")
    private UUID id;

    @Column(name = "NUMERO_DOCUMENTO", nullable = false)
    private String numeroDocumento;

    @ManyToOne
    @JoinColumn(name = "USUARIO_ID", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "TIPO_DOCUMENTO", nullable = false)
    private TipoDocumento tipoDocumento;
}
