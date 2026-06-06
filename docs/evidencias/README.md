# Evidencias Normativas

Este diretorio concentra a estrategia de rastreabilidade documental do projeto.

## Regra geral

- cada modulo possui uma pasta propria em `docs/evidencias/<modulo>/`;
- o repositório armazena apenas o indice, os links oficiais e a convencao de cache;
- os PDFs oficiais ficam em um endpoint/fonte oficial do BACEN ou em cache externo fora do git;
- cada pasta tem um `README.md` com o mapeamento entre norma, endpoint e uso no codigo;
- toda regra implementada deve apontar para pelo menos uma evidencia primaria.

## Estrutura padrao

```text
docs/evidencias/
├── README.md
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
- no caso de normas sem consolidacao final, usar o documento mais recente e registrar a pendencia.

## Status por modulo

- `commons`: nao possui norma propria; evidencia e indireta, via modulos consumidores.
- `recebiveis-cartao`: possui resolucao, circulares e atualizacao normativa.
- `pix`: possui conjunto normativo consolidado e exemplos operacionais.
- `open-finance`: possui resolucoes e instrucao tecnica.
- `arranjos-pagamento`: possui marco legal e resolucoes de vigilancia.
- `pld-ft`: possui lei, circular, resolucao e regras de comunicacao.
- `cambio`: possui resolucao e circular operacional.
- `credito`: possui resolucoes, circular do IPOC e instrucoes do Doc 3040.
- `spb`: possui lei do sistema de pagamentos.
- `drex`: ainda depende de documentos oficiais e diretrizes publicadas, sem consolidacao final.
