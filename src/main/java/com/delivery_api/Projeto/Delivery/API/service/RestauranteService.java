package com.delivery_api.Projeto.Delivery.API.service;

import com.delivery_api.Projeto.Delivery.API.entity.Restaurante;
import com.delivery_api.Projeto.Delivery.API.repository.RestauranteRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

// O nome da classe foi alterado de ClienteService para RestauranteService
@Service
@Transactional
public class RestauranteService {

    // A injeção de dependência foi alterada para RestauranteRepository
    @Autowired
    private RestauranteRepository restauranteRepository;

    /**
     * Cadastrar novo restaurante
     */
    public Restaurante cadastrar(Restaurante restaurante) {
        // Validação de CNPJ único (pressupondo que Restaurante tem um CNPJ)
        if (restauranteRepository.existsByCnpj(restaurante.getCnpj())) {
            throw new IllegalArgumentException("CNPJ já cadastrado: " + restaurante.getCnpj());
        }

        // Validações de negócio
        validarDadosRestaurante(restaurante);

        // Definir como ativo por padrão
        restaurante.setAtivo(true);

        return restauranteRepository.save(restaurante);
    }

    /**
     * Buscar restaurante por ID
     */
    @Transactional(readOnly = true)
    public Optional<Restaurante> buscarPorId(Long id) {
        return restauranteRepository.findById(id);
    }

    /**
     * Buscar restaurante por CNPJ
     * (Alterado de buscarPorEmail para buscarPorCnpj, pois é um identificador mais comum para restaurantes)
     */
    @Transactional(readOnly = true)
    public Optional<Restaurante> buscarPorCnpj(String cnpj) {
        return restauranteRepository.findByCnpj(cnpj);
    }

    /**
     * Listar todos os restaurantes ativos
     */
    @Transactional(readOnly = true)
    public List<Restaurante> listarAtivos() {
        return restauranteRepository.findByAtivoTrue();
    }

    /**
     * Atualizar dados do restaurante
     */
    public Restaurante atualizar(Long id, Restaurante restauranteAtualizado) {
        Restaurante restaurante = buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Restaurante não encontrado: " + id));

        // Verificar se CNPJ não está sendo usado por outro restaurante
        if (!restaurante.getCnpj().equals(restauranteAtualizado.getCnpj()) &&
                restauranteRepository.existsByCnpj(restauranteAtualizado.getCnpj())) {
            throw new IllegalArgumentException("CNPJ já cadastrado: " + restauranteAtualizado.getCnpj());
        }

        // Atualizar campos
        // Assumindo que a entidade Restaurante possui os campos Nome, Cnpj e Endereco
        restaurante.setNome(restauranteAtualizado.getNome());
        restaurante.setCnpj(restauranteAtualizado.getCnpj());
        restaurante.setTelefone(restauranteAtualizado.getTelefone());
        restaurante.setEndereco(restauranteAtualizado.getEndereco());

        return restauranteRepository.save(restaurante);
    }

    /**
     * Inativar restaurante (soft delete)
     */
    public void inativar(Long id) {
        Restaurante restaurante = buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Restaurante não encontrado: " + id));

        // Note: Assumindo que a entidade Restaurante tem o método inativar()
        restaurante.inativar();
        restauranteRepository.save(restaurante);
    }

    /**
     * Buscar restaurantes por nome
     */
    @Transactional(readOnly = true)
    public List<Restaurante> buscarPorNome(String nome) {
        return restauranteRepository.findByNomeContainingIgnoreCase(nome);
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
}