package br.edu.ifes.mestrado.camunda.controller.delegates.coordenacao;

import br.edu.ifes.mestrado.GenAI.pergunta.implementacoes.PerguntaAnuencia;
import br.edu.ifes.mestrado.GenAI.pergunta.implementacoes.PerguntaValidacaoAnuencia;
import br.edu.ifes.mestrado.database.dao.implementations.EmailDAO;
import br.edu.ifes.mestrado.emailAPI.controller.EmailController;
import br.edu.ifes.mestrado.emailAPI.controller.FuncoesEmail;
import br.edu.ifes.mestrado.emailAPI.model.Email;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class BuscarEmailAnuenciaDelegate implements JavaDelegate {

    @Autowired
    private EmailDAO emailDAO;

    @Autowired
    private PerguntaAnuencia perguntaAnuencia;

    @Autowired
    private PerguntaValidacaoAnuencia perguntaValidacaoAnuencia;

    @Override
    public void execute(DelegateExecution execution) throws Exception {

        if(execution.hasVariable("verificaEmail")) {
            EmailController emailController = new EmailController();
            Boolean recebeuEmail = false;

            String titulo_trabalho = (String) execution.getVariable("titulo_trabalho");

            System.out.println("Verificando o email da coordenação...");
            List<Email> emailConfirmacao = emailDAO.findAll();

            for (Email email : emailConfirmacao) {

                Map.Entry<String, String> resultado = FuncoesEmail.tratarEmailSender(email);
                String emailCoordenacaoBD =  resultado.getValue();

                if(email.getStatus().equals("ANUENCIA_COORDENACAO")) {

                    String txt = "Subject: " + email.getSubject() + "Body: " + email.getBody() + "Titulo da dissertação no sistema: " + titulo_trabalho;
                    boolean validacao = perguntaValidacaoAnuencia.booleanTakeQuestion(txt);

                    if(validacao) {
                        recebeuEmail = true;
                        execution.setVariable("recebeuEmail", recebeuEmail);

                        String resposta = perguntaAnuencia.anuenciaQuestion(txt);
                        System.out.println(resposta);

                        if (resposta.equals("autorizado")) {
                            execution.setVariable("anuencia", true);
                        } else {
                            execution.setVariable("anuencia", false);
                            execution.setVariable("justificativaAnuencia", resposta);
                        }
                        email.setStatus("PROCESSADO");
                        emailDAO.update(email);
                    }
                }
            }
        } else{
            execution.setVariable("verificaEmail", 1);
            System.out.println("Passou ao verificar o email da Coordenacao.");
        }
    }
}