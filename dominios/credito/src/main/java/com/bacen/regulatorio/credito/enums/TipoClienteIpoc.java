package com.bacen.regulatorio.credito.enums;

import java.util.Arrays;
import java.util.Optional;

/**
 * Circular BCB 3.953/2019 + Documento 3040 — tipo de cliente usado na composição do IPOC.
 */
public enum TipoClienteIpoc {
    PESSOA_FISICA_COM_CPF("1", 11),
    PESSOA_JURIDICA_COM_CNPJ("2", 8),
    PESSOA_FISICA_NO_EXTERIOR("3", 14),
    PESSOA_JURIDICA_NO_EXTERIOR("4", 14),
    PESSOA_FISICA_SEM_CPF("5", 14),
    PESSOA_JURIDICA_SEM_CNPJ("6", 14);

    private final String codigo;
    private final int tamanhoCodigoCliente;

    TipoClienteIpoc(String codigo, int tamanhoCodigoCliente) {
        this.codigo = codigo;
        this.tamanhoCodigoCliente = tamanhoCodigoCliente;
    }

    public String codigo() {
        return codigo;
    }

    public int tamanhoCodigoCliente() {
        return tamanhoCodigoCliente;
    }

    public static Optional<TipoClienteIpoc> fromCodigo(String codigo) {
        if (codigo == null) {
            return Optional.empty();
        }
        return Arrays.stream(values())
                .filter(tipo -> tipo.codigo.equals(codigo))
                .findFirst();
    }
}
