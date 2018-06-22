package com.lukas.pm2onto.model;

import java.util.List;

/**
 *
 * @author lukas
 */
//@Entity
//@Table(name = "processo", schema = "pm2onto")
public class Processo extends Elemento {

//    @NotNull
//    @Column(name = "tipo_processo")
//    @Size(max = 45)
    private String tipoProcesso;

    //@OneToMany(mappedBy = "processo", cascade = CascadeType.ALL)
    private List<SubProcesso> subProcessoList;

    //@OneToMany(mappedBy = "processo", cascade = CascadeType.ALL)
    private List<Atividade> atividadeList;

    //@OneToMany(mappedBy = "processo", cascade = CascadeType.ALL)
    private List<Evento> eventoList;

    //@OneToMany(mappedBy = "processo", cascade = CascadeType.ALL)
    private List<Gateway> gatewayList;

    //@OneToMany(mappedBy = "processo", cascade = CascadeType.ALL)
    private List<Artefato> artefatoList;

    //@OneToMany(mappedBy = "processo", cascade = CascadeType.ALL)
    private List<Ator> atorList;
    
    private List<Piscina> piscinaList;

    //    @NotNull
    //@ManyToOne
    //@JoinColumn(name = "id_modelo", referencedColumnName = "id_elemento")
    private Modelo modelo;

    //@Transient
    private List<ExecutadoPor> executadoPorList;

    //@Transient
    private List<SucedidoPor> sucedidoPorList;
    
    //@Transient
    private List<UtilizaEntrada> utilizaEntradaList;

    //@Transient
    private List<ProduzSaida> produzSaidaList;

    public Processo() {
    }

    public String getTipoProcesso() {
        return tipoProcesso;
    }

    public void setTipoProcesso(String tipoProcesso) {
        this.tipoProcesso = tipoProcesso;
    }

    public List<SubProcesso> getSubProcessoList() {
        return subProcessoList;
    }

    public void setSubProcessoList(List<SubProcesso> subProcessoList) {
        this.subProcessoList = subProcessoList;
    }

    public List<Atividade> getAtividadeList() {
        return atividadeList;
    }

    public void setAtividadeList(List<Atividade> atividadeList) {
        this.atividadeList = atividadeList;
    }

    public List<Evento> getEventoList() {
        return eventoList;
    }

    public void setEventoList(List<Evento> eventoList) {
        this.eventoList = eventoList;
    }

    public List<Gateway> getGatewayList() {
        return gatewayList;
    }

    public void setGatewayList(List<Gateway> gatewayList) {
        this.gatewayList = gatewayList;
    }

    public List<Artefato> getArtefatoList() {
        return artefatoList;
    }

    public void setArtefatoList(List<Artefato> artefatoList) {
        this.artefatoList = artefatoList;
    }

    public List<Ator> getAtorList() {
        return atorList;
    }

    public void setAtorList(List<Ator> atorList) {
        this.atorList = atorList;
    }

    public List<Piscina> getPiscinaList() {
        return piscinaList;
    }

    public void setPiscinaList(List<Piscina> piscinaList) {
        this.piscinaList = piscinaList;
    }

    public Modelo getModelo() {
        return modelo;
    }

    public void setModelo(Modelo modelo) {
        this.modelo = modelo;
    }

    public List<ExecutadoPor> getExecutadoPorList() {
        return executadoPorList;
    }

    public void setExecutadoPorList(List<ExecutadoPor> executadoPorList) {
        this.executadoPorList = executadoPorList;
    }

    public List<SucedidoPor> getSucedidoPorList() {
        return sucedidoPorList;
    }

    public void setSucedidoPorList(List<SucedidoPor> sucedidoPorList) {
        this.sucedidoPorList = sucedidoPorList;
    }
    
    public List<UtilizaEntrada> getUtilizaEntradaList() {
        return utilizaEntradaList;
    }

    public void setUtilizaEntradaList(List<UtilizaEntrada> utilizaEntradaList) {
        this.utilizaEntradaList = utilizaEntradaList;
    }

    public List<ProduzSaida> getProduzSaidaList() {
        return produzSaidaList;
    }

    public void setProduzSaidaList(List<ProduzSaida> produzSaidaList) {
        this.produzSaidaList = produzSaidaList;
    }
}
