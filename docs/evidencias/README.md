# Evidencias Normativas

Este diretorio concentra a estrategia de rastreabilidade documental do projeto.

## Regra geral

- cada modulo possui uma pasta propria em `docs/evidencias/<modulo>/`;
- o repositório armazena o indice, o manifest machine-readable e a convencao de cache;
- os PDFs oficiais ficam em endpoints/fonte oficial do BACEN, do Planalto ou do Coaf, ou em cache externo fora do git;
- cada pasta tem um `README.md` com o mapeamento entre norma, endpoint e uso no codigo;
- toda regra implementada deve apontar para pelo menos uma evidencia primaria.

## Estrutura

```text
docs/evidencias/
├── README.md
├── manifest.yml
├── commons/
│   └── README.md
├── recebiveis-cartao/
│   └── README.md
├── pix/
│   └── README.md
├── open-finance/
│   └── README.md
├── arranjos-pagamento/
│   └── README.md
├── pld-ft/
│   └── README.md
├── cambio/
│   └── README.md
├── credito/
│   └── README.md
├── spb/
│   └── README.md
└── drex/
    └── README.md
```

## Manifest

O arquivo [manifest.yml](manifest.yml) e a fonte estruturada de todas as normas
utilizadas como evidencia. A pagina abaixo e o indice humano para navegacao rapida.

## Indice

| Modulo | Evidencia | Status |
|--------|-----------|--------|
| [Commons](commons/README.md) | Validacao compartilhada de CPF/CNPJ | Indireta |
| [Recebiveis de Cartao](recebiveis-cartao/README.md) | Res. 4.734, Carta/Circular 3.952, Circ. 4.016, Res. BCB 264 | Direta |
| [PIX](pix/README.md) | Res. BCB 1, 80, 142, 191, 316 | Direta, com itens a revisar |
| [Open Finance](open-finance/README.md) | Res. BCB 32, 57, 97, 316, IN 266 | Direta, com itens a revisar |
| [Arranjos de Pagamento](arranjos-pagamento/README.md) | Res. CMN 4.282, Lei 12.865, Res. BCB 150, 195 | Direta |
| [PLD/FT](pld-ft/README.md) | Lei 9.613, Circular 3.978, Res. BCB 277, Res. COAF 36 | Direta |
| [Câmbio](cambio/README.md) | Res. BCB 277, Circular 3.691 | Direta |
| [Crédito](credito/README.md) | Res. CMN 4.558, 4.966, Circular 3.953, Doc 3040 | Direta |
| [SPB](spb/README.md) | Lei 10.214 | Direta |
| [DREX](drex/README.md) | Drex / Real Digital | Direta, sem norma final consolidada |

## Convencao de cache

- `YYYY-MM-DD_<tipo>_<numero>_<slug>.pdf`
- `YYYY-MM-DD_<tipo>_<numero>_<slug>.md` para notas auxiliares
- `YYYY-MM-DD_<tipo>_<numero>_<slug>.txt` para OCR, quando necessario

Exemplos de nome de cache:

- `2019-07-10_circular_3953_ipoc.pdf`
- `2020-08-12_resolucao_1_pix.pdf`
- `scr_doc3040_instrucoes_de_preenchimento.pdf`

## Metadados minimos por evidencia

Cada `README.md` de modulo deve registrar:

- norma ou documento base;
- data de publicacao;
- tipo da evidencia (resolucao, circular, instrucao, leiaute, FAQ);
- nome logico do arquivo em cache;
- fonte oficial;
- classes ou regras impactadas;
- observacoes de vigencia e eventual revogacao.

## Regras de governanca

- nao implementar regra nova sem referencia documental;
- nao substituir PDF oficial por captura de tela ou anotacao informal;
- quando a norma mudar, adicionar novo PDF em vez de sobrescrever o antigo;
- manter o historico para auditoria e comparacao de versoes;
- no caso de normas sem consolidacao final, usar o documento mais recente e registrar a pendencia;
- quando a documentacao do modulo estiver desatualizada, marcar a entrada no manifest como `needs_review`.
