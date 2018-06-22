package com.lukas.pm2onto.tiposconsultas;

import java.util.Objects;

/**
 *
 * @author lukas
 */
public class SubTipoElemento {

    private Short id;
    private String nome;
    private String nomeComPrefixo;
    private String nomePT;

    public SubTipoElemento() {
    }

    public SubTipoElemento(Short id, String nome) {
        this.id = id;
        this.nome = nome;
    }
    
    public SubTipoElemento(Short id, String nome, String nomePT, String nomeComPrefixo) {
        this.id = id;
        this.nome = nome;
        this.nomePT = nomePT;
        this.nomeComPrefixo = nomeComPrefixo;
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

    public String getNomeComPrefixo() {
        return nomeComPrefixo;
    }

    public void setNomeComPrefixo(String nomeComPrefixo) {
        this.nomeComPrefixo = nomeComPrefixo;
    }   

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 11 * hash + Objects.hashCode(this.id);
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
        final SubTipoElemento other = (SubTipoElemento) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return nome;
    }
}
