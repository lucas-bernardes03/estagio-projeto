package com.unikasistemas.deangular.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.unikasistemas.deangular.entities.Monitorador;

public interface MonitoradorRepository extends PagingAndSortingRepository<Monitorador,Long>{
    Monitorador findTopByOrderByIdDesc();
    Page<Monitorador> findByTipo(String tipo, Pageable pageable);

    @Query(value = "SELECT * FROM monitoradores WHERE razao_social LIKE ?1 OR cnpj LIKE ?1 OR inscricao_estadual LIKE ?1", nativeQuery = true)
    Page<Monitorador> findSearchJuridica(String search, Pageable pageable);

    @Query(value = "SELECT * FROM monitoradores WHERE nome LIKE ?1 OR cpf LIKE ?1 OR rg LIKE ?1", nativeQuery = true)
    Page<Monitorador> findSearchFisica(String search, Pageable pageable);
}
