package com.lukas.pm2onto.model;

import com.lukas.pm2onto.model.enumerador.TipoSubProcesso;

/**
 *
 * @author lukas
 */
public class SubProcesso extends Processo {

    private TipoSubProcesso tipo;

    private Processo processo;

    private Evento eventoGatilho;

    private boolean isSubFlow;

    private String idAtividadeOrigem;

    public SubProcesso() {
        this.isSubFlow = false;
    }

    public TipoSubProcesso getTipo() {
        return tipo;
    }

    public void setTipo(TipoSubProcesso tipo) {
        this.tipo = tipo;
    }

    public Processo getProcesso() {
        return processo;
    }

    public void setProcesso(Processo processo) {
        this.processo = processo;
    }

    public Evento getEventoGatilho() {
        return eventoGatilho;
    }

    public void setEventoGatilho(Evento eventoGatilho) {
        this.eventoGatilho = eventoGatilho;
    }

    public boolean getIsSubFlow() {
        return isSubFlow;
    }

    public void setIsSubFlow(boolean isSubFlow) {
        this.isSubFlow = isSubFlow;
    }

    public String getIdAtividadeOrigem() {
        return idAtividadeOrigem;
    }

    public void setIdAtividadeOrigem(String idAtividadeOrigem) {
        this.idAtividadeOrigem = idAtividadeOrigem;
    }
}
