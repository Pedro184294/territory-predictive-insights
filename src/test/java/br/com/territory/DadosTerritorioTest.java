
package br.com.territory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import br.com.territory.dominio.DadosTerritorio;
import br.com.territory.exceptions.DadosInvalidosException;

class DadosTerritorioTest {

    private static final String CODIGO_CORRETO = "TERRITORY2026";
    private static final String CODIGO_INCORRETO = "123456";

    @Test
    void deveCriarTerritorioComLimitesPadrao() throws DadosInvalidosException {
        DadosTerritorio dados = new DadosTerritorio();

        assertEquals(35.0, dados.getLimiteAtencaoTemperatura());
        assertEquals(40.0, dados.getLimiteCriticoTemperatura());
        assertEquals(700.0, dados.getLimiteAtencaoConsumo());
        assertEquals(900.0, dados.getLimiteCriticoConsumo());
    }

    @Test
    void devePermitirAlterarLimiteDeTemperaturaComCodigoCorreto()
            throws DadosInvalidosException {

        DadosTerritorio dados = new DadosTerritorio();

        dados.setLimiteAtencaoTemperatura(30.0, CODIGO_CORRETO);

        assertEquals(30.0, dados.getLimiteAtencaoTemperatura());
    }

    @Test
    void devePermitirAlterarLimiteDeConsumoComCodigoCorreto()
            throws DadosInvalidosException {

        DadosTerritorio dados = new DadosTerritorio();

        dados.setLimiteAtencaoConsumo(600.0, CODIGO_CORRETO);

        assertEquals(600.0, dados.getLimiteAtencaoConsumo());
    }

    @Test
    void deveBloquearAlteracaoComCodigoIncorreto()
            throws DadosInvalidosException {

        DadosTerritorio dados = new DadosTerritorio();

        assertThrows(DadosInvalidosException.class,
                () -> dados.setLimiteAtencaoTemperatura(
                        30.0, CODIGO_INCORRETO));

        assertEquals(35.0, dados.getLimiteAtencaoTemperatura());
    }

    @Test
    void deveLancarExcecaoParaLimiteNegativo()
            throws DadosInvalidosException {

        DadosTerritorio dados = new DadosTerritorio();

        assertThrows(DadosInvalidosException.class,
                () -> dados.setLimiteAtencaoTemperatura(
                        -5.0, CODIGO_CORRETO));
    }

    @Test
    void deveLancarExcecaoQuandoLimiteAtencaoForMaiorQueCritico()
            throws DadosInvalidosException {

        DadosTerritorio dados = new DadosTerritorio();

        assertThrows(DadosInvalidosException.class,
                () -> dados.setLimiteAtencaoTemperatura(
                        45.0, CODIGO_CORRETO));
    }

    @Test
    void devePermitirConfigurarTodosOsLimiaresComCodigoCorreto()
            throws DadosInvalidosException {

        DadosTerritorio dados = new DadosTerritorio();

        dados.configurarLimiares(
                32.0, 42.0, 650.0, 950.0, CODIGO_CORRETO);

        assertEquals(32.0, dados.getLimiteAtencaoTemperatura());
        assertEquals(42.0, dados.getLimiteCriticoTemperatura());
        assertEquals(650.0, dados.getLimiteAtencaoConsumo());
        assertEquals(950.0, dados.getLimiteCriticoConsumo());
    }

    @Test
    void deveBloquearConfiguracaoDeLimiaresComCodigoIncorreto()
            throws DadosInvalidosException {

        DadosTerritorio dados = new DadosTerritorio();

        assertThrows(DadosInvalidosException.class,
                () -> dados.configurarLimiares(
                        32.0, 42.0, 650.0, 950.0,
                        CODIGO_INCORRETO));

        assertEquals(35.0, dados.getLimiteAtencaoTemperatura());
        assertEquals(40.0, dados.getLimiteCriticoTemperatura());
        assertEquals(700.0, dados.getLimiteAtencaoConsumo());
        assertEquals(900.0, dados.getLimiteCriticoConsumo());
    }
}
