-- ==========================================
-- EXERCISE 4 : FUNCTIONS
-- ==========================================

SET SERVEROUTPUT ON;

-- ==========================================
-- CREATE TABLES
-- ==========================================

CREATE TABLE Customers (
    CustomerID NUMBER PRIMARY KEY,
    CustomerName VARCHAR2(50),
    DateOfBirth DATE
);

CREATE TABLE Accounts (
    AccountID NUMBER PRIMARY KEY,
    CustomerName VARCHAR2(50),
    Balance NUMBER(10,2)
);

-- ==========================================
-- INSERT SAMPLE DATA
-- ==========================================

INSERT INTO Customers
VALUES (101, 'Ram', TO_DATE('15-05-1990','DD-MM-YYYY'));

INSERT INTO Customers
VALUES (102, 'Krishna', TO_DATE('10-08-1985','DD-MM-YYYY'));

INSERT INTO Accounts
VALUES (201, 'Ram', 15000);

INSERT INTO Accounts
VALUES (202, 'Krishna', 5000);

COMMIT;

-- ==========================================
-- SCENARIO 1
-- FUNCTION : CalculateAge
-- ==========================================

CREATE OR REPLACE FUNCTION CalculateAge(
    p_dob DATE
)
RETURN NUMBER
IS
    v_age NUMBER;
BEGIN
    v_age := FLOOR(MONTHS_BETWEEN(SYSDATE, p_dob) / 12);
    RETURN v_age;
END;
/

-- Test Function

DECLARE
    v_age NUMBER;
BEGIN
    v_age := CalculateAge(
        TO_DATE('15-05-1990','DD-MM-YYYY')
    );

    DBMS_OUTPUT.PUT_LINE(
        'Age = ' || v_age
    );
END;
/

-- ==========================================
-- SCENARIO 2
-- FUNCTION : CalculateMonthlyInstallment
-- ==========================================

CREATE OR REPLACE FUNCTION CalculateMonthlyInstallment(
    p_loanAmount NUMBER,
    p_interestRate NUMBER,
    p_years NUMBER
)
RETURN NUMBER
IS
    v_monthlyInstallment NUMBER;
BEGIN
    v_monthlyInstallment :=
        (p_loanAmount +
        (p_loanAmount * p_interestRate * p_years / 100))
        / (p_years * 12);

    RETURN ROUND(v_monthlyInstallment,2);
END;
/

-- Test Function

DECLARE
    v_installment NUMBER;
BEGIN
    v_installment :=
        CalculateMonthlyInstallment(
            100000,
            10,
            5
        );

    DBMS_OUTPUT.PUT_LINE(
        'Monthly Installment = ' ||
        v_installment
    );
END;
/

-- ==========================================
-- SCENARIO 3
-- FUNCTION : HasSufficientBalance
-- ==========================================

CREATE OR REPLACE FUNCTION HasSufficientBalance(
    p_accountId NUMBER,
    p_amount NUMBER
)
RETURN BOOLEAN
IS
    v_balance NUMBER;
BEGIN

    SELECT Balance
    INTO v_balance
    FROM Accounts
    WHERE AccountID = p_accountId;

    IF v_balance >= p_amount THEN
        RETURN TRUE;
    ELSE
        RETURN FALSE;
    END IF;

EXCEPTION
    WHEN NO_DATA_FOUND THEN
        RETURN FALSE;
END;
/

-- Test Function

DECLARE
    v_result BOOLEAN;
BEGIN

    v_result :=
        HasSufficientBalance(
            201,
            10000
        );

    IF v_result THEN
        DBMS_OUTPUT.PUT_LINE(
            'Sufficient Balance Available'
        );
    ELSE
        DBMS_OUTPUT.PUT_LINE(
            'Insufficient Balance'
        );
    END IF;

END;
/

-- ==========================================
-- DISPLAY DATA
-- ==========================================

SELECT * FROM Customers;
SELECT * FROM Accounts;