package com.sistema.fluxo.classificacao.service;

import com.sistema.fluxo.classificacao.dto.ClassificacaoRequestDTO;
import com.sistema.fluxo.classificacao.dto.ClassificacaoResponseDTO;
import com.sistema.fluxo.classificacao.model.Classificacao;
import com.sistema.fluxo.classificacao.repository.ClassificacaoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.lang.NonNull;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Testes unitários para ClassificacaoService.
 * Utiliza Mockito para simular o repository.
 */
@SuppressWarnings("null")
@ExtendWith(MockitoExtension.class)
@DisplayName("ClassificacaoService - Testes Unitários")
class ClassificacaoServiceTest {

    @Mock
    private ClassificacaoRepository repository;

    @InjectMocks
    private ClassificacaoService service;

    // ========== CONSTANTES PARA TESTES ==========
    private static final long ID_VALIDO = 1L;
    private static final long ID_INEXISTENTE = 999L;
    private static final String NOME_VALIDO = "A";
    private static final String NOME_ALTERNATIVO = "B";
    private static final String DESCRICAO_VALIDA = "Modelo de motor tipo A";
    private static final String DESCRICAO_ALTERNATIVA = "Modelo de motor tipo B";

    // ========== OBJETOS REUTILIZÁVEIS ==========
    
    private Classificacao classificacaoExistente;
    private ClassificacaoRequestDTO requestDTO;

    // ========== MÉTODOS AUXILIARES ==========

    @NonNull
    private Classificacao criarClassificacao(long id, String nome, String descricao) {
        Classificacao classificacao = new Classificacao();
        classificacao.setId(id);
        classificacao.setNome(nome);
        classificacao.setDescricao(descricao);
        return classificacao;
    }

    @NonNull
    private ClassificacaoRequestDTO criarRequestDTO(String nome, String descricao) {
        return new ClassificacaoRequestDTO(nome, descricao);
    }

    @BeforeEach
    void setUp() {
        classificacaoExistente = criarClassificacao(ID_VALIDO, NOME_VALIDO, DESCRICAO_VALIDA);
        requestDTO = criarRequestDTO(NOME_VALIDO, DESCRICAO_VALIDA);
    }

    // ========== TESTES DE LISTAGEM (findAll) ==========
    @Nested
    @DisplayName("Testes de Listagem (findAll)")
    class FindAllTest {

        @Test
        @DisplayName("Deve listar todas as classificações")
        void deveListarTodasClassificacoes() {
            // Arrange
            Classificacao classificacao1 = criarClassificacao(ID_VALIDO, NOME_VALIDO, DESCRICAO_VALIDA);
            Classificacao classificacao2 = criarClassificacao(2L, NOME_ALTERNATIVO, DESCRICAO_ALTERNATIVA);
            when(repository.findAll()).thenReturn(List.of(classificacao1, classificacao2));

            // Act
            List<ClassificacaoResponseDTO> resultado = service.findAll();

            // Assert
            assertNotNull(resultado, "Lista não deveria ser nula");
            assertEquals(2, resultado.size(), "Deveria retornar 2 classificações");
            verify(repository, times(1)).findAll();
        }

        @Test
        @DisplayName("Deve retornar lista vazia quando não existem classificações")
        void deveRetornarListaVaziaQuandoNaoExistemClassificacoes() {
            // Arrange
            when(repository.findAll()).thenReturn(Collections.emptyList());

            // Act
            List<ClassificacaoResponseDTO> resultado = service.findAll();

            // Assert
            assertNotNull(resultado, "Lista não deveria ser nula");
            assertTrue(resultado.isEmpty(), "Lista deveria estar vazia");
            verify(repository, times(1)).findAll();
        }
    }

    // ========== TESTES DE BUSCA POR ID (findById) ==========
    @Nested
    @DisplayName("Testes de Busca por ID (findById)")
    class FindByIdTest {

        @Test
        @DisplayName("Deve buscar classificação por ID com sucesso")
        void deveBuscarClassificacaoPorId() {
            // Arrange
            when(repository.findById(ID_VALIDO)).thenReturn(Optional.of(classificacaoExistente));

            // Act
            ClassificacaoResponseDTO resultado = Objects.requireNonNull(
                service.findById(ID_VALIDO), 
                "Service retornou null"
            );

            // Assert
            assertNotNull(resultado, "DTO não deveria ser nulo");
            assertEquals(ID_VALIDO, resultado.getId(), "ID deveria corresponder");
            assertEquals(NOME_VALIDO, resultado.getNome(), "Nome deveria corresponder");
            verify(repository, times(1)).findById(ID_VALIDO);
        }

        @Test
        @DisplayName("Deve lançar exceção quando ID não existe")
        void deveLancarExcecaoQuandoIdNaoExiste() {
            // Arrange
            when(repository.findById(ID_INEXISTENTE)).thenReturn(Optional.empty());

            // Act & Assert
            RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> service.findById(ID_INEXISTENTE),
                "Deveria lançar RuntimeException"
            );

            assertTrue(
                exception.getMessage().contains("não encontrada"),
                "Mensagem deveria indicar que não foi encontrada"
            );
            verify(repository, times(1)).findById(ID_INEXISTENTE);
        }
    }

    // ========== TESTES DE BUSCA POR NOME (findByNome) ==========
    @Nested
    @DisplayName("Testes de Busca por Nome (findByNome)")
    class FindByNomeTest {

        @Test
        @DisplayName("Deve buscar classificação por nome com sucesso")
        void deveBuscarClassificacaoPorNome() {
            // Arrange
            when(repository.findByNome(NOME_VALIDO)).thenReturn(Optional.of(classificacaoExistente));

            // Act
            ClassificacaoResponseDTO resultado = Objects.requireNonNull(
                service.findByNome(NOME_VALIDO),
                "Service retornou null"
            );

            // Assert
            assertNotNull(resultado, "DTO não deveria ser nulo");
            assertEquals(NOME_VALIDO, resultado.getNome(), "Nome deveria corresponder");
            verify(repository, times(1)).findByNome(NOME_VALIDO);
        }

        @Test
        @DisplayName("Deve lançar exceção quando nome não existe")
        void deveLancarExcecaoQuandoNomeNaoExiste() {
            // Arrange
            when(repository.findByNome("INEXISTENTE")).thenReturn(Optional.empty());

            // Act & Assert
            RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> service.findByNome("INEXISTENTE"),
                "Deveria lançar RuntimeException"
            );

            assertTrue(
                exception.getMessage().contains("não encontrada"),
                "Mensagem deveria indicar que não foi encontrada"
            );
            verify(repository, times(1)).findByNome("INEXISTENTE");
        }
    }

    // ========== TESTES DE CRIAÇÃO (save) ==========
    @Nested
    @DisplayName("Testes de Criação (save)")
    class SaveTest {

        @Test
        @DisplayName("Deve salvar nova classificação com sucesso")
        void deveSalvarNovaClassificacao() {
            // Arrange
            
            when(repository.existsByNome(NOME_VALIDO)).thenReturn(false);
            //when(repository.save((Classificacao) any())).thenReturn(classificacaoExistente);
            //when(repository.save(argThat(Objects::nonNull))).thenReturn(classificacaoExistente);
            when(repository.save(any(Classificacao.class))).thenReturn(classificacaoExistente);

            // Act
            ClassificacaoResponseDTO resultado = Objects.requireNonNull(
                service.save(requestDTO),
                "Service retornou null"
            );

            // Assert
            assertNotNull(resultado, "DTO não deveria ser nulo");
            assertEquals(NOME_VALIDO, resultado.getNome(), "Nome deveria corresponder");
            assertEquals(DESCRICAO_VALIDA, resultado.getDescricao(), "Descrição deveria corresponder");
            verify(repository, times(1)).existsByNome(NOME_VALIDO);
            verify(repository, times(1)).save(any(Classificacao.class));
        }

        @Test
        @DisplayName("Deve lançar exceção ao salvar com nome duplicado")
        void deveLancarExcecaoAoSalvarNomeDuplicado() {
            // Arrange
            when(repository.existsByNome(NOME_VALIDO)).thenReturn(true);

            // Act & Assert
            RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> service.save(requestDTO),
                "Deveria lançar RuntimeException"
            );

            assertTrue(
                exception.getMessage().contains("Já existe"),
                "Mensagem deveria indicar duplicidade"
            );
            verify(repository, times(1)).existsByNome(NOME_VALIDO);
            verify(repository, never()).save(any(Classificacao.class));
        }
    }

    // ========== TESTES DE ATUALIZAÇÃO (update) ==========
    @Nested
    @DisplayName("Testes de Atualização (update)")
    class UpdateTest {

        @Test
        @DisplayName("Deve atualizar classificação com sucesso")
        void deveAtualizarClassificacao() {
            // Arrange
            ClassificacaoRequestDTO updateDTO = criarRequestDTO(NOME_ALTERNATIVO, DESCRICAO_ALTERNATIVA);
            Classificacao classificacaoAtualizada = criarClassificacao(ID_VALIDO, NOME_ALTERNATIVO, DESCRICAO_ALTERNATIVA);

            when(repository.findById(ID_VALIDO)).thenReturn(Optional.of(classificacaoExistente));
            when(repository.existsByNome(NOME_ALTERNATIVO)).thenReturn(false);
            when(repository.save(any(Classificacao.class))).thenReturn(classificacaoAtualizada);

            // Act
            ClassificacaoResponseDTO resultado = Objects.requireNonNull(
                service.update(ID_VALIDO, updateDTO),
                "Service retornou null"
            );

            // Assert
            assertNotNull(resultado, "DTO não deveria ser nulo");
            assertEquals(NOME_ALTERNATIVO, resultado.getNome(), "Nome deveria ser atualizado");
            assertEquals(DESCRICAO_ALTERNATIVA, resultado.getDescricao(), "Descrição deveria ser atualizada");
            verify(repository, times(1)).findById(ID_VALIDO);
            verify(repository, times(1)).save(any(Classificacao.class));
        }

        @Test
        @DisplayName("Deve atualizar classificação mantendo o mesmo nome")
        void deveAtualizarClassificacaoMantendoMesmoNome() {
            // Arrange
            ClassificacaoRequestDTO updateDTO = criarRequestDTO(NOME_VALIDO, DESCRICAO_ALTERNATIVA);
            Classificacao classificacaoAtualizada = criarClassificacao(ID_VALIDO, NOME_VALIDO, DESCRICAO_ALTERNATIVA);

            when(repository.findById(ID_VALIDO)).thenReturn(Optional.of(classificacaoExistente));
            when(repository.save(any(Classificacao.class))).thenReturn(classificacaoAtualizada);

            // Act
            ClassificacaoResponseDTO resultado = Objects.requireNonNull(
                service.update(ID_VALIDO, updateDTO),
                "Service retornou null"
            );

            // Assert
            assertNotNull(resultado, "DTO não deveria ser nulo");
            assertEquals(NOME_VALIDO, resultado.getNome(), "Nome deveria permanecer igual");
            assertEquals(DESCRICAO_ALTERNATIVA, resultado.getDescricao(), "Descrição deveria ser atualizada");
            verify(repository, never()).existsByNome(anyString());
        }

        @Test
        @DisplayName("Deve lançar exceção ao atualizar para nome duplicado")
        void deveLancarExcecaoAoAtualizarParaNomeDuplicado() {
            // Arrange
            ClassificacaoRequestDTO updateDTO = criarRequestDTO(NOME_ALTERNATIVO, DESCRICAO_ALTERNATIVA);

            when(repository.findById(ID_VALIDO)).thenReturn(Optional.of(classificacaoExistente));
            when(repository.existsByNome(NOME_ALTERNATIVO)).thenReturn(true);

            // Act & Assert
            RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> service.update(ID_VALIDO, updateDTO),
                "Deveria lançar RuntimeException"
            );

            assertTrue(
                exception.getMessage().contains("Já existe"),
                "Mensagem deveria indicar duplicidade"
            );
            verify(repository, times(1)).findById(ID_VALIDO);
            verify(repository, never()).save(any(Classificacao.class));
        }

        @Test
        @DisplayName("Deve lançar exceção ao atualizar ID inexistente")
        void deveLancarExcecaoAoAtualizarIdInexistente() {
            // Arrange
            when(repository.findById(ID_INEXISTENTE)).thenReturn(Optional.empty());

            // Act & Assert
            RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> service.update(ID_INEXISTENTE, requestDTO),
                "Deveria lançar RuntimeException"
            );

            assertTrue(
                exception.getMessage().contains("não encontrada"),
                "Mensagem deveria indicar que não foi encontrada"
            );
            verify(repository, times(1)).findById(ID_INEXISTENTE);
            verify(repository, never()).save(any(Classificacao.class));
        }
    }

    // ========== TESTES DE DELEÇÃO (delete) ==========
    @Nested
    @DisplayName("Testes de Deleção (delete)")
    class DeleteTest {

        @Test
        @DisplayName("Deve deletar classificação com sucesso")
        void deveDeletarClassificacao() {
            // Arrange
            when(repository.existsById(ID_VALIDO)).thenReturn(true);
            doNothing().when(repository).deleteById(ID_VALIDO);

            // Act
            assertDoesNotThrow(
                () -> service.delete(ID_VALIDO),
                "Não deveria lançar exceção"
            );

            // Assert
            verify(repository, times(1)).existsById(ID_VALIDO);
            verify(repository, times(1)).deleteById(ID_VALIDO);
        }

        @Test
        @DisplayName("Deve lançar exceção ao deletar ID inexistente")
        void deveLancarExcecaoAoDeletarIdInexistente() {
            // Arrange
            when(repository.existsById(ID_INEXISTENTE)).thenReturn(false);

            // Act & Assert
            RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> service.delete(ID_INEXISTENTE),
                "Deveria lançar RuntimeException"
            );

            assertTrue(
                exception.getMessage().contains("não encontrada"),
                "Mensagem deveria indicar que não foi encontrada"
            );
            verify(repository, times(1)).existsById(ID_INEXISTENTE);
            verify(repository, never()).deleteById(anyLong());
        }
    }
    
}
            