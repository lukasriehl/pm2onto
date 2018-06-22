package com.lukas.pm2onto.model.pk;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.Size;

/**
 *
 * @author lukas
 */
@Embeddable
public class ExecutadoPorPK implements java.io.Serializable {

    @Column(name = "id_ator")
    @Size(min = 36, max = 36)
    private String idAtor;

    @Column(name = "id_atividade")
    @Size(min = 36, max = 36)
    private String idAtividade;

    public ExecutadoPorPK() {
    }

    public ExecutadoPorPK(String idAtor, String idAtividade) {
        this.idAtor = idAtor;
        this.idAtividade = idAtividade;
    }

    public String getIdAtor() {
        return idAtor;
    }

    public void setIdAtor(String idAtor) {
        this.idAtor = idAtor;
    }

    public String getIdAtividade() {
        return idAtividade;
    }

    public void setIdAtividade(String idAtividade) {
        this.idAtividade = idAtividade;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 79 * hash + Objects.hashCode(this.idAtor);
        hash = 79 * hash + Objects.hashCode(this.idAtividade);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ExecutadoPorPK other = (ExecutadoPorPK) obj;
        if (!Objects.equals(this.idAtor, other.idAtor)) {
            return false;
        }
        return Objects.equals(this.idAtividade, other.idAtividade);
    }

}
