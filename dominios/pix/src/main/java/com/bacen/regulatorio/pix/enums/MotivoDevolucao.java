package com.bacen.regulatorio.pix.enums;

/**
 * Resolução BCB 1/2020 — motivos de devolução (campo MD01 do pacs.004).
 */
public enum MotivoDevolucao {
    /** Fraude confirmada — iniciada pela instituição recebedora. */
    MD06_FRAUDE,
    /** Solicitação do usuário pagador. */
    MD08_SOLICITACAO_PAGADOR,
    /** Erro operacional da instituição. */
    AM09_ERRO_OPERACIONAL,
    /** Conta encerrada. */
    AC03_CONTA_ENCERRADA,
    /** Beneficiário incorreto. */
    BE01_BENEFICIARIO_INCORRETO
}
