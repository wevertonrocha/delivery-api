package com.delivery_api.Projeto.Delivery.API.mapper;

import com.delivery_api.Projeto.Delivery.API.dto.request.RestauranteRequestDTO;
import com.delivery_api.Projeto.Delivery.API.dto.response.RestauranteResponseDTO;
import com.delivery_api.Projeto.Delivery.API.entity.Restaurante;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-11-07T20:24:42-0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.9 (Oracle Corporation)"
)
@Component
public class RestauranteMapperImpl implements RestauranteMapper {

    @Override
    public RestauranteResponseDTO toResponse(Restaurante restaurante) {
        if ( restaurante == null ) {
            return null;
        }

        RestauranteResponseDTO restauranteResponseDTO = new RestauranteResponseDTO();

        restauranteResponseDTO.setId( restaurante.getId() );
        restauranteResponseDTO.setNome( restaurante.getNome() );
        restauranteResponseDTO.setCnpj( restaurante.getCnpj() );
        restauranteResponseDTO.setTelefone( restaurante.getTelefone() );
        restauranteResponseDTO.setEndereco( restaurante.getEndereco() );
        restauranteResponseDTO.setAtivo( restaurante.isAtivo() );

        return restauranteResponseDTO;
    }

    @Override
    public Restaurante toEntity(RestauranteRequestDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Restaurante restaurante = new Restaurante();

        restaurante.setNome( dto.getNome() );
        restaurante.setCnpj( dto.getCnpj() );
        restaurante.setTelefone( dto.getTelefone() );
        restaurante.setEndereco( dto.getEndereco() );

        return restaurante;
    }
}
