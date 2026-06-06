# Commons

## Papel da evidencia

Este modulo nao possui norma propria. Ele centraliza validacao compartilhada de CPF/CNPJ
usada por outros dominios regulados.

## Estrategia

- nao manter PDFs proprios;
- registrar a evidencia nas pastas dos modulos que consomem `CpfCnpjValidator`;
- quando houver auditoria, vincular a validacao compartilhada aos modulos de recebiveis,
  PIX, credito e PLD/FT.

## Fontes indiretas

- Recebiveis de Cartao
- PIX
- Open Finance
- PLD/FT
- Credito

