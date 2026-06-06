# Arranjos de Pagamento — Base Normativa

## Normas aplicáveis

| Norma | Data | Tema principal |
|-------|------|----------------|
| Resolução CMN 4.282/2013 | 04/11/2013 | Define arranjos de pagamento e instituidores |
| Lei 12.865/2013 | 09/10/2013 | Marco legal dos pagamentos (Lei dos Meios de Pagamento) |
| Resolução BCB 150/2021 | 26/10/2021 | Atualiza critérios de vigilância e regulação |
| Resolução BCB 195/2022 | 10/03/2022 | Tarifação e transparência nos arranjos |

---

## Conceitos-chave

### Arranjo de Pagamento (Res. CMN 4.282 Art. 2°)
Conjunto de regras e procedimentos que disciplina a prestação de serviços de pagamento ao público.

Exemplos de arranjos regulados pelo BACEN:
- Cartões de crédito (Visa, Mastercard, Elo, Hipercard, Amex)
- Cartões de débito
- Cartões pré-pagos
- PIX

### Participantes do Arranjo

| Papel | Função |
|-------|--------|
| **Instituidor** | Cria e regulamenta o arranjo (ex: Visa, Mastercard) |
| **Emissor** | Emite o instrumento para o usuário pagador (bancos) |
| **Credenciador** | Habilita estabelecimentos a aceitar o instrumento |
| **Liquidante** | Realiza a liquidação financeira (pode ser o BACEN via SPB) |

### Critérios de Vigilância (Res. BCB 150)
O BACEN regula arranjos que atendam qualquer critério:
- Volume de transações > 1 bilhão/ano OU
- Valor transacionado > R$ 500 bilhões/ano OU
- Relevância sistêmica definida pelo BCB

### Taxa de Intercâmbio
Remuneração paga pelo credenciador ao emissor por transação.
- Regulada para cartões de débito (máx. 0,5%)
- Para crédito: livre, mas monitorada pelo CADE/BACEN

---

## Códigos de Arranjo (SPB)

| Código | Arranjo | Tipo |
|--------|---------|------|
| VCC | Visa Crédito à Vista | Crédito |
| MCC | Mastercard Crédito à Vista | Crédito |
| ECC | Elo Crédito à Vista | Crédito |
| VCD | Visa Débito | Débito |
| MCD | Mastercard Débito | Débito |
| ECD | Elo Débito | Débito |
| PIX | Pagamento Instantâneo | Instantâneo |

---

## Implementação neste módulo

| Classe | Função |
|--------|--------|
| `ArranjoValidator` | Valida código, elegibilidade para recebíveis e para crédito |
| `CodigoArranjo` | Enum com todos os arranjos registrados no BACEN |
| `TipoParticipante` | Papéis dos participantes no ecossistema |
