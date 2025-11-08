package com.delivery_api.Projeto.Delivery.API.mapper;

import com.delivery_api.Projeto.Delivery.API.dto.request.PedidoRequestDTO;
import com.delivery_api.Projeto.Delivery.API.dto.response.PedidoResponseDTO;
import com.delivery_api.Projeto.Delivery.API.entity.Cliente;
import com.delivery_api.Projeto.Delivery.API.entity.Pedido;
import com.delivery_api.Projeto.Delivery.API.entity.Restaurante;
import com.delivery_api.Projeto.Delivery.API.repository.RestauranteRepository;
import com.delivery_api.Projeto.Delivery.API.service.ClienteService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class PedidoMapper {

    @Autowired
    protected ClienteService clienteService;

    @Autowired
    protected RestauranteRepository restauranteRepository;

    @Mapping(target = "clienteId", source = "cliente.id")
    @Mapping(target = "restauranteId", source = "restaurante.id")
    public abstract PedidoResponseDTO toResponse(Pedido pedido);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "numeroPedido", ignore = true)
    @Mapping(target = "dataPedido", ignore = true)
    @Mapping(target = "cliente", source = "clienteId", qualifiedByName = "idToCliente")
    @Mapping(target = "restaurante", source = "restauranteId", qualifiedByName = "idToRestaurante")
    public abstract Pedido toEntity(PedidoRequestDTO dto);

    @Named("idToCliente")
    protected Cliente idToCliente(Long id) {
        if (id == null) return null;
        return clienteService.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado: " + id));
    }

    @Named("idToRestaurante")
    protected Restaurante idToRestaurante(Long id) {
        if (id == null) return null;
        return restauranteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Restaurante não encontrado: " + id));
    }
}
