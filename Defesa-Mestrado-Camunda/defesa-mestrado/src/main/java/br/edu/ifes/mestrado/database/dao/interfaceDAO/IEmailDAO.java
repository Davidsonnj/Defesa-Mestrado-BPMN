package br.edu.ifes.mestrado.database.dao.interfaceDAO;

import br.edu.ifes.mestrado.emailAPI.model.Email;

import java.util.List;

public interface IEmailDAO {
    void insert(Email email);
    List<Email> findAll();
    void update(Email email);
}
