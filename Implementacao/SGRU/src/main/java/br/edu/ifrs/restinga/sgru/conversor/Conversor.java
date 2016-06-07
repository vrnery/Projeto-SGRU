package br.edu.ifrs.restinga.sgru.conversor;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter("conversor")
public class Conversor implements Converter {    
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
        if (modelValue instanceof String) {
            String valor = (String) modelValue;
            StringBuilder valorFormatado = new StringBuilder(valor);

            // Telefone
            if (component.getId().equals("ofTelefone")) {                
                valorFormatado.insert(0, '(');
                valorFormatado.insert(3, ')');
                valorFormatado.insert(4, ' ');
                valorFormatado.insert(9, '-');
            }

            return valorFormatado.toString();
        } else {
            // Valor utilizado da recarga
            Boolean valor = (Boolean) modelValue;            
            //if (component.getId().equals("ofUtilizado")) {
            return valor?"Sim":"Não";
            //}       
        }
    }      
}