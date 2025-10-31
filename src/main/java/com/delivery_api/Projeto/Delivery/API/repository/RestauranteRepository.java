package com.delivery_api.Projeto.Delivery.API.repository;

import com.delivery_api.Projeto.Delivery.API.entity.Restaurante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
// JpaRepository<[Entidade], [Tipo da Chave Primária]>
public interface RestauranteRepository extends JpaRepository<Restaurante, Long> {

    // 1. Requisito do Service: Verificar se o CNPJ já existe
    boolean existsByCnpj(String cnpj);

    // 2. Requisito do Service: Buscar por CNPJ
    Optional<Restaurante> findByCnpj(String cnpj);

    // 3. Requisito do Service: Listar todos os que estão ativos
    List<Restaurante> findByAtivoTrue();

    // 4. Requisito do Service: Buscar por nome (ignorando maiúsculas/minúsculas)
    List<Restaurante> findByNomeContainingIgnoreCase(String nome);
}