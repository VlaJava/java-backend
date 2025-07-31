package com.avanade.decolatech.viajava.domain.dtos.request.payment;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MercadoPagoNotification {
    /**
     * e.g., "payment.updated", "payment.created"
     */
    private String action;

    @JsonProperty("api_version")
    private String apiVersion;

    private NotificationData data;

    @JsonProperty("date_created")
    private String dateCreated;

    private long id;

    @JsonProperty("live_mode")
    private boolean liveMode;

    @JsonProperty("user_id")
    private long userId;

    /**
     * e.g., "payment"
     */
    private String type;

    @Getter
    @Setter
    @NoArgsConstructor
    public static class NotificationData {
        /**
         * ID of the resource (e.g., payment ID)
         */
        private String id;
    }

    @Override
    public String toString() {
        return "MercadoPagoNotification{" +
                "action='" + action + '\'' +
                ", apiVersion='" + apiVersion + '\'' +
                ", data=" + data +
                ", dateCreated='" + dateCreated + '\'' +
                ", id=" + id +
                ", liveMode=" + liveMode +
                ", userId=" + userId +
                ", type='" + type + '\'' +
                '}';
    }
}
