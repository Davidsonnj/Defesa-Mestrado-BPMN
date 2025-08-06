package br.edu.ifes.mestrado.documentos.services;

import br.edu.ifes.mestrado.camunda.model.Mes;
import org.apache.poi.xwpf.usermodel.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class GeradorDeAta extends AbstractDocTextReplacer {

    public static String gerarAta(String dataDefesa, String horaDefesa, String localDefesa,
                                String nomeAluno, String tituloTese, String orientadorPrincipal,
                                String coOrientador, String membroInterno, String membroExterno) {

        String templatePath = "C:\\Users\\Davidson\\Desktop\\Defesa-Mestrado-BPMN\\Defesa-Mestrado-Camunda\\defesa-mestrado\\src\\main\\java\\br\\edu\\ifes\\mestrado\\documentos\\templates\\entrada\\MODELO_ATA.docx";
        String outputPath = "C:\\Users\\Davidson\\Desktop\\Defesa-Mestrado-BPMN\\Defesa-Mestrado-Camunda\\defesa-mestrado\\src\\main\\java\\br\\edu\\ifes\\mestrado\\documentos\\templates\\saida\\ata_defesa\\ATA_GERADA_" + tituloTese.replaceAll("[^a-zA-Z0-9.-]", "_") + "_" + nomeAluno.replaceAll("[^a-zA-Z0-9.-]", "_") + ".docx";

        try {
            FileInputStream fis = new FileInputStream(templatePath);
            XWPFDocument document = new XWPFDocument(fis);
            System.out.println("Template corrigido carregado. Substituindo marcadores...");

            String[] partesData = dataDefesa.split("/");
            int numeroDoMes = Integer.parseInt(partesData[1]);
            String nomeDoMes = Mes.fromNumero(numeroDoMes).getNomePorExtenso();

            replaceTextInDoc(document, "{{dia_defesa}}", partesData[0]);
            replaceTextInDoc(document, "{{mes_defesa}}", nomeDoMes);
            replaceTextInDoc(document, "{{ano_defesa}}", partesData[2]);
            replaceTextInDoc(document, "{{hora_defesa}}", horaDefesa);
            replaceTextInDoc(document, "{{local_defesa}}", localDefesa);
            replaceTextInDoc(document, "{{aluno_nome}}", nomeAluno);
            replaceTextInDoc(document, "{{titulo_tese}}", tituloTese);
            replaceTextInDoc(document, "{{orientador_principal}}", orientadorPrincipal);
            replaceTextInDoc(document, "{{co_orientador}}", coOrientador);
            replaceTextInDoc(document, "{{membro_interno}}", membroInterno);
            replaceTextInDoc(document, "{{membro_externo}}", membroExterno);

            File outputFile = new File(outputPath);
            File parentDir = outputFile.getParentFile();
            if (!parentDir.exists()) {
                parentDir.mkdirs();
            }

            FileOutputStream fos = new FileOutputStream(outputFile);
            document.write(fos);

            fos.close();
            fis.close();
            document.close();
            System.out.println("Documento final gerado em: " + outputPath);

        } catch (Exception e) {
            System.err.println("Falha ao gerar o documento.");
            e.printStackTrace();
        }
        return outputPath;
    }

}
