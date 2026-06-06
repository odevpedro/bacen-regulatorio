package com.bacen.regulatorio.pix.validator;

import java.util.Optional;
import java.util.regex.Pattern;

/**
 * Resolução BCB 1/2020 Anexo II — payload do QR Code PIX (EMVCo).
 *
 * O payload segue o padrão EMVCo QRCPS (QR Code Payment System):
 * - Tag-Length-Value (TLV) encadeado
 * - Payload Format Indicator (00) = "01"
 * - Merchant Account Information (26/27/28/29/30/31) com GUI "BR.GOV.BCB.PIX"
 * - Merchant Category Code (52)
 * - Merchant Name (59)
 * - Merchant City (60)
 * - CRC (63) no final
 */
public final class QrCodePayloadValidator {

    private static final Pattern CRC_PATTERN = Pattern.compile(
            "^[0-9A-F]{4}$");

    private static final int PFI_TAG = 0;
    private static final int GUI_TAG_1 = 26;
    private static final int GUI_TAG_2 = 27;
    private static final int GUI_TAG_3 = 28;
    private static final int GUI_TAG_4 = 29;
    private static final int GUI_TAG_5 = 30;
    private static final int GUI_TAG_6 = 31;
    private static final int MCC_TAG = 52;
    private static final int MERCHANT_NAME_TAG = 59;
    private static final int MERCHANT_CITY_TAG = 60;
    private static final int CRC_TAG = 63;

    private QrCodePayloadValidator() {}

    /**
     * Valida a estrutura EMVCo do payload do QR Code PIX.
     * Verificações: PFI=01, presença do GUI BR.GOV.BCB.PIX, CRC no final.
     */
    public static Optional<String> validarPayload(String payload) {
        if (payload == null || payload.isEmpty()) {
            return Optional.of("Payload não pode ser nulo ou vazio");
        }

        int pos = 0;
        boolean hasPixGui = false;
        boolean hasCrc = false;

        while (pos < payload.length()) {
            if (pos + 2 > payload.length()) {
                return Optional.of("Tag inválida no payload (posição " + pos + ")");
            }
            int tagNum;
            try {
                tagNum = Integer.parseInt(payload.substring(pos, pos + 2));
            } catch (NumberFormatException e) {
                return Optional.of("Tag inválida: " + payload.substring(pos, pos + 2));
            }
            pos += 2;

            if (pos + 2 > payload.length()) {
                return Optional.of("Length inválido para tag " + tagNum + " (posição " + pos + ")");
            }
            int length;
            try {
                length = Integer.parseInt(payload.substring(pos, pos + 2));
            } catch (NumberFormatException e) {
                return Optional.of("Length inválido para tag " + tagNum);
            }
            pos += 2;

            if (pos + length > payload.length()) {
                return Optional.of("Valor truncado para tag " + tagNum);
            }
            String value = payload.substring(pos, pos + length);
            pos += length;

            if (tagNum == PFI_TAG && !"01".equals(value)) {
                return Optional.of("Payload Format Indicator deve ser 01");
            }
            if (tagNum >= GUI_TAG_1 && tagNum <= GUI_TAG_6 && value.contains("BR.GOV.BCB.PIX")) {
                hasPixGui = true;
            }
            if (tagNum == CRC_TAG) {
                hasCrc = true;
                if (!CRC_PATTERN.matcher(value).matches()) {
                    return Optional.of("CRC deve ter 4 caracteres hexadecimais");
                }
            }
        }

        if (!hasPixGui) {
            return Optional.of("Payload não contém GUI BR.GOV.BCB.PIX");
        }
        if (!hasCrc) {
            return Optional.of("Payload não contém CRC (tag 63)");
        }
        return Optional.empty();
    }

    /**
     * Extrai o valor do GUI (Merchant Account Information) PIX do payload.
     * Retorna o template TLV completo da seção que contém o GUI do BCB.
     */
    public static Optional<String> extrairSecaoPix(String payload) {
        if (payload == null || payload.isEmpty()) return Optional.empty();

        int pos = 0;
        while (pos < payload.length()) {
            if (pos + 2 > payload.length()) break;
            int tagNum = Integer.parseInt(payload.substring(pos, pos + 2));
            pos += 2;
            if (pos + 2 > payload.length()) break;
            int length = Integer.parseInt(payload.substring(pos, pos + 2));
            pos += 2;
            if (pos + length > payload.length()) break;
            String value = payload.substring(pos, pos + length);
            pos += length;

            if (tagNum >= GUI_TAG_1 && tagNum <= GUI_TAG_6 && value.contains("BR.GOV.BCB.PIX")) {
                return Optional.of(value);
            }
        }
        return Optional.empty();
    }
}
