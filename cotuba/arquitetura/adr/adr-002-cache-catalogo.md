#ADR 002 - Cache Catalogo em memoria para o catalogo de ebooks.

## Statys
    Em proposta
## Contexto

O Cotubify cresceu para milhões de leitores ativos. A página inicial da nossa loja, que exibe o catalogo dos
"TOP 100 E-books mais vendidos", tornou-se o *endpoint* mais solicitado de todo sistema.

Atualmente, para montar essa lista, a API-Principal faz uma consulta complexa ao Banco de Dados Relacional (envolvendo
tabelas de livros, autores e histórico de vendas com múltiplos 'JOIN's e agregacoes). Essa abordagem esta levando o uso
de CPU do banco de dados a 100% nos horários de pico. A consequencia direta eh o aumento inaceitavel da latencia para
os leitores e o risco iminente de queda do banco de dados, o que paralisaria tanto as vendas quanto o painel dos autores.

A lista de TOP 100, por natureza, não precisa ter uma consistencia forte em tempo real.
Se um livro passar para o 1 lugar, nao ha problema de quem vendeu antes,

## Opcoes Consideradas

1.  **Scale Up do Banco de Dados (Aumentar Máquina): **Comprar mum servidor de banco de dados com mais CPU e memoria.
    **Problema:* Aumento de custos, a escalabilidade vertical tem um limite fisico e nao resolve o problema raiz
    de ineficiencia (fazer a nesna conta complexa milhoes de vezes para obter o mesmo resultado).
    
2. **Materialized Views no Banco Relacional:** Pre-calcular os resultados no proprio Banco Postgresql.
    **Problema:* Embora melhore a performance da query, os milhões de acessos ainda bateriam no banco de dados principal
    (I/O de disco e conexões de rede), competindo com as transações criticas de pagamento.

3. **Introdução de Cache em memoria (Key-Value:
    **Armazenar os resultados em memoria.
    ** Utilizar um banco de dados chave-valor como Redis ou Memcached, entre a API-Principal e o Banco de Dados Relacional.
    A lista "Top 100" pronta (ex: em formato JSON) é armazenada ali com um tempo de expiracao (TTL - Time To Live)
    **Problema:* A memoria eh compartilhada entre os processos,

## Decisao
    Decidimos adotar a opcao 3.
    Escolhemos o AWS ElastiCache com Redis com mecanismo de cache.
    A API Principal adotará o padrão *Cache-Aside* para consultar o Redis.
    Ao receber uma requisição para a página inicial, ela primeiro verificará o Cache (Key-Value Store). Se o catalogo
    estiver presente no cache, ela retorna imediatamente em milissegundos. Caso contrario (Cache-Miss), ala vai até
    o Banco de Dados Relacional e monta o catalogo, armazenando-o no cache com um tempo de expiracao de 5 minutos.
    
## Resultado
    A API-Principal faz apenas uma consulta ao Redis,
    
## Consequencias
    ### Positivas
        * **Queda drastica na latencia:** 
        * **Alivio do banco de dados princial.
        * ** Escalabilidade de Leitura:** escalar as instancias do Redis.      

    ### Negativas
        * **Dados Desatualizados:** A cada 5 minutos, (Stale Data)
        * **Complexidade na infraestrutura:**

