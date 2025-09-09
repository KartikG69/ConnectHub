-- Sample data for development and testing
-- ConnectHub sample users and conversations

-- Insert sample users
INSERT INTO users (firebase_uid, name, email, is_anonymous) VALUES
('user_1', 'John Doe', 'john@example.com', FALSE),
('user_2', 'Jane Smith', 'jane@example.com', FALSE),
('user_3', 'Bob Wilson', 'bob@example.com', FALSE),
('anonymous_1', 'Guest User', NULL, TRUE),
('anonymous_2', 'Anonymous Guest', NULL, TRUE);

-- Insert sample conversations
WITH user_ids AS (
    SELECT id, name FROM users WHERE firebase_uid IN ('user_1', 'user_2', 'user_3')
)
INSERT INTO conversations (id, name, is_group, creator_id) 
SELECT 
    uuid_generate_v4(),
    CASE 
        WHEN u1.name = 'John Doe' AND u2.name = 'Jane Smith' THEN NULL -- Direct message
        ELSE 'Team Chat'
    END,
    CASE 
        WHEN u1.name = 'John Doe' AND u2.name = 'Jane Smith' THEN FALSE
        ELSE TRUE
    END,
    u1.id
FROM user_ids u1, user_ids u2 
WHERE u1.id != u2.id 
LIMIT 2;

-- Add participants to conversations
WITH conversation_data AS (
    SELECT c.id as conv_id, u.id as user_id
    FROM conversations c
    CROSS JOIN users u
    WHERE u.is_anonymous = FALSE
    AND (
        (c.is_group = FALSE AND u.firebase_uid IN ('user_1', 'user_2')) OR
        (c.is_group = TRUE)
    )
)
INSERT INTO conversation_participants (conversation_id, user_id)
SELECT conv_id, user_id FROM conversation_data;

-- Insert sample messages
WITH sample_conversation AS (
    SELECT id FROM conversations LIMIT 1
),
sample_users AS (
    SELECT id, firebase_uid FROM users WHERE firebase_uid IN ('user_1', 'user_2') ORDER BY firebase_uid
)
INSERT INTO messages (conversation_id, sender_id, content, message_type)
SELECT 
    sc.id,
    su.id,
    CASE su.firebase_uid
        WHEN 'user_1' THEN 'Hello there! How are you doing?'
        WHEN 'user_2' THEN 'Hi! I''m doing great, thanks for asking!'
    END,
    'text'
FROM sample_conversation sc
CROSS JOIN sample_users su;

-- Insert sample calls
WITH caller AS (
    SELECT id FROM users WHERE firebase_uid = 'user_1'
),
receiver AS (
    SELECT id FROM users WHERE firebase_uid = 'user_2'
)
INSERT INTO calls (caller_id, receiver_id, call_type, status, duration_seconds, ended_at)
SELECT 
    c.id,
    r.id,
    'video',
    'ended',
    1205, -- 20 minutes 5 seconds
    NOW() - INTERVAL '1 hour'
FROM caller c, receiver r;