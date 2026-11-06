
package br.com.territory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import br.com.territory.dominio.DadosTerritorio;
import br.com.territory.dominio.RegiaoMonitorada;
import br.com.territory.exceptions.DadosInvalidosException;

class RegiaoMonitoradaTest {

    @Test
    void deveCriarRegiaoComMedicoesValidas() throws DadosInvalidosException {
        RegiaoMonitorada regiao = new RegiaoMonitorada(
                "R001", "Região Central", "São Paulo",
                25.0, 500.0, 60.0);

        assertEquals(25.0, regiao.getTemperatura());
        assertEquals(500.0, regiao.getConsumoEnergia());
        assertEquals(60.0, regiao.getUmidade());
    }

    @Test
    void deveRetornarStatusNormal() throws DadosInvalidosException {
        RegiaoMonitorada regiao = new RegiaoMonitorada(
                "R002", "Região Norte", "São Paulo",
                25.0, 500.0, 60.0);

        assertEquals("NORMAL", regiao.verificarStatus());
    }

    @Test
    void deveRetornarStatusCritico() throws DadosInvalidosException {
        RegiaoMonitorada regiao = new RegiaoMonitorada(
                "R003", "Região Sul", "São Paulo",
                42.0, 500.0, 60.0);

        assertEquals("CRÍTICO", regiao.verificarStatus());
    }

    @Test
    void deveIdentificarAtencaoComLimitesPersonalizados()
            throws DadosInvalidosException {

        DadosTerritorio dados = new DadosTerritorio(
                30.0, 40.0, 600.0, 800.0);

        RegiaoMonitorada regiao = new RegiaoMonitorada(
                "R004", "Região Leste", "São Paulo",
                32.0, 500.0, 60.0, dados);

        assertEquals("ATENÇÃO", regiao.verificarStatus());
    }

    @Test
    void deveIdentificarCriticoComLimitesPersonalizados()
            throws DadosInvalidosException {

        DadosTerritorio dados = new DadosTerritorio(
                30.0, 40.0, 600.0, 800.0);

        RegiaoMonitorada regiao = new RegiaoMonitorada(
                "R005", "Região Oeste", "São Paulo",
                41.0, 500.0, 60.0, dados);

        assertEquals("CRÍTICO", regiao.verificarStatus());
    }

    @Test
    void deveAtualizarStatusQuandoLimiteForAlterado()
            throws DadosInvalidosException {

        DadosTerritorio dados = new DadosTerritorio(
                30.0, 40.0, 600.0, 800.0);

        RegiaoMonitorada regiao = new RegiaoMonitorada(
                "R006", "Região Metropolitana", "São Paulo",
                32.0, 500.0, 60.0, dados);

        assertEquals("ATENÇÃO", regiao.verificarStatus());

        dados.setLimiteAtencaoTemperatura(35.0, "TERRITORY2026");

        assertEquals("NORMAL", regiao.verificarStatus());
    }

    @Test
    void deveIdentificarAlertaPorTemperatura()
            throws DadosInvalidosException {

        DadosTerritorio dados = new DadosTerritorio(
                30.0, 40.0, 600.0, 800.0);

        RegiaoMonitorada regiao = new RegiaoMonitorada(
                "R007", "Região de Teste", "São Paulo",
                37.0, 500.0, 60.0, dados);

        assertEquals("ALERTA", regiao.verificarStatus());
    }

    @Test
    void deveIdentificarAlertaPorConsumoDeEnergia()
            throws DadosInvalidosException {

        DadosTerritorio dados = new DadosTerritorio(
                30.0, 40.0, 600.0, 800.0);

        RegiaoMonitorada regiao = new RegiaoMonitorada(
                "R008", "Região de Teste", "São Paulo",
                25.0, 750.0, 60.0, dados);

        assertEquals("ALERTA", regiao.verificarStatus());
    }

    @Test
    void deveLancarExcecaoQuandoIdForVazio() {

        assertThrows(DadosInvalidosException.class, () -> new RegiaoMonitorada(
                "", "Região Central", "São Paulo",
                25.0, 500.0, 60.0));
    }

    @Test
    void deveLancarExcecaoQuandoNomeForNulo() {

        assertThrows(DadosInvalidosException.class, () -> new RegiaoMonitorada(
                "R009", null, "São Paulo",
                25.0, 500.0, 60.0));
    }

    @Test
    void deveLancarExcecaoQuandoRegiaoForVazia() {

        assertThrows(DadosInvalidosException.class, () -> new RegiaoMonitorada(
                "R010", "Região Central", " ",
                25.0, 500.0, 60.0));
    }
}
