package com.bacen.regulatorio.pix.validator;

import java.util.Optional;
import java.util.regex.Pattern;

/**
 * Resolução BCB 1/2020 — formato do txid (identificador da cobrança).
 *
 * O txid é um código alfanumérico de 26 a 35 caracteres utilizado
 * como identificador único de uma cobrança PIX no DICT.
 */
public final class TxidValidator {

    private static final Pattern TXID_PATTERN = Pattern.compile(
            "^[A-Za-z0-9]{26,35}$");

    private TxidValidator() {}

    public static Optional<String> validar(String txid) {
        if (txid == null || txid.isEmpty()) {
            return Optional.of("txid não pode ser nulo ou vazio");
        }
        if (!TXID_PATTERN.matcher(txid).matches()) {
            return Optional.of("txid deve ter entre 26 e 35 caracteres alfanuméricos");
        }
        return Optional.empty();
    }
}
