package com.unikasistemas.spring.service;

import com.unikasistemas.spring.entities.Endereco;
import com.unikasistemas.spring.entities.Monitorador;
import com.unikasistemas.spring.repository.MonitoradorRepository;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import javax.transaction.Transactional;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
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
            String filePath = "src/main/resources";

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
            String filepath = "src/main/resources";
            Path file = Paths.get(filepath).resolve("PlanilhaCadastro.xls");
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) return resource;
            else throw new RuntimeException("Arquivo base corrompido.");
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public Resource excelDados(){
        List<Monitorador> monitoradores = (List<Monitorador>) findAll();

        String[] columnsFisica = {"Nome", "CPF", "RG", "Data de Nascimento","E-mail","Ativo"};
        String[] columnsJuridica = {"Razão Social", "CNPJ", "Inscrição Estadual","E-mail","Ativo"};

        try{
            Workbook workbook = new HSSFWorkbook();

            CreationHelper creationHelper = workbook.getCreationHelper();

            Sheet sheetF = workbook.createSheet("Pessoa Física");
            Sheet sheetJ = workbook.createSheet("Pessoa Jurídica");

            Font headerFont = workbook.createFont();
            CellStyle headerCellStyle = workbook.createCellStyle();
            styleHeaderExcel(headerFont, headerCellStyle);

            //linha header
            Row headerRowF = sheetF.createRow(0);
            Row headerRowJ = sheetJ.createRow(0);

            //valor da celula header
            for(int i=0;i<columnsFisica.length;i++){
                Cell cell = headerRowF.createCell(i);
                cell.setCellValue(columnsFisica[i]);
                cell.setCellStyle(headerCellStyle);
            }

            for(int i=0;i<columnsJuridica.length;i++){
                Cell cell = headerRowJ.createCell(i);
                cell.setCellValue(columnsJuridica[i]);
                cell.setCellStyle(headerCellStyle);
            }


            //formato da data
            CellStyle dataCellStyle = workbook.createCellStyle();
            dataCellStyle.setDataFormat(creationHelper.createDataFormat().getFormat("dd/MM/yyyy"));

            //contadores de linhas
            int iF = 1;
            int iJ = 1;

            //criando as linhas monitorador
            for(Monitorador mon : monitoradores){
                if(mon.getTipo().equals("Física")){
                    Row row = sheetF.createRow(iF++);
                    row.createCell(0).setCellValue(mon.getIdentificacao());
                    row.createCell(1).setCellValue(mon.getCadastro());
                    row.createCell(2).setCellValue(mon.getRegistro());

                    Cell dataNascimento = row.createCell(3);
                    Date data = Date.from(mon.getDataNascimento());
                    dataNascimento.setCellValue(data);
                    dataNascimento.setCellStyle(dataCellStyle);


                    row.createCell(4).setCellValue(mon.getEmail());
                    row.createCell(5).setCellValue(mon.isAtivo() ? "Sim":"Não");

                }
                else{
                    Row row = sheetJ.createRow(iJ++);
                    row.createCell(0).setCellValue(mon.getIdentificacao());
                    row.createCell(1).setCellValue(mon.getCadastro());
                    row.createCell(2).setCellValue(mon.getRegistro());
                    row.createCell(3).setCellValue(mon.getEmail());
                    row.createCell(4).setCellValue(mon.isAtivo() ? "Sim":"Não");
                }
            }

            //setando o tamanho das colunas
            for(int i=0;i<columnsFisica.length;i++) sheetF.autoSizeColumn(i);
            for(int i=0;i<columnsJuridica.length;i++) sheetJ.autoSizeColumn(i);

            //imprimindo o arquivo
            FileOutputStream output = new FileOutputStream("src/main/resources/Monitoradores.xls");
            workbook.write(output);
            output.close();
            workbook.close();

            String filepath = "src/main/resources";
            Path file = Paths.get(filepath).resolve("Monitoradores.xls");
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) return resource;
            else throw new RuntimeException("Arquivo corrompido.");
        }
        catch(Exception e){
            e.printStackTrace();
        }

        return null;
    }

    public Resource excelDadosId(Long id){
        Monitorador monitorador = findById(id);

        String[] columnsFisica = {"Nome", "CPF", "RG", "Data de Nascimento","E-mail","Ativo"};
        String[] columnsJuridica = {"Razão Social", "CNPJ", "Inscrição Estadual","E-mail","Ativo"};
        String[] columnEndereco = {"Rua","Numero","CEP","Bairro","Telefone","Cidade","Estado","Endereco Principal"};

        try{
            Workbook workbook = new HSSFWorkbook();

            CreationHelper creationHelper = workbook.getCreationHelper();

            Sheet sheetM = workbook.createSheet("Monitorador");
            Sheet sheetE = workbook.createSheet("Enderecos");


            Font headerFont = workbook.createFont();
            CellStyle headerCellStyle = workbook.createCellStyle();
            styleHeaderExcel(headerFont, headerCellStyle);

            //linha header
            Row headerRow = sheetM.createRow(0);
            Row headerRowE = sheetE.createRow(0);

            //gera as colunas baseado no tipo da pessoa e do endereco
            if(monitorador.getTipo().equals("Física")){
                for(int i=0;i<columnsFisica.length;i++){
                    Cell cell = headerRow.createCell(i);
                    cell.setCellValue(columnsFisica[i]);
                    cell.setCellStyle(headerCellStyle);
                }
            }
            else{
                for(int i=0;i<columnsJuridica.length;i++){
                    Cell cell = headerRow.createCell(i);
                    cell.setCellValue(columnsJuridica[i]);
                    cell.setCellStyle(headerCellStyle);
                }
            }

            //gera as colunas dos enderecos
            for(int i=0;i<columnEndereco.length;i++){
                Cell cellE = headerRowE.createCell(i);
                cellE.setCellValue(columnEndereco[i]);
                cellE.setCellStyle(headerCellStyle);
            }

            //formato da data
            CellStyle dataCellStyle = workbook.createCellStyle();
            dataCellStyle.setDataFormat(creationHelper.createDataFormat().getFormat("dd/MM/yyyy"));

            //criando as linhas monitorador
            Row row = sheetM.createRow(1);

            row.createCell(0).setCellValue(monitorador.getIdentificacao());
            row.createCell(1).setCellValue(monitorador.getCadastro());
            row.createCell(2).setCellValue(monitorador.getRegistro());

            if(monitorador.getTipo().equals("Física")){
                Cell dataNascimentoCell = row.createCell(3);
                Date data = Date.from(monitorador.getDataNascimento());
                dataNascimentoCell.setCellValue(data);
                dataNascimentoCell.setCellStyle(dataCellStyle);

                row.createCell(4).setCellValue(monitorador.getEmail());
                row.createCell(5).setCellValue(monitorador.isAtivo() ? "Sim":"Não");
            }
            else{
                row.createCell(3).setCellValue(monitorador.getEmail());
                row.createCell(4).setCellValue(monitorador.isAtivo() ? "Sim":"Não");
            }


            //criando as lihas endereco
            int rowNum = 1;
            for(Endereco e : monitorador.getEnderecos()){
                Row rowE = sheetE.createRow(rowNum++);
                rowE.createCell(0).setCellValue(e.getEndereco());
                rowE.createCell(1).setCellValue(e.getNumero());
                rowE.createCell(2).setCellValue(e.getCep());
                rowE.createCell(3).setCellValue(e.getBairro());
                rowE.createCell(4).setCellValue(e.getTelefone());
                rowE.createCell(5).setCellValue(e.getCidade());
                rowE.createCell(6).setCellValue(e.getEstado());
                rowE.createCell(7).setCellValue(e.isPrincipal() ? "Sim":"Não");
            }

            //setando o tamanho das colunas
            if(monitorador.getTipo().equals("Física")) for(int i=0;i<columnsFisica.length;i++) sheetM.autoSizeColumn(i);
            else for(int i=0;i<columnsJuridica.length;i++) sheetM.autoSizeColumn(i);
            for(int i=0;i<columnEndereco.length;i++) sheetE.autoSizeColumn(i);

            //imprimindo o arquivo
            FileOutputStream output = new FileOutputStream("src/main/resources/Monitorador"+monitorador.getId()+".xls");
            workbook.write(output);
            output.close();
            workbook.close();

            String filepath = "src/main/resources";
            Path path = Paths.get(filepath).resolve("Monitorador"+monitorador.getId()+".xls");
            Resource resource = new UrlResource(path.toUri());

            if (resource.exists() || resource.isReadable()) return resource;
            else throw new RuntimeException("Arquivo corrompido.");
        }
        catch(IOException e){
            e.printStackTrace();
        }

        return null;
    }

    private void styleHeaderExcel(Font headerFont, CellStyle headerCellStyle){
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 14);
        headerFont.setColor(IndexedColors.BLACK.getIndex());

        headerCellStyle.setFont(headerFont);
        headerCellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    }

}
