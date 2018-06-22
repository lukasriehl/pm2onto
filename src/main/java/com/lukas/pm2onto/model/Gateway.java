package com.lukas.pm2onto.model;

import com.lukas.pm2onto.model.enumerador.DirecaoGateway;
import com.lukas.pm2onto.model.enumerador.TipoGateway;
import javax.persistence.Transient;

/**
 *
 * @author lukas
 */
//@Entity
//@Table(name = "gateway", schema = "pm2onto")
public class Gateway extends Elemento {

    //@Column(name = "tipo")
    private TipoGateway tipo;

    //@Column(name = "tipo")
    private DirecaoGateway direcao;
    
    //@ManyToOne(cascade = CascadeType.ALL)
    //@JoinColumn(name = "id_processo", referencedColumnName = "id_elemento")
    private Processo processo;

    //@ManyToOne(cascade = CascadeType.ALL)
    //@JoinColumn(name = "id_anotacao", referencedColumnName = "id_elemento")
    private Anotacao anotacao;

    //@ManyToOne(cascade = CascadeType.ALL)
    //@JoinColumn(name = "id_grupo", referencedColumnName = "id_elemento")
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
