package br.com.unipds;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.nio.file.Path;
import java.util.List;

@ApplicationScoped
public class CotubaService {

    private final RenderizadorMarkdownCommonMark renderizadorMarkdown;
    private final LeitorPropriedadesEbook leitorPropriedadesEbook;
    private final RepositorioMarkdownsDiretorio repositorioMarkdownsDiretorio;
    private final GeradorEbook geradorEPUB;
    private final GeradorEbook geradorPDF;

    @Inject
    public CotubaService(RenderizadorMarkdownCommonMark renderizadorMarkdown, LeitorPropriedadesEbook leitorPropriedadesEbook, RepositorioMarkdownsDiretorio repositorioMarkdownsDiretorio,
                         @Named("geradorEPUB") GeradorEbook geradorEPUB, @Named("geradorPDF") GeradorEbook geradorPDF) {
        this.renderizadorMarkdown = renderizadorMarkdown;
        this.leitorPropriedadesEbook = leitorPropriedadesEbook;
        this.repositorioMarkdownsDiretorio = repositorioMarkdownsDiretorio;
        this.geradorEPUB = geradorEPUB;
        this.geradorPDF = geradorPDF;
    }

    public void executar(ParametrosCotuba par){
        Path diretorioMD = par.getDiretorioMD();

        List<Capitulo> capitulos = repositorioMarkdownsDiretorio.buscar(diretorioMD);

        renderizadorMarkdown.renderizar(capitulos);

        Ebook ebook = new Ebook();

        leitorPropriedadesEbook.ler(par.getDiretorioMD(), ebook);

        ebook.setTitulo("Apostila de Design");
        ebook.setAutor("UNIPDS");
        ebook.setCapitulos(capitulos);
        ebook.setFormato(par.getFormato());
        ebook.setArquivoDeSaida(par.getArquivoSaida());

        GeradorEbook geradorEbook;

        if (FormatoEbook.PDF.equals(ebook.getFormato())) {
            geradorEbook =  geradorPDF;
        } else if (FormatoEbook.EPUB.equals(ebook.getFormato())) {
            geradorEbook = geradorEPUB;
        } else {
            throw new IllegalArgumentException("Formato do ebook inválido: " + ebook.getFormato());
        }
        geradorEbook.gerar(ebook);

    }

}
