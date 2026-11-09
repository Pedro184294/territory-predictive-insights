
package br.com.territory.dominio;

import br.com.territory.exceptions.FonteIndisponivelException;

public interface Sensor {

    double realizarLeitura() throws FonteIndisponivelException;

    boolean verificarFuncionamento();

    String getTipo();

    String getUnidadeMedida();
}
