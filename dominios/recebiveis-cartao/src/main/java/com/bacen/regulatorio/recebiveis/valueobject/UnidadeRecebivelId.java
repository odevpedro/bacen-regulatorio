package com.bacen.regulatorio.recebiveis.valueobject;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.HexFormat;

/**
 * Resolução 4.734/2019 — identidade única de uma unidade de recebível.
 *
 * Uma unidade é identificada pela combinação de 4 campos (Art. 3°):
 *   arranjo de pagamento + credenciadora + usuário de serviço de pagamento + data de liquidação
 *
 * A chave é um SHA-256 desses campos para armazenamento compacto e indexado.
 */
public record UnidadeRecebivelId(
        String arranjosPagamento,
        String cpfCnpjCredenciadora,
        String cpfCnpjUsuarioServicoPagamento,
        LocalDate dataLiquidacao
) {
    public String chaveUnica() {
        String composite = String.join("|",
                arranjosPagamento,
                cpfCnpjCredenciadora,
                cpfCnpjUsuarioServicoPagamento,
                dataLiquidacao.toString()
        );
        try {
            byte[] hash = MessageDigest.getInstance("SHA-256")
                    .digest(composite.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 indisponível", e);
        }
    }

    @Override
    public String toString() {
        return "UnidadeRecebivelId{" + arranjosPagamento + "/" + cpfCnpjCredenciadora
                + "/" + cpfCnpjUsuarioServicoPagamento + "/" + dataLiquidacao + "}";
    }
}
