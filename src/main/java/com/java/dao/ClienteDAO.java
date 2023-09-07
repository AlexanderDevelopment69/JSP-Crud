package com.java.dao;

import com.java.Cliente;

import java.util.List;

public interface ClienteDAO {
    void agregarCliente(Cliente cliente);

    boolean eliminarClientePorCodigo(int codigo);

    void actualizarCliente(Cliente cliente);

    List<Cliente> buscarClientesPorCodigoNombreApellido(String nombre);

    List<Cliente> listarTodosLosClientes();
}
