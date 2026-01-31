package com.sistema.fluxo.classificacao.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes unitários para a entidade Classificacao.
 * Valida o funcionamento correto das anotações Lombok e a estrutura da entidade.
 */
@DisplayName("Classificacao - Testes da Entidade")
class ClassificacaoTest {

    // ========== CONSTANTES PARA TESTES ==========
    private static final Long ID_VALIDO = 1L;
    private static final String NOME_VALIDO = "A";
    private static final String DESCRICAO_VALIDA = "Modelo de motor tipo A";

    // ========== TESTES DE CONSTRUTORES ==========
    @Nested
    @DisplayName("Testes de Construtores")
    class ConstrutoresTest {

        @Test
        @DisplayName("Deve criar Classificacao com construtor vazio (@NoArgsConstructor)")
        void deveCriarClassificacaoComConstrutorVazio() {
            // Act
            Classificacao classificacao = new Classificacao();

            // Assert
            assertNotNull(classificacao, "A instância não deveria ser nula");
            assertNull(classificacao.getId(), "ID deveria ser nulo");
            assertNull(classificacao.getNome(), "Nome deveria ser nulo");
            assertNull(classificacao.getDescricao(), "Descrição deveria ser nula");
        }

        @Test
        @DisplayName("Deve criar Classificacao com todos os argumentos (@AllArgsConstructor)")
        void deveCriarClassificacaoComTodosOsArgumentos() {
            // Act
            Classificacao classificacao = new Classificacao(ID_VALIDO, NOME_VALIDO, DESCRICAO_VALIDA);

            // Assert
            assertNotNull(classificacao, "A instância não deveria ser nula");
            assertEquals(ID_VALIDO, classificacao.getId(), "ID deveria ser igual ao informado");
            assertEquals(NOME_VALIDO, classificacao.getNome(), "Nome deveria ser igual ao informado");
            assertEquals(DESCRICAO_VALIDA, classificacao.getDescricao(), "Descrição deveria ser igual à informada");
        }
    }

    // ========== TESTES DE GETTERS E SETTERS ==========
    @Nested
    @DisplayName("Testes de Getters e Setters")
    class GettersSettersTest {

        @Test
        @DisplayName("Deve definir e obter todos os atributos corretamente")
        void deveDefinirEObterAtributos() {
            // Arrange
            Classificacao classificacao = new Classificacao();

            // Act
            classificacao.setId(ID_VALIDO);
            classificacao.setNome(NOME_VALIDO);
            classificacao.setDescricao(DESCRICAO_VALIDA);

            // Assert
            assertEquals(ID_VALIDO, classificacao.getId(), "Getter de ID falhou");
            assertEquals(NOME_VALIDO, classificacao.getNome(), "Getter de Nome falhou");
            assertEquals(DESCRICAO_VALIDA, classificacao.getDescricao(), "Getter de Descrição falhou");
        }
    }

    // ========== TESTES DE EQUALS E HASHCODE ==========
    @Nested
    @DisplayName("Testes de Equals e HashCode")
    class EqualsHashCodeTest {

        @Test
        @DisplayName("Equals deve comparar apenas por ID (@EqualsAndHashCode(of = 'id'))")
        void equalsDeveCompararPorId() {
            // Arrange
            Classificacao classificacao1 = new Classificacao(1L, "A", "Descrição A");
            Classificacao classificacao2 = new Classificacao(1L, "B", "Descrição B"); // Mesmo ID, dados diferentes
            Classificacao classificacao3 = new Classificacao(2L, "A", "Descrição A"); // ID diferente, mesmos dados

            // Assert
            assertEquals(classificacao1, classificacao2, 
                "Classificações com mesmo ID deveriam ser iguais");
            assertNotEquals(classificacao1, classificacao3, 
                "Classificações com IDs diferentes não deveriam ser iguais");
        }

        @Test
        @DisplayName("HashCode deve ser consistente com Equals")
        void hashCodeDeveSerConsistenteComEquals() {
            // Arrange
            Classificacao classificacao1 = new Classificacao(1L, "A", "Descrição A");
            Classificacao classificacao2 = new Classificacao(1L, "B", "Descrição B");

            // Assert
            assertEquals(classificacao1.hashCode(), classificacao2.hashCode(),
                "HashCodes de objetos iguais (mesmo ID) deveriam ser iguais");
        }

        @Test
        @DisplayName("Equals deve retornar false para null")
        void equalsDeveRetornarFalseParaNull() {
            // Arrange
            Classificacao classificacao = new Classificacao(1L, "A", "Descrição");

            // Assert
            assertNotEquals(null, classificacao, 
                "Classificação não deveria ser igual a null");
        }

        @Test
        @DisplayName("Equals deve retornar false para objeto de tipo diferente")
        void equalsDeveRetornarFalseParaTipoDiferente() {
            // Arrange
            Classificacao classificacao = new Classificacao(1L, "A", "Descrição");
            String outroObjeto = "Não sou uma Classificação";

            // Assert
            assertNotEquals(classificacao, outroObjeto,
                "Classificação não deveria ser igual a objeto de outro tipo");
        }
    }

    // ========== TESTES DE TOSTRING ==========
    @Nested
    @DisplayName("Testes de ToString")
    class ToStringTest {

        @Test
        @DisplayName("ToString deve conter informações da entidade")
        void toStringDeveConterInformacoes() {
            // Arrange
            Classificacao classificacao = new Classificacao(ID_VALIDO, NOME_VALIDO, DESCRICAO_VALIDA);

            // Act
            String resultado = classificacao.toString();

            // Assert
            assertNotNull(resultado, "ToString não deveria retornar null");
            assertTrue(resultado.contains("id=" + ID_VALIDO) || resultado.contains(ID_VALIDO.toString()),
                "ToString deveria conter o ID");
            assertTrue(resultado.contains(NOME_VALIDO),
                "ToString deveria conter o nome");
            assertTrue(resultado.contains(DESCRICAO_VALIDA),
                "ToString deveria conter a descrição");
        }
    }
}
