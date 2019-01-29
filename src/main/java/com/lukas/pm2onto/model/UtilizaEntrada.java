package com.lukas.pm2onto.model;

import java.io.Serializable;

/**
 *
 * @author lukas
 */
public class UtilizaEntrada implements Serializable {

    private static final long serialVersionUID = 1L;

    private Artefato artefato;

    private Atividade atividade;

    public UtilizaEntrada() {

    }

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
