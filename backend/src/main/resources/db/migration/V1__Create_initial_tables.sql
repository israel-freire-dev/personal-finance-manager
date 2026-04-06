-- Enum types
CREATE TYPE transaction_status AS ENUM ('PENDING', 'PAID', 'REFUNDED', 'INVESTED', 'CANCELLED');
CREATE TYPE transaction_type AS ENUM ('INCOME', 'EXPENSE');
CREATE TYPE recurring_frequency AS ENUM ('MONTHLY', 'WEEKLY', 'ANNUAL');

-- Tables
CREATE TABLE users (
    id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    name varchar NOT NULL,
    email varchar UNIQUE NOT NULL,
    password_hash varchar NOT NULL,
    created_at timestamp DEFAULT now(),
    updated_at timestamp DEFAULT now()
);

CREATE TABLE categories (
    id uuid PRIMARY KEY,
    user_id uuid REFERENCES users(id),
    parent_id uuid REFERENCES categories(id),
    name varchar NOT NULL,
    type transaction_type NOT NULL,
    color varchar(7),
    icon varchar,
    created_at timestamp DEFAULT now(),
    updated_at timestamp DEFAULT now(),
    UNIQUE (user_id, name)
);

CREATE TABLE recurring_templates (
    id uuid PRIMARY KEY,
    user_id uuid REFERENCES users(id),
    category_id uuid REFERENCES categories(id),
    description varchar NOT NULL,
    amount decimal(19,4) NOT NULL,
    frequency recurring_frequency DEFAULT 'MONTHLY',
    start_date date NOT NULL,
    end_date date,
    total_installments integer,
    created_at timestamp DEFAULT now(),
    updated_at timestamp DEFAULT now()
);

CREATE TABLE transactions (
    id uuid PRIMARY KEY,
    user_id uuid REFERENCES users(id),
    category_id uuid REFERENCES categories(id),
    recurring_template_id uuid REFERENCES recurring_templates(id),
    description varchar NOT NULL,
    amount decimal(19,4) NOT NULL,
    date date NOT NULL,
    status transaction_status DEFAULT 'PENDING',
    type transaction_type NOT NULL,
    created_at timestamp DEFAULT now(),
    updated_at timestamp DEFAULT now()
);

CREATE TABLE monthly_summaries (
    id uuid PRIMARY KEY,
    user_id uuid REFERENCES users(id),
    month integer NOT NULL,
    year integer NOT NULL,
    opening_balance decimal(19,4) DEFAULT 0,
    total_income decimal(19,4) DEFAULT 0,
    total_expenses decimal(19,4) DEFAULT 0,
    closing_balance decimal(19,4) DEFAULT 0,
    is_closed boolean DEFAULT false,
    created_at timestamp DEFAULT now(),
    updated_at timestamp DEFAULT now(),
    UNIQUE (user_id, year, month)
);

-- Indexes
CREATE INDEX idx_transactions_user_date ON transactions(user_id, date);
CREATE INDEX idx_transactions_user_status ON transactions(user_id, status);
CREATE INDEX idx_transactions_category ON transactions(category_id);
