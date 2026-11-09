
package br.com.territory.servicos;

import br.com.territory.dominio.DadosTerritorio;
import br.com.territory.dominio.RegiaoMonitorada;
import br.com.territory.exceptions.DadosInvalidosException;
import br.com.territory.exceptions.FonteIndisponivelException;
import br.com.territory.persistencia.AlertaDAO;
import br.com.territory.persistencia.MedicaoDAO;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class MonitoramentoService {

    private final MedicaoDAO medicaoDAO;
    private final AlertaDAO alertaDAO;

    public MonitoramentoService() {
        this(new MedicaoDAO(), new AlertaDAO());
    }

    // Construtor que permite utilizar mocks nos testes com Mockito.
    public MonitoramentoService(
            MedicaoDAO medicaoDAO,
            AlertaDAO alertaDAO) {

        this.medicaoDAO = Objects.requireNonNull(medicaoDAO);
        this.alertaDAO = Objects.requireNonNull(alertaDAO);
    }

    // Registra as medições e gera alertas conforme o status da região.
    public String registrarMonitoramento(RegiaoMonitorada regiao)
            throws SQLException {

        Objects.requireNonNull(
                regiao,
                "A região monitorada é obrigatória.");

        LocalDateTime dataHora = LocalDateTime.now();

        medicaoDAO.salvar(
                regiao.getId(),
                regiao.getTemperatura(),
                regiao.getUmidade(),
                regiao.getConsumoEnergia(),
                dataHora);

        String status = regiao.verificarStatus();

        if (!status.equals("NORMAL")
                && !status.equals("INATIVO")) {

            String descricao = gerarDescricaoAlerta(regiao);

            alertaDAO.salvar(
                    regiao.getId(),
                    status,
                    descricao,
                    dataHora);
        }

        return status;
    }

    // Identifica os indicadores responsáveis pelo alerta.
    private String gerarDescricaoAlerta(RegiaoMonitorada regiao) {

        DadosTerritorio dados = regiao.getDadosTerritorio();

        StringBuilder causas = new StringBuilder();

        adicionarCausa(
                causas,
                "Temperatura",
                regiao.getTemperatura(),
                "°C",
                dados.getLimiteAtencaoTemperatura(),
                dados.getLimiteCriticoTemperatura());

        adicionarCausa(
                causas,
                "Consumo de energia",
                regiao.getConsumoEnergia(),
                "kWh",
                dados.getLimiteAtencaoConsumo(),
                dados.getLimiteCriticoConsumo());

        return String.format(
                Locale.ROOT,
                "Causas do alerta: %s | Umidade registrada: %.2f %%",
                causas,
                regiao.getUmidade());
    }

    // Acrescenta a descrição de cada indicador que atingiu um limite.
    private void adicionarCausa(
            StringBuilder causas,
            String indicador,
            double valor,
            String unidade,
            double limiteAtencao,
            double limiteCritico) {

        if (valor < limiteAtencao) {
            return;
        }

        double limiteAlerta = (limiteAtencao + limiteCritico) / 2.0;

        String nivel;
        String descricaoLimite;
        double limiteAtingido;

        if (valor >= limiteCritico) {
            nivel = "CRÍTICO";
            descricaoLimite = "limite crítico";
            limiteAtingido = limiteCritico;

        } else if (valor >= limiteAlerta) {
            nivel = "ALERTA";
            descricaoLimite = "limite intermediário";
            limiteAtingido = limiteAlerta;

        } else {
            nivel = "ATENÇÃO";
            descricaoLimite = "limite de atenção";
            limiteAtingido = limiteAtencao;
        }

        if (!causas.isEmpty()) {
            causas.append("; ");
        }

        causas.append(String.format(
                Locale.ROOT,
                "%s: %.2f %s (%s; %s: %.2f %s)",
                indicador,
                valor,
                unidade,
                nivel,
                descricaoLimite,
                limiteAtingido,
                unidade));
    }

    // Executa uma estratégia de previsão utilizando o histórico do banco.
    public String preverSensor(
            String regiaoId,
            String tipoSensor,
            String nomeEstrategia,
            DadosTerritorio dadosTerritorio)
            throws SQLException,
            DadosInvalidosException,
            FonteIndisponivelException {

        Objects.requireNonNull(
                dadosTerritorio,
                "A configuração territorial é obrigatória.");

        if (regiaoId == null || regiaoId.isBlank()) {
            throw new IllegalArgumentException(
                    "O ID da região é obrigatório.");
        }

        if (tipoSensor == null || tipoSensor.isBlank()) {
            throw new IllegalArgumentException(
                    "O tipo do sensor é obrigatório.");
        }

        String tipoNormalizado = tipoSensor.trim().toUpperCase(Locale.ROOT);

        List<Double> historico = medicaoDAO.buscarValoresPorSensor(
                regiaoId,
                tipoNormalizado);

        // A Factory centraliza a criação da estratégia escolhida.
        EstrategiaPrevisao estrategia = FabricaPrevisao.criar(
                nomeEstrategia,
                tipoNormalizado);

        double valorPrevisto = estrategia.prever(historico);

        String unidade;
        double limiteAtencao;
        double limiteCritico;

        switch (tipoNormalizado) {

            case "TEMPERATURA" -> {
                unidade = "°C";
                limiteAtencao = dadosTerritorio.getLimiteAtencaoTemperatura();
                limiteCritico = dadosTerritorio.getLimiteCriticoTemperatura();
            }

            case "CONSUMO", "CONSUMO_ENERGIA" -> {
                unidade = "kWh";
                limiteAtencao = dadosTerritorio.getLimiteAtencaoConsumo();
                limiteCritico = dadosTerritorio.getLimiteCriticoConsumo();
            }

            default -> throw new IllegalArgumentException(
                    "A previsão com classificação de alertas está "
                            + "disponível para temperatura e consumo de energia.");
        }

        String nivel = classificarPrevisao(
                valorPrevisto,
                limiteAtencao,
                limiteCritico);

        double limiteAlerta = (limiteAtencao + limiteCritico) / 2.0;

        String descricaoLimite;

        if (valorPrevisto >= limiteCritico) {
            descricaoLimite = "limite crítico";

        } else if (valorPrevisto >= limiteAlerta) {
            descricaoLimite = "limite intermediário";

        } else if (valorPrevisto >= limiteAtencao) {
            descricaoLimite = "limite de atenção";

        } else {
            descricaoLimite = "nenhum limite de alerta atingido";
        }

        String descricao = String.format(
                Locale.ROOT,
                "Previsão de %s pela estratégia %s: %.2f %s | "
                        + "Classificação: %s | "
                        + "Resultado: %s | "
                        + "Limite de atenção: %.2f %s | "
                        + "Limite intermediário: %.2f %s | "
                        + "Limite crítico: %.2f %s",
                tipoNormalizado,
                nomeEstrategia,
                valorPrevisto,
                unidade,
                nivel,
                descricaoLimite,
                limiteAtencao,
                unidade,
                limiteAlerta,
                unidade,
                limiteCritico,
                unidade);

        // Registra no banco somente previsões que geram alertas.
        if (!nivel.equals("NORMAL")) {

            alertaDAO.salvar(
                    regiaoId,
                    nivel,
                    descricao,
                    LocalDateTime.now());
        }

        return String.format(
                Locale.ROOT,
                "Sensor: %s | Estratégia: %s | "
                        + "Valor previsto: %.2f %s | "
                        + "Classificação: %s",
                tipoNormalizado,
                nomeEstrategia,
                valorPrevisto,
                unidade,
                nivel);
    }

    // Classifica a previsão de acordo com os limites configurados.
    private String classificarPrevisao(
            double valorPrevisto,
            double limiteAtencao,
            double limiteCritico) {

        if (valorPrevisto >= limiteCritico) {
            return "CRÍTICO";
        }

        double limiteAlerta = (limiteAtencao + limiteCritico) / 2.0;

        if (valorPrevisto >= limiteAlerta) {
            return "ALERTA";
        }

        if (valorPrevisto >= limiteAtencao) {
            return "ATENÇÃO";
        }

        return "NORMAL";
    }
}
