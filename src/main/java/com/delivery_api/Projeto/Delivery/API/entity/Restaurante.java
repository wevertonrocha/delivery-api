package com.delivery_api.Projeto.Delivery.API.entity;

import jakarta.persistence.*; // Note: Usamos jakarta.persistence para Spring Boot 3+
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

// Anotações do Lombok para gerar Getters, Setters, Construtores
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

// Anotações da JPA
@Entity
@Table(name = "restaurantes")
public class Restaurante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Assumimos que 'nome' e 'cnpj' são campos obrigatórios
    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String cnpj; // Chave única para validação no service

    private String telefone;

    private String endereco;

    // Atributo 'ativo' para soft delete (inativação)
    @Column(nullable = false)
    private boolean ativo;

    /**
     * Método auxiliar para inativar o restaurante (soft delete)
     */
    public void inativar() {
        this.ativo = false;
    }
}