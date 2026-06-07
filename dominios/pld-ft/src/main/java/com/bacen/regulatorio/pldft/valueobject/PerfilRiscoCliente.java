package com.bacen.regulatorio.pldft.valueobject;

import com.bacen.regulatorio.pldft.enums.NivelRiscoCliente;

import java.time.LocalDate;
import java.util.List;

/**
 * Circular BCB 3.978/2020 — Art. 15 a 22.
 * Perfil de risco PLD/FT do cliente, resultado do processo de KYC.
 *
 * @param cpfCnpj              identificador do cliente
 * @param nivelRisco           classificação de risco
 * @param isPep                Pessoa Exposta Politicamente (Res. BCB 277/2022)
 * @param paisesRelacionados   países com os quais o cliente opera
 * @param dataUltimaRevisao    data da última revisão de perfil (max 1 ano para NORMAL, 6 meses para REFORCADO)
 * @param motivosRiscoElevado  razões que justificam o nível REFORCADO, se aplicável
 */
public record PerfilRiscoCliente(
        String cpfCnpj,
        NivelRiscoCliente nivelRisco,
        boolean isPep,
        List<String> paisesRelacionados,
        LocalDate dataUltimaRevisao,
        List<String> motivosRiscoElevado
) {
    /**
     * Verifica se o perfil está vencido e precisa ser revisado.
     * Prazo máximo de revisão:
     *   SIMPLIFICADO: 2 anos
     *   NORMAL: 1 ano
     *   REFORCADO: 6 meses
     */
    public boolean precisaRevisao() {
        if (nivelRisco == null || dataUltimaRevisao == null) {
            return true;
        }
        LocalDate limite = switch (nivelRisco) {
            case SIMPLIFICADO -> dataUltimaRevisao.plusYears(2);
            case NORMAL       -> dataUltimaRevisao.plusYears(1);
            case REFORCADO    -> dataUltimaRevisao.plusMonths(6);
        };
        return LocalDate.now().isAfter(limite);
    }

    /** PEPs sempre exigem diligência reforçada. */
    public NivelRiscoCliente nivelEfetivo() {
        if (isPep || nivelRisco == null) {
            return NivelRiscoCliente.REFORCADO;
        }
        return nivelRisco;
    }
}
