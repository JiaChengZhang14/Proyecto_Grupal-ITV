drop database if exists EmpresaItv;
create database if not exists EmpresaItv;
use empresaItv;

create table if not exists EstacionItv(
	id_itv int not null auto_increment primary key,
    nombre varchar(50) not null check (nombre <> ""),
    direccion varchar(50) not null check (direccion <> ""),
    telefono char(9) not null check (telefono regexp '[0-9]{9}'),
    correo_electronico varchar(50) not null check (correo_electronico regexp '^[^@]+@[^@]+\.[a-zA-Z]{2,}$')
);

/*El id del trabajador no es auto_increment, si lo fuera debería añadir el id del responsable tras crear todos los trabajadores*/
create table if not exists Trabajador(
	id_trabajador int not null primary key,
    id_itv int not null references estacionItv(id_itv) on delete cascade on update cascade,
    nombre varchar(50) not null check (nombre <> ""),
    telefono char(9) not null check (telefono regexp '[0-9]{9}'),
    email varchar(50) not null unique check (email regexp '^[^@]+@[^@]+\.[a-zA-Z]{2,}$'),
    nombre_usuario varchar(50) not null unique check (nombre_usuario <> ""),
    contraseña_usuario varchar(50) not null unique check (contraseña_usuario <> ""),
    fecha_contratacion date not null check (fecha_contratacion regexp "[0-9]{4}-[0-9]{2}-[0-9]{2}[[:space:]][0-9]{2}:[0-9]{2}:[0-9]{2}"),
    especialidad varchar(20) not null check (especialidad regexp '^(administracion|electricidad|motor|mecanica|interior)$'),
    salario decimal(6,2) not null check (salario > 0),
    id_responsable int not null
);

create table if not exists Propietario(
	dni char(9) not null primary key check (dni regexp '[0-9]{8}[A-Z]{1}'),
    nombre varchar(50) not null check (nombre <> ""),
    apellidos varchar(50) not null check (apellidos <> ""),
    telefono char(9) not null check (telefono regexp '[0-9]{9}'),
    email varchar(50) not null check (email regexp '^[^@]+@[^@]+\.[a-zA-Z]{2,}$')
);

create table if not exists Vehiculo(
	id_vehiculo int not null auto_increment primary key,
	matricula char(7) not null unique check (matricula regexp '^[0-9]{4}[BCDFGHJKLMNPRSTVWXYZ]{3}'),
    id_propietario char(9) not null references propietario(dni) on delete restrict on update cascade,
    marca varchar(50) not null check (marca <> ""),
    modelo varchar(50) not null check (modelo <> ""),
    fecha_matriculacion date not null check (fecha_matriculacion regexp "[0-9]{4}-[0-9]{2}-[0-9]{2}[[:space:]][0-9]{2}:[0-9]{2}:[0-9]{2}"),
    fecha_ultima_revision text not null check (fecha_ultima_revision regexp '[]' || fecha_ultima_revision = ""),
    tipo_motor varchar(15) not null check (matricula regexp '^(gasolina|electrico|diesel|hibrido|otro)$'),
    tipo_vehiculo varchar(15) not null check (matricula regexp '^(turismo|furgoneta|camion|motocicleta|otro)$')
);

create table if not exists Informe(
	fecha_inicio text not null check (fecha_inicio regexp "[0-9]{4}-[0-9]{2}-[0-9]{2}[[:space:]][0-9]{2}:[0-9]{2}:[0-9]{2}"),
    fecha_final text not null check (fecha_final regexp "[0-9]{4}-[0-9]{2}-[0-9]{2}[[:space:]][0-9]{2}:[0-9]{2}:[0-9]{2}" && fecha_final > fecha_inicio),
    id_vehiculo int not null references vehiculo(id_vehiculo) on delete restrict on update cascade,
    id_trabajador int not null references trabajador(id_trabajador) on delete restrict on update cascade,
    favorable text not null check (favorable regexp '^(apto|no apto)$' || favorable = ""),
    frenado numeric(4,2),
    contaminacion numeric(4,2),
    interior text not null  check (interior regexp '^(apto|no apto)$' || interior = ""),
    luces text not null  check (luces regexp '^(apto|no apto)$' || luces = ""), 
    primary key (fecha_inicio(20), fecha_final(20), id_vehiculo)
);

-- Inserciones de prueba
insert into estacionitv values (3, "ITC", "call de porai", "902220123", "correoelectrin@coerlrereo.com");
insert into informe values ("2021-05-24 11:58:12", "2022-05-24 12:00:12", 1, 1, "apto", 12.1, 32.12, "no apto", "apto");
insert into propietario values ("33456789A", "Jia", "Cebadera", "654899345", "soyjia@cebadera.es");
insert into trabajador values (1, 3, "ivan", "689444345", "ivan@email.com", "ivirc", "contra", "2012-05-24 11:58:12", "motor", "1233.23", 1);
insert into vehiculo values (1, "1111SSS", "33456789A","renol", "supra", "2012-12-24", "2017-05-24 11:58:12", "diesel", "turismo");

select * from trabajador;

/*
COSAS UTILIZDAS PARA COMPROBAR QUE TODO FUNCIONA BIEN
create table fechasdad(
	fecha text not null check (fecha regexp "[0-9]{4}-[0-9]{2}-[0-9]{2}[[:space:]][0-9]{2}:[0-9]{2}:[0-9]{2}")
);

create table a (
 a text NOT NULL CHECK (a regexp '^(administracion|electricidad|motor|mecanica|interior)$')
);*/

/*
select * from a

insert into a values ("motor")

insert into fechasdad values
("2023-05-24 11:58:12");
-- check (fecha_final > fecha_inicio),
-- check (fecha_inicio <= localtime()),
*/

