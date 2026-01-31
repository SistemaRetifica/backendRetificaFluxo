package com.sistema.fluxo.fabricante.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sistema.fluxo.exception.ResourceConflictException;
import com.sistema.fluxo.exception.ResourceNotFoundException;
import com.sistema.fluxo.fabricante.dto.FabricanteRequestDTO;
import com.sistema.fluxo.fabricante.dto.FabricanteResponseDTO;
import com.sistema.fluxo.fabricante.service.FabricanteService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SuppressWarnings("null")
@WebMvcTest(FabricanteController.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("FabricanteController - Testes de Integração")
class FabricanteControllerTest {

    private static final String BASE_URL = "/api/fabricantes";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private FabricanteService service;

    // ========================= LISTAGEM =========================

    @Nested
    @DisplayName("GET /api/fabricantes")
    class Listagem {

        @Test
        @DisplayName("Deve retornar 200 com lista de fabricantes")
        void listar_deveRetornar200ComLista() throws Exception {
            // Arrange
            List<FabricanteResponseDTO> lista = List.of(
                new FabricanteResponseDTO(1L, "MWM"),
                new FabricanteResponseDTO(2L, "Cummins")
            );
            when(service.findAll()).thenReturn(lista);

            // Act & Assert
            mockMvc.perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].nome").value("MWM"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].nome").value("Cummins"));
        }

        @Test
        @DisplayName("Deve retornar 200 com lista vazia quando não houver fabricantes")
        void listar_semRegistros_deveRetornar200ComListaVazia() throws Exception {
            // Arrange
            when(service.findAll()).thenReturn(List.of());

            // Act & Assert
            mockMvc.perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
        }
    }

    // ========================= BUSCA POR ID =========================

    @Nested
    @DisplayName("GET /api/fabricantes/{id}")
    class BuscaPorId {

        @Test
        @DisplayName("Deve retornar 200 com fabricante encontrado")
        void buscarPorId_existente_deveRetornar200() throws Exception {
            // Arrange
            Long id = 1L;
            FabricanteResponseDTO response = new FabricanteResponseDTO(id, "MWM");
            when(service.findById(id)).thenReturn(response);

            // Act & Assert
            mockMvc.perform(get(BASE_URL + "/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("MWM"));
        }

        @Test
        @DisplayName("Deve retornar 404 quando fabricante não encontrado")
        void buscarPorId_naoExistente_deveRetornar404() throws Exception {
            // Arrange
            Long id = 999L;
            when(service.findById(id))
                .thenThrow(new ResourceNotFoundException("Fabricante não encontrado com ID: " + id));

            // Act & Assert
            mockMvc.perform(get(BASE_URL + "/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Fabricante não encontrado com ID: " + id));
        }
    }

    // ========================= BUSCA POR NOME =========================

    @Nested
    @DisplayName("GET /api/fabricantes/nome/{nome}")
    class BuscaPorNome {

        @Test
        @DisplayName("Deve retornar 200 com fabricante encontrado")
        void buscarPorNome_existente_deveRetornar200() throws Exception {
            // Arrange
            String nome = "MWM";
            FabricanteResponseDTO response = new FabricanteResponseDTO(1L, nome);
            when(service.findByNome(nome)).thenReturn(response);

            // Act & Assert
            mockMvc.perform(get(BASE_URL + "/nome/{nome}", nome))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("MWM"));
        }

        @Test
        @DisplayName("Deve retornar 404 quando fabricante não encontrado por nome")
        void buscarPorNome_naoExistente_deveRetornar404() throws Exception {
            // Arrange
            String nome = "Inexistente";
            when(service.findByNome(nome))
                .thenThrow(new ResourceNotFoundException("Fabricante não encontrado com nome: " + nome));

            // Act & Assert
            mockMvc.perform(get(BASE_URL + "/nome/{nome}", nome))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Fabricante não encontrado com nome: " + nome));
        }
    }

    // ========================= CRIAÇÃO =========================

    @Nested
    @DisplayName("POST /api/fabricantes")
    class Criacao {

        @Test
        @DisplayName("Deve retornar 201 ao criar fabricante com sucesso")
        void criar_comDadosValidos_deveRetornar201() throws Exception {
            // Arrange
            FabricanteRequestDTO request = new FabricanteRequestDTO("MWM");
            FabricanteResponseDTO response = new FabricanteResponseDTO(1L, "MWM");
            when(service.save(any(FabricanteRequestDTO.class))).thenReturn(response);

            // Act & Assert
            mockMvc.perform(post(BASE_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("MWM"));
        }

        @Test
        @DisplayName("Deve retornar 409 ao tentar criar fabricante com nome duplicado")
        void criar_comNomeDuplicado_deveRetornar409() throws Exception {
            // Arrange
            FabricanteRequestDTO request = new FabricanteRequestDTO("MWM");
            when(service.save(any(FabricanteRequestDTO.class)))
                .thenThrow(new ResourceConflictException("Já existe um fabricante com o nome: MWM"));

            // Act & Assert
            mockMvc.perform(post(BASE_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.message").value("Já existe um fabricante com o nome: MWM"));
        }

        @ParameterizedTest(name = "{0}")
        @MethodSource("dadosInvalidosCriacao")
        @DisplayName("Deve retornar 400 para dados inválidos")
        void criar_comDadosInvalidos_deveRetornar400(String cenario, FabricanteRequestDTO request) throws Exception {
            // Act & Assert
            mockMvc.perform(post(BASE_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
        }

        private static Stream<Arguments> dadosInvalidosCriacao() {
            return Stream.of(
                Arguments.of("Nome nulo", new FabricanteRequestDTO(null)),
                Arguments.of("Nome vazio", new FabricanteRequestDTO("")),
                Arguments.of("Nome apenas espaços", new FabricanteRequestDTO("   ")),
                Arguments.of("Nome excede 100 caracteres", new FabricanteRequestDTO("A".repeat(101)))
            );
        }
    }

    // ========================= ATUALIZAÇÃO =========================

    @Nested
    @DisplayName("PUT /api/fabricantes/{id}")
    class Atualizacao {

        @Test
        @DisplayName("Deve retornar 200 ao atualizar fabricante com sucesso")
        void atualizar_comDadosValidos_deveRetornar200() throws Exception {
            // Arrange
            Long id = 1L;
            FabricanteRequestDTO request = new FabricanteRequestDTO("MWM Atualizado");
            FabricanteResponseDTO response = new FabricanteResponseDTO(id, "MWM Atualizado");
            when(service.update(eq(id), any(FabricanteRequestDTO.class))).thenReturn(response);

            // Act & Assert
            mockMvc.perform(put(BASE_URL + "/{id}", id)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("MWM Atualizado"));
        }

        @Test
        @DisplayName("Deve retornar 404 ao tentar atualizar fabricante inexistente")
        void atualizar_naoExistente_deveRetornar404() throws Exception {
            // Arrange
            Long id = 999L;
            FabricanteRequestDTO request = new FabricanteRequestDTO("MWM");
            when(service.update(eq(id), any(FabricanteRequestDTO.class)))
                .thenThrow(new ResourceNotFoundException("Fabricante não encontrado com ID: " + id));

            // Act & Assert
            mockMvc.perform(put(BASE_URL + "/{id}", id)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Fabricante não encontrado com ID: " + id));
        }

        @Test
        @DisplayName("Deve retornar 409 ao tentar atualizar para nome duplicado")
        void atualizar_comNomeDuplicado_deveRetornar409() throws Exception {
            // Arrange
            Long id = 1L;
            FabricanteRequestDTO request = new FabricanteRequestDTO("Cummins");
            when(service.update(eq(id), any(FabricanteRequestDTO.class)))
                .thenThrow(new ResourceConflictException("Já existe um fabricante com o nome: Cummins"));

            // Act & Assert
            mockMvc.perform(put(BASE_URL + "/{id}", id)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.message").value("Já existe um fabricante com o nome: Cummins"));
        }

        @ParameterizedTest(name = "{0}")
        @MethodSource("dadosInvalidosAtualizacao")
        @DisplayName("Deve retornar 400 para dados inválidos na atualização")
        void atualizar_comDadosInvalidos_deveRetornar400(String cenario, FabricanteRequestDTO request) throws Exception {
            // Arrange
            Long id = 1L;

            // Act & Assert
            mockMvc.perform(put(BASE_URL + "/{id}", id)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
        }

        private static Stream<Arguments> dadosInvalidosAtualizacao() {
            return Stream.of(
                Arguments.of("Nome nulo", new FabricanteRequestDTO(null)),
                Arguments.of("Nome vazio", new FabricanteRequestDTO("")),
                Arguments.of("Nome apenas espaços", new FabricanteRequestDTO("   ")),
                Arguments.of("Nome excede 100 caracteres", new FabricanteRequestDTO("A".repeat(101)))
            );
        }
    }

    // ========================= DELEÇÃO =========================

    @Nested
    @DisplayName("DELETE /api/fabricantes/{id}")
    class Delecao {

        @Test
        @DisplayName("Deve retornar 204 ao deletar fabricante com sucesso")
        void deletar_existente_deveRetornar204() throws Exception {
            // Arrange
            Long id = 1L;
            doNothing().when(service).delete(id);

            // Act & Assert
            mockMvc.perform(delete(BASE_URL + "/{id}", id))
                .andExpect(status().isNoContent());

            verify(service).delete(id);
        }

        @Test
        @DisplayName("Deve retornar 404 ao tentar deletar fabricante inexistente")
        void deletar_naoExistente_deveRetornar404() throws Exception {
            // Arrange
            Long id = 999L;
            doThrow(new ResourceNotFoundException("Fabricante não encontrado com ID: " + id))
                .when(service).delete(id);

            // Act & Assert
            mockMvc.perform(delete(BASE_URL + "/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Fabricante não encontrado com ID: " + id));
        }
    }
}
