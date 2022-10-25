package com.unikasistemas.deangular.service;

import com.unikasistemas.deangular.entities.Endereco;
import com.unikasistemas.deangular.repository.EnderecoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EnderecoService {
    @Autowired
    private EnderecoRepository repository;

    public List<Endereco> findAll(){
        return (List<Endereco>) repository.findAll();
    }

    public Endereco findById(Long id){
        return repository.findById(id).get();
    }

    public Endereco insertEndereco(Endereco endereco){
        return repository.save(endereco);
    }

    public Endereco updateEndereco(Endereco novo, Long id){
        Endereco atual = repository.findById(id).get();
        updateData(novo, atual);
        return repository.save(atual);
    }

    public void deleteEndereco(Long id){
        repository.deleteById(id);
    }

    public Page<Endereco> encontrarPorMonitoradorPageable(Long id, Pageable pageable){
        return repository.findByMonitoradorPageable(id, pageable);
    }

    public List<Endereco> encontrarPorMonitorador(Long id){
        return repository.findByMonitorador(id);
    }

    public void deletarPorMonitorador(Long id){
        List<Endereco> ends = repository.findByMonitorador(id);
        for(Endereco e : ends) deleteEndereco(e.getId());
    }

    public Endereco ultimoAdicionado(){
        return repository.findTopByOrderByIdDesc();
    }

    private void updateData(Endereco novo, Endereco atual){
        atual.setEndereco(novo.getEndereco());
        atual.setNumero(novo.getNumero());
        atual.setBairro(novo.getBairro());
        atual.setTelefone(novo.getTelefone());
        atual.setCep(novo.getCep());
        atual.setCidade(novo.getCidade());
        atual.setEstado(novo.getEstado());
        atual.setPrincipal(novo.isPrincipal());
    }
}
