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
        boolean executando = true;
        while (executando) {
            exibirMenu();

            System.out.print("Escolha uma opcao: ");

            String entrada = scanner.nextLine();

            int opcao;

            try {
                opcao = Integer.parseInt(entrada.trim());

            } catch (NumberFormatException e) {

                System.out.println(
                        "Entrada invalida. Digite um numero de 1 a 7"
                );
                continue;
            }
            switch (opcao) {
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                    System.out.println(
                            "Opcao selecionada. Funcionalidade sera implementada"
                    );
                    break;
                case 7:
                    executando = false;

                    System.out.println(
                            "Encerrando o Territory Predictive Insights..."
                    );

                    break;

                default:
                    System.out.println(
                            "Opcao inexistente. Escolha um numero de 1 a 7"
                    );
                    break;
            }
        }
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
