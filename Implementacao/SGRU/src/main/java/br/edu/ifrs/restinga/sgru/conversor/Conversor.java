package br.edu.ifrs.restinga.sgru.conversor;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter("conversor")
public class Conversor implements Converter {    
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String modelValue) {
        if (modelValue != null && !modelValue.isEmpty()) {
            return (String) component.getAttributes().get(modelValue);
        }
        return null;
     }    
    
    @Override
    public String getAsString(FacesContext context, UIComponent component, Object modelValue) {
        if (modelValue instanceof String) {
            String valor = (String) modelValue;
            StringBuilder valorFormatado = new StringBuilder(valor);

            // Telefone
            if ((component.getId().equals("ofTelefone")) && (!valor.isEmpty())) {                
                valorFormatado.insert(0, '(');
                valorFormatado.insert(3, ')');
                valorFormatado.insert(4, ' ');
                valorFormatado.insert(9, '-');                
            }            
            
            if (!valor.isEmpty()) {
                return valorFormatado.toString();
            }
        } else {
            // Valor utilizado da recarga            
            if (component.getId().equals("ofUtilizado")) {
                Boolean valor = (Boolean) modelValue;
                return valor?"Sim":"Nao";
            }       
        }
        return "";
    }      
}