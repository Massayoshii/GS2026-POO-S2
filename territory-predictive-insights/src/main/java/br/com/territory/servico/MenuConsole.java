package br.com.territory.servico;

import br.com.territory.dominio.Alerta;
import br.com.territory.dominio.Medicao;
import br.com.territory.dominio.NivelAlerta;

import br.com.territory.estrategia.ResultadoPrevisao;
import br.com.territory.estrategia.TipoPrevisao;

import br.com.territory.excecoes.DadosInvalidosException;
import br.com.territory.excecoes.FonteIndisponivelException;

import org.springframework.stereotype.Component;

import java.util.List;
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
                    consultarHistorico();
                    break;

                case 4:
                case 5:
                    System.out.println(
                            "Funcionalidade sera implementada nas proximas etapas."
                    );
                    break;

                case 6:
                    consultarAlertas();
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

            System.out.println(
                    "Nao foi possivel concluir o monitoramento: "
                            + e.getMessage()
            );
        }
    }

    private void executarPrevisoes() {

        System.out.println("\n--- SELECIONE O SENSOR ---");

        String tipoSensor = selecionarTipoSensor();

        if (tipoSensor == null) {
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
                    "Nao foi possivel executar a previsao: "
                            + e.getMessage()
            );
        }
    }

    // CONSULTA DO HISTORICO

    private void consultarHistorico() {

        System.out.println("\n--- CONSULTAR HISTORICO ---");
        System.out.println("1 - Consultar todas as medicoes");
        System.out.println("2 - Consultar medicoes por sensor");

        System.out.print("Escolha uma opcao: ");

        String opcao = scanner.nextLine().trim();

        List<Medicao> medicoes;

        try {

            switch (opcao) {

                case "1":

                    medicoes = servicoMonitoramento.consultarHistorico();
                    break;

                case "2":

                    System.out.println("\n--- SELECIONE O SENSOR ---");

                    String tipoSensor = selecionarTipoSensor();

                    if (tipoSensor == null) {
                        System.out.println("Opcao de sensor invalida.");
                        return;
                    }

                    medicoes =
                            servicoMonitoramento.consultarHistorico(tipoSensor);

                    break;

                default:

                    System.out.println("Opcao invalida.");
                    return;
            }

            exibirMedicoes(medicoes);

        } catch (DadosInvalidosException e) {

            System.out.println(
                    "Erro ao consultar historico: " + e.getMessage()
            );
        }
    }

    private void exibirMedicoes(List<Medicao> medicoes) {

        System.out.println("\n--- HISTORICO DE MEDICOES ---");

        if (medicoes.isEmpty()) {

            System.out.println("Nenhuma medicao encontrada.");
            return;
        }

        for (Medicao medicao : medicoes) {

            System.out.printf(
                    "Data: %s | Sensor: %s | Valor: %.1f %s%n",
                    medicao.getDataHora(),
                    medicao.getTipoSensor(),
                    medicao.getValor(),
                    medicao.getUnidade()
            );
        }
    }

    // CONSULTA DOS ALERTAS

    private void consultarAlertas() {

        System.out.println("\n--- CONSULTAR ALERTAS ---");
        System.out.println("1 - Consultar todos os alertas");
        System.out.println("2 - Consultar alertas por sensor");
        System.out.println("3 - Consultar alertas por nivel");

        System.out.print("Escolha uma opcao: ");

        String opcao = scanner.nextLine().trim();

        List<Alerta> alertas;

        try {

            switch (opcao) {

                case "1":

                    alertas = servicoMonitoramento.consultarAlertas();
                    break;

                case "2":

                    System.out.println("\n--- SELECIONE O SENSOR ---");

                    String tipoSensor = selecionarTipoSensor();

                    if (tipoSensor == null) {
                        System.out.println("Opcao de sensor invalida.");
                        return;
                    }

                    alertas =
                            servicoMonitoramento
                                    .consultarAlertasPorSensor(tipoSensor);

                    break;

                case "3":

                    NivelAlerta nivel = selecionarNivelAlerta();

                    if (nivel == null) {
                        System.out.println("Opcao de nivel invalida.");
                        return;
                    }

                    alertas =
                            servicoMonitoramento
                                    .consultarAlertasPorNivel(nivel);

                    break;

                default:

                    System.out.println("Opcao invalida.");
                    return;
            }

            exibirAlertas(alertas);

        } catch (DadosInvalidosException e) {

            System.out.println(
                    "Erro ao consultar alertas: " + e.getMessage()
            );
        }
    }

    private void exibirAlertas(List<Alerta> alertas) {

        System.out.println("\n--- HISTORICO DE ALERTAS ---");

        if (alertas.isEmpty()) {

            System.out.println("Nenhum alerta encontrado.");
            return;
        }

        for (Alerta alerta : alertas) {

            String unidade = obterUnidade(alerta.getTipoSensor());

            System.out.println("--------------------------------------");

            System.out.println("Data: " + alerta.getDataHora());

            System.out.println("Sensor: " + alerta.getTipoSensor());

            System.out.printf(
                    "Valor previsto: %.1f %s%n",
                    alerta.getValorPrevisto(),
                    unidade
            );

            System.out.printf(
                    "Limite: %.1f %s%n",
                    alerta.getLimiteUltrapassado(),
                    unidade
            );

            System.out.println("Nivel: " + alerta.getNivel());

            System.out.println("Motivo: " + alerta.getMotivo());
        }

        System.out.println("--------------------------------------");
    }

    // METODOS AUXILIARES

    private String selecionarTipoSensor() {

        System.out.println("1 - Pluviometro");
        System.out.println("2 - Nivel da agua");
        System.out.println("3 - Umidade do solo");

        System.out.print("Escolha uma opcao: ");

        String opcao = scanner.nextLine().trim();

        return switch (opcao) {

            case "1" -> "PLUVIOMETRO";

            case "2" -> "NIVEL_AGUA";

            case "3" -> "UMIDADE_SOLO";

            default -> null;
        };
    }

    private NivelAlerta selecionarNivelAlerta() {

        System.out.println("\n--- SELECIONE O NIVEL ---");
        System.out.println("1 - ATENCAO");
        System.out.println("2 - ALERTA");
        System.out.println("3 - CRITICO");

        System.out.print("Escolha uma opcao: ");

        String opcao = scanner.nextLine().trim();

        return switch (opcao) {

            case "1" -> NivelAlerta.ATENCAO;

            case "2" -> NivelAlerta.ALERTA;

            case "3" -> NivelAlerta.CRITICO;

            default -> null;
        };
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