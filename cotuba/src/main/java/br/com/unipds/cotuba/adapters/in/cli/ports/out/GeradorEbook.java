package br.com.unipds.cotuba.adapters.in.cli.ports.out;

import br.com.unipds.cotuba.adapters.in.cli.domain.Ebook;

import java.nio.file.Path;

public interface GeradorEbook {
    void gerar(Ebook ebook, Path arquivoDeSaida);
}
