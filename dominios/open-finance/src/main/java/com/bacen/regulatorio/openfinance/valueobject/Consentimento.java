package com.bacen.regulatorio.openfinance.valueobject;

import com.bacen.regulatorio.openfinance.enums.PermissaoConsentimento;
import com.bacen.regulatorio.openfinance.enums.StatusConsentimento;

import java.time.OffsetDateTime;
import java.util.List;

/**
 * Resolução BCB 32/2020 — consentimento do usuário para compartilhamento de dados.
 *
 * O consentId é o identificador único gerado pela instituição transmissora.
 * O usuário pode revogar o consentimento a qualquer momento.
 *
 * @param consentId        identificador único (UUID)
 * @param cpfCnpjUsuario   CPF/CNPJ do usuário titular dos dados
 * @param status           status atual do ciclo de vida
 * @param permissoes       lista de permissões concedidas
 * @param dataCriacao      quando foi criado
 * @param dataExpiracao    quando expira (máx. 12 meses)
 */
public record Consentimento(
        String consentId,
        String cpfCnpjUsuario,
        StatusConsentimento status,
        List<PermissaoConsentimento> permissoes,
        OffsetDateTime dataCriacao,
        OffsetDateTime dataExpiracao
) {
    public boolean isAtivo() {
        return status == StatusConsentimento.AUTHORISED
                && OffsetDateTime.now().isBefore(dataExpiracao);
    }

    public boolean hasPermissao(PermissaoConsentimento permissao) {
        return permissoes != null && permissoes.contains(permissao);
    }
}
