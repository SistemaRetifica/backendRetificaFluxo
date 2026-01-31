package com.sistema.fluxo.classificacao.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sistema.fluxo.classificacao.dto.ClassificacaoRequestDTO;
import com.sistema.fluxo.classificacao.service.ClassificacaoService;
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

import java.util.Objects;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Testes de validação Bean Validation para ClassificacaoController.
 * Utiliza @WebMvcTest para ativar validação automática.
 */
@SuppressWarnings("null")
@WebMvcTest(ClassificacaoController.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("ClassificacaoController - Testes de Validação")
class ClassificacaoControllerValidationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ClassificacaoService service;

    // ========== CONSTANTES PARA TESTES ==========
    private static final String BASE_URL = "/api/classificacoes"; // CORRIGIDO
    private static final long ID_VALIDO = 1L;
    private static final String NOME_VALIDO = "A";
    private static final String DESCRICAO_VALIDA = "Modelo de motor tipo A";

    // ========== MÉTODOS AUXILIARES ==========

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

    // ========== TESTES DE VALIDAÇÃO NO POST ==========
    @Nested
    @DisplayName("POST /api/classificacoes - Validação de Campos")
    class PostValidationTest {

        @Test
        @DisplayName("Deve retornar 400 quando nome está vazio")
        void deveRetornar400QuandoNomeVazio() throws Exception {
            // Arrange
            ClassificacaoRequestDTO requestInvalido = criarRequestDTO("", DESCRICAO_VALIDA);

            // Act & Assert
            mockMvc.perform(post(BASE_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(toJson(requestInvalido)))
                .andExpect(status().isBadRequest());

            verify(service, never()).save(any(ClassificacaoRequestDTO.class));
        }

        @Test
        @DisplayName("Deve retornar 400 quando nome é nulo")
        void deveRetornar400QuandoNomeNulo() throws Exception {
            // Arrange
            ClassificacaoRequestDTO requestInvalido = criarRequestDTO(null, DESCRICAO_VALIDA);

            // Act & Assert
            mockMvc.perform(post(BASE_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(toJson(requestInvalido)))
                .andExpect(status().isBadRequest());

            verify(service, never()).save(any(ClassificacaoRequestDTO.class));
        }

        @Test
        @DisplayName("Deve retornar 400 quando nome contém apenas espaços")
        void deveRetornar400QuandoNomeApenasEspacos() throws Exception {
            // Arrange
            ClassificacaoRequestDTO requestInvalido = criarRequestDTO("   ", DESCRICAO_VALIDA);

            // Act & Assert
            mockMvc.perform(post(BASE_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(toJson(requestInvalido)))
                .andExpect(status().isBadRequest());

            verify(service, never()).save(any(ClassificacaoRequestDTO.class));
        }

        @Test
        @DisplayName("Deve retornar 400 quando nome excede tamanho máximo")
        void deveRetornar400QuandoNomeExcedeTamanho() throws Exception {
            // Arrange
            String nomeGrande = "A".repeat(11);
            ClassificacaoRequestDTO requestInvalido = criarRequestDTO(nomeGrande, DESCRICAO_VALIDA);

            // Act & Assert
            mockMvc.perform(post(BASE_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(toJson(requestInvalido)))
                .andExpect(status().isBadRequest());

            verify(service, never()).save(any(ClassificacaoRequestDTO.class));
        }

        @Test
        @DisplayName("Deve retornar 400 quando descrição excede tamanho máximo")
        void deveRetornar400QuandoDescricaoExcedeTamanho() throws Exception {
            // Arrange
            String descricaoGrande = "A".repeat(201);
            ClassificacaoRequestDTO requestInvalido = criarRequestDTO(NOME_VALIDO, descricaoGrande);

            // Act & Assert
            mockMvc.perform(post(BASE_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(toJson(requestInvalido)))
                .andExpect(status().isBadRequest());

            verify(service, never()).save(any(ClassificacaoRequestDTO.class));
        }
    }

    // ========== TESTES DE VALIDAÇÃO NO PUT ==========
    @Nested
    @DisplayName("PUT /api/classificacoes/{id} - Validação de Campos")
    class PutValidationTest {

        @Test
        @DisplayName("Deve retornar 400 quando nome está vazio")
        void deveRetornar400QuandoNomeVazio() throws Exception {
            // Arrange
            ClassificacaoRequestDTO requestInvalido = criarRequestDTO("", DESCRICAO_VALIDA);

            // Act & Assert
            mockMvc.perform(put(BASE_URL + "/{id}", ID_VALIDO)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(toJson(requestInvalido)))
                .andExpect(status().isBadRequest());

            verify(service, never()).update(any(), any(ClassificacaoRequestDTO.class));
        }

        @Test
        @DisplayName("Deve retornar 400 quando nome é nulo")
        void deveRetornar400QuandoNomeNulo() throws Exception {
            // Arrange
            ClassificacaoRequestDTO requestInvalido = criarRequestDTO(null, DESCRICAO_VALIDA);

            // Act & Assert
            mockMvc.perform(put(BASE_URL + "/{id}", ID_VALIDO)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(toJson(requestInvalido)))
                .andExpect(status().isBadRequest());

            verify(service, never()).update(any(), any(ClassificacaoRequestDTO.class));
        }

        @Test
        @DisplayName("Deve retornar 400 quando nome excede tamanho máximo")
        void deveRetornar400QuandoNomeExcedeTamanho() throws Exception {
            // Arrange
            String nomeGrande = "A".repeat(11);
            ClassificacaoRequestDTO requestInvalido = criarRequestDTO(nomeGrande, DESCRICAO_VALIDA);

            // Act & Assert
            mockMvc.perform(put(BASE_URL + "/{id}", ID_VALIDO)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(toJson(requestInvalido)))
                .andExpect(status().isBadRequest());

            verify(service, never()).update(any(), any(ClassificacaoRequestDTO.class));
        }

        @Test
        @DisplayName("Deve retornar 400 quando descrição excede tamanho máximo")
        void deveRetornar400QuandoDescricaoExcedeTamanho() throws Exception {
            // Arrange
            String descricaoGrande = "A".repeat(201);
            ClassificacaoRequestDTO requestInvalido = criarRequestDTO(NOME_VALIDO, descricaoGrande);

            // Act & Assert
            mockMvc.perform(put(BASE_URL + "/{id}", ID_VALIDO)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(toJson(requestInvalido)))
                .andExpect(status().isBadRequest());

            verify(service, never()).update(any(), any(ClassificacaoRequestDTO.class));
        }
    }
}
