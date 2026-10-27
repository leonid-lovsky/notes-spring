CREATE TABLE IF NOT EXISTS `user_note` (
    `id` CHAR(36) PRIMARY KEY,
    `user_id` CHAR(36) NOT NULL,
    `note_id` CHAR(36) NOT NULL,
    `role` VARCHAR(16) NOT NULL
);
