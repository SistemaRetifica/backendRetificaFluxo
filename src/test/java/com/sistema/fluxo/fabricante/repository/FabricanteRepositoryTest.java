package com.sistema.fluxo.fabricante.repository;

import com.sistema.fluxo.fabricante.model.Fabricante;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("null")
@DataJpaTest
@DisplayName("FabricanteRepository - Testes de Integração")
class FabricanteRepositoryTest {

    @Autowired
    private FabricanteRepository repository;

    @Autowired
    private TestEntityManager entityManager;

    private Fabricante fabricante;

    @BeforeEach
    void setUp() {
        fabricante = new Fabricante();
        fabricante.setNome("MWM");
        entityManager.persistAndFlush(fabricante);
    }

    // ========================= findByNome =========================

    @Nested
    @DisplayName("findByNome()")
    class FindByNome {

        @Test
        @DisplayName("Deve retornar fabricante quando nome existe")
        void findByNome_existente_deveRetornarFabricante() {
            // Act
            Optional<Fabricante> resultado = repository.findByNome("MWM");

            // Assert
            assertThat(resultado).isPresent();
            assertThat(resultado.get().getNome()).isEqualTo("MWM");
        }

        @Test
        @DisplayName("Deve retornar vazio quando nome não existe")
        void findByNome_naoExistente_deveRetornarVazio() {
            // Act
            Optional<Fabricante> resultado = repository.findByNome("Inexistente");

            // Assert
            assertThat(resultado).isEmpty();
        }

        @Test
        @DisplayName("Deve diferenciar maiúsculas e minúsculas")
        void findByNome_caseSensitive_deveRetornarVazio() {
            // Act
            Optional<Fabricante> resultado = repository.findByNome("mwm");

            // Assert
            assertThat(resultado).isEmpty();
        }
    }

    // ========================= existsByNome =========================

    @Nested
    @DisplayName("existsByNome()")
    class ExistsByNome {

        @Test
        @DisplayName("Deve retornar true quando nome existe")
        void existsByNome_existente_deveRetornarTrue() {
            // Act
            boolean resultado = repository.existsByNome("MWM");

            // Assert
            assertThat(resultado).isTrue();
        }

        @Test
        @DisplayName("Deve retornar false quando nome não existe")
        void existsByNome_naoExistente_deveRetornarFalse() {
            // Act
            boolean resultado = repository.existsByNome("Inexistente");

            // Assert
            assertThat(resultado).isFalse();
        }
    }

    // ========================= Operações CRUD Herdadas =========================

    @Nested
    @DisplayName("Operações CRUD")
    class OperacoesCrud {

        @Test
        @DisplayName("Deve salvar fabricante corretamente")
        void save_devePersistirFabricante() {
            // Arrange
            Fabricante novo = new Fabricante();
            novo.setNome("Cummins");

            // Act
            Fabricante salvo = repository.save(novo);

            // Assert
            assertThat(salvo.getId()).isNotNull();
            assertThat(salvo.getNome()).isEqualTo("Cummins");

            Fabricante encontrado = entityManager.find(Fabricante.class, salvo.getId());
            assertThat(encontrado).isNotNull();
            assertThat(encontrado.getNome()).isEqualTo("Cummins");
        }

        @Test
        @DisplayName("Deve buscar fabricante por ID")
        void findById_deveRetornarFabricante() {
            // Act
            Optional<Fabricante> resultado = repository.findById(fabricante.getId());

            // Assert
            assertThat(resultado).isPresent();
            assertThat(resultado.get().getNome()).isEqualTo("MWM");
        }

        @Test
        @DisplayName("Deve atualizar fabricante corretamente")
        void update_deveAtualizarFabricante() {
            // Arrange
            fabricante.setNome("MWM Atualizado");

            // Act
            repository.save(fabricante);
            entityManager.flush();
            entityManager.clear();

            // Assert
            Fabricante atualizado = entityManager.find(Fabricante.class, fabricante.getId());
            assertThat(atualizado.getNome()).isEqualTo("MWM Atualizado");
        }

        @Test
        @DisplayName("Deve deletar fabricante corretamente")
        void delete_deveRemoverFabricante() {
            // Arrange
            Long id = fabricante.getId();

            // Act
            repository.deleteById(id);
            entityManager.flush();

            // Assert
            Fabricante deletado = entityManager.find(Fabricante.class, id);
            assertThat(deletado).isNull();
        }

        @Test
        @DisplayName("Deve listar todos os fabricantes")
        void findAll_deveRetornarTodos() {
            // Arrange
            Fabricante fab2 = new Fabricante();
            fab2.setNome("Cummins");
            entityManager.persistAndFlush(fab2);

            // Act
            var resultado = repository.findAll();

            // Assert
            assertThat(resultado).hasSize(2);
            assertThat(resultado).extracting(Fabricante::getNome)
                .containsExactlyInAnyOrder("MWM", "Cummins");
        }

        @Test
        @DisplayName("Deve verificar existência por ID")
        void existsById_deveRetornarCorreto() {
            // Act & Assert
            assertThat(repository.existsById(fabricante.getId())).isTrue();
            assertThat(repository.existsById(999L)).isFalse();
        }
    }
}
