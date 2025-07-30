package com.avanade.decolatech.viajava.service.user;

import com.avanade.decolatech.viajava.domain.dtos.request.user.UpdateRoleRequest;
import com.avanade.decolatech.viajava.domain.exception.ResourceNotFoundException;
import com.avanade.decolatech.viajava.domain.model.Role;
import com.avanade.decolatech.viajava.domain.model.enums.UserRole;
import com.avanade.decolatech.viajava.domain.repository.UserRepository;
import com.avanade.decolatech.viajava.utils.UserExceptionMessages;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class UpdateUserRoleService {

    private final UserRepository userRepository;

    public UpdateUserRoleService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public void updateRole(UpdateRoleRequest request) {
        this.userRepository.findByEmail(request.email())
                .ifPresentOrElse(user -> {
                    Role role = user.getRole();
                    role.setUserRole(UserRole.valueOf(request.userRole()));
                    user.setRole(role);
                    this.userRepository.save(user);
                        },
                        () -> {
                    throw new ResourceNotFoundException(UserExceptionMessages.USER_NOT_FOUND);
                        });
    }
}
