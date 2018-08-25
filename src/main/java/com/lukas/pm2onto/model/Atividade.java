package com.lukas.pm2onto.model;

import com.lukas.pm2onto.model.enumerador.TipoAtividade;
import java.util.List;
import javax.persistence.JoinColumn;

/**
 *
 * @author lukas
 */
public class Atividade extends Elemento {

    private TipoAtividade tipo;

    @JoinColumn(name = "id_processo", referencedColumnName = "id_elemento")
    private Processo processo;

    private Anotacao anotacao;

    private Grupo grupo;

    private List<ExecutadoPor> executadoPorList;
    
    private boolean isSubFlow;
    
    private String idSubFlow;

    public Atividade() {

    }

    public TipoAtividade getTipo() {
        return tipo;
    }

    public void setTipo(TipoAtividade tipo) {
        this.tipo = tipo;
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

    public List<ExecutadoPor> getExecutadoPorList() {
        return executadoPorList;
    }

    public void setExecutadoPorList(List<ExecutadoPor> executadoPorList) {
        this.executadoPorList = executadoPorList;
    }

    public boolean getIsSubFlow() {
        return isSubFlow;
    }

    public void setIsSubFlow(boolean isSubFlow) {
        this.isSubFlow = isSubFlow;
    }

    public String getIdSubFlow() {
        return idSubFlow;
    }

    public void setIdSubFlow(String idSubFlow) {
        this.idSubFlow = idSubFlow;
    }   
}
