package br.com.territory.servico;

import br.com.territory.dominio.Alerta;
import br.com.territory.dominio.DadosTerritorio;
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
                        "Entrada invalida, digite um numero de 1 a 7"
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
                    gerenciarLimites();
                    break;
                case 5:
                    consultarStatusCompleto();
                    break;
                case 6:
                    consultarAlertas();
                    break;
                case 7:
                    executando = false;
                    System.out.println(
                            "Encerrando o Territory Predictive Insights"
                    );
                    break;
                default:
                    System.out.println(
                            "Opcao inexistente, escolha um numero de 1 a 7"
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
            System.out.println("Opcao de sensor invalida");
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
                System.out.println("Opcao de previsao invalida");
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

            Alerta alerta = servicoMonitoramento.verificarPrevisaoEGerarAlerta(resultado);

            if (alerta == null) {
                System.out.println(
                        "Previsao abaixo do limite, nenhum alerta foi gerado"
                );

            } else {
                System.out.println(
                        "Alerta gerado: " + alerta.getNivel()
                );
            }

        } catch (DadosInvalidosException e) {
            System.out.println(
                    "Nao foi possivel executar a previsao: " + e.getMessage());
        }
    }

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
                        System.out.println("Opcao de sensor invalida");
                        return;
                    }
                    medicoes = servicoMonitoramento.consultarHistorico(tipoSensor);
                    break;

                default:
                    System.out.println("Opcao invalida");
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
            System.out.println("Nenhuma medicao encontrada");
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

    private void gerenciarLimites() {
        boolean continuar = true;

        while (continuar) {
            DadosTerritorio limites = servicoMonitoramento.consultarLimites();

            System.out.println("\n--- GERENCIAR LIMITES ---");
            System.out.printf(
                    "Limite de chuva: %.1f mm/h%n",
                    limites.getLimiteChuvaMmHora()
            );
            System.out.printf(
                    "Limite do nivel da agua: %.1f cm%n",
                    limites.getLimiteNivelAguaCm()
            );
            System.out.printf(
                    "Limite de umidade do solo: %.1f%%%n",
                    limites.getLimiteUmidadeSoloPercentual()
            );
            System.out.println("\n1 - Alterar limite de chuva");
            System.out.println("2 - Alterar limite do nivel da agua");
            System.out.println("3 - Alterar limite de umidade do solo");
            System.out.println("4 - Voltar ao menu principal");

            System.out.print("Escolha uma opcao: ");

            String opcao = scanner.nextLine().trim();

            if (opcao.equals("4")) {
                continuar = false;
                continue;
            }

            if (!List.of("1", "2", "3").contains(opcao)) {
                System.out.println("Opcao invalida");
                continue;
            }

            System.out.print("Digite o novo limite: ");

            String entrada = scanner.nextLine().trim();

            try {
                double novoLimite = Double.parseDouble(
                        entrada.replace(',', '.')
                );
                switch (opcao) {
                    case "1":
                        servicoMonitoramento.atualizarLimiteChuva(
                                novoLimite
                        );

                        break;

                    case "2":
                        servicoMonitoramento.atualizarLimiteNivelAgua(
                                novoLimite
                        );
                        break;

                    case "3":
                        servicoMonitoramento.atualizarLimiteUmidadeSolo(
                                novoLimite
                        );
                        break;
                }

                System.out.println("Limite atualizado com sucesso");

            } catch (NumberFormatException e) {
                System.out.println(
                        "Valor invalido, digite um numero"
                );

            } catch (DadosInvalidosException e) {
                System.out.println(
                        "Nao foi possivel atualizar o limite: "
                                + e.getMessage()
                );
            }
        }
    }

    private void consultarStatusCompleto() {
        System.out.println("\n======================================");
        System.out.println("       STATUS COMPLETO DO TERRITORIO");
        System.out.println("======================================");

        System.out.println("\n--- SENSORES ---");

        servicoMonitoramento.verificarSensores();

        System.out.println("\n--- LIMITES ATUAIS ---");

        DadosTerritorio limites = servicoMonitoramento.consultarLimites();

        System.out.printf(
                "Chuva: %.1f mm/h%n",
                limites.getLimiteChuvaMmHora()
        );

        System.out.printf(
                "Nivel da agua: %.1f cm%n",
                limites.getLimiteNivelAguaCm()
        );

        System.out.printf(
                "Umidade do solo: %.1f%%%n",
                limites.getLimiteUmidadeSoloPercentual()
        );

        System.out.println("\n--- ULTIMAS MEDICOES ---");

        List<Medicao> medicoes = servicoMonitoramento.consultarHistorico();

        exibirUltimasMedicoes(medicoes);

        System.out.println("\n--- RESUMO DOS ALERTAS ---");

        List<Alerta> alertas = servicoMonitoramento.consultarAlertas();

        System.out.println(
                "Total de alertas registrados: " + alertas.size()
        );

        if (!alertas.isEmpty()) {
            Alerta ultimoAlerta = alertas.get(0);
            System.out.println(
                    "Ultimo nivel de alerta: "
                            + ultimoAlerta.getNivel()
            );

            System.out.println(
                    "Sensor relacionado: "
                            + ultimoAlerta.getTipoSensor()
            );

        } else {
            System.out.println("Nenhum alerta registrado");
        }
        System.out.println("\n======================================");
    }

    private void exibirUltimasMedicoes(List<Medicao> medicoes) {
        if (medicoes.isEmpty()) {
            System.out.println("Nenhuma medicao registrada");
            return;
        }

        boolean pluviometroExibido = false;
        boolean nivelAguaExibido = false;
        boolean umidadeExibida = false;

        for (Medicao medicao : medicoes) {
            String tipo = medicao.getTipoSensor();
            if (tipo.equals("PLUVIOMETRO") && !pluviometroExibido) {
                System.out.printf(
                        "Pluviometro: %.1f mm/h%n",
                        medicao.getValor()
                );
                pluviometroExibido = true;
            }
            if (tipo.equals("NIVEL_AGUA") && !nivelAguaExibido) {
                System.out.printf(
                        "Nivel da agua: %.1f cm%n",
                        medicao.getValor()
                );
                nivelAguaExibido = true;
            }
            if (tipo.equals("UMIDADE_SOLO") && !umidadeExibida) {
                System.out.printf(
                        "Umidade do solo: %.1f%%%n",
                        medicao.getValor()
                );
                umidadeExibida = true;
            }
            if (pluviometroExibido
                    && nivelAguaExibido
                    && umidadeExibida) {
                break;
            }
        }

        if (!pluviometroExibido) {
            System.out.println("Pluviometro: sem medicoes");
        }

        if (!nivelAguaExibido) {
            System.out.println("Nivel da agua: sem medicoes");
        }

        if (!umidadeExibida) {
            System.out.println("Umidade do solo: sem medicoes");
        }
    }

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
                        System.out.println("Opcao de sensor invalida");
                        return;
                    }

                    alertas = servicoMonitoramento.consultarAlertasPorSensor(tipoSensor);
                    break;

                case "3":
                    NivelAlerta nivel = selecionarNivelAlerta();
                    if (nivel == null) {
                        System.out.println("Opcao de nivel invalida");
                        return;
                    }

                    alertas = servicoMonitoramento.consultarAlertasPorNivel(nivel);
                    break;
                default:
                    System.out.println("Opcao invalida");
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
            System.out.println("Nenhum alerta encontrado");
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