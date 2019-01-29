package com.lukas.pm2onto.model;

import com.lukas.pm2onto.model.enumerador.DirecaoGateway;
import com.lukas.pm2onto.model.enumerador.TipoGateway;

/**
 *
 * @author lukas
 */


public class Gateway extends Elemento {

    private TipoGateway tipo;

    private DirecaoGateway direcao;
        
    private Processo processo;

    private Anotacao anotacao;

    private Grupo grupo;    

    public Gateway() {
    }

    public TipoGateway getTipo() {
        return tipo;
    }

    public void setTipo(TipoGateway tipo) {
        this.tipo = tipo;
    }

    public DirecaoGateway getDirecao() {
        return direcao;
    }

    public void setDirecao(DirecaoGateway direcao) {
        this.direcao = direcao;
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
}
