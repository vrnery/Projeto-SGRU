/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifrs.restinga.sgru.conversor;

import br.edu.ifrs.restinga.sgru.modelo.TipoFuncionario;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author marcelo.lima
 */
@FacesConverter(forClass = TipoFuncionario.class)
public class ConversorTipoFuncionario implements Converter {

    @Override
    public Object getAsObject(FacesContext fc, UIComponent uic, String string) {
        if ((string != null) && (!string.isEmpty())) {
            return (TipoFuncionario) uic.getAttributes().get(string);
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext fc, UIComponent uic, Object o) {
        if (o instanceof TipoFuncionario) {
            TipoFuncionario tipoFuncionario = (TipoFuncionario) o;            
            uic.getAttributes().put(String.valueOf(tipoFuncionario.getId()), tipoFuncionario);
            return String.valueOf(tipoFuncionario.getId());            
        }
        return "";
    }
    
}
