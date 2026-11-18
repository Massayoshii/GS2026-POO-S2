package br.com.territory.estrategia;

import br.com.territory.excecoes.DadosInvalidosException;

public class FabricaPrevisao {
    public static EstrategiaPrevisao criar(TipoPrevisao tipo) {
        if (tipo == null) {
            throw new DadosInvalidosException(
                    "O tipo de previsao deve ser informado"
            );
        }
        switch (tipo) {
            case MEDIA_MOVEL:
                return new PrevisaoMediaMovel();
            case COMPARACAO:
                return new PrevisaoComparacao();
            default:
                throw new DadosInvalidosException(
                        "Tipo de previsao nao reconhecido"
                );
        }
    }
}