package com.avanade.decolatech.viajava.service.pacote;

import com.avanade.decolatech.viajava.domain.model.Package;
import com.avanade.decolatech.viajava.domain.repository.PackageRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
public class GetFilterPackagesService {

    private final PackageRepository packageRepository;

    public GetFilterPackagesService(PackageRepository packageRepository) {
        this.packageRepository = packageRepository;
    }

    public Page<Package> execute(
            String source,
            String destination,
            LocalDate startDate,
            LocalDate endDate,
            BigDecimal price,
            int page,
            int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Specification<Package> spec = (root, query, cb) -> cb.isTrue(root.get("available"));

        if (source != null) {
            spec = spec.and((root, query, cb) -> cb.like(cb.lower(root.get("source")), "%" + source.toLowerCase() + "%"));
        }
        if (destination != null) {
            spec = spec.and((root, query, cb) -> cb.like(cb.lower(root.get("destination")), "%" + destination.toLowerCase() + "%"));
        }
        if (startDate != null) {
            spec = spec.and((root, query, cb) -> cb.greaterThanOrEqualTo(root.get("startDate"), startDate));
        }
        if (endDate != null) {
            spec = spec.and((root, query, cb) -> cb.lessThanOrEqualTo(root.get("endDate"), endDate));
        }
        if (price != null) {
            spec = spec.and((root, query, cb) -> cb.lessThanOrEqualTo(root.get("price"), price));
        }
        return packageRepository.findAll(spec, pageable);
    }
}
