package br.com.unipds.cotuba.adapters.in.cli.ports.out;

import br.com.unipds.cotuba.adapters.in.cli.domain.Markdown;

import java.nio.file.Path;
import java.util.List;


public interface RepositorioMarkdowns {
    List<Markdown> buscar(Path diretorioMD);
}
