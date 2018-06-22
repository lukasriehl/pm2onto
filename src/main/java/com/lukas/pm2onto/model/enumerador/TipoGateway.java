package com.lukas.pm2onto.model.enumerador;

/**
 *
 * @author lukas
 */
public enum TipoGateway {
    
    Exclusive(Short.valueOf("0")),
    Parallel(Short.valueOf("1")),
    Inclusive(Short.valueOf("2")),
    EventBased(Short.valueOf("3")),
    ExclusiveEventBased(Short.valueOf("4")),
    ParallelEventBased(Short.valueOf("5")),
    Complex(Short.valueOf("6"));
    
    private Short id;
    
    TipoGateway(Short id){
        this.id = id;
    }

    public Short getId() {
        return id;
    }
}
