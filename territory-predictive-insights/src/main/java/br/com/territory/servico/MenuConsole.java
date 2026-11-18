package br.com.territory.servico;

import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class MenuConsole {
    private final Scanner scanner;
    private final ServicoMonitoramento servicoMonitoramento;

    public MenuConsole(ServicoMonitoramento servicoMonitoramento) {
        this.scanner = new Scanner(System.in);
        this.servicoMonitoramento = servicoMonitoramento;
    }

    public void iniciar() {
        exibirMenu();
    }

    private void exibirMenu() {
        System.out.println();
        System.out.println("======================================");
        System.out.println("   TERRITORY PREDICTIVE INSIGHTS");
        System.out.println("======================================");
        System.out.println("1 - Verificar sensores");
        System.out.println("2 - Executar previsoes");
        System.out.println("3 - Consultar historico");
        System.out.println("4 - Gerenciar dados e limiares");
        System.out.println("5 - Consultar status completo");
        System.out.println("6 - Consultar alertas");
        System.out.println("7 - Sair");
        System.out.println("======================================");
    }
}
