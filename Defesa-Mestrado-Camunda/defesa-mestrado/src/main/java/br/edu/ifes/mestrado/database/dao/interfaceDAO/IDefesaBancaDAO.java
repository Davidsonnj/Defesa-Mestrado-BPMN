package br.edu.ifes.mestrado.database.dao.interfaceDAO;

import java.sql.SQLException;

public interface IDefesaBancaDAO {
    int inserir(int idDefesa, int idBanca) throws SQLException;

}
