
package br.com.territory;

import br.com.territory.dominio.RegiaoMonitorada;
import br.com.territory.exceptions.DadosInvalidosException;
import br.com.territory.persistencia.AlertaDAO;
import br.com.territory.persistencia.MedicaoDAO;
import br.com.territory.servicos.MonitoramentoService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
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

    @Test
    void deveInformarLimiteIntermediarioNaDescricaoDoAlerta()
            throws Exception {

        // Arrange
        RegiaoMonitorada regiao = criarRegiao(38.0, 500.0, 60.0);

        // Act
        service.registrarMonitoramento(regiao);

        // Assert
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);

        verify(alertaDAO).salvar(
                eq(regiao.getId()),
                eq("ALERTA"),
                captor.capture(),
                any());

        String descricao = captor.getValue();

        assertTrue(descricao.contains("Temperatura: 38.00 °C"));
        assertTrue(descricao.contains("ALERTA"));
        assertTrue(descricao.contains("limite intermediário"));
        assertTrue(descricao.contains("37.50 °C"));
    }

    @Test
    void deveIdentificarTemperaturaEConsumoNaDescricaoDoAlerta()
            throws Exception {

        // Arrange
        RegiaoMonitorada regiao = criarRegiao(41.0, 950.0, 60.0);

        // Act
        service.registrarMonitoramento(regiao);

        // Assert
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);

        verify(alertaDAO).salvar(
                eq(regiao.getId()),
                eq("CRÍTICO"),
                captor.capture(),
                any());

        String descricao = captor.getValue();

        assertTrue(descricao.contains("Temperatura: 41.00 °C"));
        assertTrue(descricao.contains("Consumo de energia: 950.00 kWh"));
        assertTrue(descricao.contains("limite crítico"));
        assertTrue(descricao.contains("40.00 °C"));
        assertTrue(descricao.contains("900.00 kWh"));
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
