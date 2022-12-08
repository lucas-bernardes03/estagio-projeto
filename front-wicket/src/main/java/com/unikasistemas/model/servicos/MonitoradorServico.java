package com.unikasistemas.model.servicos;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.unikasistemas.model.entities.Endereco;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.classic.methods.HttpDelete;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.classic.methods.HttpPut;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.wicket.markup.html.WebPage;

import com.unikasistemas.model.entities.Monitorador;

public class MonitoradorServico extends WebPage {
    private final String baseUrl = "http://localhost:8080/api/monitoradores/";
    private final ObjectMapper objectMapper = new ObjectMapper();


    public List<Monitorador> listarTodos(){
        CloseableHttpClient httpClient = HttpClients.createDefault();
        try{
            HttpGet request = new HttpGet(baseUrl+"m");
            CloseableHttpResponse response = httpClient.execute(request);
            HttpEntity entity = response.getEntity();
            String responseString = EntityUtils.toString(entity);

            httpClient.close();
            response.close();

            return objectMapper.readValue(responseString, new TypeReference<List<Monitorador>>(){});
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


    public Monitorador adicionarOuEditar(Monitorador m){
        if(m.getTipo().equals("Física")) parseDate(m);
        CloseableHttpClient httpClient = HttpClients.createDefault();
        try {
            if(m.getId()==null){
                HttpPost post = new HttpPost(baseUrl);

                String json = objectMapper.writeValueAsString(m);
                StringEntity entity = new StringEntity(json, ContentType.APPLICATION_JSON);

                post.setEntity(entity);
                post.setHeader("Content-type", "application/json");

                CloseableHttpResponse response = httpClient.execute(post);
                HttpEntity responseEntity = response.getEntity();
                String obj = EntityUtils.toString(responseEntity);

                httpClient.close();
                response.close();

                return objectMapper.readValue(obj, new TypeReference<Monitorador>() {});
            }
            else{
                HttpPut put = new HttpPut(baseUrl+m.getId());
                String json = objectMapper.writeValueAsString(m);
                StringEntity entity = new StringEntity(json, ContentType.APPLICATION_JSON);

                put.setEntity(entity);
                put.setHeader("Content-type", "application/json");

                CloseableHttpResponse response = httpClient.execute(put);
                HttpEntity responseEntity = response.getEntity();
                String obj = EntityUtils.toString(responseEntity);

                httpClient.close();
                response.close();

                return objectMapper.readValue(obj, new TypeReference<Monitorador>() {});
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public Monitorador addExcel(Monitorador m){
        if(m.getTipo().equals("Física")){
            m.setDataNascimento(m.getDataNascimento().replace("Z", ".000Z"));
        }
        CloseableHttpClient httpClient = HttpClients.createDefault();
        try {

            HttpPost post = new HttpPost(baseUrl);

            String json = objectMapper.writeValueAsString(m);
            StringEntity entity = new StringEntity(json, ContentType.APPLICATION_JSON);

            post.setEntity(entity);
            post.setHeader("Content-type", "application/json");

            CloseableHttpResponse response = httpClient.execute(post);
            HttpEntity responseEntity = response.getEntity();
            String obj = EntityUtils.toString(responseEntity);

            httpClient.close();
            response.close();

            return objectMapper.readValue(obj, new TypeReference<Monitorador>() {
            });
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    public int deletar(Long id){
        CloseableHttpClient httpClient = HttpClients.createDefault();
        try{
            HttpDelete delete = new HttpDelete(baseUrl+id);
            CloseableHttpResponse response = httpClient.execute(delete);

            httpClient.close();

            return response.getCode();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return -1;
    }

    public List<Endereco> listarEnderecos(Long id){
        CloseableHttpClient httpClient = HttpClients.createDefault();
        try{
            HttpGet get = new HttpGet(baseUrl+id+"/enderecos/m");
            CloseableHttpResponse response = httpClient.execute(get);
            HttpEntity entity = response.getEntity();
            String responseString = EntityUtils.toString(entity);

            httpClient.close();
            response.close();
            return objectMapper.readValue(responseString, new TypeReference<List<Endereco>>(){});
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }


    public Endereco adicionarEndereco(Endereco endereco){
        CloseableHttpClient httpClient = HttpClients.createDefault();
        try{
            HttpPost post = new HttpPost(baseUrl+endereco.getMonitorador().getId()+"/enderecos");
            String json = objectMapper.writeValueAsString(endereco);
            StringEntity entity = new StringEntity(json, ContentType.APPLICATION_JSON);

            post.setEntity(entity);
            post.setHeader("Content-type", "application/json");

            CloseableHttpResponse response = httpClient.execute(post);
            HttpEntity responseEntity = response.getEntity();
            String obj = EntityUtils.toString(responseEntity);

            httpClient.close();
            response.close();

            return objectMapper.readValue(obj, new TypeReference<Endereco>() {});
        }
        catch( Exception e){
            e.printStackTrace();
        }

        return null;
    }


    public File excelId(Long id){
        CloseableHttpClient httpClient = HttpClients.createDefault();
        try{
            HttpGet get = new HttpGet(baseUrl+id+"/excel");
            CloseableHttpResponse response = httpClient.execute(get);
            HttpEntity entity = response.getEntity();

            OutputStream out = Files.newOutputStream(Paths.get("src/main/resources/monitorador.xls"));
            entity.writeTo(out);

            httpClient.close();
            response.close();

            return new File("src/main/resources/monitorador.xls");
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public File excelAll(){
        CloseableHttpClient httpClient = HttpClients.createDefault();
        try{
            HttpGet get = new HttpGet(baseUrl+"excel");
            CloseableHttpResponse response = httpClient.execute(get);
            HttpEntity entity = response.getEntity();

            OutputStream out = Files.newOutputStream(Paths.get("src/main/resources/Monitoradores.xls"));
            entity.writeTo(out);

            httpClient.close();
            response.close();

            return new File("src/main/resources/Monitoradores.xls");
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    public File pdf(){
        CloseableHttpClient httpClient = HttpClients.createDefault();
        try{
            HttpGet get = new HttpGet(baseUrl+"pdf");
            CloseableHttpResponse response = httpClient.execute(get);
            HttpEntity entity = response.getEntity();

            OutputStream out = Files.newOutputStream(Paths.get("src/main/resources/Monitoradores.pdf"));
            entity.writeTo(out);

            httpClient.close();
            response.close();

            return new File("src/main/resources/Monitoradores.pdf");
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    public File excelModel(){
        CloseableHttpClient httpClient = HttpClients.createDefault();
        try{
            HttpGet get = new HttpGet(baseUrl+"modelo");
            CloseableHttpResponse response = httpClient.execute(get);
            HttpEntity entity = response.getEntity();

            OutputStream out = Files.newOutputStream(Paths.get("src/main/resources/Modelo.xls"));
            entity.writeTo(out);

            httpClient.close();
            response.close();

            return new File("src/main/resources/Modelo.xls");
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    private void parseDate(Monitorador m){
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        try{
            Date date = formatter.parse(m.getDataNascimento());
            Instant i = date.toInstant();
            String dataP = i.toString().replace("Z", ".000Z");
            m.setDataNascimento(dataP);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public List<Monitorador> parseExcel(File excel){
        List<Monitorador> monitoradores = new ArrayList<>();

        try{
            FileInputStream arquivo = new FileInputStream(excel);

            HSSFWorkbook workbook = new HSSFWorkbook(arquivo);

            HSSFSheet sheet = workbook.getSheetAt(0);

            Iterator<Row> rowIterator = sheet.rowIterator();

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();

                if(row.getRowNum()==0) continue;
                if(emptyRow(row)) continue;

                Iterator<Cell> cellIterator = row.cellIterator();

                Monitorador mon = new Monitorador();
                Endereco end = new Endereco();

                mon.setAtivo(true);
                mon.addEndereco(end);

                while(cellIterator.hasNext()){
                    Cell cell = cellIterator.next();
                    switch(cell.getColumnIndex()){
                        case 0:
                            mon.setTipo(cell.getStringCellValue());
                            break;
                        case 1:
                            mon.setIdentificacao(cell.getStringCellValue());
                            break;
                        case 2:
                            cell.setCellType(CellType.STRING);
                            mon.setCadastro(cell.getStringCellValue());
                            break;
                        case 3:
                            cell.setCellType(CellType.STRING);
                            mon.setRegistro(cell.getStringCellValue());
                            break;
                        case 4:
                            if (mon.getTipo().equals("Física")){
                                String date = cell.getDateCellValue().toInstant().toString();
                                mon.setDataNascimento(date);
                            }
                            else mon.setDataNascimento(null);
                            break;
                        case 5:
                            mon.setEmail(cell.getStringCellValue());
                            break;
                        case 6:
                            end.setEndereco(cell.getStringCellValue());
                            break;
                        case 7:
                            cell.setCellType(CellType.STRING);
                            end.setNumero(cell.getStringCellValue());
                            break;
                        case 8:
                            cell.setCellType(CellType.STRING);
                            end.setCep(cell.getStringCellValue());
                            break;
                        case 9:
                            end.setBairro(cell.getStringCellValue());
                            break;
                        case 10:
                            end.setCidade(cell.getStringCellValue());
                            break;
                        case 11:
                            end.setEstado(cell.getStringCellValue());
                            break;
                        case 12:
                            cell.setCellType(CellType.STRING);
                            end.setTelefone(parsePhone(cell.getStringCellValue()));
                            break;
                        default:
                            System.out.println("something happened");
                            break;
                    }
                }
                monitoradores.add(mon);
            }

            arquivo.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return monitoradores;
    }

    private String parsePhone(String phone){
        return phone.replaceAll("^(\\d{0,2})(\\d{0,9})$", "($1) $2");
    }

    private boolean emptyRow(Row row){
        if(row == null) return true;
        if(row.getCell(0) == null) return true;

        for(int i = row.getFirstCellNum(); i<row.getLastCellNum();i++){
            Cell cell = row.getCell(i);

            if(i == 4 && row.getCell(0).getStringCellValue().equals("Jurídica")) continue;
            if(cell == null  || cell.getCellTypeEnum() == CellType.BLANK) return true;
        }

        return false;
    }

    public String validateFields(Monitorador m){
        StringBuilder sb = new StringBuilder();

        if(m.getTipo().equals("Física")){
            if(!m.getNome().matches("^[A-Za-zÀ-ÖØ-öø-ÿ ]{1,50}$")){
                sb.append("Nome");
                if(!m.getCpf().matches("^\\d{11}$") || !m.getRg().matches("^\\d{7}$")) sb.append(", ");
            }
            if(!m.getCpf().matches("^\\d{11}$")){
                sb.append("CPF");
                if(!m.getRg().matches("^\\d{7}$")) sb.append(", ");
            }
            if(!m.getRg().matches("^\\d{7}$")) sb.append("RG");
        }
        else{
            if(!m.getRazaoSocial().matches("^[A-Za-zÀ-ÖØ-öø-ÿ \\d]{1,50}$")){
                sb.append("Razão Social");
                if(!m.getCnpj().matches("^\\d{14}$") || !m.getInscricaoEstadual().matches("^\\d{12}$")) sb.append(", ");
            }
            if(!m.getCnpj().matches("^\\d{14}$")){
                sb.append("CNPJ");
                if(!m.getInscricaoEstadual().matches("^\\d{12}$")) sb.append(", ");
            }
            if(!m.getInscricaoEstadual().matches("^\\d{12}$")) sb.append("Inscrição Estadual");
        }

        return sb.toString();
    }
}
