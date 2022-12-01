package com.unikasistemas.model.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;



public class Monitorador implements Serializable {
    private Long id;
    private String tipo;
    private String nome;
    private String cpf;
    private String rg;
    private String dataNascimento;
    private String razaoSocial;
    private String cnpj;
    private String inscricaoEstadual;
    private String email;
    private Boolean ativo;
    private final List<Endereco> enderecos = new ArrayList<>();

    public Monitorador(){}

    public Monitorador(Long id,String tipoP, String identificacao, String cadastro, String email, String registro, String dataNascimento, Boolean ativo){
        this.id = id;
        this.tipo = tipoP;

        if(tipoP.equals("Física")){
            this.nome = identificacao;
            this.cpf = cadastro;
            this.rg = registro;
            this.dataNascimento = dataNascimento;
        }
        else {
            this.razaoSocial = identificacao;
            this.cnpj = cadastro;
            this.inscricaoEstadual = registro;
        }

        this.email = email;
        this.ativo = ativo;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRg() {
        return rg;
    }

    public String getInscricaoEstadual() {
        return inscricaoEstadual;
    }

    public String getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(String dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public Boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    @JsonIgnore
    public String getIdentificacao(){
        if(tipo.equals("Física")) return nome;
        else return razaoSocial;
    }

    public void setIdentificacao(String novo){
        if(tipo.equals("Física")) this.nome = novo;
        else this.razaoSocial = novo;
    }
    @JsonIgnore
    public String getCadastro(){
        if(tipo.equals("Física")) return cpf;
        else return cnpj;
    }

    public void setCadastro(String novo){
        if(tipo.equals("Física")) this.cpf = novo;
        else this.cnpj = novo;
    }
    @JsonIgnore
    public String getRegistro(){
        if(tipo.equals("Física")) return rg;
        else return inscricaoEstadual;
    }

    public void setRegistro(String novo){
        if(tipo.equals("Física")) this.rg = novo;
        else this.inscricaoEstadual = novo;
    }

    public List<Endereco> getEnderecos() {
        return enderecos;
    }

    public void addEndereco(Endereco end){
        enderecos.add(end);
    }

    public String getNome() {
        return nome;
    }

    public String getRazaoSocial() {
        return razaoSocial;
    }

    public String getCpf() {
        return cpf;
    }

    public String getCnpj() {
        return cnpj;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Monitorador other = (Monitorador) obj;
        if (id == null) {
            return other.id == null;
        } else return id.equals(other.id);
    }

    @Override
    public String toString() {
        return "Monitorador [ativo=" + ativo + ", cnpj=" + cnpj + ", cpf=" + cpf + ", dataNascimento=" + dataNascimento
                + ", email=" + email + ", id=" + id + ", inscricaoEstadual=" + inscricaoEstadual + ", nome=" + nome
                + ", razaoSocial=" + razaoSocial + ", rg=" + rg + ", tipo=" + tipo +"]";
    }


}
