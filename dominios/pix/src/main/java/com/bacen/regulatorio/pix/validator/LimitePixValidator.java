package com.bacen.regulatorio.pix.validator;

import java.math.BigDecimal;
import java.time.LocalTime;

/**
 * Resolução BCB 1/2020 + Resolução BCB 142/2021 — limites de valor no Pix.
 *
 * Limites padrão definidos pelo BACEN (o usuário pode reduzir via app):
 *   - Noturno (20h–06h): R$ 1.000,00 por transação
 *   - Diário (06h–20h): sem limite regulatório (limite operacional da instituição)
 *
 * Obs.: cada instituição pode configurar limites menores a pedido do cliente.
 */
public final class LimitePixValidator {

    public static final BigDecimal LIMITE_NOTURNO_PADRAO = new BigDecimal("1000.00");

    private static final LocalTime INICIO_PERIODO_NOTURNO = LocalTime.of(20, 0);
    private static final LocalTime FIM_PERIODO_NOTURNO    = LocalTime.of(6, 0);

    private LimitePixValidator() {}

    public static boolean isPeriodoNoturno(LocalTime horario) {
        return horario.isAfter(INICIO_PERIODO_NOTURNO) || horario.isBefore(FIM_PERIODO_NOTURNO);
    }

    /**
     * Valida se o valor respeita o limite noturno configurado pelo usuário.
     *
     * @param valor         valor da transação
     * @param horario       horário da transação
     * @param limiteNoturno limite personalizado do usuário (ou padrão)
     */
    public static boolean isValorPermitido(BigDecimal valor, LocalTime horario, BigDecimal limiteNoturno) {
        if (!isPeriodoNoturno(horario)) return true;
        BigDecimal limite = limiteNoturno != null ? limiteNoturno : LIMITE_NOTURNO_PADRAO;
        return valor.compareTo(limite) <= 0;
    }

    public static boolean isValorPermitido(BigDecimal valor, LocalTime horario) {
        return isValorPermitido(valor, horario, LIMITE_NOTURNO_PADRAO);
    }
}
