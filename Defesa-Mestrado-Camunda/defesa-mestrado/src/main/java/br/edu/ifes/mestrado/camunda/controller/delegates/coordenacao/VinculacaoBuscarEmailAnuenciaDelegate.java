package br.edu.ifes.mestrado.camunda.controller.delegates.coordenacao;

import br.edu.ifes.mestrado.GenAI.pergunta.implementacoes.PerguntaAnuencia;
import br.edu.ifes.mestrado.GenAI.pergunta.implementacoes.PerguntaValidacaoAnuencia;
import br.edu.ifes.mestrado.GenAI.pergunta.implementacoes.VinculacaoPerguntaAnuencia;
import br.edu.ifes.mestrado.GenAI.pergunta.implementacoes.VinculacaoPerguntaValidacaoAnuencia;
import br.edu.ifes.mestrado.database.dao.implementations.EmailDAO;
import br.edu.ifes.mestrado.emailAPI.controller.EmailController;
import br.edu.ifes.mestrado.emailAPI.controller.FuncoesEmail;
import br.edu.ifes.mestrado.emailAPI.model.Email;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Component
public class VinculacaoBuscarEmailAnuenciaDelegate implements JavaDelegate{
    @Autowired
    private EmailDAO emailDAO;

    @Autowired
    private VinculacaoPerguntaAnuencia VinculacaoPerguntaAnuencia;

    @Autowired
    private VinculacaoPerguntaValidacaoAnuencia vinculacaoPerguntaValidacaoAnuencia;

    private static final Logger LOGGER = LoggerFactory.getLogger(BuscarEmailAnuenciaDelegate.class);

    @Override
    public void execute(DelegateExecution execution) throws Exception {

        if(execution.hasVariable("verificaEmail")) {
            EmailController emailController = new EmailController();
            Boolean recebeuEmail = false;

            LOGGER.info("Verificando o email da coordenação...");
            List<Email> emailConfirmacao = emailDAO.findAll();

            for (Email email : emailConfirmacao) {

                if(email.getStatus().equals("ANUENCIA_COORDENACAO")) {

                    // Envio do id ANUENCIA_COORDENACAO do email para o camunda
                    long idAnuenciaCoordenacao = email.getUid();
                    execution.setVariable("idAnuenciaCoordenacao", idAnuenciaCoordenacao);

                    String txt = "Subject: " + email.getSubject() + "Body: " + email.getBody();
                    boolean validacao = vinculacaoPerguntaValidacaoAnuencia.booleanTakeQuestion(txt);

                    if(validacao) {
                        recebeuEmail = true;
                        execution.setVariable("recebeuEmail", recebeuEmail);

                        String resposta = VinculacaoPerguntaAnuencia.anuenciaQuestion(txt);
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
            LOGGER.info("Passou ao verificar o email da Coordenacao.");
        }
    }
}

