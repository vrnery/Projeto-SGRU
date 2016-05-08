-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema SGRU
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `SGRU` DEFAULT CHARACTER SET utf8 ;
USE `SGRU` ;

-- -----------------------------------------------------
-- Table `SGRU`.`pessoa`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `SGRU`.`Pessoa` (
  `id` INT(10) UNSIGNED NOT NULL,
  `matricula` VARCHAR(15) NOT NULL,
  `nome` VARCHAR(70) NOT NULL,
  `email` VARCHAR(100) NULL DEFAULT NULL,
  `telefone` VARCHAR(11) NULL DEFAULT NULL,
  `login` VARCHAR(30) NULL DEFAULT NULL,
  `senha` VARCHAR(32) NULL DEFAULT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `SGRU`.`operadorcaixa`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `SGRU`.`OperadorCaixa` (
  `id` INT(10) UNSIGNED NOT NULL,
  INDEX `fk_OperadorCaixa_Pessoa1_idx` (`id` ASC),
  CONSTRAINT `fk_OperadorCaixa_Pessoa1`
    FOREIGN KEY (`id`)
    REFERENCES `SGRU`.`Pessoa` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `SGRU`.`caixaru`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `SGRU`.`CaixaRU` (
  `id` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `dataAbertura` DATETIME NOT NULL,
  `valorAbertura` DECIMAL(6,2) NOT NULL DEFAULT '0.00',
  `dataFechamento` DATETIME NULL DEFAULT NULL,
  `valorFechamento` DECIMAL(6,2) NOT NULL DEFAULT '0.00',
  `idOperadorCaixa` INT(10) UNSIGNED NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_CaixaRU_OperadorCaixa1_idx` (`idOperadorCaixa` ASC),
  CONSTRAINT `fk_CaixaRU_OperadorCaixa1`
    FOREIGN KEY (`idOperadorCaixa`)
    REFERENCES `SGRU`.`OperadorCaixa` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `SGRU`.`cartao`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `SGRU`.`Cartao` (
  `id` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `dataCredito` DATETIME NOT NULL,
  `saldo` DECIMAL(6,2) NOT NULL DEFAULT '0.00',
  PRIMARY KEY (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 5
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `SGRU`.`codtipocliente`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `SGRU`.`CodTipoCliente` (
  `id` INT(10) UNSIGNED NOT NULL,
  `descricao` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `SGRU`.`cliente`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `SGRU`.`Cliente` (
  `id` INT(10) UNSIGNED NOT NULL,
  `caminhoFoto` TEXT NULL DEFAULT NULL,
  `idCartao` INT(10) UNSIGNED NOT NULL,
  `idCodTipoCliente` INT(10) UNSIGNED NOT NULL,
  INDEX `fk_Professor_Pessoa1_idx` (`id` ASC),
  INDEX `fk_professor_cartao1_idx` (`idCartao` ASC),
  INDEX `fk_cliente_codTipoCliente1_idx` (`idCodTipoCliente` ASC),
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_Professor_Pessoa1`
    FOREIGN KEY (`id`)
    REFERENCES `SGRU`.`Pessoa` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_cliente_codTipoCliente1`
    FOREIGN KEY (`idCodTipoCliente`)
    REFERENCES `SGRU`.`CodTipoCliente` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_professor_cartao1`
    FOREIGN KEY (`idCartao`)
    REFERENCES `SGRU`.`Cartao` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `SGRU`.`recarga`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `SGRU`.`Recarga` (
  `id` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `valorRecarregado` DECIMAL(6,2) NOT NULL,
  `dataCredito` DATE NOT NULL,
  `utilizado` TINYINT(1) NOT NULL DEFAULT '0',
  `idCartao` INT(10) UNSIGNED NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_Recarga_Cartao1_idx` (`idCartao` ASC),
  CONSTRAINT `fk_Recarga_Cartao1`
    FOREIGN KEY (`idCartao`)
    REFERENCES `SGRU`.`Cartao` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
AUTO_INCREMENT = 17
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `SGRU`.`ticket`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `SGRU`.`Ticket` (
  `id` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `valor` DECIMAL(6,2) NOT NULL,
  `dataCriado` DATETIME NOT NULL,
  `dataUtilizado` DATETIME NULL DEFAULT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `SGRU`.`valoralmoco`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `SGRU`.`ValorAlmoco` (
  `id` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `valorAlmoco` DECIMAL(6,2) NOT NULL,
  `dataValor` DATETIME NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 5
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `SGRU`.`vendaalmoco`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `SGRU`.`VendaAlmoco` (
  `id` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `dataVenda` DATETIME NOT NULL,
  `idCaixaRU` INT(10) UNSIGNED NOT NULL,
  `idValorAlmoco` INT(10) UNSIGNED NOT NULL,
  `idCartao` INT(10) UNSIGNED NULL DEFAULT NULL,
  `idTicket` INT(10) UNSIGNED NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_VendaAlmoco_Cartao1_idx` (`idCartao` ASC),
  INDEX `fk_VendaAlmoco_ValorAlmoco1_idx` (`idValorAlmoco` ASC),
  INDEX `fk_VendaAlmoco_Ticket1_idx` (`idTicket` ASC),
  INDEX `fk_VendaAlmoco_CaixaRU1_idx` (`idCaixaRU` ASC),
  CONSTRAINT `fk_VendaAlmoco_CaixaRU1`
    FOREIGN KEY (`idCaixaRU`)
    REFERENCES `SGRU`.`CaixaRU` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_VendaAlmoco_Cartao1`
    FOREIGN KEY (`idCartao`)
    REFERENCES `SGRU`.`Cartao` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_VendaAlmoco_Ticket1`
    FOREIGN KEY (`idTicket`)
    REFERENCES `SGRU`.`Ticket` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_VendaAlmoco_ValorAlmoco1`
    FOREIGN KEY (`idValorAlmoco`)
    REFERENCES `SGRU`.`ValorAlmoco` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

-- Cria usuário da aplicação:

CREATE USER 'USERSGRUAPP'@'LOCALHOST' IDENTIFIED BY 'APPURGSUSER';

--
-- Configura permissões do usuário da aplicação:
--

GRANT SELECT, INSERT, UPDATE, DELETE, EXECUTE, SHOW VIEW ON SGRU.* TO 'USERSGRUAPP'@'LOCALHOST'; 