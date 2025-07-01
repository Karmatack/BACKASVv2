package com.adoptasalvavidas.adoptasalvavidasBack.Respository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.adoptasalvavidas.adoptasalvavidasBack.Models.Entity.PasswordResetToken;
import com.adoptasalvavidas.adoptasalvavidasBack.Models.Entity.Usuario;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, String> {
    Optional<PasswordResetToken> findByToken(String token);
    Optional<PasswordResetToken> findByUsuario(Usuario usuario);
}
