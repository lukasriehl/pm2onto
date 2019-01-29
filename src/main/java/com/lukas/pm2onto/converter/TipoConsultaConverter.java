package com.lukas.pm2onto.converter;

import com.lukas.pm2onto.bo.ConsultaOntologiaBO;
import com.lukas.pm2onto.tiposconsultas.TipoConsulta;
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
@FacesConverter("tipoConsultaConverter")
public class TipoConsultaConverter implements Converter {

    private ConsultaOntologiaBO bo;

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value != null && value.trim().length() > 0) {
            bo = new ConsultaOntologiaBO();
            try {
                List<TipoConsulta> tipoConsultaList = bo.retornaTiposConsultas();
                if (tipoConsultaList != null && !tipoConsultaList.isEmpty()) {
                    tipoConsultaList = tipoConsultaList.stream().filter(tip -> tip.getNome().equals(value))
                            .collect(Collectors.toList());
                    return tipoConsultaList != null && !tipoConsultaList.isEmpty() ?
                            tipoConsultaList.get(0) : null;
                } else {
                    return null;
                }
            } catch (NumberFormatException e) {
                throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao realizar conversão"
                        + "de tipos de consultas."));
            }
        } else {
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value != null) {
            TipoConsulta tipoConsulta = (TipoConsulta) value;
            return tipoConsulta.getNome();
        } else {
            return null;
        }        
    }

}
