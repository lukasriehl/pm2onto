package com.lukas.pm2onto.model;

import com.lukas.pm2onto.model.enumerador.TipoArtefato;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author lukas
 */
//@Entity
//@Table(name = "anotacao", schema = "pm2onto")
public class Anotacao extends Artefato {

    //@OneToMany(mappedBy = "anotacao", cascade = CascadeType.ALL)
    private List<Atividade> atividadeList;

    //@OneToMany(mappedBy = "anotacao", cascade = CascadeType.ALL)
    private List<Evento> eventoList;

    //@OneToMany(mappedBy = "anotacao", cascade = CascadeType.ALL)
    private List<Gateway> gatewayList;

    //@OneToMany(mappedBy = "anotacao", cascade = CascadeType.ALL)
    private List<Artefato> artefatoList;

    public Anotacao() {

    }

    @Override
    public void setTipo(TipoArtefato tipo) {
        tipo = TipoArtefato.Annotation;
        super.setTipo(tipo);
    }

    @Override
    public TipoArtefato getTipo() {
        return TipoArtefato.Annotation;
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
}
