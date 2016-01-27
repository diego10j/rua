/*
 * Autor: Diego Jácome   11-03-2015
 */
package comprobantesElectronicos.ejb;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.ejb.Singleton;

/**
 *
 * @author dfjacome
 */
@Singleton
public class ejbUtilitario {

    public final static String FORMATO_FECHA = "yyyy-MM-dd";

    public boolean isFechaMayor(Date fecha1, Date fecha2) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(fecha1);
        cal2.setTime(fecha2);
        if (cal2.before(cal1)) {
            return true;
        }
        return false;
    }

    public String getFormatoFecha(Object fecha) {
        try {
            SimpleDateFormat formatoFecha = new SimpleDateFormat(FORMATO_FECHA);
            return formatoFecha.format(fecha);
        } catch (Exception e) {
        }
        return (String) fecha;
    }

    public String getFormatoFecha(Object fecha, String formato) {
        try {
            SimpleDateFormat formatoFecha = new SimpleDateFormat(formato);
            return formatoFecha.format(fecha);
        } catch (Exception e) {
        }
        return (String) fecha;
    }

    public String getValorEtiqueta(String cadenaXML, String etiqueta) {
        String str_valor = "";
        try {
            String str_etiqueta1 = "<" + etiqueta + ">";
            String str_etiqueta2 = "</" + etiqueta + ">";
            str_valor = cadenaXML.substring((cadenaXML.indexOf(str_etiqueta1) + str_etiqueta1.length()), (cadenaXML.indexOf(str_etiqueta2)));
            str_valor = str_valor.trim();
        } catch (Exception e) {
        }
        return str_valor;
    }
}
