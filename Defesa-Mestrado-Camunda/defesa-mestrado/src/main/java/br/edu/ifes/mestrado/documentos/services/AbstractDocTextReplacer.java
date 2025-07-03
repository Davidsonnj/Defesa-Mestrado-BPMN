package br.edu.ifes.mestrado.documentos.services;

import org.apache.poi.xwpf.usermodel.*;
import java.util.List;

public abstract class AbstractDocTextReplacer {

    /**
     * Método principal que percorre o documento.
     */
    public static void replaceTextInDoc(XWPFDocument doc, String findText, String replaceText) {
        // Itera sobre parágrafos no corpo do documento
        for (XWPFParagraph p : doc.getParagraphs()) {
            replaceInParagraph(p, findText, replaceText);
        }

        // Itera sobre parágrafos dentro de tabelas
        for (XWPFTable tbl : doc.getTables()) {
            for (XWPFTableRow row : tbl.getRows()) {
                for (XWPFTableCell cell : row.getTableCells()) {
                    for (XWPFParagraph p : cell.getParagraphs()) {
                        replaceInParagraph(p, findText, replaceText);
                    }
                }
            }
        }
    }

    /**
     * Orquestra os dois passos: 1. Juntar placeholders quebrados. 2. Fazer a substituição simples.
     */
    private static void replaceInParagraph(XWPFParagraph paragraph, String findText, String replaceText) {
        // Passo 1: Prepara o parágrafo, juntando os "runs" que compõem o placeholder.
        mergeRunsContainingPlaceholder(paragraph, findText);

        // Passo 2: Executa a substituição simples no estilo que você solicitou.
        simpleTextReplace(paragraph, findText, replaceText);
    }

    /**
     * Lógica de substituição simples, que funciona bem após a preparação.
     */
    private static void simpleTextReplace(XWPFParagraph p, String findText, String replaceText) {
        for (XWPFRun r : p.getRuns()) {
            String text = r.getText(0);
            if (text != null && text.contains(findText)) {
                // Usamos a substituição simples que preserva melhor a formatação do run
                text = text.replace(findText, replaceText);
                r.setText(text, 0);
            }
        }
    }

    /**
     * Esta função encontra um placeholder que foi quebrado pelo Word em múltiplos "runs"
     * e os une em um único run, permitindo que a substituição simples funcione.
     */
    private static void mergeRunsContainingPlaceholder(XWPFParagraph p, String placeholder) {
        List<XWPFRun> runs = p.getRuns();
        if (runs.size() < 2) return;

        for (int i = 0; i < runs.size(); i++) {
            XWPFRun run = runs.get(i);
            String text = run.getText(0);
            if (text == null) continue;

            // Encontramos o início do placeholder?
            if (text.contains("{{")) {
                StringBuilder compositeText = new StringBuilder(text);

                // Se o placeholder não está completo neste run, olhe para os próximos
                if (!text.contains("}}")) {
                    for (int j = i + 1; j < runs.size(); j++) {
                        XWPFRun nextRun = runs.get(j);
                        String nextText = nextRun.getText(0);
                        if (nextText == null) continue;

                        compositeText.append(nextText);

                        // Se encontramos o final, podemos parar de juntar
                        if (nextText.contains("}}")) {
                            // Achamos a sequência completa. Agora vamos juntar os runs.
                            // Define o texto completo no primeiro run da sequência.
                            run.setText(compositeText.toString(), 0);

                            // Remove os runs subsequentes que foram juntados.
                            // Removemos de trás para frente para não bagunçar os índices.
                            for (int k = j; k > i; k--) {
                                p.removeRun(k);
                            }
                            // Reinicia a verificação, pois a lista de runs mudou.
                            break;
                        }
                    }
                }
            }
        }
    }
}