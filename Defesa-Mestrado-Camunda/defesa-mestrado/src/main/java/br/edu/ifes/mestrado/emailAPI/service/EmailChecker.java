package br.edu.ifes.mestrado.emailAPI.service;

import br.edu.ifes.mestrado.GenAI.pergunta.implementacoes.PerguntaDadosIniciais;
import br.edu.ifes.mestrado.camunda.model.Banca;
import br.edu.ifes.mestrado.database.dao.implementations.EmailDAO;
import br.edu.ifes.mestrado.emailAPI.controller.EmailController;
import br.edu.ifes.mestrado.emailAPI.controller.FuncoesEmail;
import br.edu.ifes.mestrado.emailAPI.model.Email;
import br.edu.ifes.mestrado.emailAPI.model.ExtrairDadosEmail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;
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

        EmailDAO emailDAO = new EmailDAO();
        List<Email> emailConfirmacao = emailDAO.findAll();

        if (!emailConfirmacao.isEmpty()) {
            for (Email email : emailConfirmacao) {
                Map.Entry<String, String> resultado = FuncoesEmail.tratarEmailSender(email);

                String nomeOrientador = resultado.getKey();
                String emailOrientador = resultado.getValue();
                if(email.getStatus().equals("DADOS_INICIAIS")) {
                    System.out.println("Email encontrado: " + email.getSubject() + " - " + email.getSender());

                    long idDadosIniciais = email.getUid();

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


                    ExtrairDadosEmail.DadosExtraidos dados = ExtrairDadosEmail.extrairDados(resposta);

                    if (dados != null) {
                        String aluno = dados.aluno;
                        String emailAluno = dados.email;
                        String titulo_trabalho = dados.titulo;
                        String dataDefesa = dados.dataDefesa;
                        String horaDefesa = dados.horaDefesa;
                        String localDefesa = dados.localDefesa;
                        String nomeCoorientador = dados.nomeCoorientador;
                        String emailCoorientador = dados.emailCoorientador;
                        List<Banca> banca = dados.banca;

                        boolean camundaResquest = camundaRequester.iniciarProcesso(aluno, titulo_trabalho, emailAluno, nomeOrientador, emailOrientador, dataDefesa, horaDefesa, localDefesa, nomeCoorientador, emailCoorientador, banca, idDadosIniciais);
                        if (camundaResquest) {
                            System.out.println("Requisição enviada ao Camunda com sucesso!");
                            emailController.sendEmail(emailOrientador, "\"Dados Extraídos com Sucesso\"\n",
                                    "Prezado(a),<br><br>" +
                                          "Os dados foram extraídos com sucesso e o processo foi iniciado.<br><br>" +
                                          "Agradecemos a colaboração.<br><br>" +
                                          "Atenciosamente,<br>" +
                                          "PPComp - Programa de Pós-Graduação em Computação");

                            email.setStatus("PROCESSADO");
                            emailDAO.update(email);
                        } else {
                            System.out.println("Erro ao enviar requisição para o Camunda.");
                        }
                    }
                } else if(email.getStatus().equals("DADOS_INICIAIS_INCORRETOS")) {
                    System.out.println("Dados não encontrados no email.");
                    emailController.sendEmail(emailOrientador, "\"Formato de Dados Incorreto para Cadastro de Defesa\"\n",
                            "Prezado(a) Orientador(a),<br><br>" +
                                    "Encaminhamos, abaixo, o modelo com as informações obrigatórias para a formalização do processo de defesa de dissertação no âmbito do Programa de Pós-Graduação em Computação Aplicada (PPComp) – IFES, Campus Serra, referentes ao(à) discente.<br><br>" +

                                    "Solicitamos que todos os campos abaixo sejam devidamente preenchidos e enviados no corpo do e-mail, com o assunto: <strong>Defesa</strong>.<br><br>" +

                                    "<strong>Informações obrigatórias do(a) discente:</strong><br>" +
                                    "- Nome completo do(a) aluno(a)<br>" +
                                    "- E-mail do(a) aluno(a)<br>" +
                                    "- Título da dissertação<br><br>" +

                                    "<strong>Informações obrigatórias para a defesa:</strong><br>" +
                                    "- Data da defesa<br>" +
                                    "- Horário da defesa<br>" +
                                    "- Local da defesa (presencial ou remoto, com link se for online)<br>" +
                                    "- Nome do(a) coorientador(a) (caso não haja, deixar em branco)<br>" +
                                    "- E-mail do(a) coorientador(a) (caso não haja, deixar em branco)<br><br>" +

                                    "<strong>Composição da banca examinadora (dados obrigatórios para cada membro):</strong><br>" +
                                    "- Nome completo<br>" +
                                    "- E-mail<br>" +
                                    "- Instituição de origem<br>" +
                                    "- Minicurrículo resumido<br><br>" +

                                    "<strong>Observações adicionais:</strong><br>" +
                                    "- Caso a defesa seja remota, incluir o link de acesso<br>" +
                                    "- Enviar os dados com identificação, ex.: nome do aluno: 'Jose Silva'<br><br>" +

                                    "O envio completo e correto dessas informações é essencial para o adequado registro e andamento dos trâmites acadêmicos.<br><br>" +

                                    "Agradecemos sua atenção e colaboração, e permanecemos à disposição para quaisquer esclarecimentos.<br><br>" +
                                    "Atenciosamente,<br><br>" +
                                    "Programa de Pós-Graduação em Computação Aplicada (PPComp)<br>" +
                                    "IFES – Campus Serra");


                    email.setStatus("PROCESSADO");
                    emailDAO.update(email);
                }
            }
        } else {
            System.out.println("Nenhum email encontrado com o status DADOS_INICIAIS");
        }
    }
}
