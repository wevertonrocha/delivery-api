package com.delivery_api.Projeto.Delivery.API.dto.request;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

@Data
public class ProdutoRequestDTO {

    @NotBlank
    private String nome;

    private String descricao;

    @NotNull
    @PositiveOrZero
    private BigDecimal preco;

    private String categoria;

    @NotNull
    private Boolean disponivel;

    @NotNull
    private Long restauranteId;
}

