package com.bacen.regulatorio.pldft.enums;

/**
 * Circular BCB 3.978/2020 — Art. 36 a 44.
 * Status do ciclo de comunicação de operação suspeita ao COAF.
 */
public enum StatusComunicacaoCoaf {
    /** Operação identificada como suspeita, aguardando análise interna. */
    PENDENTE_ANALISE,
    /** Análise concluída — operação considerada suspeita. */
    CONFIRMADA_SUSPEITA,
    /** Análise concluída — operação considerada regular. */
    DESCARTADA,
    /** Comunicação enviada ao COAF (prazo: até o dia útil seguinte à confirmação). */
    COMUNICADA,
    /** Comunicação recebida e protocolo gerado pelo COAF. */
    PROTOCOLO_GERADO
}
