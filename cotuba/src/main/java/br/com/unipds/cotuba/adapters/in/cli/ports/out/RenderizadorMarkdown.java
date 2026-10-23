package br.com.unipds.cotuba.adapters.in.cli.ports.out;

import br.com.unipds.cotuba.adapters.in.cli.domain.Capitulo;
import br.com.unipds.cotuba.adapters.in.cli.domain.Markdown;

import java.util.List;


public interface RenderizadorMarkdown {
        List<Capitulo> renderizar(List<Markdown> markdowns);

}

