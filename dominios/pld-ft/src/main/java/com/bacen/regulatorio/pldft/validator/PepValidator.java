package com.bacen.regulatorio.pldft.validator;

import java.util.Optional;
import java.util.Set;

/**
 * Circular BCB 3.978/2020 Art. 22 + Res. BCB 277/2022 — validacao de Pessoa Exposta Politicamente (PEP).
 *
 * Simula identificacao de PEP. Em producao, deve integrar com a base oficial de PEPs:
 * - CGU (Portal da Transparencia)
 * - TSE (candidatos)
 * - BACEN (cadastro de administradores)
 */
public final class PepValidator {

    private static final Set<String> CARGOS_PEP = Set.of(
            "PRESIDENTE_REPUBLICA",
            "VICE_PRESIDENTE",
            "MINISTRO_ESTADO",
            "SECRETARIO_EXECUTIVO",
            "SENADOR",
            "DEPUTADO_FEDERAL",
            "DEPUTADO_ESTADUAL",
            "PREFEITO",
            "VEREADOR",
            "GOVERNADOR",
            "VICE_GOVERNADOR",
            "JUIZ_TRIBUNAL_SUPERIOR",
            "MINISTRO_STF",
            "PROCURADOR_REPUBLICA",
            "MILITAR_ALTO_COMANDO"
    );

    private PepValidator() {}

    /**
     * Verifica se um cargo e considerado PEP.
     */
    public static boolean isCargoPep(String cargo) {
        return cargo != null && CARGOS_PEP.contains(cargo.toUpperCase());
    }

    /**
     * Verifica se o cliente e PEP com base em cargo e periodo de exercicio.
     * PEP permanece sob monitoramento por 5 anos apos o termino do mandato (Res. BCB 277).
     */
    public static Optional<String> validarCondicaoPep(String cpfCnpj, String cargo, int anosDesdeTermino) {
        if (cargo != null && isCargoPep(cargo)) {
            if (anosDesdeTermino <= 5) {
                return Optional.of("Cliente " + cpfCnpj + " e PEP (cargo: " + cargo
                        + "). Exige diligencia reforcada por " + (5 - anosDesdeTermino) + " anos");
            }
        }
        return Optional.empty();
    }
}
