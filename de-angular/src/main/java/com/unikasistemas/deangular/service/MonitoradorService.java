package com.unikasistemas.deangular.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.unikasistemas.deangular.entities.Monitorador;
import com.unikasistemas.deangular.repository.MonitoradorRepository;

@Service
public class MonitoradorService {
    @Autowired
    private MonitoradorRepository repository;

    public List<Monitorador> findAll(){
        return repository.findAll();
    }

    public Monitorador findById(Long id){
        return repository.findById(id).get();
    }

    public Monitorador insertMonitorador(Monitorador monitorador){
        return repository.save(monitorador);
    }

    public Monitorador updateMonitorador(Monitorador novo, Long idAtual){
        Monitorador atual = repository.findById(idAtual).get();
        updateData(novo, atual);
        return repository.save(atual);
    }

    public void deleteMonitorador(Long id){
        repository.deleteById(id);
    }

    private void updateData(Monitorador novo, Monitorador atual){
        atual.setTipo(novo.getTipo());
        atual.setIdentificacao(novo.getIdentificacao());
        atual.setCadastro(novo.getCadastro());
        atual.setRegistro(novo.getCadastro());
        atual.setDataNascimento(novo.getDataNascimento());
        atual.setEmail(novo.getEmail());
        atual.setAtivo(novo.isAtivo());
    }
}
