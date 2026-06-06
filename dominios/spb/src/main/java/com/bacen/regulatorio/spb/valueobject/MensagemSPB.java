package com.bacen.regulatorio.spb.valueobject;

import com.bacen.regulatorio.spb.enums.SistemaLiquidacao;
import com.bacen.regulatorio.spb.enums.StatusLiquidacao;
import com.bacen.regulatorio.spb.enums.TipoMensagemSPB;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Lei 10.214/2001 — mensagem do SPB.
 *
 * @param idMensagem    identificador unico da mensagem
 * @param tipo          tipo de mensagem ISO 20022
 * @param sistema       sistema de liquidacao alvo
 * @param valor         valor da transacao
 * @param ispbOrigem    ISPB do participante remetente
 * @param ispbDestino   ISPB do participante destinatario
 * @param dataHora      data e hora da mensagem
 * @param status        status da liquidacao
 */
public record MensagemSPB(
        String idMensagem,
        TipoMensagemSPB tipo,
        SistemaLiquidacao sistema,
        BigDecimal valor,
        String ispbOrigem,
        String ispbDestino,
        LocalDateTime dataHora,
        StatusLiquidacao status
) {
    public MensagemSPB {
        if (idMensagem == null || idMensagem.isBlank()) {
            throw new IllegalArgumentException("ID da mensagem e obrigatorio");
        }
        if (valor != null && valor.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Valor nao pode ser negativo");
        }
    }
}
