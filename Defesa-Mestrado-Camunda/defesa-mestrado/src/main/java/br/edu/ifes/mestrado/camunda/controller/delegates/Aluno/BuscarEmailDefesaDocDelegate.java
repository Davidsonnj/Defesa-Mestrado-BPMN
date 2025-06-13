package br.edu.ifes.mestrado.camunda.controller.delegates.Aluno;

import br.edu.ifes.mestrado.GenAI.pergunta.implementacoes.PerguntaValidacaoDoc;
import br.edu.ifes.mestrado.database.dao.implementations.EmailDAO;
import br.edu.ifes.mestrado.emailAPI.controller.EmailController;
import br.edu.ifes.mestrado.emailAPI.controller.FuncoesEmail;
import br.edu.ifes.mestrado.emailAPI.model.Email;
import br.edu.ifes.mestrado.emailAPI.service.MarkEmail;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class BuscarEmailDefesaDocDelegate implements JavaDelegate {

    @Autowired
    EmailDAO emailDAO;

    @Autowired
    PerguntaValidacaoDoc perguntaValidacaoDoc;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        if(execution.hasVariable("verificaEmail2")) {
            Boolean recebeuEmail = false;

            String titulo_trabalho = (String) execution.getVariable("titulo_trabalho");
            String emailAluno = (String) execution.getVariable("emailAluno");

            List<Email> emails = emailDAO.findAll();
            System.out.println("Verificando se existem doc da dissertação " + titulo_trabalho);
            int cont = 0;
            for (Email email : emails) {

                Map.Entry<String, String> resultado = FuncoesEmail.tratarEmailSender(email);
                String emailAlunoBD =  resultado.getValue();

                if(email.getStatus().equals("DOCUMENTO_DISSERTACAO") && emailAluno.equals(emailAlunoBD)) {

                    String txt = "Titulo do trabalho no sistema: " + titulo_trabalho + "Subject: " + email.getSubject() + "Body: " + email.getBody();
                    boolean validacao = perguntaValidacaoDoc.booleanTakeQuestion(txt);

                    if(validacao) {

                        if (!email.getAttachmentPaths().isEmpty()) {
                            String caminhoFormatado = email.getAttachmentPaths().toString().replace("[", "").replace("]", "");
                            /* Testar Amanha
                            List<String> anexos = email.getAttachmentPaths();
                            if (anexos != null && !anexos.isEmpty()) {
                                for (String path : anexos) {
                                    System.out.println("📎 Anexo encontrado: " + path);
                                    paths.append(path).append(";"); // separa os paths com ponto e vírgula, por exemplo
                                }
                        }
                             */
                            execution.setVariable("caminhosDosAnexos", caminhoFormatado);

                        } else {
                            System.out.println("PDF de dissertação não foi enviado.");
                        }

                        cont++;
                        email.setStatus("PROCESSADO");
                        emailDAO.update(email);
                    }
                }
            }
            if(cont > 0){
                recebeuEmail = true;
            }
            execution.setVariable("recebeuEmail2", recebeuEmail);
        } else{
            execution.setVariable("verificaEmail2", 1);
            System.out.println("Passou ao verificar o email de Docs do Aluno.");
        }
    }
}
