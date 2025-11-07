package com.delivery_api.Projeto.Delivery.API.service;

import com.delivery_api.Projeto.Delivery.API.dto.RestauranteRequestDTO;
import com.delivery_api.Projeto.Delivery.API.dto.RestauranteResponseDTO;
import com.delivery_api.Projeto.Delivery.API.entity.Restaurante;
import com.delivery_api.Projeto.Delivery.API.mapper.RestauranteMapper;
import com.delivery_api.Projeto.Delivery.API.repository.RestauranteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

// O nome da classe foi alterado de ClienteService para RestauranteService
@Service
@Transactional
public class RestauranteService {

    // A injeção de dependência foi alterada para RestauranteRepository
    @Autowired
    private RestauranteRepository restauranteRepository;

    private final RestauranteMapper restauranteMapper;

    @Autowired
    public RestauranteService(RestauranteMapper restauranteMapper) {
        this.restauranteMapper = restauranteMapper;
    }

    /**
     * Cadastrar novo restaurante (DTO)
     */
    public RestauranteResponseDTO cadastrar(RestauranteRequestDTO dto) {
        Restaurante restaurante = restauranteMapper.toEntity(dto);

        // Validação de CNPJ único
        if (restauranteRepository.existsByCnpj(restaurante.getCnpj())) {
            throw new IllegalArgumentException("CNPJ já cadastrado: " + restaurante.getCnpj());
        }

        validarDadosRestaurante(restaurante);
        restaurante.setAtivo(true);

        Restaurante salvo = restauranteRepository.save(restaurante);
        return restauranteMapper.toResponse(salvo);
    }

    /**
     * Buscar restaurante por ID (DTO)
     */
    @Transactional(readOnly = true)
    public Optional<RestauranteResponseDTO> buscarPorIdDTO(Long id) {
        return restauranteRepository.findById(id).map(restauranteMapper::toResponse);
    }

    /**
     * Buscar restaurante por CNPJ (DTO)
     */
    @Transactional(readOnly = true)
    public Optional<RestauranteResponseDTO> buscarPorCnpjDTO(String cnpj) {
        return restauranteRepository.findByCnpj(cnpj).map(restauranteMapper::toResponse);
    }

    /**
     * Listar todos os restaurantes ativos (DTO)
     */
    @Transactional(readOnly = true)
    public List<RestauranteResponseDTO> listarAtivosDTO() {
        return restauranteRepository.findByAtivoTrue().stream().map(restauranteMapper::toResponse).collect(Collectors.toList());
    }

    /**
     * Atualizar dados do restaurante (DTO)
     */
    public RestauranteResponseDTO atualizarDTO(Long id, RestauranteRequestDTO dto) {
        Restaurante restauranteAtual = restauranteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Restaurante não encontrado: " + id));

        // Se CNPJ for alterado, checar único
        if (!restauranteAtual.getCnpj().equals(dto.getCnpj()) && restauranteRepository.existsByCnpj(dto.getCnpj())) {
            throw new IllegalArgumentException("CNPJ já cadastrado: " + dto.getCnpj());
        }

        Restaurante temp = restauranteMapper.toEntity(dto);
        restauranteAtual.setNome(temp.getNome());
        restauranteAtual.setCnpj(temp.getCnpj());
        restauranteAtual.setTelefone(temp.getTelefone());
        restauranteAtual.setEndereco(temp.getEndereco());

        Restaurante atualizado = restauranteRepository.save(restauranteAtual);
        return restauranteMapper.toResponse(atualizado);
    }

    /**
     * Inativar restaurante (soft delete)
     */
    public void inativar(Long id) {
        Restaurante restaurante = buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Restaurante não encontrado: " + id));

        restaurante.inativar();
        restauranteRepository.save(restaurante);
    }

    /**
     * Buscar restaurantes por nome (DTO)
     */
    @Transactional(readOnly = true)
    public List<RestauranteResponseDTO> buscarPorNomeDTO(String nome) {
        return restauranteRepository.findByNomeContainingIgnoreCase(nome).stream().map(restauranteMapper::toResponse).collect(Collectors.toList());
    }

    /**
     * Validações de negócio específicas para Restaurante
     */
    private void validarDadosRestaurante(Restaurante restaurante) {
        if (restaurante.getNome() == null || restaurante.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do restaurante é obrigatório");
        }

        if (restaurante.getCnpj() == null || restaurante.getCnpj().trim().isEmpty()) {
            throw new IllegalArgumentException("CNPJ é obrigatório");
        }

        if (restaurante.getNome().length() < 3) {
            throw new IllegalArgumentException("Nome do restaurante deve ter pelo menos 3 caracteres");
        }

        // Você pode adicionar validações específicas de CNPJ (formato e tamanho) aqui.
    }

    // Métodos compatíveis que retornam entidade (mantidos para compatibilidade interna)
    @Transactional(readOnly = true)
    public Optional<Restaurante> buscarPorId(Long id) {
        return restauranteRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<Restaurante> buscarPorCnpj(String cnpj) {
        return restauranteRepository.findByCnpj(cnpj);
    }

    @Transactional(readOnly = true)
    public List<Restaurante> listarAtivos() {
        return restauranteRepository.findByAtivoTrue();
    }

    @Transactional(readOnly = true)
    public List<Restaurante> buscarPorNome(String nome) {
        return restauranteRepository.findByNomeContainingIgnoreCase(nome);
    }

    public Restaurante cadastrar(Restaurante restaurante) {
        if (restauranteRepository.existsByCnpj(restaurante.getCnpj())) {
            throw new IllegalArgumentException("CNPJ já cadastrado: " + restaurante.getCnpj());
        }

        validarDadosRestaurante(restaurante);
        restaurante.setAtivo(true);
        return restauranteRepository.save(restaurante);
    }

    public Restaurante atualizar(Long id, Restaurante restauranteAtualizado) {
        Restaurante restaurante = buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Restaurante não encontrado: " + id));

        if (!restaurante.getCnpj().equals(restauranteAtualizado.getCnpj()) &&
                restauranteRepository.existsByCnpj(restauranteAtualizado.getCnpj())) {
            throw new IllegalArgumentException("CNPJ já cadastrado: " + restauranteAtualizado.getCnpj());
        }

        restaurante.setNome(restauranteAtualizado.getNome());
        restaurante.setCnpj(restauranteAtualizado.getCnpj());
        restaurante.setTelefone(restauranteAtualizado.getTelefone());
        restaurante.setEndereco(restauranteAtualizado.getEndereco());

        return restauranteRepository.save(restaurante);
    }
}