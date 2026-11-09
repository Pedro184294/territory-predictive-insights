package br.com.territory;

import br.com.territory.persistencia.AlertaDAO;
import br.com.territory.persistencia.BancoDados;
import br.com.territory.persistencia.MedicaoDAO;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class PersistenciaTest {

    @BeforeAll
    static void configurarBanco() throws SQLException {
        BancoDados.inicializar();
    }

    @Test
    void deveSalvarEConsultarMedicao() throws SQLException {

        // Arrange
        MedicaoDAO dao = new MedicaoDAO();
        String regiaoId = UUID.randomUUID().toString();

        // Act
        dao.salvar(
                regiaoId,
                37.5,
                65.0,
                810.0,
                LocalDateTime.now());

        List<String> medicoes = dao.listarPorRegiao(regiaoId);

        // Assert
        assertEquals(1, medicoes.size());
        assertTrue(medicoes.get(0).contains(regiaoId));
        assertTrue(medicoes.get(0).contains("Temperatura"));
    }

    @Test
    void deveSalvarEConsultarAlerta() throws SQLException {

        // Arrange
        AlertaDAO dao = new AlertaDAO();
        String regiaoId = UUID.randomUUID().toString();

        // Act
        dao.salvar(
                regiaoId,
                "ALERTA",
                "Temperatura acima do nível esperado.",
                LocalDateTime.now());

        List<String> alertas = dao.listarPorRegiao(regiaoId);

        // Assert
        assertEquals(1, alertas.size());
        assertTrue(alertas.get(0).contains(regiaoId));
        assertTrue(alertas.get(0).contains("ALERTA"));
        assertTrue(alertas.get(0).contains("Temperatura acima"));
    }

    @Test
    void deveInicializarBancoMaisDeUmaVez() throws SQLException {

        // Arrange
        // O banco já foi inicializado pelo BeforeAll.

        // Act
        BancoDados.inicializar();

        // Assert
        assertDoesNotThrow(BancoDados::inicializar);
    }
}