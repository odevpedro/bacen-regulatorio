package com.bacen.regulatorio.pldft.enums;

/**
 * Circular BCB 3.978/2020 — Art. 19.
 * Classificação de risco do cliente para fins de PLD/FT.
 * Determina a intensidade das diligências de conhecimento do cliente (KYC).
 */
public enum NivelRiscoCliente {
    /**
     * Baixo risco — diligência simplificada.
     * Ex: beneficiários de programas sociais, micro-empreendedores informais.
     */
    SIMPLIFICADO,
    /**
     * Risco médio — diligência padrão (KYC completo).
     * Aplicável à maioria dos clientes pessoa física e jurídica.
     */
    NORMAL,
    /**
     * Alto risco — diligência reforçada (Enhanced Due Diligence).
     * Ex: PEP (Pessoa Exposta Politicamente), clientes de países de alto risco,
     * operações com jurisdições não cooperantes com o GAFI.
     */
    REFORCADO
}
