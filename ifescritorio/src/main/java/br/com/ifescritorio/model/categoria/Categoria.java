package br.com.ifescritorio.model.categoria;

import br.com.ifescritorio.util.entity.EntidadeAuditavel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

}