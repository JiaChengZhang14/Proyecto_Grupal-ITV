drop database if exists EmpresaItv;
create database if not exists EmpresaItv;
use empresaItv;

create table if not exists EstacionItv(
	id_itv int not null auto_increment primary key,
    nombre varchar(50) not null check (nombre <> ''),
    direccion varchar(50) not null check (direccion <> ''),
    telefono char(9) not null check (telefono regexp '[0-9]{9}'),
    correo_electronico varchar(50) not null check (correo_electronico regexp '^[^@]+@[^@]+\.[a-zA-Z]{2,}$')
);

/*El id del trabajador no es auto_increment, si lo fuera debería añadir el id del responsable tras crear todos los trabajadores*/
create table if not exists Trabajador(
	id_trabajador int not null primary key,
    id_itv int not null references estacionItv(id_itv) on delete cascade on update cascade,
    nombre varchar(50) not null check (nombre <> ''),
    telefono char(9) not null check (telefono regexp '[0-9]{9}'),
    email varchar(50) not null unique check (email regexp '^[^@]+@[^@]+\.[a-zA-Z]{2,}$'),
    nombre_usuario varchar(50) not null unique check (nombre_usuario <> ''),
    contraseña_usuario varchar(50) not null unique check (contraseña_usuario <> ''),
    fecha_contratacion date not null check (fecha_contratacion regexp '[0-9]{4}-[0-1][0-9]-[0-9]{2}'),
    especialidad varchar(20) not null check (especialidad regexp '^(administracion|electricidad|motor|mecanica|interior)$'),
    salario decimal(6,2) not null check (salario > 0),
    id_responsable int not null
);

create table if not exists Propietario(
	dni char(9) not null primary key check (dni regexp '[0-9]{8}[A-Z]{1}'),
    nombre varchar(50) not null check (nombre <> ''),
    apellidos varchar(50) not null check (apellidos <> ''),
    telefono char(9) not null check (telefono regexp '[0-9]{9}'),
    email varchar(50) not null check (email regexp '^[^@]+@[^@]+\.[a-zA-Z]{2,}$')
);

create table if not exists Vehiculo(
	id_vehiculo int not null auto_increment primary key,
	matricula char(7) not null unique check (matricula regexp '^[BCDFGHJKLMNPRSTVWXYZ]{4}[0-9]{3}'),
    id_propietario char(9) not null references propietario(dni) on delete restrict on update cascade,
    marca varchar(50) not null check (marca <> ''),
    modelo varchar(50) not null check (modelo <> ''),
    fecha_matriculacion date not null check (fecha_matriculacion regexp '[0-9]{4}-[0-1][0-9]-[0-9]{2}'),
    fecha_ultima_revision text not null check (fecha_ultima_revision regexp '[0-9]{4}-[0-1][0-9]-[0-9]{2} [0-9]{2}:[0-5][0-9](:[0-5][0-9])?' || fecha_ultima_revision regexp '[0-9]{4}-[0-1][0-9]-[0-9]{2}T[0-9]{2}:[0-5][0-9](:[0-5][0-9])?' || fecha_ultima_revision = ''),
    tipo_motor varchar(15) not null check (tipo_motor regexp '^(gasolina|electrico|diesel|hibrido|otro)$'),
    tipo_vehiculo varchar(15) not null check (tipo_vehiculo regexp '^(turismo|furgoneta|camion|motocicleta|otro)$')
);

create table if not exists Informe(
	id_informe int not null auto_increment primary key,
	fecha_inicio text not null check (fecha_inicio regexp '[0-9]{4}-[0-1][0-9]-[0-9]{2} [0-9]{2}:[0-5][0-9](:[0-5][0-9])?' || fecha_inicio regexp '[0-9]{4}-[0-1][0-9]-[0-9]{2}T[0-9]{2}:[0-5][0-9](:[0-5][0-9])?'),
    fecha_final text not null check ((fecha_final regexp '[0-9]{4}-[0-1][0-9]-[0-9]{2} [0-9]{2}:[0-5][0-9](:[0-5][0-9])?' || fecha_final regexp '[0-9]{4}-[0-1][0-9]-[0-9]{2}T[0-9]{2}:[0-5][0-9](:[0-5][0-9])?') && fecha_final > fecha_inicio),
    id_vehiculo int not null references vehiculo(id_vehiculo) on delete restrict on update cascade,
    id_trabajador int not null references trabajador(id_trabajador) on delete restrict on update cascade,
    favorable text not null check (favorable regexp '^(apto|no apto)$' || favorable = ''),
    frenado numeric(4,2) check (frenado IS NULL or (frenado >= 0.0 and frenado <= 10.0)),
    contaminacion numeric(4,2) check (contaminacion IS NULL or (contaminacion >= 20.0 and contaminacion <= 50.0)),
    interior text not null  check (interior regexp '^(apto|no apto)$' || interior = ''),
    luces text not null  check (luces regexp '^(apto|no apto)$' || luces = '')
);

-- Inserciones de prueba
insert into estacionitv values (3, 'ITC', 'Paseo de la ermita', '902220123', 'iesluisvivesItc@luisvives.org');

insert into propietario values ('33456789A', 'Ivan', 'Cebadera', '654899345', 'soyjia@hotmail.es');
insert into propietario values ('84718291N', 'Juan', 'Alberto', '691283733', 'soyjuanalberto@gmail.es');

insert into informe values (1, '2021-05-24 11:58:12', '2022-05-24 12:00:12', 1, 1, 'apto', 6.1, 32.12, 'no apto', 'apto');
insert into informe values (2,'2021-11-24 11:57:12', '2022-05-24 12:00:12', 1, 1, 'apto', 8.1, 32.12, 'no apto', 'apto');



insert into trabajador values (4, 3, 'Fran', '689444545', 'fran@email.com', 'fran', md5('contrasega'), '2012-05-24 11:58:12', 'administracion', '1233.23', 1);
insert into trabajador values (3, 3, 'Pedro', '689345645', 'Pedro@email.com', 'pedro', md5( 'conasdaega'), '2012-05-24 11:01:12', 'administracion', '1233.23', 1);
insert into trabajador values (1, 3, 'pablo', '684345645', 'pablo@email.com', 'pablo', md5('conasasdaega'), '2012-05-24 11:01:12', 'motor', '1233.23', 1);
insert into trabajador values (2, 3, 'ivan', '689444345', 'ivan2@email.com', 'ivan', md5('contrRa'), '2012-05-24', 'motor', '1233.23', 1);

insert into vehiculo values (1, 'BBBB123', '33456789A','Renault', 'Supra', '2013-11-24', '2018-05-01 11:58', 'electrico', 'turismo');
insert into vehiculo values (2, 'JJKJ834', '33456789A','Citroen', 'Mega', '2022-09-12', '2012-05-04 12:00:00', 'diesel', 'camion');
insert into vehiculo values (3, 'KDLG531', '84718291N','Toyota', 'Super', '2021-02-19', '2014-05-16 13:30', 'hibrido', 'motocicleta');
insert into vehiculo values (4, 'LKJN581', '84718291N','Peugeot', 'Ultra', '2004-10-20', '2017-05-24 17:00', 'gasolina', 'turismo');

select * from trabajador;
select * from informe;
select * from estacionitv;
select * from vehiculo;
select * from propietario;
