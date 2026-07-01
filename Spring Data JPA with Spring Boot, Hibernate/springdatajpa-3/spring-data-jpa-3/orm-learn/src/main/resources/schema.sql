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

-- ============================================================
-- Quiz application schema (springdata3.pdf Hands-on 3)
-- Mirrors the schema normally generated from quiz.mwb via
-- MySQL Workbench's "Forward Engineer SQL CREATE Script".
-- ============================================================

-- ── user ──────────────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS `user` (
  `us_id`    INT          NOT NULL AUTO_INCREMENT,
  `us_name`  VARCHAR(45),
  `us_email` VARCHAR(45),
  PRIMARY KEY (`us_id`)
);

-- ── question ──────────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS `question` (
  `qt_id`   INT           NOT NULL AUTO_INCREMENT,
  `qt_text` VARCHAR(150),
  PRIMARY KEY (`qt_id`)
);

-- ── options ───────────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS `options` (
  `op_id`    INT            NOT NULL AUTO_INCREMENT,
  `op_qt_id` INT,
  `op_score` DOUBLE,
  `op_text`  VARCHAR(100),
  PRIMARY KEY (`op_id`),
  CONSTRAINT `fk_options_question` FOREIGN KEY (`op_qt_id`) REFERENCES `question` (`qt_id`)
);

-- ── attempt ───────────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS `attempt` (
  `at_id`    INT           NOT NULL AUTO_INCREMENT,
  `at_date`  DATE,
  `at_us_id` INT,
  `at_score` DOUBLE,
  PRIMARY KEY (`at_id`),
  CONSTRAINT `fk_attempt_user` FOREIGN KEY (`at_us_id`) REFERENCES `user` (`us_id`)
);

-- ── attempt_question ─────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS `attempt_question` (
  `aq_id`    INT NOT NULL AUTO_INCREMENT,
  `aq_at_id` INT,
  `aq_qt_id` INT,
  PRIMARY KEY (`aq_id`),
  CONSTRAINT `fk_aq_attempt`  FOREIGN KEY (`aq_at_id`) REFERENCES `attempt`  (`at_id`),
  CONSTRAINT `fk_aq_question` FOREIGN KEY (`aq_qt_id`) REFERENCES `question` (`qt_id`)
);

-- ── attempt_option ────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS `attempt_option` (
  `ao_id`       INT NOT NULL AUTO_INCREMENT,
  `ao_op_id`    INT,
  `ao_aq_id`    INT,
  `ao_selected` BIT,
  PRIMARY KEY (`ao_id`),
  CONSTRAINT `fk_ao_option`          FOREIGN KEY (`ao_op_id`) REFERENCES `options`          (`op_id`),
  CONSTRAINT `fk_ao_attempt_question` FOREIGN KEY (`ao_aq_id`) REFERENCES `attempt_question` (`aq_id`)
);

-- ============================================================
-- Product table (springdata3.pdf Hands-on 6 — Criteria Query)
-- Models the "Amazon laptop search" filter scenario.
-- ============================================================
CREATE TABLE IF NOT EXISTS `product` (
  `pr_id`               INT            NOT NULL AUTO_INCREMENT,
  `pr_name`             VARCHAR(100),
  `pr_category`         VARCHAR(45),
  `pr_review`           DOUBLE,
  `pr_hard_disk_gb`     INT,
  `pr_ram_gb`           INT,
  `pr_cpu_speed_ghz`    DOUBLE,
  `pr_operating_system` VARCHAR(45),
  `pr_weight_kg`        DOUBLE,
  `pr_cpu`              VARCHAR(45),
  `pr_price`            DECIMAL(10,2),
  PRIMARY KEY (`pr_id`)
);
