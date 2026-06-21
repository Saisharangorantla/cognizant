-- ==========================================
-- CREATE TABLES
-- ==========================================

CREATE TABLE Customers (
    CustomerID NUMBER PRIMARY KEY,
    Name VARCHAR2(50),
    Age NUMBER,
    Balance NUMBER(10,2),
    IsVIP VARCHAR2(5)
);

CREATE TABLE Loans (
    LoanID NUMBER PRIMARY KEY,
    CustomerID NUMBER,
    InterestRate NUMBER(5,2),
    DueDate DATE,
    CONSTRAINT fk_customer
        FOREIGN KEY (CustomerID)
        REFERENCES Customers(CustomerID)
);

-- ==========================================
-- INSERT SAMPLE DATA
-- ==========================================

INSERT INTO Customers VALUES (101, 'Virat', 65, 15000, 'FALSE');
INSERT INTO Customers VALUES (102, 'Kumar', 45, 8000, 'FALSE');
INSERT INTO Customers VALUES (103, 'Priya', 70, 20000, 'FALSE');
INSERT INTO Customers VALUES (104, 'Sita', 30, 12000, 'FALSE');
INSERT INTO Customers VALUES (105, 'Ravi', 55, 5000, 'FALSE');

INSERT INTO Loans VALUES (201, 101, 8.50, SYSDATE + 10);
INSERT INTO Loans VALUES (202, 102, 9.00, SYSDATE + 40);
INSERT INTO Loans VALUES (203, 103, 7.50, SYSDATE + 20);
INSERT INTO Loans VALUES (204, 104, 8.00, SYSDATE + 25);
INSERT INTO Loans VALUES (205, 105, 9.25, SYSDATE + 60);

COMMIT;

-- ==========================================
-- DISPLAY INITIAL DATA
-- ==========================================

SELECT * FROM Customers;
SELECT * FROM Loans;

-- ==========================================
-- SCENARIO 1:
-- Apply 1% Discount to Interest Rates
-- for Customers Above 60 Years
-- ==========================================

BEGIN
    FOR i IN (
        SELECT l.LoanID
        FROM Loans l
        JOIN Customers c
        ON l.CustomerID = c.CustomerID
        WHERE c.Age > 60
    )
    LOOP
        UPDATE Loans
        SET InterestRate = InterestRate - 1
        WHERE LoanID = i.LoanID;
    END LOOP;

    COMMIT;
END;
/

SELECT * FROM Loans;

-- ==========================================
-- SCENARIO 2:
-- Promote Customers to VIP Status
-- Balance > 10000
-- ==========================================

BEGIN
    FOR i IN (
        SELECT CustomerID
        FROM Customers
        WHERE Balance > 10000
    )
    LOOP
        UPDATE Customers
        SET IsVIP = 'TRUE'
        WHERE CustomerID = i.CustomerID;
    END LOOP;

    COMMIT;
END;
/

SELECT * FROM Customers;

-- ==========================================
-- SCENARIO 3:
-- Loan Due Reminder Within Next 30 Days
-- ==========================================

SET SERVEROUTPUT ON;

BEGIN
    FOR i IN (
        SELECT c.Name,
               l.LoanID,
               l.DueDate
        FROM Customers c
        JOIN Loans l
        ON c.CustomerID = l.CustomerID
        WHERE l.DueDate BETWEEN SYSDATE AND SYSDATE + 30
    )
    LOOP
        DBMS_OUTPUT.PUT_LINE(
            'Reminder: Dear ' || i.Name ||
            ', your Loan ID ' || i.LoanID ||
            ' is due on ' ||
            TO_CHAR(i.DueDate, 'DD-MON-YYYY')
        );
    END LOOP;
END;
/