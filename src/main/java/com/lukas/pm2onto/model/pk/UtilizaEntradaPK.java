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
public class UtilizaEntradaPK implements java.io.Serializable {

    @Column(name = "id_atividade")
    @Size(min = 36, max = 36)
    private String idAtividade;

    @Column(name = "id_artefato")
    @Size(min = 36, max = 36)
    private String idArtefato;

    public UtilizaEntradaPK() {
    }

    public UtilizaEntradaPK(String idAtividade, String idArtefato) {
        this.idAtividade = idAtividade;
        this.idArtefato = idArtefato;
    }

    public String getIdAtividade() {
        return idAtividade;
    }

    public void setIdAtividade(String idAtividade) {
        this.idAtividade = idAtividade;
    }

    public String getIdArtefato() {
        return idArtefato;
    }

    public void setIdArtefato(String idArtefato) {
        this.idArtefato = idArtefato;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 53 * hash + Objects.hashCode(this.idAtividade);
        hash = 53 * hash + Objects.hashCode(this.idArtefato);
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
        final UtilizaEntradaPK other = (UtilizaEntradaPK) obj;
        if (!Objects.equals(this.idAtividade, other.idAtividade)) {
            return false;
        }
        return Objects.equals(this.idArtefato, other.idArtefato);
    }
}
