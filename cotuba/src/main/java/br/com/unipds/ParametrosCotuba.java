package br.com.unipds;

import java.nio.file.Path;
import java.util.Objects;

public class ParametrosCotuba {
    private Path diretorioMD;
    private FormatoEbook formato;
    private Path arquivoSaida;
    private boolean modoVerboso;

    public boolean isModoVerboso() {
        return modoVerboso;
    }

    public void setModoVerboso(boolean modoVerboso) {
        this.modoVerboso = modoVerboso;
    }

    public Path getDiretorioMD() {
        return diretorioMD;
    }

    public void setDiretorioMD(Path diretorioMD) {
        this.diretorioMD = diretorioMD;
    }

    public FormatoEbook getFormato() {
        return formato;
    }

    public void setFormato(FormatoEbook formato) {
        this.formato = formato;
    }

    public Path getArquivoSaida() {
        return arquivoSaida;
    }

    public void setArquivoSaida(Path arquivoSaida) {
        this.arquivoSaida = arquivoSaida;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ParametrosCotuba that = (ParametrosCotuba) o;
        return Objects.equals(diretorioMD, that.diretorioMD) && formato == that.formato && Objects.equals(arquivoSaida, that.arquivoSaida);
    }

    @Override
    public int hashCode() {
        return Objects.hash(diretorioMD, formato, arquivoSaida);
    }
}
