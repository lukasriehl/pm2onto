package com.lukas.pm2onto.model.enumerador;

/**
 *
 * @author lukas
 */
public enum TipoCondicao {
    
    Condition(Short.valueOf("0")),
    Other(Short.valueOf("1")),
    Exception(Short.valueOf("2")),
    DefaultException(Short.valueOf("3"));
    
    private Short id;
    
    TipoCondicao(Short id){
        this.id = id;
    }

    public Short getId() {
        return id;
    }
}
