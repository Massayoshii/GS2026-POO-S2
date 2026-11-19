
package br.com.territory.servico;

import br.com.territory.dominio.Alerta;
import br.com.territory.estrategia.ResultadoPrevisao;
import br.com.territory.estrategia.TipoPrevisao;
import br.com.territory.excecoes.DadosInvalidosException;
import br.com.territory.excecoes.FonteIndisponivelException;

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
                        "Entrada invalida. Digite um numero de 1 a 7."
                );

                continue;
            }

            switch (opcao) {

                case 1:
                    verificarSensores();
                    break;

                case 2:
                    executarPrevisoes();
                    break;

                case 3:
                case 4:
                case 5:
                case 6:
                    System.out.println(
                            "Funcionalidade sera implementada nas proximas etapas."
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
                            "Opcao inexistente. Escolha um numero de 1 a 7."
                    );
                    break;
            }
        }
    }

    private void verificarSensores() {

        try {

            System.out.println("\n--- VERIFICACAO DOS SENSORES ---");

            servicoMonitoramento.verificarSensores();

            System.out.println("\n--- REGISTRANDO MEDICOES ---");

            servicoMonitoramento.registrarMedicoes();

        } catch (FonteIndisponivelException | DadosInvalidosException e) {

            System.out.println("Nao foi possivel concluir o monitoramento: "
                    + e.getMessage());
        }
    }


    private void executarPrevisoes() {

        System.out.println("\n--- SELECIONE O SENSOR ---");
        System.out.println("1 - Pluviometro");
        System.out.println("2 - Nivel da agua");
        System.out.println("3 - Umidade do solo");

        System.out.print("Escolha uma opcao: ");

        String opcaoSensor = scanner.nextLine().trim();

        String tipoSensor;

        switch (opcaoSensor) {

            case "1":
                tipoSensor = "PLUVIOMETRO";
                break;

            case "2":
                tipoSensor = "NIVEL_AGUA";
                break;

            case "3":
                tipoSensor = "UMIDADE_SOLO";
                break;

            default:
                System.out.println("Opcao de sensor invalida.");
                return;
        }

        System.out.println("\n--- SELECIONE A ESTRATEGIA ---");
        System.out.println("1 - Media movel");
        System.out.println("2 - Comparacao");

        System.out.print("Escolha uma opcao: ");

        String opcaoPrevisao = scanner.nextLine().trim();

        TipoPrevisao tipoPrevisao;

        switch (opcaoPrevisao) {

            case "1":
                tipoPrevisao = TipoPrevisao.MEDIA_MOVEL;
                break;

            case "2":
                tipoPrevisao = TipoPrevisao.COMPARACAO;
                break;

            default:
                System.out.println("Opcao de previsao invalida.");
                return;
        }

        try {

            ResultadoPrevisao resultado =
                    servicoMonitoramento.executarPrevisao(
                            tipoSensor,
                            tipoPrevisao
                    );

            String unidade = obterUnidade(tipoSensor);

            System.out.println("\n--- RESULTADO DA PREVISAO ---");

            System.out.println("Sensor: " + tipoSensor);

            System.out.println(
                    "Estrategia: " + resultado.getNomeEstrategia()
            );

            System.out.printf(
                    "Valor previsto: %.1f %s%n",
                    resultado.getValorPrevisto(),
                    unidade
            );

            Alerta alerta =
                    servicoMonitoramento.verificarPrevisaoEGerarAlerta(
                            resultado
                    );

            if (alerta == null) {

                System.out.println(
                        "Previsao abaixo do limite. Nenhum alerta foi gerado."
                );

            } else {

                System.out.println(
                        "Alerta gerado: " + alerta.getNivel()
                );
            }

        } catch (DadosInvalidosException e) {

            System.out.println(
                    "Nao foi possivel executar a previsao: " + e.getMessage()
            );
        }
    }

    private String obterUnidade(String tipoSensor) {

        return switch (tipoSensor) {

            case "PLUVIOMETRO" -> "mm/h";

            case "NIVEL_AGUA" -> "cm";

            case "UMIDADE_SOLO" -> "%";

            default -> "";
        };
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
