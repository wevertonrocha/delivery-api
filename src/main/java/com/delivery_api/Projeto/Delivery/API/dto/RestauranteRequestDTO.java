package com.delivery_api.Projeto.Delivery.API.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RestauranteRequestDTO {

    @NotBlank
    @Size(min = 2, max = 150)
    private String nome;

    @NotBlank
    @Pattern(regexp = "\\d{14}", message = "CNPJ deve conter 14 dígitos numéricos")
    private String cnpj;

    private String telefone;

    private String endereco;
}

