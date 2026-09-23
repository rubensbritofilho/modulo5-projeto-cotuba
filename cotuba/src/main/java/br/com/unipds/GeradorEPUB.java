package br.com.unipds;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import nl.siegmann.epublib.domain.Author;
import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.GuideReference;
import nl.siegmann.epublib.domain.Resource;
import nl.siegmann.epublib.epub.EpubWriter;
import nl.siegmann.epublib.service.MediatypeService;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
@ApplicationScoped @Named("geradorEPUB")
public class GeradorEPUB implements GeradorEbook {

    public void gerar(Ebook ebook) {
        List<Capitulo> capitulos = ebook.getCapitulos();
        Path arquivoDeSaida = ebook.getArquivoDeSaida();
        try {
            var epub = new Book();

            //TODO: definir título e autor para o livro
            epub.getMetadata().addTitle(ebook.getTitulo());
            epub.getMetadata().addAuthor(new Author(ebook.getAutor()));

            boolean[] ehPrimeiroCapitulo = {true};

            capitulos.forEach(capitulo -> {
                        String tituloDoCapitulo = capitulo.getTitulo();
                        String html = capitulo.getHtml();
                try{
                        StringWriter sw = new StringWriter();
                        XMLStreamWriter writer = XMLOutputFactory.newInstance().
                                createXMLStreamWriter(sw);

                            writer.writeStartElement("html");
                            writer.writeDefaultNamespace("http://www.w3.org/1999/xhtml");

                            writer.writeStartElement("head");
                            writer.writeStartElement("title");
                            writer.writeCharacters(ebook.getTitulo());
                            writer.writeEndElement();
                            writer.writeEndElement();

                            writer.writeStartElement("body");
                            writer.writeCharacters("");

                            writer.flush();
                            sw.write(html);

                            writer.writeEndElement();//body
                            writer.writeEndElement();//html

                            var chapter = new Resource(sw.toString().getBytes(), MediatypeService.XHTML);
                            epub.addSection(tituloDoCapitulo, chapter);

                            if (ehPrimeiroCapitulo[0]) {
                                epub.getGuide().addReference(new GuideReference(chapter, "text", "Start Reading"));
                                ehPrimeiroCapitulo[0] = false;
                            }
                        }catch(XMLStreamException ex){
                            throw new IllegalStateException("Erro ao capitulo do  EPUB: " +
                                    capitulo.getTitulo(), ex);
                        }
                    });

            var epubWriter = new EpubWriter();

            try {
                epubWriter.write(epub, Files.newOutputStream(arquivoDeSaida));
            } catch (IOException ex) {
                throw new IllegalStateException("Erro ao criar arquivo EPUB: " + arquivoDeSaida.toAbsolutePath(), ex);
            }

        } catch (Exception ex) {
            throw new IllegalStateException("Erro ao gerar EPUB: " + arquivoDeSaida.toAbsolutePath(), ex);
        }
    }
}
