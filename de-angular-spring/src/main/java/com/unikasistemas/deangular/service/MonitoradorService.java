package com.unikasistemas.deangular.service;

import com.unikasistemas.deangular.entities.Monitorador;
import com.unikasistemas.deangular.repository.MonitoradorRepository;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import javax.transaction.Transactional;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.Locale;


@Service
public class MonitoradorService {
    @Autowired
    private MonitoradorRepository repository;

    @Autowired
    private EnderecoService enderecoService;

    @Autowired
    private DataSource dataSource;

    public Page<Monitorador> findAllPaginated(String tipo, Pageable pageable){
        return repository.findByTipo(tipo, pageable);
    }

    public Page<Monitorador> findSearch(String tipo, Pageable pageable, String search){
        search = "%" + search + "%";
        search = search.toUpperCase(Locale.ROOT);
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

    @Transactional
    public Monitorador updateMonitorador(Monitorador novo, Long idAtual){
        Monitorador atual = repository.findById(idAtual).get();
        updateData(novo, atual);
        return repository.save(atual);
    }

    @Transactional
    public void deleteMonitorador(Long id){
        enderecoService.deletarPorMonitorador(id);
        repository.deleteById(id);
    }

    public boolean checkUpdate(Long id, Monitorador novo){
        boolean check = false;
        if(novo.getTipo().equals("Física")){
            Long cpfCount = repository.updateCountCpf(id , novo.getCpf());
            Long rgCount = repository.updateCountRg(id, novo.getRg());

            if(cpfCount > 0){
                novo.setCadastro(null);
                check = true;
            }
            if(rgCount > 0){
                novo.setRegistro(null);
                check = true;
            }
        }
        else{
            Long cnpjCount = repository.updateCountCnpj(id, novo.getCnpj());
            Long insCount = repository.updateCountIns(id, novo.getInscricaoEstadual());

            if(cnpjCount > 0){
                novo.setCadastro(null);
                check = true;
            }
            if(insCount > 0){
                novo.setRegistro(null);
                check = true;
            }
        }

        return check;
    }

    public boolean checkIguais(Monitorador m){
        boolean check = false;

        if(m.getTipo().equals("Física")){
            Long cpfCount = repository.countByCpf(m.getCpf());
            Long rgCount = repository.countByRg(m.getRg());

            if(cpfCount > 0){
                m.setCadastro(null);
                check = true;
            }
            if(rgCount > 0){
                m.setRegistro(null);
                check = true;
            }
        }
        else{
            Long cnpjCount = repository.countByCnpj(m.getCnpj());
            Long insCount = repository.countByInscricaoEstadual(m.getInscricaoEstadual());

            if(cnpjCount > 0){
                m.setCadastro(null);
                check = true;
            }
            if(insCount > 0){
                m.setRegistro(null);
                check = true;
            }
        }

        return check;
    }

    public Monitorador ultimoAdicionado(){
        return repository.findTopByOrderByIdDesc();
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

    public Resource exportModelo() {
        try {
            String filepath = "C:\\Users\\dev\\Documents\\GitHub\\desafioestagio-angular-spring\\de-angular-spring\\src\\main\\resources";
            Path file = Paths.get(filepath).resolve("PlanilhaCadastro.xlsx");
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) return resource;
            else throw new RuntimeException("Arquivo base corrompido.");
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

}
