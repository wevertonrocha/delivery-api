package com.delivery_api.Projeto.Delivery.API.repository;

import com.delivery_api.Projeto.Delivery.API.entity.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    // Buscar produtos por restaurante
    List<Produto> findByRestauranteId(Long restauranteId);

    // Buscar produtos disponíveis
    List<Produto> findByDisponivelTrue();

    // Buscar produtos por categoria
    List<Produto> findByCategoria(String categoria);

    // Buscar produtos pelo nome (contendo, case-insensitive)
    List<Produto> findByNomeContainingIgnoreCase(String nome);

    // Buscar produtos disponíveis de um restaurante
    List<Produto> findByRestauranteIdAndDisponivelTrue(Long restauranteId);
}
