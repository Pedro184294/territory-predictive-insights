package br.com.territory;

import br.com.territory.dominio.SensorTemperatura;
import br.com.territory.dominio.SensorConsumoEnergia;
import br.com.territory.dominio.SensorUmidade;
import br.com.territory.exceptions.DadosInvalidosException;
import br.com.territory.exceptions.FonteIndisponivelException;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SensoresTest {

    @Test
    void deveRealizarLeituraDeTemperatura()
            throws DadosInvalidosException, FonteIndisponivelException {

        SensorTemperatura sensor = new SensorTemperatura(-5.0);

        assertEquals(-5.0, sensor.realizarLeitura());
    }

    @Test
    void deveLancarExcecaoQuandoSensorTemperaturaEstiverIndisponivel()
            throws DadosInvalidosException {

        SensorTemperatura sensor = new SensorTemperatura(30.0);
        sensor.setDisponivel(false);

        assertThrows(
                FonteIndisponivelException.class,
                sensor::realizarLeitura);
    }

    @Test
    void deveRejeitarConsumoNegativo() {

        assertThrows(
                DadosInvalidosException.class,
                () -> new SensorConsumoEnergia(-100.0));
    }

    @Test
    void deveLancarExcecaoQuandoSensorEnergiaEstiverIndisponivel()
            throws DadosInvalidosException {

        SensorConsumoEnergia sensor = new SensorConsumoEnergia(500.0);
        sensor.setDisponivel(false);

        assertThrows(
                FonteIndisponivelException.class,
                sensor::realizarLeitura);
    }

    @Test
    void deveRejeitarUmidadeAcimaDeCem() {

        assertThrows(
                DadosInvalidosException.class,
                () -> new SensorUmidade(120.0));
    }

    @Test
    void deveRealizarLeituraDeUmidade()
            throws DadosInvalidosException, FonteIndisponivelException {

        SensorUmidade sensor = new SensorUmidade(65.0);

        assertEquals(65.0, sensor.realizarLeitura());
    }
}