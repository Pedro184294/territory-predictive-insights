
package br.com.territory.servicos;

import br.com.territory.exceptions.DadosInvalidosException;
import br.com.territory.exceptions.FonteIndisponivelException;

import java.util.List;

public class PrevisaoComparacao extends EstrategiaPrevisao {

    private static final int MAXIMO_LEITURAS_POR_PERIODO = 3;

    public PrevisaoComparacao(String tipoSensor) {
        super(tipoSensor);
    }

    @Override
    public double prever(List<Double> historico)
            throws DadosInvalidosException, FonteIndisponivelException {

        List<Double> valores = validarHistorico(historico);

        if (valores.size() < 4) {
            throw new FonteIndisponivelException(
                    "São necessárias pelo menos quatro medições "
                            + "para comparar os períodos.");
        }

        int quantidadePorPeriodo = Math.min(
                MAXIMO_LEITURAS_POR_PERIODO,
                valores.size() / 2);

        int inicioPeriodoRecente = valores.size() - quantidadePorPeriodo;

        int inicioPeriodoAnterior = inicioPeriodoRecente - quantidadePorPeriodo;

        double somaAnterior = 0.0;
        double somaRecente = 0.0;

        for (int i = inicioPeriodoAnterior; i < inicioPeriodoRecente; i++) {

            somaAnterior += valores.get(i);
        }

        for (int i = inicioPeriodoRecente; i < valores.size(); i++) {

            somaRecente += valores.get(i);
        }

        double mediaAnterior = somaAnterior / quantidadePorPeriodo;

        double mediaRecente = somaRecente / quantidadePorPeriodo;

        double variacao = mediaRecente - mediaAnterior;

        return mediaRecente + variacao;
    }
}
