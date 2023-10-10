-- liquibase formatted sql

-- changeset taras:5
create TABLE notification_task (
                                   chat_id BIGINT PRIMARY KEY ,
                                   notification_text VARCHAR(255),
                                   notification_time TIMESTAMP
);

CREATE INDEX idx_notification_task_chat_id ON notification_task (chat_id);