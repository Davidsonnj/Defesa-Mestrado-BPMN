package br.edu.ifes.mestrado.camunda.controller.delegates.Aluno;

import br.edu.ifes.mestrado.GenAI.pergunta.implementacoes.PerguntaConfirmacaoDefesa;
import br.edu.ifes.mestrado.database.dao.implementations.EmailDAO;
import br.edu.ifes.mestrado.emailAPI.controller.FuncoesEmail;
import br.edu.ifes.mestrado.emailAPI.model.Email;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class BuscarEmailDefesaDelegate implements JavaDelegate {

    @Autowired
    private PerguntaConfirmacaoDefesa perguntaConfirmacaoDefesa;
    @Autowired
    private EmailDAO emailDAO;

    @Override
    public void execute(DelegateExecution execution) throws Exception {

        if(execution.hasVariable("verificaEmail")) {
            Boolean recebeuEmail = false;

            String titulo_trabalho = (String) execution.getVariable("titulo_trabalho");
            String emailAluno = (String) execution.getVariable("emailAluno");
            String aluno = (String) execution.getVariable("aluno");

            System.out.println("Verificando email do aluno: " + aluno + "Email: " + emailAluno);
            List<Email> emailConfirmacao = emailDAO.findAll();

            if (!emailConfirmacao.isEmpty()) {
                int cont = 0;
                for(Email email : emailConfirmacao) {
                    Map.Entry<String, String> resultado = FuncoesEmail.tratarEmailSender(email);
                    String emailAlunoBD =  resultado.getValue();

                    if(email.getStatus().equals("CONFIRMACAO_DEFESA") && emailAlunoBD.equals(emailAluno) ) {

                        String txt = "Titulo do sistema:" + titulo_trabalho +"Corpo do Email:" + email.getBody();
                        boolean titulo_igual = perguntaConfirmacaoDefesa.booleanTakeQuestion(txt);

                        if(titulo_igual) {
                            email.setStatus("PROCESSADO");
                            emailDAO.update(email);
                            cont++;
                        }

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