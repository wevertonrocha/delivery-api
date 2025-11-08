package com.delivery_api.Projeto.Delivery.API.controller;

import com.delivery_api.Projeto.Delivery.API.dto.request.PedidoRequestDTO;
import com.delivery_api.Projeto.Delivery.API.dto.response.PedidoResponseDTO;
import com.delivery_api.Projeto.Delivery.API.entity.Pedido;
import com.delivery_api.Projeto.Delivery.API.mapper.PedidoMapper;
import com.delivery_api.Projeto.Delivery.API.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/pedidos")
@CrossOrigin(origins = "*")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @Autowired
    private PedidoMapper pedidoMapper;

    @PostMapping
    public ResponseEntity<?> cadastrar(@Validated @Valid @RequestBody PedidoRequestDTO pedidoDto) {
        try {
            Pedido salvo = pedidoService.cadastrar(pedidoDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(pedidoMapper.toResponse(salvo));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro interno do servidor");
        }
    }

    @GetMapping
    public ResponseEntity<List<PedidoResponseDTO>> listar() {
        List<PedidoResponseDTO> pedidos = pedidoService.listarTodos().stream().map(pedidoMapper::toResponse).collect(Collectors.toList());
        return ResponseEntity.ok(pedidos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        Optional<Pedido> pedido = pedidoService.buscarPorId(id);
        if (pedido.isPresent()) {
            return ResponseEntity.ok(pedidoMapper.toResponse(pedido.get()));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Buscar por número do pedido
    @GetMapping("/numero/{numero}")
    public ResponseEntity<?> buscarPorNumero(@PathVariable String numero) {
        Optional<Pedido> pedido = pedidoService.buscarPorNumero(numero);
        return pedido.map(p -> ResponseEntity.ok(pedidoMapper.toResponse(p))).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Buscar por status (query param)
    @GetMapping("/status")
    public ResponseEntity<List<PedidoResponseDTO>> buscarPorStatus(@RequestParam String status) {
        List<PedidoResponseDTO> pedidos = pedidoService.buscarPorStatus(status).stream().map(pedidoMapper::toResponse).collect(Collectors.toList());
        return ResponseEntity.ok(pedidos);
    }

    // Buscar por cliente
    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<PedidoResponseDTO>> buscarPorCliente(@PathVariable Long clienteId) {
        List<PedidoResponseDTO> pedidos = pedidoService.buscarPorCliente(clienteId).stream().map(pedidoMapper::toResponse).collect(Collectors.toList());
        return ResponseEntity.ok(pedidos);
    }

    // Buscar por restaurante
    @GetMapping("/restaurante/{restauranteId}")
    public ResponseEntity<List<PedidoResponseDTO>> buscarPorRestaurante(@PathVariable Long restauranteId) {
        List<PedidoResponseDTO> pedidos = pedidoService.buscarPorRestaurante(restauranteId).stream().map(pedidoMapper::toResponse).collect(Collectors.toList());
        return ResponseEntity.ok(pedidos);
    }

    // Buscar por período (datas em ISO)
    @GetMapping("/periodo")
    public ResponseEntity<List<PedidoResponseDTO>> buscarPorPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fim) {
        List<PedidoResponseDTO> pedidos = pedidoService.buscarPorPeriodo(inicio, fim).stream().map(pedidoMapper::toResponse).collect(Collectors.toList());
        return ResponseEntity.ok(pedidos);
    }

    // Buscar por cliente e status
    @GetMapping("/cliente/{clienteId}/status")
    public ResponseEntity<List<PedidoResponseDTO>> buscarPorClienteEStatus(@PathVariable Long clienteId,
                                                                @RequestParam String status) {
        List<PedidoResponseDTO> pedidos = pedidoService.buscarPorClienteEStatus(clienteId, status).stream().map(pedidoMapper::toResponse).collect(Collectors.toList());
        return ResponseEntity.ok(pedidos);
    }

    // Buscar por restaurante e status
    @GetMapping("/restaurante/{restauranteId}/status")
    public ResponseEntity<List<PedidoResponseDTO>> buscarPorRestauranteEStatus(@PathVariable Long restauranteId,
                                                                    @RequestParam String status) {
        List<PedidoResponseDTO> pedidos = pedidoService.buscarPorRestauranteEStatus(restauranteId, status).stream().map(pedidoMapper::toResponse).collect(Collectors.toList());
        return ResponseEntity.ok(pedidos);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @Validated @Valid @RequestBody PedidoRequestDTO pedidoDto) {
        try {
            Pedido atualizado = pedidoService.atualizar(id, pedidoDto);
            return ResponseEntity.ok(pedidoMapper.toResponse(atualizado));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro interno do servidor");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        try {
            pedidoService.deletar(id);
            return ResponseEntity.ok().body("Pedido deletado com sucesso");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro interno do servidor");
        }
    }
}
