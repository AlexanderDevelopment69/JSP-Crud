package com.java.dao;

import com.java.Cliente;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAOImpl implements ClienteDAO {

    private final Connection conexion;

    public ClienteDAOImpl(Connection conexion) {
        this.conexion = conexion;
    }

    @Override
    public void agregarCliente(Cliente cliente) {

            String procedimientoAlmacenado = "{call AgregarCliente(?, ?, ?, ?, ?)}";
            try (CallableStatement cstmt = conexion.prepareCall(procedimientoAlmacenado)) {
                cstmt.setString(1, cliente.getNombre());
                cstmt.setString(2, cliente.getApellido());
                cstmt.setString(3, cliente.getEmail());
                cstmt.setString(4, cliente.getTelefono());
                cstmt.setDate(5, java.sql.Date.valueOf(cliente.getFechaRegistro()));
                cstmt.execute();
                System.out.println("Cliente agregado en la base de datos utilizando un procedimiento almacenado.");

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error al agregar el cliente en la base de datos.");
        }
    }

    @Override
    public boolean eliminarClientePorCodigo(int codigo) {

            String procedimientoAlmacenado = "{call EliminarClientePorCodigo(?)}";
            try (CallableStatement cstmt = conexion.prepareCall(procedimientoAlmacenado)) {
                cstmt.setInt(1, codigo);
                cstmt.execute();
                System.out.println("Cliente eliminado en la base de datos utilizando un procedimiento almacenado.");

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error al eliminar el cliente en la base de datos.");
        }
        return true;
    }

    @Override
    public void actualizarCliente(Cliente cliente) {

            String procedimientoAlmacenado = "{call ActualizarCliente(?, ?, ?, ?, ?, ?)}";
            try (CallableStatement cstmt = conexion.prepareCall(procedimientoAlmacenado)) {
                cstmt.setInt(1, cliente.getCodigo());
                cstmt.setString(2, cliente.getNombre());
                cstmt.setString(3, cliente.getApellido());
                cstmt.setString(4, cliente.getEmail());
                cstmt.setString(5, cliente.getTelefono());
                cstmt.setDate(6, java.sql.Date.valueOf(cliente.getFechaRegistro()));
                cstmt.execute();
                System.out.println("Cliente actualizado en la base de datos utilizando un procedimiento almacenado.");

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error al actualizar el cliente en la base de datos.");
        }
    }



    @Override
    public List<Cliente> buscarClientesPorCodigoNombreApellido(String nombre) {
        List<Cliente> listaClientes = new ArrayList<>();
            String procedimientoAlmacenado = "{call BuscarCliente(?)}";
            try (CallableStatement cstmt = conexion.prepareCall(procedimientoAlmacenado)) {
                cstmt.setString(1, nombre);
                try (ResultSet rs = cstmt.executeQuery()) {
                    while (rs.next()) {
                        // Crear objetos Cliente y agregarlos a la lista
                        Cliente cliente = new Cliente(
                                rs.getInt("codigo"),
                                rs.getString("nombre"),
                                rs.getString("apellido"),
                                rs.getString("email"),
                                rs.getString("telefono"),
                                rs.getDate("fechaRegistro").toLocalDate()
                        );
                        listaClientes.add(cliente);
                    }
                }
            }
         catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error al listar clientes por nombre desde la base de datos.");
        }
        return listaClientes;
    }



    @Override
    public List<Cliente> listarTodosLosClientes() {
        List<Cliente> listaClientes = new ArrayList<>();

            String procedimientoAlmacenado = "{call ListarClientes()}";
            try (CallableStatement cstmt = conexion.prepareCall(procedimientoAlmacenado)) {
                try (ResultSet rs = cstmt.executeQuery()) {
                    while (rs.next()) {
                        // Crear objetos Cliente y agregarlos a la lista
                        Cliente cliente = new Cliente(
                                rs.getInt("codigo"),
                                rs.getString("nombre"),
                                rs.getString("apellido"),
                                rs.getString("email"),
                                rs.getString("telefono"),
                                rs.getDate("fechaRegistro").toLocalDate()
                        );
                        listaClientes.add(cliente);
                    }
                }
            }
        catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error al listar todos los clientes desde la base de datos.");
        }
        return listaClientes;
    }
}
