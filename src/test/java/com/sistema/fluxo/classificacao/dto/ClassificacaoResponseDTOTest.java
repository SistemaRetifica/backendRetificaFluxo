package com.sistema.fluxo.classificacao.dto;

import com.sistema.fluxo.classificacao.model.Classificacao;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes unitários para ClassificacaoResponseDTO.
 * Valida construtores, getters/setters e conversão de entidade para DTO.
 */
@DisplayName("ClassificacaoResponseDTO - Testes de Conversão")
class ClassificacaoResponseDTOTest {

    // ========== CONSTANTES PARA TESTES ==========
    private static final Long ID_VALIDO = 1L;
    private static final String NOME_VALIDO = "A";
    private static final String DESCRICAO_VALIDA = "Modelo de motor tipo A";

    // ========== MÉTODOS AUXILIARES ==========

    private Classificacao criarEntidade(Long id, String nome, String descricao) {
        return new Classificacao(id, nome, descricao);
    }

    // ========== TESTES DE CONSTRUTORES ==========
    @Nested
    @DisplayName("Testes de Construtores")
    class ConstrutoresTest {

        @Test
        @DisplayName("Deve criar ResponseDTO com construtor vazio")
        void deveCriarResponseDTOComConstrutorVazio() {
            // Act
            ClassificacaoResponseDTO dto = new ClassificacaoResponseDTO();

            // Assert
            assertNotNull(dto, "DTO não deveria ser nulo");
            assertNull(dto.getId(), "ID deveria ser nulo");
            assertNull(dto.getNome(), "Nome deveria ser nulo");
            assertNull(dto.getDescricao(), "Descrição deveria ser nula");
        }

        @Test
        @DisplayName("Deve criar ResponseDTO com todos os argumentos")
        void deveCriarResponseDTOComTodosOsArgumentos() {
            // Act
            ClassificacaoResponseDTO dto = new ClassificacaoResponseDTO(
                ID_VALIDO, 
                NOME_VALIDO, 
                DESCRICAO_VALIDA
            );

            // Assert
            assertNotNull(dto, "DTO não deveria ser nulo");
            assertEquals(ID_VALIDO, dto.getId(), "ID deveria ser igual ao informado");
            assertEquals(NOME_VALIDO, dto.getNome(), "Nome deveria ser igual ao informado");
            assertEquals(DESCRICAO_VALIDA, dto.getDescricao(), "Descrição deveria ser igual à informada");
        }
    }

    // ========== TESTES DE CONVERSÃO ==========
    @Nested
    @DisplayName("Testes de Conversão Entidade -> DTO")
    class ConversaoTest {

        @Test
        @DisplayName("Deve converter entidade para DTO corretamente")
        void deveConverterEntidadeParaDTO() {
            // Arrange
            Classificacao entidade = criarEntidade(ID_VALIDO, NOME_VALIDO, DESCRICAO_VALIDA);

            // Act
            ClassificacaoResponseDTO dto = new ClassificacaoResponseDTO(entidade);

            // Assert
            assertNotNull(dto, "DTO convertido não deveria ser nulo");
        }

        @Test
        @DisplayName("Deve mapear todos os campos corretamente na conversão")
        void deveMapearTodosOsCamposCorretamente() {
            // Arrange
            Classificacao entidade = criarEntidade(ID_VALIDO, NOME_VALIDO, DESCRICAO_VALIDA);

            // Act
            ClassificacaoResponseDTO dto = new ClassificacaoResponseDTO(entidade);

            // Assert
            assertAll("Mapeamento de campos",
                () -> assertEquals(entidade.getId(), dto.getId(), 
                    "ID deveria ser mapeado corretamente"),
                () -> assertEquals(entidade.getNome(), dto.getNome(), 
                    "Nome deveria ser mapeado corretamente"),
                () -> assertEquals(entidade.getDescricao(), dto.getDescricao(), 
                    "Descrição deveria ser mapeada corretamente")
            );
        }

        @Test
        @DisplayName("Deve converter entidade com descrição nula")
        void deveConverterEntidadeComDescricaoNula() {
            // Arrange
            Classificacao entidade = criarEntidade(ID_VALIDO, NOME_VALIDO, null);

            // Act
            ClassificacaoResponseDTO dto = new ClassificacaoResponseDTO(entidade);

            // Assert
            assertNotNull(dto, "DTO não deveria ser nulo mesmo com descrição nula");
            assertEquals(ID_VALIDO, dto.getId(), "ID deveria ser mapeado");
            assertEquals(NOME_VALIDO, dto.getNome(), "Nome deveria ser mapeado");
            assertNull(dto.getDescricao(), "Descrição deveria permanecer nula");
        }

        @Test
        @DisplayName("Deve converter entidade com ID nulo (entidade não persistida)")
        void deveConverterEntidadeComIdNulo() {
            // Arrange
            Classificacao entidade = criarEntidade(null, NOME_VALIDO, DESCRICAO_VALIDA);

            // Act
            ClassificacaoResponseDTO dto = new ClassificacaoResponseDTO(entidade);

            // Assert
            assertNotNull(dto, "DTO não deveria ser nulo");
            assertNull(dto.getId(), "ID deveria permanecer nulo");
            assertEquals(NOME_VALIDO, dto.getNome(), "Nome deveria ser mapeado");
            assertEquals(DESCRICAO_VALIDA, dto.getDescricao(), "Descrição deveria ser mapeada");
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
            ClassificacaoResponseDTO dto = new ClassificacaoResponseDTO();

            // Act
            dto.setId(ID_VALIDO);
            dto.setNome(NOME_VALIDO);
            dto.setDescricao(DESCRICAO_VALIDA);

            // Assert
            assertEquals(ID_VALIDO, dto.getId(), "Getter de ID falhou");
            assertEquals(NOME_VALIDO, dto.getNome(), "Getter de Nome falhou");
            assertEquals(DESCRICAO_VALIDA, dto.getDescricao(), "Getter de Descrição falhou");
        }

        @Test
        @DisplayName("Deve permitir atualizar valores após criação")
        void devePermitirAtualizarValores() {
            // Arrange
            ClassificacaoResponseDTO dto = new ClassificacaoResponseDTO(
                ID_VALIDO, 
                NOME_VALIDO, 
                DESCRICAO_VALIDA
            );
            
            Long novoId = 2L;
            String novoNome = "B";
            String novaDescricao = "Nova descrição";

            // Act
            dto.setId(novoId);
            dto.setNome(novoNome);
            dto.setDescricao(novaDescricao);

            // Assert
            assertEquals(novoId, dto.getId(), "ID deveria ser atualizado");
            assertEquals(novoNome, dto.getNome(), "Nome deveria ser atualizado");
            assertEquals(novaDescricao, dto.getDescricao(), "Descrição deveria ser atualizada");
        }
    }
}
