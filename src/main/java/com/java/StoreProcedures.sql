CREATE DATABASE ventas;
USE ventas;

CREATE TABLE clientes (
                          codigo INT AUTO_INCREMENT PRIMARY KEY,
                          nombre VARCHAR(255),
                          apellido VARCHAR(255),
                          email VARCHAR(255),
                          telefono VARCHAR(15),
                          fechaRegistro DATE
);


/*PROCEDURES*/
CREATE PROCEDURE AgregarCliente(
    IN p_nombre VARCHAR(255),
    IN p_apellido VARCHAR(255),
    IN p_email VARCHAR(255),
    IN p_telefono VARCHAR(15),
    IN p_fechaRegistro DATE
)
BEGIN
INSERT INTO clientes (nombre, apellido, email, telefono, fechaRegistro)
VALUES (p_nombre, p_apellido, p_email, p_telefono, p_fechaRegistro);
END;


CREATE PROCEDURE ActualizarCliente(
    IN p_codigo INT,
    IN p_nombre VARCHAR(255),
    IN p_apellido VARCHAR(255),
    IN p_email VARCHAR(255),
    IN p_telefono VARCHAR(15),
    IN p_fechaRegistro DATE
)
BEGIN
UPDATE clientes
SET
    nombre = p_nombre,
    apellido = p_apellido,
    email = p_email,
    telefono = p_telefono,
    fechaRegistro = p_fechaRegistro
WHERE codigo = p_codigo;
END;



CREATE PROCEDURE BuscarCliente(
    IN filtro VARCHAR(255)
)
BEGIN
SELECT * FROM clientes
WHERE codigo LIKE CONCAT('%', filtro, '%')
   OR nombre LIKE CONCAT('%', filtro, '%')
   OR apellido LIKE CONCAT('%', filtro, '%');
END;



CREATE PROCEDURE EliminarClientePorCodigo(IN p_codigo INT)
BEGIN
DELETE FROM clientes WHERE codigo = p_codigo;
END ;




CREATE PROCEDURE ListarClientes()
BEGIN
SELECT * FROM clientes;
END;
