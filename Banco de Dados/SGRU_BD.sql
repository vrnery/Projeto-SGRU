-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema SGRU
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema SGRU
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `SGRU` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci ;
USE `SGRU` ;

-- -----------------------------------------------------
-- Table `SGRU`.`Cartao`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `SGRU`.`Cartao` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `dataExpiracao` DATE NOT NULL,
  `saldo` DECIMAL(6,2) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `SGRU`.`Pessoa`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `SGRU`.`Pessoa` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `nome` VARCHAR(70) NOT NULL,
  `email` VARCHAR(100) NULL,
  `telefone` VARCHAR(11) NULL,
  `login` VARCHAR(30) NULL,
  `senha` VARCHAR(32) NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `SGRU`.`Aluno`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `SGRU`.`Aluno` (
  `id` INT UNSIGNED NOT NULL,
  `matricula` VARCHAR(15) NOT NULL,
  `caminhoFoto` TEXT NULL,
  `idCartao` INT UNSIGNED NOT NULL,
  INDEX `fk_Aluno_Cartao_idx` (`idCartao` ASC),
  INDEX `fk_Aluno_Pessoa1_idx` (`id` ASC),
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_Aluno_Cartao`
    FOREIGN KEY (`idCartao`)
    REFERENCES `SGRU`.`Cartao` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Aluno_Pessoa1`
    FOREIGN KEY (`id`)
    REFERENCES `SGRU`.`Pessoa` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `SGRU`.`Recarga`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `SGRU`.`Recarga` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `valorRecarregado` DECIMAL(6,2) NOT NULL,
  `dataCredito` DATETIME NOT NULL,
  `utilizado` TINYINT(1) NOT NULL DEFAULT 0,
  `idCartao` INT UNSIGNED NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_Recarga_Cartao1_idx` (`idCartao` ASC),
  CONSTRAINT `fk_Recarga_Cartao1`
    FOREIGN KEY (`idCartao`)
    REFERENCES `SGRU`.`Cartao` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `SGRU`.`OperadorCaixa`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `SGRU`.`OperadorCaixa` (
  `idPessoa` INT UNSIGNED NOT NULL,
  INDEX `fk_OperadorCaixa_Pessoa1_idx` (`idPessoa` ASC),
  PRIMARY KEY (`idPessoa`),
  CONSTRAINT `fk_OperadorCaixa_Pessoa1`
    FOREIGN KEY (`idPessoa`)
    REFERENCES `SGRU`.`Pessoa` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `SGRU`.`CaixaRU`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `SGRU`.`CaixaRU` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `dataAbertura` DATETIME NOT NULL,
  `valorAbertura` DECIMAL(6,2) NOT NULL DEFAULT 0,
  `valorFechamento` DECIMAL(6,2) NOT NULL DEFAULT 0,
  `idPessoa` INT UNSIGNED NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_CaixaRU_OperadorCaixa1_idx` (`idPessoa` ASC),
  CONSTRAINT `fk_CaixaRU_OperadorCaixa1`
    FOREIGN KEY (`idPessoa`)
    REFERENCES `SGRU`.`OperadorCaixa` (`idPessoa`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `SGRU`.`ValorAlmoco`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `SGRU`.`ValorAlmoco` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `valorAlmoco` DECIMAL(6,2) NOT NULL,
  `dataValor` DATETIME NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `SGRU`.`Ticket`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `SGRU`.`Ticket` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `valor` DECIMAL(6,2) NOT NULL,
  `dataCriado` DATETIME NOT NULL,
  `dataUtilizado` DATETIME NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `SGRU`.`VendaAlmoco`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `SGRU`.`VendaAlmoco` (
  `id` INT NOT NULL,
  `formaPagamento` VARCHAR(15) NOT NULL,
  `idCartao` INT UNSIGNED NULL,
  `idValorAlmoco` INT UNSIGNED NOT NULL,
  `idTicket` INT NULL,
  `idCaixaRU` INT UNSIGNED NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_VendaAlmoco_Cartao1_idx` (`idCartao` ASC),
  INDEX `fk_VendaAlmoco_ValorAlmoco1_idx` (`idValorAlmoco` ASC),
  INDEX `fk_VendaAlmoco_Ticket1_idx` (`idTicket` ASC),
  INDEX `fk_VendaAlmoco_CaixaRU1_idx` (`idCaixaRU` ASC),
  CONSTRAINT `fk_VendaAlmoco_Cartao1`
    FOREIGN KEY (`idCartao`)
    REFERENCES `SGRU`.`Cartao` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_VendaAlmoco_ValorAlmoco1`
    FOREIGN KEY (`idValorAlmoco`)
    REFERENCES `SGRU`.`ValorAlmoco` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_VendaAlmoco_Ticket1`
    FOREIGN KEY (`idTicket`)
    REFERENCES `SGRU`.`Ticket` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_VendaAlmoco_CaixaRU1`
    FOREIGN KEY (`idCaixaRU`)
    REFERENCES `SGRU`.`CaixaRU` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `SGRU`.`Professor`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `SGRU`.`Professor` (
  `id` INT UNSIGNED NOT NULL,
  `matricula` VARCHAR(15) NOT NULL,
  `caminhoFoto` TEXT NULL,
  `idCartao` INT UNSIGNED NOT NULL,
  INDEX `fk_Professor_Cartao1_idx` (`idCartao` ASC),
  INDEX `fk_Professor_Pessoa1_idx` (`id` ASC),
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_Professor_Cartao1`
    FOREIGN KEY (`idCartao`)
    REFERENCES `SGRU`.`Cartao` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Professor_Pessoa1`
    FOREIGN KEY (`id`)
    REFERENCES `SGRU`.`Pessoa` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
