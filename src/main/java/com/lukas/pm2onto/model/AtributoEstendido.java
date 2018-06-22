package com.lukas.pm2onto.model;

import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author lukas
 */
//@Entity
//@Table(name = "atributo_estendido", schema = "pm2onto")
public class AtributoEstendido implements Serializable {
    
    private static final long serialVersionUID = 1L;

//    @Id
//    @Column(name = "id_elemento")
//    @Size(min = 36, max = 36)
    private String idElemento;

//    @NotNull
//    @Column(name = "nome")
    private String nome;
    
//    @NotNull
//    @Column(name = "valor")
    private String valor;

//    @ManyToOne
//    @JoinColumn(name = "id_elemento", referencedColumnName = "id_elemento")
    private Elemento elemento;
    
    //@Transient
    private boolean isRegraNegocio;
    
    //@Transient    
    private boolean isRequisitoNaoFuncional;
    
    //@Transient    
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

    public Elemento getElemento() {
        return elemento;
    }

    public void setElemento(Elemento elemento) {
        this.elemento = elemento;
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
