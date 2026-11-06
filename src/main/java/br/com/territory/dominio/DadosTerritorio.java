
package br.com.territory.dominio;

import br.com.territory.exceptions.DadosInvalidosException;
import br.com.territory.validation.ValidadorDados;

public class DadosTerritorio {

    // Código necessário para alterar os limites do território.
    private static final String CODIGO_ACESSO = "TERRITORY2026";

    private double limiteAtencaoTemperatura;
    private double limiteCriticoTemperatura;

    private double limiteAtencaoConsumo;
    private double limiteCriticoConsumo;

    // Construtor com os limites iniciais do projeto.
    public DadosTerritorio() throws DadosInvalidosException {
        this(35.0, 40.0, 700.0, 900.0);
    }

    // Construtor que permite configurar os limites iniciais.
    public DadosTerritorio(
            double limiteAtencaoTemperatura,
            double limiteCriticoTemperatura,
            double limiteAtencaoConsumo,
            double limiteCriticoConsumo) throws DadosInvalidosException {

        configurarLimiaresInternamente(
                limiteAtencaoTemperatura,
                limiteCriticoTemperatura,
                limiteAtencaoConsumo,
                limiteCriticoConsumo);
    }

    // Consultas dos limites.
    public double getLimiteAtencaoTemperatura() {
        return limiteAtencaoTemperatura;
    }

    public double getLimiteCriticoTemperatura() {
        return limiteCriticoTemperatura;
    }

    public double getLimiteAtencaoConsumo() {
        return limiteAtencaoConsumo;
    }

    public double getLimiteCriticoConsumo() {
        return limiteCriticoConsumo;
    }

    // Verifica se o código de acesso está correto.
    private void validarCodigoAcesso(String codigoAcesso)
            throws DadosInvalidosException {

        if (codigoAcesso == null || !CODIGO_ACESSO.equals(codigoAcesso)) {
            throw new DadosInvalidosException(
                    "Código de acesso inválido. Alteração dos limites não autorizada.");
        }
    }

    // Altera o limite de atenção da temperatura.
    public void setLimiteAtencaoTemperatura(
            double limite, String codigoAcesso)
            throws DadosInvalidosException {

        validarCodigoAcesso(codigoAcesso);
        ValidadorDados.validarLimite(limite);

        if (limite >= limiteCriticoTemperatura) {
            throw new DadosInvalidosException(
                    "O limite de atenção da temperatura deve ser menor que o limite crítico.");
        }

        this.limiteAtencaoTemperatura = limite;
    }

    // Altera o limite crítico da temperatura.
    public void setLimiteCriticoTemperatura(
            double limite, String codigoAcesso)
            throws DadosInvalidosException {

        validarCodigoAcesso(codigoAcesso);
        ValidadorDados.validarLimite(limite);

        if (limite <= limiteAtencaoTemperatura) {
            throw new DadosInvalidosException(
                    "O limite crítico da temperatura deve ser maior que o limite de atenção.");
        }

        this.limiteCriticoTemperatura = limite;
    }

    // Altera o limite de atenção do consumo.
    public void setLimiteAtencaoConsumo(
            double limite, String codigoAcesso)
            throws DadosInvalidosException {

        validarCodigoAcesso(codigoAcesso);
        ValidadorDados.validarLimite(limite);

        if (limite >= limiteCriticoConsumo) {
            throw new DadosInvalidosException(
                    "O limite de atenção do consumo deve ser menor que o limite crítico.");
        }

        this.limiteAtencaoConsumo = limite;
    }

    // Altera o limite crítico do consumo.
    public void setLimiteCriticoConsumo(
            double limite, String codigoAcesso)
            throws DadosInvalidosException {

        validarCodigoAcesso(codigoAcesso);
        ValidadorDados.validarLimite(limite);

        if (limite <= limiteAtencaoConsumo) {
            throw new DadosInvalidosException(
                    "O limite crítico do consumo deve ser maior que o limite de atenção.");
        }

        this.limiteCriticoConsumo = limite;
    }

    // Configura todos os limites mediante autorização.
    public void configurarLimiares(
            double limiteAtencaoTemperatura,
            double limiteCriticoTemperatura,
            double limiteAtencaoConsumo,
            double limiteCriticoConsumo,
            String codigoAcesso) throws DadosInvalidosException {

        validarCodigoAcesso(codigoAcesso);

        configurarLimiaresInternamente(
                limiteAtencaoTemperatura,
                limiteCriticoTemperatura,
                limiteAtencaoConsumo,
                limiteCriticoConsumo);
    }

    // Realiza a validação e configuração interna dos limites.
    private void configurarLimiaresInternamente(
            double limiteAtencaoTemperatura,
            double limiteCriticoTemperatura,
            double limiteAtencaoConsumo,
            double limiteCriticoConsumo) throws DadosInvalidosException {

        ValidadorDados.validarLimite(limiteAtencaoTemperatura);
        ValidadorDados.validarLimite(limiteCriticoTemperatura);
        ValidadorDados.validarLimite(limiteAtencaoConsumo);
        ValidadorDados.validarLimite(limiteCriticoConsumo);

        if (limiteAtencaoTemperatura >= limiteCriticoTemperatura) {
            throw new DadosInvalidosException(
                    "O limite de atenção da temperatura deve ser menor que o crítico.");
        }

        if (limiteAtencaoConsumo >= limiteCriticoConsumo) {
            throw new DadosInvalidosException(
                    "O limite de atenção do consumo deve ser menor que o crítico.");
        }

        this.limiteAtencaoTemperatura = limiteAtencaoTemperatura;
        this.limiteCriticoTemperatura = limiteCriticoTemperatura;
        this.limiteAtencaoConsumo = limiteAtencaoConsumo;
        this.limiteCriticoConsumo = limiteCriticoConsumo;
    }
}
