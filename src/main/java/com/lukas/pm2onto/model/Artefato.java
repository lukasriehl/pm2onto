package com.lukas.pm2onto.model;

import com.lukas.pm2onto.model.enumerador.TipoArtefato;
import java.util.List;

/**
 *
 * @author lukas
 */
//@Entity
//@Table(name = "artefato", schema = "pm2onto")
public class Artefato extends Elemento {

    //@Column(name = "tipo")
    private TipoArtefato tipo;

    //@ManyToOne(cascade = CascadeType.ALL)
    //@JoinColumn(name = "id_processo", referencedColumnName = "id_elemento")
    private Processo processo;

    //@ManyToOne(cascade = CascadeType.ALL)
    //@JoinColumn(name = "id_anotacao", referencedColumnName = "id_elemento")
    private Anotacao anotacao;

    //@ManyToOne(cascade = CascadeType.ALL)
    //@JoinColumn(name = "id_grupo", referencedColumnName = "id_elemento")
    private Grupo grupo;

    //@OneToMany(mappedBy = "artefato", cascade = CascadeType.ALL)
    private List<ProduzSaida> produzSaidaList;

    public Artefato() {

    }

    public TipoArtefato getTipo() {
        return tipo;
    }

    public void setTipo(TipoArtefato tipo) {
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

    public List<ProduzSaida> getProduzSaidaList() {
        return produzSaidaList;
    }

    public void setProduzSaidaList(List<ProduzSaida> produzSaidaList) {
        this.produzSaidaList = produzSaidaList;
    }
}
