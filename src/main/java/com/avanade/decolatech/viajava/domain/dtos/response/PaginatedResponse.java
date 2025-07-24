package com.avanade.decolatech.viajava.domain.dtos.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PaginatedResponse<T> {

    private List<T> content;
    private int currentPage;
    private long totalItems;
    private int totalPages;

}