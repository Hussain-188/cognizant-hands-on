-- ============================================================================
-- Exercise 3: Stored Procedures
-- Banking System: Interest Processing, Employee Bonus, and Fund Transfer
-- ============================================================================

-- Scenario 1: Process Monthly Interest for Savings Accounts
-- ============================================================================
-- Purpose: Calculate and update balance of all savings accounts by applying 
--          an interest rate of 1% to the current balance
-- ============================================================================

CREATE OR REPLACE PROCEDURE ProcessMonthlyInterest
AS
    DECLARE
        v_interest_rate NUMBER := 0.01; -- 1% interest rate
        v_updated_accounts NUMBER := 0;
    BEGIN
        -- Update all savings accounts with 1% interest
        UPDATE Accounts
        SET balance = balance * (1 + v_interest_rate),
            last_updated = SYSDATE
        WHERE account_type = 'SAVINGS';
        
        -- Get the number of updated accounts
        v_updated_accounts := SQL%ROWCOUNT;
        
        -- Log the transaction
        INSERT INTO Transaction_Log (log_date, log_type, description, affected_records)
        VALUES (SYSDATE, 'MONTHLY_INTEREST', 'Monthly interest processed at 1%', v_updated_accounts);
        
        COMMIT;
        
        DBMS_OUTPUT.PUT_LINE('Monthly interest processed successfully!');
        DBMS_OUTPUT.PUT_LINE('Total savings accounts updated: ' || v_updated_accounts);
        
    EXCEPTION
        WHEN OTHERS THEN
            ROLLBACK;
            DBMS_OUTPUT.PUT_LINE('Error processing monthly interest: ' || SQLERRM);
            INSERT INTO Error_Log (error_date, error_message, error_code) 
            VALUES (SYSDATE, SQLERRM, SQLCODE);
            COMMIT;
    END ProcessMonthlyInterest;
/

-- ============================================================================
-- Scenario 2: Update Employee Bonus Based on Department Performance
-- ============================================================================
-- Purpose: Update the salary of employees in a given department by adding a 
--          bonus percentage passed as a parameter
-- Parameters:
--   p_department_id: Department ID to apply bonus to
--   p_bonus_percentage: Bonus percentage to add to salary (e.g., 10 for 10%)
-- ============================================================================

CREATE OR REPLACE PROCEDURE UpdateEmployeeBonus (
    p_department_id IN NUMBER,
    p_bonus_percentage IN NUMBER
)
AS
    DECLARE
        v_updated_employees NUMBER := 0;
        v_total_bonus_amount NUMBER := 0;
        v_department_name VARCHAR2(100);
    BEGIN
        -- Validate bonus percentage
        IF p_bonus_percentage < 0 OR p_bonus_percentage > 100 THEN
            RAISE_APPLICATION_ERROR(-20001, 'Bonus percentage must be between 0 and 100');
        END IF;
        
        -- Validate department exists
        SELECT department_name INTO v_department_name
        FROM Departments
        WHERE department_id = p_department_id;
        
        -- Calculate total bonus amount before update
        SELECT NVL(SUM(salary * p_bonus_percentage / 100), 0) INTO v_total_bonus_amount
        FROM Employees
        WHERE department_id = p_department_id;
        
        -- Update employee salaries with bonus
        UPDATE Employees
        SET salary = salary + (salary * p_bonus_percentage / 100),
            last_updated = SYSDATE
        WHERE department_id = p_department_id;
        
        -- Get the number of updated employees
        v_updated_employees := SQL%ROWCOUNT;
        
        -- Log the bonus transaction
        INSERT INTO Transaction_Log (log_date, log_type, description, affected_records, amount)
        VALUES (SYSDATE, 'EMPLOYEE_BONUS', 
                'Bonus applied to ' || v_department_name || ' department at ' || p_bonus_percentage || '%',
                v_updated_employees, v_total_bonus_amount);
        
        COMMIT;
        
        DBMS_OUTPUT.PUT_LINE('Employee bonus updated successfully!');
        DBMS_OUTPUT.PUT_LINE('Department: ' || v_department_name);
        DBMS_OUTPUT.PUT_LINE('Bonus Percentage: ' || p_bonus_percentage || '%');
        DBMS_OUTPUT.PUT_LINE('Total Employees Updated: ' || v_updated_employees);
        DBMS_OUTPUT.PUT_LINE('Total Bonus Amount: ' || v_total_bonus_amount);
        
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            ROLLBACK;
            DBMS_OUTPUT.PUT_LINE('Error: Department ID ' || p_department_id || ' does not exist');
        WHEN OTHERS THEN
            ROLLBACK;
            DBMS_OUTPUT.PUT_LINE('Error updating employee bonus: ' || SQLERRM);
            INSERT INTO Error_Log (error_date, error_message, error_code) 
            VALUES (SYSDATE, SQLERRM, SQLCODE);
            COMMIT;
    END UpdateEmployeeBonus;
/

-- ============================================================================
-- Scenario 3: Transfer Funds Between Accounts
-- ============================================================================
-- Purpose: Transfer a specified amount from one account to another, checking 
--          that the source account has sufficient balance before making the transfer
-- Parameters:
--   p_source_account_id: ID of the account to transfer from
--   p_destination_account_id: ID of the account to transfer to
--   p_amount: Amount to transfer
-- ============================================================================

CREATE OR REPLACE PROCEDURE TransferFunds (
    p_source_account_id IN NUMBER,
    p_destination_account_id IN NUMBER,
    p_amount IN NUMBER
)
AS
    DECLARE
        v_source_balance NUMBER;
        v_source_customer_id NUMBER;
        v_destination_customer_id NUMBER;
        v_transfer_id NUMBER;
    BEGIN
        -- Validate amount
        IF p_amount <= 0 THEN
            RAISE_APPLICATION_ERROR(-20002, 'Transfer amount must be greater than zero');
        END IF;
        
        -- Check if source and destination accounts are different
        IF p_source_account_id = p_destination_account_id THEN
            RAISE_APPLICATION_ERROR(-20003, 'Source and destination accounts cannot be the same');
        END IF;
        
        -- Lock and retrieve source account details
        SELECT balance, customer_id INTO v_source_balance, v_source_customer_id
        FROM Accounts
        WHERE account_id = p_source_account_id
        FOR UPDATE;
        
        -- Retrieve destination account details
        SELECT customer_id INTO v_destination_customer_id
        FROM Accounts
        WHERE account_id = p_destination_account_id;
        
        -- Check sufficient balance in source account
        IF v_source_balance < p_amount THEN
            RAISE_APPLICATION_ERROR(-20004, 
                'Insufficient balance. Available: ' || v_source_balance || ', Required: ' || p_amount);
        END IF;
        
        -- Deduct amount from source account
        UPDATE Accounts
        SET balance = balance - p_amount,
            last_updated = SYSDATE
        WHERE account_id = p_source_account_id;
        
        -- Add amount to destination account
        UPDATE Accounts
        SET balance = balance + p_amount,
            last_updated = SYSDATE
        WHERE account_id = p_destination_account_id;
        
        -- Generate transfer ID (using sequence or timestamp-based ID)
        v_transfer_id := TRUNC(DBMS_RANDOM.VALUE(100000, 999999));
        
        -- Record the transfer transaction
        INSERT INTO Transfers (transfer_id, source_account_id, destination_account_id, 
                              amount, transfer_date, status)
        VALUES (v_transfer_id, p_source_account_id, p_destination_account_id, 
                p_amount, SYSDATE, 'COMPLETED');
        
        -- Log the transaction
        INSERT INTO Transaction_Log (log_date, log_type, description, amount)
        VALUES (SYSDATE, 'FUND_TRANSFER', 
                'Transfer from Account ' || p_source_account_id || ' to Account ' || 
                p_destination_account_id, p_amount);
        
        COMMIT;
        
        DBMS_OUTPUT.PUT_LINE('Fund transfer completed successfully!');
        DBMS_OUTPUT.PUT_LINE('Transfer ID: ' || v_transfer_id);
        DBMS_OUTPUT.PUT_LINE('Source Account: ' || p_source_account_id);
        DBMS_OUTPUT.PUT_LINE('Destination Account: ' || p_destination_account_id);
        DBMS_OUTPUT.PUT_LINE('Amount Transferred: ' || p_amount);
        
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            ROLLBACK;
            DBMS_OUTPUT.PUT_LINE('Error: One or both account IDs do not exist');
        WHEN OTHERS THEN
            ROLLBACK;
            DBMS_OUTPUT.PUT_LINE('Error during fund transfer: ' || SQLERRM);
            INSERT INTO Error_Log (error_date, error_message, error_code) 
            VALUES (SYSDATE, SQLERRM, SQLCODE);
            COMMIT;
    END TransferFunds;
/

-- ============================================================================
-- USAGE EXAMPLES
-- ============================================================================

-- Example 1: Process monthly interest for all savings accounts
-- EXECUTE ProcessMonthlyInterest();

-- Example 2: Add 10% bonus to Sales department employees
-- EXECUTE UpdateEmployeeBonus(10, 10);

-- Example 3: Transfer 5000 from Account 101 to Account 102
-- EXECUTE TransferFunds(101, 102, 5000);

-- ============================================================================
-- NOTES:
-- ============================================================================
-- - Assumes the following tables exist:
--   * Accounts (account_id, account_type, balance, customer_id, last_updated)
--   * Employees (emp_id, salary, department_id, last_updated)
--   * Departments (department_id, department_name)
--   * Transaction_Log (log_date, log_type, description, affected_records, amount)
--   * Transfers (transfer_id, source_account_id, destination_account_id, amount, transfer_date, status)
--   * Error_Log (error_date, error_message, error_code)
--
-- - All procedures include error handling and transaction logging
-- - Transactions are automatically rolled back on errors
-- - Use proper exception handling in calling code
-- ============================================================================
