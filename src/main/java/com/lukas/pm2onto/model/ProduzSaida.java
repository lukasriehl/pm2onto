package com.lukas.pm2onto.model;

import com.lukas.pm2onto.model.pk.ProduzSaidaPK;
import java.io.Serializable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author lukas
 */
//@Entity
//@Table(name = "produz_saida", schema = "pm2onto")
public class ProduzSaida implements Serializable {
    
    private static final long serialVersionUID = 1L;

//    @EmbeddedId
//    private ProduzSaidaPK id;

//    @ManyToOne
//    @JoinColumn(name = "id_artefato", referencedColumnName = "id_elemento")
    private Artefato artefato;

    //Pode ser uma atividade ou evento em BPMN
//    @ManyToOne
//    @JoinColumn(name = "id_artefato", referencedColumnName = "id_elemento")
    private Elemento elemento;

    public ProduzSaida() {
    }

//    public ProduzSaidaPK getId() {
//        return id;
//    }
//
//    public void setId(ProduzSaidaPK id) {
//        this.id = id;
//    }    

    public Artefato getArtefato() {
        return artefato;
    }

    public void setArtefato(Artefato artefato) {
        this.artefato = artefato;
    }

    public Elemento getElemento() {
        return elemento;
    }

    public void setElemento(Elemento elemento) {
        this.elemento = elemento;
    }
}
