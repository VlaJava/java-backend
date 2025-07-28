package com.avanade.decolatech.viajava.domain.mapper;

import com.avanade.decolatech.viajava.domain.dtos.request.TravelerRequest;
import com.avanade.decolatech.viajava.domain.dtos.response.TravelerResponse;
import com.avanade.decolatech.viajava.domain.model.Traveler;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TravelerMapper {

    Traveler toTraveler(TravelerRequest request);

    TravelerResponse toResponse(Traveler traveler);

    List<TravelerResponse> toResponseList(List<Traveler> travelers);
}
