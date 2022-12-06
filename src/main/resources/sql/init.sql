DROP TABLE IBF_DATA;
CREATE TABLE IBF_DATA
(
    STRING_COLUMN  varchar(100),
    NUMBER_COLUMN number(20, 2),
    DATE_COLUMN   date,
    CLOB_COLUMN   clob
);


insert into IBF_DATA
values ('A', 0, TO_DATE('2022-10-01', 'YYYY-MM-DD'), 'AAA');
insert into IBF_DATA
values ('B', 1, TO_DATE('2022-10-02', 'YYYY-MM-DD'), 'BBB');
insert into IBF_DATA
values (NULL, 2, TO_DATE('2022-10-03', 'YYYY-MM-DD'), 'CCC');
insert into IBF_DATA
values ('D', NULL, TO_DATE('2022-10-04', 'YYYY-MM-DD'), 'DDD');
insert into IBF_DATA
values ('E', 4, NULL, 'EEE');
insert into IBF_DATA
values ('F', 5, TO_DATE('2022-10-05', 'YYYY-MM-DD'), NULL);
insert into IBF_DATA
values ('G', 6, TO_DATE('2022-10-06', 'YYYY-MM-DD'), 'GGG');
insert into IBF_DATA
values ('H', 7, TO_DATE('2022-10-07', 'YYYY-MM-DD'), 'HHH');
insert into IBF_DATA
values ('I', 8, TO_DATE('2022-10-08', 'YYYY-MM-DD'), 'III');
insert into IBF_DATA
values ('I', 8, TO_DATE('2022-10-08', 'YYYY-MM-DD'), 'III');





