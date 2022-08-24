package com.unikasistemas.deangular.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.unikasistemas.deangular.entities.Endereco;
import com.unikasistemas.deangular.repository.EnderecoRepository;

@Service
public class EnderecoService {
    @Autowired
    private EnderecoRepository repository;

    public List<Endereco> findAll(){
        return repository.findAll();
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
