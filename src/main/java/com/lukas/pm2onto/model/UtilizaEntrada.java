package com.lukas.pm2onto.model;

import java.io.Serializable;

/**
 *
 * @author lukas
 */
//@Entity
//@Table(name = "utiliza_entrada", schema = "pm2onto")
public class UtilizaEntrada implements Serializable {
    
    private static final long serialVersionUID = 1L;

    //@EmbeddedId
//    private UtilizaEntradaPK id;

    //@ManyToOne
    //@JoinColumn(name = "id_artefato", referencedColumnName = "id_elemento")
    private Artefato artefato;

    //@ManyToOne
    //@JoinColumn(name = "id_atividade", referencedColumnName = "id_elemento")
    private Atividade atividade;

    public UtilizaEntrada() {

    }

//    public UtilizaEntradaPK getId() {
//        return id;
//    }
//
//    public void setId(UtilizaEntradaPK id) {
//        this.id = id;
//    }

    public Artefato getArtefato() {
        return artefato;
    }

    public void setArtefato(Artefato artefato) {
        this.artefato = artefato;
    }

    public Atividade getAtividade() {
        return atividade;
    }

    public void setAtividade(Atividade atividade) {
        this.atividade = atividade;
    }
}
