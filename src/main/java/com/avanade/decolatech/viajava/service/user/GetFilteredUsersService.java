package com.avanade.decolatech.viajava.service.user;

import com.avanade.decolatech.viajava.domain.model.User;
import com.avanade.decolatech.viajava.domain.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GetFilteredUsersService {

    private final UserRepository userRepository;


    public GetFilteredUsersService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public Page<User> execute(
            String name,
            String email,
            String documentNumber,
            int pageNumber,
            int size
    ) {

        Pageable pageable = PageRequest.of(pageNumber, size);
        Specification<User> spec = (root, query, cb) -> cb.isTrue(root.get("active"));

        if(name != null) {
            spec.and((root, query, cb) -> cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
        }

        if(email != null) {
            spec.and((root, query, cb) -> cb.like(cb.lower(root.get("email")), "%" + email.toLowerCase() + "%"));
        }

        if(documentNumber != null) {
            spec.and((root, query, cb) -> cb.equal(root.get("documentNumber"), documentNumber));
        }

        return this.userRepository.findAll(spec, pageable);
    }
}
