package br.com.territory.dominio;

import br.com.territory.excecoes.DadosInvalidosException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MedicaoTest {

    @Test
    @DisplayName("Deve aceitar medicao valida do pluviometro")
    void deveAceitarMedicaoValidaPluviometro() {
        Medicao medicao = new Medicao(
                "PLUVIOMETRO", 100.0, "mm/h"
        );

        assertEquals(100.0, medicao.getValor(), 0.001);
    }

    @Test
    @DisplayName("Deve rejeitar chuva acima de 100 mm/h")
    void deveRejeitarChuvaAcimaDoLimite() {
        assertThrows(
                DadosInvalidosException.class,
                () -> new Medicao("PLUVIOMETRO", 100.1, "mm/h")
        );
    }

    @Test
    @DisplayName("Deve aceitar nivel da agua dentro da faixa")
    void deveAceitarNivelAguaValido() {
        Medicao medicao = new Medicao(
                "NIVEL_AGUA", 300.0, "cm"
        );

        assertEquals(300.0, medicao.getValor(), 0.001);
    }

    @Test
    @DisplayName("Deve rejeitar nivel da agua acima de 300 cm")
    void deveRejeitarNivelAguaAcimaDoLimite() {
        assertThrows(
                DadosInvalidosException.class,
                () -> new Medicao("NIVEL_AGUA", 300.1, "cm")
        );
    }

    @Test
    @DisplayName("Deve aceitar umidade do solo dentro da faixa")
    void deveAceitarUmidadeValida() {
        Medicao medicao = new Medicao(
                "UMIDADE_SOLO", 100.0, "%"
        );

        assertEquals(100.0, medicao.getValor(), 0.001);
    }

    @Test
    @DisplayName("Deve rejeitar umidade acima de 100 por cento")
    void deveRejeitarUmidadeAcimaDoLimite() {
        assertThrows(
                DadosInvalidosException.class,
                () -> new Medicao("UMIDADE_SOLO", 100.1, "%")
        );
    }

    @Test
    @DisplayName("Deve rejeitar tipo de sensor desconhecido")
    void deveRejeitarTipoDeSensorDesconhecido() {
        assertThrows(
                DadosInvalidosException.class,
                () -> new Medicao("SENSOR_DESCONHECIDO", 50.0, "un")
        );
    }
}