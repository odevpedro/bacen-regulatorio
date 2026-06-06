package com.bacen.regulatorio.pix.valueobject;

import com.bacen.regulatorio.pix.enums.TipoChavePix;
import com.bacen.regulatorio.pix.validator.ChavePixValidator;

/**
 * Resolução BCB 1/2020 — chave Pix registrada no DICT.
 *
 * Uma chave Pix é um alias para uma conta transacional.
 * O DICT garante unicidade: cada chave pertence a apenas uma conta.
 *
 * @param tipo  tipo da chave (CPF, CNPJ, EMAIL, TELEFONE, EVP)
 * @param valor valor da chave no formato correto para o tipo
 */
public record ChavePix(TipoChavePix tipo, String valor) {

    public ChavePix {
        if (!ChavePixValidator.isValid(valor, tipo)) {
            throw new IllegalArgumentException(
                    "Chave Pix inválida para o tipo " + tipo + ": " + valor);
        }
    }

    /** Cria uma ChavePix detectando automaticamente o tipo. */
    public static ChavePix of(String valor) {
        TipoChavePix tipo = ChavePixValidator.detectarTipo(valor);
        if (tipo == null) throw new IllegalArgumentException("Formato de chave Pix não reconhecido: " + valor);
        return new ChavePix(tipo, valor);
    }
}
