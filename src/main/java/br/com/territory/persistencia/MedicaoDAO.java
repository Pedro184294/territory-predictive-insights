
package br.com.territory.persistencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MedicaoDAO {

    public void salvar(
            String regiaoId,
            double temperatura,
            double umidade,
            double consumoEnergia,
            LocalDateTime dataHora) throws SQLException {

        String sqlMedicao = """
                INSERT INTO medicoes
                (regiao_id, temperatura, umidade, consumo_energia, data_hora)
                VALUES (?, ?, ?, ?, ?)
                """;

        String sqlSensor = """
                INSERT INTO leituras_sensor
                (regiao_id, tipo_sensor, valor, unidade, data_hora)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (Connection conexao = ConexaoBanco.obterConexao()) {

            conexao.setAutoCommit(false);

            try {
                // Salva a medição completa da região.
                try (PreparedStatement statement = conexao.prepareStatement(sqlMedicao)) {

                    statement.setString(1, regiaoId);
                    statement.setDouble(2, temperatura);
                    statement.setDouble(3, umidade);
                    statement.setDouble(4, consumoEnergia);
                    statement.setTimestamp(
                            5, Timestamp.valueOf(dataHora));

                    statement.executeUpdate();
                }

                // Salva individualmente cada leitura de sensor.
                try (PreparedStatement statement = conexao.prepareStatement(sqlSensor)) {

                    // Sensor de temperatura.
                    adicionarLeitura(
                            statement,
                            regiaoId,
                            "TEMPERATURA",
                            temperatura,
                            "°C",
                            dataHora);

                    // Sensor de umidade.
                    adicionarLeitura(
                            statement,
                            regiaoId,
                            "UMIDADE",
                            umidade,
                            "%",
                            dataHora);

                    // Sensor de consumo de energia.
                    adicionarLeitura(
                            statement,
                            regiaoId,
                            "CONSUMO_ENERGIA",
                            consumoEnergia,
                            "kWh",
                            dataHora);

                    statement.executeBatch();
                }

                conexao.commit();

            } catch (SQLException e) {

                conexao.rollback();
                throw e;
            }
        }
    }

    private void adicionarLeitura(
            PreparedStatement statement,
            String regiaoId,
            String tipoSensor,
            double valor,
            String unidade,
            LocalDateTime dataHora) throws SQLException {

        statement.setString(1, regiaoId);
        statement.setString(2, tipoSensor);
        statement.setDouble(3, valor);
        statement.setString(4, unidade);
        statement.setTimestamp(
                5, Timestamp.valueOf(dataHora));

        statement.addBatch();
    }

    public List<String> listarPorRegiao(String regiaoId)
            throws SQLException {

        List<String> medicoes = new ArrayList<>();

        String sql = """
                SELECT * FROM medicoes
                WHERE regiao_id = ?
                ORDER BY data_hora DESC
                """;

        try (Connection conexao = ConexaoBanco.obterConexao();
                PreparedStatement statement = conexao.prepareStatement(sql)) {

            statement.setString(1, regiaoId);

            try (ResultSet resultado = statement.executeQuery()) {

                while (resultado.next()) {

                    String medicao = String.format(
                            "ID: %d | Região: %s | Temperatura: %.2f °C | "
                                    + "Umidade: %.2f %% | Consumo: %.2f kWh | Data: %s",
                            resultado.getLong("id"),
                            resultado.getString("regiao_id"),
                            resultado.getDouble("temperatura"),
                            resultado.getDouble("umidade"),
                            resultado.getDouble("consumo_energia"),
                            resultado.getTimestamp("data_hora")
                                    .toLocalDateTime());

                    medicoes.add(medicao);
                }
            }
        }

        return medicoes;
    }

    public List<String> listarPorSensor(
            String regiaoId,
            String tipoSensor) throws SQLException {

        List<String> medicoes = new ArrayList<>();

        if (regiaoId == null || regiaoId.isBlank()) {
            throw new IllegalArgumentException(
                    "O ID da região é obrigatório.");
        }

        if (tipoSensor == null || tipoSensor.isBlank()) {
            throw new IllegalArgumentException(
                    "O tipo de sensor é obrigatório.");
        }

        String tipoNormalizado;

        switch (tipoSensor.trim().toUpperCase(Locale.ROOT)) {

            case "TEMPERATURA" ->
                tipoNormalizado = "TEMPERATURA";

            case "UMIDADE" ->
                tipoNormalizado = "UMIDADE";

            case "CONSUMO", "CONSUMO_ENERGIA" ->
                tipoNormalizado = "CONSUMO_ENERGIA";

            default -> throw new IllegalArgumentException(
                    "Tipo de sensor inválido. "
                            + "Utilize TEMPERATURA, UMIDADE ou CONSUMO.");
        }

        String sql = """
                SELECT id, regiao_id, tipo_sensor, valor,
                       unidade, data_hora
                FROM leituras_sensor
                WHERE regiao_id = ?
                  AND tipo_sensor = ?
                ORDER BY data_hora DESC
                """;

        try (Connection conexao = ConexaoBanco.obterConexao();
                PreparedStatement statement = conexao.prepareStatement(sql)) {

            statement.setString(1, regiaoId);
            statement.setString(2, tipoNormalizado);

            try (ResultSet resultado = statement.executeQuery()) {

                while (resultado.next()) {

                    String medicao = String.format(
                            "ID: %d | Região: %s | Sensor: %s | "
                                    + "Valor: %.2f %s | Data: %s",
                            resultado.getLong("id"),
                            resultado.getString("regiao_id"),
                            resultado.getString("tipo_sensor"),
                            resultado.getDouble("valor"),
                            resultado.getString("unidade"),
                            resultado.getTimestamp("data_hora")
                                    .toLocalDateTime());

                    medicoes.add(medicao);
                }
            }
        }

        return medicoes;
    }

    // Busca os valores históricos de um sensor
    // em ordem cronológica, da leitura mais antiga à mais recente.
    public List<Double> buscarValoresPorSensor(
            String regiaoId,
            String tipoSensor) throws SQLException {

        if (regiaoId == null || regiaoId.isBlank()) {
            throw new IllegalArgumentException(
                    "O ID da região é obrigatório.");
        }

        if (tipoSensor == null || tipoSensor.isBlank()) {
            throw new IllegalArgumentException(
                    "O tipo de sensor é obrigatório.");
        }

        String tipoNormalizado;

        switch (tipoSensor.trim().toUpperCase(Locale.ROOT)) {

            case "TEMPERATURA" ->
                tipoNormalizado = "TEMPERATURA";

            case "UMIDADE" ->
                tipoNormalizado = "UMIDADE";

            case "CONSUMO", "CONSUMO_ENERGIA" ->
                tipoNormalizado = "CONSUMO_ENERGIA";

            default -> throw new IllegalArgumentException(
                    "Tipo de sensor inválido.");
        }

        List<Double> valores = new ArrayList<>();

        String sql = """
                SELECT valor
                FROM leituras_sensor
                WHERE regiao_id = ?
                  AND tipo_sensor = ?
                ORDER BY data_hora ASC, id ASC
                """;

        try (Connection conexao = ConexaoBanco.obterConexao();
                PreparedStatement statement = conexao.prepareStatement(sql)) {

            statement.setString(1, regiaoId);
            statement.setString(2, tipoNormalizado);

            try (ResultSet resultado = statement.executeQuery()) {

                while (resultado.next()) {
                    valores.add(resultado.getDouble("valor"));
                }
            }
        }

        return valores;
    }
}
