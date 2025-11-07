package com.delivery_api.Projeto.Delivery.API.mapper;

import com.delivery_api.Projeto.Delivery.API.dto.PedidoRequestDTO;
import com.delivery_api.Projeto.Delivery.API.dto.PedidoResponseDTO;
import com.delivery_api.Projeto.Delivery.API.entity.Cliente;
import com.delivery_api.Projeto.Delivery.API.entity.Pedido;
import com.delivery_api.Projeto.Delivery.API.entity.Restaurante;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-11-06T22:32:05-0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.9 (Oracle Corporation)"
)
@Component
public class PedidoMapperImpl extends PedidoMapper {

    @Override
    public PedidoResponseDTO toResponse(Pedido pedido) {
        if ( pedido == null ) {
            return null;
        }

        PedidoResponseDTO pedidoResponseDTO = new PedidoResponseDTO();

        pedidoResponseDTO.setClienteId( pedidoClienteId( pedido ) );
        pedidoResponseDTO.setRestauranteId( pedidoRestauranteId( pedido ) );
        pedidoResponseDTO.setId( pedido.getId() );
        pedidoResponseDTO.setNumeroPedido( pedido.getNumeroPedido() );
        pedidoResponseDTO.setDataPedido( pedido.getDataPedido() );
        pedidoResponseDTO.setStatus( pedido.getStatus() );
        pedidoResponseDTO.setValorTotal( pedido.getValorTotal() );
        pedidoResponseDTO.setObservacoes( pedido.getObservacoes() );
        pedidoResponseDTO.setItens( pedido.getItens() );

        return pedidoResponseDTO;
    }

    @Override
    public Pedido toEntity(PedidoRequestDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Pedido pedido = new Pedido();

        pedido.setCliente( idToCliente( dto.getClienteId() ) );
        pedido.setRestaurante( idToRestaurante( dto.getRestauranteId() ) );
        pedido.setStatus( dto.getStatus() );
        pedido.setValorTotal( dto.getValorTotal() );
        pedido.setObservacoes( dto.getObservacoes() );
        pedido.setItens( dto.getItens() );

        return pedido;
    }

    private Long pedidoClienteId(Pedido pedido) {
        if ( pedido == null ) {
            return null;
        }
        Cliente cliente = pedido.getCliente();
        if ( cliente == null ) {
            return null;
        }
        Long id = cliente.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private Long pedidoRestauranteId(Pedido pedido) {
        if ( pedido == null ) {
            return null;
        }
        Restaurante restaurante = pedido.getRestaurante();
        if ( restaurante == null ) {
            return null;
        }
        Long id = restaurante.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
