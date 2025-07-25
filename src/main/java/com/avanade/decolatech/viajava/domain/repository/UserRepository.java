package com.avanade.decolatech.viajava.domain.repository;

import com.avanade.decolatech.viajava.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByEmail(String email);
    Optional<User> findByPhone(String telefone);

    @Query("SELECT U FROM User U WHERE U.email = :username")
    UserDetails findByUsername(String username);

}
