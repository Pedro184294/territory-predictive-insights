package br.com.territory.dominio;

import br.com.territory.exceptions.FonteIndisponivelException;

public interface Sensor {

    double realizarLeitura() throws FonteIndisponivelException;

    String getTipo();

    String getUnidadeMedida();
}