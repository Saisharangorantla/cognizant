-- ============================================================
-- schema.sql  –  Run this once to create the ormlearn schema
--               and all required tables.
-- Usage:  mysql -u root -p < schema.sql
-- ============================================================

CREATE DATABASE IF NOT EXISTS `ormlearn`
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE `ormlearn`;

-- ── country ──────────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS `country` (
  `co_code` VARCHAR(2)  NOT NULL,
  `co_name` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`co_code`)
);

-- ── stock ─────────────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS `stock` (
  `st_id`     INT            NOT NULL AUTO_INCREMENT,
  `st_code`   VARCHAR(10),
  `st_date`   DATE,
  `st_open`   NUMERIC(10,2),
  `st_close`  NUMERIC(10,2),
  `st_volume` NUMERIC,
  PRIMARY KEY (`st_id`)
);

-- ── department ────────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS `department` (
  `dp_id`   INT          NOT NULL AUTO_INCREMENT,
  `dp_name` VARCHAR(45),
  PRIMARY KEY (`dp_id`)
);

-- ── skill ─────────────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS `skill` (
  `sk_id`   INT          NOT NULL AUTO_INCREMENT,
  `sk_name` VARCHAR(45),
  PRIMARY KEY (`sk_id`)
);

-- ── employee ──────────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS `employee` (
  `em_id`           INT            NOT NULL AUTO_INCREMENT,
  `em_name`         VARCHAR(45),
  `em_salary`       DECIMAL(10,2),
  `em_permanent`    BOOLEAN,
  `em_date_of_birth` DATE,
  `em_dp_id`        INT,
  PRIMARY KEY (`em_id`),
  CONSTRAINT `fk_employee_department`
    FOREIGN KEY (`em_dp_id`) REFERENCES `department` (`dp_id`)
);

-- ── employee_skill (join table) ───────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS `employee_skill` (
  `es_id`    INT NOT NULL AUTO_INCREMENT,
  `es_em_id` INT NOT NULL,
  `es_sk_id` INT NOT NULL,
  PRIMARY KEY (`es_id`),
  CONSTRAINT `fk_es_employee` FOREIGN KEY (`es_em_id`) REFERENCES `employee` (`em_id`),
  CONSTRAINT `fk_es_skill`    FOREIGN KEY (`es_sk_id`) REFERENCES `skill`    (`sk_id`)
);
