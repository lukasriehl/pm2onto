package com.lukas.pm2onto.model.enumerador;

/**
 *
 * @author lukas
 */
public enum TipoAtividade {
    
    Simple(Short.valueOf("0")),
    User(Short.valueOf("1")),
    Script(Short.valueOf("2")),
    Service(Short.valueOf("3")),
    Receive(Short.valueOf("4")),
    Send(Short.valueOf("5")),
    Manual(Short.valueOf("6")),
    /*O valor a seguir é importante, pois irá representar as regras de negócio,
     elementos importantes para os requisitos.*/
    BusinessRule(Short.valueOf("7")),
    SubProcess(Short.valueOf("8"));
    
    private Short id;
    
    TipoAtividade(Short id){
        this.id = id;
    }

    public Short getId() {
        return id;
    }    
}
