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
@RequestMapping(value = "/api/monitoradores")
public class MonitoradorController {
    @Autowired
    private MonitoradorService service;

    @Autowired
    private EnderecoService enderecoService;

    @GetMapping
    public ResponseEntity<List<Monitorador>> listarTodos(){
        List<Monitorador> list = service.findAll();
        return ResponseEntity.ok(list);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Monitorador> buscarPorId(@PathVariable Long id){
        Monitorador m = service.findById(id);
        return ResponseEntity.ok(m);
    }

    @GetMapping(value = "/{id}/enderecos")
    public ResponseEntity<List<Endereco>> enderecos(@PathVariable Long id){
        List<Endereco> enderecos = enderecoService.encontrarPorMonitorador(id);
        return ResponseEntity.ok(enderecos);
    }

    @PostMapping(value = "/{id}/enderecos")
    public ResponseEntity<Endereco> adicionarEndereco(@PathVariable Long id, @RequestBody Endereco endereco){
        Monitorador m = service.findById(id);
        endereco.setMonitorador(m);
        Endereco e = enderecoService.insertEndereco(endereco);
        return ResponseEntity.ok(e);
    }

    @PostMapping
    public ResponseEntity<Monitorador> inserir(@RequestBody Monitorador monitorador){
        Monitorador m = service.insertMonitorador(monitorador);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(m.getId()).toUri();
        return ResponseEntity.created(uri).body(m);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Monitorador> atualizar(@RequestBody Monitorador novo,@PathVariable Long id){
        Monitorador atual = service.updateMonitorador(novo, id);
        return ResponseEntity.ok(atual);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id){
        enderecoService.deletarPorMonitorador(id);
        service.deleteMonitorador(id);
        return ResponseEntity.noContent().build();
    }
}
