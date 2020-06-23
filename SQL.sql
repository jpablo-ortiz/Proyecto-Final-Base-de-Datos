--drop sequence Prestamo_seq;
--drop sequence Moneda_seq;
--drop sequence Linea_seq;

DROP TABLE linea;
DROP TABLE moneda;
DROP TABLE denominacion;
DROP TABLE libro;
DROP TABLE prestamo;

--create sequence Prestamo_seq start with 1 increment by 1 nocache nocycle;
create table Prestamo
(
    numero int,
    fecha  date,
    primary key(numero)  
);

create table Libro
(
    isbn                varchar2(255),
    preciobase          double PRECISION,
    unidadesDisponibles int ,
    numeroImagenes      int,
    numeroVideos        int,
    primary key(isbn)
);

--create sequence Linea_seq start with 1 increment by 1 nocache nocycle;
create table Linea
(
    id int,
    cantidad int,
    libroIsbn varchar2(255),
    prestamoNumero int,
    subtotal double PRECISION,
    primary key (id, prestamoNumero),
    foreign key (prestamoNumero) references Prestamo(numero) on delete cascade,    
    foreign key (libroIsbn) references Libro(isbn)
);


create table Denominacion
(
    denominacion varchar2 (20),    
    check(denominacion in('MIL','QUINIENTOS')),
    primary key(denominacion)
);

--create sequence Moneda_seq start with 1 increment by 1 nocache nocycle;
create table Moneda
(
    id int,
    denominacionMoneda varchar2 (255),
    prestamoNumero int,
    foreign key(prestamoNumero) references Prestamo(numero), 
    foreign key(denominacionMoneda) references Denominacion(denominacion), 
    primary key(id)
);

insert into Libro (isbn, preciobase, unidadesDisponibles, numeroImagenes, numeroVideos) values ('542', 150000, 10, 2, 3);
insert into Libro (isbn, preciobase, unidadesDisponibles, numeroImagenes, numeroVideos) values ('511', 1000, 10, 2, 3);
insert into Prestamo (numero, fecha) values (0, to_date('16/04/2020','dd/mm/yyyy'));
insert into Denominacion (denominacion) values ('MIL');
insert into Moneda (id, denominacionMoneda, prestamoNumero) values (0, 'MIL', 0);
insert into Linea (id, cantidad, libroIsbn, prestamonumero, subtotal) values (0, 1, '542', 0, 150000);
commit;
    