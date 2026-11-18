package br.com.territory.estrategia;

import br.com.territory.dominio.Medicao;
import br.com.territory.excecoes.DadosInvalidosException;

import java.util.List;

public class PrevisaoComparacao extends EstrategiaPrevisao {
    public PrevisaoComparacao() {
        super("Comparacao");
    }

    @Override
    public double prever(List<Medicao> medicoes) {
        if (medicoes == null || medicoes.size() < 2) {
            throw new DadosInvalidosException(
                    "Sao necessarias pelo menos duas medicoes para realizar a comparacao"
            );
        }

        Medicao medicaoAtual = medicoes.get(0);
        Medicao medicaoAnterior = medicoes.get(1);

        if (medicaoAtual == null || medicaoAnterior == null) {
            throw new DadosInvalidosException(
                    "As medicoes informadas nao podem ser nulas"
            );
        }

        double valorAtual = medicaoAtual.getValor();
        double valorAnterior = medicaoAnterior.getValor();

        if (!Double.isFinite(valorAtual) || valorAtual < 0 || !Double.isFinite(valorAnterior) || valorAnterior < 0) {
            throw new DadosInvalidosException(
                    "As medicoes possuem valores invalidos"
            );
        }

        double diferenca = valorAtual - valorAnterior;

        double previsao = valorAtual + diferenca;

        return Math.max(0, previsao);
    }
}