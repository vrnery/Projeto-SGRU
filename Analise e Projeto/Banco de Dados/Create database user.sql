--
-- Cria banco no MySQL e o coloca em uso:
--

CREATE DATABASE BD_SGRU;

USE BD_SGRU;

--
-- Cria tabelas do banco:
--

--
-- CRIA CHAVES ESTRANGEIRAS DO BANCO:
--

--
-- Cria índices únicos:
--

--
-- Cria índices em chaves estrangeiras:
--

--
-- Cria índices de agrupamento:
--

--
-- Cria índice composto:
--

--
-- Cria usuário da aplicação:
--

CREATE USER 'USERSGRUAPP'@'LOCALHOST' IDENTIFIED BY 'APPURGSUSER';
--
-- Configura permissões do usuário da aplicação:
--

GRANT SELECT, INSERT, UPDATE, DELETE, EXECUTE, SHOW VIEW ON BD_SGRU.* TO 'USERSGRUAPP'@'LOCALHOST';