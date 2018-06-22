package com.lukas.pm2onto.tiposconsultas;

import java.util.Objects;

/**
 *
 * @author lukas
 */
public class AtributoElemento {

    private Short id;
    private String nome;
    private String nomePT;

    public AtributoElemento() {
    }

    public AtributoElemento(Short id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public AtributoElemento(Short id, String nome, String nomePT) {
        this.id = id;
        this.nome = nome;
        this.nomePT = nomePT;
    }

    public Short getId() {
        return id;
    }

    public void setId(Short id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNomePT() {
        return nomePT;
    }

    public void setNomePT(String nomePT) {
        this.nomePT = nomePT;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 53 * hash + Objects.hashCode(this.id);
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
        final AtributoElemento other = (AtributoElemento) obj;
        return Objects.equals(this.id, other.id);
    }

    @Override
    public String toString() {
        return nome;
    }
}
