package br.com.territory.dominio;

import br.com.territory.exceptions.DadosInvalidosException;
import br.com.territory.validation.ValidadorDados;

public class RegiaoMonitorada extends ComponenteTerritorial {

    private double temperatura;
    private double consumoEnergia;
    private double umidade;

    public RegiaoMonitorada(
            String id,
            String nome,
            String regiao,
            double temperatura,
            double consumoEnergia,
            double umidade) throws DadosInvalidosException {

        super(id, nome, regiao);

        atualizarMedicoes(temperatura, consumoEnergia, umidade);
    }

    @Override
    public String verificarStatus() {

        if (!isAtivo()) {
            return "INATIVO";
        }

        if (temperatura >= 40 || consumoEnergia >= 900) {
            return "CRÍTICO";
        }

        if (temperatura >= 35 || consumoEnergia >= 700) {
            return "ATENÇÃO";
        }

        return "NORMAL";
    }

    public double getTemperatura() {
        return temperatura;
    }

    public double getConsumoEnergia() {
        return consumoEnergia;
    }

    public double getUmidade() {
        return umidade;
    }

    public void atualizarMedicoes(
            double temperatura,
            double consumoEnergia,
            double umidade) throws DadosInvalidosException {

        // Valida todas as medições antes de alterar os atributos.
        ValidadorDados.validarTemperatura(temperatura);
        ValidadorDados.validarConsumoEnergia(consumoEnergia);
        ValidadorDados.validarUmidade(umidade);

        this.temperatura = temperatura;
        this.consumoEnergia = consumoEnergia;
        this.umidade = umidade;
    }

    @Override
    public void exibirInformacoes() {

        super.exibirInformacoes();

        System.out.println("Temperatura: " + temperatura + " °C");
        System.out.println("Consumo de energia: " + consumoEnergia + " kWh");
        System.out.println("Umidade: " + umidade + "%");
        System.out.println("Status: " + verificarStatus());
    }
}