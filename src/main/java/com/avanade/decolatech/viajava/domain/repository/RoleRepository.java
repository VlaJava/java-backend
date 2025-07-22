package com.avanade.decolatech.viajava.domain.repository;

import com.avanade.decolatech.viajava.domain.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RoleRepository extends JpaRepository<Role, UUID> {
}
