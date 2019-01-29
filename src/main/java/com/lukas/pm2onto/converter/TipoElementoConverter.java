package com.lukas.pm2onto.converter;

import com.lukas.pm2onto.bo.ConsultaOntologiaBO;
import com.lukas.pm2onto.tiposconsultas.TipoElemento;
import java.util.List;
import java.util.stream.Collectors;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author lukas
 */
@FacesConverter("tipoElementoConverter")
public class TipoElementoConverter implements Converter {

    private ConsultaOntologiaBO bo;

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value != null && value.trim().length() > 0) {
            bo = new ConsultaOntologiaBO();
            try {
                List<TipoElemento> tipoElementoList = bo.retornaTiposElementos(true, true);
                if (tipoElementoList != null && !tipoElementoList.isEmpty()) {
                    tipoElementoList = tipoElementoList.stream().filter(tip -> tip.getNome().equals(value))
                            .collect(Collectors.toList());
                    return tipoElementoList != null && !tipoElementoList.isEmpty()
                            ? tipoElementoList.get(0) : null;
                } else {
                    return null;
                }
            } catch (NumberFormatException e) {
                throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao realizar conversão"
                        + "de tipos de elementos."));
            }
        } else {
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value != null) {
            TipoElemento tipoElemento = (TipoElemento) value;
            return tipoElemento.getNome();
        } else {
            return null;
        }
    }

}
