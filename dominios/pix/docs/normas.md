# PIX — Base Normativa

## Normas aplicáveis

| Norma | Data | Tema principal |
|-------|------|----------------|
| Resolução BCB 1/2020 | 12/08/2020 | Institui o PIX e define o SPI |
| Resolução BCB 80/2021 | 25/03/2021 | Pix Saque e Pix Troco |
| Resolução BCB 142/2021 | 30/09/2021 | Limites de valor (noturno) |
| Resolução BCB 191/2022 | 03/03/2022 | Pix Automático (débito recorrente) |
| Resolução BCB 316/2023 | 27/07/2023 | Pix por aproximação (NFC) |

---

## Conceitos-chave

### SPI — Sistema de Pagamentos Instantâneos
Infra operada pelo BACEN. Mensagens em ISO 20022:
- `pacs.008` — pagamento
- `pacs.004` — devolução
- `pain.013` — cobrança (QR dinâmico)

### DICT — Diretório de Identificadores de Contas Transacionais
Banco de dados gerido pelo BACEN que mapeia chaves Pix para contas.
- Cada CPF/CNPJ pode ter 1 chave do próprio tipo + outras (email, telefone, EVP)
- Máximo de 20 chaves por conta

### Chaves Pix (Res. BCB 1 Art. 7°–12°)

| Tipo | Formato | Validação |
|------|---------|-----------|
| CPF | 11 dígitos | Dígitos verificadores |
| CNPJ | 14 dígitos | Dígitos verificadores |
| E-mail | RFC 5321 | Máx. 77 caracteres |
| Telefone | +55XXXXXXXXXXX | DDD + 8 ou 9 dígitos |
| EVP | UUID v4 | Gerado pelo DICT |

### End-to-End ID (E2EID)
Identificador único de cada transação no SPI. Formato:
`E{ISPB_pagador}{AAAAMMDD}{HHmm}{XXXXXXXXXXXXXXX}`

### Limites de Valor (Res. BCB 142/2021)
- **Período noturno** (20h–06h): R$ 1.000,00 por transação (padrão, ajustável pelo usuário)
- **Período diurno** (06h–20h): sem limite regulatório

---

## Fluxo de Pagamento Pix

```
Pagador              PSP Pagador         SPI (BACEN)        PSP Recebedor
   |                      |                   |                    |
   |-- inicia Pix ------->|                   |                    |
   |                  valida chave            |                    |
   |                  consulta DICT           |                    |
   |                      |-- pacs.008 ------>|                    |
   |                      |                  |-- pacs.008 ------->|
   |                      |                  |<-- pacs.002 -------|
   |<-- comprovante -------|                  |                    |
```

---

## Motivos de Devolução (pacs.004)

| Código | Motivo |
|--------|--------|
| `MD06` | Fraude confirmada |
| `MD08` | Solicitação do pagador |
| `AM09` | Erro operacional da instituição |
| `AC03` | Conta encerrada |
| `BE01` | Beneficiário incorreto |

---

## Implementação neste módulo

| Classe | Função |
|--------|--------|
| `ChavePixValidator` | Validação de todos os tipos de chave + detecção automática |
| `LimitePixValidator` | Regra de limite noturno |
| `ChavePix` | Value object com validação no construtor |
| Enums: `TipoChavePix`, `TipoOperacaoPix`, `MotivoDevolucao` | Domínio completo |
