package com.sistema.fluxo.fabricante.service;

import com.sistema.fluxo.exception.ResourceConflictException;
import com.sistema.fluxo.exception.ResourceNotFoundException;
import com.sistema.fluxo.fabricante.dto.FabricanteRequestDTO;
import com.sistema.fluxo.fabricante.dto.FabricanteResponseDTO;
import com.sistema.fluxo.fabricante.model.Fabricante;
import com.sistema.fluxo.fabricante.repository.FabricanteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SuppressWarnings("null")
@ExtendWith(MockitoExtension.class)
@DisplayName("FabricanteService - Testes Unitários")
class FabricanteServiceTest {

    @Mock
    private FabricanteRepository repository;

    @InjectMocks
    private FabricanteService service;

    private Fabricante fabricante;
    private FabricanteRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        fabricante = new Fabricante();
        fabricante.setId(1L);
        fabricante.setNome("MWM");

        requestDTO = new FabricanteRequestDTO("MWM");
    }

    // ========================= LISTAGEM =========================

    @Nested
    @DisplayName("findAll()")
    class FindAll {

        @Test
        @DisplayName("Deve retornar lista de fabricantes")
        void findAll_comRegistros_deveRetornarLista() {
            // Arrange
            Fabricante fab2 = new Fabricante();
            fab2.setId(2L);
            fab2.setNome("Cummins");

            when(repository.findAll()).thenReturn(List.of(fabricante, fab2));

            // Act
            List<FabricanteResponseDTO> resultado = service.findAll();

            // Assert
            assertThat(resultado).hasSize(2);
            assertThat(resultado.get(0).getNome()).isEqualTo("MWM");
            assertThat(resultado.get(1).getNome()).isEqualTo("Cummins");
            verify(repository).findAll();
        }

        @Test
        @DisplayName("Deve retornar lista vazia quando não houver registros")
        void findAll_semRegistros_deveRetornarListaVazia() {
            // Arrange
            when(repository.findAll()).thenReturn(List.of());

            // Act
            List<FabricanteResponseDTO> resultado = service.findAll();

            // Assert
            assertThat(resultado).isEmpty();
            verify(repository).findAll();
        }
    }

    // ========================= BUSCA POR ID =========================

    @Nested
    @DisplayName("findById()")
    class FindById {

        @Test
        @DisplayName("Deve retornar fabricante quando encontrado")
        void findById_existente_deveRetornarFabricante() {
            // Arrange
            when(repository.findById(1L)).thenReturn(Optional.of(fabricante));

            // Act
            FabricanteResponseDTO resultado = service.findById(1L);

            // Assert
            assertThat(resultado.getId()).isEqualTo(1L);
            assertThat(resultado.getNome()).isEqualTo("MWM");
            verify(repository).findById(1L);
        }

        @Test
        @DisplayName("Deve lançar ResourceNotFoundException quando não encontrado")
        void findById_naoExistente_deveLancarException() {
            // Arrange
            when(repository.findById(999L)).thenReturn(Optional.empty());

            // Act & Assert
            assertThatThrownBy(() -> service.findById(999L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Fabricante não encontrado com ID: 999");

            verify(repository).findById(999L);
        }
    }

    // ========================= BUSCA POR NOME =========================

    @Nested
    @DisplayName("findByNome()")
    class FindByNome {

        @Test
        @DisplayName("Deve retornar fabricante quando encontrado por nome")
        void findByNome_existente_deveRetornarFabricante() {
            // Arrange
            when(repository.findByNome("MWM")).thenReturn(Optional.of(fabricante));

            // Act
            FabricanteResponseDTO resultado = service.findByNome("MWM");

            // Assert
            assertThat(resultado.getId()).isEqualTo(1L);
            assertThat(resultado.getNome()).isEqualTo("MWM");
            verify(repository).findByNome("MWM");
        }

        @Test
        @DisplayName("Deve lançar ResourceNotFoundException quando não encontrado por nome")
        void findByNome_naoExistente_deveLancarException() {
            // Arrange
            when(repository.findByNome("Inexistente")).thenReturn(Optional.empty());

            // Act & Assert
            assertThatThrownBy(() -> service.findByNome("Inexistente"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Fabricante não encontrado com nome: Inexistente");

            verify(repository).findByNome("Inexistente");
        }
    }

    // ========================= CRIAÇÃO =========================

    @Nested
    @DisplayName("save()")
    class Save {

        @Test
        @DisplayName("Deve criar fabricante com sucesso")
        void save_comDadosValidos_deveCriarFabricante() {
            // Arrange
            when(repository.existsByNome("MWM")).thenReturn(false);
            when(repository.save(any(Fabricante.class))).thenReturn(fabricante);

            // Act
            FabricanteResponseDTO resultado = service.save(requestDTO);

            // Assert
            assertThat(resultado.getId()).isEqualTo(1L);
            assertThat(resultado.getNome()).isEqualTo("MWM");
            verify(repository).existsByNome("MWM");
            verify(repository).save(any(Fabricante.class));
        }

        @Test
        @DisplayName("Deve lançar ResourceConflictException quando nome já existe")
        void save_comNomeDuplicado_deveLancarException() {
            // Arrange
            when(repository.existsByNome("MWM")).thenReturn(true);

            // Act & Assert
            assertThatThrownBy(() -> service.save(requestDTO))
                .isInstanceOf(ResourceConflictException.class)
                .hasMessageContaining("Já existe um fabricante com o nome: MWM");

            verify(repository).existsByNome("MWM");
            verify(repository, never()).save(any(Fabricante.class));
        }
    }

    // ========================= ATUALIZAÇÃO =========================

    @Nested
    @DisplayName("update()")
    class Update {

        @Test
        @DisplayName("Deve atualizar fabricante com sucesso")
        void update_comDadosValidos_deveAtualizarFabricante() {
            // Arrange
            FabricanteRequestDTO updateRequest = new FabricanteRequestDTO("MWM Atualizado");
            Fabricante updated = new Fabricante();
            updated.setId(1L);
            updated.setNome("MWM Atualizado");

            when(repository.findById(1L)).thenReturn(Optional.of(fabricante));
            when(repository.existsByNome("MWM Atualizado")).thenReturn(false);
            when(repository.save(any(Fabricante.class))).thenReturn(updated);

            // Act
            FabricanteResponseDTO resultado = service.update(1L, updateRequest);

            // Assert
            assertThat(resultado.getId()).isEqualTo(1L);
            assertThat(resultado.getNome()).isEqualTo("MWM Atualizado");
            verify(repository).findById(1L);
            verify(repository).existsByNome("MWM Atualizado");
            verify(repository).save(any(Fabricante.class));
        }

        @Test
        @DisplayName("Deve permitir atualizar mantendo o mesmo nome")
        void update_mantendoMesmoNome_deveAtualizarSemVerificarDuplicidade() {
            // Arrange
            FabricanteRequestDTO sameNameRequest = new FabricanteRequestDTO("MWM");

            when(repository.findById(1L)).thenReturn(Optional.of(fabricante));
            when(repository.save(any(Fabricante.class))).thenReturn(fabricante);

            // Act
            FabricanteResponseDTO resultado = service.update(1L, sameNameRequest);

            // Assert
            assertThat(resultado.getNome()).isEqualTo("MWM");
            verify(repository).findById(1L);
            verify(repository, never()).existsByNome(anyString());
            verify(repository).save(any(Fabricante.class));
        }

        @Test
        @DisplayName("Deve lançar ResourceNotFoundException quando fabricante não existe")
        void update_naoExistente_deveLancarException() {
            // Arrange
            when(repository.findById(999L)).thenReturn(Optional.empty());

            // Act & Assert
            assertThatThrownBy(() -> service.update(999L, requestDTO))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Fabricante não encontrado com ID: 999");

            verify(repository).findById(999L);
            verify(repository, never()).save(any(Fabricante.class));
        }

        @Test
        @DisplayName("Deve lançar ResourceConflictException quando novo nome já existe")
        void update_comNomeDuplicado_deveLancarException() {
            // Arrange
            FabricanteRequestDTO duplicateRequest = new FabricanteRequestDTO("Cummins");

            when(repository.findById(1L)).thenReturn(Optional.of(fabricante));
            when(repository.existsByNome("Cummins")).thenReturn(true);

            // Act & Assert
            assertThatThrownBy(() -> service.update(1L, duplicateRequest))
                .isInstanceOf(ResourceConflictException.class)
                .hasMessageContaining("Já existe um fabricante com o nome: Cummins");

            verify(repository).findById(1L);
            verify(repository).existsByNome("Cummins");
            verify(repository, never()).save(any(Fabricante.class));
        }
    }

    // ========================= DELEÇÃO =========================

    @Nested
    @DisplayName("delete()")
    class Delete {

        @Test
        @DisplayName("Deve deletar fabricante com sucesso")
        void delete_existente_deveDeletarFabricante() {
            // Arrange
            when(repository.existsById(1L)).thenReturn(true);
            doNothing().when(repository).deleteById(1L);

            // Act
            service.delete(1L);

            // Assert
            verify(repository).existsById(1L);
            verify(repository).deleteById(1L);
        }

        @Test
        @DisplayName("Deve lançar ResourceNotFoundException quando fabricante não existe")
        void delete_naoExistente_deveLancarException() {
            // Arrange
            when(repository.existsById(999L)).thenReturn(false);

            // Act & Assert
            assertThatThrownBy(() -> service.delete(999L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Fabricante não encontrado com ID: 999");

            verify(repository).existsById(999L);
            verify(repository, never()).deleteById(anyLong());
        }
    }
}
