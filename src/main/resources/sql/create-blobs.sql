CREATE TABLE IF NOT EXISTS BLOBS (
                                     id SERIAL PRIMARY KEY,
                                     content oid
);
ALTER TABLE BLOBS ALTER COLUMN id TYPE VARCHAR(36);