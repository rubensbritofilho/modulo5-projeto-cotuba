package br.com.unipds.cotuba.adapters.in.cli.domain;

import io.soabase.recordbuilder.core.RecordBuilder;
import org.jmolecules.ddd.annotation.Entity;
import org.jmolecules.ddd.annotation.Identity;

@Entity
@RecordBuilder
public record Capitulo(@Identity String titulo,
                       Markdown markdown,
                       String html) {
}
