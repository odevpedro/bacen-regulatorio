package com.bacen.regulatorio.recebiveis.validator;

import com.bacen.regulatorio.recebiveis.valueobject.AgendaRecebivel;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

/**
 * Res. 4.734/2019 — validacao da agenda de liquidacao de recebiveis.
 */
public final class AgendaRecebivelValidator {

    private AgendaRecebivelValidator() {}

    /**
     * Valida se o saldo projetado cobre o valor do gravame desejado.
     */
    public static Optional<String> validarCoberturaGravame(
            AgendaRecebivel agenda, BigDecimal valorGravame, LocalDate dataOperacao) {
        BigDecimal saldoProjetado = agenda.saldoProjetadoAte(dataOperacao);
        if (saldoProjetado.compareTo(valorGravame) < 0) {
            return Optional.of("Saldo projetado de " + saldoProjetado
                    + " insuficiente para cobrir gravame de " + valorGravame + " ate " + dataOperacao);
        }
        return Optional.empty();
    }

    /**
     * Valida que as datas da agenda estao em ordem cronologica.
     */
    public static Optional<String> validarOrdemCronologica(AgendaRecebivel agenda) {
        if (agenda.entradas().size() <= 1) return Optional.empty();
        for (int i = 1; i < agenda.entradas().size(); i++) {
            if (agenda.entradas().get(i).data().isBefore(agenda.entradas().get(i - 1).data())) {
                return Optional.of("Datas da agenda fora de ordem cronologica");
            }
        }
        return Optional.empty();
    }

    /**
     * Valida se a data da operacao e anterior a liquidacao maxima prevista.
     */
    public static Optional<String> validarPrazoMaximoLiquidacao(
            AgendaRecebivel agenda, LocalDate dataOperacao) {
        LocalDate ultimaData = agenda.entradas().stream()
                .map(AgendaRecebivel.EntradaAgenda::data)
                .max(LocalDate::compareTo)
                .orElse(null);
        if (ultimaData == null) return Optional.empty();
        if (dataOperacao.isAfter(ultimaData)) {
            return Optional.of("Data da operacao " + dataOperacao
                    + " posterior a ultima liquidacao prevista " + ultimaData);
        }
        return Optional.empty();
    }
}
