package com.lukas.pm2onto.model;

import java.io.Serializable;

/**
 *
 * @author lukas
 */
public class ProduzSaida implements Serializable {

    private static final long serialVersionUID = 1L;

    private Artefato artefato;

    private Elemento elemento;

    public ProduzSaida() {
    }

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
