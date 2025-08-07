package com.avanade.decolatech.viajava.domain.repository;

import com.avanade.decolatech.viajava.domain.dtos.response.view.PopularDestinationView;
import com.avanade.decolatech.viajava.domain.model.Package;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface PackageRepository extends JpaRepository<Package, UUID>, JpaSpecificationExecutor<Package> {

    @Query(value = "SELECT " +
            "DESTINATION AS destination, " +
            "BOOKINGS AS bookings, " +
            "REVENUE AS revenue " +
            "FROM vw_popular_destinations",
            nativeQuery = true)
    List<PopularDestinationView> findPopularDestinations();

}