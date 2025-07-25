package com.avanade.decolatech.viajava.config.security;

import com.avanade.decolatech.viajava.domain.model.Role;
import com.avanade.decolatech.viajava.domain.model.User;
import com.avanade.decolatech.viajava.domain.model.enums.UserRole;
import com.avanade.decolatech.viajava.domain.repository.RoleRepository;
import com.avanade.decolatech.viajava.domain.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;


/**
 * Classe responsável por encriptar a password do admin a nível de banco de dados.
 */
@Configuration
public class AdminUserConfig implements CommandLineRunner {


    private final Logger logger = LoggerFactory.getLogger(AdminUserConfig.class);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminUserConfig(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    @Override
    public void run(String... args) throws Exception {
        String email = "adminmain@email.com";
        this.updateUsersPassword();

        var admin = this.userRepository.findByEmail(email);

        admin.ifPresentOrElse(
                user -> {
                    this.logger.info("admin ja existe");
                },
                () -> {
                    User user = User
                            .builder()
                            .name("Administrador")
                            .email(email)
                            .password(this.passwordEncoder.encode("senha1234"))
                            .phone("21992477508")
                            .active(true)
                            .birthdate(LocalDate.of(1990, 1, 1))
                            .build();

                    Role role = Role
                            .builder()
                            .user(user)
                            .userRole(UserRole.ADMIN)
                            .build();

                    user.setRole(role);

                    userRepository.save(user);
                });

    }


    @Transactional
    public void updateUsersPassword() {
        userRepository.findAll().forEach(user -> {
            if(!isBCryptHash(user.getPassword())) {
                user.setPassword(this.passwordEncoder.encode(user.getPassword()));
                this.userRepository.save(user);
            }

        });
    }

    public boolean isBCryptHash(String password) {
        if (password == null
                || password.length() != 60
                || !password.startsWith("$2")) return false;

        String[] parts = password.split("\\$");
        if (parts.length != 4) return false;

        String version = parts[1];
        String cost = parts[2];
        String saltAndHash = parts[3];

        return (version.equals("2a") || version.equals("2b") || version.equals("2y")) &&
                cost.matches("^[0-9]{2}$") &&
                saltAndHash.length() == 53 &&
                saltAndHash.matches("^[A-Za-z0-9./]+$");

    }

}
