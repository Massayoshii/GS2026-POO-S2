package br.com.territory.dominio;

import br.com.territory.excecoes.DadosInvalidosException;

public class DadosTerritorio {
    private double limiteChuvaMmHora;
    private double limiteNivelAguaCm;
    private double limiteUmidadeSoloPercentual;

    public DadosTerritorio(double limiteChuvaMmHora,
                           double limiteNivelAguaCm,
                           double limiteUmidadeSoloPercentual) {

        setLimiteChuvaMmHora(limiteChuvaMmHora);
        setLimiteNivelAguaCm(limiteNivelAguaCm);
        setLimiteUmidadeSoloPercentual(limiteUmidadeSoloPercentual);
    }

    public double getLimiteChuvaMmHora() {
        return limiteChuvaMmHora;
    }

    public void setLimiteChuvaMmHora(double limiteChuvaMmHora) {
        validarValorNaoNegativo(limiteChuvaMmHora, "Limite de chuva");
        this.limiteChuvaMmHora = limiteChuvaMmHora;
    }

    public double getLimiteNivelAguaCm() {
        return limiteNivelAguaCm;
    }

    public void setLimiteNivelAguaCm(double limiteNivelAguaCm) {
        validarValorNaoNegativo(limiteNivelAguaCm, "Limite do nível da água");
        this.limiteNivelAguaCm = limiteNivelAguaCm;
    }

    public double getLimiteUmidadeSoloPercentual() {
        return limiteUmidadeSoloPercentual;
    }

    public void setLimiteUmidadeSoloPercentual(double limiteUmidadeSoloPercentual) {
        if (!Double.isFinite(limiteUmidadeSoloPercentual)
                || limiteUmidadeSoloPercentual < 0
                || limiteUmidadeSoloPercentual > 100) {

            throw new DadosInvalidosException(
                    "A umidade do solo deve estar entre 0 e 100%."
            );
        }
        this.limiteUmidadeSoloPercentual = limiteUmidadeSoloPercentual;
    }

    private void validarValorNaoNegativo(double valor, String nomeCampo) {
        if (!Double.isFinite(valor) || valor < 0) {
            throw new DadosInvalidosException(
                    nomeCampo + " não pode ser negativo ou inválido."
            );
        }
    }
}