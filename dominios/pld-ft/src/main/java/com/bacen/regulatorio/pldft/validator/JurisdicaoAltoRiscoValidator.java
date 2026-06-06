package com.bacen.regulatorio.pldft.validator;

import java.util.Optional;
import java.util.Set;

/**
 * Circular BCB 3.978/2020 Anexo I — deteccao de jurisdicoes de alto risco conforme lista GAFI.
 *
 * Lista GAFI (atualizada periodicamente):
 *   - Jurisdicoes sob monitoramento intensificado ("lista cinza")
 *   - Jurisdicoes de alto risco ("lista preta")
 *   - Pais nao cooperantes
 */
public final class JurisdicaoAltoRiscoValidator {

    private static final Set<String> JURISDICOES_ALTO_RISCO = Set.of(
            "COREEIA_DO_NORTE",
            "IRA",
            "MYANMAR",
            "AFEGANISTAO",
            "IEMEN",
            "SIRIA"
    );

    private static final Set<String> JURISDICOES_MONITORADAS = Set.of(
            "PAQUISTAO",
            "NIGERIA",
            "AFRICA_DO_SUL",
            "INDONESIA",
            "TURQUIA",
            "FILIPINAS",
            "CAMAROES",
            "VIETNA",
            "CROACIA",
            "JAMAICA"
    );

    private JurisdicaoAltoRiscoValidator() {}

    /**
     * Verifica se o pais esta na lista de jurisdicoes de alto risco.
     */
    public static boolean isAltoRisco(String pais) {
        return pais != null && JURISDICOES_ALTO_RISCO.contains(pais.toUpperCase());
    }

    /**
     * Verifica se o pais esta na lista de jurisdicoes monitoradas.
     */
    public static boolean isMonitorada(String pais) {
        return pais != null && JURISDICOES_MONITORADAS.contains(pais.toUpperCase());
    }

    /**
     * Avalia o risco de uma operacao envolvendo determinada jurisdicao.
     */
    public static Optional<String> avaliarJurisdicao(String pais) {
        if (pais == null) return Optional.empty();
        String paisUpper = pais.toUpperCase();
        if (JURISDICOES_ALTO_RISCO.contains(paisUpper)) {
            return Optional.of("Jurisdicao de alto risco GAFI: " + pais
                    + ". Operacao exige comunicacao ao COAF");
        }
        if (JURISDICOES_MONITORADAS.contains(paisUpper)) {
            return Optional.of("Jurisdicao monitorada GAFI: " + pais
                    + ". Exige diligencia reforcada");
        }
        return Optional.empty();
    }
}
