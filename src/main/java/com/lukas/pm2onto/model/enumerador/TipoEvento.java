package com.lukas.pm2onto.model.enumerador;

/**
 *
 * @author lukas
 */
public enum TipoEvento {
    
    Start(Short.valueOf("0")),
    Intermediate(Short.valueOf("1")),
    End(Short.valueOf("2"));
    
    private Short id;
    
    TipoEvento(Short id){
        this.id = id;
    }

    public Short getId() {
        return id;
    }
}
