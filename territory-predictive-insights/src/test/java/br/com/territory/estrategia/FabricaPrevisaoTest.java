package br.com.territory.estrategia;

import br.com.territory.excecoes.DadosInvalidosException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FabricaPrevisaoTest {
    @Test
    @DisplayName("Deve criar estrategia de media movel")
    void deveCriarEstrategiaMediaMovel() {
        EstrategiaPrevisao estrategia =
                FabricaPrevisao.criar(TipoPrevisao.MEDIA_MOVEL);

        assertInstanceOf(
                PrevisaoMediaMovel.class,
                estrategia
        );
    }

    @Test
    @DisplayName("Deve criar estrategia de comparacao")
    void deveCriarEstrategiaComparacao() {
        EstrategiaPrevisao estrategia =
                FabricaPrevisao.criar(TipoPrevisao.COMPARACAO);

        assertInstanceOf(
                PrevisaoComparacao.class,
                estrategia
        );
    }

    @Test
    @DisplayName("Deve lancar excecao quando o tipo for nulo")
    void deveLancarExcecaoQuandoTipoForNulo() {
        assertThrows(
                DadosInvalidosException.class,
                () -> FabricaPrevisao.criar(null)
        );
    }
}