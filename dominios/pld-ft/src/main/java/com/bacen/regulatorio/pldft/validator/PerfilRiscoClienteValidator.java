package com.bacen.regulatorio.pldft.validator;

import com.bacen.regulatorio.commons.validator.CpfCnpjValidator;
import com.bacen.regulatorio.pldft.enums.NivelRiscoCliente;
import com.bacen.regulatorio.pldft.valueobject.PerfilRiscoCliente;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Circular BCB 3.978/2020 — validacao estrutural e de vigencia do perfil de risco.
 */
public final class PerfilRiscoClienteValidator {

    private PerfilRiscoClienteValidator() {}

    /**
     * Valida a estrutura minima do perfil de risco do cliente.
     */
    public static Optional<String> validarPerfil(PerfilRiscoCliente perfil) {
        if (perfil == null) {
            return Optional.of("Perfil de risco nao pode ser nulo");
        }
        if (!CpfCnpjValidator.isValid(perfil.cpfCnpj())) {
            return Optional.of("CPF/CNPJ do cliente invalido");
        }
        if (perfil.nivelRisco() == null) {
            return Optional.of("Nivel de risco e obrigatorio");
        }
        if (perfil.dataUltimaRevisao() == null) {
            return Optional.of("Data da ultima revisao e obrigatoria");
        }
        if (perfil.paisesRelacionados() == null) {
            return Optional.of("Lista de paises relacionados nao pode ser nula");
        }
        if (perfil.paisesRelacionados().stream().anyMatch(Objects::isNull)) {
            return Optional.of("Lista de paises relacionados nao pode conter valores nulos");
        }
        if (perfil.paisesRelacionados().stream().anyMatch(String::isBlank)) {
            return Optional.of("Lista de paises relacionados nao pode conter valores vazios");
        }
        if (perfil.nivelRisco() == NivelRiscoCliente.REFORCADO
                && (perfil.motivosRiscoElevado() == null || perfil.motivosRiscoElevado().isEmpty())) {
            return Optional.of("Perfil REFORCADO deve registrar motivos de risco elevado");
        }
        if (perfil.motivosRiscoElevado() != null && perfil.motivosRiscoElevado().stream().anyMatch(Objects::isNull)) {
            return Optional.of("Lista de motivos de risco elevado nao pode conter valores nulos");
        }
        return Optional.empty();
    }

    /**
     * Valida se o perfil esta dentro do prazo de revisao.
     */
    public static Optional<String> validarPerfilVigente(PerfilRiscoCliente perfil) {
        Optional<String> erro = validarPerfil(perfil);
        if (erro.isPresent()) {
            return erro;
        }
        return perfil.precisaRevisao()
                ? Optional.of("Perfil de risco do cliente precisa de revisao")
                : Optional.empty();
    }
}
