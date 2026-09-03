package br.com.unipds;
import java.nio.file.Path;
import java.util.List;

import nl.siegmann.epublib.domain.*;
public class Main {

    void main(String[] args) {
        int exitCode = executar(args);
        if (exitCode != 0) {
            System.exit(exitCode);
        }
    }

    int executar(String[] args) {


        boolean modoVerboso = true;

        try{
            LeitorOpcoesCLI leitorOpcoesCLI = new LeitorOpcoesCLI();
            leitorOpcoesCLI.ler(args);

            Path diretorioDosMD = leitorOpcoesCLI.getDiretorioDosMD();
            String formato = leitorOpcoesCLI.getFormato();
            Path arquivoDeSaida = leitorOpcoesCLI.getArquivoDeSaida();
            modoVerboso = leitorOpcoesCLI.isModoVerboso();

            RenderizadorMarkdown renderizadorMarkdown = new RenderizadorMarkdown();
            List<String> htmls = renderizadorMarkdown.renderizar(diretorioDosMD);

            if ("pdf".equals(formato)) {
                GeradorPDF geradorPDF = new GeradorPDF();
                geradorPDF.gerarPDF(htmls, arquivoDeSaida);

            } else if ("epub".equals(formato)) {
                GeradorEPUB geradorEPUB = new GeradorEPUB();
                geradorEPUB.gerarEPUB(htmls, arquivoDeSaida);

            } else {
               throw new IllegalArgumentException("Formato do ebook inválido: " + formato);
            }

                System.out.println("Arquivo gerado com sucesso: " + arquivoDeSaida);
                return 0;

        } catch (Exception ex) {
            System.err.println(ex.getMessage());
            if (modoVerboso) {
                System.err.println();
                ex.printStackTrace();
            }
            return 1;
        }
    }
}