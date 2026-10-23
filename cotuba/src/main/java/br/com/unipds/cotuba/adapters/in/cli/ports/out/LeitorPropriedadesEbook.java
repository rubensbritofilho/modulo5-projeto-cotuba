package br.com.unipds.cotuba.adapters.in.cli.ports.out;

import br.com.unipds.cotuba.adapters.in.cli.dto.PropriedadesEbook;

import java.nio.file.Path;

public interface LeitorPropriedadesEbook {
    PropriedadesEbook ler(Path diretorioMD);
}
