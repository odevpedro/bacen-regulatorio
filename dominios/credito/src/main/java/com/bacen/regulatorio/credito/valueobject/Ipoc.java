package com.bacen.regulatorio.credito.valueobject;

import com.bacen.regulatorio.credito.enums.TipoClienteIpoc;
import com.bacen.regulatorio.credito.validator.IpocValidator;

/**
 * Circular BCB 3.953/2019 + Documento 3040 — Identificação Padronizada da Operação de Crédito.
 *
 * @param valor valor completo do IPOC
 */
public record Ipoc(String valor) {

    public Ipoc {
        var erro = IpocValidator.validar(valor);
        if (erro.isPresent()) {
            throw new IllegalArgumentException(erro.get());
        }
    }

    public static Ipoc of(
            String cnpjInstituicao,
            String modalidade,
            TipoClienteIpoc tipoCliente,
            String codigoCliente,
            String codigoContrato) {
        return new Ipoc(IpocValidator.montar(
                cnpjInstituicao,
                modalidade,
                tipoCliente,
                codigoCliente,
                codigoContrato));
    }
}
