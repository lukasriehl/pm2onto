package com.lukas.pm2onto.model;

import java.util.List;
import java.util.Objects;
import javax.persistence.Transient;

/**
 *
 * @author lukas Classe com os atributos comuns a todas as demais classes.
 */
//@Entity
//@Table(name = "elemento", schema = "pm2onto")
public class Elemento implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

private String idElemento;

private String nome;

    private String descricao;

private String documentacao;

    private List<AtributoEstendido> atributoEstendidoList;

    private SucedidoPor sucedidoPorOrigem;

    private SucedidoPor sucedidoPorDestino;

    private List<TrocaMensagemCom> trocaMensagemComOrigemList;

    private List<TrocaMensagemCom> trocaMensagemComDestinoList;

    private List<ProduzSaida> produzSaidaList;

    //@Transient
    private boolean isRegraDeNegocio;

    //@Transient
    private boolean isRequisitoFuncional;

    //@Transient
    private boolean isRequisitoNaoFuncional;

    @Transient
    private String regraDeNegocio;
    
    private boolean fimFluxo;

    public Elemento() {
        fimFluxo = false;
    }

    public Elemento(String idElemento, String nome, String descricao, String documentacao) {
        this();
        this.idElemento = idElemento;
        this.nome = nome;
        this.descricao = descricao;
        this.documentacao = documentacao;
    }

    public String getIdElemento() {
        return idElemento;
    }

    public void setIdElemento(String idElemento) {
        this.idElemento = idElemento;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getDocumentacao() {
        return documentacao;
    }

    public void setDocumentacao(String documentacao) {
        this.documentacao = documentacao;
    }

    public List<AtributoEstendido> getAtributoEstendidoList() {
        return atributoEstendidoList;
    }

    public void setAtributoEstendidoList(List<AtributoEstendido> atributoEstendidoList) {
        this.atributoEstendidoList = atributoEstendidoList;
    }

    public SucedidoPor getSucedidoPorOrigem() {
        return sucedidoPorOrigem;
    }

    public void setSucedidoPorOrigem(SucedidoPor sucedidoPorOrigem) {
        this.sucedidoPorOrigem = sucedidoPorOrigem;
    }

    public SucedidoPor getSucedidoPorDestino() {
        return sucedidoPorDestino;
    }

    public void setSucedidoPorDestino(SucedidoPor sucedidoPorDestino) {
        this.sucedidoPorDestino = sucedidoPorDestino;
    }

    public List<TrocaMensagemCom> getTrocaMensagemComOrigemList() {
        return trocaMensagemComOrigemList;
    }

    public void setTrocaMensagemComOrigemList(List<TrocaMensagemCom> trocaMensagemComOrigemList) {
        this.trocaMensagemComOrigemList = trocaMensagemComOrigemList;
    }

    public List<TrocaMensagemCom> getTrocaMensagemComDestinoList() {
        return trocaMensagemComDestinoList;
    }

    public void setTrocaMensagemComDestinoList(List<TrocaMensagemCom> trocaMensagemComDestinoList) {
        this.trocaMensagemComDestinoList = trocaMensagemComDestinoList;
    }

    public List<ProduzSaida> getProduzSaidaList() {
        return produzSaidaList;
    }

    public void setProduzSaidaList(List<ProduzSaida> produzSaidaList) {
        this.produzSaidaList = produzSaidaList;
    }

    public boolean getIsRegraDeNegocio() {
        return isRegraDeNegocio;
    }

    public void setIsRegraDeNegocio(boolean isRegraDeNegocio) {
        this.isRegraDeNegocio = isRegraDeNegocio;
    }

    public boolean getIsRequisitoFuncional() {
        return isRequisitoFuncional;
    }

    public void setIsRequisitoFuncional(boolean isRequisitoFuncional) {
        this.isRequisitoFuncional = isRequisitoFuncional;
    }

    public boolean getIsRequisitoNaoFuncional() {
        return isRequisitoNaoFuncional;
    }

    public void setIsRequisitoNaoFuncional(boolean isRequisitoNaoFuncional) {
        this.isRequisitoNaoFuncional = isRequisitoNaoFuncional;
    }

    public String getRegraDeNegocio() {
        return regraDeNegocio;
    }

    public void setRegraDeNegocio(String regraDeNegocio) {
        this.regraDeNegocio = regraDeNegocio;
    }

    public boolean getFimFluxo() {
        return fimFluxo;
    }

    public void setFimFluxo(boolean fimFluxo) {
        this.fimFluxo = fimFluxo;
    }    

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + Objects.hashCode(this.idElemento);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Elemento other = (Elemento) obj;
        return Objects.equals(this.idElemento, other.idElemento);
    }

    @Override
    public String toString() {
        return "Elemento{" + "idElemento=" + idElemento + '}';
    }

}
