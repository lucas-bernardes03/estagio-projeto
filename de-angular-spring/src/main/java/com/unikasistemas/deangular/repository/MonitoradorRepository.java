package com.unikasistemas.deangular.repository;

import com.unikasistemas.deangular.entities.Monitorador;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface MonitoradorRepository extends PagingAndSortingRepository<Monitorador,Long>{
    Monitorador findTopByOrderByIdDesc();
    Page<Monitorador> findByTipo(String tipo, Pageable pageable);

    @Query(value = "SELECT * FROM monitoradores WHERE UPPER(razao_social) LIKE ?1 OR UPPER(cnpj) LIKE ?1 OR UPPER(inscricao_estadual) LIKE ?1", nativeQuery = true)
    Page<Monitorador> findSearchJuridica(String search, Pageable pageable);

    @Query(value = "SELECT * FROM monitoradores WHERE UPPER(nome) LIKE ?1 OR UPPER(cpf) LIKE ?1 OR UPPER(rg) LIKE ?1", nativeQuery = true)
    Page<Monitorador> findSearchFisica(String search, Pageable pageable);
}
