drop database if exists EmpresaItvParaTest;
create database if not exists EmpresaItvParaTest;
use empresaItvParaTest;

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
    fecha_contratacion date not null check (fecha_contratacion regexp "[0-9]{4}-[0-9]{2}-[0-9]{2}"),
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
	matricula char(7) not null unique check (matricula regexp '^[BCDFGHJKLMNPRSTVWXYZ]{4}[0-9]{3}'),
    id_propietario char(9) not null references propietario(dni) on delete restrict on update cascade,
    marca varchar(50) not null check (marca <> ""),
    modelo varchar(50) not null check (modelo <> ""),
    fecha_matriculacion date not null check (fecha_matriculacion regexp "[0-9]{4}-[0-9]{2}-[0-9]{2}"),
    fecha_ultima_revision text not null check (fecha_ultima_revision regexp "[0-9]{4}-[0-9]{2}-[0-9]{2}[[:space:]][0-9]{2}:[0-9]{2}(:[0-9]{2})?$" || fecha_ultima_revision regexp "[0-9]{4}-[0-9]{2}-[0-9]{2}T[0-9]{2}:[0-9]{2}(:[0-9]{2})?$" || fecha_ultima_revision = ""),
    tipo_motor varchar(15) not null check (tipo_motor regexp '^(gasolina|electrico|diesel|hibrido|otro)$'),
    tipo_vehiculo varchar(15) not null check (tipo_vehiculo regexp '^(turismo|furgoneta|camion|motocicleta|otro)$')
);

create table if not exists Informe(
	id_informe int not null auto_increment primary key,
	fecha_inicio text not null check (fecha_inicio regexp "[0-9]{4}-[0-9]{2}-[0-9]{2}[[:space:]][0-9]{2}:[0-9]{2}(:[0-9]{2})?$" || fecha_inicio regexp "[0-9]{4}-[0-9]{2}-[0-9]{2}T[0-9]{2}:[0-9]{2}(:[0-9]{2})?$"),
    fecha_final text not null check ((fecha_final regexp "[0-9]{4}-[0-9]{2}-[0-9]{2}[[:space:]][0-9]{2}:[0-9]{2}(:[0-9]{2})?$" || fecha_final regexp "[0-9]{4}-[0-9]{2}-[0-9]{2}T[0-9]{2}:[0-9]{2}(:[0-9]{2})?$") && fecha_final > fecha_inicio),
    id_vehiculo int not null references vehiculo(id_vehiculo) on delete restrict on update cascade,
    id_trabajador int not null references trabajador(id_trabajador) on delete restrict on update cascade,
    favorable text not null check (favorable regexp '^(apto|no apto)$' || favorable = ""),
    frenado numeric(4,2) check (frenado >= 0.0 and frenado <= 10.0),
    contaminacion numeric(4,2) check (contaminacion >= 20.0 and contaminacion <= 50.0),
    interior text not null  check (interior regexp '^(apto|no apto)$' || interior = ""),
    luces text not null  check (luces regexp '^(apto|no apto)$' || luces = "")
);

-- Insertar trabajador 1
INSERT INTO trabajador (id_trabajador, id_itv, nombre, telefono, email, nombre_usuario, contraseña_usuario, fecha_contratacion, especialidad, salario, id_responsable)
VALUES (1, 1, 'Juan Pérez', '123456789', 'juan@example.com', 'juancito', 'contraseña1', '2023-5-27', 'administracion', 1650.00, 1);

-- Insertar trabajador 2
INSERT INTO trabajador (id_trabajador, id_itv, nombre, telefono, email, nombre_usuario, contraseña_usuario, fecha_contratacion, especialidad, salario, id_responsable)
VALUES (2, 1, 'María López', '987654321', 'maria@example.com', 'marialo', 'contraseña2', '2023-5-27', 'mecanica', 1600.00, 1);

INSERT INTO Informe (fecha_inicio, fecha_final, id_vehiculo, id_trabajador, favorable, frenado, contaminacion, interior, luces)
VALUES ('2023-05-01 09:00:00', '2023-05-01 10:30:00', 1, 1, 'apto', 8.5, 35.0, 'apto', 'apto');


INSERT INTO Informe (fecha_inicio, fecha_final, id_vehiculo, id_trabajador, favorable, frenado, contaminacion, interior, luces)
VALUES ('2023-05-02 14:00:00', '2023-05-02 15:30:00', 2, 2, 'no apto', 6.0, 40.0, 'no apto', 'apto');

-- Insertar vehículo 1
INSERT INTO Vehiculo (matricula, id_propietario, marca, modelo, fecha_matriculacion, fecha_ultima_revision, tipo_motor, tipo_vehiculo)
VALUES ('FBCD123', '12345678A', 'Ford', 'Focus', '2020-01-01', '2023-05-01 09:00:00', 'gasolina', 'turismo');

-- Insertar vehículo 2
INSERT INTO Vehiculo (matricula, id_propietario, marca, modelo, fecha_matriculacion, fecha_ultima_revision, tipo_motor, tipo_vehiculo)
VALUES ('WXYZ987', '98765432B', 'Volkswagen', 'Golf', '2018-06-15', '2023-05-02 14:00:00', 'diesel', 'turismo');


-- Insertar propietario 1
INSERT INTO Propietario (dni, nombre, apellidos, telefono, email)
VALUES ('12345678A', 'Juan', 'Pérez', '123456789', 'juan@example.com');

-- Insertar propietario 2
INSERT INTO Propietario (dni, nombre, apellidos, telefono, email)
VALUES ('98765432B', 'María', 'López', '987654321', 'maria@example.com');



use empresaitv

