package com.unikasistemas.deangular.controller;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    public Page<Monitorador> listarPaginas(@RequestParam(required = true) String tipo, @RequestParam(required = false) String search , @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size){
        Pageable pageable = PageRequest.of(page, size);
        if(search == null) return service.findAllPaginated(tipo, pageable);
        else return service.findSearch(tipo, pageable, search);

    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Monitorador> buscarPorId(@PathVariable Long id){
        Monitorador m = service.findById(id);
        return ResponseEntity.ok(m);
    }

    @GetMapping(value = "/{id}/enderecos")
    public Page<Endereco> enderecos(@PathVariable Long id, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size){
        Pageable pageable = PageRequest.of(page, size);
        return enderecoService.encontrarPorMonitoradorPageable(id, pageable);
    }

    @GetMapping(value = "/{id}/enderecos/m")
    public ResponseEntity<List<Endereco>> enderecosTodos(@PathVariable Long id){
        List<Endereco> e = enderecoService.encontrarPorMonitorador(id);
        return ResponseEntity.ok(e);
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
        monitorador.getEnderecos().forEach(endereco -> endereco.setMonitorador(m));
        monitorador.getEnderecos().forEach(endereco -> enderecoService.insertEndereco(endereco));
        return ResponseEntity.ok(m);
    }

    @GetMapping(value = "/m")
    public ResponseEntity<List<Monitorador>> listarTodos(){
        List<Monitorador> m = (List<Monitorador>) service.findAll();
        return ResponseEntity.ok(m);
    }

    @PostMapping(value = "/m")
    public ResponseEntity<List<Monitorador>> inserirMultiplos(@RequestBody List<Monitorador> monitoradores){
        List<Monitorador> mons = new ArrayList<>();
        
        for(Monitorador m : monitoradores){
            Monitorador mSalvo = service.insertMonitorador(m);
            m.getEnderecos().forEach(endereco -> endereco.setMonitorador(mSalvo));
            m.getEnderecos().forEach(endereco -> enderecoService.insertEndereco(endereco));
            mons.add(mSalvo);
        }
        
        return ResponseEntity.ok(mons);
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

    @GetMapping(value = "/pdf")
    public ResponseEntity<Resource> pdf() throws IOException{
        Resource file = service.exportReport();
        return ResponseEntity.ok(file);
    }

}
