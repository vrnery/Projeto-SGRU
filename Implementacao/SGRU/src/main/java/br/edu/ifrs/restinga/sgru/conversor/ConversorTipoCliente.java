/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifrs.restinga.sgru.conversor;

import br.edu.ifrs.restinga.sgru.modelo.TipoCliente;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author marcelo.lima
 */
@FacesConverter(forClass = TipoCliente.class)
public class ConversorTipoCliente implements Converter {

    @Override
    public Object getAsObject(FacesContext fc, UIComponent uic, String string) {
        if ((string != null) && (!string.isEmpty())) {
            return (TipoCliente) uic.getAttributes().get(string);
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext fc, UIComponent uic, Object o) {
        if (o instanceof TipoCliente) {
            TipoCliente tipoCliente = (TipoCliente) o;            
            uic.getAttributes().put(String.valueOf(tipoCliente.getId()), tipoCliente);
            return String.valueOf(tipoCliente.getId());            
        }
        return "";
    }
    
}
