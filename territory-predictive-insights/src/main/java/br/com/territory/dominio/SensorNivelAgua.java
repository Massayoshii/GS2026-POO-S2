package br.com.territory.dominio;

import br.com.territory.excecoes.FonteIndisponivelException;

public class SensorNivelAgua extends ComponenteTerritorial implements Sensor {
    public SensorNivelAgua(Long id, String nome, String regiao, boolean ativo) {
        super(id, nome, regiao, ativo);
    }

    @Override
    public double realizarLeitura() {
        if (!verificarFuncionamento()) {
            throw new FonteIndisponivelException(
                    "O sensor de nivel da agua " + getNome() + " esta indisponivel"
            );
        }

        return Math.round(Math.random() * 3000) / 10.0;
    }

    @Override
    public boolean verificarFuncionamento() {
        return isAtivo();
    }

    @Override
    public String getTipoSensor() {
        return "NIVEL_AGUA";
    }

    @Override
    public void verificarStatus() {
        if (verificarFuncionamento()) {
            System.out.println("Sensor de nivel da agua " + getNome() + " esta funcionando");
        } else {
            System.out.println("Sensor de nivel da agua " + getNome() + " esta indisponivel");
        }
    }
}
