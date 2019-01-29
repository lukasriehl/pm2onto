package com.lukas.pm2onto.model;

import com.lukas.pm2onto.model.enumerador.TipoArtefato;
import java.util.List;

/**
 *
 * @author lukas
 */
public class Anotacao extends Artefato {

    private List<Atividade> atividadeList;

    private List<Evento> eventoList;

    private List<Gateway> gatewayList;

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
