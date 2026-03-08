INSERT INTO account (account_id, created_at, updated_at, version, account_balance, account_name, user, default_amount, status)
VALUES
    (1, NULL, NULL, NULL, NULL, 'Card', NULL, 1, 'ACTIVE'),
    (2, NULL, NULL, NULL, NULL, 'Cash', NULL, 1, 'ACTIVE'),
    (3, NULL, NULL, NULL, NULL, 'Savings', NULL, 1, 'ACTIVE');

INSERT INTO transaction
(transaction_id, created_at, updated_at, version, status, transaction_display_name, transaction_key_name, default_transaction, user_user_id)
VALUES
    (1, NOW(), NOW(), 0, TRUE, 'Income', 'INCOME', TRUE, NULL),
    (2, NOW(), NOW(), 0, TRUE, 'Expense', 'EXPENSE', TRUE, NULL),
    (3, NOW(), NOW(), 0, TRUE, 'Transfer', 'TRANSFER', TRUE, NULL);


INSERT INTO category
(
    category_id,
    created_at,
    updated_at,
    version,
    status,
    category_name,
    transaction_id,
    default_category,
    user_user_id
)
VALUES
    (1, NOW(), NOW(), 0, TRUE, 'Food', 2, TRUE, NULL),
    (2, NOW(), NOW(), 0, TRUE, 'Home', 2, TRUE, NULL),
    (3, NOW(), NOW(), 0, TRUE, 'Transportation', 2, TRUE, NULL),
    (4, NOW(), NOW(), 0, TRUE, 'Social', 2, TRUE, NULL),
    (5, NOW(), NOW(), 0, TRUE, 'Shopping', 2, TRUE, NULL);