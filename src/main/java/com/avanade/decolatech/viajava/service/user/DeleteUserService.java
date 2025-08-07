package com.avanade.decolatech.viajava.service.user;

import com.avanade.decolatech.viajava.domain.exception.BusinessException;
import com.avanade.decolatech.viajava.domain.exception.ResourceNotFoundException;
import com.avanade.decolatech.viajava.domain.model.User;
import com.avanade.decolatech.viajava.domain.model.enums.UserRole;
import com.avanade.decolatech.viajava.domain.repository.UserRepository;
import com.avanade.decolatech.viajava.utils.UserExceptionMessages;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class DeleteUserService {

    private final UserRepository userRepository;

    public DeleteUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public void execute(UUID id, User userLogged) {
        User user = userRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                String.format("[%s execute] - %s",
                                        DeleteUserService.class.getName(),
                                        UserExceptionMessages.USER_NOT_FOUND)
                        ));

        if(!user.isActive()) {
            return;
        }

        boolean isAdminOrOwnerOfAccount =
                userLogged.getRole().getUserRole().equals(UserRole.ADMIN) |
                userLogged.getId().equals(user.getId());

        if (!isAdminOrOwnerOfAccount) {
            throw new BusinessException("You don't can delete a account for other user.");
        }

        user.setActive(false);

        userRepository.save(user);
    }
}
