/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dj.comprobantes.offline.util;

import framework.aplicacion.Framework;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 *
 * @author djacome
 */
public class UtilitarioCeo extends Framework {

    /**
     * Retorna una fecha en formato especificado
     *
     * @param fecha
     * @param formato
     * @return
     */
    @Override
    public String getFormatoFecha(Object fecha, String formato) {
        try {
            SimpleDateFormat formatoFecha = new SimpleDateFormat(formato);
            String fechaStr = formatoFecha.format(fecha);
            fechaStr = fechaStr.toUpperCase();
            if (fechaStr != null) {
                fechaStr = fechaStr.replace("JANUARY ", "ENERO ");
                fechaStr = fechaStr.replace("FEBRUARY ", "FEBRERO ");
                fechaStr = fechaStr.replace("MARCH ", "MARZO ");
                fechaStr = fechaStr.replace("APRIL ", "ABRIL ");
                fechaStr = fechaStr.replace("MAY ", "MAYO ");
                fechaStr = fechaStr.replace("JUNE ", "JUNIO ");
                fechaStr = fechaStr.replace("JULY ", "JULIO ");
                fechaStr = fechaStr.replace("AUGUST ", "AGOSTO ");
                fechaStr = fechaStr.replace("SEPTEMBER ", "SEPTIEMBRE ");
                fechaStr = fechaStr.replace("OCTOBER ", "OCTUBRE ");
                fechaStr = fechaStr.replace("NOVEMBER ", "NOVIEMBRE ");
                fechaStr = fechaStr.replace("DECEMBER ", "DICIEMBRE ");
            }
            return fechaStr;
        } catch (Exception e) {
        }
        return (String) fecha;
    }

    /**
     * Transforma a Date de una fecha en un formato especifico
     *
     * @param fecha
     * @param formato
     * @return
     */
    public Date toDate(String fecha, String formato) {
        try {
            SimpleDateFormat formatoFecha = new SimpleDateFormat(formato);
            Date dat_fecha = formatoFecha.parse(fecha);
            return dat_fecha;
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * Valida una direccion de correo
     *
     * @param email
     * @return
     */
    public boolean isCorreoValido(String email) {
        Pattern pat = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
        Matcher mat = pat.matcher(email);
        if (mat.find()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Permite hacer una istalacion a certificados SSL para webservice
     *
     * @return
     */
    public String instalarCertificados() {
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            @Override
            public void checkClientTrusted(X509Certificate[] certs, String authType) {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] certs, String authType) {
            }
        }};

        // Install the all-trusting trust manager
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            return e.getMessage();
        }
        return null;
    }

    /**
     * Retorna un valor dentro de una estructura XML
     *
     * @param cadenaXML
     * @param etiqueta
     * @return
     */
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

    /**
     * Retorna una cantidad numerica sin decimales
     *
     * @param numero
     * @return
     */
    public String getFormatoNumeroEntero(Object numero) {
        try {
            Double ldob_valor = Double.parseDouble(numero.toString());
            return String.valueOf(ldob_valor.intValue());
        } catch (Exception ex) {
            return null;
        }
    }

    public String reemplazarCaracteresEspeciales(String xml) {
        //REMPLAZAR TILDES Y Ñ
        //remplaza ñ y tildes
        xml = xml.replaceAll("Ñ", "N");
        xml = xml.replaceAll("ñ", "n");
        xml = xml.replaceAll("Á", "A");
        xml = xml.replaceAll("á", "a");
        xml = xml.replaceAll("É", "E");
        xml = xml.replaceAll("é", "e");
        xml = xml.replaceAll("Í", "I");
        xml = xml.replaceAll("í", "i");
        xml = xml.replaceAll("ó", "o");
        xml = xml.replaceAll("Ó", "O");
        xml = xml.replaceAll("Ú", "U");
        xml = xml.replaceAll("ú", "u");
        xml = xml.replaceAll("&", "");
        return xml;
    }

    /**
     * Asigna un formato con una cantidad de decimales a una cantidad numérica
     *
     * @param numero
     * @param numero_decimales
     * @return
     */
    @Override
    public String getFormatoNumero(Object numero, int numero_decimales) {
        if (numero_decimales == 2) {
            numero = getFormatoNumero(numero, 4);
            String lstr_formato = "0";
            String ceros = "1";
            double dceros = 1;
            for (int i = 0; i < 2; i++) {
                if (i == 0) {
                    lstr_formato += ".";
                }
                lstr_formato += "0";
                ceros += "0";
            }
            ceros += ".00";
            dceros = Double.parseDouble(ceros);
            DecimalFormat formatoNumero;
            DecimalFormatSymbols idfs_simbolos = new DecimalFormatSymbols();
            idfs_simbolos.setDecimalSeparator('.');
            formatoNumero = new DecimalFormat(lstr_formato, idfs_simbolos);

            try {
                double ldob_valor = Double.parseDouble(numero.toString());
                ldob_valor = Math.round(ldob_valor * dceros) / dceros;
                return formatoNumero.format(ldob_valor);
            } catch (Exception ex) {
                return null;
            }
        }

        String lstr_formato = "0";
        String ceros = "1";
        double dceros = 1;
        for (int i = 0; i < numero_decimales; i++) {
            if (i == 0) {
                lstr_formato += ".";
            }
            lstr_formato += "0";
            ceros += "0";
        }
        ceros += ".00";
        dceros = Double.parseDouble(ceros);
        DecimalFormat formatoNumero;
        DecimalFormatSymbols idfs_simbolos = new DecimalFormatSymbols();
        idfs_simbolos.setDecimalSeparator('.');
        formatoNumero = new DecimalFormat(lstr_formato, idfs_simbolos);

        try {
            double ldob_valor = Double.parseDouble(numero.toString());
            ldob_valor = Math.round(ldob_valor * dceros) / dceros;
            return formatoNumero.format(ldob_valor);
        } catch (Exception ex) {
            return null;
        }
    }
}
