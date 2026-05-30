package br.com.ifescritorio.model.categoria;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import br.com.ifescritorio.model.material.Material;
import br.com.ifescritorio.util.entity.EntidadeAuditavel;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "categoria")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Categoria extends EntidadeAuditavel {

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(length = 255)
    private String descricao;

    @JsonIgnore
    @OneToMany(mappedBy = "categoria")
    private List<Material> materiais;
}