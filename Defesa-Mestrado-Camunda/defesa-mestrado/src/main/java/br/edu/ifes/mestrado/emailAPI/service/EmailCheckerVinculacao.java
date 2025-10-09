package br.edu.ifes.mestrado.emailAPI.service;

import br.edu.ifes.mestrado.GenAI.pergunta.implementacoes.PerguntaVinculacao;
import br.edu.ifes.mestrado.database.dao.implementations.EmailDAO;
import br.edu.ifes.mestrado.emailAPI.controller.EmailController;
import br.edu.ifes.mestrado.emailAPI.controller.FuncoesEmail;
import br.edu.ifes.mestrado.emailAPI.model.Email;
import br.edu.ifes.mestrado.emailAPI.model.ExtrairDadosVinculacao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class EmailCheckerVinculacao {

    private final CamundaRequesterVinculacao camundaRequesterVinculacao;
    private final PerguntaVinculacao perguntaVinculacao;

    @Autowired
    public EmailCheckerVinculacao(CamundaRequesterVinculacao camundaRequesterVinculacao, PerguntaVinculacao perguntaVinculacao) {
        this.camundaRequesterVinculacao = camundaRequesterVinculacao;
        this.perguntaVinculacao = perguntaVinculacao;

    }

    @Scheduled(fixedDelay = 60000)
    public void verificarEmailsVinculacao() {
        System.out.println("Verificando emails de VINCULAÇÃO...");
        EmailController emailController = new EmailController();
        EmailDAO emailDAO = new EmailDAO();

        List<Email> emails = emailDAO.findAll();

        for (Email email : emails) {
            if (email.getStatus().equals("DADOS_INICIAIS_VINCULACAO")) {
                System.out.println("Email de vinculação encontrado: " + email.getSubject());

                Map.Entry<String, String> resultado = FuncoesEmail.tratarEmailSender(email);
                String nomeOrientador = resultado.getKey();
                String emailOrientador = resultado.getValue();

                String resposta;
                try {
                    resposta = perguntaVinculacao.takeQuestion(email.getBody());
                } catch (Exception e) {
                    System.err.println("Erro ao processar vinculação: " + e.getMessage());
                    continue;
                }

                ExtrairDadosVinculacao.DadosVinculacao dados = ExtrairDadosVinculacao.extrair(resposta);
                System.out.println(dados);
                if (dados != null) {
                    // Chamar o Camunda para iniciar o processo Vinculação de Orientação
                    boolean enviado = camundaRequesterVinculacao.iniciarProcessoVinculacao(
                            dados.alunoNome,
                            dados.matricula,
                            dados.emailAluno,
                            dados.tema,
                            dados.periodoInicio,
                            emailOrientador,
                            nomeOrientador,
                            email.getUid()
                    );

                    if (enviado) {
                        emailController.sendEmail(
                                emailOrientador,
                                "Vinculação registrada com sucesso",
                                "Prezado(a),<br><br>" +
                                        "A vinculação com o(a) aluno(a) <b>" + dados.alunoNome + "</b> foi registrada com sucesso.<br><br>" +
                                        "Atenciosamente,<br>" +
                                        "PPComp - IFES Serra"
                        );
                        email.setStatus("PROCESSADO");
                        emailDAO.update(email);
                    }
                }
            }
        }
    }
}
