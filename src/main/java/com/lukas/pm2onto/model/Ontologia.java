package com.lukas.pm2onto.model;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author lukas
 */
@Entity
@Table(name = "ontologia", schema = "pm2onto")
public class Ontologia implements java.io.Serializable {

    @Id
    @Column(name = "id_ontologia")
    private Long idOntologia;

    @NotNull
    @Column(name = "nome")
    @Size(max = 50)
    private String nome;
    
    @Column(name = "descricao")
    private String descricao;

    @NotNull
    @Column(name = "arquivo")
    private byte[] arquivo;

    @Temporal(TemporalType.DATE)
    @Column(name = "data_criacao")
    private Date dataCriacao;

    @OneToMany(mappedBy = "ontologia", cascade = CascadeType.ALL)
    private List<Modelo> modeloList;

    public Ontologia() {

    }

    public Long getIdOntologia() {
        return idOntologia;
    }

    public void setIdOntologia(Long idOntologia) {
        this.idOntologia = idOntologia;
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

    public Date getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(Date dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public List<Modelo> getModeloList() {
        return modeloList;
    }

    public void setModeloList(List<Modelo> modeloList) {
        this.modeloList = modeloList;
    }

    public byte[] getArquivo() {
        return arquivo;
    }

    public void setArquivo(byte[] arquivo) {
        this.arquivo = arquivo;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 59 * hash + Objects.hashCode(this.idOntologia);
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
        final Ontologia other = (Ontologia) obj;
        return Objects.equals(this.idOntologia, other.idOntologia);
    }

}
