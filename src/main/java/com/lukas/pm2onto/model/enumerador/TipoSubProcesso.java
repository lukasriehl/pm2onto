package com.lukas.pm2onto.model.enumerador;

/**
 *
 * @author lukas
 */
public enum TipoSubProcesso {

    Reusable(Short.valueOf("0")),
    Transactional(Short.valueOf("1")),
    Adhoc(Short.valueOf("2")),
    Loop(Short.valueOf("3")),
    ParallelMultipleInstances(Short.valueOf("4")),
    SequenceMultipleInstances(Short.valueOf("5")),
    Simple(Short.valueOf("6")),
    Event(Short.valueOf("7"));        

    private Short id;

    TipoSubProcesso(Short id) {
        this.id = id;
    }

    public Short getId() {
        return id;
    }
}
