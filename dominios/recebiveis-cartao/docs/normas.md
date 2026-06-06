# Recebíveis de Cartão — Base Normativa

## Normas aplicáveis

| Norma | Data | Tema principal |
|-------|------|----------------|
| Resolução CMN 4.734/2019 | 27/06/2019 | Define unidade de recebível, agenda e gravames |
| Circular BCB 3.952/2019 | 27/06/2019 | Mensagens e campos obrigatórios da registradora |
| Circular BCB 4.016/2020 | 24/01/2020 | Interoperabilidade e titularidade |
| Resolução BCB 264/2022 | 25/11/2022 | Atualiza regras de cancelamento e alteração |

---

## Conceitos-chave

### Unidade de Recebível (Res. 4.734 Art. 3°)
Menor granularidade de recebível. Identificada por:
- Arranjo de pagamento (ex: VCC, MCC)
- CNPJ da credenciadora
- CPF/CNPJ do usuário do serviço de pagamento (estabelecimento)
- Data prevista de liquidação

Uma UnidadeRecebivel tem:
- `valorTotal`: soma de todos os recebíveis brutos nessa combinação
- `valorComprometido`: soma dos gravames ativos
- `saldoDisponível = valorTotal - valorComprometido`

### Gravame / Garantia (Res. 4.734 Art. 4°–7°)
Vínculo entre um credor e uma unidade de recebível, usado como colateral.

Regras obrigatórias:
1. Prioridade única por unidade ativa
2. valor_gravame <= saldo_disponivel
3. Cancelamento libera o saldo imediatamente
4. Alteração re-valida todas as regras

### Indicador de Interoperabilidade (Circ. 4.016 Art. 2° IX)
- `SI` — Sem interoperabilidade (recebível permanece na credenciadora original)
- `CI` — Com interoperabilidade (pode ser transferido entre credenciadoras)
- `NI` — Não informado

### Número de Controle Único (Circ. 3.952)
Gerado pela registradora ao aceitar uma operação. O solicitante só obtém esse número ao consumir o evento de resposta no tópico de saída.

---

## Fluxo de Inclusão

```
Participante                    Registradora
    |                                |
    |-- POST /negociacoes ---------->|
    |<-- 202 {correlation_id} -------|
    |                                |
    |   [Kafka: inclusao_solicitada] |
    |                          valida regras
    |                          persiste gravame
    |                          atualiza saldo
    |   [Kafka: inclusao_aceita]     |
    |<-- {numero_controle} ----------|
```

---

## Motivos de Recusa

| Código | Descrição |
|--------|-----------|
| `PRIORIDADE_DUPLICADA` | Já existe gravame ativo com mesma prioridade na unidade |
| `SALDO_INSUFICIENTE` | valor_gravame > saldo_disponivel |
| `NEGOCIACAO_NAO_ENCONTRADA` | numero_controle inexistente |
| `NEGOCIACAO_JA_CANCELADA` | Operação sobre negociação cancelada |
| `CPF_CNPJ_INVALIDO` | Dígitos verificadores inválidos |
| `INTEROPERABILIDADE_INVALIDA` | Valor fora de SI/CI/NI |

---

## Implementação neste módulo

| Classe | Função |
|--------|--------|
| `CpfCnpjValidator` | Validação de CPF e CNPJ com dígito verificador |
| `RecebivelValidator` | Regras de prioridade, saldo e interoperabilidade |
| `UnidadeRecebivelId` | Value object com cálculo de chaveUnica (SHA-256) |
| Enums: `TipoEvento`, `StatusNegociacao`, `MotivoRecusa`, `IndicadorInteroperabilidade` | Domínio completo |
