package br.com.territory.dominio;

import br.com.territory.exceptions.DadosInvalidosException;
import br.com.territory.exceptions.FonteIndisponivelException;
import br.com.territory.validation.ValidadorDados;

public class SensorUmidade implements Sensor {

    private double umidade;
    private boolean disponivel;

    public SensorUmidade(double umidade)
            throws DadosInvalidosException {

        ValidadorDados.validarUmidade(umidade);

        this.umidade = umidade;
        this.disponivel = true;
    }

    @Override
    public double realizarLeitura() throws FonteIndisponivelException {

        if (!disponivel) {
            throw new FonteIndisponivelException(
                    "Sensor de umidade indisponível.");
        }

        return umidade;
    }

    @Override
    public String getTipo() {
        return "Umidade";
    }

    @Override
    public String getUnidadeMedida() {
        return "%";
    }

    public void setUmidade(double umidade)
            throws DadosInvalidosException {

        ValidadorDados.validarUmidade(umidade);
        this.umidade = umidade;
    }

    public void setDisponivel(boolean disponivel) {
        this.disponivel = disponivel;
    }
}