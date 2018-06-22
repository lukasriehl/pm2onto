package com.lukas.pm2onto.model;

import java.io.Serializable;

/**
 *
 * @author lukas
 */
//@Entity
//@Table(name = "executado_por", schema = "pm2onto")
public class ExecutadoPor implements Serializable {
    
    private static final long serialVersionUID = 1L;

//    @EmbeddedId
//    private ExecutadoPorPK id;

//    @ManyToOne
//    @JoinColumn(name = "id_atividade", referencedColumnName = "id_elemento")
    private Atividade atividade;

//    @ManyToOne
//    @JoinColumn(name = "id_ator", referencedColumnName = "id_elemento")
    private Ator ator;

    public ExecutadoPor() {

    }

//    public ExecutadoPorPK getId() {
//        return id;
//    }
//
//    public void setId(ExecutadoPorPK id) {
//        this.id = id;
//    }

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
