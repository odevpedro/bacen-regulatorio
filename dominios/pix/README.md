# PIX

> Implementacao minima das regras regulatorias do arranjo de pagamento instantaneo PIX conforme Resolucao BCB 1/2020.

---

## Normas cobertas

| Norma | Status |
|-------|--------|
| Resolucao BCB 1/2020 | Coberta |
| Resolucao BCB 142/2021 (limites) | Coberta |
| Resolucao BCB 191/2022 (Pix Automatico) | Parcial — enums |

---

## Stack

| Item | Tecnologia |
|------|-----------|
| Runtime | Java 21 |
| Build | Maven (modulo do parent) |
| Testes | JUnit 5 + AssertJ |

---

## Estrutura

```
src/
├── main/java/com/bacen/regulatorio/pix/
│   ├── enums/
│   │   ├── TipoChavePix.java
│   │   ├── TipoOperacaoPix.java
│   │   └── MotivoDevolucao.java
│   ├── validator/
│   │   ├── ChavePixValidator.java
│   │   ├── LimitePixValidator.java
│   │   ├── EndToEndIdValidator.java
│   │   ├── TxidValidator.java
│   │   └── QrCodePayloadValidator.java
│   └── valueobject/
│       ├── ChavePix.java
│       ├── EndToEndId.java
│       └── Txid.java
└── test/java/com/bacen/regulatorio/pix/validator/
    ├── ChavePixValidatorTest.java
    ├── EndToEndIdValidatorTest.java
    ├── TxidValidatorTest.java
    └── QrCodePayloadValidatorTest.java
exemplos/
├── pagamento_pix.json
├── qrcode_dinamico.json
└── devolucao.json
docs/
└── normas.md
```

---

## Regras implementadas

| Regra | Classe | Norma |
|-------|--------|-------|
| Validacao de CPF como chave | `ChavePixValidator` | Res. BCB 1 Art. 7 |
| Validacao de CNPJ como chave | `ChavePixValidator` | Res. BCB 1 Art. 8 |
| Validacao de e-mail (max 77 chars) | `ChavePixValidator` | Res. BCB 1 Art. 9 |
| Validacao de telefone (+55XXXXXXXXXXX) | `ChavePixValidator` | Res. BCB 1 Art. 10 |
| Validacao de EVP (UUID v4) | `ChavePixValidator` | Res. BCB 1 Art. 11 |
| Deteccao automatica de tipo de chave | `ChavePixValidator.detectarTipo` | Res. BCB 1 |
| Limite noturno (20h–06h) | `LimitePixValidator` | Res. BCB 142/2021 |
| End-to-End ID (E{ISPB}{data}{hora}{seq}) | `EndToEndIdValidator` | Res. BCB 1 |
| Txid (26 a 35 alfanumericos) | `TxidValidator` | Res. BCB 1 |
| Payload QR Code EMVCo (TLV, GUI PIX, CRC) | `QrCodePayloadValidator` | Res. BCB 1 Anexo II |
| Validacao estrutural de mandato | `MandatoPixValidator.validarMandato` | Res. BCB 191/2022 |
| Validacao completa de cobranca recorrente | `MandatoPixValidator.validarCobranca` | Res. BCB 191/2022 |

> Validacao de CPF/CNPJ delegada para `CpfCnpjValidator` no modulo `commons`.

---

## Uso como dependencia

```xml
<dependency>
    <groupId>com.bacen.regulatorio</groupId>
    <artifactId>bacen-regulatorio-pix</artifactId>
    <version>1.1.0</version>
</dependency>
```

```java
// Validar chave antes de consultar o DICT
ChavePix chave = ChavePix.of("user@banco.com.br");

// Validar limite noturno
boolean permitido = LimitePixValidator.isValorPermitido(
    new BigDecimal("500.00"), LocalTime.now());

// Validar End-to-End ID da transacao
EndToEndId e2e = new EndToEndId("E1234567820240101123456ABC");

// Validar payload do QR Code
QrCodePayloadValidator.validarPayload(payload)
    .ifPresent(erro -> recusarCobranca(erro));

// Validar uma cobranca recorrente completa
MandatoPixValidator.validarCobranca(mandato, valor, totalCobradoNoMes, quantidadeJaRealizada)
    .ifPresent(erro -> recusarCobrancaRecorrente(erro));
```

---

## Documentacao detalhada

Ver [docs/normas.md](docs/normas.md) para fluxos ISO 20022, codigos E2EID e motivos de devolucao.
