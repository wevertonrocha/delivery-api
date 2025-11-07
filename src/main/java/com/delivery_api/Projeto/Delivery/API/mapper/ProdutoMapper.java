package com.delivery_api.Projeto.Delivery.API.mapper;

import com.delivery_api.Projeto.Delivery.API.dto.ProdutoRequestDTO;
import com.delivery_api.Projeto.Delivery.API.dto.ProdutoResponseDTO;
import com.delivery_api.Projeto.Delivery.API.entity.Produto;
import com.delivery_api.Projeto.Delivery.API.entity.Restaurante;
import com.delivery_api.Projeto.Delivery.API.repository.RestauranteRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class ProdutoMapper {

    @Autowired
    protected RestauranteRepository restauranteRepository;

    @Mapping(target = "restauranteId", source = "restaurante.id")
    public abstract ProdutoResponseDTO toResponse(Produto produto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "restaurante", source = "restauranteId", qualifiedByName = "idToRestaurante")
    public abstract Produto toEntity(ProdutoRequestDTO dto);

    @Named("idToRestaurante")
    protected Restaurante idToRestaurante(Long id) {
        if (id == null) return null;
        return restauranteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Restaurante não encontrado: " + id));
    }
}

