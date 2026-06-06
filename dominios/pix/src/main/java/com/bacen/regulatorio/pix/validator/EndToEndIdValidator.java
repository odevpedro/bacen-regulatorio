package com.bacen.regulatorio.pix.validator;

import java.util.Optional;
import java.util.regex.Pattern;

/**
 * Resolução BCB 1/2020 — formato do End-to-End ID do SPI.
 *
 * E{ISPB}{YYYYMMDD}{HHMMSS}{sequencial}
 *   - E: prefixo fixo (1 char)
 *   - ISPB: 8 dígitos do participante no SPI
 *   - YYYYMMDD: 8 dígitos da data de liquidação
 *   - HHMMSS: 6 dígitos do horário da transação
 *   - sequencial: alfanumérico (1 a 12 chars)
 * Total: 24 a 35 caracteres.
 */
public final class EndToEndIdValidator {

    private static final Pattern E2E_PATTERN = Pattern.compile(
            "^E[0-9]{8}[0-9]{8}[0-9]{6}[A-Za-z0-9]{1,12}$");

    private EndToEndIdValidator() {}

    public static Optional<String> validar(String endToEndId) {
        if (endToEndId == null || endToEndId.isEmpty()) {
            return Optional.of("End-to-End ID não pode ser nulo ou vazio");
        }
        if (endToEndId.length() < 24 || endToEndId.length() > 35) {
            return Optional.of("End-to-End ID deve ter entre 24 e 35 caracteres");
        }
        if (!E2E_PATTERN.matcher(endToEndId).matches()) {
            return Optional.of("End-to-End ID não respeita o formato E{ISPB}{data}{hora}{sequencial}");
        }
        return Optional.empty();
    }
}
