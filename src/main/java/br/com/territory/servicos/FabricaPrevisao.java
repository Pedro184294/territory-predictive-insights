package br.com.territory.servicos;

import java.util.Locale;

public final class FabricaPrevisao {

    private FabricaPrevisao() {
        // Impede a criação de objetos desta classe.
    }

    public static EstrategiaPrevisao criar(
            String estrategia,
            String tipoSensor) {

        if (estrategia == null || estrategia.isBlank()) {
            throw new IllegalArgumentException(
                    "A estratégia de previsão é obrigatória.");
        }

        return switch (estrategia.trim().toUpperCase(Locale.ROOT)) {

            case "MEDIA_MOVEL" ->
                new PrevisaoMediaMovel(tipoSensor);

            case "COMPARACAO" ->
                new PrevisaoComparacao(tipoSensor);

            default -> throw new IllegalArgumentException(
                    "Estratégia de previsão desconhecida: " + estrategia);
        };
    }
}