package br.edu.ifes.mestrado.documentos.services;

import br.edu.ifes.mestrado.camunda.model.Mes;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class GeradorDeDeclaracaoMembroExterno extends AbstractDocTextReplacer {

    public static String gerarDeclaracao(String nomeCoordenador, String membroExterno, String nomeAluno,
                                       String tituloTese, String dataDefesa) {

        String templatePath = "C:\\Users\\Davidson\\Desktop\\Defesa-Mestrado-BPMN\\Defesa-Mestrado-Camunda\\defesa-mestrado\\src\\main\\java\\br\\edu\\ifes\\mestrado\\documentos\\templates\\entrada\\Modelo_Declaração_Membro_Externo.docx";
        String outputPath = "C:\\Users\\Davidson\\Desktop\\Defesa-Mestrado-BPMN\\Defesa-Mestrado-Camunda\\defesa-mestrado\\src\\main\\java\\br\\edu\\ifes\\mestrado\\documentos\\templates\\saida\\declaracao_externa\\DECLARACAO_EXTERNA_" + tituloTese.replaceAll("[^a-zA-Z0-9.-]", "_") + "_" + nomeAluno.replaceAll("[^a-zA-Z0-9.-]", "_") + ".docx";

        try {
            FileInputStream fis = new FileInputStream(templatePath);
            XWPFDocument document = new XWPFDocument(fis);

            String[] partesData = dataDefesa.split("/");
            int numeroDoMes = Integer.parseInt(partesData[1]);
            String nomeDoMes = Mes.fromNumero(numeroDoMes).getNomePorExtenso();

            replaceTextInDoc(document, "{{coordenador_nome}}", nomeCoordenador);
            replaceTextInDoc(document, "{{membro_externo}}", membroExterno);
            replaceTextInDoc(document, "{{aluno_nome}}", nomeAluno);
            replaceTextInDoc(document, "{{titulo_tese}}", tituloTese);
            replaceTextInDoc(document, "{{dia_defesa}}", partesData[0]);
            replaceTextInDoc(document, "{{mes_defesa}}", nomeDoMes);
            replaceTextInDoc(document, "{{ano_defesa}}", partesData[2]);
            replaceTextInDoc(document, "{{coordenador_nome}}", nomeCoordenador);

            File outputFile = new File(outputPath);
            outputFile.getParentFile().mkdirs();

            FileOutputStream fos = new FileOutputStream(outputFile);
            document.write(fos);

            fos.close();
            fis.close();
            document.close();
            System.out.println("Declaração de Membro Externo gerada em: " + outputPath);

        } catch (Exception e) {
            System.err.println("Falha ao gerar a Declaração de Membro Externo.");
            e.printStackTrace();
        }
        return outputPath;
    }
}