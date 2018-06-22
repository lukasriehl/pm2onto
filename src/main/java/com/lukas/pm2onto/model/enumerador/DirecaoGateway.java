package com.lukas.pm2onto.model.enumerador;

/**
 *
 * @author lukas
 */
public enum DirecaoGateway {
    
    Diverging(Short.valueOf("0")),
    Converging(Short.valueOf("1"));
    
    private Short id;
    
    DirecaoGateway(Short id){
        this.id = id;
    }

    public Short getId() {
        return id;
    }
    
}
