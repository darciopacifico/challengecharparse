package br.com.avaliacao.parser;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "br.com.avaliacao.checkout.db")
public class AvaliacaoJavaApplicationTest {

    public static void main(String[] args) {
        SpringApplication.run(AvaliacaoJavaApplicationTest.class, args);
    }




}
