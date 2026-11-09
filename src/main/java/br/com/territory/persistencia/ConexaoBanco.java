package br.com.territory.persistencia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class ConexaoBanco {

    private static final String URL = "jdbc:h2:file:./data/territorydb;DB_CLOSE_ON_EXIT=FALSE";

    private static final String USUARIO = "sa";
    private static final String SENHA = "";

    private ConexaoBanco() {
        // Impede a instanciação desta classe utilitária.
    }

    public static Connection obterConexao() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, SENHA);
    }
}