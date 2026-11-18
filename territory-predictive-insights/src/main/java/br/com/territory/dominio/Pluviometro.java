package br.com.territory.dominio;

import br.com.territory.excecoes.FonteIndisponivelException;

public class Pluviometro extends ComponenteTerritorial implements Sensor {
    public Pluviometro(Long id, String nome, String regiao, boolean ativo) {
        super(id, nome, regiao, ativo);
    }

    @Override
    public double realizarLeitura() {
        if (!verificarFuncionamento()) {
            throw new FonteIndisponivelException(
                    "O pluviometro " + getNome() + " esta indisponivel."
            );
        }
        return Math.round(Math.random() * 1000) / 10.0;
    }

    @Override
    public boolean verificarFuncionamento() {
        return isAtivo();
    }

    @Override
    public String getTipoSensor() {
        return "PLUVIOMETRO";
    }

    @Override
    public void verificarStatus() {
        if (verificarFuncionamento()) {
            System.out.println("Pluviometro " + getNome() + " esta funcionando");
        } else {
            System.out.println("Pluviometro " + getNome() + " esta indisponivel");
        }
    }
}
