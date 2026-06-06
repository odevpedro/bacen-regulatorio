# Open Finance — Base Normativa

## Normas aplicáveis

| Norma | Data | Tema principal |
|-------|------|----------------|
| Resolução BCB 32/2020 | 29/10/2020 | Institui o Open Banking (Fase 1 e 2) |
| Resolução BCB 57/2021 | 26/01/2021 | Iniciação de pagamento (Fase 3) |
| Resolução BCB 97/2021 | 29/04/2021 | Serviços de câmbio no Open Finance |
| Resolução BCB 316/2023 | 27/07/2023 | Atualiza escopo — Fase 4 (seguros, previdência) |
| Instrução Normativa BCB 266/2022 | — | Especificações técnicas das APIs |

---

## Fases de Implementação

| Fase | Vigência | Escopo |
|------|---------|--------|
| 1 | fev/2021 | Dados públicos: produtos, tarifas, canais |
| 2 | ago/2021 | Dados do cliente: contas, cartões, crédito |
| 3 | out/2021 | Iniciação de pagamento (Pix via OF) |
| 4 | dez/2021 | Câmbio, investimentos, seguros, previdência |

---

## Conceitos-chave

### Consentimento (Res. BCB 32 Art. 10–19)
O usuário autoriza explicitamente cada permissão, com prazo de validade.

Ciclo de vida:
```
AWAITING_AUTHORISATION → AUTHORISED → [uso dos dados]
                      ↘ REJECTED
AUTHORISED → REVOKED (pelo usuário a qualquer momento)
AUTHORISED → CONSUMED (após uso em iniciação de pagamento)
```

Restrições:
- Validade mínima: 1 segundo
- Validade máxima: 12 meses
- O usuário pode revogar a qualquer momento via canal da instituição
- Permissões são granulares e explícitas

### Permissões e Dependências
Algumas permissões têm dependência hierárquica:
- `ACCOUNTS_TRANSACTIONS_READ` requer `ACCOUNTS_READ`
- `ACCOUNTS_BALANCES_READ` requer `ACCOUNTS_READ`
- `CREDIT_CARDS_ACCOUNTS_TRANSACTIONS_READ` requer `CREDIT_CARDS_ACCOUNTS_READ`

### FAPI — Financial-grade API
Protocolo de segurança obrigatório para as APIs do Open Finance.
Baseado em OAuth 2.0 + PKCE + mTLS (certificados ICP-Brasil).

---

## Fluxo de Consentimento (Fase 2)

```
Usuário        Instituição Receptora      Instituição Transmissora
   |                    |                           |
   |-- acessa app ----->|                           |
   |                    |-- POST /consents -------->|
   |                    |<-- consentId -------------|
   |<-- redirect -------|                           |
   |-- autoriza ---------------------------------------->|
   |                    |                    status=AUTHORISED
   |                    |-- GET /accounts --------->|
   |                    |<-- dados com consentId ----|
```

---

## Implementação neste módulo

| Classe | Função |
|--------|--------|
| `ConsentimentoValidator` | Validade, status, permissões e dependências |
| `Consentimento` | Value object com verificação de atividade |
| Enums: `StatusConsentimento`, `PermissaoConsentimento`, `FaseOpenFinance` | Domínio completo |
