
package br.com.territory.dominio;

import br.com.territory.exceptions.DadosInvalidosException;
import br.com.territory.exceptions.FonteIndisponivelException;
import br.com.territory.validation.ValidadorDados;

public class SensorConsumoEnergia implements Sensor {

    private double consumo;
    private boolean disponivel;

    public SensorConsumoEnergia(double consumo)
            throws DadosInvalidosException {

        ValidadorDados.validarConsumoEnergia(consumo);

        this.consumo = consumo;
        this.disponivel = true;
    }

    @Override
    public double realizarLeitura() throws FonteIndisponivelException {

        if (!verificarFuncionamento()) {
            throw new FonteIndisponivelException(
                    "Sensor de consumo de energia indisponível.");
        }

        return consumo;
    }

    @Override
    public boolean verificarFuncionamento() {
        return disponivel;
    }

    @Override
    public String getTipo() {
        return "Consumo de energia";
    }

    @Override
    public String getUnidadeMedida() {
        return "kWh";
    }

    public void setConsumo(double consumo)
            throws DadosInvalidosException {

        ValidadorDados.validarConsumoEnergia(consumo);
        this.consumo = consumo;
    }

    public void setDisponivel(boolean disponivel) {
        this.disponivel = disponivel;
    }
}
