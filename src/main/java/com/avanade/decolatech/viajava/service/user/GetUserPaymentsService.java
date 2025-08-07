package com.avanade.decolatech.viajava.service.user;

import com.avanade.decolatech.viajava.domain.dtos.response.payment.PaymentUserResponse;
import com.avanade.decolatech.viajava.domain.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class GetUserPaymentsService {

    private final UserRepository userRepository;

    public GetUserPaymentsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public Page<PaymentUserResponse> execute(UUID userId, Integer page, Integer size) {
        return this
                .userRepository
                .findAllPaymentsByUserId(userId, PageRequest.of(page, size));
    }
}
