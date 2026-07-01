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

-- ============================================================
-- Quiz application sample data (springdata3.pdf Hands-on 3)
-- Reproduces the exact attempt walked through in the PDF's
-- expected-output example (HTML quiz, user attempt id 1).
-- ============================================================

INSERT INTO `user` (`us_id`, `us_name`, `us_email`) VALUES
(1, 'Priya Sharma', 'priya.sharma@example.com'),
(2, 'Raj Mehta', 'raj.mehta@example.com');

INSERT INTO `question` (`qt_id`, `qt_text`) VALUES
(1, 'What is the extension of the hyper text markup language file?'),
(2, 'What is the maximum level of heading tag can be used in a HTML page?'),
(3, 'The HTML document itself begins with <html> and ends </html>. State True of False'),
(4, 'Choose the right option to store text value value in a variable');

INSERT INTO `options` (`op_id`, `op_qt_id`, `op_score`, `op_text`) VALUES
-- Question 1 options
(1, 1, 0.0, '.xhtm'),
(2, 1, 0.0, '.ht'),
(3, 1, 1.0, '.html'),
(4, 1, 0.0, '.htmx'),
-- Question 2 options
(5, 2, 0.0, '5'),
(6, 2, 1.0, '3'),
(7, 2, 0.0, '4'),
(8, 2, 0.0, '6'),
-- Question 3 options
(9,  3, 0.0, 'false'),
(10, 3, 1.0, 'true'),
-- Question 4 options
(11, 4, 0.5, "'John'"),
(12, 4, 0.0, 'John'),
(13, 4, 0.5, '"John"'),
(14, 4, 0.0, '/John/');

INSERT INTO `attempt` (`at_id`, `at_date`, `at_us_id`, `at_score`) VALUES
(1, '2026-05-10', 1, 3.5);

INSERT INTO `attempt_question` (`aq_id`, `aq_at_id`, `aq_qt_id`) VALUES
(1, 1, 1),
(2, 1, 2),
(3, 1, 3),
(4, 1, 4);

-- ao_selected: BIT(1) — 1 = selected by the user, 0 = not selected.
-- Mirrors the PDF's expected output:
--   Q1 -> selected ".html" (correct)
--   Q2 -> selected "3" but it was wrong per displayed output (4th row marked correct=1.0 but selected=false;
--         row 1 marked selected=true with score 0.0 — i.e. user picked "5" which was incorrect)
--   Q3 -> selected "true" (correct)
--   Q4 -> selected "'John'" (one of two partially-correct options)
INSERT INTO `attempt_option` (`ao_id`, `ao_op_id`, `ao_aq_id`, `ao_selected`) VALUES
-- Q1 (aq_id=1): user selected option 3 (.html, correct)
(1, 1, 1, 0),
(2, 2, 1, 0),
(3, 3, 1, 1),
(4, 4, 1, 0),
-- Q2 (aq_id=2): user selected option 5 ("5", incorrect — correct answer was "3")
(5, 5, 2, 1),
(6, 6, 2, 0),
(7, 7, 2, 0),
(8, 8, 2, 0),
-- Q3 (aq_id=3): user selected option 10 (true, correct)
(9,  9,  3, 0),
(10, 10, 3, 1),
-- Q4 (aq_id=4): user selected option 11 ('John', partially correct)
(11, 11, 4, 1),
(12, 12, 4, 0),
(13, 13, 4, 0),
(14, 14, 4, 0);

-- ============================================================
-- Product sample data (springdata3.pdf Hands-on 6 — Criteria Query)
-- A handful of laptops with varying specs to exercise the
-- dynamic filter combinations described in the PDF.
-- ============================================================

INSERT INTO `product` (`pr_id`, `pr_name`, `pr_category`, `pr_review`, `pr_hard_disk_gb`, `pr_ram_gb`, `pr_cpu_speed_ghz`, `pr_operating_system`, `pr_weight_kg`, `pr_cpu`, `pr_price`) VALUES
(1, 'UltraBook Pro 14 Laptop',   'laptop', 4.5, 512, 16, 3.2, 'Windows 11', 1.3, 'Intel i7', 1199.00),
(2, 'EcoBook Air Laptop',       'laptop', 4.0, 256,  8, 2.4, 'macOS',      1.0, 'Apple M2', 999.00),
(3, 'PowerStation Gaming Laptop','laptop', 4.7, 1024, 32, 3.8, 'Windows 11', 2.4, 'Intel i9', 2199.00),
(4, 'ValueBook 15 Laptop',      'laptop', 3.5, 256,  8, 2.0, 'Windows 11', 1.8, 'AMD Ryzen 5', 549.00),
(5, 'DevBook Linux Laptop',     'laptop', 4.2, 512, 16, 2.9, 'Linux',      1.5, 'Intel i7', 1099.00),
(6, 'BudgetBook 11 Laptop',     'laptop', 3.0, 128,  4, 1.6, 'Windows 11', 1.2, 'Intel i3', 349.00);
