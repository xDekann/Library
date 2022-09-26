-- MySQL Script generated by MySQL Workbench
-- Mon Sep 26 00:39:35 2022
-- Model: New Model    Version: 1.0
-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema bookstore
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema bookstore
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `bookstore` DEFAULT CHARACTER SET utf8mb3 ;
USE `bookstore` ;

-- -----------------------------------------------------
-- Table `bookstore`.`author`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bookstore`.`author` (
  `authorid` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `surname` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`authorid`))
ENGINE = InnoDB
AUTO_INCREMENT = 20
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `bookstore`.`book`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bookstore`.`book` (
  `bookid` INT NOT NULL AUTO_INCREMENT,
  `isbn` BIGINT NOT NULL,
  `title` VARCHAR(45) NOT NULL,
  `publishing_house` VARCHAR(45) NOT NULL,
  `year_of_publishment` INT NOT NULL,
  PRIMARY KEY (`bookid`))
ENGINE = InnoDB
AUTO_INCREMENT = 39
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `bookstore`.`author_connector`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bookstore`.`author_connector` (
  `author_idC` INT NOT NULL,
  `book_idC` INT NOT NULL,
  INDEX `fk_author_con_idx` (`author_idC` ASC) VISIBLE,
  INDEX `fk_book_con_idx` (`book_idC` ASC) VISIBLE,
  CONSTRAINT `fk_author_con`
    FOREIGN KEY (`author_idC`)
    REFERENCES `bookstore`.`author` (`authorid`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_book_con`
    FOREIGN KEY (`book_idC`)
    REFERENCES `bookstore`.`book` (`bookid`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `bookstore`.`authorities`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bookstore`.`authorities` (
  `idRole` INT NOT NULL AUTO_INCREMENT,
  `authority` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`idRole`))
ENGINE = InnoDB
AUTO_INCREMENT = 4
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `bookstore`.`book_copy`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bookstore`.`book_copy` (
  `copyid` INT NOT NULL AUTO_INCREMENT,
  `isbn` BIGINT NOT NULL,
  `title` VARCHAR(45) NOT NULL,
  `status` TINYINT(1) NOT NULL,
  `fk_book` INT NOT NULL,
  PRIMARY KEY (`copyid`),
  INDEX `fk_book_copy_idx` (`fk_book` ASC) VISIBLE,
  CONSTRAINT `fk_book_copy`
    FOREIGN KEY (`fk_book`)
    REFERENCES `bookstore`.`book` (`bookid`))
ENGINE = InnoDB
AUTO_INCREMENT = 23
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `bookstore`.`client`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bookstore`.`client` (
  `clientid` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `surname` VARCHAR(45) NOT NULL,
  `email` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`clientid`))
ENGINE = InnoDB
AUTO_INCREMENT = 7
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `bookstore`.`book_rent`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bookstore`.`book_rent` (
  `rentid` INT NOT NULL AUTO_INCREMENT,
  `clientidR` INT NOT NULL,
  `copyidR` INT NOT NULL,
  `title` VARCHAR(45) NOT NULL,
  `date_of_rent` DATE NOT NULL,
  `end_date_of_rent` DATE NULL DEFAULT NULL,
  `penalty` TINYINT(1) NOT NULL,
  PRIMARY KEY (`rentid`),
  INDEX `fk_client_rent_idx` (`clientidR` ASC) VISIBLE,
  INDEX `fk_copy_rent_idx` (`copyidR` ASC) VISIBLE,
  CONSTRAINT `fk_client_rent`
    FOREIGN KEY (`clientidR`)
    REFERENCES `bookstore`.`client` (`clientid`),
  CONSTRAINT `fk_copy_rent`
    FOREIGN KEY (`copyidR`)
    REFERENCES `bookstore`.`book_copy` (`copyid`))
ENGINE = InnoDB
AUTO_INCREMENT = 5
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `bookstore`.`users`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bookstore`.`users` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(50) NOT NULL,
  `password` VARCHAR(60) NOT NULL,
  `enabled` TINYINT(1) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 25
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `bookstore`.`client_connector`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bookstore`.`client_connector` (
  `user_id_springC` INT NOT NULL,
  `user_id_userC` INT NOT NULL,
  INDEX `fk_users_client_con_idx` (`user_id_springC` ASC) VISIBLE,
  INDEX `fk_client_client_con_idx` (`user_id_userC` ASC) VISIBLE,
  CONSTRAINT `fk_client_client_con`
    FOREIGN KEY (`user_id_userC`)
    REFERENCES `bookstore`.`client` (`clientid`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_users_client_con`
    FOREIGN KEY (`user_id_springC`)
    REFERENCES `bookstore`.`users` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `bookstore`.`employee`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bookstore`.`employee` (
  `emplid` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `surname` VARCHAR(45) NOT NULL,
  `email` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`emplid`))
ENGINE = InnoDB
AUTO_INCREMENT = 7
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `bookstore`.`employee_connector`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bookstore`.`employee_connector` (
  `user_id_spring` INT NOT NULL,
  `user_id_user` INT NOT NULL,
  INDEX `user_id_spring_idx` (`user_id_spring` ASC) VISIBLE,
  INDEX `used_id_user_idx` (`user_id_user` ASC) VISIBLE,
  CONSTRAINT `user_id_empl`
    FOREIGN KEY (`user_id_user`)
    REFERENCES `bookstore`.`employee` (`emplid`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `user_id_spring`
    FOREIGN KEY (`user_id_spring`)
    REFERENCES `bookstore`.`users` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `bookstore`.`user_auth`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bookstore`.`user_auth` (
  `user_id` INT NOT NULL,
  `role_id` INT NOT NULL,
  INDEX `user_fk_idx` (`user_id` ASC) VISIBLE,
  INDEX `role_fk_idx` (`role_id` ASC) VISIBLE,
  CONSTRAINT `role_fk`
    FOREIGN KEY (`role_id`)
    REFERENCES `bookstore`.`authorities` (`idRole`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `user_fk`
    FOREIGN KEY (`user_id`)
    REFERENCES `bookstore`.`users` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb3;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
