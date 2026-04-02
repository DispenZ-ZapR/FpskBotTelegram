-- Создание таблицы для хранения состояний пользователей
CREATE TABLE IF NOT EXISTS user_states (
    chat_id BIGINT PRIMARY KEY,
    state VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Добавление нового поля is_read в таблицу user_requests
ALTER TABLE user_requests 
ADD COLUMN IF NOT EXISTS is_read BOOLEAN DEFAULT FALSE;

-- Создание индекса для быстрого поиска по chat_id и дате
CREATE INDEX IF NOT EXISTS idx_user_requests_chat_date 
ON user_requests(chat_id, request_date);

-- Создание индекса для быстрого поиска по статусу
CREATE INDEX IF NOT EXISTS idx_user_requests_status 
ON user_requests(status);
