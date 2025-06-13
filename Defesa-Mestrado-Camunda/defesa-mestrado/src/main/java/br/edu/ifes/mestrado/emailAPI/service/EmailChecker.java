package br.edu.ifes.mestrado.emailAPI.service;

import br.edu.ifes.mestrado.GenAI.pergunta.implementacoes.PerguntaDadosIniciais;
import br.edu.ifes.mestrado.GenAI.pergunta.interfaces.PromptPergunta;
import br.edu.ifes.mestrado.emailAPI.controller.EmailController;
import br.edu.ifes.mestrado.emailAPI.model.Email;
import br.edu.ifes.mestrado.emailAPI.model.ExtrairDadosEmail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        EmailController emailController = new EmailController();
        MarkEmail markEmail = new MarkEmail();

        String subject = "Defesa";
        System.out.println("Verificando o email do aluno...");
        List<Email> emailConfirmacao = emailController.emails(subject, null, null);

        if (!emailConfirmacao.isEmpty()) {
            for (Email email : emailConfirmacao) {
                System.out.println("Email encontrado: " + email.getSubject() + " - " + email.getSender());

                String emailOrientador = email.getSender();

                // Trata o email do orientador (Pega o nome e o email
                String regex = "^(.*?)\\s<([^<>@\\s]+@[^<>@\\s]+)>";
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(emailOrientador);

                if (matcher.find()) {
                    String nome = matcher.group(1);
                    emailOrientador = matcher.group(2); // emailOrientador agora contém só o e-mail

                    System.out.println("Nome: " + nome);
                    System.out.println("Email: " + emailOrientador);
                }

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
                    if(camundaResquest){
                        System.out.println("Requisição enviada ao Camunda com sucesso!");
                        markEmail.markEmailAsRead(email.getUid());
                        emailController.sendEmail(emailOrientador, "\"Dados Extraídos com Sucesso\"\n",
                                "Prezado(a),"
                                        + "\n\nOs dados foram extraídos com sucesso e o processo foi iniciado."
                                        + "\n\nAgradecemos a colaboração."
                                        + "\n\nAtenciosamente,"
                                        + "\nPPComp - Programa de Pós-Graduação em Computação");
                    } else {
                        System.out.println("Erro ao enviar requisição para o Camunda.");
                    }
                } else {
                    System.out.println("Dados não encontrados no email.");
                    emailController.sendEmail(emailOrientador, "\"Formato de Dados Incorreto para Cadastro de Defesa\"\n",
                                "Prezado(a),"
                                    + "\n\nNão foi possível extrair os dados do e-mail enviado. Para que o cadastro do trabalho seja realizado corretamente, envie os seguintes dados no corpo do e-mail:"
                                    + "\n\nAssunto do email: Defesa"
                                    + "\naluno: Nome do Aluno"
                                    + "\nemail: email@exemplo.com"
                                    + "\ntitulo: Título do Trabalho"
                                    + "\n\nExemplo: aluno: João da Silva email: joao.silva@exemplo.com titulo: Análise de Dados com IA"
                                    + "\n\nAgradecemos a colaboração."
                                    + "\n\nAtenciosamente,"
                                    + "\nPPComp - Programa de Pós-Graduação em Computação");
                    markEmail.markEmailAsRead(email.getUid());
                }
            }
        } else {
            System.out.println("Nenhum email encontrado com o assunto: " + subject);
        }
    }
}

