package br.com.territory.estrategia;

import br.com.territory.dominio.Medicao;

import java.util.List;

public abstract class EstrategiaPrevisao {
    private final String nomeEstrategia;

    protected EstrategiaPrevisao(String nomeEstrategia) {
        this.nomeEstrategia = nomeEstrategia;
    }

    public String getNomeEstrategia() {
        return nomeEstrategia;
    }

    public abstract double prever(List<Medicao> medicoes);
}