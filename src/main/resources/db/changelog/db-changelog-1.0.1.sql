ALTER TABLE actor
    RENAME COLUMN birth_year TO birth_date;

ALTER TABLE actor
    ALTER COLUMN birth_date TYPE DATE
        USING TO_DATE(birth_date::TEXT, 'YYYY');