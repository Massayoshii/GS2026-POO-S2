package br.com.territory.dominio;

import br.com.territory.excecoes.DadosInvalidosException;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Locale;

@Entity
@Table(name = "medicoes")
public class Medicao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tipo_sensor", nullable = false)
    private String tipoSensor;

    @Column(nullable = false)
    private double valor;

    @Column(nullable = false)
    private String unidade;

    @Column(name = "data_hora", nullable = false)
    private LocalDateTime dataHora;

    protected Medicao() {
    }

    public Medicao(String tipoSensor, double valor, String unidade) {
        setTipoSensor(tipoSensor);
        setValor(valor);
        setUnidade(unidade);
        this.dataHora = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public String getTipoSensor() {
        return tipoSensor;
    }

    public void setTipoSensor(String tipoSensor) {
        if (tipoSensor == null || tipoSensor.isBlank()) {
            throw new DadosInvalidosException(
                    "O tipo do sensor deve ser informado"
            );
        }

        String tipoNormalizado = tipoSensor.trim().toUpperCase(Locale.ROOT);

        validarTipoSensor(tipoNormalizado);
        validarValorPorTipoSensor(valor, tipoNormalizado);

        this.tipoSensor = tipoNormalizado;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        if (!Double.isFinite(valor) || valor < 0) {
            throw new DadosInvalidosException(
                    "O valor da medicao nao pode ser negativo ou invalido"
            );
        }

        if (tipoSensor != null) {
            validarValorPorTipoSensor(valor, tipoSensor);
        }

        this.valor = valor;
    }

    public String getUnidade() {
        return unidade;
    }

    public void setUnidade(String unidade) {
        if (unidade == null || unidade.isBlank()) {
            throw new DadosInvalidosException(
                    "A unidade da medicao deve ser informada"
            );
        }
        this.unidade = unidade;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    private void validarTipoSensor(String tipoSensor) {
        if (!tipoSensor.equals("PLUVIOMETRO") && !tipoSensor.equals("NIVEL_AGUA") && !tipoSensor.equals("UMIDADE_SOLO")) {
            throw new DadosInvalidosException(
                    "Tipo de sensor nao reconhecido"
            );
        }
    }

    private void validarValorPorTipoSensor(double valor, String tipoSensor) {
        double limiteMaximo = switch (tipoSensor) {

            case "PLUVIOMETRO" -> 100.0;

            case "NIVEL_AGUA" -> 300.0;

            case "UMIDADE_SOLO" -> 100.0;

            default -> throw new DadosInvalidosException(
                    "Tipo de sensor nao reconhecido"
            );
        };

        if (!Double.isFinite(valor) || valor < 0 || valor > limiteMaximo) {
            throw new DadosInvalidosException(
                    "Valor invalido para o sensor " + tipoSensor
                            + ", faixa permitida de 0 a " + limiteMaximo
            );
        }
    }
}
