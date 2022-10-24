package com.unikasistemas.deangular.repository;

import com.unikasistemas.deangular.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario,Long> {
}
