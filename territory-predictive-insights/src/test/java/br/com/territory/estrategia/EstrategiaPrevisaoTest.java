package br.com.territory.estrategia;

import br.com.territory.dominio.Medicao;
import br.com.territory.excecoes.DadosInvalidosException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EstrategiasPrevisaoTest {

    @Test
    @DisplayName("Deve calcular corretamente a media movel")
    void deveCalcularMediaMovel() {
        List<Medicao> medicoes = List.of(
                new Medicao("PLUVIOMETRO", 80.0, "mm/h"),
                new Medicao("PLUVIOMETRO", 70.0, "mm/h"),
                new Medicao("PLUVIOMETRO", 60.0, "mm/h"),
                new Medicao("PLUVIOMETRO", 50.0, "mm/h"),
                new Medicao("PLUVIOMETRO", 40.0, "mm/h")
        );

        PrevisaoMediaMovel estrategia = new PrevisaoMediaMovel();

        double resultado = estrategia.prever(medicoes);

        assertEquals(60.0, resultado, 0.001);
    }

    @Test
    @DisplayName("Deve calcular media movel com menos de cinco medicoes")
    void deveCalcularMediaComMenosDeCincoMedicoes() {
        List<Medicao> medicoes = List.of(
                new Medicao("PLUVIOMETRO", 20.0, "mm/h"),
                new Medicao("PLUVIOMETRO", 40.0, "mm/h"),
                new Medicao("PLUVIOMETRO", 60.0, "mm/h")
        );

        PrevisaoMediaMovel estrategia = new PrevisaoMediaMovel();

        double resultado = estrategia.prever(medicoes);

        assertEquals(40.0, resultado, 0.001);
    }

    @Test
    @DisplayName("Deve lancar excecao quando nao existirem medicoes")
    void deveLancarExcecaoQuandoNaoExistiremMedicoes() {
        PrevisaoMediaMovel estrategia = new PrevisaoMediaMovel();

        assertThrows(
                DadosInvalidosException.class,
                () -> estrategia.prever(List.of())
        );
    }

    @Test
    @DisplayName("Deve projetar aumento pela estrategia de comparacao")
    void deveProjetarAumentoPelaComparacao() {
        List<Medicao> medicoes = List.of(
                new Medicao("NIVEL_AGUA", 80.0, "cm"),
                new Medicao("NIVEL_AGUA", 60.0, "cm")
        );

        PrevisaoComparacao estrategia = new PrevisaoComparacao();

        double resultado = estrategia.prever(medicoes);

        assertEquals(100.0, resultado, 0.001);
    }

    @Test
    @DisplayName("Deve impedir previsao negativa na comparacao")
    void deveImpedirPrevisaoNegativa() {
        List<Medicao> medicoes = List.of(
                new Medicao("NIVEL_AGUA", 20.0, "cm"),
                new Medicao("NIVEL_AGUA", 60.0, "cm")
        );

        PrevisaoComparacao estrategia = new PrevisaoComparacao();

        double resultado = estrategia.prever(medicoes);

        assertEquals(0.0, resultado, 0.001);
    }

    @Test
    @DisplayName("Deve lancar excecao quando existirem menos de duas medicoes")
    void deveLancarExcecaoComMenosDeDuasMedicoes() {
        List<Medicao> medicoes = List.of(
                new Medicao("NIVEL_AGUA", 80.0, "cm")
        );

        PrevisaoComparacao estrategia = new PrevisaoComparacao();

        assertThrows(
                DadosInvalidosException.class,
                () -> estrategia.prever(medicoes)
        );
    }
}
