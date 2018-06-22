package com.lukas.pm2onto.dto;

import java.io.Serializable;

/**
 *
 * @author lukas
 */
public class ConsultaDTO implements Serializable {

    private String id;
    private String nome;
    private String descricao;
    private String documentacao;
    private String tipo;
    private String subTipo;
    private String idDest;
    private String nomeDest;
    private String descricaoDest;
    private String documentacaoDest;
    private String tipoDest;
    private String nomeAnt;
    private String descricaoAnt;
    private String documentacaoAnt;
    private String tipoAnt;

    public ConsultaDTO() {
    }

    public ConsultaDTO(String id, String nome, String descricao, String documentacao, String tipo, String subTipo) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.documentacao = documentacao;
        this.tipo = tipo;
        this.subTipo = subTipo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getSubTipo() {
        return subTipo;
    }

    public void setSubTipo(String subTipo) {
        this.subTipo = subTipo;
    }

    public String getIdDest() {
        return idDest;
    }

    public void setIdDest(String idDest) {
        this.idDest = idDest;
    }

    public String getNomeDest() {
        return nomeDest;
    }

    public void setNomeDest(String nomeDest) {
        this.nomeDest = nomeDest;
    }

    public String getDescricaoDest() {
        return descricaoDest;
    }

    public void setDescricaoDest(String descricaoDest) {
        this.descricaoDest = descricaoDest;
    }

    public String getDocumentacaoDest() {
        return documentacaoDest;
    }

    public void setDocumentacaoDest(String documentacaoDest) {
        this.documentacaoDest = documentacaoDest;
    }

    public String getTipoDest() {
        return tipoDest;
    }

    public void setTipoDest(String tipoDest) {
        this.tipoDest = tipoDest;
    }

    public String getNomeAnt() {
        return nomeAnt;
    }

    public void setNomeAnt(String nomeAnt) {
        this.nomeAnt = nomeAnt;
    }

    public String getDescricaoAnt() {
        return descricaoAnt;
    }

    public void setDescricaoAnt(String descricaoAnt) {
        this.descricaoAnt = descricaoAnt;
    }

    public String getDocumentacaoAnt() {
        return documentacaoAnt;
    }

    public void setDocumentacaoAnt(String documentacaoAnt) {
        this.documentacaoAnt = documentacaoAnt;
    }

    public String getTipoAnt() {
        return tipoAnt;
    }

    public void setTipoAnt(String tipoAnt) {
        this.tipoAnt = tipoAnt;
    }
    
}
