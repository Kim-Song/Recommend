LOAD DATA LOCAL INFILE
'/Users/user/Desktop/sejong/3학년 2학기/캡스톤디자인/PP_Baekjoon_Problem_.csv'
INTO TABLE problem
FIELDS TERMINATED BY ','
ENCLOSED BY '"'
LINES TERMINATED BY '\n'
IGNORE 1 rows
(number, name, tier, algorithm, time_condition, memory_condition, submission_count, answer_submission_count, answered_people_count, correct_rate, @avg_try) SET avg_try = CAST(@avg_try AS DOUBLE);