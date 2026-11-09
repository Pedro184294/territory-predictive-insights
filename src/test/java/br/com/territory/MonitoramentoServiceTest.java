
package br.com.territory;

import br.com.territory.dominio.RegiaoMonitorada;
import br.com.territory.exceptions.DadosInvalidosException;
import br.com.territory.persistencia.AlertaDAO;
import br.com.territory.persistencia.MedicaoDAO;
import br.com.territory.servicos.MonitoramentoService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class MonitoramentoServiceTest {

    private MedicaoDAO medicaoDAO;
    private AlertaDAO alertaDAO;
    private MonitoramentoService service;

    @BeforeEach
    void configurar() {
        medicaoDAO = mock(MedicaoDAO.class);
        alertaDAO = mock(AlertaDAO.class);

        service = new MonitoramentoService(medicaoDAO, alertaDAO);
    }

    @Test
    void deveSalvarMedicaoAoRegistrarMonitoramento()
            throws Exception {

        // Arrange
        RegiaoMonitorada regiao = criarRegiao(25.0, 500.0, 60.0);

        // Act
        String status = service.registrarMonitoramento(regiao);

        // Assert
        assertEquals("NORMAL", status);

        verify(medicaoDAO, times(1)).salvar(
                eq(regiao.getId()),
                eq(25.0),
                eq(60.0),
                eq(500.0),
                any());

        verifyNoInteractions(alertaDAO);
    }

    @Test
    void deveRegistrarAlertaQuandoStatusForAlerta()
            throws Exception {

        // Arrange
        RegiaoMonitorada regiao = criarRegiao(38.0, 500.0, 60.0);

        // Act
        String status = service.registrarMonitoramento(regiao);

        // Assert
        assertEquals("ALERTA", status);

        verify(alertaDAO, times(1)).salvar(
                eq(regiao.getId()),
                eq("ALERTA"),
                anyString(),
                any());
    }

    @Test
    void deveRegistrarAlertaQuandoStatusForCritico()
            throws Exception {

        // Arrange
        RegiaoMonitorada regiao = criarRegiao(41.0, 500.0, 60.0);

        // Act
        String status = service.registrarMonitoramento(regiao);

        // Assert
        assertEquals("CRÍTICO", status);

        verify(alertaDAO, times(1)).salvar(
                eq(regiao.getId()),
                eq("CRÍTICO"),
                anyString(),
                any());
    }

    @Test
    void naoDeveRegistrarAlertaQuandoStatusForNormal()
            throws Exception {

        // Arrange
        RegiaoMonitorada regiao = criarRegiao(25.0, 500.0, 60.0);

        // Act
        service.registrarMonitoramento(regiao);

        // Assert
        verifyNoInteractions(alertaDAO);
    }

    @Test
    void naoDeveRegistrarAlertaQuandoRegiaoEstiverInativa()
            throws Exception {

        // Arrange
        RegiaoMonitorada regiao = criarRegiao(25.0, 500.0, 60.0);
        regiao.setAtivo(false);

        // Act
        String status = service.registrarMonitoramento(regiao);

        // Assert
        assertEquals("INATIVO", status);
        verifyNoInteractions(alertaDAO);
    }

    private RegiaoMonitorada criarRegiao(
            double temperatura,
            double consumo,
            double umidade) throws DadosInvalidosException {

        return new RegiaoMonitorada(
                "REG-001",
                "Região de Teste",
                "São Paulo",
                temperatura,
                consumo,
                umidade);
    }
}
