package com.sistema.fluxo.classificacao.repository;

import com.sistema.fluxo.classificacao.model.Classificacao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.lang.NonNull;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes de integração para ClassificacaoRepository.
 * Utiliza banco H2 em memória para testes isolados.
 */
@DataJpaTest
@ActiveProfiles("test")
@DisplayName("ClassificacaoRepository - Testes de Integração")
class ClassificacaoRepositoryTest {

    @Autowired
    private ClassificacaoRepository repository;

    @Autowired
    private TestEntityManager entityManager;

    // ========== CONSTANTES PARA TESTES ==========
    private static final String NOME_VALIDO = "A";
    private static final String NOME_ALTERNATIVO = "B";
    private static final String DESCRICAO_VALIDA = "Modelo de motor tipo A";
    private static final String DESCRICAO_ALTERNATIVA = "Modelo de motor tipo B";

    // ========== MÉTODOS AUXILIARES ==========

    @NonNull
    private Classificacao criarClassificacao(String nome, String descricao) {
        Classificacao classificacao = new Classificacao();
        classificacao.setNome(nome);
        classificacao.setDescricao(descricao);
        return classificacao;
    }

    @NonNull
    private Classificacao persistirClassificacao(String nome, String descricao) {
        Classificacao classificacao = criarClassificacao(nome, descricao);
        entityManager.persistAndFlush(classificacao);
        return classificacao;
    }

    @BeforeEach
    void setUp() {
        // Limpa o banco antes de cada teste
        repository.deleteAll();
        entityManager.flush();
        entityManager.clear();
    }

    // ========== TESTES DE PERSISTÊNCIA ==========
    @Nested
    @DisplayName("Testes de Persistência (Save)")
    class PersistenciaTest {

        @Test
        @DisplayName("Deve salvar classificação com sucesso")
        void deveSalvarClassificacao() {
            // Arrange
            Classificacao classificacao = criarClassificacao(NOME_VALIDO, DESCRICAO_VALIDA);

            // Act
            Classificacao salva = Objects.requireNonNull(repository.save(classificacao), "repository.save returned null");

            // Assert
            assertNotNull(salva.getId(), "ID deveria ser gerado após persistência");
            assertEquals(NOME_VALIDO, salva.getNome(), "Nome deveria ser persistido");
            assertEquals(DESCRICAO_VALIDA, salva.getDescricao(), "Descrição deveria ser persistida");
        }

        @Test
        @DisplayName("Deve atualizar classificação existente")
        void deveAtualizarClassificacao() {
            // Arrange
            Classificacao classificacao = persistirClassificacao(NOME_VALIDO, DESCRICAO_VALIDA);
            Long idOriginal = classificacao.getId();
            
            String novaDescricao = "Descrição atualizada";
            classificacao.setDescricao(novaDescricao);

            // Act
            Classificacao atualizada = repository.save(classificacao);

            // Assert
            assertEquals(idOriginal, atualizada.getId(), "ID não deveria mudar na atualização");
            assertEquals(novaDescricao, atualizada.getDescricao(), "Descrição deveria ser atualizada");
        }
    }

    // ========== TESTES DE BUSCA POR ID ==========
    @Nested
    @DisplayName("Testes de Busca por ID")
    class BuscaPorIdTest {

        @Test
        @DisplayName("Deve buscar classificação por ID")
        void deveBuscarClassificacaoPorId() {
            // Arrange

            Classificacao classificacao = persistirClassificacao(NOME_VALIDO, DESCRICAO_VALIDA);

            // Act
            Long id = Objects.requireNonNull(classificacao.getId(), "ID deveria ter sido gerado");
            Optional<Classificacao> resultado = repository.findById(id);

            // Assert
            assertTrue(resultado.isPresent(), "Deveria encontrar a classificação");
            assertEquals(NOME_VALIDO, resultado.get().getNome(), "Nome deveria corresponder");
        }

        @Test
        @DisplayName("Deve retornar Optional vazio quando ID não existe")
        void deveRetornarOptionalVazioQuandoIdNaoExiste() {
            // Act
            Optional<Classificacao> resultado = repository.findById(999L);

            // Assert
            assertTrue(resultado.isEmpty(), "Deveria retornar Optional vazio para ID inexistente");
        }
    }

    // ========== TESTES DE BUSCA POR NOME ==========
    @Nested
    @DisplayName("Testes de Busca por Nome")
    class BuscaPorNomeTest {

        @Test
        @DisplayName("Deve buscar classificação por nome")
        void deveBuscarClassificacaoPorNome() {
            // Arrange
            persistirClassificacao(NOME_VALIDO, DESCRICAO_VALIDA);

            // Act
            Optional<Classificacao> resultado = repository.findByNome(NOME_VALIDO);

            // Assert
            assertTrue(resultado.isPresent(), "Deveria encontrar a classificação pelo nome");
            assertEquals(NOME_VALIDO, resultado.get().getNome(), "Nome deveria corresponder");
            assertEquals(DESCRICAO_VALIDA, resultado.get().getDescricao(), "Descrição deveria corresponder");
        }

        @Test
        @DisplayName("Deve retornar Optional vazio quando nome não existe")
        void deveRetornarOptionalVazioQuandoNomeNaoExiste() {
            // Act
            Optional<Classificacao> resultado = repository.findByNome("INEXISTENTE");

            // Assert
            assertTrue(resultado.isEmpty(), "Deveria retornar Optional vazio para nome inexistente");
        }
    }

    // ========== TESTES DE EXISTÊNCIA ==========
    @Nested
    @DisplayName("Testes de Verificação de Existência")
    class ExistenciaTest {

        @Test
        @DisplayName("Deve retornar true quando nome existe")
        void deveVerificarExistenciaPorNome() {
            // Arrange
            persistirClassificacao(NOME_VALIDO, DESCRICAO_VALIDA);

            // Act
            boolean existe = repository.existsByNome(NOME_VALIDO);

            // Assert
            assertTrue(existe, "Deveria retornar true para nome existente");
        }

        @Test
        @DisplayName("Deve retornar false quando nome não existe")
        void deveRetornarFalseQuandoNomeNaoExiste() {
            // Act
            boolean existe = repository.existsByNome("INEXISTENTE");

            // Assert
            assertFalse(existe, "Deveria retornar false para nome inexistente");
        }

        @Test
        @DisplayName("Deve retornar true quando ID existe")
        void deveRetornarTrueQuandoIdExiste() {
            // Arrange
            Classificacao classificacao = persistirClassificacao(NOME_VALIDO, DESCRICAO_VALIDA);

            // Act
            Long id = Objects.requireNonNull(classificacao.getId(), "ID deveria ter sido gerado");
            boolean existe = repository.existsById(id);

            // Assert
            assertTrue(existe, "Deveria retornar true para ID existente");
        }

        @Test
        @DisplayName("Deve retornar false quando ID não existe")
        void deveRetornarFalseQuandoIdNaoExiste() {
            // Act
            boolean existe = repository.existsById(999L);

            // Assert
            assertFalse(existe, "Deveria retornar false para ID inexistente");
        }
    }

    // ========== TESTES DE LISTAGEM ==========
    @Nested
    @DisplayName("Testes de Listagem")
    class ListagemTest {

        @Test
        @DisplayName("Deve listar todas as classificações")
        void deveListarTodasClassificacoes() {
            // Arrange
            persistirClassificacao(NOME_VALIDO, DESCRICAO_VALIDA);
            persistirClassificacao(NOME_ALTERNATIVO, DESCRICAO_ALTERNATIVA);

            // Act
            List<Classificacao> resultado = repository.findAll();

            // Assert
            assertEquals(2, resultado.size(), "Deveria retornar 2 classificações");
        }

        @Test
        @DisplayName("Deve retornar lista vazia quando não existem classificações")
        void deveRetornarListaVaziaQuandoNaoExistemClassificacoes() {
            // Act
            List<Classificacao> resultado = repository.findAll();

            // Assert
            assertTrue(resultado.isEmpty(), "Deveria retornar lista vazia");
        }
    }

    // ========== TESTES DE DELEÇÃO ==========
    @Nested
    @DisplayName("Testes de Deleção")
    class DelecaoTest {

        @Test
        @DisplayName("Deve deletar classificação por ID")
        void deveDeletarClassificacao() {
            // Arrange
            Classificacao classificacao = persistirClassificacao(NOME_VALIDO, DESCRICAO_VALIDA);
            long id = classificacao.getId();

            // Act
            repository.deleteById(id);
            entityManager.flush();

            // Assert
            Optional<Classificacao> resultado = repository.findById(id);
            assertTrue(resultado.isEmpty(), "Classificação deveria ter sido deletada");
        }

        @Test
        @DisplayName("Deve deletar todas as classificações")
        void deveDeletarTodasClassificacoes() {
            // Arrange
            persistirClassificacao(NOME_VALIDO, DESCRICAO_VALIDA);
            persistirClassificacao(NOME_ALTERNATIVO, DESCRICAO_ALTERNATIVA);

            // Act
            repository.deleteAll();
            entityManager.flush();

            // Assert
            List<Classificacao> resultado = repository.findAll();
            assertTrue(resultado.isEmpty(), "Todas as classificações deveriam ter sido deletadas");
        }
    }
}
