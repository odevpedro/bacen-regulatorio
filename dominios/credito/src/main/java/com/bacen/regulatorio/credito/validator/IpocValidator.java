package com.bacen.regulatorio.credito.validator;

import com.bacen.regulatorio.credito.enums.TipoClienteIpoc;

import java.util.Optional;
import java.util.regex.Pattern;

/**
 * Circular BCB 3.953/2019 + Documento 3040 — validação do IPOC.
 *
 * O IPOC é a Identificação Padronizada da Operação de Crédito.
 * Ele é composto por:
 * - 8 posições do CNPJ da instituição;
 * - 4 posições da modalidade;
 * - 1 posição do tipo de cliente;
 * - código do cliente conforme o tipo;
 * - código do contrato com até 40 posições.
 *
 * A unicidade do valor no SCR depende também da base consumidora.
 */
public final class IpocValidator {

    private static final Pattern DIGITOS_8 = Pattern.compile("^\\d{8}$");
    private static final Pattern DIGITOS_4 = Pattern.compile("^\\d{4}$");
    private static final Pattern DIGITOS_11 = Pattern.compile("^\\d{11}$");
    private static final Pattern DIGITOS_14 = Pattern.compile("^\\d{14}$");

    private IpocValidator() {}

    public static Optional<String> validar(String ipoc) {
        if (ipoc == null || ipoc.isBlank()) {
            return Optional.of("IPOC nao pode ser nulo ou vazio");
        }
        if (ipoc.length() < 22 || ipoc.length() > 67) {
            return Optional.of("IPOC deve ter entre 22 e 67 caracteres");
        }
        if (ipoc.length() < 13) {
            return Optional.of("IPOC incompleto");
        }

        String cnpjInstituicao = ipoc.substring(0, 8);
        String modalidade = ipoc.substring(8, 12);
        String tipoClienteCodigo = ipoc.substring(12, 13);

        if (!DIGITOS_8.matcher(cnpjInstituicao).matches()) {
            return Optional.of("CNPJ da instituicao deve conter 8 digitos");
        }
        if (!DIGITOS_4.matcher(modalidade).matches()) {
            return Optional.of("Modalidade da operacao deve conter 4 digitos");
        }

        TipoClienteIpoc tipoCliente = TipoClienteIpoc.fromCodigo(tipoClienteCodigo).orElse(null);
        if (tipoCliente == null) {
            return Optional.of("Tipo de cliente deve ser um valor entre 1 e 6");
        }

        int inicioCodigoCliente = 13;
        int fimCodigoCliente = inicioCodigoCliente + tipoCliente.tamanhoCodigoCliente();
        if (ipoc.length() < fimCodigoCliente + 1) {
            return Optional.of("IPOC incompleto para o tipo de cliente informado");
        }

        String codigoCliente = ipoc.substring(inicioCodigoCliente, fimCodigoCliente);
        Optional<String> erroCodigoCliente = validarCodigoClienteFormatado(codigoCliente, tipoCliente);
        if (erroCodigoCliente.isPresent()) {
            return erroCodigoCliente;
        }

        String codigoContrato = ipoc.substring(fimCodigoCliente);
        if (codigoContrato.isBlank()) {
            return Optional.of("Codigo do contrato nao pode ser vazio");
        }
        if (codigoContrato.length() > 40) {
            return Optional.of("Codigo do contrato deve ter ate 40 caracteres");
        }
        return Optional.empty();
    }

    public static Optional<String> validarComponentes(
            String cnpjInstituicao,
            String modalidade,
            TipoClienteIpoc tipoCliente,
            String codigoCliente,
            String codigoContrato) {

        Optional<String> erroCnpj = validarCnpjInstituicaoEntrada(cnpjInstituicao);
        if (erroCnpj.isPresent()) {
            return erroCnpj;
        }

        if (modalidade == null || !DIGITOS_4.matcher(modalidade.trim()).matches()) {
            return Optional.of("Modalidade da operacao deve conter 4 digitos");
        }

        if (tipoCliente == null) {
            return Optional.of("Tipo de cliente deve ser informado");
        }

        Optional<String> erroCodigoCliente = validarCodigoClienteEntrada(codigoCliente, tipoCliente);
        if (erroCodigoCliente.isPresent()) {
            return erroCodigoCliente;
        }

        if (codigoContrato == null || codigoContrato.isBlank()) {
            return Optional.of("Codigo do contrato nao pode ser vazio");
        }
        if (codigoContrato.trim().length() > 40) {
            return Optional.of("Codigo do contrato deve ter ate 40 caracteres");
        }
        return Optional.empty();
    }

    public static String montar(
            String cnpjInstituicao,
            String modalidade,
            TipoClienteIpoc tipoCliente,
            String codigoCliente,
            String codigoContrato) {

        Optional<String> erro = validarComponentes(
                cnpjInstituicao, modalidade, tipoCliente, codigoCliente, codigoContrato);
        if (erro.isPresent()) {
            throw new IllegalArgumentException(erro.get());
        }

        String cnpjNormalizado = normalizarCnpjInstituicao(cnpjInstituicao);
        String modalidadeNormalizada = modalidade.trim();
        String codigoClienteNormalizado = normalizarCodigoCliente(codigoCliente, tipoCliente);
        String codigoContratoNormalizado = codigoContrato.trim();

        return cnpjNormalizado
                + modalidadeNormalizada
                + tipoCliente.codigo()
                + codigoClienteNormalizado
                + codigoContratoNormalizado;
    }

    private static Optional<String> validarCodigoClienteFormatado(String codigoCliente, TipoClienteIpoc tipoCliente) {
        return switch (tipoCliente) {
            case PESSOA_FISICA_COM_CPF -> DIGITOS_11.matcher(codigoCliente).matches()
                    ? Optional.empty()
                    : Optional.of("CPF do cliente deve conter 11 digitos");
            case PESSOA_JURIDICA_COM_CNPJ -> DIGITOS_8.matcher(codigoCliente).matches()
                    ? Optional.empty()
                    : Optional.of("CNPJ do cliente deve conter 8 digitos");
            default -> codigoCliente.length() == 14
                    ? Optional.empty()
                    : Optional.of("Codigo do cliente deve ter 14 caracteres");
        };
    }

    private static Optional<String> validarCodigoClienteEntrada(String codigoCliente, TipoClienteIpoc tipoCliente) {
        if (codigoCliente == null || codigoCliente.isBlank()) {
            return Optional.of("Codigo do cliente nao pode ser vazio");
        }

        String normalizado = codigoCliente.trim();
        return switch (tipoCliente) {
            case PESSOA_FISICA_COM_CPF -> DIGITOS_11.matcher(soDigitos(normalizado)).matches()
                    ? Optional.empty()
                    : Optional.of("CPF do cliente deve conter 11 digitos");
            case PESSOA_JURIDICA_COM_CNPJ -> DIGITOS_8.matcher(soDigitos(normalizado)).matches()
                    ? Optional.empty()
                    : Optional.of("CNPJ do cliente deve conter 8 digitos");
            default -> normalizado.length() <= 14
                    ? Optional.empty()
                    : Optional.of("Codigo do cliente deve ter ate 14 caracteres");
        };
    }

    private static Optional<String> validarCnpjInstituicaoEntrada(String cnpjInstituicao) {
        String normalizado = soDigitos(cnpjInstituicao);
        if (normalizado.length() != 8 && normalizado.length() != 14) {
            return Optional.of("CNPJ da instituicao deve conter 8 digitos ou um CNPJ completo com 14 digitos");
        }
        return Optional.empty();
    }

    private static String normalizarCnpjInstituicao(String cnpjInstituicao) {
        String normalizado = soDigitos(cnpjInstituicao);
        if (normalizado.length() == 14) {
            return normalizado.substring(0, 8);
        }
        return normalizado;
    }

    private static String normalizarCodigoCliente(String codigoCliente, TipoClienteIpoc tipoCliente) {
        String normalizado = codigoCliente.trim();
        return switch (tipoCliente) {
            case PESSOA_FISICA_COM_CPF, PESSOA_JURIDICA_COM_CNPJ -> soDigitos(normalizado);
            default -> leftPadZeros(normalizado, 14);
        };
    }

    private static String soDigitos(String value) {
        return value == null ? "" : value.replaceAll("[^0-9]", "");
    }

    private static String leftPadZeros(String value, int tamanho) {
        if (value.length() >= tamanho) {
            return value;
        }
        return "0".repeat(tamanho - value.length()) + value;
    }
}
