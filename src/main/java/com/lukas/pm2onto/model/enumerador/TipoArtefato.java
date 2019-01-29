package com.lukas.pm2onto.model.enumerador;

/**
 *
 * @author lukas
 */
public enum TipoArtefato {
    
    DataObject(Short.valueOf("0")),
    DataStore(Short.valueOf("1")),
    Group(Short.valueOf("2")),
    Annotation(Short.valueOf("3"));
    
    private Short id;
    
    TipoArtefato(Short id){
        this.id = id;
    }

    public Short getId() {
        return id;
    }
    
}
