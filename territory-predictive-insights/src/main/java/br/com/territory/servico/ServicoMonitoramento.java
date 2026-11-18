package br.com.territory.servico;

import br.com.territory.dominio.Alerta;
import br.com.territory.dominio.DadosTerritorio;
import br.com.territory.dominio.Medicao;
import br.com.territory.dominio.NivelAlerta;
import br.com.territory.dominio.Pluviometro;
import br.com.territory.dominio.Sensor;
import br.com.territory.dominio.SensorNivelAgua;
import br.com.territory.dominio.SensorUmidadeSolo;

import br.com.territory.estrategia.EstrategiaPrevisao;
import br.com.territory.estrategia.FabricaPrevisao;
import br.com.territory.estrategia.ResultadoPrevisao;
import br.com.territory.estrategia.TipoPrevisao;

import br.com.territory.excecoes.DadosInvalidosException;

import br.com.territory.repository.AlertaRepository;
import br.com.territory.repository.MedicaoRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServicoMonitoramento {
    private final MedicaoRepository medicaoRepository;
    private final AlertaRepository alertaRepository;
    private final List<Sensor> sensores;
    private final DadosTerritorio dadosTerritorio;

    public ServicoMonitoramento(MedicaoRepository medicaoRepository, AlertaRepository alertaRepository) {
        this.medicaoRepository = medicaoRepository;
        this.alertaRepository = alertaRepository;
        this.sensores = List.of(
                new Pluviometro(1L, "Pluviometro Principal", "Area Monitorada", true),
                new SensorNivelAgua(2L, "Sensor de Nivel", "Area Monitorada", true),
                new SensorUmidadeSolo(3L, "Sensor de Umidade", "Area Monitorada", true)
        );
        this.dadosTerritorio = new DadosTerritorio(40.0, 120.0, 60.0);
    }

    public void verificarSensores() {
        for (Sensor sensor : sensores) {
            if (sensor.verificarFuncionamento()) {
                System.out.println(
                        sensor.getTipoSensor() + " esta funcionando"
                );
            } else {
                System.out.println(
                        sensor.getTipoSensor() + " esta indisponivel"
                );
            }
        }
    }

    public void registrarMedicoes() {
        for (Sensor sensor : sensores) {

            double valor = sensor.realizarLeitura();

            String unidade = obterUnidade(sensor.getTipoSensor());

            Medicao medicao = new Medicao(
                    sensor.getTipoSensor(),
                    valor,
                    unidade
            );

            medicaoRepository.save(medicao);

            System.out.printf(
                    "Medicao salva: %s = %.1f %s%n",
                    sensor.getTipoSensor(),
                    valor,
                    unidade
            );
        }
    }

    public List<Medicao> consultarHistorico() {
        return medicaoRepository.findAllByOrderByDataHoraDesc();
    }

    public List<Medicao> consultarHistorico(String tipoSensor) {
        if (tipoSensor == null || tipoSensor.isBlank()) {
            throw new DadosInvalidosException(
                    "O tipo do sensor deve ser informado"
            );
        }

        String tipoNormalizado = tipoSensor.trim().toUpperCase();

        if (!List.of("PLUVIOMETRO", "NIVEL_AGUA", "UMIDADE_SOLO")
                .contains(tipoNormalizado)) {
            throw new DadosInvalidosException(
                    "Tipo de sensor nao reconhecido"
            );
        }
        return medicaoRepository.findByTipoSensorOrderByDataHoraDesc(tipoNormalizado);
    }

    public ResultadoPrevisao executarPrevisao(String tipoSensor, TipoPrevisao tipoPrevisao) {
        List<Medicao> medicoes = consultarHistorico(tipoSensor);

        EstrategiaPrevisao estrategia = FabricaPrevisao.criar(tipoPrevisao);

        double valorPrevisto = estrategia.prever(medicoes);

        return new ResultadoPrevisao(
                tipoSensor.trim().toUpperCase(),
                valorPrevisto,
                estrategia.getNomeEstrategia()
        );
    }

    public Alerta verificarPrevisaoEGerarAlerta(String tipoSensor, TipoPrevisao tipoPrevisao) {
        ResultadoPrevisao resultado =
                executarPrevisao(tipoSensor, tipoPrevisao);

        double limite = obterLimite(resultado.getTipoSensor());

        double valorPrevisto = resultado.getValorPrevisto();

        if (valorPrevisto < limite) {
            System.out.println(
                    "Previsao abaixo do limite. Nenhum alerta foi gerado"
            );
            return null;
        }

        NivelAlerta nivel = determinarNivelAlerta(
                valorPrevisto,
                limite
        );

        String unidade = obterUnidade(resultado.getTipoSensor());

        String motivo = String.format(
                "Previsao de %.1f %s atingiu ou ultrapassou o limite de %.1f %s",
                valorPrevisto,
                unidade,
                limite,
                unidade
        );
        Alerta alerta = new Alerta(
                resultado.getTipoSensor(),
                valorPrevisto,
                limite,
                nivel,
                motivo
        );
        Alerta alertaSalvo = alertaRepository.save(alerta);

        System.out.println(
                "Alerta " + nivel + " gerado e salvo com sucesso"
        );
        return alertaSalvo;
    }

    private double obterLimite(String tipoSensor) {
        return switch (tipoSensor) {
            case "PLUVIOMETRO" ->
                    dadosTerritorio.getLimiteChuvaMmHora();
            case "NIVEL_AGUA" ->
                    dadosTerritorio.getLimiteNivelAguaCm();
            case "UMIDADE_SOLO" ->
                    dadosTerritorio.getLimiteUmidadeSoloPercentual();
            default -> throw new DadosInvalidosException(
                    "Tipo de sensor nao reconhecido"
            );
        };
    }

    private NivelAlerta determinarNivelAlerta(
            double valorPrevisto,
            double limite) {
        if (valorPrevisto >= limite * 1.5) {
            return NivelAlerta.CRITICO;
        }
        if (valorPrevisto >= limite * 1.2) {
            return NivelAlerta.ALERTA;
        }
        return NivelAlerta.ATENCAO;
    }

    private String obterUnidade(String tipoSensor) {
        return switch (tipoSensor) {
            case "PLUVIOMETRO" -> "mm/h";
            case "NIVEL_AGUA" -> "cm";
            case "UMIDADE_SOLO" -> "%";
            default -> throw new DadosInvalidosException(
                    "Tipo de sensor nao reconhecido"
            );
        };
    }
}