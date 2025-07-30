package com.avanade.decolatech.viajava.service.payment;

import com.avanade.decolatech.viajava.domain.model.PaymentEntity;
import com.avanade.decolatech.viajava.domain.repository.PaymentRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GetAllPaymentsService {

    private final PaymentRepository paymentRepository;

    public GetAllPaymentsService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Transactional(readOnly = true)
    public Page<PaymentEntity> execute(Integer page, Integer size) {
        return this.paymentRepository.findAll(PageRequest.of(page, size));
    }
}
