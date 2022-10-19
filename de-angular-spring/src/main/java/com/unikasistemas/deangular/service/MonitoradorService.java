package com.unikasistemas.deangular.service;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.unikasistemas.deangular.entities.Monitorador;
import com.unikasistemas.deangular.repository.MonitoradorRepository;

import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;


@Service
public class MonitoradorService {
    @Autowired
    private MonitoradorRepository repository;

    @Autowired
    private DataSource dataSource;

    public Page<Monitorador> findAllPaginated(String tipo, Pageable pageable){
        return repository.findByTipo(tipo, pageable);
    }

    public Page<Monitorador> findSearch(String tipo, Pageable pageable, String search){
        search = "%" + search + "%";
        if(tipo.equals("Física")) return repository.findSearchFisica(search, pageable);
        else return repository.findSearchJuridica(search, pageable);
    }

    public Iterable<Monitorador> findAll(){
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

    public Monitorador ultimoAdicionado(){
        return repository.findTopByOrderByIdDesc();
    }

    public List<Monitorador> inserirVarios(List<Monitorador> monitoradores){
        return (List<Monitorador>) repository.saveAll(monitoradores);
    }

    private void updateData(Monitorador novo, Monitorador atual){
        atual.setTipo(novo.getTipo());
        atual.setIdentificacao(novo.getIdentificacao());
        atual.setCadastro(novo.getCadastro());
        atual.setRegistro(novo.getRegistro());
        atual.setDataNascimento(novo.getDataNascimento());
        atual.setEmail(novo.getEmail());
        atual.setAtivo(novo.isAtivo());
    }

    public Resource exportReport() {  
        try{
            JasperPrint jasperPrint = JasperFillManager.fillReport("src/main/resources/Leaf_Green.jasper", null, dataSource.getConnection());

            JRPdfExporter exporter = new JRPdfExporter();
            exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput("MONITORADORES.pdf"));

            exporter.exportReport();

            //returning the file
            String filePath = "C:\\Users\\dev\\Documents\\GitHub\\desafioestagio-angular-spring\\de-angular-spring";

            Path file = Paths.get(filePath).resolve("MONITORADORES.pdf");
            Resource resource = new UrlResource(file.toUri());

            if(resource.exists() || resource.isReadable()){
                return resource;
            }
            else{
                throw new RuntimeException();
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        
        return null;

    }

    

}
