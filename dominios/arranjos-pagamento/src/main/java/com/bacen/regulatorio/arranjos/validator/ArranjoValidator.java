package com.bacen.regulatorio.arranjos.validator;

import com.bacen.regulatorio.arranjos.enums.CodigoArranjo;

import java.util.Optional;

/**
 * Resolução CMN 4.282/2013 + Resolução BCB 150/2021.
 * Regras de validação para uso de arranjos de pagamento.
 */
public final class ArranjoValidator {

    private ArranjoValidator() {}

    /**
     * Valida se o código de arranjo existe no registro do BACEN.
     */
    public static Optional<String> validarCodigo(String codigo) {
        try {
            CodigoArranjo.valueOf(codigo.toUpperCase());
            return Optional.empty();
        } catch (IllegalArgumentException | NullPointerException e) {
            return Optional.of("Código de arranjo desconhecido: " + codigo);
        }
    }

    /**
     * Resolução BCB 150/2021 — Pix não pode ser usado como arranjo de garantia
     * em operações de recebíveis de cartão.
     */
    public static Optional<String> validarArranjoParaRecebiveis(CodigoArranjo arranjo) {
        if (arranjo == CodigoArranjo.PIX || arranjo == CodigoArranjo.TED || arranjo == CodigoArranjo.DOC) {
            return Optional.of("Arranjo " + arranjo + " não é válido para negociação de recebíveis de cartão");
        }
        return Optional.empty();
    }

    /**
     * Verifica se o arranjo é elegível para o produto de crédito informado.
     * Crédito parcelado exige arranjo do tipo crédito, não débito ou pré-pago.
     */
    public static Optional<String> validarElegibilidadeCredito(CodigoArranjo arranjo) {
        if (arranjo.isDebito() || arranjo.isPrePago()) {
            return Optional.of("Arranjo de débito/pré-pago não suporta parcelamento de crédito");
        }
        return Optional.empty();
    }
}
