LOAD DATA LOCAL INFILE
'/Users/user/Desktop/capstone/backend/backjoon-recommend/src/main/resources/PP_Baekjoon_Problem_.csv'
INTO TABLE problem
FIELDS TERMINATED BY ','
ENCLOSED BY '"'
LINES TERMINATED BY '\n'
IGNORE 1 rows
(number, name, tier, algorithm, time_condition, memory_condition, submission_count,
    answer_submission_count, answered_people_count, correct_rate, @avg_try) SET avg_try = CAST(@avg_try AS DOUBLE);

insert into video
values (1, "이분 탐색", "94RC-DsGMLo");

insert into video
values (2, "이분 탐색", "rBRu9T8v37w");

insert into video
values (3, "이분 탐색", "F6lKjRDlOpk");

insert into video
values (4, "이분 탐색", "Fn22nMA00o4");

insert into video
values (5, "이분 탐색", "SKcG0p73yo4");

insert into video
values (6, "그리디", "_IZuE7NIeW4");

insert into video
values (7, "그리디", "2zjoKjt97vQ");

insert into video
values (8, "그리디", "8V2zw6Qxarc");

insert into video
values (9, "그리디", "nCcOTgrhgRU");

insert into video
values (10, "그리디", "s14vkkAvLRI");

insert into video
values (11, "다이나믹 프로그래밍", "0bqfTzpWySY");

insert into video
values (12, "다이나믹 프로그래밍", "5Lu34WIx2Us");

insert into video
values (13, "다이나믹 프로그래밍", "eJC2oetXaNk");

insert into video
values (14, "다이나믹 프로그래밍", "GtqHli8HIqk");

insert into video
values (15, "다이나믹 프로그래밍", "EQMd8bTIf-8");

insert into video
values (16, "구현", "puH2p1CQEg4");

insert into video
values (17, "구현", "QhMY4t2xwG0");

insert into video
values (18, "구현", "DBXEWJx2mIw");

insert into video
values (19, "구현", "WZK_wOOtuSI");

insert into video
values (20, "구현", "4F955xbBuQ8");

insert into video
values (21, "그래프 이론", "aOhhNFTIeFI");

insert into video
values (22, "그래프 이론", "4izGhUk2L1s");

insert into video
values (23, "그래프 이론", "8dWT-bozhII");

insert into video
values (24, "자료구조", "RZHYuAhUrwE");

insert into video
values (25, "자료구조", "Nk_dGScimz8");

insert into video
values (26, "자료구조", "bh23BDYOry8");

insert into video
values (27, "BFS, DFS", "BsYbdUnKZ-Y");

insert into video
values (28, "BFS, DFS", "7C9RgOcvkvo");

insert into video
values (29, "BFS, DFS", "gl5RhtU2mF8");

insert into video
values (30, "세그먼트트리", "1d9sqmuLy-o");

insert into video
values (31, "세그먼트트리", "075fcq7oCC8");

insert into video
values (32, "세그먼트트리", "XaodfglnhVs");

insert into video
values (33, "비트마스킹", "yHBYeguDR0A");

insert into video
values (34, "비트마스킹", "OFQAEBowlss");

insert into video
values (35, "비트마스킹", "4BSrpZlGW4A");

insert into video
values (36, "백 트래킹", "Ar40zcPoKEI");

insert into video
values (37, "백 트래킹", "HRwFgtiqHH0");

insert into video
values (38, "백 트래킹", "atTzqxbt4DM");

insert into video
values (39, "데이크스트라", "acqm9mM1P6o");

insert into video
values (40, "데이크스트라", "tzUJ7GE1qVs");

insert into video
values (41, "데이크스트라", "qaiuC3Q73-M");

insert into video
values (42, "우선순위 큐", "AjFlp951nz0");

insert into video
values (43, "우선순위 큐", "P-FTb1faxlo");

insert into video
values (44, "우선순위 큐", "_9mbqoF9qzc");

insert into video
values (45, "배낭 문제", "A8nOpWRXQrs");

insert into video
values (46, "배낭 문제", "rhda6lR5kyQ");

insert into video
values (47, "배낭 문제", "uggO0uzGboY");

insert into video
values (48, "정렬", "Bor_CRWEIXo");

insert into video
values (49, "정렬", "KGyK-pNvWos");

insert into video
values (50, "정렬", "iyl9bfp_8ag");

insert into video
values (51, "브루트포스 알고리즘", "ZNa9-86uVEA");

insert into video
values (52, "브루트포스 알고리즘", "ynF1ZTaL4rE");

insert into video
values (53, "브루트포스 알고리즘", "4_P5KrgmvD0");
