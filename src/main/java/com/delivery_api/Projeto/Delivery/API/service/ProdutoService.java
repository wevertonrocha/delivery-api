package com.delivery_api.Projeto.Delivery.API.service;

import com.delivery_api.Projeto.Delivery.API.dto.request.ProdutoRequestDTO;
import com.delivery_api.Projeto.Delivery.API.dto.response.ProdutoResponseDTO;
import com.delivery_api.Projeto.Delivery.API.entity.Produto;
import com.delivery_api.Projeto.Delivery.API.entity.Restaurante;
import com.delivery_api.Projeto.Delivery.API.mapper.ProdutoMapper;
import com.delivery_api.Projeto.Delivery.API.repository.ProdutoRepository;
import com.delivery_api.Projeto.Delivery.API.repository.RestauranteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final RestauranteRepository restauranteRepository;
    private final ProdutoMapper produtoMapper;

    @Autowired
    public ProdutoService(ProdutoRepository produtoRepository, RestauranteRepository restauranteRepository, ProdutoMapper produtoMapper) {
        this.produtoRepository = produtoRepository;
        this.restauranteRepository = restauranteRepository;
        this.produtoMapper = produtoMapper;
    }

    public ProdutoResponseDTO cadastrar(ProdutoRequestDTO dto) {
        Produto produto = produtoMapper.toEntity(dto);
        Produto salvo = produtoRepository.save(produto);
        return produtoMapper.toResponse(salvo);
    }

    @Transactional(readOnly = true)
    public List<ProdutoResponseDTO> listarTodosDTO() {
        return produtoRepository.findAll().stream().map(produtoMapper::toResponse).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<ProdutoResponseDTO> buscarPorIdDTO(Long id) {
        return produtoRepository.findById(id).map(produtoMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public List<ProdutoResponseDTO> buscarPorNomeDTO(String nome) {
        return produtoRepository.findByNomeContainingIgnoreCase(nome).stream().map(produtoMapper::toResponse).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ProdutoResponseDTO> listarPorRestauranteDTO(Long restauranteId) {
        return produtoRepository.findByRestauranteId(restauranteId).stream().map(produtoMapper::toResponse).collect(Collectors.toList());
    }

    public ProdutoResponseDTO atualizarDTO(Long id, ProdutoRequestDTO dto) {
        Produto existente = produtoRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Produto não encontrado: " + id));
        Produto temp = produtoMapper.toEntity(dto);

        existente.setNome(temp.getNome());
        existente.setDescricao(temp.getDescricao());
        existente.setPreco(temp.getPreco());
        existente.setCategoria(temp.getCategoria());
        existente.setDisponivel(temp.getDisponivel());
        existente.setRestaurante(temp.getRestaurante());

        Produto atualizado = produtoRepository.save(existente);
        return produtoMapper.toResponse(atualizado);
    }

    public void deletar(Long id) {
        if (!produtoRepository.existsById(id)) {
            throw new IllegalArgumentException("Produto não encontrado: " + id);
        }
        produtoRepository.deleteById(id);
    }

    // Métodos existentes que retornam/aceitam entidade (mantidos se necessário)
    public Produto cadastrar(Produto produto) {
        if (produto == null) {
            throw new IllegalArgumentException("Produto não pode ser nulo");
        }

        // Se foi informado restaurante com id, validar existência
        if (produto.getRestaurante() != null && produto.getRestaurante().getId() != null) {
            Long rid = produto.getRestaurante().getId();
            Restaurante restaurante = restauranteRepository.findById(rid)
                    .orElseThrow(() -> new IllegalArgumentException("Restaurante não encontrado: " + rid));
            produto.setRestaurante(restaurante);
        }

        return produtoRepository.save(produto);
    }

    @Transactional(readOnly = true)
    public List<Produto> listarTodos() {
        return produtoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Produto> buscarPorId(Long id) {
        return produtoRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Produto> buscarPorNome(String nome) {
        return produtoRepository.findByNomeContainingIgnoreCase(nome);
    }

    @Transactional(readOnly = true)
    public List<Produto> listarPorRestaurante(Long restauranteId) {
        return produtoRepository.findByRestauranteId(restauranteId);
    }

    public Produto atualizar(Long id, Produto produtoAtualizado) {
        Produto produto = buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado: " + id));

        // Se restaurante informado, validar e setar
        if (produtoAtualizado.getRestaurante() != null && produtoAtualizado.getRestaurante().getId() != null) {
            Long rid = produtoAtualizado.getRestaurante().getId();
            Restaurante restaurante = restauranteRepository.findById(rid)
                    .orElseThrow(() -> new IllegalArgumentException("Restaurante não encontrado: " + rid));
            produto.setRestaurante(restaurante);
        } else {
            produto.setRestaurante(null);
        }

        produto.setNome(produtoAtualizado.getNome());
        produto.setDescricao(produtoAtualizado.getDescricao());
        produto.setPreco(produtoAtualizado.getPreco());
        produto.setCategoria(produtoAtualizado.getCategoria());
        produto.setDisponivel(produtoAtualizado.getDisponivel());

        return produtoRepository.save(produto);
    }
}
