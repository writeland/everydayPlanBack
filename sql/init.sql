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
