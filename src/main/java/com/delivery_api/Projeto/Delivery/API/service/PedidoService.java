package com.delivery_api.Projeto.Delivery.API.service;

import com.delivery_api.Projeto.Delivery.API.dto.PedidoRequestDTO;
import com.delivery_api.Projeto.Delivery.API.entity.Pedido;
import com.delivery_api.Projeto.Delivery.API.mapper.PedidoMapper;
import com.delivery_api.Projeto.Delivery.API.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PedidoService {

    private final PedidoRepository pedidoRepository;

    private final PedidoMapper pedidoMapper;

    @Autowired
    public PedidoService(PedidoRepository pedidoRepository,
                         PedidoMapper pedidoMapper) {
        this.pedidoRepository = pedidoRepository;
        this.pedidoMapper = pedidoMapper;
    }

    /**
     * Cadastrar novo pedido a partir do DTO (mapper resolve cliente/restaurante)
     */
    public Pedido cadastrar(PedidoRequestDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("Pedido não pode ser nulo");
        }

        // Mapear DTO para entidade (cliente/restaurante resolvidos pelo mapper)
        Pedido pedido = pedidoMapper.toEntity(dto);

        // Definir data e número se ausentes
        if (pedido.getDataPedido() == null) {
            pedido.setDataPedido(LocalDateTime.now());
        }
        if (pedido.getNumeroPedido() == null || pedido.getNumeroPedido().trim().isEmpty()) {
            pedido.setNumeroPedido("PED-" + System.currentTimeMillis());
        }

        return pedidoRepository.save(pedido);
    }

    /**
     * Atualizar pedido a partir do DTO (mapper resolve cliente/restaurante)
     */
    public Pedido atualizar(Long id, PedidoRequestDTO dto) {
        Pedido existente = buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Pedido não encontrado: " + id));

        // Mapear campos simples + cliente/restaurante
        Pedido temp = pedidoMapper.toEntity(dto);

        // Atualizar associação e campos editáveis
        existente.setCliente(temp.getCliente());
        existente.setRestaurante(temp.getRestaurante());
        existente.setItens(temp.getItens());
        existente.setValorTotal(temp.getValorTotal());
        existente.setObservacoes(temp.getObservacoes());
        existente.setStatus(temp.getStatus());

        return pedidoRepository.save(existente);
    }

    /**
     * Métodos existentes que operam diretamente com entidade (mantidos para compatibilidade interna)
     */
    public Pedido cadastrar(Pedido pedido) {
        if (pedido == null) {
            throw new IllegalArgumentException("Pedido não pode ser nulo");
        }

        if (pedido.getDataPedido() == null) {
            pedido.setDataPedido(LocalDateTime.now());
        }

        if (pedido.getNumeroPedido() == null || pedido.getNumeroPedido().trim().isEmpty()) {
            pedido.setNumeroPedido("PED-" + System.currentTimeMillis());
        }

        return pedidoRepository.save(pedido);
    }

    @Transactional(readOnly = true)
    public List<Pedido> listarTodos() {
        return pedidoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Pedido> buscarPorId(Long id) {
        return pedidoRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<Pedido> buscarPorNumero(String numeroPedido) {
        return pedidoRepository.findByNumeroPedido(numeroPedido);
    }

    @Transactional(readOnly = true)
    public List<Pedido> buscarPorStatus(String status) {
        return pedidoRepository.findByStatus(status);
    }

    @Transactional(readOnly = true)
    public List<Pedido> buscarPorCliente(Long clienteId) {
        return pedidoRepository.findByClienteId(clienteId);
    }

    @Transactional(readOnly = true)
    public List<Pedido> buscarPorRestaurante(Long restauranteId) {
        return pedidoRepository.findByRestauranteId(restauranteId);
    }

    @Transactional(readOnly = true)
    public List<Pedido> buscarPorPeriodo(LocalDateTime inicio, LocalDateTime fim) {
        return pedidoRepository.findByDataPedidoBetween(inicio, fim);
    }

    @Transactional(readOnly = true)
    public List<Pedido> buscarPorClienteEStatus(Long clienteId, String status) {
        return pedidoRepository.findByClienteIdAndStatus(clienteId, status);
    }

    @Transactional(readOnly = true)
    public List<Pedido> buscarPorRestauranteEStatus(Long restauranteId, String status) {
        return pedidoRepository.findByRestauranteIdAndStatus(restauranteId, status);
    }

    public Pedido atualizar(Long id, Pedido pedidoAtualizado) {
        Pedido pedido = buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Pedido não encontrado: " + id));

        // Atualizar campos editáveis
        pedido.setStatus(pedidoAtualizado.getStatus());
        pedido.setValorTotal(pedidoAtualizado.getValorTotal());
        pedido.setObservacoes(pedidoAtualizado.getObservacoes());
        pedido.setItens(pedidoAtualizado.getItens());
        pedido.setCliente(pedidoAtualizado.getCliente());
        pedido.setRestaurante(pedidoAtualizado.getRestaurante());

        return pedidoRepository.save(pedido);
    }

    public void deletar(Long id) {
        if (!pedidoRepository.existsById(id)) {
            throw new IllegalArgumentException("Pedido não encontrado: " + id);
        }
        pedidoRepository.deleteById(id);
    }
}
