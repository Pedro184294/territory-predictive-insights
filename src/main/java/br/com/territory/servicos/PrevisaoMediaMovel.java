
package br.com.territory.servicos;

import br.com.territory.exceptions.DadosInvalidosException;
import br.com.territory.exceptions.FonteIndisponivelException;

import java.util.List;

public class PrevisaoMediaMovel extends EstrategiaPrevisao {

    private static final int QUANTIDADE_LEITURAS = 5;

    public PrevisaoMediaMovel(String tipoSensor) {
        super(tipoSensor);
    }

    @Override
    public double prever(List<Double> historico)
            throws DadosInvalidosException, FonteIndisponivelException {

        List<Double> valores = validarHistorico(historico);

        int inicio = Math.max(
                0,
                valores.size() - QUANTIDADE_LEITURAS);

        List<Double> ultimasLeituras = valores.subList(
                inicio,
                valores.size());

        double soma = 0.0;

        for (double valor : ultimasLeituras) {
            soma += valor;
        }

        return soma / ultimasLeituras.size();
    }
}
