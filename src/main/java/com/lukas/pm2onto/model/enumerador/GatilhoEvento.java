package com.lukas.pm2onto.model.enumerador;

/**
 *
 * @author lukas
 */
public enum GatilhoEvento {
    
    None(Short.valueOf("0")),
    Message(Short.valueOf("1")),
    Time(Short.valueOf("2")),
    Condition(Short.valueOf("3")),
    Signal(Short.valueOf("4")),
    Multiple(Short.valueOf("5")),
    ParallelMultiple(Short.valueOf("6")),
    Link(Short.valueOf("7")),
    Compensation(Short.valueOf("8")),
    Scale(Short.valueOf("9")),
    Exception(Short.valueOf("10")),
    Cancel(Short.valueOf("11")),
    EndTrigger(Short.valueOf("12"));
    
    private Short id;
    
    GatilhoEvento(Short id){
        this.id = id;
    }

    public Short getId() {
        return id;
    }
    
}
