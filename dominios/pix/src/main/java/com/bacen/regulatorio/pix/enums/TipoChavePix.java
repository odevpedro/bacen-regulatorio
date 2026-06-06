package com.bacen.regulatorio.pix.enums;

/**
 * Resolução BCB 1/2020 — tipos de chave do DICT.
 * Cada tipo tem formato e regras de validação próprias.
 */
public enum TipoChavePix {
    /** CPF (11 dígitos com dígitos verificadores). */
    CPF,
    /** CNPJ (14 dígitos com dígitos verificadores). */
    CNPJ,
    /** Endereço de e-mail (máx. 77 caracteres). */
    EMAIL,
    /** Número de telefone celular no formato +55XXXXXXXXXXX. */
    TELEFONE,
    /** EVP (Endereço Virtual de Pagamento) — UUID gerado pelo DICT. */
    EVP
}
