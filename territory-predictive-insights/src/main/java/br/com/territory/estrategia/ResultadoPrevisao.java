package br.com.territory.estrategia;

import br.com.territory.excecoes.DadosInvalidosException;

public class ResultadoPrevisao {
    private final String tipoSensor;
    private final double valorPrevisto;
    private final String nomeEstrategia;

    public ResultadoPrevisao(String tipoSensor,
                             double valorPrevisto,
                             String nomeEstrategia) {

        if (tipoSensor == null || tipoSensor.isBlank()) {
            throw new DadosInvalidosException(
                    "O tipo do sensor deve ser informado"
            );
        }

        if (!Double.isFinite(valorPrevisto) || valorPrevisto < 0) {
            throw new DadosInvalidosException(
                    "O valor previsto deve ser valido e nao negativo"
            );
        }

        if (nomeEstrategia == null || nomeEstrategia.isBlank()) {
            throw new DadosInvalidosException(
                    "O nome da estrategia deve ser informado"
            );
        }

        this.tipoSensor = tipoSensor;
        this.valorPrevisto = valorPrevisto;
        this.nomeEstrategia = nomeEstrategia;
    }

    public String getTipoSensor() {
        return tipoSensor;
    }

    public double getValorPrevisto() {
        return valorPrevisto;
    }

    public String getNomeEstrategia() {
        return nomeEstrategia;
    }
}
