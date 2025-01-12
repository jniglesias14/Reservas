CREATE USER user10@localhost IDENTIFIED BY 'A12345a';
grant all on practica37.* to user10@localhost;
use practica37;
create database practica37;

create table salas(
                      id_sala char(3),
                      nombre varchar(50) not null ,
                      constraint salas_pk primary key(id_sala),
                      constraint  salas_uk unique (nombre)
);

create table departamentos(
                              id_departamento char(3),
                              nombre varchar(50) not null,
                              constraint departamento_pk primary key(id_departamento),
                              constraint departamento_uk unique (nombre)
);

create table reservas(
                         id_sala char(3),
                         id_departamento char(3),
                         fecha_inicio datetime not null ,
                         fecha_fin datetime not null ,
                         constraint reservas_pk primary key (id_sala,id_departamento,fecha_inicio),
                         constraint reservas_fk1 foreign key (id_sala) references salas(id_sala) on delete cascade,
                         constraint reservas_fk2 foreign key (id_departamento) references departamentos(id_departamento) on delete cascade

);

