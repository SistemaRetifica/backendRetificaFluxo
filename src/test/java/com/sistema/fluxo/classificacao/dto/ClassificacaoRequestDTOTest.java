package com.sistema.fluxo.classificacao.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes unitários para ClassificacaoRequestDTO.
 * Valida as constraints de Bean Validation (@NotBlank, @Size).
 */
@DisplayName("ClassificacaoRequestDTO - Testes de Validação")
class ClassificacaoRequestDTOTest {

    private static Validator validator;

    // ========== CONSTANTES PARA TESTES ==========
    private static final String NOME_VALIDO = "A";
    private static final String NOME_LIMITE_MAXIMO = "ABCDEFGHIJ"; // Exatamente 10 caracteres
    private static final String NOME_EXCEDE_LIMITE = "ABCDEFGHIJK"; // 11 caracteres
    private static final String DESCRICAO_VALIDA = "Modelo de motor tipo A";
    private static final String DESCRICAO_LIMITE_MAXIMO = "A".repeat(200); // Exatamente 200 caracteres
    private static final String DESCRICAO_EXCEDE_LIMITE = "A".repeat(201); // 201 caracteres

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    // ========== MÉTODOS AUXILIARES ==========
    
    private ClassificacaoRequestDTO criarDTO(String nome, String descricao) {
        return new ClassificacaoRequestDTO(nome, descricao);
    }

    private Set<ConstraintViolation<ClassificacaoRequestDTO>> validar(ClassificacaoRequestDTO dto) {
        return validator.validate(dto);
    }

    private boolean contemViolacaoNoCampo(Set<ConstraintViolation<ClassificacaoRequestDTO>> violacoes, String campo) {
        return violacoes.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals(campo));
    }

    // ========== TESTES DO CAMPO NOME ==========
    @Nested
    @DisplayName("Validações do campo 'nome'")
    class NomeValidationTest {

        @Test
        @DisplayName("Deve aceitar nome válido")
        void deveAceitarNomeValido() {
            // Arrange
            ClassificacaoRequestDTO dto = criarDTO(NOME_VALIDO, DESCRICAO_VALIDA);

            // Act
            Set<ConstraintViolation<ClassificacaoRequestDTO>> violacoes = validar(dto);

            // Assert
            assertTrue(violacoes.isEmpty(), 
                "Não deveria haver violações para DTO válido");
        }

        @Test
        @DisplayName("Deve aceitar nome com exatamente 10 caracteres (limite máximo)")
        void deveAceitarNomeNoLimiteMaximo() {
            // Arrange
            ClassificacaoRequestDTO dto = criarDTO(NOME_LIMITE_MAXIMO, DESCRICAO_VALIDA);

            // Act
            Set<ConstraintViolation<ClassificacaoRequestDTO>> violacoes = validar(dto);

            // Assert
            assertFalse(contemViolacaoNoCampo(violacoes, "nome"),
                "Nome com 10 caracteres deveria ser aceito");
        }

        @Test
        @DisplayName("Deve falhar com nome vazio")
        void deveFalharComNomeVazio() {
            // Arrange
            ClassificacaoRequestDTO dto = criarDTO("", DESCRICAO_VALIDA);

            // Act
            Set<ConstraintViolation<ClassificacaoRequestDTO>> violacoes = validar(dto);

            // Assert
            assertFalse(violacoes.isEmpty(), 
                "Deveria haver violações para nome vazio");
            assertTrue(contemViolacaoNoCampo(violacoes, "nome"),
                "Violação deveria ser no campo 'nome'");
        }

        @Test
        @DisplayName("Deve falhar com nome nulo")
        void deveFalharComNomeNulo() {
            // Arrange
            ClassificacaoRequestDTO dto = criarDTO(null, DESCRICAO_VALIDA);

            // Act
            Set<ConstraintViolation<ClassificacaoRequestDTO>> violacoes = validar(dto);

            // Assert
            assertFalse(violacoes.isEmpty(), 
                "Deveria haver violações para nome nulo");
            assertTrue(contemViolacaoNoCampo(violacoes, "nome"),
                "Violação deveria ser no campo 'nome'");
        }

        @Test
        @DisplayName("Deve falhar com nome contendo apenas espaços em branco")
        void deveFalharComNomeApenasEspacos() {
            // Arrange
            ClassificacaoRequestDTO dto = criarDTO("   ", DESCRICAO_VALIDA);

            // Act
            Set<ConstraintViolation<ClassificacaoRequestDTO>> violacoes = validar(dto);

            // Assert
            assertFalse(violacoes.isEmpty(), 
                "Deveria haver violações para nome com apenas espaços");
            assertTrue(contemViolacaoNoCampo(violacoes, "nome"),
                "Violação deveria ser no campo 'nome'");
        }

        @Test
        @DisplayName("Deve falhar com nome maior que 10 caracteres")
        void deveFalharComNomeMaiorQue10Caracteres() {
            // Arrange
            ClassificacaoRequestDTO dto = criarDTO(NOME_EXCEDE_LIMITE, DESCRICAO_VALIDA);

            // Act
            Set<ConstraintViolation<ClassificacaoRequestDTO>> violacoes = validar(dto);

            // Assert
            assertFalse(violacoes.isEmpty(), 
                "Deveria haver violações para nome com mais de 10 caracteres");
            assertTrue(contemViolacaoNoCampo(violacoes, "nome"),
                "Violação deveria ser no campo 'nome'");
        }
    }

    // ========== TESTES DO CAMPO DESCRIÇÃO ==========
    @Nested
    @DisplayName("Validações do campo 'descricao'")
    class DescricaoValidationTest {

        @Test
        @DisplayName("Deve aceitar descrição nula (campo opcional)")
        void deveAceitarDescricaoNula() {
            // Arrange
            ClassificacaoRequestDTO dto = criarDTO(NOME_VALIDO, null);

            // Act
            Set<ConstraintViolation<ClassificacaoRequestDTO>> violacoes = validar(dto);

            // Assert
            assertFalse(contemViolacaoNoCampo(violacoes, "descricao"),
                "Descrição nula deveria ser aceita");
        }

        @Test
        @DisplayName("Deve aceitar descrição vazia (campo opcional)")
        void deveAceitarDescricaoVazia() {
            // Arrange
            ClassificacaoRequestDTO dto = criarDTO(NOME_VALIDO, "");

            // Act
            Set<ConstraintViolation<ClassificacaoRequestDTO>> violacoes = validar(dto);

            // Assert
            assertFalse(contemViolacaoNoCampo(violacoes, "descricao"),
                "Descrição vazia deveria ser aceita");
        }

        @Test
        @DisplayName("Deve aceitar descrição com exatamente 200 caracteres (limite máximo)")
        void deveAceitarDescricaoNoLimiteMaximo() {
            // Arrange
            ClassificacaoRequestDTO dto = criarDTO(NOME_VALIDO, DESCRICAO_LIMITE_MAXIMO);

            // Act
            Set<ConstraintViolation<ClassificacaoRequestDTO>> violacoes = validar(dto);

            // Assert
            assertFalse(contemViolacaoNoCampo(violacoes, "descricao"),
                "Descrição com 200 caracteres deveria ser aceita");
        }

        @Test
        @DisplayName("Deve falhar com descrição maior que 200 caracteres")
        void deveFalharComDescricaoMaiorQue200Caracteres() {
            // Arrange
            ClassificacaoRequestDTO dto = criarDTO(NOME_VALIDO, DESCRICAO_EXCEDE_LIMITE);

            // Act
            Set<ConstraintViolation<ClassificacaoRequestDTO>> violacoes = validar(dto);

            // Assert
            assertFalse(violacoes.isEmpty(), 
                "Deveria haver violações para descrição com mais de 200 caracteres");
            assertTrue(contemViolacaoNoCampo(violacoes, "descricao"),
                "Violação deveria ser no campo 'descricao'");
        }
    }

    // ========== TESTES DE CONSTRUTORES ==========
    @Nested
    @DisplayName("Testes de Construtores")
    class ConstrutoresTest {

        @Test
        @DisplayName("Deve criar DTO com construtor vazio")
        void deveCriarDTOComConstrutorVazio() {
            // Act
            ClassificacaoRequestDTO dto = new ClassificacaoRequestDTO();

            // Assert
            assertNotNull(dto, "DTO não deveria ser nulo");
            assertNull(dto.getNome(), "Nome deveria ser nulo");
            assertNull(dto.getDescricao(), "Descrição deveria ser nula");
        }

        @Test
        @DisplayName("Deve criar DTO com todos os argumentos")
        void deveCriarDTOComTodosOsArgumentos() {
            // Act
            ClassificacaoRequestDTO dto = new ClassificacaoRequestDTO(NOME_VALIDO, DESCRICAO_VALIDA);

            // Assert
            assertNotNull(dto, "DTO não deveria ser nulo");
            assertEquals(NOME_VALIDO, dto.getNome(), "Nome deveria ser igual ao informado");
            assertEquals(DESCRICAO_VALIDA, dto.getDescricao(), "Descrição deveria ser igual à informada");
        }
    }
}
