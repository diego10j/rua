/*
 * Copyright (c) 2012, Diego Fernando Jácome. All rights reserved.
 */
package portal.convertidores;

import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;


@FacesConverter("ConvertidorAutoCompletar")
public class ConvertidorAutoCompletar implements Converter {

     private  List lis_opciones;

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent component, String submittedValue) {
        if (submittedValue == null || submittedValue.trim().equals("")) {
            return null;
        } else {
            try {
                String value = submittedValue;                
               lis_opciones=(List) component.getAttributes().get("lisOpciones");                
                if (lis_opciones != null) {
                    for (int i = 0; i < lis_opciones.size(); i++) {
                        Object[] fila = (Object[]) lis_opciones.get(i);                        
                            String f = fila[0] + "";                            
                            if (f.trim().equals(value)) {
                                return fila;
                            }                        
                    }
                }
            } catch (Exception exception) {
                exception.printStackTrace();
                throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Not a valid "));
            }
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent component, Object value) {
        if (value == null) {
            return null;
        } else {
            try {
                String v = (((Object[]) value)[0]) + "";
                return v;
            } catch (Exception e) {
            }
        }
        return null;
    }

    public List getLis_opciones() {
        return lis_opciones;
    }

    public void setLis_opciones(List lis_opciones) {
        this.lis_opciones = lis_opciones;
    }
    
}
