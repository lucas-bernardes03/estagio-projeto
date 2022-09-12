package com.unikasistemas.deangular.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.unikasistemas.deangular.entities.Endereco;

public interface EnderecoRepository extends JpaRepository<Endereco,Long>{
    @Query(value = "SELECT * FROM enderecos WHERE monitorador_id = ?", nativeQuery = true)
    List<Endereco> findByMonitorador(Long id);
}
