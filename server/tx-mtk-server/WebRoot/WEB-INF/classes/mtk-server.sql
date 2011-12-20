create table pet(
	ownerid int,
	uname char(50),
	type tinyint,
	name char(50),
	sex tinyint,
	level tinyint,
	color tinyint,
	mood tinyint,
	streangth int,
	exp int,
	bone int,
	food int,
	background int,
	mat int,
	icon char(255),
	time datetime,
	primary key(ownerid)
)engine=InnoDB default charset=utf8;
create table prop(
	id int auto_increment,
	category char(10),
	num char(10),
	price char(10),
	descrip char(50),
	primary key(id)
)engine=InnoDB default charset=utf8;

create table petlog(
	id int auto_increment,
	viewerid int,
	ownerid int,
	action int,
	bone int,
	time datetime,
	primary key(id)
)engine=InnoDB default charset=utf8;


create table phonebook(
	id int auto_increment,
	viewerid int,
	json varchar(5000),
	time datetime,
	primary key(id)
)engine=InnoDB default charset=utf8;
