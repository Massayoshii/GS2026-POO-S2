
package br.com.territory.dominio;

import br.com.territory.excecoes.FonteIndisponivelException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SensoresTest {

    @Nested
    class PluviometroTest {

        @Test
        void deveRealizarLeituraValidaDoPluviometro() {
            // Arrange
            Pluviometro sensor = new Pluviometro(
                    1L, "Pluviometro Principal", "Area Monitorada", true
            );

            // Act
            double valor = sensor.realizarLeitura();

            // Assert
            assertTrue(valor >= 0 && valor <= 100);
        }

        @Test
        void deveIdentificarCorretamenteOPluviometro() {
            // Arrange
            Pluviometro sensor = new Pluviometro(
                    1L, "Pluviometro Principal", "Area Monitorada", true
            );

            // Assert
            assertEquals("PLUVIOMETRO", sensor.getTipoSensor());
            assertTrue(sensor.verificarFuncionamento());
        }

        @Test
        void deveLancarExcecaoQuandoPluviometroEstiverInativo() {
            // Arrange
            Pluviometro sensor = new Pluviometro(
                    1L, "Pluviometro Principal", "Area Monitorada", false
            );

            // Act e Assert
            assertFalse(sensor.verificarFuncionamento());

            assertThrows(
                    FonteIndisponivelException.class,
                    () -> sensor.realizarLeitura()
            );
        }
    }

    @Nested
    class NivelDaAguaTest {

        @Test
        void deveRealizarLeituraValidaDoNivelDaAgua() {
            // Arrange
            SensorNivelAgua sensor = new SensorNivelAgua(
                    2L, "Sensor de Nivel", "Area Monitorada", true
            );

            // Act
            double valor = sensor.realizarLeitura();

            // Assert
            assertTrue(valor >= 0 && valor <= 300);
        }

        @Test
        void deveIdentificarCorretamenteOSensorNivelAgua() {
            // Arrange
            SensorNivelAgua sensor = new SensorNivelAgua(
                    2L, "Sensor de Nivel", "Area Monitorada", true
            );

            // Assert
            assertEquals("NIVEL_AGUA", sensor.getTipoSensor());
            assertTrue(sensor.verificarFuncionamento());
        }

        @Test
        void deveLancarExcecaoQuandoSensorNivelAguaEstiverInativo() {
            // Arrange
            SensorNivelAgua sensor = new SensorNivelAgua(
                    2L, "Sensor de Nivel", "Area Monitorada", false
            );

            // Act e Assert
            assertFalse(sensor.verificarFuncionamento());

            assertThrows(
                    FonteIndisponivelException.class,
                    () -> sensor.realizarLeitura()
            );
        }
    }

    @Nested
    class UmidadeDoSolo {

        @Test
        void deveRealizarLeituraValidaDaUmidadeDoSolo() {
            // Arrange
            SensorUmidadeSolo sensor = new SensorUmidadeSolo(
                    3L, "Sensor de Umidade", "Area Monitorada", true
            );

            // Act
            double valor = sensor.realizarLeitura();

            // Assert
            assertTrue(valor >= 0 && valor <= 100);
        }

        @Test
        void deveIdentificarCorretamenteOSensorUmidadeSolo() {
            // Arrange
            SensorUmidadeSolo sensor = new SensorUmidadeSolo(
                    3L, "Sensor de Umidade", "Area Monitorada", true
            );

            // Assert
            assertEquals("UMIDADE_SOLO", sensor.getTipoSensor());
            assertTrue(sensor.verificarFuncionamento());
        }

        @Test
        void deveLancarExcecaoQuandoSensorUmidadeEstiverInativo() {
            // Arrange
            SensorUmidadeSolo sensor = new SensorUmidadeSolo(
                    3L, "Sensor de Umidade", "Area Monitorada", false
            );

            // Act e Assert
            assertFalse(sensor.verificarFuncionamento());

            assertThrows(
                    FonteIndisponivelException.class,
                    () -> sensor.realizarLeitura()
            );
        }
    }
}
