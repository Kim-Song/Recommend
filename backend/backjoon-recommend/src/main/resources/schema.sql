CREATE OR REPLACE DATABASE backjoon_reco;

USE backjoon_reco;

CREATE OR REPLACE TABLE code (id BIGINT PRIMARY KEY AUTO_INCREMENT, memory BIGINT, time BIGINT,
    code TEXT, language VARCHAR(255), number BIGINT);

CREATE OR REPLACE TABLE problem (number BIGINT PRIMARY KEY,  name VARCHAR(255), tier BIGINT,
    algorithm VARCHAR(255), time_condition DOUBLE, memory_condition BIGINT, submission_count BIGINT,
    answer_submission_count BIGINT, answered_people_count BIGINT, correct_rate DOUBLE, avg_try DOUBLE);

CREATE OR REPLACE TABLE video (id BIGINT PRIMARY KEY AUTO_INCREMENT, algorithm VARCHAR(255), url VARCHAR(255));