package com.lukas.pm2onto.model;

import com.lukas.pm2onto.model.enumerador.GatilhoEvento;
import com.lukas.pm2onto.model.enumerador.TipoEvento;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author lukas
 */
//@Entity
//@Table(name = "evento", schema = "pm2onto")
public class Evento extends Elemento{
    
    //@Column(name = "tipo")
    private TipoEvento tipo;
    
    //@Column(name = "gatilho")
    private GatilhoEvento gatilho;
    
    //@ManyToOne(cascade = CascadeType.ALL)
    //@JoinColumn(name = "id_processo", referencedColumnName = "id_elemento")
    private Processo processo;
    
    //@ManyToOne(cascade = CascadeType.ALL)
    //@JoinColumn(name = "id_anotacao", referencedColumnName = "id_elemento")
    private Anotacao anotacao;
    
    //@ManyToOne(cascade = CascadeType.ALL)
    //@JoinColumn(name = "id_grupo", referencedColumnName = "id_elemento")
    private Grupo grupo;
    
    private String targetId;
    
    public Evento(){
        
    }

    public TipoEvento getTipo() {
        return tipo;
    }

    public void setTipo(TipoEvento tipo) {
        this.tipo = tipo;
    }

    public GatilhoEvento getGatilho() {
        return gatilho;
    }

    public void setGatilho(GatilhoEvento gatilho) {
        this.gatilho = gatilho;
    }

    public Processo getProcesso() {
        return processo;
    }

    public void setProcesso(Processo processo) {
        this.processo = processo;
    }

    public Anotacao getAnotacao() {
        return anotacao;
    }

    public void setAnotacao(Anotacao anotacao) {
        this.anotacao = anotacao;
    }

    public Grupo getGrupo() {
        return grupo;
    }

    public void setGrupo(Grupo grupo) {
        this.grupo = grupo;
    }   

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }  
}
