package com.sistema.fluxo.modelo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sistema.fluxo.classificacao.dto.ClassificacaoResponseDTO;
import com.sistema.fluxo.exception.ResourceConflictException;
import com.sistema.fluxo.exception.ResourceNotFoundException;
import com.sistema.fluxo.fabricante.dto.FabricanteResponseDTO;
import com.sistema.fluxo.modelo.dto.ModeloRequestDTO;
import com.sistema.fluxo.modelo.dto.ModeloResponseDTO;
import com.sistema.fluxo.modelo.service.ModeloService;
import org.junit.jupiter.api.BeforeEach;
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
@WebMvcTest(ModeloController.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("ModeloController - Testes de Integração")
class ModeloControllerTest {

    private static final String BASE_URL = "/api/modelos";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ModeloService service;

    private ModeloResponseDTO modeloResponse;
    private FabricanteResponseDTO fabricanteResponse;
    private ClassificacaoResponseDTO classificacaoResponse;

    @BeforeEach
    void setUp() {
        fabricanteResponse = new FabricanteResponseDTO(1L, "MWM");
        classificacaoResponse = new ClassificacaoResponseDTO(1L, "A", "Classificação A");
        modeloResponse = new ModeloResponseDTO(1L, "D229", fabricanteResponse, classificacaoResponse, "Motor diesel");
    }

    // ========================= LISTAGEM =========================

    @Nested
    @DisplayName("GET /api/modelos")
    class Listagem {

        @Test
        @DisplayName("Deve retornar 200 com lista de modelos")
        void listar_deveRetornar200ComLista() throws Exception {
            // Arrange
            ModeloResponseDTO modelo2 = new ModeloResponseDTO(2L, "TD229", fabricanteResponse, classificacaoResponse, null);
            when(service.findAll()).thenReturn(List.of(modeloResponse, modelo2));

            // Act & Assert
            mockMvc.perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].nome").value("D229"))
                .andExpect(jsonPath("$[0].fabricante.nome").value("MWM"))
                .andExpect(jsonPath("$[0].classificacao.nome").value("A"))
                .andExpect(jsonPath("$[1].nome").value("TD229"));
        }

        @Test
        @DisplayName("Deve retornar 200 com lista vazia")
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
    @DisplayName("GET /api/modelos/{id}")
    class BuscaPorId {

        @Test
        @DisplayName("Deve retornar 200 com modelo encontrado")
        void buscarPorId_existente_deveRetornar200() throws Exception {
            // Arrange
            when(service.findById(1L)).thenReturn(modeloResponse);

            // Act & Assert
            mockMvc.perform(get(BASE_URL + "/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("D229"))
                .andExpect(jsonPath("$.fabricante.id").value(1))
                .andExpect(jsonPath("$.fabricante.nome").value("MWM"))
                .andExpect(jsonPath("$.classificacao.id").value(1))
                .andExpect(jsonPath("$.classificacao.nome").value("A"));
        }

        @Test
        @DisplayName("Deve retornar 404 quando modelo não encontrado")
        void buscarPorId_naoExistente_deveRetornar404() throws Exception {
            // Arrange
            when(service.findById(999L))
                .thenThrow(new ResourceNotFoundException("Modelo não encontrado com ID: 999"));

            // Act & Assert
            mockMvc.perform(get(BASE_URL + "/{id}", 999L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Modelo não encontrado com ID: 999"));
        }
    }

    // ========================= BUSCA POR NOME =========================

    @Nested
    @DisplayName("GET /api/modelos/nome/{nome}")
    class BuscaPorNome {

        @Test
        @DisplayName("Deve retornar 200 com modelos encontrados")
        void buscarPorNome_existente_deveRetornar200() throws Exception {
            // Arrange
            when(service.findByNome("D229")).thenReturn(List.of(modeloResponse));

            // Act & Assert
            mockMvc.perform(get(BASE_URL + "/nome/{nome}", "D229"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].nome").value("D229"));
        }

        @Test
        @DisplayName("Deve retornar 200 com lista vazia quando não encontrar")
        void buscarPorNome_naoExistente_deveRetornar200ComListaVazia() throws Exception {
            // Arrange
            when(service.findByNome("XYZ")).thenReturn(List.of());

            // Act & Assert
            mockMvc.perform(get(BASE_URL + "/nome/{nome}", "XYZ"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
        }
    }

    // ========================= BUSCA POR FABRICANTE =========================

    @Nested
    @DisplayName("GET /api/modelos/fabricante/{fabricanteId}")
    class BuscaPorFabricante {

        @Test
        @DisplayName("Deve retornar 200 com modelos do fabricante")
        void buscarPorFabricante_existente_deveRetornar200() throws Exception {
            // Arrange
            when(service.findByFabricante(1L)).thenReturn(List.of(modeloResponse));

            // Act & Assert
            mockMvc.perform(get(BASE_URL + "/fabricante/{fabricanteId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].fabricante.id").value(1));
        }

        @Test
        @DisplayName("Deve retornar 404 quando fabricante não existe")
        void buscarPorFabricante_naoExistente_deveRetornar404() throws Exception {
            // Arrange
            when(service.findByFabricante(999L))
                .thenThrow(new ResourceNotFoundException("Fabricante não encontrado com ID: 999"));

            // Act & Assert
            mockMvc.perform(get(BASE_URL + "/fabricante/{fabricanteId}", 999L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Fabricante não encontrado com ID: 999"));
        }
    }

    // ========================= BUSCA POR CLASSIFICAÇÃO =========================

    @Nested
    @DisplayName("GET /api/modelos/classificacao/{classificacaoId}")
    class BuscaPorClassificacao {

        @Test
        @DisplayName("Deve retornar 200 com modelos da classificação")
        void buscarPorClassificacao_existente_deveRetornar200() throws Exception {
            // Arrange
            when(service.findByClassificacao(1L)).thenReturn(List.of(modeloResponse));

            // Act & Assert
            mockMvc.perform(get(BASE_URL + "/classificacao/{classificacaoId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].classificacao.id").value(1));
        }

        @Test
        @DisplayName("Deve retornar 404 quando classificação não existe")
        void buscarPorClassificacao_naoExistente_deveRetornar404() throws Exception {
            // Arrange
            when(service.findByClassificacao(999L))
                .thenThrow(new ResourceNotFoundException("Classificação não encontrada com ID: 999"));

            // Act & Assert
            mockMvc.perform(get(BASE_URL + "/classificacao/{classificacaoId}", 999L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Classificação não encontrada com ID: 999"));
        }
    }

    // ========================= BUSCA POR FILTRO =========================

    @Nested
    @DisplayName("GET /api/modelos/filtro")
    class BuscaPorFiltro {

        @Test
        @DisplayName("Deve retornar 200 com modelos filtrados")
        void buscarPorFiltro_existente_deveRetornar200() throws Exception {
            // Arrange
            when(service.findByFabricanteAndClassificacao(1L, 1L)).thenReturn(List.of(modeloResponse));

            // Act & Assert
            mockMvc.perform(get(BASE_URL + "/filtro")
                    .param("fabricanteId", "1")
                    .param("classificacaoId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].fabricante.id").value(1))
                .andExpect(jsonPath("$[0].classificacao.id").value(1));
        }

        @Test
        @DisplayName("Deve retornar 404 quando fabricante não existe no filtro")
        void buscarPorFiltro_fabricanteNaoExiste_deveRetornar404() throws Exception {
            // Arrange
            when(service.findByFabricanteAndClassificacao(999L, 1L))
                .thenThrow(new ResourceNotFoundException("Fabricante não encontrado com ID: 999"));

            // Act & Assert
            mockMvc.perform(get(BASE_URL + "/filtro")
                    .param("fabricanteId", "999")
                    .param("classificacaoId", "1"))
                .andExpect(status().isNotFound());
        }
    }

    // ========================= CRIAÇÃO =========================

    @Nested
    @DisplayName("POST /api/modelos")
    class Criacao {

        @Test
        @DisplayName("Deve retornar 201 ao criar modelo com sucesso")
        void criar_comDadosValidos_deveRetornar201() throws Exception {
            // Arrange
            ModeloRequestDTO request = new ModeloRequestDTO("D229", 1L, 1L, "Motor diesel");
            when(service.save(any(ModeloRequestDTO.class))).thenReturn(modeloResponse);

            // Act & Assert
            mockMvc.perform(post(BASE_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("D229"))
                .andExpect(jsonPath("$.fabricante.nome").value("MWM"))
                .andExpect(jsonPath("$.classificacao.nome").value("A"));
        }

        @Test
        @DisplayName("Deve retornar 404 quando fabricante não existe")
        void criar_fabricanteNaoExiste_deveRetornar404() throws Exception {
            // Arrange
            ModeloRequestDTO request = new ModeloRequestDTO("D229", 999L, 1L, null);
            when(service.save(any(ModeloRequestDTO.class)))
                .thenThrow(new ResourceNotFoundException("Fabricante não encontrado com ID: 999"));

            // Act & Assert
            mockMvc.perform(post(BASE_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Fabricante não encontrado com ID: 999"));
        }

        @Test
        @DisplayName("Deve retornar 404 quando classificação não existe")
        void criar_classificacaoNaoExiste_deveRetornar404() throws Exception {
            // Arrange
            ModeloRequestDTO request = new ModeloRequestDTO("D229", 1L, 999L, null);
            when(service.save(any(ModeloRequestDTO.class)))
                .thenThrow(new ResourceNotFoundException("Classificação não encontrada com ID: 999"));

            // Act & Assert
            mockMvc.perform(post(BASE_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Classificação não encontrada com ID: 999"));
        }

        @Test
        @DisplayName("Deve retornar 409 ao criar modelo com nome duplicado para o fabricante")
        void criar_nomeDuplicado_deveRetornar409() throws Exception {
            // Arrange
            ModeloRequestDTO request = new ModeloRequestDTO("D229", 1L, 1L, null);
            when(service.save(any(ModeloRequestDTO.class)))
                .thenThrow(new ResourceConflictException("Já existe um modelo com o nome 'D229' para o fabricante MWM"));

            // Act & Assert
            mockMvc.perform(post(BASE_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.message").value("Já existe um modelo com o nome 'D229' para o fabricante MWM"));
        }

        @ParameterizedTest(name = "{0}")
        @MethodSource("dadosInvalidosCriacao")
        @DisplayName("Deve retornar 400 para dados inválidos")
        void criar_comDadosInvalidos_deveRetornar400(String cenario, ModeloRequestDTO request) throws Exception {
            // Act & Assert
            mockMvc.perform(post(BASE_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
        }

        private static Stream<Arguments> dadosInvalidosCriacao() {
            return Stream.of(
                Arguments.of("Nome nulo", new ModeloRequestDTO(null, 1L, 1L, null)),
                Arguments.of("Nome vazio", new ModeloRequestDTO("", 1L, 1L, null)),
                Arguments.of("Nome apenas espaços", new ModeloRequestDTO("   ", 1L, 1L, null)),
                Arguments.of("Nome excede 100 caracteres", new ModeloRequestDTO("A".repeat(101), 1L, 1L, null)),
                Arguments.of("FabricanteId nulo", new ModeloRequestDTO("D229", null, 1L, null)),
                Arguments.of("ClassificacaoId nulo", new ModeloRequestDTO("D229", 1L, null, null)),
                Arguments.of("Observações excede 500 caracteres", new ModeloRequestDTO("D229", 1L, 1L, "A".repeat(501)))
            );
        }
    }

    // ========================= ATUALIZAÇÃO =========================

    @Nested
    @DisplayName("PUT /api/modelos/{id}")
    class Atualizacao {

        @Test
        @DisplayName("Deve retornar 200 ao atualizar modelo com sucesso")
        void atualizar_comDadosValidos_deveRetornar200() throws Exception {
            // Arrange
            ModeloRequestDTO request = new ModeloRequestDTO("TD229", 1L, 1L, "Motor turbo diesel");
            ModeloResponseDTO updated = new ModeloResponseDTO(1L, "TD229", fabricanteResponse, classificacaoResponse, "Motor turbo diesel");
            when(service.update(eq(1L), any(ModeloRequestDTO.class))).thenReturn(updated);

            // Act & Assert
            mockMvc.perform(put(BASE_URL + "/{id}", 1L)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("TD229"))
                .andExpect(jsonPath("$.observacoes").value("Motor turbo diesel"));
        }

        @Test
        @DisplayName("Deve retornar 404 ao atualizar modelo inexistente")
        void atualizar_naoExistente_deveRetornar404() throws Exception {
            // Arrange
            ModeloRequestDTO request = new ModeloRequestDTO("D229", 1L, 1L, null);
            when(service.update(eq(999L), any(ModeloRequestDTO.class)))
                .thenThrow(new ResourceNotFoundException("Modelo não encontrado com ID: 999"));

            // Act & Assert
            mockMvc.perform(put(BASE_URL + "/{id}", 999L)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Modelo não encontrado com ID: 999"));
        }

        @Test
        @DisplayName("Deve retornar 409 ao atualizar para nome duplicado")
        void atualizar_nomeDuplicado_deveRetornar409() throws Exception {
            // Arrange
            ModeloRequestDTO request = new ModeloRequestDTO("TD229", 1L, 1L, null);
            when(service.update(eq(1L), any(ModeloRequestDTO.class)))
                .thenThrow(new ResourceConflictException("Já existe um modelo com o nome 'TD229' para o fabricante MWM"));

            // Act & Assert
            mockMvc.perform(put(BASE_URL + "/{id}", 1L)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());
        }

        @ParameterizedTest(name = "{0}")
        @MethodSource("dadosInvalidosAtualizacao")
        @DisplayName("Deve retornar 400 para dados inválidos na atualização")
        void atualizar_comDadosInvalidos_deveRetornar400(String cenario, ModeloRequestDTO request) throws Exception {
            // Act & Assert
            mockMvc.perform(put(BASE_URL + "/{id}", 1L)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
        }

        private static Stream<Arguments> dadosInvalidosAtualizacao() {
            return Stream.of(
                Arguments.of("Nome nulo", new ModeloRequestDTO(null, 1L, 1L, null)),
                Arguments.of("Nome vazio", new ModeloRequestDTO("", 1L, 1L, null)),
                Arguments.of("FabricanteId nulo", new ModeloRequestDTO("D229", null, 1L, null)),
                Arguments.of("ClassificacaoId nulo", new ModeloRequestDTO("D229", 1L, null, null))
            );
        }
    }

    // ========================= DELEÇÃO =========================

    @Nested
    @DisplayName("DELETE /api/modelos/{id}")
    class Delecao {

        @Test
        @DisplayName("Deve retornar 204 ao deletar modelo com sucesso")
        void deletar_existente_deveRetornar204() throws Exception {
            // Arrange
            doNothing().when(service).delete(1L);

            // Act & Assert
            mockMvc.perform(delete(BASE_URL + "/{id}", 1L))
                .andExpect(status().isNoContent());

            verify(service).delete(1L);
        }

        @Test
        @DisplayName("Deve retornar 404 ao deletar modelo inexistente")
        void deletar_naoExistente_deveRetornar404() throws Exception {
            // Arrange
            doThrow(new ResourceNotFoundException("Modelo não encontrado com ID: 999"))
                .when(service).delete(999L);

            // Act & Assert
            mockMvc.perform(delete(BASE_URL + "/{id}", 999L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Modelo não encontrado com ID: 999"));
        }
    }
}
