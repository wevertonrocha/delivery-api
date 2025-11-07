package com.delivery_api.Projeto.Delivery.API.dto;

import lombok.Data;

@Data
public class RestauranteResponseDTO {
    private Long id;
    private String nome;
    private String cnpj;
    private String telefone;
    private String endereco;
    private boolean ativo;
}
