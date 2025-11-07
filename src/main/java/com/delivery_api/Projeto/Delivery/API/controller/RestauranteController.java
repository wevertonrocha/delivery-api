package com.delivery_api.Projeto.Delivery.API.controller;

import com.delivery_api.Projeto.Delivery.API.dto.RestauranteRequestDTO;
import com.delivery_api.Projeto.Delivery.API.dto.RestauranteResponseDTO;
import com.delivery_api.Projeto.Delivery.API.service.RestauranteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/restaurantes")
@CrossOrigin(origins = "*")
public class RestauranteController {

    @Autowired
    private RestauranteService restauranteService;

    /**
     * Cadastrar novo restaurante (DTO)
     */
    @PostMapping
    public ResponseEntity<?> cadastrar(@Validated @Valid @RequestBody RestauranteRequestDTO dto) {
        try {
            RestauranteResponseDTO salvo = restauranteService.cadastrar(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro interno do servidor");
        }
    }

    /**
     * Listar todos os restaurantes ativos (DTO)
     */
    @GetMapping
    public ResponseEntity<List<RestauranteResponseDTO>> listar() {
        List<RestauranteResponseDTO> restaurantes = restauranteService.listarAtivosDTO();
        return ResponseEntity.ok(restaurantes);
    }

    /**
     * Buscar restaurante por ID (DTO)
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        Optional<RestauranteResponseDTO> restaurante = restauranteService.buscarPorIdDTO(id);
        return restaurante.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Atualizar restaurante (DTO)
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @Validated @Valid @RequestBody RestauranteRequestDTO dto) {
        try {
            RestauranteResponseDTO atualizado = restauranteService.atualizarDTO(id, dto);
            return ResponseEntity.ok(atualizado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro interno do servidor");
        }
    }

    /**
     * Inativar restaurante (soft delete)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> inativar(@PathVariable Long id) {
        try {
            restauranteService.inativar(id);
            return ResponseEntity.ok().body("Restaurante inativado com sucesso");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro interno do servidor");
        }
    }

    /**
     * Buscar restaurantes por nome (DTO)
     */
    @GetMapping("/buscar")
    public ResponseEntity<List<RestauranteResponseDTO>> buscarPorNome(@RequestParam String nome) {
        List<RestauranteResponseDTO> restaurantes = restauranteService.buscarPorNomeDTO(nome);
        return ResponseEntity.ok(restaurantes);
    }

    /**
     * Buscar restaurante por CNPJ (DTO)
     */
    @GetMapping("/cnpj/{cnpj}")
    public ResponseEntity<?> buscarPorCnpj(@PathVariable String cnpj) {
        Optional<RestauranteResponseDTO> restaurante = restauranteService.buscarPorCnpjDTO(cnpj);
        return restaurante.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}