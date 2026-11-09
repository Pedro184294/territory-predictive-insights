package br.com.territory.persistencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AlertaDAO {

    public void salvar(
            String regiaoId,
            String nivel,
            String descricao,
            LocalDateTime dataHora) throws SQLException {

        String sql = """
                INSERT INTO alertas
                (regiao_id, nivel, descricao, data_hora)
                VALUES (?, ?, ?, ?)
                """;

        try (Connection conexao = ConexaoBanco.obterConexao();
                PreparedStatement statement = conexao.prepareStatement(sql)) {

            statement.setString(1, regiaoId);
            statement.setString(2, nivel);
            statement.setString(3, descricao);
            statement.setTimestamp(4, Timestamp.valueOf(dataHora));

            statement.executeUpdate();
        }
    }

    public List<String> listarPorRegiao(String regiaoId)
            throws SQLException {

        List<String> alertas = new ArrayList<>();

        String sql = """
                SELECT * FROM alertas
                WHERE regiao_id = ?
                ORDER BY data_hora DESC
                """;

        try (Connection conexao = ConexaoBanco.obterConexao();
                PreparedStatement statement = conexao.prepareStatement(sql)) {

            statement.setString(1, regiaoId);

            try (ResultSet resultado = statement.executeQuery()) {

                while (resultado.next()) {

                    String alerta = String.format(
                            "ID: %d | Região: %s | Nível: %s | "
                                    + "Descrição: %s | Data: %s",
                            resultado.getLong("id"),
                            resultado.getString("regiao_id"),
                            resultado.getString("nivel"),
                            resultado.getString("descricao"),
                            resultado.getTimestamp("data_hora").toLocalDateTime());

                    alertas.add(alerta);
                }
            }
        }

        return alertas;
    }
}