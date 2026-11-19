package br.com.territory.dominio;

import br.com.territory.excecoes.DadosInvalidosException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DadosTerritorioTest {
    @Test
    void deveCriarDadosTerritorioComLimitesValidos() {
        // Arrange
        double limiteChuva = 40.0;
        double limiteNivelAgua = 120.0;
        double limiteUmidade = 60.0;

        // Act
        DadosTerritorio dados = new DadosTerritorio(
                limiteChuva,
                limiteNivelAgua,
                limiteUmidade
        );

        // Assert
        assertEquals(40.0, dados.getLimiteChuvaMmHora(), 0.001);
        assertEquals(120.0, dados.getLimiteNivelAguaCm(), 0.001);
        assertEquals(60.0, dados.getLimiteUmidadeSoloPercentual(), 0.001);
    }

    @Test
    void deveAtualizarLimiteChuvaComValorValido() {
        // Arrange
        DadosTerritorio dados = new DadosTerritorio(40.0, 120.0, 60.0);

        // Act
        dados.setLimiteChuvaMmHora(50.0);

        // Assert
        assertEquals(50.0, dados.getLimiteChuvaMmHora(), 0.001);
    }

    @Test
    void deveLancarExcecaoQuandoLimiteChuvaForZero() {
        // Arrange
        DadosTerritorio dados = new DadosTerritorio(40.0, 120.0, 60.0);

        // Act e Assert
        assertThrows(
                DadosInvalidosException.class,
                () -> dados.setLimiteChuvaMmHora(0)
        );
    }

    @Test
    void deveLancarExcecaoQuandoLimiteNivelAguaForNegativo() {
        // Arrange
        DadosTerritorio dados = new DadosTerritorio(40.0, 120.0, 60.0);

        // Act e Assert
        assertThrows(
                DadosInvalidosException.class,
                () -> dados.setLimiteNivelAguaCm(-10)
        );
    }

    @Test
    void deveLancarExcecaoQuandoUmidadeUltrapassar100() {
        // Arrange
        DadosTerritorio dados = new DadosTerritorio(40.0, 120.0, 60.0);

        // Act e Assert
        assertThrows(
                DadosInvalidosException.class,
                () -> dados.setLimiteUmidadeSoloPercentual(101)
        );
    }

    @Test
    void deveLancarExcecaoQuandoUmidadeForZero() {
        // Arrange
        DadosTerritorio dados = new DadosTerritorio(40.0, 120.0, 60.0);

        // Act e Assert
        assertThrows(
                DadosInvalidosException.class,
                () -> dados.setLimiteUmidadeSoloPercentual(0)
        );
    }
}
