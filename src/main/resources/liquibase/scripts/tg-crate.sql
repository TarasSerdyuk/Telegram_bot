-- liquibase formatted sql

-- changeset taras:4
create TABLE notification_task (
                                   id serial primary key,
                                   chat_id numeric,
                                   notification_text varchar(250),
                                   notification_time timestamp
);

CREATE INDEX idx_notification_task_chat_id ON notification_task (chat_id);