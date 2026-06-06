package com.bacen.regulatorio.commons.validator;

public final class CpfCnpjValidator {

    private CpfCnpjValidator() {}

    public static boolean isValid(String value) {
        if (value == null) return false;
        String digits = value.replaceAll("[^0-9]", "");
        return switch (digits.length()) {
            case 11 -> isValidCpf(digits);
            case 14 -> isValidCnpj(digits);
            default -> false;
        };
    }

    public static String tipo(String value) {
        if (value == null) return null;
        String digits = value.replaceAll("[^0-9]", "");
        if (digits.length() == 11 && isValidCpf(digits)) return "CPF";
        if (digits.length() == 14 && isValidCnpj(digits)) return "CNPJ";
        return null;
    }

    public static boolean isValidCpf(String cpf) {
        if (cpf.length() != 11 || cpf.chars().distinct().count() == 1) return false;

        int sum = 0;
        for (int i = 0; i < 9; i++) sum += (cpf.charAt(i) - '0') * (10 - i);
        int r = (sum * 10) % 11;
        if (r == 10 || r == 11) r = 0;
        if (r != (cpf.charAt(9) - '0')) return false;

        sum = 0;
        for (int i = 0; i < 10; i++) sum += (cpf.charAt(i) - '0') * (11 - i);
        r = (sum * 10) % 11;
        if (r == 10 || r == 11) r = 0;
        return r == (cpf.charAt(10) - '0');
    }

    public static boolean isValidCnpj(String cnpj) {
        if (cnpj.length() != 14 || cnpj.chars().distinct().count() == 1) return false;

        int[] w1 = {5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
        int[] w2 = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};

        int sum = 0;
        for (int i = 0; i < 12; i++) sum += (cnpj.charAt(i) - '0') * w1[i];
        int r = sum % 11;
        int d1 = r < 2 ? 0 : 11 - r;
        if (d1 != (cnpj.charAt(12) - '0')) return false;

        sum = 0;
        for (int i = 0; i < 13; i++) sum += (cnpj.charAt(i) - '0') * w2[i];
        r = sum % 11;
        int d2 = r < 2 ? 0 : 11 - r;
        return d2 == (cnpj.charAt(13) - '0');
    }
}
