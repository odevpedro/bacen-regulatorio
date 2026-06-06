package com.bacen.regulatorio.recebiveis.enums;

/** Motivos de recusa definidos pela registradora conforme Circular 3.952/2019. */
public enum MotivoRecusa {
    /** Já existe gravame com a mesma prioridade para a mesma unidade de recebível. */
    PRIORIDADE_DUPLICADA,
    /** Valor do gravame supera o saldo disponível da unidade. */
    SALDO_INSUFICIENTE,
    /** Número de controle não encontrado na registradora. */
    NEGOCIACAO_NAO_ENCONTRADA,
    /** A negociação já foi cancelada e não pode ser alterada. */
    NEGOCIACAO_JA_CANCELADA,
    /** CPF ou CNPJ com formato ou dígitos verificadores inválidos. */
    CPF_CNPJ_INVALIDO,
    /** Indicador de interoperabilidade fora dos valores permitidos (SI/CI/NI). */
    INTEROPERABILIDADE_INVALIDA,
    /** Conflito entre operações simultâneas sobre a mesma unidade. */
    CONFLITO_DE_NEGOCIACAO,
    ERRO_INTERNO
}
