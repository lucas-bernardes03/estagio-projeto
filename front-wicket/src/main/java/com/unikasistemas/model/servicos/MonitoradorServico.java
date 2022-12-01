package com.unikasistemas.model.servicos;


import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.unikasistemas.model.entities.Endereco;
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
}
