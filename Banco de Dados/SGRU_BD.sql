-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

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
  `matricula` VARCHAR(15) NOT NULL,
  `nome` VARCHAR(70) NOT NULL,
  `email` VARCHAR(100) NULL DEFAULT NULL,
  `telefone` VARCHAR(11) NULL DEFAULT NULL,
  `login` VARCHAR(30) NULL DEFAULT NULL,
  `senha` VARCHAR(32) NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `matricula_UNIQUE` (`matricula` ASC),
  UNIQUE INDEX `login_UNIQUE` (`login` ASC))
ENGINE = InnoDB
AUTO_INCREMENT = 9
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `sgru`.`tipofuncionario`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `sgru`.`tipofuncionario` (
  `id` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `descricao` VARCHAR(45) NOT NULL,
  `codigo` VARCHAR(3) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `codigo_UNIQUE` (`codigo` ASC))
ENGINE = InnoDB
AUTO_INCREMENT = 4
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `sgru`.`funcionario`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `sgru`.`funcionario` (
  `id` INT(10) UNSIGNED NOT NULL,
  `idTipoFuncionario` INT(10) UNSIGNED NOT NULL,
  INDEX `fk_Funcionario_Pessoa1_idx` (`id` ASC),
  INDEX `fk_funcionario_tipoFuncionario1_idx` (`idTipoFuncionario` ASC),
  CONSTRAINT `fk_Funcionario_Pessoa1`
    FOREIGN KEY (`id`)
    REFERENCES `sgru`.`pessoa` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_funcionario_tipoFuncionario1`
    FOREIGN KEY (`idTipoFuncionario`)
    REFERENCES `sgru`.`tipofuncionario` (`id`)
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
  `dataFechamento` DATETIME NULL DEFAULT NULL,
  `valorFechamento` DECIMAL(6,2) NOT NULL DEFAULT '0.00',
  `idFuncionario` INT(10) UNSIGNED NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_CaixaRU_Funcionario1_idx` (`idFuncionario` ASC),
  CONSTRAINT `fk_CaixaRU_Funcionario1`
    FOREIGN KEY (`idFuncionario`)
    REFERENCES `sgru`.`funcionario` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `sgru`.`cartao`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `sgru`.`cartao` (
  `id` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `dataCredito` DATETIME NOT NULL,
  `saldo` DECIMAL(6,2) NOT NULL DEFAULT '0.00',
  `saldoUltimaRecarga` DECIMAL(6,2) NOT NULL DEFAULT 0.00,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 6
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `sgru`.`tipocliente`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `sgru`.`tipocliente` (
  `id` INT(10) UNSIGNED NOT NULL,
  `descricao` VARCHAR(45) NOT NULL,
  `codigo` VARCHAR(3) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `codigo_UNIQUE` (`codigo` ASC))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `sgru`.`cliente`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `sgru`.`cliente` (
  `id` INT(10) UNSIGNED NOT NULL,
  `caminhoFoto` TEXT NULL DEFAULT NULL,
  `idCartao` INT(10) UNSIGNED NOT NULL,
  `idTipoCliente` INT(10) UNSIGNED NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_Professor_Pessoa1_idx` (`id` ASC),
  INDEX `fk_professor_cartao1_idx` (`idCartao` ASC),
  INDEX `fk_cliente_codTipoCliente1_idx` (`idTipoCliente` ASC),
  CONSTRAINT `fk_Professor_Pessoa1`
    FOREIGN KEY (`id`)
    REFERENCES `sgru`.`pessoa` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_cliente_codTipoCliente1`
    FOREIGN KEY (`idTipoCliente`)
    REFERENCES `sgru`.`tipocliente` (`id`)
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
AUTO_INCREMENT = 17
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `sgru`.`ticket`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `sgru`.`ticket` (
  `id` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
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
AUTO_INCREMENT = 5
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `sgru`.`vendaalmoco`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `sgru`.`vendaalmoco` (
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


-- -----------------------------------------------------
-- Table `sgru`.`vendaticketsrecargas`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `sgru`.`vendaticketsrecargas` (
  `id` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `dataVenda` DATETIME NOT NULL,
  `idCaixaRU` INT(10) UNSIGNED NULL,
  `idValorAlmoco` INT(10) UNSIGNED NULL DEFAULT NULL,
  `idTicket` INT(10) UNSIGNED NULL DEFAULT NULL,
  `idRecarga` INT(10) UNSIGNED NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_VendaTicketsRecargas_valoralmoco1_idx` (`idValorAlmoco` ASC),
  INDEX `fk_VendaTicketsRecargas_ticket1_idx` (`idTicket` ASC),
  INDEX `fk_VendaTicketsRecargas_caixaru1_idx` (`idCaixaRU` ASC),
  INDEX `fk_VendaTicketsRecargas_recarga1_idx` (`idRecarga` ASC),
  CONSTRAINT `fk_VendaTicketsRecargas_caixaru1`
    FOREIGN KEY (`idCaixaRU`)
    REFERENCES `sgru`.`caixaru` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_VendaTicketsRecargas_recarga1`
    FOREIGN KEY (`idRecarga`)
    REFERENCES `sgru`.`recarga` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_VendaTicketsRecargas_ticket1`
    FOREIGN KEY (`idTicket`)
    REFERENCES `sgru`.`ticket` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_VendaTicketsRecargas_valoralmoco1`
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

CREATE USER 'USERSGRUAPP'@'LOCALHOST' IDENTIFIED BY 'APPURGSUSER';

--
-- Configura permissões do usuário da aplicação:
--

GRANT SELECT, INSERT, UPDATE, DELETE, EXECUTE, SHOW VIEW ON SGRU.* TO 'USERSGRUAPP'@'LOCALHOST';