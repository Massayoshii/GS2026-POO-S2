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
        validarLimite(limiteChuvaMmHora, "Limite de chuva");
        this.limiteChuvaMmHora = limiteChuvaMmHora;
    }

    public double getLimiteNivelAguaCm() {
        return limiteNivelAguaCm;
    }

    public void setLimiteNivelAguaCm(double limiteNivelAguaCm) {
        validarLimite(limiteNivelAguaCm, "Limite do nivel da agua");
        this.limiteNivelAguaCm = limiteNivelAguaCm;
    }

    public double getLimiteUmidadeSoloPercentual() {
        return limiteUmidadeSoloPercentual;
    }

    public void setLimiteUmidadeSoloPercentual(double limiteUmidadeSoloPercentual) {
        if (!Double.isFinite(limiteUmidadeSoloPercentual) || limiteUmidadeSoloPercentual <= 0 || limiteUmidadeSoloPercentual > 100) {
            throw new DadosInvalidosException(
                    "O limite de umidade deve ser maior que 0 e menor ou igual a 100%"
            );
        }
        this.limiteUmidadeSoloPercentual = limiteUmidadeSoloPercentual;
    }

    private void validarLimite(double valor, String nomeCampo) {
        if (!Double.isFinite(valor) || valor <= 0) {
            throw new DadosInvalidosException(
                    nomeCampo + " deve ser maior que zero"
            );
        }
    }
}