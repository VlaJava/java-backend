package com.avanade.decolatech.viajava.domain.model.enums;

public enum DomainPaymentStatus {
    PENDING,
    APPROVED,
    AUTHORIZED,
    IN_PROCESS,
    IN_MEDIATION,
    REJECTED,
    CANCELLED,
    REFUNDED,
    CHARGED_BACK,
    UNKNOWN;

    String value;

    DomainPaymentStatus(){}

    public String getValue(){
        return this.value;
    }
}
