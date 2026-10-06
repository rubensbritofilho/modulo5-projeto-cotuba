package br.com.unipds;

import io.soabase.recordbuilder.core.RecordBuilder;

import java.nio.file.Path;
import java.util.Objects;

@RecordBuilder
public record ParametrosCotuba(Path diretorioMD, FormatoEbook formato, Path arquivoSaida, boolean modoVerboso) {

    public ParametrosCotuba(Path diretorioMD, FormatoEbook formato, Path arquivoSaida) {
        this(diretorioMD, formato, arquivoSaida, false);
    }
}
