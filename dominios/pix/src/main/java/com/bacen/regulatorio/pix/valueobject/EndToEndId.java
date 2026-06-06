package com.bacen.regulatorio.pix.valueobject;

import com.bacen.regulatorio.pix.validator.EndToEndIdValidator;

/**
 * Resolução BCB 1/2020 — End-to-End ID do SPI.
 * Identificador único da transação no Sistema de Pagamentos Instantâneos.
 *
 * @param valor o identificador no formato E{ISPB}{YYYYMMDD}{HHMMSS}{sequencial}
 */
public record EndToEndId(String valor) {

    public EndToEndId {
        var erro = EndToEndIdValidator.validar(valor);
        if (erro.isPresent()) {
            throw new IllegalArgumentException(erro.get());
        }
    }
}
