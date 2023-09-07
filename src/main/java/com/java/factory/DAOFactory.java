package com.java.factory;

import com.java.dao.ClienteDAO;

public interface DAOFactory {
    ClienteDAO getClienteDAO();
}
