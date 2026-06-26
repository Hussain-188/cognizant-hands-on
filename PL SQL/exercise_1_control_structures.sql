-- Exercise 1: Control Structures
-- Assumed tables:
--   CUSTOMERS(customer_id, customer_name, age, balance, is_vip)
--   LOANS(loan_id, customer_id, interest_rate, due_date)
-- Adjust column names and data types to match your schema.

SET SERVEROUTPUT ON;

--------------------------------------------------------------------------------
-- Scenario 1:
-- Apply a 1% discount to loan interest rates for customers above 60 years old.
--------------------------------------------------------------------------------
BEGIN
  FOR customer_rec IN (
    SELECT c.customer_id, c.age
    FROM customers c
    WHERE c.age > 60
  ) LOOP
    UPDATE loans l
    SET l.interest_rate = l.interest_rate - (l.interest_rate * 0.01)
    WHERE l.customer_id = customer_rec.customer_id;
  END LOOP;

  COMMIT;
  DBMS_OUTPUT.PUT_LINE('Scenario 1 completed: 1% interest discount applied to eligible customers.');
END;
/

--------------------------------------------------------------------------------
-- Scenario 2:
-- Set IsVIP to TRUE for customers with a balance over $10,000.
--------------------------------------------------------------------------------
BEGIN
  FOR customer_rec IN (
    SELECT c.customer_id, c.balance
    FROM customers c
    WHERE c.balance > 10000
  ) LOOP
    UPDATE customers c
    SET c.is_vip = 'TRUE'
    WHERE c.customer_id = customer_rec.customer_id;
  END LOOP;

  COMMIT;
  DBMS_OUTPUT.PUT_LINE('Scenario 2 completed: VIP flag set for customers with balance above 10000.');
END;
/

--------------------------------------------------------------------------------
-- Scenario 3:
-- Print reminder messages for loans due within the next 30 days.
--------------------------------------------------------------------------------
BEGIN
  FOR loan_rec IN (
    SELECT c.customer_name,
           l.loan_id,
           l.due_date
    FROM customers c
    JOIN loans l
      ON l.customer_id = c.customer_id
    WHERE l.due_date BETWEEN TRUNC(SYSDATE) AND TRUNC(SYSDATE) + 30
    ORDER BY l.due_date
  ) LOOP
    DBMS_OUTPUT.PUT_LINE(
      'Reminder: Loan ' || loan_rec.loan_id ||
      ' for customer ' || loan_rec.customer_name ||
      ' is due on ' || TO_CHAR(loan_rec.due_date, 'YYYY-MM-DD') || '.'
    );
  END LOOP;
END;
/