
package br.com.territory.app;

import br.com.territory.dominio.DadosTerritorio;
import br.com.territory.dominio.RegiaoMonitorada;
import br.com.territory.dominio.Sensor;
import br.com.territory.dominio.SensorTemperatura;
import br.com.territory.dominio.SensorUmidade;
import br.com.territory.dominio.SensorConsumoEnergia;
import br.com.territory.exceptions.DadosInvalidosException;
import br.com.territory.exceptions.FonteIndisponivelException;
import br.com.territory.persistencia.AlertaDAO;
import br.com.territory.persistencia.BancoDados;
import br.com.territory.persistencia.MedicaoDAO;
import br.com.territory.servicos.MonitoramentoService;

import java.sql.SQLException;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Main {

    private final Scanner scanner;
    private final Random random;
    private final DadosTerritorio dadosTerritorio;
    private final MonitoramentoService monitoramentoService;
    private final MedicaoDAO medicaoDAO;
    private final AlertaDAO alertaDAO;

    private Main() throws DadosInvalidosException {

        scanner = new Scanner(System.in);
        random = new Random();

        dadosTerritorio = new DadosTerritorio();

        medicaoDAO = new MedicaoDAO();
        alertaDAO = new AlertaDAO();

        monitoramentoService = new MonitoramentoService(
                medicaoDAO,
                alertaDAO);
    }

    public static void main(String[] args) {

        try {

            BancoDados.inicializar();

            System.out.println("========================================");
            System.out.println("     TERRITORY PREDICTIVE INSIGHTS");
            System.out.println("========================================");
            System.out.println("Banco de dados inicializado com sucesso!");

            Main aplicacao = new Main();
            aplicacao.executar();

        } catch (SQLException e) {

            System.out.println("Erro ao inicializar ou acessar o banco: "
                    + e.getMessage());

        } catch (DadosInvalidosException e) {

            System.out.println("Erro na configuração do sistema: "
                    + e.getMessage());
        }
    }

    private void executar() {

        boolean continuar = true;

        while (continuar) {

            exibirMenu();

            int opcao = lerInteiro("Escolha uma opção: ");

            try {

                switch (opcao) {

                    case 1 -> registrarMonitoramento();

                    case 2 -> consultarHistoricoMedicoes();

                    case 3 -> consultarHistoricoPorSensor();

                    case 4 -> consultarHistoricoAlertas();

                    case 5 -> consultarLimites();

                    case 6 -> alterarLimites();

                    case 7 -> executarPrevisao();

                    case 8 -> {
                        System.out.println("\nEncerrando o sistema...");
                        continuar = false;
                    }

                    case 9 -> simularMonitoramento();

                    default -> System.out.println(
                            "Opção inválida. Tente novamente.");
                }

            } catch (DadosInvalidosException e) {

                System.out.println("\nDados inválidos: "
                        + e.getMessage());

            } catch (FonteIndisponivelException e) {

                System.out.println("\nNão foi possível realizar a operação: "
                        + e.getMessage());

            } catch (SQLException e) {

                System.out.println("\nErro de acesso ao banco: "
                        + e.getMessage());

            } catch (IllegalArgumentException e) {

                System.out.println("\nOperação inválida: "
                        + e.getMessage());
            }
        }

        System.out.println("Sistema encerrado.");
    }

    private void exibirMenu() {

        System.out.println("\n========================================");
        System.out.println("              MENU PRINCIPAL");
        System.out.println("========================================");
        System.out.println("1 - Registrar monitoramento manualmente");
        System.out.println("2 - Consultar histórico de medições");
        System.out.println("3 - Consultar histórico por sensor");
        System.out.println("4 - Consultar histórico de alertas");
        System.out.println("5 - Consultar limites de monitoramento");
        System.out.println("6 - Alterar limites de monitoramento");
        System.out.println("7 - Executar previsão");
        System.out.println("8 - Sair");
        System.out.println("9 - Simular monitoramento automaticamente");
        System.out.println("========================================");
    }

    // Registra medições informadas manualmente pelo usuário.
    private void registrarMonitoramento()
            throws DadosInvalidosException, SQLException {

        System.out.println("\n===== REGISTRAR MONITORAMENTO =====");

        String id = lerTexto("ID da região: ");
        String nome = lerTexto("Nome da região: ");
        String regiao = lerTexto("Localização: ");

        double temperatura = lerDouble("Temperatura (°C): ");
        double consumo = lerDouble("Consumo de energia (kWh): ");
        double umidade = lerDouble("Umidade (%): ");

        RegiaoMonitorada novaRegiao = new RegiaoMonitorada(
                id,
                nome,
                regiao,
                temperatura,
                consumo,
                umidade,
                dadosTerritorio);

        String status = monitoramentoService.registrarMonitoramento(
                novaRegiao);

        System.out.println("\nMonitoramento registrado com sucesso!");
        System.out.println("----------------------------------------");

        novaRegiao.exibirInformacoes();

        System.out.println("----------------------------------------");
        System.out.println("Classificação: " + status);
        System.out.println("Medição salva no banco de dados.");

        if (!status.equals("NORMAL")
                && !status.equals("INATIVO")) {

            System.out.println("Alerta registrado no banco de dados!");
        }
    }

    // Gera leituras aleatórias utilizando os três sensores.
    private void simularMonitoramento()
            throws DadosInvalidosException,
            FonteIndisponivelException,
            SQLException {

        System.out.println("\n===== SIMULAÇÃO AUTOMÁTICA =====");

        String id = lerTexto("ID da região: ");
        String nome = lerTexto("Nome da região: ");
        String regiao = lerTexto("Localização: ");

        System.out.println("\nGerando leituras simuladas...");

        // Geração de valores aleatórios dentro dos intervalos definidos.
        double temperaturaSimulada = 20.0 + random.nextDouble() * 25.0;
        double consumoSimulado = 300.0 + random.nextDouble() * 700.0;
        double umidadeSimulada = 30.0 + random.nextDouble() * 60.0;

        // Criação dos sensores com validação dos valores.
        Sensor sensorTemperatura = new SensorTemperatura(
                temperaturaSimulada);

        Sensor sensorConsumo = new SensorConsumoEnergia(
                consumoSimulado);

        Sensor sensorUmidade = new SensorUmidade(
                umidadeSimulada);

        // Verificação de funcionamento dos sensores.
        if (!sensorTemperatura.verificarFuncionamento()) {
            throw new FonteIndisponivelException(
                    "Sensor de temperatura indisponível.");
        }

        if (!sensorConsumo.verificarFuncionamento()) {
            throw new FonteIndisponivelException(
                    "Sensor de consumo de energia indisponível.");
        }

        if (!sensorUmidade.verificarFuncionamento()) {
            throw new FonteIndisponivelException(
                    "Sensor de umidade indisponível.");
        }

        // Realização das leituras.
        double temperatura = sensorTemperatura.realizarLeitura();
        double consumo = sensorConsumo.realizarLeitura();
        double umidade = sensorUmidade.realizarLeitura();

        System.out.println("\nLeituras dos sensores:");
        System.out.printf("Temperatura: %.2f °C%n", temperatura);
        System.out.printf("Consumo de energia: %.2f kWh%n", consumo);
        System.out.printf("Umidade: %.2f %%%n", umidade);

        // Criação da região monitorada.
        RegiaoMonitorada novaRegiao = new RegiaoMonitorada(
                id,
                nome,
                regiao,
                temperatura,
                consumo,
                umidade,
                dadosTerritorio);

        // Registro e classificação das medições.
        String status = monitoramentoService.registrarMonitoramento(
                novaRegiao);

        System.out.println("\n===== RESULTADO DA SIMULAÇÃO =====");
        System.out.println("----------------------------------------");

        novaRegiao.exibirInformacoes();

        System.out.println("----------------------------------------");
        System.out.println("Classificação: " + status);
        System.out.println("Leituras validadas com sucesso.");
        System.out.println("Medições salvas no banco de dados.");

        if (!status.equals("NORMAL")
                && !status.equals("INATIVO")) {

            System.out.println("Alerta registrado no banco de dados!");
        }
    }

    // Consulta o histórico completo de medições de uma região.
    private void consultarHistoricoMedicoes() throws SQLException {

        System.out.println("\n===== HISTÓRICO DE MEDIÇÕES =====");

        String id = lerTexto("Informe o ID da região: ");

        List<String> medicoes = medicaoDAO.listarPorRegiao(id);

        if (medicoes.isEmpty()) {

            System.out.println("Nenhuma medição encontrada.");
            return;
        }

        System.out.println("\nMedições encontradas:");

        for (String medicao : medicoes) {
            System.out.println(medicao);
        }
    }

    // Consulta o histórico de um sensor específico.
    private void consultarHistoricoPorSensor() throws SQLException {

        System.out.println("\n===== HISTÓRICO POR SENSOR =====");

        String id = lerTexto("Informe o ID da região: ");

        System.out.println("\nTipos de sensores:");
        System.out.println("1 - Temperatura");
        System.out.println("2 - Umidade");
        System.out.println("3 - Consumo de energia");

        int opcaoSensor = lerInteiro("Escolha o sensor: ");

        String tipoSensor;

        switch (opcaoSensor) {

            case 1 -> tipoSensor = "TEMPERATURA";

            case 2 -> tipoSensor = "UMIDADE";

            case 3 -> tipoSensor = "CONSUMO";

            default -> {
                System.out.println("Opção de sensor inválida.");
                return;
            }
        }

        List<String> medicoes = medicaoDAO.listarPorSensor(
                id,
                tipoSensor);

        if (medicoes.isEmpty()) {

            System.out.println(
                    "Nenhuma medição encontrada para esse sensor.");

            return;
        }

        System.out.println("\nMedições encontradas:");

        for (String medicao : medicoes) {
            System.out.println(medicao);
        }
    }

    // Consulta o histórico de alertas de uma região.
    private void consultarHistoricoAlertas() throws SQLException {

        System.out.println("\n===== HISTÓRICO DE ALERTAS =====");

        String id = lerTexto("Informe o ID da região: ");

        List<String> alertas = alertaDAO.listarPorRegiao(id);

        if (alertas.isEmpty()) {

            System.out.println("Nenhum alerta encontrado.");
            return;
        }

        System.out.println("\nAlertas encontrados:");

        for (String alerta : alertas) {
            System.out.println(alerta);
        }
    }

    // Exibe os limites de monitoramento configurados.
    private void consultarLimites() {

        System.out.println("\n===== LIMITES DE MONITORAMENTO =====");

        System.out.println("\nTEMPERATURA");

        System.out.printf(
                "Atenção: %.2f °C%n",
                dadosTerritorio.getLimiteAtencaoTemperatura());

        System.out.printf(
                "Crítico: %.2f °C%n",
                dadosTerritorio.getLimiteCriticoTemperatura());

        System.out.println("\nCONSUMO DE ENERGIA");

        System.out.printf(
                "Atenção: %.2f kWh%n",
                dadosTerritorio.getLimiteAtencaoConsumo());

        System.out.printf(
                "Crítico: %.2f kWh%n",
                dadosTerritorio.getLimiteCriticoConsumo());
    }

    // Altera os limites de monitoramento mediante código de acesso.
    private void alterarLimites() throws DadosInvalidosException {

        System.out.println("\n===== ALTERAR LIMITES =====");

        System.out.println(
                "Esta operação exige autorização.");

        String codigo = lerTexto("Código de acesso: ");

        double atencaoTemperatura = lerDouble(
                "Novo limite de atenção da temperatura: ");

        double criticoTemperatura = lerDouble(
                "Novo limite crítico da temperatura: ");

        double atencaoConsumo = lerDouble(
                "Novo limite de atenção do consumo: ");

        double criticoConsumo = lerDouble(
                "Novo limite crítico do consumo: ");

        dadosTerritorio.configurarLimiares(
                atencaoTemperatura,
                criticoTemperatura,
                atencaoConsumo,
                criticoConsumo,
                codigo);

        System.out.println(
                "\nLimites de monitoramento atualizados com sucesso!");
    }

    // Executa uma previsão utilizando o histórico de um sensor.
    private void executarPrevisao()
            throws SQLException,
            DadosInvalidosException,
            FonteIndisponivelException {

        System.out.println("\n===== PREVISÃO TERRITORIAL =====");

        String id = lerTexto("Informe o ID da região: ");

        System.out.println("\nSensores disponíveis para previsão:");
        System.out.println("1 - Temperatura");
        System.out.println("2 - Consumo de energia");

        int opcaoSensor = lerInteiro("Escolha o sensor: ");

        String tipoSensor;

        switch (opcaoSensor) {

            case 1 -> tipoSensor = "TEMPERATURA";

            case 2 -> tipoSensor = "CONSUMO_ENERGIA";

            default -> {
                System.out.println("Opção de sensor inválida.");
                return;
            }
        }

        System.out.println("\nEstratégias de previsão:");
        System.out.println("1 - Média móvel");
        System.out.println("2 - Comparação entre períodos");

        int opcaoEstrategia = lerInteiro(
                "Escolha a estratégia: ");

        String estrategia;

        switch (opcaoEstrategia) {

            case 1 -> estrategia = "MEDIA_MOVEL";

            case 2 -> estrategia = "COMPARACAO";

            default -> {
                System.out.println("Opção de estratégia inválida.");
                return;
            }
        }

        System.out.println("\nCalculando previsão...");

        String resultado = monitoramentoService.preverSensor(
                id,
                tipoSensor,
                estrategia,
                dadosTerritorio);

        System.out.println("\n===== RESULTADO DA PREVISÃO =====");
        System.out.println(resultado);

        System.out.println("----------------------------------------");

        System.out.println(
                "A previsão foi calculada utilizando o histórico "
                        + "armazenado no banco de dados.");

        System.out.println(
                "Os alertas gerados foram registrados automaticamente.");
    }

    private String lerTexto(String mensagem) {

        System.out.print(mensagem);

        return scanner.nextLine().trim();
    }

    private int lerInteiro(String mensagem) {

        while (true) {

            System.out.print(mensagem);

            String entrada = scanner.nextLine().trim();

            try {

                return Integer.parseInt(entrada);

            } catch (NumberFormatException e) {

                System.out.println(
                        "Digite um número inteiro válido.");
            }
        }
    }

    private double lerDouble(String mensagem) {

        while (true) {

            System.out.print(mensagem);

            String entrada = scanner.nextLine()
                    .trim()
                    .replace(',', '.');

            try {

                return Double.parseDouble(entrada);

            } catch (NumberFormatException e) {

                System.out.println(
                        "Digite um número válido.");
            }
        }
    }
}
