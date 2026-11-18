CREATE DATABASE IF NOT EXISTS daily_checkin CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE daily_checkin;
CREATE TABLE IF NOT EXISTS entries (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(120) NOT NULL,
    note VARCHAR(2000),
    entry_date DATE NOT NULL,
    reminder_time TIME,
    done BOOLEAN NOT NULL DEFAULT FALSE
) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS checkin_records (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    entry_id BIGINT NULL,
    entry_title VARCHAR(120) NOT NULL,
    checkin_date DATE NOT NULL,
    checked_at DATETIME NOT NULL,
    UNIQUE KEY uk_checkin_entry_date (entry_id, checkin_date),
    KEY idx_checkin_date (checkin_date),
    CONSTRAINT fk_checkin_entry
        FOREIGN KEY (entry_id) REFERENCES entries(id) ON DELETE SET NULL
) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
