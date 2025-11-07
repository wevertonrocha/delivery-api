package com.delivery_api.Projeto.Delivery.API.dto;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

@Data
public class PedidoRequestDTO {

    @NotNull
    private Long clienteId;

    @NotNull
    private Long restauranteId;

    @NotBlank
    private String itens;

    @NotNull
    @PositiveOrZero
    private BigDecimal valorTotal;

    private String observacoes;

    private String status;
}

