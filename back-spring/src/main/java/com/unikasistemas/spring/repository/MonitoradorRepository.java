package com.unikasistemas.spring.repository;

import com.unikasistemas.spring.entities.Monitorador;
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

    Long countByCpf(String cpf);
    Long countByCnpj(String cnpj);
    Long countByRg(String rg);
    Long countByInscricaoEstadual(String inscricaoEstadual);

    @Query(value = "SELECT COUNT(cpf) from monitoradores where id != ?1 and cpf = ?2", nativeQuery = true)
    Long updateCountCpf(Long id, String cpf);

    @Query(value = "SELECT COUNT(rg) from monitoradores where id != ?1 and rg = ?2", nativeQuery = true)
    Long updateCountRg(Long id, String rg);

    @Query(value = "SELECT COUNT(cnpj) from monitoradores where id != ?1 and cnpj = ?2", nativeQuery = true)
    Long updateCountCnpj(Long id, String cnpj);

    @Query(value = "SELECT COUNT(inscricao_estadual) from monitoradores where id != ?1 and inscricao_estadual = ?2", nativeQuery = true)
    Long updateCountIns(Long id, String ins);
}
