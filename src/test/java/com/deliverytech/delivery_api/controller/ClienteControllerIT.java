package com.deliverytech.delivery_api.controller;

import com.deliverytech.delivery_api.dto.request.ClienteRequestDTO;
import com.deliverytech.delivery_api.entity.Cliente;
import com.deliverytech.delivery_api.entity.Usuario;
import com.deliverytech.delivery_api.repository.ClienteRepository;
import com.deliverytech.delivery_api.repository.UsuarioRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.crypto.password.PasswordEncoder;


import java.time.LocalDateTime;

import static com.deliverytech.delivery_api.enums.Role.ADMIN;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import(TestDataConfiguration.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@DisplayName("Testes de Integração do ClienteController")
class ClienteControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    Cliente novoCliente = new Cliente();

    @BeforeEach
    void setupAdminUser() {
        if (usuarioRepository.findByEmail("admin@delivery.com").isEmpty()) {
            Usuario admin = new Usuario();
            admin.setNome("Admin");
            admin.setEmail("admin@delivery.com");
            admin.setSenha(passwordEncoder.encode("123456"));
            admin.setDataCriacao(LocalDateTime.now());
            admin.setRole(ADMIN);
            usuarioRepository.save(admin);
        }
        // criar cliente

        novoCliente.setNome("Maria Silva");
        novoCliente.setEmail("maria.teste@email.com");
        novoCliente.setTelefone("+5511888888888");
        novoCliente.setEndereco("Rua das Flores, 123");
        novoCliente.setAtivo(true);
        novoCliente.setDataCadastro(LocalDateTime.now());
        novoCliente = clienteRepository.save(novoCliente);
    }

    public String getToken() throws Exception {
        // Simula o login do usuário para obter o token JWT
        String loginJson = """
                {
                    "email": "admin@delivery.com",
                    "senha": "123456"
                }
                """;

        String resposta = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String token = objectMapper.readTree(resposta).get("token").asText();

        return token;
    }

    @Test
    @DisplayName("Deve criar cliente com dados válidos")
    void should_CreateCliente_When_ValidData() throws Exception {
        // Given
        ClienteRequestDTO cliente = new ClienteRequestDTO();
        cliente.setNome("Jose Silva");
        cliente.setEmail("jose.teste@email.com");
        cliente.setTelefone("+551188888899");
        cliente.setEndereco("Rua das Flores, 22");


        // When & Then
        mockMvc.perform(post("/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + getToken())
                        .content(objectMapper.writeValueAsString(cliente)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome", is("Jose Silva")))
                .andExpect(jsonPath("$.email", is("jose.teste@email.com")))
                .andExpect(jsonPath("$.telefone", is("+551188888899")))
                .andExpect(jsonPath("$.endereco", is("Rua das Flores, 22")))
                .andExpect(jsonPath("$.id", notNullValue()));
    }

    @Test
    @DisplayName("Deve retornar erro 400 quando dados inválidos")
    void should_ReturnBadRequest_When_InvalidData() throws Exception {
        // Given
        Cliente clienteInvalido = new Cliente();
        clienteInvalido.setNome(""); // Nome vazio
        clienteInvalido.setEmail("email-invalido"); // Email inválido

        // When & Then
        mockMvc.perform(post("/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + getToken())
                        .content(objectMapper.writeValueAsString(clienteInvalido)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.timestamp", notNullValue()))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.error", is("Dados inválidos")))
                .andExpect(jsonPath("$.message", is("Erro de validação nos dados enviados")))
                .andExpect(jsonPath("$.path", is("/clientes")))
                .andExpect(jsonPath("$.errorCode", is("VALIDATION_ERROR")))
                .andExpect(jsonPath("$.details.nome", is("O nome é obrigatório")))
                .andExpect(jsonPath("$.details.email", is("O email deve ser válido")));
    }

    @Test
    @DisplayName("Deve buscar cliente por ID existente")
    void should_ReturnCliente_When_IdExists() throws Exception {
        // Given
        Cliente cliente = clienteRepository.findAll().get(0);

        // When & Then
        mockMvc.perform(get("/clientes/{id}", cliente.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + getToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(cliente.getId().intValue())))
                .andExpect(jsonPath("$.nome", is(cliente.getNome())))
                .andExpect(jsonPath("$.email", is(cliente.getEmail())));
    }

    @Test
    @DisplayName("Deve retornar 404 quando cliente não existe")
    void should_ReturnNotFound_When_ClienteNotExists() throws Exception {
        // When & Then
        mockMvc.perform(get("/clientes/{id}", 999L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + getToken()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.timestamp", notNullValue()))
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.error", is("Não encontrado")))
                .andExpect(jsonPath("$.message", containsString("Cliente não encontrado")))
                .andExpect(jsonPath("$.path", is("/clientes/999")));
    }

    @Test
    @DisplayName("Deve atualizar cliente existente")
    void should_UpdateCliente_When_ClienteExists() throws Exception {
        // Given
        Cliente cliente = clienteRepository.findAll().get(1);
        cliente.setNome("Nome Atualizado");
        cliente.setTelefone("11777777777");


        // When & Then
        mockMvc.perform(put("/clientes/{id}", cliente.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + getToken())
                        .content(objectMapper.writeValueAsString(cliente)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome", is("Nome Atualizado")))
                .andExpect(jsonPath("$.telefone", is("11777777777")));
    }
}
