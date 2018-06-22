package com.lukas.pm2onto.model;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author lukas
 */
//@Entity
//@Table(name = "ator", schema = "pm2onto")
public class Ator extends Elemento {

//    @ManyToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "id_processo", referencedColumnName = "id_elemento")
    private Processo processo;
    
    //@OneToMany(mappedBy = "ator", cascade = CascadeType.ALL)
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
