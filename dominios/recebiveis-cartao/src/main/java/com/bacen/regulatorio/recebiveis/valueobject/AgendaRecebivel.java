package com.bacen.regulatorio.recebiveis.valueobject;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Res. 4.734/2019 — agenda de liquidacao de uma unidade de recebivel.
 * Define as datas de liquidacao e os valores projetados para cada data.
 *
 * @param unidadeId       identificador da unidade de recebivel
 * @param entradas        lista de datas com valores projetados de entrada
 * @param saldoInicial    saldo disponivel no inicio da agenda
 */
public record AgendaRecebivel(
        UnidadeRecebivelId unidadeId,
        List<EntradaAgenda> entradas,
        BigDecimal saldoInicial
) {
    public AgendaRecebivel {
        if (entradas == null || entradas.isEmpty()) {
            throw new IllegalArgumentException("Agenda deve ter pelo menos uma entrada");
        }
    }

    /**
     * Saldo projetado para uma data especifica.
     * Considera todas as entradas com data anterior ou igual.
     */
    public BigDecimal saldoProjetadoAte(LocalDate data) {
        BigDecimal saldo = saldoInicial;
        for (EntradaAgenda e : entradas) {
            if (!e.data().isAfter(data)) {
                saldo = saldo.add(e.valor());
            }
        }
        return saldo;
    }

    /**
     * Saldo projetado total (considerando todas as entradas).
     */
    public BigDecimal saldoProjetadoTotal() {
        BigDecimal totalEntradas = entradas.stream()
                .map(EntradaAgenda::valor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return saldoInicial.add(totalEntradas);
    }

    /**
     * Entrada individual na agenda.
     *
     * @param data  data de liquidacao prevista
     * @param valor valor a ser liquidado na data
     */
    public record EntradaAgenda(LocalDate data, BigDecimal valor) {
        public EntradaAgenda {
            if (valor == null || valor.compareTo(BigDecimal.ZERO) < 0) {
                throw new IllegalArgumentException("Valor da entrada nao pode ser negativo");
            }
        }
    }
}
