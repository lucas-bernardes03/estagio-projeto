package com.unikasistemas.deangular.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.unikasistemas.deangular.entities.Endereco;

public interface EnderecoRepository extends PagingAndSortingRepository<Endereco,Long>{
    @Query(value = "SELECT * FROM enderecos WHERE monitorador_id = ?1", nativeQuery = true)
    Page<Endereco> findByMonitoradorPageable(Long id, Pageable pageable);

    @Query(value = "SELECT * FROM enderecos WHERE monitorador_id = ?", nativeQuery = true)
    List<Endereco> findByMonitorador(Long id);

    Endereco findTopByOrderByIdDesc();
}
