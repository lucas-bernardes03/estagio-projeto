package com.unikasistemas.model.servicos;

import java.sql.Connection;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import com.unikasistemas.model.entities.Endereco;
import com.unikasistemas.model.entities.Monitorador;

public class EnderecoServico extends WebPage{

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String baseUrl = "http://localhost:8080/api/enderecos/";

    public Endereco Editar(Endereco endereco){
        CloseableHttpClient httpClient = HttpClients.createDefault();
        try{
            HttpPut put = new HttpPut(baseUrl+endereco.getId());
            String json = objectMapper.writeValueAsString(endereco);
            StringEntity entity = new StringEntity(json, ContentType.APPLICATION_JSON);

            put.setEntity(entity);
            put.setHeader("Content-type", "application/json");

            CloseableHttpResponse response = httpClient.execute(put);
            HttpEntity responseEntity = response.getEntity();
            String obj = EntityUtils.toString(responseEntity);

            httpClient.close();
            response.close();

            return objectMapper.readValue(obj, new TypeReference<Endereco>() {});
        }
        catch(Exception e){
            e.printStackTrace();
        }

        return null;
    }

    public List<Endereco> listarTodos(Monitorador monitorador){
        //todo
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

    public List<Endereco> buscarRua(Monitorador monitorador, String rua){
        //todo
        return null;
    }
}
