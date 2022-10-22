CREATE
SET TABLE test.users (
   id INTEGER GENERATED ALWAYS AS IDENTITY
(START WITH 1
INCREMENT BY 1
MINVALUE 0
MAXVALUE 999999999
NO CYCLE),
   name VARCHAR(100),
   pass VARCHAR(100),
   phone_number VARCHAR(100)
)
UNIQUE PRIMARY INDEX ( id );

insert into test.users( name,  pass, phone_number)
values('jack','123123a@','0123456789');

insert into test.users( name,  pass, phone_number)
values('jay','123456a@','1234567890');