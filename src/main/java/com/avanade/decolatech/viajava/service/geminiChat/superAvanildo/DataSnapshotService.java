package com.avanade.decolatech.viajava.service.geminiChat.superAvanildo;

import com.avanade.decolatech.viajava.domain.repository.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class DataSnapshotService {

    private final UserRepository userRepository;
    private final PackageRepository packageRepository;
    private final BookingRepository bookingRepository;
    private final ReviewRepository reviewRepository;
    private final DocumentRepository documentRepository;
    private final PaymentRepository paymentRepository;
    private final RoleRepository roleRepository;
    private final ObjectMapper objectMapper;

    public DataSnapshotService(UserRepository userRepository, PackageRepository packageRepository, BookingRepository bookingRepository, ReviewRepository reviewRepository, DocumentRepository documentRepository, PaymentRepository paymentRepository, RoleRepository roleRepository, ObjectMapper objectMapper) {
        this.userRepository = userRepository;
        this.packageRepository = packageRepository;
        this.bookingRepository = bookingRepository;
        this.reviewRepository = reviewRepository;
        this.documentRepository = documentRepository;
        this.paymentRepository = paymentRepository;
        this.roleRepository = roleRepository;
        this.objectMapper = objectMapper;
    }

    public String getDatabaseSnapshotAsJson() throws JsonProcessingException {
        Map<String, Object> databaseSnapshot = new HashMap<>();
        databaseSnapshot.put("users", userRepository.findAll());
        databaseSnapshot.put("documents", documentRepository.findAll());
        databaseSnapshot.put("roles", roleRepository.findAll());
        databaseSnapshot.put("packages", packageRepository.findAll());
        databaseSnapshot.put("bookings", bookingRepository.findAll());
        databaseSnapshot.put("payments", paymentRepository.findAll());
        databaseSnapshot.put("reviews", reviewRepository.findAll());

        return objectMapper.writeValueAsString(databaseSnapshot);
    }
}