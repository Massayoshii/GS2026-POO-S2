package br.com.territory.servico;

import br.com.territory.dominio.Alerta;
import br.com.territory.dominio.Medicao;
import br.com.territory.dominio.NivelAlerta;

import br.com.territory.estrategia.ResultadoPrevisao;
import br.com.territory.estrategia.TipoPrevisao;

import br.com.territory.excecoes.DadosInvalidosException;

import br.com.territory.repository.AlertaRepository;
import br.com.territory.repository.MedicaoRepository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ServicoMonitoramentoTest {

    @Mock
    private MedicaoRepository medicaoRepository;

    @Mock
    private AlertaRepository alertaRepository;

    @InjectMocks
    private ServicoMonitoramento servicoMonitoramento;

    @Test
    @DisplayName("Deve registrar as tres medicoes dos sensores")
    void deveRegistrarTresMedicoes() {

        servicoMonitoramento.registrarMedicoes();

        ArgumentCaptor<Medicao> captor =
                ArgumentCaptor.forClass(Medicao.class);

        verify(medicaoRepository, times(3))
                .save(captor.capture());

        List<Medicao> medicoes = captor.getAllValues();

        assertEquals(3, medicoes.size());

        assertTrue(medicoes.stream().anyMatch(
                m -> m.getTipoSensor().equals("PLUVIOMETRO")
        ));

        assertTrue(medicoes.stream().anyMatch(
                m -> m.getTipoSensor().equals("NIVEL_AGUA")
        ));

        assertTrue(medicoes.stream().anyMatch(
                m -> m.getTipoSensor().equals("UMIDADE_SOLO")
        ));
    }

    @Test
    @DisplayName("Deve consultar todo o historico de medicoes")
    void deveConsultarHistoricoCompleto() {
        List<Medicao> medicoes = List.of(
                new Medicao("PLUVIOMETRO", 45.0, "mm/h"),
                new Medicao("NIVEL_AGUA", 120.0, "cm")
        );

        when(medicaoRepository.findAllByOrderByDataHoraDesc())
                .thenReturn(medicoes);

        List<Medicao> resultado =
                servicoMonitoramento.consultarHistorico();

        assertEquals(2, resultado.size());
        assertEquals(medicoes, resultado);

        verify(medicaoRepository)
                .findAllByOrderByDataHoraDesc();
    }

    @Test
    @DisplayName("Deve consultar historico de um sensor especifico")
    void deveConsultarHistoricoPorSensor() {
        List<Medicao> medicoes = List.of(
                new Medicao("PLUVIOMETRO", 45.0, "mm/h"),
                new Medicao("PLUVIOMETRO", 50.0, "mm/h")
        );

        when(medicaoRepository
                .findByTipoSensorOrderByDataHoraDesc("PLUVIOMETRO"))
                .thenReturn(medicoes);

        List<Medicao> resultado =
                servicoMonitoramento.consultarHistorico("PLUVIOMETRO");

        assertEquals(2, resultado.size());

        verify(medicaoRepository)
                .findByTipoSensorOrderByDataHoraDesc("PLUVIOMETRO");
    }

    @Test
    @DisplayName("Deve lancar excecao para tipo de sensor invalido")
    void deveLancarExcecaoParaSensorInvalido() {
        assertThrows(
                DadosInvalidosException.class,
                () -> servicoMonitoramento.consultarHistorico("DESCONHECIDO")
        );

        verifyNoInteractions(medicaoRepository);
    }

    @Test
    @DisplayName("Deve executar previsao utilizando media movel")
    void deveExecutarPrevisaoMediaMovel() {
        List<Medicao> medicoes = List.of(
                new Medicao("PLUVIOMETRO", 80.0, "mm/h"),
                new Medicao("PLUVIOMETRO", 70.0, "mm/h"),
                new Medicao("PLUVIOMETRO", 60.0, "mm/h"),
                new Medicao("PLUVIOMETRO", 50.0, "mm/h"),
                new Medicao("PLUVIOMETRO", 40.0, "mm/h")
        );

        when(medicaoRepository
                .findByTipoSensorOrderByDataHoraDesc("PLUVIOMETRO"))
                .thenReturn(medicoes);

        ResultadoPrevisao resultado =
                servicoMonitoramento.executarPrevisao(
                        "PLUVIOMETRO",
                        TipoPrevisao.MEDIA_MOVEL
                );

        assertEquals(60.0, resultado.getValorPrevisto(), 0.001);
        assertEquals("PLUVIOMETRO", resultado.getTipoSensor());
        assertEquals("Media Movel", resultado.getNomeEstrategia());
    }

    @Test
    @DisplayName("Deve executar previsao utilizando comparacao")
    void deveExecutarPrevisaoComparacao() {
        List<Medicao> medicoes = List.of(
                new Medicao("NIVEL_AGUA", 80.0, "cm"),
                new Medicao("NIVEL_AGUA", 60.0, "cm")
        );

        when(medicaoRepository
                .findByTipoSensorOrderByDataHoraDesc("NIVEL_AGUA"))
                .thenReturn(medicoes);

        ResultadoPrevisao resultado =
                servicoMonitoramento.executarPrevisao(
                        "NIVEL_AGUA",
                        TipoPrevisao.COMPARACAO
                );

        assertEquals(100.0, resultado.getValorPrevisto(), 0.001);
        assertEquals("NIVEL_AGUA", resultado.getTipoSensor());
        assertEquals("Comparacao", resultado.getNomeEstrategia());
    }

    @Test
    @DisplayName("Deve gerar e salvar alerta critico")
    void deveGerarAlertaCritico() {
        ResultadoPrevisao resultado = new ResultadoPrevisao(
                "PLUVIOMETRO",
                60.0,
                "Media Movel"
        );

        when(alertaRepository.save(any(Alerta.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Alerta alerta =
                servicoMonitoramento.verificarPrevisaoEGerarAlerta(
                        resultado
                );

        assertNotNull(alerta);
        assertEquals(NivelAlerta.CRITICO, alerta.getNivel());
        assertEquals("PLUVIOMETRO", alerta.getTipoSensor());
        assertEquals(60.0, alerta.getValorPrevisto(), 0.001);
        assertEquals(40.0, alerta.getLimiteUltrapassado(), 0.001);

        verify(alertaRepository).save(any(Alerta.class));
    }

    @Test
    @DisplayName("Nao deve gerar alerta quando previsao estiver abaixo do limite")
    void naoDeveGerarAlertaAbaixoDoLimite() {
        ResultadoPrevisao resultado = new ResultadoPrevisao(
                "PLUVIOMETRO",
                30.0,
                "Media Movel"
        );

        Alerta alerta =
                servicoMonitoramento.verificarPrevisaoEGerarAlerta(
                        resultado
                );

        assertNull(alerta);

        verify(alertaRepository, never()).save(any(Alerta.class));
    }

    @Test
    @DisplayName("Deve atualizar corretamente o limite de chuva")
    void deveAtualizarLimiteChuva() {
        servicoMonitoramento.atualizarLimiteChuva(50.0);

        assertEquals(
                50.0,
                servicoMonitoramento.consultarLimites()
                        .getLimiteChuvaMmHora(),
                0.001
        );
    }
}
