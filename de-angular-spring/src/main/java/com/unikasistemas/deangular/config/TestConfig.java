package com.unikasistemas.deangular.config;

import java.time.Instant;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.unikasistemas.deangular.entities.Endereco;
import com.unikasistemas.deangular.entities.Monitorador;
import com.unikasistemas.deangular.repository.EnderecoRepository;
import com.unikasistemas.deangular.repository.MonitoradorRepository;


@Configuration
@Profile("test")
public class TestConfig implements CommandLineRunner{
    @Autowired
    private MonitoradorRepository monitoradorRepository;

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Override
    public void run(String... args) throws Exception {
        Monitorador m1 = new Monitorador(null, "Física", "Marcos Paulo Castro Neves", "12345678900", "mcastroneves@gmail.com", "7638946", Instant.parse("1998-02-22T22:45:22Z"), true);
        Monitorador m2 = new Monitorador(null, "Física", "Julia Bonifacio da Silva", "98765432100", "jbsilva@gmail.com", "8927354", Instant.parse("1999-12-30T02:15:26Z"), false);
        Monitorador m3 = new Monitorador(null, "Jurídica", "Ferragista Loureira", "12345678912399", "loureiro@ferragista.com.br", "837462518", null, true);
        Monitorador m4 = new Monitorador(null, "Jurídica", "Tapecaria Anapolis", "98765432198700", "anpstapecaria@gmail.com", "846291029", null, true);
        monitoradorRepository.saveAll(Arrays.asList(m1,m2,m3,m4));

        Endereco e1 = new Endereco(null, "Rua 12 de Setembro", "101", "37564782", "Santa Marcia", "(62)999999999", "Cocalzin", "Goias", true, m1);
        Endereco e2 = new Endereco(null, "Rua Antonio Jobim", "221", "74625163", "Ubatuba", "(32)911111111", "Marilandia", "Mato Grosso", false, m2);
        Endereco e3 = new Endereco(null, "Avenida Brasilia", "332", "89283764", "Setor Central", "(54)23456789", "Hidrolandia", "Goias", true, m3);
        Endereco e4 = new Endereco(null, "Avenida Soa", "0", "73846154", "Santa Maria", "(33)922222222", "Matagal", "Rio Grande do Norte", true, m4);
        Endereco e5 = new Endereco(null, "Rua Paulo Viana", "55", "82938476", "Marialia", "(12)87462521", "Rio Avel", "Amapa", false, m1);
        Endereco e6 = new Endereco(null, "Rua Malindera", "211", "73847211", "Lourdes", "(77)977777777", "Cachoeiras", "Roraima", true, m2);
        enderecoRepository.saveAll(Arrays.asList(e1,e2,e3,e4,e5,e6));
        
        m1.addEndereco(e1);
        m1.addEndereco(e5);
        m2.addEndereco(e2);
        m2.addEndereco(e6);
        m3.addEndereco(e3);
        m4.addEndereco(e4);
        
        monitoradorRepository.saveAll(Arrays.asList(m1,m2,m3,m4));

    }
    
}
