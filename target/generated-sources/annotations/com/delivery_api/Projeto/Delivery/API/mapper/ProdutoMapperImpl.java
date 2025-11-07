package com.delivery_api.Projeto.Delivery.API.mapper;

import com.delivery_api.Projeto.Delivery.API.dto.ProdutoRequestDTO;
import com.delivery_api.Projeto.Delivery.API.dto.ProdutoResponseDTO;
import com.delivery_api.Projeto.Delivery.API.entity.Produto;
import com.delivery_api.Projeto.Delivery.API.entity.Restaurante;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-11-06T22:32:05-0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.9 (Oracle Corporation)"
)
@Component
public class ProdutoMapperImpl extends ProdutoMapper {

    @Override
    public ProdutoResponseDTO toResponse(Produto produto) {
        if ( produto == null ) {
            return null;
        }

        ProdutoResponseDTO produtoResponseDTO = new ProdutoResponseDTO();

        produtoResponseDTO.setRestauranteId( produtoRestauranteId( produto ) );
        produtoResponseDTO.setId( produto.getId() );
        produtoResponseDTO.setNome( produto.getNome() );
        produtoResponseDTO.setDescricao( produto.getDescricao() );
        produtoResponseDTO.setPreco( produto.getPreco() );
        produtoResponseDTO.setCategoria( produto.getCategoria() );
        produtoResponseDTO.setDisponivel( produto.getDisponivel() );

        return produtoResponseDTO;
    }

    @Override
    public Produto toEntity(ProdutoRequestDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Produto produto = new Produto();

        produto.setRestaurante( idToRestaurante( dto.getRestauranteId() ) );
        produto.setNome( dto.getNome() );
        produto.setDescricao( dto.getDescricao() );
        produto.setPreco( dto.getPreco() );
        produto.setCategoria( dto.getCategoria() );
        produto.setDisponivel( dto.getDisponivel() );

        return produto;
    }

    private Long produtoRestauranteId(Produto produto) {
        if ( produto == null ) {
            return null;
        }
        Restaurante restaurante = produto.getRestaurante();
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
