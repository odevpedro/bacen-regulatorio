package com.bacen.regulatorio.spb.enums;

/**
 * Lei 10.214/2001 — tipos de mensagem ISO 20022 no SPB.
 */
public enum TipoMensagemSPB {
    PACS_008("Transferencia de credito", "FIToFICustomerCreditTransfer"),
    PACS_004("Devolucao", "PaymentReturn"),
    PACS_002("Status", "FIToFIPaymentStatusReport"),
    CAMT_053("Extrato", "BankToCustomerStatement"),
    CAMT_054("Notificacao de credito/debito", "BankToCustomerDebitCreditNotification"),
    PAIN_001("Ordem de pagamento", "CustomerCreditTransferInitiation"),
    PAIN_002("Status da ordem", "CustomerPaymentStatusReport");

    private final String descricao;
    private final String iso20022Name;

    TipoMensagemSPB(String descricao, String iso20022Name) {
        this.descricao = descricao;
        this.iso20022Name = iso20022Name;
    }

    public String getDescricao() { return descricao; }
    public String getIso20022Name() { return iso20022Name; }
}
