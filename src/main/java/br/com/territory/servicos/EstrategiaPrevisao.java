
package br.com.territory.servicos;

import br.com.territory.exceptions.DadosInvalidosException;
import br.com.territory.exceptions.FonteIndisponivelException;

import java.util.ArrayList;
import java.util.List;

public abstract class EstrategiaPrevisao {

    private final String tipoSensor;

    protected EstrategiaPrevisao(String tipoSensor) {

        if (tipoSensor == null || tipoSensor.isBlank()) {
            throw new IllegalArgumentException(
                    "O tipo do sensor é obrigatório.");
        }

        this.tipoSensor = tipoSensor.trim();
    }

    public String getTipoSensor() {
        return tipoSensor;
    }

    public abstract double prever(List<Double> historico)
            throws DadosInvalidosException, FonteIndisponivelException;

    protected List<Double> validarHistorico(List<Double> historico)
            throws DadosInvalidosException, FonteIndisponivelException {

        if (historico == null || historico.isEmpty()) {
            throw new FonteIndisponivelException(
                    "Não há histórico disponível para realizar a previsão.");
        }

        List<Double> historicoValidado = new ArrayList<>();

        for (Double valor : historico) {

            if (valor == null || !Double.isFinite(valor)) {
                throw new DadosInvalidosException(
                        "O histórico contém uma medição inválida.");
            }

            historicoValidado.add(valor);
        }

        return historicoValidado;
    }
}
