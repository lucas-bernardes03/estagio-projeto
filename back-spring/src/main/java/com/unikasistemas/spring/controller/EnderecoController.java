package com.unikasistemas.spring.controller;

import com.unikasistemas.spring.entities.Endereco;
import com.unikasistemas.spring.entities.Monitorador;
import com.unikasistemas.spring.service.EnderecoService;
import com.unikasistemas.spring.service.MonitoradorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        return ResponseEntity.ok(e);
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
