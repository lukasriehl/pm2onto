package com.lukas.pm2onto.model;

import com.lukas.pm2onto.model.enumerador.TipoDado;
import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author lukas
 */
public class AtributoEstendido implements Serializable {

    private static final long serialVersionUID = 1L;

    private String idElemento;

    private String nome;

    private String valor;
    
    private TipoDado tipoDado;

    private Elemento elementoPai;

    private boolean isRegraNegocio;

    private boolean isRequisitoNaoFuncional;

    private boolean isRequisitoFuncional;

    public AtributoEstendido() {
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

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public TipoDado getTipoDado() {
        return tipoDado;
    }

    public void setTipoDado(TipoDado tipoDado) {
        this.tipoDado = tipoDado;
    }   

    public Elemento getElementoPai() {
        return elementoPai;
    }

    public void setElementoPai(Elemento elementoPai) {
        this.elementoPai = elementoPai;
    }

    public boolean getIsRegraNegocio() {
        return isRegraNegocio;
    }

    public void setIsRegraNegocio(boolean isRegraNegocio) {
        this.isRegraNegocio = isRegraNegocio;
    }

    public boolean getIsRequisitoNaoFuncional() {
        return isRequisitoNaoFuncional;
    }

    public void setIsRequisitoNaoFuncional(boolean isRequisitoNaoFuncional) {
        this.isRequisitoNaoFuncional = isRequisitoNaoFuncional;
    }

    public boolean getIsRequisitoFuncional() {
        return isRequisitoFuncional;
    }

    public void setIsRequisitoFuncional(boolean isRequisitoFuncional) {
        this.isRequisitoFuncional = isRequisitoFuncional;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.idElemento);
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
        final AtributoEstendido other = (AtributoEstendido) obj;
        return Objects.equals(this.idElemento, other.idElemento);
    }

}
