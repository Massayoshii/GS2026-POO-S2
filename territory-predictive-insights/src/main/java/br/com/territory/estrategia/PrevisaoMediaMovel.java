package br.com.territory.estrategia;

import br.com.territory.dominio.Medicao;
import br.com.territory.excecoes.DadosInvalidosException;

import java.util.List;

public class PrevisaoMediaMovel extends EstrategiaPrevisao {
    private static final int QUANTIDADE_MEDICOES = 5;

    public PrevisaoMediaMovel() {
        super("Media Movel");
    }

    @Override
    public double prever(List<Medicao> medicoes) {
        if (medicoes == null || medicoes.isEmpty()) {
            throw new DadosInvalidosException(
                    "Nao existem medicoes para calcular a previsao"
            );
        }

        int quantidade = Math.min(
                QUANTIDADE_MEDICOES,
                medicoes.size()
        );

        double soma = 0;

        for (int i = 0; i < quantidade; i++) {

            Medicao medicao = medicoes.get(i);

            if (medicao == null || !Double.isFinite(medicao.getValor()) || medicao.getValor() < 0) {

                throw new DadosInvalidosException(
                        "Uma das medicoes possui valor invalido"
                );
            }
            soma += medicao.getValor();
        }
        return soma / quantidade;
    }
}
