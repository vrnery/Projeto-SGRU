package br.edu.ifrs.restinga.sgru.conversor;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter("conversorMonetario")
public class ConversorMonetario implements Converter {    
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String modelValue) {
        if (modelValue != null && !modelValue.isEmpty()) {
            try {
                Locale ptBR = new Locale("pt", "BR");
                NumberFormat format = NumberFormat.getInstance(ptBR);
                format.setMaximumFractionDigits(2);                
                // Formata o numero com separador decimal "," para separador decimal "."
                Number number = format.parse(modelValue);
                return number.doubleValue();
            } catch (ParseException ex) {                
            }            
        }
        return null;
     }    
    
    @Override
    public String getAsString(FacesContext context, UIComponent component, Object modelValue) {
        DecimalFormat formatoBR = new DecimalFormat("##,###,###,##0.00", new DecimalFormatSymbols (new Locale ("pt", "BR")));
        formatoBR.setMaximumFractionDigits(2);
        // Formata o numero com separador decimal "." para separador decimal ","
        return formatoBR.format(modelValue);
    }      
}