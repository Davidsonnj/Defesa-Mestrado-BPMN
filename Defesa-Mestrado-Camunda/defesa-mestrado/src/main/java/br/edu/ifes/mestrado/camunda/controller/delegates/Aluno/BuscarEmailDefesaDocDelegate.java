package br.edu.ifes.mestrado.camunda.controller.delegates.Aluno;

import br.edu.ifes.mestrado.emailAPI.controller.EmailController;
import br.edu.ifes.mestrado.emailAPI.model.Email;
import br.edu.ifes.mestrado.emailAPI.service.MarkEmail;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import java.util.List;

public class BuscarEmailDefesaDocDelegate implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution){
        if(execution.hasVariable("verificaEmail2")) {
            EmailController emailController = new EmailController();
            MarkEmail markEmail = new MarkEmail();
            Boolean recebeuEmail = false;

            String titulo_trabalho = (String) execution.getVariable("titulo_trabalho");
            String emailAluno = (String) execution.getVariable("emailAluno");
            String aluno = (String) execution.getVariable("aluno");

            String subject = String.format("Dissertação da Defesa - %s", titulo_trabalho);

            List<Email> emailConfirmacao = emailController.emails(subject, null, emailAluno);
            StringBuilder paths = new StringBuilder();

            for (Email email : emailConfirmacao) {
                List<String> anexos = email.getAttachmentPaths();
                if (anexos != null && !anexos.isEmpty()) {
                    for (String path : anexos) {
                        System.out.println("📎 Anexo encontrado: " + path);
                        paths.append(path).append(";"); // separa os paths com ponto e vírgula, por exemplo
                    }
                }
            }
            if(!paths.isEmpty()){
                execution.setVariable("caminhosDosAnexos", paths.toString());
            }else{
                System.out.println("PDF de dissertação não foi enviado.");
            }

            if (!emailConfirmacao.isEmpty()) {
                recebeuEmail = true;
                for(Email email : emailConfirmacao) {
                    markEmail.markEmailAsRead(email.getUid());
                }
            } else {
                recebeuEmail = false;
            }
            execution.setVariable("recebeuEmail2", recebeuEmail);
        } else{
            execution.setVariable("verificaEmail2", 1);
            System.out.println("Passou ao verificar o email de Docs do Aluno.");
        }
    }
}
