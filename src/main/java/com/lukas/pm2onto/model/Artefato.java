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

    private TipoArtefato tipo;

    private Processo processo;

    private Anotacao anotacao;

    private Grupo grupo;

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
