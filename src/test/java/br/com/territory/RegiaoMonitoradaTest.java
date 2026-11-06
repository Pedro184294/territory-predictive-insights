package br.com.territory;

import br.com.territory.dominio.RegiaoMonitorada;
import br.com.territory.exceptions.DadosInvalidosException;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RegiaoMonitoradaTest {

    @Test
    void deveCriarRegiaoComMedicoesValidas() throws DadosInvalidosException {

        RegiaoMonitorada regiao = new RegiaoMonitorada(
                "001", "Centro", "Zona Norte",
                28.0, 500.0, 60.0);

        assertEquals(28.0, regiao.getTemperatura());
        assertEquals(500.0, regiao.getConsumoEnergia());
        assertEquals(60.0, regiao.getUmidade());
    }

    @Test
    void deveIdentificarStatusCritico() throws DadosInvalidosException {

        RegiaoMonitorada regiao = new RegiaoMonitorada(
                "002", "Industrial", "Zona Sul",
                42.0, 800.0, 55.0);

        assertEquals("CRÍTICO", regiao.verificarStatus());
    }

    @Test
    void devePreservarDadosQuandoAtualizacaoForInvalida()
            throws DadosInvalidosException {

        RegiaoMonitorada regiao = new RegiaoMonitorada(
                "003", "Residencial", "Zona Leste",
                28.0, 500.0, 60.0);

        assertThrows(
                DadosInvalidosException.class,
                () -> regiao.atualizarMedicoes(35.0, -100.0, 70.0));

        assertEquals(28.0, regiao.getTemperatura());
        assertEquals(500.0, regiao.getConsumoEnergia());
        assertEquals(60.0, regiao.getUmidade());
    }
}