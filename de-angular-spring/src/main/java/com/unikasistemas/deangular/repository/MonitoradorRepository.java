package com.unikasistemas.deangular.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.unikasistemas.deangular.entities.Monitorador;

public interface MonitoradorRepository extends JpaRepository<Monitorador,Long>{
    Monitorador findTopByOrderByIdDesc();
}
