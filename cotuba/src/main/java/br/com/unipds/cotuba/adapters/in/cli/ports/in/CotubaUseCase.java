package br.com.unipds.cotuba.adapters.in.cli.ports.in;

import br.com.unipds.cotuba.adapters.in.cli.dto.ParametrosCotuba;

public interface CotubaUseCase {
    void executar(ParametrosCotuba parametrosCotuba);
}
