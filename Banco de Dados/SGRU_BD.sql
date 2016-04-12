-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema sgru
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema sgru
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `sgru` DEFAULT CHARACTER SET utf8 ;
USE `sgru` ;

-- -----------------------------------------------------
-- Table `sgru`.`pessoa`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `sgru`.`pessoa` (
  `id` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `nome` VARCHAR(70) NOT NULL,
  `email` VARCHAR(100) NULL DEFAULT NULL,
  `telefone` VARCHAR(11) NULL DEFAULT NULL,
  `login` VARCHAR(30) NULL DEFAULT NULL,
  `senha` VARCHAR(32) NULL DEFAULT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `sgru`.`cartao`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `sgru`.`cartao` (
  `id` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `dataExpiracao` DATE NOT NULL,
  `saldo` DECIMAL(6,2) NULL DEFAULT 0,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `sgru`.`aluno`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `sgru`.`aluno` (
  `id` INT(10) UNSIGNED NOT NULL,
  `matricula` VARCHAR(15) NOT NULL,
  `caminhoFoto` TEXT NULL DEFAULT NULL,
  `idCartao` INT(10) UNSIGNED NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_Aluno_Pessoa1_idx` (`id` ASC),
  INDEX `fk_aluno_cartao1_idx` (`idCartao` ASC),
  CONSTRAINT `fk_Aluno_Pessoa1`
    FOREIGN KEY (`id`)
    REFERENCES `sgru`.`pessoa` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_aluno_cartao1`
    FOREIGN KEY (`idCartao`)
    REFERENCES `sgru`.`cartao` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `sgru`.`operadorcaixa`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `sgru`.`operadorcaixa` (
  `id` INT(10) UNSIGNED NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_OperadorCaixa_Pessoa1_idx` (`id` ASC),
  CONSTRAINT `fk_OperadorCaixa_Pessoa1`
    FOREIGN KEY (`id`)
    REFERENCES `sgru`.`pessoa` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `sgru`.`caixaru`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `sgru`.`caixaru` (
  `id` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `dataAbertura` DATETIME NOT NULL,
  `valorAbertura` DECIMAL(6,2) NOT NULL DEFAULT '0.00',
  `valorFechamento` DECIMAL(6,2) NOT NULL DEFAULT '0.00',
  `idOperadorCaixa` INT(10) UNSIGNED NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_CaixaRU_OperadorCaixa1_idx` (`idOperadorCaixa` ASC),
  CONSTRAINT `fk_CaixaRU_OperadorCaixa1`
    FOREIGN KEY (`idOperadorCaixa`)
    REFERENCES `sgru`.`operadorcaixa` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `sgru`.`professor`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `sgru`.`professor` (
  `id` INT(10) UNSIGNED NOT NULL,
  `matricula` VARCHAR(15) NOT NULL,
  `caminhoFoto` TEXT NULL DEFAULT NULL,
  `idCartao` INT(10) UNSIGNED NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_Professor_Pessoa1_idx` (`id` ASC),
  INDEX `fk_professor_cartao1_idx` (`idCartao` ASC),
  CONSTRAINT `fk_Professor_Pessoa1`
    FOREIGN KEY (`id`)
    REFERENCES `sgru`.`pessoa` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_professor_cartao1`
    FOREIGN KEY (`idCartao`)
    REFERENCES `sgru`.`cartao` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `sgru`.`recarga`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `sgru`.`recarga` (
  `id` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `valorRecarregado` DECIMAL(6,2) NOT NULL,
  `dataCredito` DATETIME NOT NULL,
  `utilizado` TINYINT(1) NOT NULL DEFAULT '0',
  `idCartao` INT(10) UNSIGNED NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_Recarga_Cartao1_idx` (`idCartao` ASC),
  CONSTRAINT `fk_Recarga_Cartao1`
    FOREIGN KEY (`idCartao`)
    REFERENCES `sgru`.`cartao` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `sgru`.`ticket`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `sgru`.`ticket` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `valor` DECIMAL(6,2) NOT NULL,
  `dataCriado` DATETIME NOT NULL,
  `dataUtilizado` DATETIME NULL DEFAULT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `sgru`.`valoralmoco`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `sgru`.`valoralmoco` (
  `id` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `valorAlmoco` DECIMAL(6,2) NOT NULL,
  `dataValor` DATETIME NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `sgru`.`vendaalmoco`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `sgru`.`vendaalmoco` (
  `id` INT(11) NOT NULL,
  `formaPagamento` VARCHAR(15) NOT NULL,
  `idCaixaRU` INT(10) UNSIGNED NOT NULL,
  `idValorAlmoco` INT(10) UNSIGNED NOT NULL,
  `idCartao` INT(10) UNSIGNED NULL DEFAULT NULL,
  `idTicket` INT(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_VendaAlmoco_Cartao1_idx` (`idCartao` ASC),
  INDEX `fk_VendaAlmoco_ValorAlmoco1_idx` (`idValorAlmoco` ASC),
  INDEX `fk_VendaAlmoco_Ticket1_idx` (`idTicket` ASC),
  INDEX `fk_VendaAlmoco_CaixaRU1_idx` (`idCaixaRU` ASC),
  CONSTRAINT `fk_VendaAlmoco_CaixaRU1`
    FOREIGN KEY (`idCaixaRU`)
    REFERENCES `sgru`.`caixaru` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_VendaAlmoco_Cartao1`
    FOREIGN KEY (`idCartao`)
    REFERENCES `sgru`.`cartao` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_VendaAlmoco_Ticket1`
    FOREIGN KEY (`idTicket`)
    REFERENCES `sgru`.`ticket` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_VendaAlmoco_ValorAlmoco1`
    FOREIGN KEY (`idValorAlmoco`)
    REFERENCES `sgru`.`valoralmoco` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

-- Cria usuário da aplicação:

CREATE USER IF NOT EXISTS 'USERSGRUAPP'@'LOCALHOST' IDENTIFIED BY 'APPURGSUSER';

--
-- Configura permissões do usuário da aplicação:
--

GRANT SELECT, INSERT, UPDATE, DELETE, EXECUTE, SHOW VIEW ON SGRU.* TO 'USERSGRUAPP'@'LOCALHOST'; 