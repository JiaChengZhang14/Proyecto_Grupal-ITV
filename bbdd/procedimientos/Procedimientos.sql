/*crea un procedimiento que liste trabajadores de la estacion que le entre por parametro */

use empresaitv;
-- PROCEDURE 2
delimiter $$
drop procedure if exists listWorkersByStation $$
create procedure listWorkersByStation(stationId INT)
begin
    declare l_last_row_fetched int;
    declare id int;
    declare c1 cursor for select * from trabajador where id_itv = stationId;
    declare continue handler for not found set l_last_row_fetched = 1;
    set l_last_row_fetched = 0;
    open c1;
    bucle: LOOP
        fetch  c1 into id;
        if l_last_row_fetched =1 then leave bucle;
        end if;
    end loop bucle;
    close c1;
    select * from Trabajador where id_trabajador = id;
end ;$$

-- estacion 3
/*Hay que llamar al procedimiento anterior, para saber si el trabajador es de esa estacion y cargar los datos en inspeccion */

-- PROCEDURE 2
delimiter $$
drop procedure if exists comprobarSiPerteneceANuestraEstacion $$
create procedure comprobarSiPerteneceANuestraEstacion(fechaInicio date, fechaFin date, idVehiculo char(7), idTrabajador int)
    begin
        call listWorkersByStation(3);
        if(idTrabajador IN (listWorkersByStation(3))) THEN
            INSERT INTO citas values (fechaInicio, fechaFin, idVehiculo, idTrabajador);
        end if;
    end $$

/*Controla, mediante un disparador, si se actualiza alguna inspección guardando la
información previa y la información que se ha modificado.*/


-- CREAR TABLA + TRIGGER
create table citasBeforeUpdate(
    fecha_inicio DATETIME,
    fecha_final DATETIME,
    id_vehiculo  char(7),
    id_trabajador int
);
delimiter $$
drop trigger if exists saveBeforeUpdate $$
create trigger saveBeforeUpdate after update on citas
    for each row
    begin
        insert into citasBeforeUpdate(fecha_inicio,fecha_final,id_vehiculo,id_trabajador )
            values (old.fecha_inicio, old.fecha_final, old.id_vehiculo, old.id_trabajador) ;
    end ;$$

-- EVENTO
delimiter $$
DROP EVENT IF EXISTS borrado_citas_bimestral $$
CREATE EVENT borrado_citas_bimestral ON SCHEDULE AT CURRENT_TIMESTAMP + INTERVAL 2 month
    DO
    BEGIN
    DELETE FROM citas;
    END; $$


/**/