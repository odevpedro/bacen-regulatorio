# Evidencias - PIX

## Escopo

Base documental para chave Pix, txid, end-to-end id, QR Code EMVCo, limites noturnos e
Pix Automatico.

## Evidencias primarias esperadas

| Norma | Nome de cache sugerido | Fonte oficial |
|-------|------------------------|---------------|
| Resolução BCB 1/2020 | `pdf/2020-08-12_res-bcb-1-pix.pdf` | Portal de normativos do BACEN |
| Resolução BCB 80/2021 | `pdf/2021-03-25_res-bcb-80-pix-saque-troco.pdf` | Portal de normativos do BACEN |
| Resolução BCB 142/2021 | `pdf/2021-09-30_res-bcb-142-limite-noturno.pdf` | Portal de normativos do BACEN |
| Resolução BCB 191/2022 | `pdf/2022-03-03_res-bcb-191-pix-automatico.pdf` | Portal de normativos do BACEN |
| Resolução BCB 316/2023 | `pdf/2023-07-27_res-bcb-316-pix-aproximacao.pdf` | Portal de normativos do BACEN |

## Regras associadas

- formato das chaves DICT
- limite noturno
- txid
- end-to-end id
- QR Code EMVCo

## Uso no codigo

- `ChavePixValidator`
- `TxidValidator`
- `EndToEndIdValidator`
- `QrCodePayloadValidator`
- `LimitePixValidator`
- value objects de Pix
