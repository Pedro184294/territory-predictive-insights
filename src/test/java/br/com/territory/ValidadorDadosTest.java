package br.com.territory;

import br.com.territory.exceptions.DadosInvalidosException;
import br.com.territory.validation.ValidadorDados;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ValidadorDadosTest {

    @Test
    void deveAceitarTemperaturaNegativa() {
        assertDoesNotThrow(
                () -> ValidadorDados.validarTemperatura(-5.0));
    }

    @Test
    void deveRejeitarTemperaturaInvalida() {
        assertThrows(
                DadosInvalidosException.class,
                () -> ValidadorDados.validarTemperatura(Double.NaN));
    }

    @Test
    void deveAceitarConsumoPositivo() {
        assertDoesNotThrow(
                () -> ValidadorDados.validarConsumoEnergia(500.0));
    }

    @Test
    void deveRejeitarConsumoNegativo() {
        assertThrows(
                DadosInvalidosException.class,
                () -> ValidadorDados.validarConsumoEnergia(-10.0));
    }

    @Test
    void deveAceitarUmidadeNosLimites() {
        assertDoesNotThrow(
                () -> ValidadorDados.validarUmidade(0.0));

        assertDoesNotThrow(
                () -> ValidadorDados.validarUmidade(100.0));
    }

    @Test
    void deveRejeitarUmidadeForaDoIntervalo() {
        assertThrows(
                DadosInvalidosException.class,
                () -> ValidadorDados.validarUmidade(120.0));
    }

    @Test
    void deveRejeitarLimiteZero() {
        assertThrows(
                DadosInvalidosException.class,
                () -> ValidadorDados.validarLimite(0.0));
    }
}