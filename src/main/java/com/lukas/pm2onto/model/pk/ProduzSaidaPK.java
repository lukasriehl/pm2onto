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
public class ProduzSaidaPK implements java.io.Serializable {

    @Column(name = "id_elemento")
    @Size(min = 36, max = 36)
    private String idElemento;

    @Column(name = "id_artefato")
    @Size(min = 36, max = 36)
    private String idArtefato;

    public ProduzSaidaPK() {
    }

    public ProduzSaidaPK(String idElemento, String idArtefato) {
        this.idElemento = idElemento;
        this.idArtefato = idArtefato;
    }

    public String getIdElemento() {
        return idElemento;
    }

    public void setIdElemento(String idElemento) {
        this.idElemento = idElemento;
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
        hash = 53 * hash + Objects.hashCode(this.idElemento);
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
        final ProduzSaidaPK other = (ProduzSaidaPK) obj;
        if (!Objects.equals(this.idElemento, other.idElemento)) {
            return false;
        }
        return Objects.equals(this.idArtefato, other.idArtefato);
    }
}
