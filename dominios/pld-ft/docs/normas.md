# PLD/FT — Prevenção à Lavagem de Dinheiro e Financiamento ao Terrorismo

## Normas aplicáveis

| Norma | Data | Tema principal |
|-------|------|----------------|
| Lei 9.613/1998 | 03/03/1998 | Lei de Lavagem de Dinheiro |
| Circular BCB 3.978/2020 | 23/01/2020 | PLD/FT para instituições financeiras |
| Resolução BCB 277/2022 | 31/03/2022 | Atualiza Pessoa Exposta Politicamente (PEP) |
| Resolução COAF 36/2021 | 23/08/2021 | Comunicação de operações suspeitas |

---

## Conceitos-chave

### KYC — Conheça Seu Cliente (Circ. 3.978 Art. 3°–14°)
Processo de identificação e verificação da identidade do cliente.

Informações mínimas obrigatórias:
- CPF/CNPJ e nome completo
- Data de nascimento / constituição
- Endereço atualizado
- Renda/faturamento (para perfil de risco)
- Finalidade do relacionamento

### Perfil de Risco (Circ. 3.978 Art. 15°–22°)

| Nível | Diligência | Prazo de revisão |
|-------|-----------|-----------------|
| `SIMPLIFICADO` | Mínima — dados básicos | 2 anos |
| `NORMAL` | Padrão | 1 ano |
| `REFORCADO` | EDD (Enhanced Due Diligence) | 6 meses |

Fatores que elevam para REFORCADO:
- PEP (Pessoa Exposta Politicamente)
- Operações com países de alto risco (lista GAFI/FATF)
- Atividades de maior risco (câmbio, joias, imóveis)

### PEP — Pessoa Exposta Politicamente (Res. BCB 277/2022)
Agentes públicos que exercem ou exerceram nos últimos 5 anos:
- Cargos políticos (presidente, governadores, prefeitos, parlamentares)
- Cargos relevantes no Judiciário, Ministério Público, TCU
- Familiares de primeiro grau e sócios próximos também são considerados PEP

### Limites de Comunicação ao COAF (Circ. 3.978 Art. 36°–44°)

| Situação | Ação | Prazo |
|----------|------|-------|
| Espécie >= R$ 2.000 | Registro obrigatório | Imediato |
| Espécie >= R$ 10.000 | Comunicação ao COAF | Dia útil seguinte |
| Operação suspeita confirmada | Comunicação ao COAF | Dia útil seguinte |

---

## Fluxo de Monitoramento

```
Transação          Sistema de Monitoramento      Compliance
    |                        |                       |
    |-- evento ------------>|                        |
    |               avalia alertas                   |
    |               [ESPECIE_ACIMA_LIMITE]            |
    |                        |-- alerta ------------>|
    |                                         analisa contexto
    |                                         [confirmada suspeita]
    |                                                |-- COAF |
```

---

## Implementação neste módulo

| Classe | Função |
|--------|--------|
| `OperacaoAtipicaValidator` | Avalia alertas de espécie e fracionamento suspeito |
| `PerfilRiscoCliente` | Value object com nível efetivo e verificação de revisão |
| Enums: `NivelRiscoCliente`, `TipoOperacaoAtipica`, `StatusComunicacaoCoaf` | Domínio completo |
