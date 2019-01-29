package com.lukas.pm2onto.model;

import com.lukas.pm2onto.model.enumerador.TipoCondicao;
import java.util.Objects;

/**
 *
 * @author lukas Classe que representa as transições. Elas podem ocorrer entre
 * uma atividade e uma outra atividade, atividade e evento, atividade e gateway,
 * evento e outro evento, evento e atividade, evento e gateway, gateway e outro
 * gateway, gateway e atividade e gateway e evento.
 */
public class SucedidoPor implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    private String idSucedidoPor;

    private Elemento elementoOrigem;

    private Elemento elementoDestino;

    private TipoCondicao tipoCondicao;

    private String descricaoCondicao;

    private String nomeTransicao;

    public SucedidoPor() {
    }

    public String getIdSucedidoPor() {
        return idSucedidoPor;
    }

    public void setIdSucedidoPor(String idSucedidoPor) {
        this.idSucedidoPor = idSucedidoPor;
    }

    public Elemento getElementoOrigem() {
        return elementoOrigem;
    }

    public void setElementoOrigem(Elemento elementoOrigem) {
        this.elementoOrigem = elementoOrigem;
    }

    public Elemento getElementoDestino() {
        return elementoDestino;
    }

    public void setElementoDestino(Elemento elementoDestino) {
        this.elementoDestino = elementoDestino;
    }

    public TipoCondicao getTipoCondicao() {
        return tipoCondicao;
    }

    public void setTipoCondicao(TipoCondicao tipoCondicao) {
        this.tipoCondicao = tipoCondicao;
    }

    public String getDescricaoCondicao() {
        return descricaoCondicao;
    }

    public void setDescricaoCondicao(String descricaoCondicao) {
        this.descricaoCondicao = descricaoCondicao;
    }

    public String getNomeTransicao() {
        return nomeTransicao;
    }

    public void setNomeTransicao(String nomeTransicao) {
        this.nomeTransicao = nomeTransicao;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + Objects.hashCode(this.idSucedidoPor);
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
        final SucedidoPor other = (SucedidoPor) obj;
        return Objects.equals(this.idSucedidoPor, other.idSucedidoPor);
    }
}
