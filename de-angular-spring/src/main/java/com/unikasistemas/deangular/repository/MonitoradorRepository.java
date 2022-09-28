package com.unikasistemas.deangular.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.unikasistemas.deangular.entities.Monitorador;

public interface MonitoradorRepository extends PagingAndSortingRepository<Monitorador,Long>{
    Monitorador findTopByOrderByIdDesc();
    Page<Monitorador> findByTipo(String tipo, Pageable pageable);
}
