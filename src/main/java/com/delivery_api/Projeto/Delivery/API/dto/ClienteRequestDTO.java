package com.delivery_api.Projeto.Delivery.API.dto;

import lombok.Data;

@Data
public class ClienteRequestDTO {

    private String nome;

    private String email;

    private String telefone;

    private String endereco;

    public void setAtivo(boolean b) {
    }
}
