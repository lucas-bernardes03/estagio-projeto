package com.unikasistemas.deangular.entities;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.unikasistemas.deangular.enums.TipoPessoa;

@Entity
@Table(name = "monitoradores")
public class Monitorador implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private TipoPessoa tipo;
    
    private String nome = null;
    private String cpf = null;
    private String rg = null;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "GMT")
    private Instant dataNascimento = null;
    
    private String razaoSocial = null;
    private String cnpj = null;
    private String inscricaoEstadual = null;

    private String email;
    private Boolean ativo;

    
    @OneToMany(mappedBy = "monitorador")
    private List<Endereco> enderecos = new ArrayList<>();

    public Monitorador(){}

    public Monitorador(Long id,TipoPessoa tipoP, String identificacao, String cadastro, String email, String registro, Instant dataNascimento, Boolean ativo){
            this.id = id;
            this.tipo = tipoP;
            
            if(tipoP.equals(TipoPessoa.FISICA)){
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

    public TipoPessoa getTipo() {
        return tipo;
    }

    public void setTipo(TipoPessoa tipo) {
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

    public Instant getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(Instant dataNascimento) {
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
        if(tipo.equals(TipoPessoa.FISICA)) return nome;
        else return razaoSocial;
    }

    public void setIdentificacao(String novo){
        if(tipo.equals(TipoPessoa.FISICA)) this.nome = novo;
        else this.razaoSocial = novo;
    }

    @JsonIgnore
    public String getCadastro(){
        if(tipo.equals(TipoPessoa.FISICA)) return cpf;
        else return cnpj;
    }

    public void setCadastro(String novo){
        if(tipo.equals(TipoPessoa.FISICA)) this.cpf = novo;
        else this.cnpj = novo;
    }

    @JsonIgnore
    public String getRegistro(){
        if(tipo.equals(TipoPessoa.FISICA)) return rg;
        else return inscricaoEstadual;
    }

    public void setRegistro(String novo){
        if(tipo.equals(TipoPessoa.FISICA)) this.rg = novo;
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
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Monitorador [ativo=" + ativo + ", cnpj=" + cnpj + ", cpf=" + cpf + ", dataNascimento=" + dataNascimento
                + ", email=" + email + ", id=" + id + ", inscricaoEstadual=" + inscricaoEstadual + ", nome=" + nome
                + ", razaoSocial=" + razaoSocial + ", rg=" + rg + ", tipo=" + tipo + "]";
    }


}
