package br.com.unipds;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.commonmark.node.AbstractVisitor;
import org.commonmark.node.Heading;
import org.commonmark.node.Node;
import org.commonmark.node.Text;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

import java.nio.file.Path;
import java.util.List;

@ApplicationScoped
public class RenderizadorMarkdownCommonMark implements RenderizadorMarkdown {



    public void renderizar(List<Capitulo> capitulos) {


           capitulos.forEach(capitulo -> {
                Parser parser = Parser.builder().build();
                Node document;
                try {
                    String markdown = capitulo.getMarkdown();
                    document = parser.parse(markdown);
                    document.accept(new AbstractVisitor() {
                        @Override
                        public void visit(Heading heading) {
                            if (heading.getLevel() == 1) {
                                // capítulo
                                String tituloDoCapitulo = ((Text) heading.getFirstChild()).getLiteral();
                                capitulo.setTitulo(tituloDoCapitulo);
                            } else if (heading.getLevel() == 2) {
                                // seção
                            } else if (heading.getLevel() == 3) {
                                // título
                            }
                        }

                    });
                } catch (Exception ex) {
                    throw new IllegalStateException("Erro ao fazer parse do capitulo " + capitulo.getArquivoMarkdown(), ex);
                }

                try {
                    HtmlRenderer renderer = HtmlRenderer.builder().build();

                    capitulo.setHtml(renderer.render(document));
                } catch (Exception ex) {
                    throw new IllegalStateException("Erro ao renderizar para HTML o arquivo " + capitulo.getArquivoMarkdown(), ex);
                }
            });
    }
}
