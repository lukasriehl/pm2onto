package com.lukas.pm2onto.converter;

import com.lukas.pm2onto.bo.ConsultaOntologiaBO;
import com.lukas.pm2onto.tiposconsultas.SubTipoElemento;
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
@FacesConverter("subTipoElementoConverter")
public class SubTipoElementoConverter implements Converter {

    private ConsultaOntologiaBO bo;

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value != null && value.trim().length() > 0) {
            bo = new ConsultaOntologiaBO();
            try {
                List<SubTipoElemento> subTipoElementoList = bo.retornaSubTiposElementos();
                if (subTipoElementoList != null && !subTipoElementoList.isEmpty()) {
                    subTipoElementoList = subTipoElementoList.stream().filter(tip -> tip.getNome().equals(value))
                            .collect(Collectors.toList());
                    return subTipoElementoList != null && !subTipoElementoList.isEmpty()
                            ? subTipoElementoList.get(0) : null;
                } else {
                    return null;
                }
            } catch (NumberFormatException e) {
                throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao realizar conversão"
                        + "de subtipos de elementos."));
            }
        } else {
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value != null) {
            SubTipoElemento subTipoElemento = (SubTipoElemento) value;
            return subTipoElemento.getNome();
        } else {
            return null;
        }
    }

}
