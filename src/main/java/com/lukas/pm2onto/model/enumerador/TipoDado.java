package com.lukas.pm2onto.model.enumerador;

import org.apache.jena.datatypes.xsd.XSDDatatype;

/**
 *
 * @author lukas
 */
public enum TipoDado {
    CHAR("CH", XSDDatatype.XSDstring),
    STRING("ST", XSDDatatype.XSDstring),
    INTEGER("IN", XSDDatatype.XSDinteger),
    FLOAT("FL", XSDDatatype.XSDfloat),
    DECIMAL("DE", XSDDatatype.XSDdecimal),
    DATE("DA", XSDDatatype.XSDdate),
    DATETIME("DT", XSDDatatype.XSDdateTime),
    BYTE("BY", XSDDatatype.XSDbyte),
    BOOLEAN("BO", XSDDatatype.XSDboolean);

    private String chave;
    private XSDDatatype valor;

    public static TipoDado getTipoDadoByChave(String chave) {
        if (chave == null || chave.isEmpty()) {
            return TipoDado.STRING;
        }

        for (TipoDado tipDad : TipoDado.values()) {
            if (tipDad.getChave().equals(chave)) {
                return tipDad;
            }
        }

        return TipoDado.STRING;
    }

    public String getChave() {
        return chave;
    }

    public void setChave(String chave) {
        this.chave = chave;
    }

    public XSDDatatype getValor() {
        return valor;
    }

    public void setValue(XSDDatatype valor) {
        this.valor = valor;
    }

    private TipoDado(String chave, XSDDatatype valor) {
        this.chave = chave;
        this.valor = valor;
    }
}
