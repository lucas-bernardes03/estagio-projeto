package com.unikasistemas.deangular.controller;

import com.unikasistemas.deangular.entities.Endereco;
import com.unikasistemas.deangular.entities.Monitorador;
import com.unikasistemas.deangular.service.EnderecoService;
import com.unikasistemas.deangular.service.MonitoradorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping(value = "/api/monitoradores")
public class MonitoradorController {
    @Autowired
    private MonitoradorService service;

    @Autowired
    private EnderecoService enderecoService;

    //listar monitoradores com paginação e filtro
    @GetMapping
    public Page<Monitorador> listarPaginas(@RequestParam(required = true) String tipo, @RequestParam(required = false) String search , @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size){
        Pageable pageable = PageRequest.of(page, size);
        if(search == null) return service.findAllPaginated(tipo, pageable);
        else return service.findSearch(tipo, pageable, search);
    }

    //listar monitorador por id
    @GetMapping(value = "/{id}")
    public ResponseEntity<Monitorador> buscarPorId(@PathVariable Long id){
        Monitorador m = service.findById(id);
        return ResponseEntity.ok(m);
    }

    //listar todos endereços de um monitorador, paginado
    @GetMapping(value = "/{id}/enderecos")
    public Page<Endereco> enderecos(@PathVariable Long id, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size){
        Pageable pageable = PageRequest.of(page, size);
        return enderecoService.encontrarPorMonitoradorPageable(id, pageable);
    }

    //listar todos os endereços de um monitorador
    @GetMapping(value = "/{id}/enderecos/m")
    public ResponseEntity<List<Endereco>> enderecosTodos(@PathVariable Long id){
        List<Endereco> e = enderecoService.encontrarPorMonitorador(id);
        return ResponseEntity.ok(e);
    }

    //adicionar um endereço a um monitorador
    @PostMapping(value = "/{id}/enderecos")
    public ResponseEntity<Endereco> adicionarEndereco(@PathVariable Long id, @RequestBody Endereco endereco){
        Monitorador m = service.findById(id);
        endereco.setMonitorador(m);
        Endereco e = enderecoService.insertEndereco(endereco);
        return ResponseEntity.ok(e);
    }

    //adicionar um novo monitorador
    @PostMapping
    public ResponseEntity<Monitorador> inserir(@RequestBody Monitorador monitorador){
        if(!service.checkIguais(monitorador)){
            Monitorador m = service.insertMonitorador(monitorador);
            monitorador.getEnderecos().forEach(endereco -> {
                endereco.setMonitorador(m);
                enderecoService.insertEndereco(endereco);
            });
            return ResponseEntity.ok(m);
        }
        else return ResponseEntity.ok(monitorador);

    }

    //listar todos os monitoradores
    @GetMapping(value = "/m")
    public ResponseEntity<List<Monitorador>> listarTodos(){
        List<Monitorador> m = (List<Monitorador>) service.findAll();
        return ResponseEntity.ok(m);
    }

    //adicionar uma lista de novos monitoradores
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

    //atualizar dos dados de um monitorador
    @PutMapping(value = "/{id}")
    public ResponseEntity<Monitorador> atualizar(@RequestBody Monitorador novo,@PathVariable Long id){
        if(!service.checkUpdate(id, novo)){
            Monitorador atual = service.updateMonitorador(novo, id);
            return ResponseEntity.ok(atual);
        }
        else{
            novo.setId(null);
            return ResponseEntity.ok(novo);
        }
    }

    //deletar um monitorador
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id){
        service.deleteMonitorador(id);
        return ResponseEntity.noContent().build();
    }

    //gerar um pdf dos monitoradores
    @GetMapping(value = "/pdf")
    public ResponseEntity<Resource> pdf(){
        Resource file = service.exportReport();
        return ResponseEntity.ok(file);
    }

    // modelo excel
    @GetMapping(value = "/modelo")
    public ResponseEntity<Resource> excelModelo() {
        Resource file = service.exportModelo();
        return ResponseEntity.ok(file);
    }

    @GetMapping(value = "/excel")
    public ResponseEntity<Resource> excel(){
        Resource file = service.excelDados();
        return ResponseEntity.ok(file);
    }

    @GetMapping(value = "/{id}/excel")
    public ResponseEntity<Resource> excelId(@PathVariable Long id){
        Resource file = service.excelDadosId(id);
        return ResponseEntity.ok(file);
    }

}
