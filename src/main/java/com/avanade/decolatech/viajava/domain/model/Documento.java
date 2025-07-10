package com.avanade.decolatech.viajava.domain.model;

import com.avanade.decolatech.viajava.domain.model.enums.TipoDocumento;
import jakarta.persistence.*;
import lombok.*;

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
    @Column(name = "NUMERO_DOCUMENTO", nullable = false)
    private String numeroDocumento;

    @ManyToOne
    @JoinColumn(name = "USUARIO_ID", nullable = false)
    private Usuario usuario;

    @Enumerated(EnumType.STRING)
    @Column(name = "TIPO_DOCUMENTO", nullable = false)
    private TipoDocumento tipoDocumento;
}
