package br.com.territory.dominio;

import br.com.territory.excecoes.FonteIndisponivelException;

public class SensorUmidadeSolo extends ComponenteTerritorial implements Sensor {
    public SensorUmidadeSolo(Long id, String nome, String regiao, boolean ativo) {
        super(id, nome, regiao, ativo);
    }

    @Override
    public double realizarLeitura() {
        if (!verificarFuncionamento()) {
            throw new FonteIndisponivelException(
                    "O sensor de umidade do solo " + getNome() + " esta indisponivel"
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
        return "UMIDADE_SOLO";
    }

    @Override
    public void verificarStatus() {
        if (verificarFuncionamento()) {
            System.out.println("Sensor de umidade do solo " + getNome() + " esta funcionando");
        } else {
            System.out.println("Sensor de umidade do solo " + getNome() + " esta indisponivel");
        }
    }
}