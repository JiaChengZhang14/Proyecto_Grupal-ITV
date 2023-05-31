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


/*Procedimientos y funciones del apartado de Bases de Datos del Proyecto ITV*/

use empresaitv;

-- PROCEDIMIENTO 1
delimiter $$
drop procedure if exists listWorkersByStation $$
create procedure listWorkersByStation(stationId INT)
begin
    declare l_last_row_fetched int;
    declare idTrabajador, idItv int;
    declare nombreTrabajador varchar(50);
    declare telefonoTrabajador char(9);
    declare c1 cursor for select id_trabajador, id_itv, nombre, telefono from trabajador where id_itv = stationId;
    declare continue handler for not found set l_last_row_fetched = 1;
    set l_last_row_fetched = 0;
    open c1;
    repeat
        fetch c1 into idTrabajador, idItv, nombreTrabajador, telefonoTrabajador;
        select idTrabajador, idItv, nombreTrabajador, telefonoTrabajador;
    until l_last_row_fetched = 1 end repeat;
    close c1;
end ;$$

-- PROCEDUIMIENTO 2
delimiter $$
drop procedure if exists insertarInformeCompleto $$
create procedure insertarInformeCompleto (idInforme int(11), fechaInicio text, fechaFin text, idVehiculo int(11), idTrabajador int(11), favorable text, frenado decimal(4,2), contaminacion decimal(4,2), interior text, luces text,
														dniPropietario char(9), nombrePropietario varchar(50), apellidosPropietario varchar(50), telefonoPropietario char(9), emailPropietario varchar(50),
														trabajadorIdItv int(11), nombreTrabajador varchar(50), telefonoTrabajador char(9), emailTrabajador varchar(50),
														nombreUsuario varchar(50), contraseñaUsuario varchar(50), fechaContratacion text, especialidadTrabajador varchar(20), salarioTrabajador decimal(6,2), idResponsable int(11),
														matriculaVehiculo char(7), marcaVehiculo varchar(50), modeloVehiculo varchar(50), fechaMatriculacionVehiculo text, fechaUltimaRevisionVehiculo text, tipoMotorVehiculo varchar(15), tipoVehiculoVehiculo varchar(15), IdItvPedida int(11))
    begin
    if(trabajadorIdItv = IdItvPedida) THEN
			if((SELECT count(*) from propietario where dni = dniPropietario) = 0) then
            INSERT INTO propietario values (dniPropietario, nombrePropietario, apellidosPropietario, telefonoPropietario, emailPropietario);
            else
            UPDATE propietario set nombre = nombrePropietario, apellidos = apellidosPropietario, telefono = telefonoPropietario, email = emailPropietario
            WHERE dni = dniPropietario;
            end if;

            if((SELECT count(*) from vehiculo where id_vehiculo = idVehiculo) = 0) then
            INSERT INTO vehiculo values (idVehiculo, matriculaVehiculo, dniPropietario, marcaVehiculo, modeloVehiculo, fechaMatriculacionVehiculo,
										fechaUltimaRevisionVehiculo, tipoMotorVehiculo, tipoVehiculoVehiculo);
            else
            UPDATE vehiculo set matricula = matriculaVehiculo, id_propietario = dniPropietario, marca = marcaVehiculo, modelo = modeloVehiculo,
								fecha_matriculacion = fechaMatriculacionVehiculo, fecha_ultima_revision = fechaUltimaRevisionVehiculo, tipo_motor = tipoMotorVehiculo,
                                tipo_vehiculo = tipoVehiculoVehiculo
            WHERE id_vehiculo = idVehiculo;
            end if;
            --
            if((SELECT count(*) from trabajador where id_trabajador = idTrabajador) = 0) then
            call insertTrabajadorEncriptandoContraseña(idTrabajador, trabajadorIdItv, nombreTrabajador, telefonoTrabajador, emailTrabajador,
													nombreUsuario, contraseñaUsuario, fechaContratacion, especialidadTrabajador, salarioTrabajador,
                                                    idResponsable);
            else
            call updateTrabajadorEncriptandoContraseña(idTrabajador, trabajadorIdItv, nombreTrabajador, telefonoTrabajador, emailTrabajador,
													nombreUsuario, contraseñaUsuario, fechaContratacion, especialidadTrabajador, salarioTrabajador,
                                                    idResponsable);
            end if;
            INSERT INTO informe values (idInforme, fechaInicio, fechaFin, idVehiculo, idTrabajador, favorable, frenado, contaminacion, interior, luces);
        end if;

    end $$

-- CREACION DE TABLA + TRIGGER

-- tabla que sirve como almacen de informes
create table if not exists informesBeforeUpdate(
	id_informe int(11),
    fecha_inicio DATETIME,
    fecha_final DATETIME,
    id_vehiculo  char(7),
    id_trabajador int,
    favorable text,
    frenado decimal(4,2),
    contaminacion decimal(4,2),
    interior text,
    luces text
);

-- Trigger que hace una guarda los datos antigüos de la tabla informe cada vez que se introduce un informe nuevo
delimiter $$
drop trigger if exists save_before_update $$
create trigger save_before_update after update on informe
    for each row
    begin
        insert into informesBeforeUpdate(id_informe, fecha_inicio, fecha_final, id_vehiculo, id_trabajador, favorable, frenado, contaminacion, interior, luces)
            values (old.id_informe, old.fecha_inicio, old.fecha_final, old.id_vehiculo, old.id_trabajador, old.favorable, old.frenado, old.contaminacion,
					old.interior, old.luces) ;
    end ;$$

-- EVENTO que hace un borrado de la tabla informes cada dos meses
delimiter $$
DROP EVENT IF EXISTS borrado_informes_bimestral $$
CREATE EVENT borrado_informes_bimestral ON SCHEDULE AT CURRENT_TIMESTAMP + INTERVAL 2 month
    DO
    BEGIN
    DELETE FROM informe;
    END; $$

set global event_scheduler = on;

-- Procedimiento que inserta un trabajador a la tabla trabajador y encripta su contraseña
delimiter $$
drop procedure if exists insertTrabajadorEncriptandoContraseña; $$
create procedure insertTrabajadorEncriptandoContraseña
(id_trabajador int(11), id_itv int(11), nombre varchar(50), telefono char(9), email varchar(50), nombre_usuario varchar(50), contraseña_usuario varchar(50), fecha_contratacion date,
especialidad varchar(20), salario decimal(6, 2), id_responsable int(11))
begin
    insert into trabajador values
    (id_trabajador, id_itv, nombre, telefono, email, nombre_usuario, MD5(contraseña_usuario), fecha_contratacion, especialidad, salario, id_responsable);
end; $$

-- Procedimiento que actualiza la tabla trabajador y encripta su contraseña
delimiter $$
drop procedure if exists updateTrabajadorEncriptandoContraseña; $$
create procedure updateTrabajadorEncriptandoContraseña
(id_trabajadorP int(11), id_itvP int(11), nombreP varchar(50), telefonoP char(9), emailP varchar(50), nombre_usuarioP varchar(50), contraseña_usuarioP varchar(50), fecha_contratacionP date,
especialidadP varchar(20), salarioP decimal(6, 2), id_responsableP int(11))
begin
    update trabajador set id_itv = id_itvP, nombre = nombreP, telefono = telefonoP, email = emailP, nombre_usuario = nombre_usuarioP,
    contraseña_usuario = MD5(contraseña_usuarioP), fecha_contratacion = fecha_contratacionP, especialidad = especialidadP, salario = salarioP, id_responsable = id_responsableP
    where id_trabajador = id_trabajadorP;
end; $$

-- Comprobacion de procedimientos
-- Procedimiento 1
call listWorkersByStation(3);

-- Procedimiento 2
call insertarInformeCompleto(4, '2021-11-24 11:57:11', '2022-05-24 12:00:12', 3, 4, 'apto', 9.10, 32.12, 'no apto', 'apto',
											'33456556J', 'Pedro', 'Marquez', '655409907', 'aasdasd@email.com',
											3, 'Julian', '677758432', 'aasdasd@email.com', 'julian', 'contrasenia', '2012-05-24', 'motor', 1233.50, 1,
											'SSSS222', 'Opel', '"Corsa', '2012-12-24', '2017-05-24 11:58:21', 'diesel', 'turismo', 3
											);
-- para el trigger
update informe set luces = 'no apto' where id_informe = 4;
select * from informesbeforeupdate;