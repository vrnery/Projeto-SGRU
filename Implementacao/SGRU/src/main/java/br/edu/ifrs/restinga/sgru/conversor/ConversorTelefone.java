package br.edu.ifrs.restinga.sgru.conversor;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter("conversorTelefone")
public class ConversorTelefone implements Converter {    
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String modelValue) {
        String telefone = (String) modelValue;
        StringBuilder telefoneFormatado = new StringBuilder(telefone);
        
        telefoneFormatado.insert(0, '(');
        telefoneFormatado.insert(3, ')');
        telefoneFormatado.insert(4, ' ');
        telefoneFormatado.insert(9, '-');
        
        return telefoneFormatado.toString();
     }    
    
    @Override
    public String getAsString(FacesContext context, UIComponent component, Object modelValue) {
        String telefone = (String) modelValue;
        StringBuilder telefoneFormatado = new StringBuilder(telefone);
        
        telefoneFormatado.insert(0, '(');
        telefoneFormatado.insert(3, ')');
        telefoneFormatado.insert(4, ' ');
        telefoneFormatado.insert(9, '-');
        
        return telefoneFormatado.toString();
    }      
}