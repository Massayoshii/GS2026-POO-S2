package br.com.territory.dominio;

public class AreaMonitorada extends ComponenteTerritorial {
    public AreaMonitorada(Long id, String nome, String regiao, boolean ativo) {
        super(id, nome, regiao, ativo);
    }

    @Override
    public void verificarStatus() {
        if (isAtivo()) {
            System.out.println("Área monitorada " + getNome()
                    + " está ativa na região " + getRegiao());
        } else {
            System.out.println("Área monitorada " + getNome()
                    + " está inativa na região " + getRegiao());
        }
    }
}