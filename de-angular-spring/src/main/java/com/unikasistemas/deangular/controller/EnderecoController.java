package com.unikasistemas.deangular.controller;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.unikasistemas.deangular.entities.Endereco;
import com.unikasistemas.deangular.entities.Monitorador;
import com.unikasistemas.deangular.service.EnderecoService;
import com.unikasistemas.deangular.service.MonitoradorService;

@RestController
@RequestMapping(value = "/api/enderecos")
public class EnderecoController {
    
    @Autowired
    private EnderecoService service;

    @Autowired
    private MonitoradorService monitoradorService;

    @GetMapping
    public ResponseEntity<List<Endereco>> listarTodos(){
        List<Endereco> list = service.findAll();
        return ResponseEntity.ok(list);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Endereco> buscarPorId(@PathVariable Long id){
        Endereco e = service.findById(id);
        return ResponseEntity.ok(e);
    }

    @PostMapping
    public ResponseEntity<Endereco> inserir(@RequestBody Endereco endereco){
        Monitorador m = monitoradorService.ultimoAdicionado();
        endereco.setMonitorador(m);
        Endereco e = service.insertEndereco(endereco);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(e.getId()).toUri();
        return ResponseEntity.created(uri).body(e);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Endereco> atualizar(@PathVariable Long id, @RequestBody Endereco endereco){
        Endereco atual = service.updateEndereco(endereco, id);
        return ResponseEntity.ok(atual);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id){
        service.deleteEndereco(id);
        return ResponseEntity.noContent().build();
    }
}
