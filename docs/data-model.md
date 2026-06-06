# Data Model — bacen-regulatorio

> Documento vivo do modelo de dados. Atualizado sempre que uma entidade for criada, alterada ou removida.
> **Ultima atualizacao:** 2026-06-03

---

## Indice

- [Visao Geral](#visao-geral)
- [Entidades (Value Objects)](#entidades-value-objects)
- [Enums e Dominio de Valores](#enums-e-dominio-de-valores)
- [Validators](#validators)
- [Decisoes de Modelagem](#decisoes-de-modelagem)

---

## Visao Geral

O projeto nao possui banco de dados ou ORM. O "modelo de dados" e representado por value objects imutaveis (records Java 21) e enums que documentam os conceitos regulatorios.

Cada modulo e uma biblioteca Java pura, sem estado ou persistencia.

---

## Entidades (Value Objects)

---

### UnidadeRecebivelId

> Identificador unico de uma unidade de recebivel conforme Res. 4.734/2019 Art. 3.

**Modulo:** `recebiveis-cartao`
**Pacote:** `com.bacen.regulatorio.recebiveis.valueobject`

| Campo | Tipo | Descricao |
|-------|------|-----------|
| `arranjosPagamento` | String | Codigo do arranjo de pagamento |
| `cpfCnpjCredenciadora` | String | CPF/CNPJ da credenciadora |
| `cpfCnpjUsuarioServicoPagamento` | String | CPF/CNPJ do usuario |
| `dataLiquidacao` | LocalDate | Data de liquidacao |

**Metodos:**
- `chaveUnica()` — SHA-256 dos 4 campos, retorna hex string

---

### ChavePix

> Chave Pix registrada no DICT conforme Res. BCB 1/2020.

**Modulo:** `pix`
**Pacote:** `com.bacen.regulatorio.pix.valueobject`

| Campo | Tipo | Descricao |
|-------|------|-----------|
| `tipo` | TipoChavePix | Tipo da chave |
| `valor` | String | Valor da chave no formato correto |

**Metodos:**
- `of(String valor)` — factory com deteccao automatica de tipo
- Construtor valida com `ChavePixValidator.isValid`

---

### EndToEndId

> Identificador unico de transacao no SPI.

**Modulo:** `pix`
**Pacote:** `com.bacen.regulatorio.pix.valueobject`

| Campo | Tipo | Descricao |
|-------|------|-----------|
| `valor` | String | Formato E{ISPB}{YYYYMMDD}{HHMMSS}{sequencial} |

---

### Txid

> Identificador unico de cobranca PIX.

**Modulo:** `pix`
**Pacote:** `com.bacen.regulatorio.pix.valueobject`

| Campo | Tipo | Descricao |
|-------|------|-----------|
| `valor` | String | 26-35 caracteres alfanumericos |

---

### Consentimento

> Consentimento do usuario para compartilhamento de dados no Open Finance.

**Modulo:** `open-finance`
**Pacote:** `com.bacen.regulatorio.openfinance.valueobject`

| Campo | Tipo | Descricao |
|-------|------|-----------|
| `consentId` | String | UUID |
| `cpfCnpjUsuario` | String | CPF/CNPJ do titular |
| `status` | StatusConsentimento | Status do ciclo de vida |
| `permissoes` | List<PermissaoConsentimento> | Permissoes concedidas |
| `dataCriacao` | OffsetDateTime | Data de criacao |
| `dataExpiracao` | OffsetDateTime | Data de expiracao (max 12 meses) |

**Metodos:**
- `isAtivo()` — status AUTHORISED + nao expirado
- `hasPermissao(PermissaoConsentimento)` — checa permissao especifica

---

### PerfilRiscoCliente

> Perfil de risco PLD/FT do cliente conforme Circ. 3.978/2020.

**Modulo:** `pld-ft`
**Pacote:** `com.bacen.regulatorio.pldft.valueobject`

| Campo | Tipo | Descricao |
|-------|------|-----------|
| `cpfCnpj` | String | Identificador do cliente |
| `nivelRisco` | NivelRiscoCliente | Classificacao de risco |
| `isPep` | boolean | Pessoa Exposta Politicamente |
| `paisesRelacionados` | List<String> | Paises com que o cliente opera |
| `dataUltimaRevisao` | LocalDate | Data da ultima revisao de perfil |
| `motivosRiscoElevado` | List<String> | Justificativa para risco reforcado |

**Metodos:**
- `precisaRevisao()` — prazo: 2 anos SIMPLIFICADO, 1 ano NORMAL, 6 meses REFORCADO
- `nivelEfetivo()` — PEP sempre eleva para REFORCADO

---

## Enums e Dominio de Valores

### recebiveis-cartao

#### StatusNegociacao
Usado em: operacoes de recebiveis
| Valor | Significado |
|-------|-------------|
| `PENDENTE` | Aguardando processamento |
| `ACEITA` | Operacao aceita |
| `RECUSADA` | Operacao recusada |
| `ALTERADA` | Operacao alterada |
| `CANCELADA` | Operacao cancelada |

#### MotivoRecusa
Usado em: `RecebivelValidator`
| Valor | Significado |
|-------|-------------|
| `PRIORIDADE_DUPLICADA` | Gravame com prioridade ja existente |
| `SALDO_INSUFICIENTE` | Valor supera saldo disponivel |
| `NEGOCIACAO_NAO_ENCONTRADA` | Numero de controle invalido |
| `NEGOCIACAO_JA_CANCELADA` | Operacao ja cancelada |
| `CPF_CNPJ_INVALIDO` | Documento com DV invalido |
| `INTEROPERABILIDADE_INVALIDA` | Indicador fora de SI/CI/NI |
| `CONFLITO_DE_NEGOCIACAO` | Conflito de operacoes simultaneas |
| `ERRO_INTERNO` | Erro interno |

#### IndicadorInteroperabilidade
Usado em: operacoes de recebiveis (Circ. 4.016 Art. 2 IX)
| Valor | Significado |
|-------|-------------|
| `SI` | Sem interoperabilidade |
| `CI` | Com interoperabilidade |
| `NI` | Nao informado |

#### TipoEvento
Usado em: operacoes de recebiveis
| Valor | Significado |
|-------|-------------|
| `INCLUSAO` | Inclusao de gravame |
| `ALTERACAO` | Alteracao de gravame |
| `CANCELAMENTO` | Cancelamento de gravame |

---

### pix

#### TipoChavePix
Usado em: `ChavePix` (Res. BCB 1)
| Valor | Significado |
|-------|-------------|
| `CPF` | CPF com 11 digitos e DV |
| `CNPJ` | CNPJ com 14 digitos e DV |
| `EMAIL` | Email <= 77 caracteres |
| `TELEFONE` | +55 + DDD + numero |
| `EVP` | UUID v4 |

#### TipoOperacaoPix
Usado em: operacoes PIX (Res. BCB 1)
| Valor | Significado |
|-------|-------------|
| `PAGAMENTO` | Pagamento padrao (pacs.008) |
| `DEVOLUCAO` | Devolucao (pacs.004) |
| `QR_ESTATICO` | QR Code estatico |
| `QR_DINAMICO` | QR Code dinamico |
| `AGENDADO` | Pix agendado |
| `AUTOMATICO` | Pix automatico |

#### MotivoDevolucao
Usado em: devolucoes PIX (Res. BCB 1)
| Valor | Significado |
|-------|-------------|
| `MD06_FRAUDE` | Fraude confirmada |
| `MD08_SOLICITACAO_PAGADOR` | Solicitacao do pagador |
| `AM09_ERRO_OPERACIONAL` | Erro operacional |
| `AC03_CONTA_ENCERRADA` | Conta encerrada |
| `BE01_BENEFICIARIO_INCORRETO` | Beneficiario incorreto |

---

### open-finance

#### StatusConsentimento
Usado em: `Consentimento` (Res. BCB 32)
| Valor | Significado |
|-------|-------------|
| `AWAITING_AUTHORISATION` | Aguardando autorizacao |
| `AUTHORISED` | Autorizado e ativo |
| `REJECTED` | Rejeitado |
| `CONSUMED` | Utilizado |
| `REVOKED` | Revogado |

#### PermissaoConsentimento
Usado em: `Consentimento` (Res. BCB 32)
| Valor | Significado |
|-------|-------------|
| `CUSTOMERS_PERSONAL_IDENTIFICATIONS_READ` | Dados cadastrais pessoa fisica |
| `CUSTOMERS_BUSINESS_IDENTIFICATIONS_READ` | Dados cadastrais pessoa juridica |
| `ACCOUNTS_READ` | Dados de conta |
| `ACCOUNTS_BALANCES_READ` | Saldos |
| `ACCOUNTS_TRANSACTIONS_READ` | Transacoes |
| `CREDIT_CARDS_ACCOUNTS_READ` | Dados de cartao |
| `PAYMENTS_INITIATE` | Iniciacao de pagamento |
| (demais permissoes no enum completo) | — |

#### FaseOpenFinance
Usado em: cronograma regulatorio
| Valor | Significado |
|-------|-------------|
| `FASE_1` | Dados publicos (fev/2021) |
| `FASE_2` | Dados cadastrais e transacionais (ago/2021) |
| `FASE_3` | Iniciacao de pagamento (out/2021) |
| `FASE_4` | Cambio, investimentos, seguros (dez/2021) |

---

### arranjos-pagamento

#### CodigoArranjo
Usado em: `ArranjoValidator` (Res. CMN 4.282)
| Valor | Significado | Categoria |
|-------|-------------|-----------|
| `VCC` | Visa Credito a Vista | Credito |
| `VCF` | Visa Credito Parcelado Lojista | Credito |
| `VCD` | Visa Debito | Debito |
| `VPS` | Visa Pre-Pago | Pre-Pago |
| `MCC` | Mastercard Credito a Vista | Credito |
| `MCD` | Mastercard Debito | Debito |
| `ECC` | Elo Credito a Vista | Credito |
| `PIX` | Arranjo de Pagamento Instantaneo | Instantaneo |
| `TED` | Transferencia Eletronica Disponivel | Transferencia |
| `DOC` | Documento de Ordem de Credito | Transferencia |
| (demais codigos no enum completo) | — | — |

**Metodos de classificacao:** `isCredito()`, `isDebito()`, `isPrePago()`

#### TipoParticipante
Usado em: arranjos de pagamento
| Valor | Significado |
|-------|-------------|
| `INSTITUIDOR` | Institui o arranjo |
| `EMISSOR` | Emite instrumento de pagamento |
| `CREDENCIADOR` | Credencia estabelecimentos |
| `LIQUIDANTE` | Realiza liquidacao |
| `PAGADOR` | Usuario pagador |
| `RECEBEDOR` | Usuario recebedor |

---

### pld-ft

#### NivelRiscoCliente
Usado em: `PerfilRiscoCliente` (Circ. 3.978 Art. 19)
| Valor | Significado |
|-------|-------------|
| `SIMPLIFICADO` | Baixo risco, diligencia simplificada |
| `NORMAL` | Risco medio, KYC padrao |
| `REFORCADO` | Alto risco, Enhanced Due Diligence |

#### TipoOperacaoAtipica
Usado em: `OperacaoAtipicaValidator` (Circ. 3.978 Anexo I)
| Valor | Significado |
|-------|-------------|
| `ESPECIE_ACIMA_LIMITE` | Especie >= R$2.000 |
| `ESPECIE_COMUNICACAO_COAF` | Especie >= R$10.000 |
| `FRACIONAMENTO_SUSPEITO` | Fracionamento para evitar limites |
| `INCOMPATIVEL_PERFIL` | Incompativel com perfil economico |
| `JURISDICAO_ALTO_RISCO` | Operacao com pais de alto risco |
| `PEP` | Pessoa Exposta Politicamente |
| `OCULTACAO_ORIGEM` | Tentativa de anonimizacao |
| `SEM_PROPOSITO_ECONOMICO` | Sem proposito economico aparente |

#### StatusComunicacaoCoaf
Usado em: comunicacao ao COAF (Circ. 3.978 Art. 36-44)
| Valor | Significado |
|-------|-------------|
| `PENDENTE_ANALISE` | Suspeita identificada, aguardando analise |
| `CONFIRMADA_SUSPEITA` | Suspeita confirmada |
| `DESCARTADA` | Considerada regular |
| `COMUNICADA` | Enviada ao COAF |
| `PROTOCOLO_GERADO` | COAF gerou protocolo |

---

## Validators

| Modulo | Validator | Metodo | Retorno |
|--------|-----------|--------|---------|
| commons | CpfCnpjValidator | `isValid(String)` | boolean |
| commons | CpfCnpjValidator | `tipo(String)` | String (CPF/CNPJ/null) |
| commons | CpfCnpjValidator | `isValidCpf(String)` | boolean |
| commons | CpfCnpjValidator | `isValidCnpj(String)` | boolean |
| recebiveis | RecebivelValidator | `validarPrioridade` | Optional<MotivoRecusa> |
| recebiveis | RecebivelValidator | `validarSaldo` | Optional<MotivoRecusa> |
| recebiveis | RecebivelValidator | `validarInteroperabilidade` | Optional<MotivoRecusa> |
| recebiveis | RecebivelValidator | `validarCpfCnpj` | Optional<MotivoRecusa> |
| recebiveis | RecebivelValidator | `validarSaldoAlteracao` | Optional<MotivoRecusa> |
| pix | ChavePixValidator | `isValid(valor, tipo)` | boolean |
| pix | ChavePixValidator | `detectarTipo(valor)` | TipoChavePix |
| pix | LimitePixValidator | `isPeriodoNoturno` | boolean |
| pix | LimitePixValidator | `isValorPermitido` | boolean |
| pix | EndToEndIdValidator | `validar(String)` | Optional<String> |
| pix | TxidValidator | `validar(String)` | Optional<String> |
| pix | QrCodePayloadValidator | `validarPayload(String)` | Optional<String> |
| pix | QrCodePayloadValidator | `extrairSecaoPix(String)` | Optional<String> |
| open-finance | ConsentimentoValidator | `validarValidade` | Optional<String> |
| open-finance | ConsentimentoValidator | `validarStatusParaUso` | Optional<String> |
| open-finance | ConsentimentoValidator | `validarPermissoes` | Optional<String> |
| open-finance | ConsentimentoValidator | `validarDependenciasPermissoes` | Optional<String> |
| arranjos | ArranjoValidator | `validarCodigo` | Optional<String> |
| arranjos | ArranjoValidator | `validarArranjoParaRecebiveis` | Optional<String> |
| arranjos | ArranjoValidator | `validarElegibilidadeCredito` | Optional<String> |
| pld-ft | OperacaoAtipicaValidator | `avaliarOperacaoEspecie` | List<TipoOperacaoAtipica> |
| pld-ft | OperacaoAtipicaValidator | `isIncompativelComPerfil` | boolean |
| pld-ft | OperacaoAtipicaValidator | `isFracionamentoSuspeito` | boolean |

---

## Decisoes de Modelagem

### ADR-DM-001 — Value objects como records Java 21

| Campo | Detalhe |
|-------|---------|
| **Status** | Aceita |
| **Data** | 2026-05-03 |
| **Contexto** | Necessidade de representar conceitos regulatorios como tipos imutaveis com validacao na construcao |
| **Decisao** | Usar records Java 21 com validacao no construtor compacto |
| **Alternativas consideradas** | Classes tradicionais com getters (mais boilerplate), POJOs com setters (mutaveis) |
| **Consequencias** | Imutabilidade garantida pela JVM, construtor compacto elimina codigo repetitivo, `equals`/`hashCode` automaticos |

### ADR-DM-002 — CpfCnpjValidator extraido para modulo commons

| Campo | Detalhe |
|-------|---------|
| **Status** | Aceita |
| **Data** | 2026-06-03 |
| **Contexto** | Validacao de CPF/CNPJ duplicada em recebiveis-cartao e pix com implementacoes identicas |
| **Decisao** | Extrair para modulo commons com API publica |
| **Consequencias** | Dependencia de commons adicionada em 2 modulos; eliminacao de ~60 linhas de codigo duplicado |

### ADR-DM-003 — Validators retornam Optional em vez de excecoes

| Campo | Detalhe |
|-------|---------|
| **Status** | Aceita |
| **Data** | 2026-05-03 |
| **Contexto** | Validators sao chamados em pipelines onde excecoes interrompem o fluxo |
| **Decisao** | Retornar `Optional<T>` para permitir composicao com `.or()` sem try/catch |
| **Consequencias** | Codigo mais limpo no chamador; value objects ainda lancam excecao no construtor |

### ADR-DM-004 — Identificador SHA-256 para unidade de recebivel

| Campo | Detalhe |
|-------|---------|
| **Status** | Aceita |
| **Data** | 2026-05-03 |
| **Contexto** | Unidade de recebivel e identificada por 4 campos (arranjo + credenciadora + usuario + data) |
| **Decisao** | Usar SHA-256 dos campos concatenados como chave unica |
| **Consequencias** | Chave compacta e indexavel; colisao praticamente impossivel; impossivel reverter para os campos originais |
