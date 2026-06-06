# System Feature Flows — bacen-regulatorio

> Registro histórico e incremental dos fluxos internos de cada funcionalidade.
> Este documento cresce a cada novo domínio implementado e nunca tem seções removidas.

---

## Indice

- [Visao Geral da Arquitetura](#visao-geral-da-arquitetura)
- [Convencoes deste Documento](#convencoes-deste-documento)
- [Feature: Validacao de Recebiveis de Cartao](#feature-validacao-de-recebiveis-de-cartao)
- [Feature: Validacao de Chaves e Limites PIX](#feature-validacao-de-chaves-e-limites-pix)
- [Feature: End-to-End ID e Txid PIX](#feature-end-to-end-id-e-txid-pix)
- [Feature: Payload QR Code EMVCo PIX](#feature-payload-qr-code-emvco-pix)
- [Feature: Ciclo de Vida do Consentimento Open Finance](#feature-ciclo-de-vida-do-consentimento-open-finance)
- [Feature: Arranjos de Pagamento](#feature-arranjos-de-pagamento)
- [Feature: Monitoramento PLD/FT](#feature-monitoramento-pldft)
- [Feature: Modulo Commons — Validacao Compartilhada](#feature-modulo-commons---validacao-compartilhada)

---

## Visao Geral da Arquitetura

Padrão: **Biblioteca de domínio puro** — sem framework, sem persistência.

```
Código do projeto consumidor
    └── Validator (retorna Optional<Erro>)
            └── ValueObject (imutável, valida no construtor)
                    └── Enum (documenta o domínio com javadoc normativo)
```

Cada módulo é independente:

| Módulo | Pacote | Dependências externas |
|--------|--------|-----------------------|
| recebiveis-cartao | `com.bacen.regulatorio.recebiveis` | Nenhuma |
| pix | `com.bacen.regulatorio.pix` | Nenhuma |
| open-finance | `com.bacen.regulatorio.openfinance` | Nenhuma |
| arranjos-pagamento | `com.bacen.regulatorio.arranjos` | Nenhuma |
| pld-ft | `com.bacen.regulatorio.pldft` | Nenhuma |

---

## Convencoes deste Documento

- Validators retornam `Optional.empty()` quando a regra passa
- Value objects lançam `IllegalArgumentException` se criados com dados inválidos
- Testes usam `@DisplayName("Norma — comportamento")` para documentação viva
- Enums têm javadoc com o artigo da norma que os define

---
---

# Feature: Validação de Recebíveis de Cartão

> **Versao:** 1.0.0
> **Implementada em:** 2026-05-03
> **Status:** Concluida

---

## Resumo

Implementa as regras da Resolução 4.734/2019 e Circular 3.952/2019 para validar operações sobre gravames em unidades de recebíveis de cartão. Permite que qualquer projeto Java use essas regras como biblioteca sem precisar reimplementá-las.

**Motivacao:** Regras complexas e específicas que, sem uma biblioteca, são reimplementadas de formas diferentes em cada projeto, gerando divergências regulatórias.
**Resultado:** Validators composíveis com `Optional`, value object para identidade da unidade, e todos os enums do domínio.

---

## Fluxo Principal

### Composição de validações (uso típico)

```java
Optional<MotivoRecusa> erro = RecebivelValidator.validarCpfCnpj(cpf, cnpj)
    .or(() -> RecebivelValidator.validarInteroperabilidade(indicador))
    .or(() -> RecebivelValidator.validarPrioridade(prioridadeExiste))
    .or(() -> RecebivelValidator.validarSaldo(valorGarantia, saldoDisponivel));

erro.ifPresentOrElse(
    motivo -> publicarRecusada(numeroPedido, motivo.name()),
    () -> aceitarEPersistir(request)
);
```

---

## Regras de Negocio

| Regra | Metodo | Norma |
|-------|--------|-------|
| CPF/CNPJ válidos | `validarCpfCnpj` | Circ. 4.016 Art. 2° |
| Interoperabilidade SI/CI/NI | `validarInteroperabilidade` | Circ. 4.016 Art. 2° IX |
| Prioridade única por unidade ativa | `validarPrioridade` | Res. 4.734 Art. 4° |
| Saldo disponível suficiente | `validarSaldo` | Res. 4.734 Art. 7° |
| Saldo recalculado em alteração | `validarSaldoAlteracao` | Res. 4.734 Art. 7° |

---

## Decisoes Tecnicas

### ADR-001 — Optional em vez de exceções nos validators

| Campo | Detalhe |
|-------|---------|
| **Status** | Aceita |
| **Data** | 2026-05-03 |
| **Contexto** | Validators são chamados em pipelines de processamento Kafka onde exceções interrompem o fluxo |
| **Decisao** | Retornar `Optional<MotivoRecusa>` permite compor validações com `.or()` sem try/catch |
| **Consequencias** | Código mais limpo no chamador; o value object ainda lança exceção se construído diretamente com dados inválidos |

---
---

# Feature: Validação de Chaves e Limites PIX

> **Versao:** 1.0.0
> **Implementada em:** 2026-05-03
> **Status:** Concluida

---

## Resumo

Valida os 5 tipos de chave Pix (CPF, CNPJ, EMAIL, TELEFONE, EVP) com detecção automática de tipo, e implementa a regra de limite noturno da Resolução BCB 142/2021.

---

## Fluxo de Validação de Chave

```
Chave Pix recebida
    └── ChavePixValidator.detectarTipo(valor)
            ├── CPF  → algoritmo dígito verificador
            ├── CNPJ → algoritmo dígito verificador
            ├── EMAIL → regex + tamanho <= 77
            ├── TELEFONE → regex +55XXXXXXXXXXX
            └── EVP → regex UUID v4
```

---

## Regras de Negocio

| Regra | Metodo | Norma |
|-------|--------|-------|
| CPF com dígito verificador | `isValid(..., CPF)` | Res. BCB 1 Art. 7° |
| CNPJ com dígito verificador | `isValid(..., CNPJ)` | Res. BCB 1 Art. 8° |
| E-mail <= 77 caracteres | `isValid(..., EMAIL)` | Res. BCB 1 Art. 9° |
| Telefone: +55 + DDD + número | `isValid(..., TELEFONE)` | Res. BCB 1 Art. 10° |
| EVP: UUID v4 | `isValid(..., EVP)` | Res. BCB 1 Art. 11° |
| Limite noturno (20h–06h) | `LimitePixValidator.isValorPermitido` | Res. BCB 142/2021 |

---
---

# Feature: Ciclo de Vida do Consentimento Open Finance

> **Versao:** 1.0.0
> **Implementada em:** 2026-05-03
> **Status:** Concluida

---

## Resumo

Valida o consentimento do Open Finance: validade temporal, status, permissões concedidas versus solicitadas, e dependências hierárquicas entre permissões.

---

## Fluxo de Uso do Consentimento

```
API recebe requisição de dados
    └── Consentimento.isAtivo()
            ├── false → 403 Forbidden (consentimento expirado/revogado)
            └── true
                └── Consentimento.hasPermissao(ACCOUNTS_READ)
                        ├── false → 403 Forbidden (sem permissão)
                        └── true → buscar dados e retornar
```

---

## Regras de Negocio

| Regra | Metodo | Norma |
|-------|--------|-------|
| Validade 1s a 12 meses | `validarValidade` | Res. BCB 32 Art. 12° |
| Status AUTHORISED para uso | `validarStatusParaUso` | Res. BCB 32 Art. 14° |
| Permissões solicitadas <= concedidas | `validarPermissoes` | Res. BCB 32 Art. 13° |
| TRANSACTIONS_READ requer READ | `validarDependenciasPermissoes` | Res. BCB 32 |
| Ativo = AUTHORISED + não expirado | `Consentimento.isAtivo` | Res. BCB 32 Art. 16° |

---
---

# Feature: Arranjos de Pagamento

> **Versao:** 1.0.0
> **Implementada em:** 2026-05-03
> **Status:** Concluida

---

## Resumo

Catálogo dos arranjos de pagamento registrados no BACEN com validações de elegibilidade para produtos de crédito e recebíveis de cartão.

---

## Regras de Negocio

| Regra | Metodo | Norma |
|-------|--------|-------|
| Código registrado no BCB | `validarCodigo` | Res. CMN 4.282 |
| Arranjo elegível para recebíveis | `validarArranjoParaRecebiveis` | Res. BCB 150/2021 |
| Arranjo elegível para crédito parcelado | `validarElegibilidadeCredito` | Res. CMN 4.282 |

---
---

# Feature: Monitoramento PLD/FT

> **Versao:** 1.0.0
> **Implementada em:** 2026-05-03
> **Status:** Concluida

---

## Resumo

Implementa as regras de monitoramento de operações atípicas da Circular BCB 3.978/2020: limites de espécie, detecção de fracionamento, perfil de risco KYC e regras para PEP.

---

## Fluxo de Monitoramento

```
Operação financeira recebida
    └── OperacaoAtipicaValidator.avaliarOperacaoEspecie(valor)
            ├── [] vazio → operação regular
            ├── [ESPECIE_ACIMA_LIMITE] → registrar obrigatoriamente
            └── [ESPECIE_ACIMA_LIMITE, ESPECIE_COMUNICACAO_COAF] → comunicar COAF

    └── OperacaoAtipicaValidator.isFracionamentoSuspeito(operacoesRecentes, 24h)
            ├── false → regular
            └── true → marcar para análise manual

    └── PerfilRiscoCliente.precisaRevisao()
            ├── false → KYC em dia
            └── true → iniciar processo de atualização
```

---

## Regras de Negocio

| Regra | Metodo | Norma |
|-------|--------|-------|
| Espécie >= R$2.000 → registro | `avaliarOperacaoEspecie` | Circ. 3.978 Art. 36° |
| Espécie >= R$10.000 → COAF | `avaliarOperacaoEspecie` | Circ. 3.978 Art. 37° |
| Fracionamento suspeito | `isFracionamentoSuspeito` | Circ. 3.978 Anexo I |
| Incompatibilidade com perfil | `isIncompativelComPerfil` | Circ. 3.978 Art. 19° |
| Prazo de revisão por nível de risco | `PerfilRiscoCliente.precisaRevisao` | Circ. 3.978 Art. 22° |
| PEP eleva automaticamente para REFORCADO | `PerfilRiscoCliente.nivelEfetivo` | Res. BCB 277/2022 |

---

---

# Feature: End-to-End ID e Txid PIX

> **Versao:** 1.1.0
> **Implementada em:** 2026-06-03
> **Status:** Concluida

---

## Resumo

Validacao do formato do End-to-End ID (identificador unico de transacao no SPI) e do txid (identificador da cobranca PIX) conforme Resolucao BCB 1/2020.

**Motivacao:** O E2E ID e o txid sao campos críticos em mensagens ISO 20022 do SPI. Formatos invalidos causam rejeicao das transacoes pelas instituicoes liquidantes.
**Resultado:** Validadores especificos + value objects imutaveis com validacao no construtor.

---

## Regras de Negocio

| Regra | Metodo | Norma |
|-------|--------|-------|
| E2E ID: formato E{ISPB}{data}{hora}{sequencial} | `EndToEndIdValidator.validar` | Res. BCB 1 |
| E2E ID: 24 a 35 caracteres | `EndToEndIdValidator.validar` | Res. BCB 1 |
| E2E ID: prefixo E fixo | `EndToEndIdValidator.validar` | Res. BCB 1 |
| txid: 26 a 35 caracteres alfanumericos | `TxidValidator.validar` | Res. BCB 1 |

---

---

# Feature: Payload QR Code EMVCo PIX

> **Versao:** 1.1.0
> **Implementada em:** 2026-06-03
> **Status:** Concluida

---

## Resumo

Validacao do payload do QR Code PIX segundo o padrao EMVCo QRCPS e Resolucao BCB 1 Anexo II. O payload e uma string TLV (Tag-Length-Value) que contem as informacoes da cobranca.

**Motivacao:** QR Codes PIX mal formatados sao recusados pelas instituicoes e causam frostacao na experiencia do usuario.
**Resultado:** Validador de estrutura TLV com verificacao de PFI=01, GUI BR.GOV.BCB.PIX obrigatorio e CRC no final.

---

## Fluxo de Validacao

```
Payload QR Code recebido
    └── QrCodePayloadValidator.validarPayload(payload)
            ├── payload nulo/vazio → erro
            └── parse TLV sequencial:
                    ├── tag 00 (PFI) = "01" obrigatorio
                    ├── tag 26-31 (Merchant Account Info)
                    │       └── contem "BR.GOV.BCB.PIX"? → PIX habilitado
                    ├── tag 52 (MCC)
                    ├── tag 59 (Merchant Name)
                    ├── tag 60 (Merchant City)
                    └── tag 63 (CRC) → 4 hex digitos obrigatorio
                            ├── sem GUI PIX → erro
                            ├── sem CRC → erro
                            └── OK → payload valido
```

---

## Regras de Negocio

| Regra | Metodo | Norma |
|-------|--------|-------|
| PFI deve ser 01 | `validarPayload` | EMVCo QRCPS / Res. BCB 1 Anexo II |
| GUI BR.GOV.BCB.PIX obrigatorio | `validarPayload` | Res. BCB 1 Anexo II |
| CRC (tag 63) obrigatorio no final | `validarPayload` | EMVCo QRCPS |
| Extracao da secao PIX do payload | `extrairSecaoPix` | Res. BCB 1 Anexo II |

---

---

# Feature: Modulo Commons — Validacao Compartilhada

> **Versao:** 1.1.0
> **Implementada em:** 2026-06-03
> **Status:** Concluida

---

## Resumo

Modulo compartilhado com utilitarios de validacao usados por multiplos dominios. Centraliza a logica de CPF e CNPJ que antes estava duplicada em `recebiveis-cartao` e `pix`.

**Motivacao:** Dois modulos implementavam validacao identica de CPF/CNPJ com digito verificador. Qualquer correcao ou melhoria precisava ser replicada manualmente.
**Resultado:** Um unico `CpfCnpjValidator` no modulo `commons`, usado pelos modulos `recebiveis-cartao` e `pix` via dependencia Maven.

---

## Regras de Negocio

| Regra | Metodo | Norma |
|-------|--------|-------|
| CPF com 11 digitos e DV | `isValid` / `isValidCpf` | Circular 4.016 |
| CNPJ com 14 digitos e DV | `isValid` / `isValidCnpj` | Circular 4.016 |
| Deteccao automatica CPF/CNPJ | `tipo` | Circular 4.016 |
