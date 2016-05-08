INSERT INTO `SGRU`.`Ticket`
(`valor`, `dataCriado`)
VALUES
((SELECT valorAlmoco FROM SGRU.ValorAlmoco ORDER BY dataValor DESC LIMIT 1), now());