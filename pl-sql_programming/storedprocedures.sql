-- ==========================================
-- EXERCISE 3 : STORED PROCEDURES
-- ==========================================

SET SERVEROUTPUT ON;

-- ==========================================
-- CREATE TABLES
-- ==========================================

CREATE TABLE Accounts (
    AccountID NUMBER PRIMARY KEY,
    CustomerName VARCHAR2(50),
    AccountType VARCHAR2(20),
    Balance NUMBER(10,2)
);

CREATE TABLE Employees (
    EmployeeID NUMBER PRIMARY KEY,
    EmployeeName VARCHAR2(50),
    Department VARCHAR2(30),
    Salary NUMBER(10,2)
);

-- ==========================================
-- INSERT SAMPLE DATA
-- ==========================================

INSERT INTO Accounts VALUES (101, 'Ram', 'Savings', 10000);
INSERT INTO Accounts VALUES (102, 'Lohith', 'Savings', 15000);
INSERT INTO Accounts VALUES (103, 'virat', 'Current', 20000);
INSERT INTO Accounts VALUES (104, 'dhoni', 'Savings', 12000);

INSERT INTO Employees VALUES (1, 'Ravi', 'IT', 50000);
INSERT INTO Employees VALUES (2, 'Kiran', 'IT', 60000);
INSERT INTO Employees VALUES (3, 'Anu', 'HR', 45000);
INSERT INTO Employees VALUES (4, 'sita', 'HR', 55000);

COMMIT;

-- ==========================================
-- SCENARIO 1
-- PROCESS MONTHLY INTEREST
-- Apply 1% interest to all Savings Accounts
-- ==========================================

CREATE OR REPLACE PROCEDURE ProcessMonthlyInterest
IS
BEGIN
    UPDATE Accounts
    SET Balance = Balance + (Balance * 0.01)
    WHERE AccountType = 'Savings';

    COMMIT;

    DBMS_OUTPUT.PUT_LINE(
        'Monthly Interest Processed Successfully'
    );
END;
/

-- Execute Procedure
BEGIN
    ProcessMonthlyInterest;
END;
/

-- ==========================================
-- SCENARIO 2
-- UPDATE EMPLOYEE BONUS
-- Add Bonus Percentage to Employees
-- of a Given Department
-- ==========================================

CREATE OR REPLACE PROCEDURE UpdateEmployeeBonus(
    p_department VARCHAR2,
    p_bonus_percent NUMBER
)
IS
BEGIN
    UPDATE Employees
    SET Salary = Salary +
                (Salary * p_bonus_percent / 100)
    WHERE Department = p_department;

    COMMIT;

    DBMS_OUTPUT.PUT_LINE(
        'Employee Bonus Updated Successfully'
    );
END;
/

-- Execute Procedure
BEGIN
    UpdateEmployeeBonus('IT',10);
END;
/

-- ==========================================
-- SCENARIO 3
-- TRANSFER FUNDS
-- Transfer Amount Between Accounts
-- ==========================================

CREATE OR REPLACE PROCEDURE TransferFunds(
    p_fromAccount NUMBER,
    p_toAccount NUMBER,
    p_amount NUMBER
)
IS
    v_balance NUMBER;
BEGIN

    SELECT Balance
    INTO v_balance
    FROM Accounts
    WHERE AccountID = p_fromAccount;

    IF v_balance < p_amount THEN
        DBMS_OUTPUT.PUT_LINE(
            'Insufficient Balance'
        );
    ELSE

        UPDATE Accounts
        SET Balance = Balance - p_amount
        WHERE AccountID = p_fromAccount;

        UPDATE Accounts
        SET Balance = Balance + p_amount
        WHERE AccountID = p_toAccount;

        COMMIT;

        DBMS_OUTPUT.PUT_LINE(
            'Fund Transfer Successful'
        );

    END IF;

EXCEPTION
    WHEN NO_DATA_FOUND THEN
        DBMS_OUTPUT.PUT_LINE(
            'Account Not Found'
        );

    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE(
            'Error : ' || SQLERRM
        );
END;
/

-- Execute Procedure
BEGIN
    TransferFunds(101,102,2000);
END;
/

-- ==========================================
-- DISPLAY UPDATED DATA
-- ==========================================

SELECT * FROM Accounts;
SELECT * FROM Employees;