package com.bacen.regulatorio.drex.valueobject;

import com.bacen.regulatorio.drex.enums.TipoAtivoDigital;
import com.bacen.regulatorio.drex.enums.StatusLiquidacaoDrex;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Transacao no ecossistema DREX.
 *
 * @param idTransacao    identificador unico da transacao
 * @param tipoAtivo      tipo de ativo digital
 * @param valor          valor da transacao em DREX
 * @param cpfCnpjOrigem  CPF/CNPJ do remetente
 * @param cpfCnpjDestino CPF/CNPJ do destinatario
 * @param dataHora       data e hora da transacao
 * @param status         status da liquidacao
 */
public record TransacaoDrex(
        String idTransacao,
        TipoAtivoDigital tipoAtivo,
        BigDecimal valor,
        String cpfCnpjOrigem,
        String cpfCnpjDestino,
        LocalDateTime dataHora,
        StatusLiquidacaoDrex status
) {
    public TransacaoDrex {
        if (idTransacao == null || idTransacao.isBlank()) {
            throw new IllegalArgumentException("ID da transacao e obrigatorio");
        }
        if (valor == null || valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valor deve ser positivo");
        }
    }
}
