package com.lukas.pm2onto.model;

import com.lukas.pm2onto.model.enumerador.TipoAtividade;
import java.util.List;
import javax.persistence.JoinColumn;

/**
 *
 * @author lukas
 */
//@Entity
//@Table(name = "atividade", schema = "pm2onto")
public class Atividade extends Elemento {

    //@Column(name = "tipo")
    private TipoAtividade tipo;

    //@ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_processo", referencedColumnName = "id_elemento")
    private Processo processo;

    //@ManyToOne(cascade = CascadeType.ALL)
    //@JoinColumn(name = "id_anotacao", referencedColumnName = "id_elemento")
    private Anotacao anotacao;

    //@ManyToOne(cascade = CascadeType.ALL)
    //@JoinColumn(name = "id_grupo", referencedColumnName = "id_elemento")
    private Grupo grupo;

    //@OneToMany(mappedBy = "atividade", cascade = CascadeType.ALL)
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
