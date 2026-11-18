package br.com.territory.dominio;

import br.com.territory.excecoes.DadosInvalidosException;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "alertas")
public class Alerta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tipo_sensor", nullable = false)
    private String tipoSensor;

    @Column(name = "valor_previsto", nullable = false)
    private double valorPrevisto;

    @Column(name = "limite_ultrapassado", nullable = false)
    private double limiteUltrapassado;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NivelAlerta nivel;

    @Column(nullable = false)
    private String motivo;

    @Column(name = "data_hora", nullable = false)
    private LocalDateTime dataHora;

    protected Alerta() {
    }

    public Alerta(String tipoSensor,
                  double valorPrevisto,
                  double limiteUltrapassado,
                  NivelAlerta nivel,
                  String motivo) {

        setTipoSensor(tipoSensor);
        setValorPrevisto(valorPrevisto);
        setLimiteUltrapassado(limiteUltrapassado);

        if (nivel == null) {
            throw new DadosInvalidosException(
                    "O nivel do alerta deve ser informado"
            );
        }

        this.nivel = nivel;
        setMotivo(motivo);

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
        this.tipoSensor = tipoSensor;
    }

    public double getValorPrevisto() {
        return valorPrevisto;
    }

    public void setValorPrevisto(double valorPrevisto) {
        if (!Double.isFinite(valorPrevisto) || valorPrevisto < 0) {
            throw new DadosInvalidosException(
                    "O valor previsto deve ser valido e nao negativo"
            );
        }
        this.valorPrevisto = valorPrevisto;
    }

    public double getLimiteUltrapassado() {
        return limiteUltrapassado;
    }

    public void setLimiteUltrapassado(double limiteUltrapassado) {
        if (!Double.isFinite(limiteUltrapassado)
                || limiteUltrapassado < 0) {

            throw new DadosInvalidosException(
                    "O limite deve ser valido e nao negativo"
            );
        }
        this.limiteUltrapassado = limiteUltrapassado;
    }

    public NivelAlerta getNivel() {
        return nivel;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        if (motivo == null || motivo.isBlank()) {
            throw new DadosInvalidosException(
                    "O motivo do alerta deve ser informado"
            );
        }
        this.motivo = motivo;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }
}