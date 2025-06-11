package br.edu.ifes.mestrado.camunda.controller.delegates.Aluno;

import br.edu.ifes.mestrado.database.dao.implementations.EmailDAO;
import br.edu.ifes.mestrado.emailAPI.controller.EmailController;
import br.edu.ifes.mestrado.emailAPI.controller.FuncoesEmail;
import br.edu.ifes.mestrado.emailAPI.model.Email;
import br.edu.ifes.mestrado.emailAPI.service.MarkEmail;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import java.util.List;
import java.util.Map;

public class BuscarEmailDefesaDelegate implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution) throws Exception {

        if(execution.hasVariable("verificaEmail")) {
            EmailController emailController = new EmailController();
            EmailDAO emailDAO = new EmailDAO();
            Boolean recebeuEmail = false;

            String titulo_trabalho = (String) execution.getVariable("titulo_trabalho");
            String emailAluno = (String) execution.getVariable("emailAluno");
            String aluno = (String) execution.getVariable("aluno");

            System.out.println("Verificando se existem emails do " + aluno);
            List<Email> emailConfirmacao = emailDAO.findAll();

            if (!emailConfirmacao.isEmpty()) {
                int cont = 0;
                for(Email email : emailConfirmacao) {
                    Map.Entry<String, String> resultado = FuncoesEmail.tratarEmailSender(email);
                    String emailAlunoBD =  resultado.getValue();

                    if(email.getStatus().equals("CONFIRMACAO_DEFESA") && emailAlunoBD.equals(emailAluno) ) {
                        email.setStatus("PROCESSADO");
                        emailDAO.update(email);
                        cont++;
                    }
                }
                if(cont > 0) {
                    recebeuEmail = true;
                }
            } else {
                recebeuEmail = false;
            }
            execution.setVariable("recebeuEmail", recebeuEmail);
        } else{
            execution.setVariable("verificaEmail", 1);
            System.out.println("Passou ao verificar o email do Aluno.");
        }
    }
}