package com.bacen.regulatorio.spb.validator;

import com.bacen.regulatorio.spb.enums.SistemaLiquidacao;
import com.bacen.regulatorio.spb.enums.TipoMensagemSPB;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Optional;

/**
 * Lei 10.214/2001 — regras de validacao do SPB.
 *
 * Horarios de corte padrao (cada sistema pode ter horarios especificos):
 *   - STR: 17:00 (horario de Brasilia)
 *   - LBTR: 17:30
 *   - SILOC: 17:00
 *   - Horario estendido ate 19:00 para transacoes selecionadas
 */
public final class SpbValidator {

    public static final LocalTime CORTE_STR = LocalTime.of(17, 0);
    public static final LocalTime CORTE_LBTR = LocalTime.of(17, 30);
    public static final LocalTime CORTE_SILOC = LocalTime.of(17, 0);
    public static final LocalTime CORTE_ESTENDIDO = LocalTime.of(19, 0);

    private SpbValidator() {}

    /**
     * Valida horario de corte para cada sistema de liquidacao.
     */
    public static Optional<String> validarHorarioCorte(SistemaLiquidacao sistema, LocalTime horario, DayOfWeek diaSemana) {
        if (diaSemana == DayOfWeek.SATURDAY || diaSemana == DayOfWeek.SUNDAY) {
            return Optional.of("SPB nao opera em finais de semana");
        }
        LocalTime corte = switch (sistema) {
            case LBTR -> CORTE_LBTR;
            case STR -> CORTE_STR;
            case SILOC -> CORTE_SILOC;
            default -> CORTE_ESTENDIDO;
        };
        if (horario.isAfter(corte)) {
            return Optional.of("Horario " + horario + " excede o corte das " + corte + " para " + sistema);
        }
        return Optional.empty();
    }

    /**
     * Valida se o tipo de mensagem e compativel com o sistema de liquidacao.
     */
    public static Optional<String> validarCompatibilidadeMensagem(TipoMensagemSPB tipo, SistemaLiquidacao sistema) {
        if (sistema == SistemaLiquidacao.LBTR && tipo == TipoMensagemSPB.PAIN_001) {
            return Optional.of("LBTR nao aceita PAIN_001, use PACS_008");
        }
        return Optional.empty();
    }

    /**
     * Valida o valor minimo para cada sistema de liquidacao.
     */
    public static Optional<String> validarValorMinimo(BigDecimal valor, SistemaLiquidacao sistema) {
        BigDecimal minimo = switch (sistema) {
            case LBTR -> new BigDecimal("100000.00");
            case SILOC -> BigDecimal.ONE;
            default -> BigDecimal.ZERO;
        };
        if (valor.compareTo(minimo) < 0) {
            return Optional.of("Valor minimo para " + sistema + " e " + minimo);
        }
        return Optional.empty();
    }
}
