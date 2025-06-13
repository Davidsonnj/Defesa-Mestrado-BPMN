package br.edu.ifes.mestrado.emailAPI.service;

import br.edu.ifes.mestrado.GenAI.pergunta.implementacoes.PerguntaDadosIniciais;
import br.edu.ifes.mestrado.database.dao.implementations.EmailDAO;
import br.edu.ifes.mestrado.emailAPI.controller.EmailController;
import br.edu.ifes.mestrado.emailAPI.controller.FuncoesEmail;
import br.edu.ifes.mestrado.emailAPI.model.Email;
import br.edu.ifes.mestrado.emailAPI.model.ExtrairDadosEmail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class EmailChecker {

    private final CamundaRequester camundaRequester;
    private final PerguntaDadosIniciais perguntaDadosIniciais;

    @Autowired
    public EmailChecker(CamundaRequester camundaRequester, PerguntaDadosIniciais perguntaDadosIniciais) {
        this.camundaRequester = camundaRequester;
        this.perguntaDadosIniciais = perguntaDadosIniciais;
    }

    @Scheduled(fixedDelay = 60000)
    public void verificarEmails() {
        System.out.println("Verificando emails...");
        EmailController emailController = new EmailController();
        MarkEmail markEmail = new MarkEmail();

        EmailDAO emailDAO = new EmailDAO();
        List<Email> emailConfirmacao = emailDAO.findAll();

        if (!emailConfirmacao.isEmpty()) {
            for (Email email : emailConfirmacao) {
                if(email.getStatus().equals("DADOS_INICIAIS")) {
                    System.out.println("Email encontrado: " + email.getSubject() + " - " + email.getSender());

                    Map.Entry<String, String> resultado = FuncoesEmail.tratarEmailSender(email);

                    String nome = resultado.getKey();
                    String emailOrientador = resultado.getValue();

                    String body = email.getBody();
                    String resposta = null;
                    try {
                        resposta = perguntaDadosIniciais.takeQuestion(body);
                    } catch (Exception e) {
                        System.err.println("Erro ao processar takeQuestion: " + e.getMessage());
                        continue;
                    }
                    System.out.println("Resposta: " + resposta);

                    if (resposta == null) {
                        System.out.println("Resposta é nula, pulando esse email.");
                        continue;
                    }

                    String respostaLimpa = resposta.replaceAll("[\\n\\r]", " ")
                            .replaceAll("\\s+", " ")
                            .replace("'", "")
                            .trim();

                    ExtrairDadosEmail.DadosExtraidos dados = ExtrairDadosEmail.extrairDados(respostaLimpa);

                    if (dados != null) {
                        String aluno = dados.aluno;
                        String emailAluno = dados.email;
                        String titulo_trabalho = dados.titulo;

                        boolean camundaResquest = camundaRequester.iniciarProcesso(aluno, titulo_trabalho, emailAluno, emailOrientador);
                        if (camundaResquest) {
                            System.out.println("Requisição enviada ao Camunda com sucesso!");
                            emailController.sendEmail(emailOrientador, "\"Dados Extraídos com Sucesso\"\n",
                                    "Prezado(a),"
                                            + "\n\nOs dados foram extraídos com sucesso e o processo foi iniciado."
                                            + "\n\nAgradecemos a colaboração."
                                            + "\n\nAtenciosamente,"
                                            + "\nPPComp - Programa de Pós-Graduação em Computação");
                            // markEmail.markEmailAsRead(email.getUid());
                            email.setStatus("PROCESSADO");
                            emailDAO.update(email);
                        } else {
                            System.out.println("Erro ao enviar requisição para o Camunda.");
                        }
                    } else {
                        System.out.println("Dados não encontrados no email.");
                        emailController.sendEmail(emailOrientador, "\"Formato de Dados Incorreto para Cadastro de Defesa\"\n",
                                "Prezado(a),"
                                        + "\n\nNão foi possível extrair os dados do e-mail enviado. Para que o cadastro do trabalho seja realizado corretamente, envie os seguintes dados no corpo do e-mail:"
                                        + "\naluno: Nome do Aluno"
                                        + "\nemail: email@exemplo.com"
                                        + "\ntitulo: Título do Trabalho"
                                        + "\n\nAgradecemos a colaboração."
                                        + "\n\nAtenciosamente,"
                                        + "\nPPComp - Programa de Pós-Graduação em Computação");

                        // markEmail.markEmailAsRead(email.getUid());
                        email.setStatus("PROCESSADO");
                        emailDAO.update(email);
                    }
                }
            }
        } else {
            System.out.println("Nenhum email encontrado com o status DADOS_INICIAIS");
        }
    }
}
