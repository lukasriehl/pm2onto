package com.lukas.pm2onto.model;

import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author lukas
 */
@Entity
@Table(name = "modelo", schema = "pm2onto")
public class Modelo implements java.io.Serializable {
    
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    private Long id;
    
    @NotNull
    @Column(name = "id_modelo")
    @Size(min = 36, max = 36)
    private String idModelo;
    
    @NotNull
    @Column(name = "nome")
    @Size(max = 100)
    private String nome;

    @NotNull
    @Column(name = "descricao")
    private String descricao;

    @Column(name = "documentacao")
    private String documentacao;

    //Atributos do XPDL
    @Column(name = "data_criacao")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dataCriacao;

    @Column(name = "data_modificacao")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dataModificacao;

    @Column(name = "autor")
    @Size(max = 100)
    private String autor;

    @Column(name = "caminho_arquivo")
    @Size(max = 100)
    private String caminhoArquivo;

    @Column(name = "id_documento")
    @Size(max = 100)
    private String idDocumento;
    //Fim dos Atributos do XPDL

    //    @NotNull
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_ontologia", referencedColumnName = "id_ontologia")
    private Ontologia ontologia;

    @Transient
    //@OneToMany(mappedBy = "modelo", cascade = CascadeType.ALL)
    private List<Processo> processoList;

    @Transient
    //@OneToMany(mappedBy = "modelo", cascade = CascadeType.ALL)
    private List<TrocaMensagemCom> trocaMensagemComList;

    public Modelo() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }    

    public String getIdModelo() {
        return idModelo;
    }

    public void setIdModelo(String idModelo) {
        this.idModelo = idModelo;
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

    public Date getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(Date dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public Date getDataModificacao() {
        return dataModificacao;
    }

    public void setDataModificacao(Date dataModificacao) {
        this.dataModificacao = dataModificacao;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getCaminhoArquivo() {
        return caminhoArquivo;
    }

    public void setCaminhoArquivo(String caminhoArquivo) {
        this.caminhoArquivo = caminhoArquivo;
    }

    public String getIdDocumento() {
        return idDocumento;
    }

    public void setIdDocumento(String idDocumento) {
        this.idDocumento = idDocumento;
    }

    public Ontologia getOntologia() {
        return ontologia;
    }

    public void setOntologia(Ontologia ontologia) {
        this.ontologia = ontologia;
    }

    public List<Processo> getProcessoList() {
        return processoList;
    }

    public void setProcessoList(List<Processo> processoList) {
        this.processoList = processoList;
    }

    public List<TrocaMensagemCom> getTrocaMensagemComList() {
        return trocaMensagemComList;
    }

    public void setTrocaMensagemComList(List<TrocaMensagemCom> trocaMensagemComList) {
        this.trocaMensagemComList = trocaMensagemComList;
    }
}
