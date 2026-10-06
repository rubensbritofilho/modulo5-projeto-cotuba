package br.com.unipds;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Any;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;

import java.nio.file.Path;
import java.util.List;

@ApplicationScoped
public class CotubaService {

    private final RenderizadorMarkdownCommonMark renderizadorMarkdown;
    private final LeitorPropriedadesEbook leitorPropriedadesEbook;
    private final RepositorioMarkdownsDiretorio repositorioMarkdownsDiretorio;
    private final Instance<GeradorEbook> geradororesEbook;

    @Inject
    public CotubaService(RenderizadorMarkdownCommonMark renderizadorMarkdown, LeitorPropriedadesEbook leitorPropriedadesEbook, RepositorioMarkdownsDiretorio repositorioMarkdownsDiretorio,
                         @Any Instance<GeradorEbook> geradororesEbook) {
        this.renderizadorMarkdown = renderizadorMarkdown;
        this.leitorPropriedadesEbook = leitorPropriedadesEbook;
        this.repositorioMarkdownsDiretorio = repositorioMarkdownsDiretorio;
        this.geradororesEbook = geradororesEbook;
    }

    public void executar(ParametrosCotuba par){
        Path diretorioMD = par.diretorioMD();

        List<Markdown > markdowns = repositorioMarkdownsDiretorio.buscar(diretorioMD);

        List<Capitulo> capitulos =  renderizadorMarkdown.renderizar(markdowns);

        var propriedadeEbook = leitorPropriedadesEbook.ler(par.diretorioMD());

        var ebook = EbookBuilder.builder()
                .titulo(propriedadeEbook.titulo())
                .autor(propriedadeEbook.autor())
                .capitulos(capitulos)
                .formato(par.formato())
                .arquivoDeSaida(par.arquivoSaida())
                .build();


        GeradorEbook geradorEbook = geradororesEbook.select(FormatoEbookFilter.of(ebook.formato())).get();

        geradorEbook.gerar(ebook);

    }

}
