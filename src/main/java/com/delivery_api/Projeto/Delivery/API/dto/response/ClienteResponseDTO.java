package com.delivery_api.Projeto.Delivery.API.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ClienteResponseDTO {
    private Long id;

    private String nome;

    private String email;

    private String telefone;

    private String endereco;

    private LocalDateTime dataCadastro;

    private Boolean ativo;
}
