package br.edu.ifes.mestrado.database.dao.interfaceDAO;

import java.sql.SQLException;

public interface IDefesaEmailDAO {
    int  inserir(int idDefesa, long idEmail) throws SQLException;

}
