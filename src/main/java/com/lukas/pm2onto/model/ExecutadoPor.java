package com.lukas.pm2onto.model;

import java.io.Serializable;

/**
 *
 * @author lukas
 */
public class ExecutadoPor implements Serializable {

    private static final long serialVersionUID = 1L;

    private Atividade atividade;

    private Ator ator;

    public ExecutadoPor() {

    }

    public Atividade getAtividade() {
        return atividade;
    }

    public void setAtividade(Atividade atividade) {
        this.atividade = atividade;
    }

    public Ator getAtor() {
        return ator;
    }

    public void setAtor(Ator ator) {
        this.ator = ator;
    }
}
