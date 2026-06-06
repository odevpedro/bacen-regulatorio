package com.bacen.regulatorio.pix.valueobject;

import com.bacen.regulatorio.pix.validator.TxidValidator;

/**
 * Resolução BCB 1/2020 — txid (identificador da cobrança PIX).
 * Código alfanumérico de 26 a 35 caracteres.
 *
 * @param valor o identificador da cobrança
 */
public record Txid(String valor) {

    public Txid {
        var erro = TxidValidator.validar(valor);
        if (erro.isPresent()) {
            throw new IllegalArgumentException(erro.get());
        }
    }
}
