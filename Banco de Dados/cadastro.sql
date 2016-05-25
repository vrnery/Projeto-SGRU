use SGRU;

insert into Pessoa values(1,'123','Cassiane','cassiane@email.com','5144444444','cassiane','123');
insert into Pessoa values(2,'456','Marcelo','marcelo@email.com','5133333333','marcelo','456');
insert into Pessoa values(3,'789','Vanderson','vanderson@email.com','5122222222','vanderson','789');
insert into Pessoa values(4,'987','Leonardo','leonardo@email.com','5111111111','leonardo','987');
insert into Pessoa values(5,'147','Calvin','calvin@email.com','5100000000','calvin','147');
insert into Pessoa values(6,'258','Gerente','gerente@email.com','5500000000','gerente','258');
insert into Pessoa values(7,'123456','Operador','oper1@email.com','5155555555','oper1','oper');

insert into TipoFuncionario values(1,'Gerente');
insert into TipoFuncionario values(2,'Operador de Caixa');

insert into Funcionario values(6,1);
insert into Funcionario values(7,2);

insert into Cartao values(1,'2016-04-29',0);
insert into Cartao values(2,'2016-04-29',0);
insert into Cartao values(3,'2016-04-29',0);
insert into Cartao values(4,'2016-04-29',0);
insert into Cartao values(5,'2016-04-29',0);

insert into TipoCliente values(1,'Aluno');
insert into TipoCliente values(2,'Professor');

insert into Cliente values(1, 'C:\\imagens\\123.jpg',1,1);
insert into Cliente values(2, 'C:\\imagens\\456.jpg',2,1);
insert into Cliente values(3, 'C:\\imagens\\789.jpg',3,1);
insert into Cliente values(4, 'C:\\imagens\\987.jpg',4,2);
insert into Cliente values(5, 'C:\\imagens\\147.jpg',5,1);

insert into Recarga values(1,100,'2016-04-14',0,1);
insert into Recarga values(2,200,'2016-04-14',0,1);
insert into Recarga values(3,300,'2016-04-15',0,1);
insert into Recarga values(4,400,'2016-04-16',0,1);

insert into Recarga values(5,100,'2016-04-19',0,2);
insert into Recarga values(6,200,'2016-04-17',0,2);
insert into Recarga values(7,300,'2016-04-18',0,2);
insert into Recarga values(8,400,'2016-04-16',0,2);

insert into Recarga values(9,100,'2016-04-29',0,3);
insert into Recarga values(10,200,'2016-04-29',0,3);
insert into Recarga values(11,300,'2016-04-29',0,3);
insert into Recarga values(12,400,'2016-04-29',0,3);

insert into Recarga values(13,100,'2016-04-14',0,4);
insert into Recarga values(14,200,'2016-04-14',0,4);
insert into Recarga values(15,300,'2016-04-15',0,4);
insert into Recarga values(16,400,'2016-04-16',0,4);

insert into ValorAlmoco values(1,1,'2016-04-14');
insert into ValorAlmoco values(2,2,'2016-04-15');
insert into ValorAlmoco values(3,3,'2016-04-16');
insert into ValorAlmoco values(4,4,'2016-04-18');