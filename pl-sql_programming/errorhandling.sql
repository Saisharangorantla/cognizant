-- ==========================================
-- EXERCISE 2 : ERROR HANDLING
-- ==========================================

SET SERVEROUTPUT ON;

-- ==========================================
-- CREATE TABLES
-- ==========================================

CREATE TABLE Accounts (
    AccountID NUMBER PRIMARY KEY,
    CustomerName VARCHAR2(50),
    Balance NUMBER(10,2)
);

CREATE TABLE Employees (
    EmployeeID NUMBER PRIMARY KEY,
    EmployeeName VARCHAR2(50),
    Salary NUMBER(10,2)
);

CREATE TABLE Customers (
    CustomerID NUMBER PRIMARY KEY,
    Name VARCHAR2(50),
    Age NUMBER,
    Balance NUMBER(10,2)
);

-- ==========================================
-- INSERT SAMPLE DATA
-- ==========================================

INSERT INTO Accounts VALUES (101, 'virat', 15000);
INSERT INTO Accounts VALUES (102, 'dhoni', 5000);

INSERT INTO Employees VALUES (1, 'Hari', 40000);
INSERT INTO Employees VALUES (2, 'Shashi', 50000);

INSERT INTO Customers VALUES (201, 'Ravi', 25, 12000);
INSERT INTO Customers VALUES (202, 'Shashi', 30, 8000);

COMMIT;

-- ==========================================
-- SCENARIO 1
-- SAFE TRANSFER FUNDS
-- ==========================================

CREATE OR REPLACE PROCEDURE SafeTransferFunds(
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
        RAISE_APPLICATION_ERROR(
            -20001,
            'Insufficient Funds'
        );
    END IF;

    UPDATE Accounts
    SET Balance = Balance - p_amount
    WHERE AccountID = p_fromAccount;

    UPDATE Accounts
    SET Balance = Balance + p_amount
    WHERE AccountID = p_toAccount;

    COMMIT;

    DBMS_OUTPUT.PUT_LINE('Fund Transfer Successful');

EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        DBMS_OUTPUT.PUT_LINE(
            'Transfer Failed : ' || SQLERRM
        );
END;
/

-- Execute Procedure
BEGIN
    SafeTransferFunds(101,102,3000);
END;
/

-- ==========================================
-- SCENARIO 2
-- UPDATE EMPLOYEE SALARY
-- ==========================================

CREATE OR REPLACE PROCEDURE UpdateSalary(
    p_employeeId NUMBER,
    p_percentage NUMBER
)
IS
BEGIN
    UPDATE Employees
    SET Salary = Salary + (Salary * p_percentage / 100)
    WHERE EmployeeID = p_employeeId;

    IF SQL%ROWCOUNT = 0 THEN
        RAISE NO_DATA_FOUND;
    END IF;

    COMMIT;

    DBMS_OUTPUT.PUT_LINE(
        'Salary Updated Successfully'
    );

EXCEPTION
    WHEN NO_DATA_FOUND THEN
        DBMS_OUTPUT.PUT_LINE(
            'Error : Employee ID Not Found'
        );

    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE(
            'Error : ' || SQLERRM
        );
END;
/

-- Execute Procedure
BEGIN
    UpdateSalary(1,10);
END;
/

-- ==========================================
-- SCENARIO 3
-- ADD NEW CUSTOMER
-- ==========================================

CREATE OR REPLACE PROCEDURE AddNewCustomer(
    p_customerId NUMBER,
    p_name VARCHAR2,
    p_age NUMBER,
    p_balance NUMBER
)
IS
BEGIN
    INSERT INTO Customers
    VALUES(
        p_customerId,
        p_name,
        p_age,
        p_balance
    );

    COMMIT;

    DBMS_OUTPUT.PUT_LINE(
        'Customer Added Successfully'
    );

EXCEPTION
    WHEN DUP_VAL_ON_INDEX THEN
        DBMS_OUTPUT.PUT_LINE(
            'Error : Customer ID Already Exists'
        );

    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE(
            'Error : ' || SQLERRM
        );
END;
/

-- Execute Procedure
BEGIN
    AddNewCustomer(
        201,
        'Ramesh',
        28,
        15000
    );
END;
/

-- ==========================================
-- DISPLAY DATA
-- ==========================================

SELECT * FROM Accounts;
SELECT * FROM Employees;
SELECT * FROM Customers;