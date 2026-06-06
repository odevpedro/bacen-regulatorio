package com.bacen.regulatorio.arranjos.enums;

/**
 * Resolução CMN 4.282/2013 e Resolução BCB 150/2021 — códigos de arranjo
 * de pagamento registrados no Banco Central do Brasil.
 *
 * Os códigos são usados como chave de identificação em mensagens do SPB,
 * na agenda de recebíveis (Res. 4.734/2019) e no DICT.
 */
public enum CodigoArranjo {

    // Visa
    VCC("Visa Crédito à Vista"),
    VCF("Visa Crédito Parcelado Lojista"),
    VCD("Visa Débito"),
    VPS("Visa Pré-Pago"),

    // Mastercard
    MCC("Mastercard Crédito à Vista"),
    MCF("Mastercard Crédito Parcelado Lojista"),
    MCD("Mastercard Débito"),
    MPS("Mastercard Pré-Pago"),

    // Elo
    ECC("Elo Crédito à Vista"),
    ECF("Elo Crédito Parcelado Lojista"),
    ECD("Elo Débito"),
    EPS("Elo Pré-Pago"),

    // Hipercard
    HCC("Hipercard Crédito à Vista"),
    HCD("Hipercard Débito"),

    // American Express
    ACC("Amex Crédito à Vista"),
    ACF("Amex Crédito Parcelado"),

    // PIX
    PIX("Arranjo de Pagamento Instantâneo"),

    // Boleto
    BOL("Boleto Bancário"),

    // TED/DOC (legado)
    TED("Transferência Eletrônica Disponível"),
    DOC("Documento de Ordem de Crédito");

    private final String descricao;

    CodigoArranjo(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    public boolean isCredito() {
        String name = this.name();
        return name.contains("CC") || name.contains("CF") || name.contains("C_");
    }

    public boolean isDebito() {
        return this.name().contains("CD");
    }

    public boolean isPrePago() {
        return this.name().contains("PS");
    }
}
