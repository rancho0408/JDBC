-- 실습에서는 jdbctest 계정을 사용

-- 삭제용
drop table board; -- 기존의 board table을 삭제한다
drop sequence board_seq; -- 자동 번호 생성 제거 

-- 수정용

-- 조회
select * from board; -- board table 조회


create table board(
bno		 number(5)		 primary key,
btitle   nvarchar2(30)   not null,
bcontent nvarchar2(1000) not null,
bwriter  nvarchar2(10)   not null,
bdate	 date			 not null
);

create sequence board_seq increment by 1 start with 1 nocycle nocache ;

-- inserting dummy data

insert into BOARD (bno, btitle, bcontent, bwriter, bdate)
values (board_seq.nextval, '비가 오네요...', '비오는데 등교하느라 고생했겠다...', '허명옥', sysdate);
insert into BOARD (bno, btitle, bcontent, bwriter, bdate)
values (board_seq.nextval, '안녕하세요.', '비오는데 등교하느라 고생했겠다...', '허명옥', sysdate);
insert into BOARD (bno, btitle, bcontent, bwriter, bdate)
values (board_seq.nextval, '감사합니다.', '비오는데 등교하느라 고생했겠다...', '허명옥', sysdate);
insert into BOARD (bno, btitle, bcontent, bwriter, bdate)
values (board_seq.nextval, '수고하세요.', '비오는데 등교하느라 고생했겠다...', '허명옥', sysdate);
insert into BOARD (bno, btitle, bcontent, bwriter, bdate)
values (board_seq.nextval, '화이팅.', '비오는데 등교하느라 고생했겠다...', '허명옥', sysdate);
insert into BOARD (bno, btitle, bcontent, bwriter, bdate)
values (board_seq.nextval, '밥먹고 해요.', '비오는데 등교하느라 고생했겠다...', '허명옥', sysdate);

