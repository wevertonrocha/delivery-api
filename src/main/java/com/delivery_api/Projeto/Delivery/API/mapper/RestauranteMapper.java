package com.delivery_api.Projeto.Delivery.API.mapper;

import com.delivery_api.Projeto.Delivery.API.dto.RestauranteRequestDTO;
import com.delivery_api.Projeto.Delivery.API.dto.RestauranteResponseDTO;
import com.delivery_api.Projeto.Delivery.API.entity.Restaurante;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RestauranteMapper {

    // Map entity -> response DTO
    RestauranteResponseDTO toResponse(Restaurante restaurante);

    // Map request DTO -> entity (id handled elsewhere)
    @Mapping(target = "id", ignore = true)
    Restaurante toEntity(RestauranteRequestDTO dto);
}

