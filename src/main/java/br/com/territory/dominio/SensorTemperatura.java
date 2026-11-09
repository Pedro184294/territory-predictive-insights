
package br.com.territory.dominio;

import br.com.territory.exceptions.DadosInvalidosException;
import br.com.territory.exceptions.FonteIndisponivelException;
import br.com.territory.validation.ValidadorDados;

public class SensorTemperatura implements Sensor {

    private double temperatura;
    private boolean disponivel;

    public SensorTemperatura(double temperatura)
            throws DadosInvalidosException {

        ValidadorDados.validarTemperatura(temperatura);

        this.temperatura = temperatura;
        this.disponivel = true;
    }

    @Override
    public double realizarLeitura() throws FonteIndisponivelException {

        if (!verificarFuncionamento()) {
            throw new FonteIndisponivelException(
                    "Sensor de temperatura indisponível.");
        }

        return temperatura;
    }

    @Override
    public boolean verificarFuncionamento() {
        return disponivel;
    }

    @Override
    public String getTipo() {
        return "Temperatura";
    }

    @Override
    public String getUnidadeMedida() {
        return "°C";
    }

    public void setTemperatura(double temperatura)
            throws DadosInvalidosException {

        ValidadorDados.validarTemperatura(temperatura);
        this.temperatura = temperatura;
    }

    public void setDisponivel(boolean disponivel) {
        this.disponivel = disponivel;
    }
}
