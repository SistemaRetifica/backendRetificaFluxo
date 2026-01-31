package com.sistema.fluxo.classificacao.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sistema.fluxo.classificacao.dto.ClassificacaoRequestDTO;
import com.sistema.fluxo.classificacao.dto.ClassificacaoResponseDTO;
import com.sistema.fluxo.classificacao.service.ClassificacaoService;
import com.sistema.fluxo.exception.ResourceConflictException;
import com.sistema.fluxo.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Testes unitários para ClassificacaoController.
 * Utiliza @WebMvcTest para carregar o contexto do controller.
 */
@SuppressWarnings("null")
@WebMvcTest(ClassificacaoController.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("ClassificacaoController - Testes Unitários")
class ClassificacaoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ClassificacaoService service;

    // ========== CONSTANTES PARA TESTES ==========
    private static final String BASE_URL = "/api/classificacoes";
    private static final long ID_VALIDO = 1L;
    private static final long ID_INEXISTENTE = 999L;
    private static final String NOME_VALIDO = "A";
    private static final String NOME_ALTERNATIVO = "B";
    private static final String DESCRICAO_VALIDA = "Modelo de motor tipo A";
    private static final String DESCRICAO_ALTERNATIVA = "Modelo de motor tipo B";

    // ========== OBJETOS REUTILIZÁVEIS ==========
    private ClassificacaoResponseDTO responseDTO;
    private ClassificacaoRequestDTO requestDTO;

    // ========== MÉTODOS AUXILIARES ==========

    @NonNull
    private ClassificacaoResponseDTO criarResponseDTO(long id, String nome, String descricao) {
        return new ClassificacaoResponseDTO(id, nome, descricao);
    }

    @NonNull
    private ClassificacaoRequestDTO criarRequestDTO(String nome, String descricao) {
        return new ClassificacaoRequestDTO(nome, descricao);
    }

    @NonNull
    private String toJson(@NonNull Object obj) throws Exception {
        return Objects.requireNonNull(
            objectMapper.writeValueAsString(obj),
            "Falha ao serializar objeto para JSON"
        );
    }

    @BeforeEach
    void setUp() {
        responseDTO = criarResponseDTO(ID_VALIDO, NOME_VALIDO, DESCRICAO_VALIDA);
        requestDTO = criarRequestDTO(NOME_VALIDO, DESCRICAO_VALIDA);
    }

    // ========== TESTES DE LISTAGEM (GET /api/classificacoes) ==========
    @Nested
    @DisplayName("GET /api/classificacoes - Listagem")
    class ListagemTest {

        @Test
        @DisplayName("Deve listar todas as classificações - 200 OK")
        void deveListarTodasClassificacoes() throws Exception {
            // Arrange
            ClassificacaoResponseDTO response1 = criarResponseDTO(ID_VALIDO, NOME_VALIDO, DESCRICAO_VALIDA);
            ClassificacaoResponseDTO response2 = criarResponseDTO(2L, NOME_ALTERNATIVO, DESCRICAO_ALTERNATIVA);
            when(service.findAll()).thenReturn(List.of(response1, response2));

            // Act & Assert
            mockMvc.perform(get(BASE_URL)
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].nome", is(NOME_VALIDO)))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].nome", is(NOME_ALTERNATIVO)));

            verify(service, times(1)).findAll();
        }

        @Test
        @DisplayName("Deve retornar lista vazia - 200 OK")
        void deveRetornarListaVazia() throws Exception {
            // Arrange
            when(service.findAll()).thenReturn(Collections.emptyList());

            // Act & Assert
            mockMvc.perform(get(BASE_URL)
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));

            verify(service, times(1)).findAll();
        }
    }

    // ========== TESTES DE BUSCA POR ID (GET /api/classificacoes/{id}) ==========
    @Nested
    @DisplayName("GET /api/classificacoes/{id} - Busca por ID")
    class BuscaPorIdTest {

        @Test
        @DisplayName("Deve buscar classificação por ID - 200 OK")
        void deveBuscarClassificacaoPorId() throws Exception {
            // Arrange
            when(service.findById(ID_VALIDO)).thenReturn(responseDTO);

            // Act & Assert
            mockMvc.perform(get(BASE_URL + "/{id}", ID_VALIDO)
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.nome", is(NOME_VALIDO)))
                .andExpect(jsonPath("$.descricao", is(DESCRICAO_VALIDA)));

            verify(service, times(1)).findById(ID_VALIDO);
        }

        @Test
        @DisplayName("Deve retornar 404 quando ID não existe")
        void deveRetornar404QuandoIdNaoExiste() throws Exception {
            // Arrange
            when(service.findById(ID_INEXISTENTE))
                .thenThrow(new ResourceNotFoundException("Classificação não encontrada com ID: " + ID_INEXISTENTE));

            // Act & Assert
            mockMvc.perform(get(BASE_URL + "/{id}", ID_INEXISTENTE)
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.message", containsString("não encontrada")));

            verify(service, times(1)).findById(ID_INEXISTENTE);
        }
    }

    // ========== TESTES DE BUSCA POR NOME (GET /api/classificacoes/nome/{nome}) ==========
    @Nested
    @DisplayName("GET /api/classificacoes/nome/{nome} - Busca por Nome")
    class BuscaPorNomeTest {

        @Test
        @DisplayName("Deve buscar classificação por nome - 200 OK")
        void deveBuscarClassificacaoPorNome() throws Exception {
            // Arrange
            when(service.findByNome(NOME_VALIDO)).thenReturn(responseDTO);

            // Act & Assert
            mockMvc.perform(get(BASE_URL + "/nome/{nome}", NOME_VALIDO)
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.nome", is(NOME_VALIDO)));

            verify(service, times(1)).findByNome(NOME_VALIDO);
        }

        @Test
        @DisplayName("Deve retornar 404 quando nome não existe")
        void deveRetornar404QuandoNomeNaoExiste() throws Exception {
            // Arrange
            String nomeInexistente = "INEXISTENTE";
            when(service.findByNome(nomeInexistente))
                .thenThrow(new ResourceNotFoundException("Classificação não encontrada com nome: " + nomeInexistente));

            // Act & Assert
            mockMvc.perform(get(BASE_URL + "/nome/{nome}", nomeInexistente)
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.message", containsString("não encontrada")));

            verify(service, times(1)).findByNome(nomeInexistente);
        }
    }

    // ========== TESTES DE CRIAÇÃO (POST /api/classificacoes) ==========
    @Nested
    @DisplayName("POST /api/classificacoes - Criação")
    class CriacaoTest {

        @Test
        @DisplayName("Deve criar nova classificação - 201 Created")
        void deveCriarNovaClassificacao() throws Exception {
            // Arrange
            when(service.save(any(ClassificacaoRequestDTO.class))).thenReturn(responseDTO);

            // Act & Assert
            mockMvc.perform(post(BASE_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(toJson(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.nome", is(NOME_VALIDO)))
                .andExpect(jsonPath("$.descricao", is(DESCRICAO_VALIDA)));

            verify(service, times(1)).save(any(ClassificacaoRequestDTO.class));
        }

        @Test
        @DisplayName("Deve retornar 409 quando nome já existe")
        void deveRetornar409QuandoNomeDuplicado() throws Exception {
            // Arrange
            when(service.save(any(ClassificacaoRequestDTO.class)))
                .thenThrow(new ResourceConflictException("Já existe uma classificação com o nome: " + NOME_VALIDO));

            // Act & Assert
            mockMvc.perform(post(BASE_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(toJson(requestDTO)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status", is(409)))
                .andExpect(jsonPath("$.message", containsString("Já existe")));

            verify(service, times(1)).save(any(ClassificacaoRequestDTO.class));
        }
    }

    // ========== TESTES DE ATUALIZAÇÃO (PUT /api/classificacoes/{id}) ==========
    @Nested
    @DisplayName("PUT /api/classificacoes/{id} - Atualização")
    class AtualizacaoTest {

        @Test
        @DisplayName("Deve atualizar classificação - 200 OK")
        void deveAtualizarClassificacao() throws Exception {
            // Arrange
            ClassificacaoRequestDTO updateDTO = criarRequestDTO(NOME_ALTERNATIVO, DESCRICAO_ALTERNATIVA);
            ClassificacaoResponseDTO updatedResponse = criarResponseDTO(ID_VALIDO, NOME_ALTERNATIVO, DESCRICAO_ALTERNATIVA);
            when(service.update(anyLong(), any(ClassificacaoRequestDTO.class))).thenReturn(updatedResponse);

            // Act & Assert
            mockMvc.perform(put(BASE_URL + "/{id}", ID_VALIDO)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(toJson(updateDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.nome", is(NOME_ALTERNATIVO)))
                .andExpect(jsonPath("$.descricao", is(DESCRICAO_ALTERNATIVA)));

            verify(service, times(1)).update(anyLong(), any(ClassificacaoRequestDTO.class));
        }

        @Test
        @DisplayName("Deve retornar 404 ao atualizar ID inexistente")
        void deveRetornar404AoAtualizarIdInexistente() throws Exception {
            // Arrange
            when(service.update(anyLong(), any(ClassificacaoRequestDTO.class)))
                .thenThrow(new ResourceNotFoundException("Classificação não encontrada com ID: " + ID_INEXISTENTE));

            // Act & Assert
            mockMvc.perform(put(BASE_URL + "/{id}", ID_INEXISTENTE)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(toJson(requestDTO)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.message", containsString("não encontrada")));

            verify(service, times(1)).update(anyLong(), any(ClassificacaoRequestDTO.class));
        }

        @Test
        @DisplayName("Deve retornar 409 ao atualizar para nome duplicado")
        void deveRetornar409AoAtualizarParaNomeDuplicado() throws Exception {
            // Arrange
            when(service.update(anyLong(), any(ClassificacaoRequestDTO.class)))
                .thenThrow(new ResourceConflictException("Já existe uma classificação com o nome: " + NOME_VALIDO));

            // Act & Assert
            mockMvc.perform(put(BASE_URL + "/{id}", ID_VALIDO)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(toJson(requestDTO)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status", is(409)))
                .andExpect(jsonPath("$.message", containsString("Já existe")));

            verify(service, times(1)).update(anyLong(), any(ClassificacaoRequestDTO.class));
        }
    }

    // ========== TESTES DE DELEÇÃO (DELETE /api/classificacoes/{id}) ==========
    @Nested
    @DisplayName("DELETE /api/classificacoes/{id} - Deleção")
    class DelecaoTest {

        @Test
        @DisplayName("Deve deletar classificação - 204 No Content")
        void deveDeletarClassificacao() throws Exception {
            // Arrange
            doNothing().when(service).delete(ID_VALIDO);

            // Act & Assert
            mockMvc.perform(delete(BASE_URL + "/{id}", ID_VALIDO)
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

            verify(service, times(1)).delete(ID_VALIDO);
        }

        @Test
        @DisplayName("Deve retornar 404 ao deletar ID inexistente")
        void deveRetornar404AoDeletarIdInexistente() throws Exception {
            // Arrange
            doThrow(new ResourceNotFoundException("Classificação não encontrada com ID: " + ID_INEXISTENTE))
                .when(service).delete(ID_INEXISTENTE);

            // Act & Assert
            mockMvc.perform(delete(BASE_URL + "/{id}", ID_INEXISTENTE)
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.message", containsString("não encontrada")));

            verify(service, times(1)).delete(ID_INEXISTENTE);
        }
    }
}
