package com.java.factory;

import com.java.factory.MySQL.ConexionDB;
import com.java.dao.ClienteDAO;
import com.java.dao.ClienteDAOImpl;

import java.sql.Connection;

public class MySQLDAOFactory implements DAOFactory {

    private Connection conexion;

    // Constructor privado para evitar instancias no deseadas
    public MySQLDAOFactory() {
        // Puedes agregar aquí la lógica de configuración de la fuente de datos si es necesario
    }


    @Override
    public ClienteDAO getClienteDAO() {
        // En esta parte, puedes crear una instancia de MySQLClienteDAO
        // y pasar la conexión obtenida de ConexionDB
        conexion = ConexionDB.conectar(); // Obtiene la conexión
        return new ClienteDAOImpl(conexion); // Crea el DAO con la conexión
    }

}