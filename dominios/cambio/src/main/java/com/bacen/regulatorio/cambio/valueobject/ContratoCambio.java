package com.bacen.regulatorio.cambio.valueobject;

import com.bacen.regulatorio.cambio.enums.FinalidadeCambio;
import com.bacen.regulatorio.cambio.enums.Moeda;
import com.bacen.regulatorio.cambio.enums.TipoContratoCambio;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Res. BCB 277/2022 + Circ. 3.691/2013 — contrato de cambio.
 *
 * @param numeroContrato   numero do contrato no e-CAC
 * @param tipoContrato     tipo de contrato de cambio
 * @param finalidade       finalidade da operacao
 * @param moedaOrigem      moeda de origem
 * @param moedaDestino     moeda de destino
 * @param valorOperacao    valor da operacao na moeda de origem
 * @param taxaCambio       taxa de cambio aplicada
 * @param dataContratacao  data de contratacao
 * @param dataLiquidacao   data prevista para liquidacao
 * @param cpfCnpjCliente   CPF/CNPJ do cliente
 */
public record ContratoCambio(
        String numeroContrato,
        TipoContratoCambio tipoContrato,
        FinalidadeCambio finalidade,
        Moeda moedaOrigem,
        Moeda moedaDestino,
        BigDecimal valorOperacao,
        TaxaCambio taxaCambio,
        LocalDate dataContratacao,
        LocalDate dataLiquidacao,
        String cpfCnpjCliente
) {
    public ContratoCambio {
        if (numeroContrato == null || numeroContrato.isBlank()) {
            throw new IllegalArgumentException("Numero do contrato e obrigatorio");
        }
        if (valorOperacao != null && valorOperacao.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valor da operacao deve ser positivo");
        }
    }

    public BigDecimal valorLiquidacao() {
        return taxaCambio.converter(valorOperacao);
    }
}
