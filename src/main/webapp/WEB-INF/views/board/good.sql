
create table good2(
	idx int not null auto_increment primary key,    /* 좋아요 고유번호 */
	mid varchar(20) not null,            /* 해당 분야의 해당 게시글에 접속한 사용자 아이디*/
	partIdx int not null,	            /*그 분야(테이블)의 고유번호  */
	part varchar(20) not null,        /* 어떤 분야(테이블)  */
	goodCheck int not null      -- 좋아요 0 : no, 1 : ok 
);

drop table good2;

select * from good2;

select goodCheck from good2 where mid like '%%' and boardIdx like '%%';

select count(*) from good2 where mid='#{mid}' and boardIdx='#{idx}';