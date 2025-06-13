package br.edu.ifes.mestrado.camunda.dao.interfaceDAO;

import java.sql.SQLException;

public interface IDefesaBancaDAO {
    int inserir(int idDefesa, int idBanca) throws SQLException;

}
