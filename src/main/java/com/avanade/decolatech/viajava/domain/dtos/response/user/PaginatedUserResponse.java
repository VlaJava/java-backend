package com.avanade.decolatech.viajava.domain.dtos.response.user;

import lombok.*;
import java.util.List;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaginatedUserResponse {
    List<UserResponse> users;
    int current_page;
    long total_items;
    int total_pages;
}
