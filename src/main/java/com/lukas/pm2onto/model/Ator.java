package com.lukas.pm2onto.model;

import java.util.List;

/**
 *
 * @author lukas
 */
//@Entity
//@Table(name = "ator", schema = "pm2onto")
public class Ator extends Elemento {

    private Processo processo;

    private List<ExecutadoPor> executadoPorList;

    public Ator() {
    }

    public Processo getProcesso() {
        return processo;
    }

    public void setProcesso(Processo processo) {
        this.processo = processo;
    }

    public List<ExecutadoPor> getExecutadoPorList() {
        return executadoPorList;
    }

    public void setExecutadoPorList(List<ExecutadoPor> executadoPorList) {
        this.executadoPorList = executadoPorList;
    }
}
