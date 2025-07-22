package com.avanade.decolatech.viajava.config.security;

import com.avanade.decolatech.viajava.domain.model.Role;
import com.avanade.decolatech.viajava.domain.model.Usuario;
import com.avanade.decolatech.viajava.domain.model.enums.UsuarioRole;
import com.avanade.decolatech.viajava.domain.repository.RoleRepository;
import com.avanade.decolatech.viajava.domain.repository.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;


/**
 * Classe responsável por encriptar a senha do admin a nível de banco de dados.
 */
@Configuration
public class AdminUsuarioConfig implements CommandLineRunner {


    private final Logger logger = LoggerFactory.getLogger(AdminUsuarioConfig.class);
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminUsuarioConfig(UsuarioRepository usuarioRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    @Override
    public void run(String... args) throws Exception {
        String email = "adminmain@email.com";
        this.updateUsersPassword();

        var admin = this.usuarioRepository.findByEmail(email);

        admin.ifPresentOrElse(
                user -> {
                    this.logger.info("admin ja existe");
                },
                () -> {
                    Usuario usuario = Usuario
                            .builder()
                            .nome("Administrador")
                            .email(email)
                            .senha(this.passwordEncoder.encode("senha1234"))
                            .telefone("21992477508")
                            .ativo(true)
                            .dataNasc(LocalDate.of(1990, 1, 1))
                            .build();

                    Role role = Role
                            .builder()
                            .usuario(usuario)
                            .usuarioRole(UsuarioRole.ADMIN)
                            .build();

                    usuario.setRole(role);

                    usuarioRepository.save(usuario);
                });

    }


    @Transactional
    public void updateUsersPassword() {
        usuarioRepository.findAll().forEach(user -> {
            if(!isBCryptHash(user.getSenha())) {
                user.setSenha(this.passwordEncoder.encode(user.getSenha()));
                this.usuarioRepository.save(user);
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
