package com.delivery_api.Projeto.Delivery.API.service;

import com.delivery_api.Projeto.Delivery.API.entity.Cliente;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    public Cliente cadastrar(Cliente clinte) {
        return new Cliente();
    }

    public List<Cliente> listarAtivos() {
        return new ArrayList<>();
    }

    public Optional<Cliente> buscarPorId(Long id) {
        return Optional.of(new Cliente());
    }

    public Cliente atualizar(Long id, Cliente cliente) {
        return new Cliente();
    }

    public void inativar(Long id) {
    }

    public List<Cliente> buscarPorNome(String nome) {
        return new ArrayList<>();
    }

    public Optional<Cliente> buscarPorEmail(String email) {
        return Optional.of(new Cliente());
    }
}
