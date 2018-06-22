package com.lukas.pm2onto.model;

import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author lukas
 */
//@Entity
//@Table(name = "troca_mensagem_com", schema = "pm2onto")
public class TrocaMensagemCom implements Serializable {

    private static final long serialVersionUID = 1L;

//    @Id
//    @Column(name = "id_troca_mensagem_com")
//    @Size(min = 36, max = 36)
    private String idTrocaMensagemCom;
    
//    @NotNull
//    @Column(name = "nome")
    private String nome;

    //Pode ser uma raia, atividade ou evento em BPMN
//    @JoinColumn(name = "id_elemento_origem", referencedColumnName = "id_elemento")
//    @ManyToOne
    private Elemento elementoOrigem;

//    @JoinColumn(name = "id_elemento_destino", referencedColumnName = "id_elemento")
//    @ManyToOne
    private Elemento elementoDestino;
    
//    @JoinColumn(name = "id_modelo", referencedColumnName = "id_elemento")
//    @ManyToOne
    private Modelo modelo;

    public TrocaMensagemCom() {

    }

    public String getIdTrocaMensagemCom() {
        return idTrocaMensagemCom;
    }

    public void setIdTrocaMensagemCom(String idTrocaMensagemCom) {
        this.idTrocaMensagemCom = idTrocaMensagemCom;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
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

    public Modelo getModelo() {
        return modelo;
    }

    public void setModelo(Modelo modelo) {
        this.modelo = modelo;
    }    

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.idTrocaMensagemCom);
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
        final TrocaMensagemCom other = (TrocaMensagemCom) obj;
        if (!Objects.equals(this.idTrocaMensagemCom, other.idTrocaMensagemCom)) {
            return false;
        }
        return true;
    }
}
