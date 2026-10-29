# ADR 001: Geracao de ebooks via mensageria assincrona

## Status:

Em proposta

## Contexto:

O Cotubify trouxe um aumento massivo de trafego

Quando um autor pública a API principal faz uma chamada HTTP sincrona para o Servico Gerador de Ebooks e fica aguardando
a geração de PDF e EPUB. Com esse processo eh intensivo em CPU, levando em media 20 segundos, as threads da API principal
ficam bloqueadas. No Servico Gerador o uso de CPU e memoria durante a geracao de ebooks eh intensa.

Resultando em lentidao e timeouts na plataforma, devido ao uso intensivo de memoria e cpu.

## Opcoes consideradas:

1. **Manter sincrono e ter escalabilidade horizontal**: adicionar mais servidores. Apenas adia o gargalo, threads
continuariam a ser desperdicadas aguardando o processamento dos ebooks.
2.  **Mensageria com Fila Assincrona**: A API Principal atua como um publisher de um comando de geracao de ebooks
    ('GeradorEbookCommand') para uma fila do nosso Message Broker. A API principal ja responde para o front-end 
('HTTP 202 Accepted'). O serviço gerador passa a atuar como um Consumer/Worker, puxando as filas das mensagens no
proprio ritmo

## Decisao

Decidimos pela opcao 2 (Mensageria com Fila Assincrona)

Sera introduzida um Message Broker para gerenciar as filas de comandos de geracao de ebooks na nossa infraestrutura.

O Message Broker escolhido sera o AWS SQS (Simple Queue Service), alinhado com o favorecimento da plataforma AWS.

## Consequencias:

### Positivas
    * Alta disponibilidade e vazao (throughput): A API Principal nao fica bloqueada e pode responder rapidamente 
para o front-end. Libera a thread imediadamente para outras requisicoes.
    * Escalabilidade horizontal: Se a fila aumentar demais, podemos alocar mais instancias do Gerador.
    * Resiliencia em picos de carga: Se 1000 autores publicarem em simultaneidade, as mensagens irao para as filas.
O Gerador ira consumindo aos poucos, sem ficar sobrecarregado.
    

### Negativas
    * Complexidade na infraestrutura: provisionar, manter e configurar o Message Broker.
    * Mudança da UX: a experiencia do usuario passa a ser assincrone ("em processamento"), impacto mínimo.
    * Aumento de latencia:
