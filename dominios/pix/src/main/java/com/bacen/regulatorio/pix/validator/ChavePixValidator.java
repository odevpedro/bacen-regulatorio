package com.bacen.regulatorio.pix.validator;

import com.bacen.regulatorio.commons.validator.CpfCnpjValidator;
import com.bacen.regulatorio.pix.enums.TipoChavePix;

import java.util.regex.Pattern;

/**
 * Resolução BCB 1/2020 — Art. 7° a 12.
 * Regras de formato para cada tipo de chave Pix registrada no DICT.
 *
 * Uso:
 *   ChavePixValidator.isValid("user@email.com", TipoChavePix.EMAIL)  // true
 *   ChavePixValidator.isValid("+5511987654321", TipoChavePix.TELEFONE) // true
 */
public final class ChavePixValidator {

    private static final Pattern EMAIL = Pattern.compile(
            "^[a-zA-Z0-9._%+\\-]+@[a-zA-Z0-9.\\-]+\\.[a-zA-Z]{2,}$");

    private static final Pattern TELEFONE = Pattern.compile(
            "^\\+55[1-9][0-9]9?[0-9]{8}$");

    private static final Pattern EVP = Pattern.compile(
            "^[0-9a-f]{8}-[0-9a-f]{4}-4[0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$",
            Pattern.CASE_INSENSITIVE);

    private ChavePixValidator() {}

    public static boolean isValid(String valor, TipoChavePix tipo) {
        if (valor == null || tipo == null) return false;
        return switch (tipo) {
            case CPF -> CpfCnpjValidator.isValidCpf(valor.replaceAll("[^0-9]", ""));
            case CNPJ -> CpfCnpjValidator.isValidCnpj(valor.replaceAll("[^0-9]", ""));
            case EMAIL -> valor.length() <= 77 && EMAIL.matcher(valor).matches();
            case TELEFONE -> TELEFONE.matcher(valor).matches();
            case EVP -> EVP.matcher(valor).matches();
        };
    }

    /** Detecta o tipo da chave automaticamente pelo formato. */
    public static TipoChavePix detectarTipo(String valor) {
        if (valor == null) return null;
        String digits = valor.replaceAll("[^0-9]", "");
        if (digits.length() == 11 && !valor.startsWith("+") && CpfCnpjValidator.isValidCpf(digits)) return TipoChavePix.CPF;
        if (digits.length() == 14 && CpfCnpjValidator.isValidCnpj(digits)) return TipoChavePix.CNPJ;
        if (valor.startsWith("+55") && TELEFONE.matcher(valor).matches()) return TipoChavePix.TELEFONE;
        if (EVP.matcher(valor).matches()) return TipoChavePix.EVP;
        if (EMAIL.matcher(valor).matches()) return TipoChavePix.EMAIL;
        return null;
    }

}
