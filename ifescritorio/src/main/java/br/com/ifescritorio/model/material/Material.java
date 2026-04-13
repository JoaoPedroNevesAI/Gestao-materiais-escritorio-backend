package br.com.ifescritorio.model.material;

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
@Table(name = "material")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Material extends EntidadeAuditavel {
    
	@Column(nullable = false, length = 100)
    private String nome;

    @Column(length = 255)
    private String descricao;

    @Column(nullable = false)
    private Integer quantidade;

}