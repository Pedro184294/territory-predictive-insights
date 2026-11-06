package br.com.territory.validation;

import br.com.territory.exceptions.DadosInvalidosException;

public final class ValidadorDados {

    private ValidadorDados() {
        // Impede a criação de objetos desta classe utilitária.
    }

    public static void validarTemperatura(double temperatura)
            throws DadosInvalidosException {

        if (!Double.isFinite(temperatura)) {
            throw new DadosInvalidosException(
                    "A temperatura deve ser um número válido.");
        }
    }

    public static void validarConsumoEnergia(double consumo)
            throws DadosInvalidosException {

        if (!Double.isFinite(consumo) || consumo < 0) {
            throw new DadosInvalidosException(
                    "O consumo de energia não pode ser negativo ou inválido.");
        }
    }

    public static void validarUmidade(double umidade)
            throws DadosInvalidosException {

        if (!Double.isFinite(umidade) || umidade < 0 || umidade > 100) {
            throw new DadosInvalidosException(
                    "A umidade deve estar entre 0% e 100%.");
        }
    }

    public static void validarLimite(double limite)
            throws DadosInvalidosException {

        if (!Double.isFinite(limite) || limite <= 0) {
            throw new DadosInvalidosException(
                    "O limite deve ser um número positivo e válido.");
        }
    }
}