package com.lukas.pm2onto.model;

import com.lukas.pm2onto.model.enumerador.GatilhoEvento;
import com.lukas.pm2onto.model.enumerador.TipoEvento;

/**
 *
 * @author lukas
 */
public class Evento extends Elemento{
    
    private TipoEvento tipo;
    
    private GatilhoEvento gatilho;
    
    private Processo processo;
    
    private Anotacao anotacao;
    
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
