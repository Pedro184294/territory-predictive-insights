
package br.com.territory.dominio;

import br.com.territory.exceptions.DadosInvalidosException;
import br.com.territory.validation.ValidadorDados;

public class RegiaoMonitorada extends ComponenteTerritorial {

    private double temperatura;
    private double consumoEnergia;
    private double umidade;

    private DadosTerritorio dadosTerritorio;

    public RegiaoMonitorada(
            String id,
            String nome,
            String regiao,
            double temperatura,
            double consumoEnergia,
            double umidade) throws DadosInvalidosException {

        this(id, nome, regiao, temperatura, consumoEnergia, umidade,
                new DadosTerritorio());
    }

    public RegiaoMonitorada(
            String id,
            String nome,
            String regiao,
            double temperatura,
            double consumoEnergia,
            double umidade,
            DadosTerritorio dadosTerritorio) throws DadosInvalidosException {

        super(id, nome, regiao);

        if (dadosTerritorio == null) {
            throw new DadosInvalidosException(
                    "Os dados de configuração do território são obrigatórios.");
        }

        this.dadosTerritorio = dadosTerritorio;

        atualizarMedicoes(temperatura, consumoEnergia, umidade);
    }

    @Override
    public String verificarStatus() {

        if (!isAtivo()) {
            return "INATIVO";
        }

        double alertaTemperatura = (dadosTerritorio.getLimiteAtencaoTemperatura()
                + dadosTerritorio.getLimiteCriticoTemperatura()) / 2.0;

        double alertaConsumo = (dadosTerritorio.getLimiteAtencaoConsumo()
                + dadosTerritorio.getLimiteCriticoConsumo()) / 2.0;

        if (temperatura >= dadosTerritorio.getLimiteCriticoTemperatura()
                || consumoEnergia >= dadosTerritorio.getLimiteCriticoConsumo()) {
            return "CRÍTICO";
        }

        if (temperatura >= alertaTemperatura
                || consumoEnergia >= alertaConsumo) {
            return "ALERTA";
        }

        if (temperatura >= dadosTerritorio.getLimiteAtencaoTemperatura()
                || consumoEnergia >= dadosTerritorio.getLimiteAtencaoConsumo()) {
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

    public DadosTerritorio getDadosTerritorio() {
        return dadosTerritorio;
    }

    public void atualizarMedicoes(
            double temperatura,
            double consumoEnergia,
            double umidade) throws DadosInvalidosException {

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
