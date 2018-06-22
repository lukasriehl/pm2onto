package com.lukas.pm2onto.model;

import com.lukas.pm2onto.model.enumerador.TipoArtefato;
import java.util.List;

/**
 *
 * @author lukas
 */
//@Entity
//@Table(name = "grupo", schema = "pm2onto")
public class Grupo extends Artefato {

    //@OneToMany(mappedBy = "grupo", cascade = CascadeType.ALL)
    private List<Atividade> atividadeList;

    //@OneToMany(mappedBy = "grupo", cascade = CascadeType.ALL)
    private List<Evento> eventoList;

    //@OneToMany(mappedBy = "grupo", cascade = CascadeType.ALL)
    private List<Gateway> gatewayList;

    //@OneToMany(mappedBy = "grupo", cascade = CascadeType.ALL)
    private List<Artefato> artefatoList;

    public Grupo() {
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

    @Override
    public void setTipo(TipoArtefato tipo) {
        tipo = TipoArtefato.Group;
        super.setTipo(tipo);
    }

    @Override
    public TipoArtefato getTipo() {
        return TipoArtefato.Group;
    }
}
