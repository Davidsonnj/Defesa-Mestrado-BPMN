package br.edu.ifes.mestrado.documentos.services;

import br.edu.ifes.mestrado.camunda.model.Mes;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class GeradorDeFolhaDeAprovação extends AbstractDocTextReplacer {

    public static void gerarFolhaDeAprovacao(String dataDefesa, String nomeAluno, String tituloTese,
                                             String orientadorPrincipal, String coOrientador,
                                             String membroInterno, String membroExterno) {

        String templatePath = "C:\\Users\\Davidson\\Desktop\\Defesa-Mestrado-BPMN\\Defesa-Mestrado-Camunda\\defesa-mestrado\\src\\main\\java\\br\\edu\\ifes\\mestrado\\documentos\\templates\\entrada\\MODELO_FOLHA_DE_APROVACAO.docx";
        String outputPath = "C:\\Users\\Davidson\\Desktop\\Defesa-Mestrado-BPMN\\Defesa-Mestrado-Camunda\\defesa-mestrado\\src\\main\\java\\br\\edu\\ifes\\mestrado\\documentos\\templates\\saida\\folha_aprovacao\\FOLHA_APROVACAO_" + tituloTese.replaceAll("[^a-zA-Z0-9.-]", "_") + ".docx";

        File outputFile = new File(outputPath);
        outputFile.getParentFile().mkdirs();

        // Usando try-with-resources para garantir que tudo seja fechado automaticamente
        try (FileInputStream fis = new FileInputStream(templatePath);
             XWPFDocument document = new XWPFDocument(fis);
             FileOutputStream fos = new FileOutputStream(outputFile)) {

            String[] partesData = dataDefesa.split("/");
            int numeroDoMes = Integer.parseInt(partesData[1]);
            String nomeDoMes = Mes.fromNumero(numeroDoMes).getNomePorExtenso();

            replaceTextInDoc(document, "{{aluno_nome}}", nomeAluno);
            replaceTextInDoc(document, "{{titulo_tese}}", tituloTese);
            replaceTextInDoc(document, "{{dia_defesa}}", partesData[0]);
            replaceTextInDoc(document, "{{mes_defesa}}", nomeDoMes);
            replaceTextInDoc(document, "{{ano_defesa}}", partesData[2]);
            replaceTextInDoc(document, "{{orientador_principal}}", orientadorPrincipal);
            replaceTextInDoc(document, "{{co_orientador}}", coOrientador);
            replaceTextInDoc(document, "{{membro_interno}}", membroInterno);
            replaceTextInDoc(document, "{{membro_externo}}", membroExterno);

            document.write(fos); // Escreve no arquivo de saída

            System.out.println("Folha de Aprovação gerada em: " + outputPath);

        } catch (Exception e) {
            System.err.println("Falha ao gerar a Folha de Aprovação.");
            e.printStackTrace();
        }
    }
}