-- ============================================================
-- sample-data.sql  –  Sample seed data for testing.
-- Run AFTER schema.sql.
-- Usage:  mysql -u root -p ormlearn < sample-data.sql
--
-- NOTE: stock data is large (1 year x 3 companies) and is NOT
-- included here. See stock-data-instructions.txt /
-- generate-stock-data.sql for how to load it.
-- ============================================================

USE `ormlearn`;

-- ── country (subset relevant to the hands-on examples + a broader sample) ─────
INSERT INTO `country` (`co_code`, `co_name`) VALUES
('BV', 'Bouvet Island'),
('DJ', 'Djibouti'),
('GP', 'Guadeloupe'),
('GS', 'South Georgia and the South Sandwich Islands'),
('LU', 'Luxembourg'),
('SS', 'South Sudan'),
('TF', 'French Southern Territories'),
('UM', 'United States Minor Outlying Islands'),
('ZA', 'South Africa'),
('ZM', 'Zambia'),
('ZW', 'Zimbabwe'),
('US', 'United States of America'),
('IN', 'India'),
('GB', 'United Kingdom'),
('FR', 'France'),
('DE', 'Germany'),
('JP', 'Japan'),
('CN', 'China'),
('AU', 'Australia'),
('CA', 'Canada'),
('BR', 'Brazil'),
('MX', 'Mexico'),
('IT', 'Italy'),
('ES', 'Spain'),
('NL', 'Netherlands'),
('RU', 'Russia'),
('ZA', 'South Africa');

-- ── department ──────────────────────────────────────────────────────────────
INSERT INTO `department` (`dp_id`, `dp_name`) VALUES
(1, 'Engineering'),
(2, 'Human Resources'),
(3, 'Finance'),
(4, 'Marketing');

-- ── skill ───────────────────────────────────────────────────────────────────
INSERT INTO `skill` (`sk_id`, `sk_name`) VALUES
(1, 'Java'),
(2, 'Spring Boot'),
(3, 'SQL'),
(4, 'Python'),
(5, 'Project Management'),
(6, 'Communication');

-- ── employee (department id = 1 has 3 employees, good for HO-5 testing) ───────
INSERT INTO `employee` (`em_id`, `em_name`, `em_salary`, `em_permanent`, `em_date_of_birth`, `em_dp_id`) VALUES
(1, 'Alice Johnson', 85000.00, TRUE,  '1990-04-12', 1),
(2, 'Bob Smith',     72000.00, TRUE,  '1988-11-23', 1),
(3, 'Carol White',   65000.00, FALSE, '1995-06-30', 1),
(4, 'David Brown',   58000.00, TRUE,  '1992-02-17', 2),
(5, 'Eva Green',     91000.00, TRUE,  '1985-09-05', 3);

-- ── employee_skill (Alice has Java + Spring Boot; skill id 3 = SQL is free
--    for employee 1 to demonstrate testAddSkillToEmployee()) ───────────────────
INSERT INTO `employee_skill` (`es_em_id`, `es_sk_id`) VALUES
(1, 1),  -- Alice - Java
(1, 2),  -- Alice - Spring Boot
(2, 1),  -- Bob   - Java
(2, 3),  -- Bob   - SQL
(3, 4),  -- Carol - Python
(4, 5),  -- David - Project Management
(5, 6);  -- Eva   - Communication
