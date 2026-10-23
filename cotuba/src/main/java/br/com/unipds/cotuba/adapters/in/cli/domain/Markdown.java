package br.com.unipds.cotuba.adapters.in.cli.domain;

import org.jmolecules.ddd.annotation.Entity;
import org.jmolecules.ddd.annotation.Identity;

@Entity
public record Markdown(@Identity String nome,
                       String conteudo) {

}
