/*
 *********************************************************************
 Objetivo: Enum Parámetros del Sistema Comprobantes Electrónicos Offline
 ********************************************************************** 
 MODIFICACIONES
 FECHA                     AUTOR             RAZON
 01-Sep-2016             D. Jácome        Emision Inicial
 ********************************************************************** 
 */
package dj.comprobantes.offline.enums;

/**
 *
 * @author diego.jacome
 */
public enum ParametrosSistemaEnum {

    //RUTA_SISTEMA("C:\\ComprobantesElectronicosSRIOffline"), //Ruta del sistema 
    RUTA_SISTEMA("/opt/rua"), //Ruta del sistema RUA
    PROXY_HOST(""),
    PROXY_PORT(""),
    MAIL_SMTP_HOST("smtp.office365.com"),
    MAIL_SMTP_PORT("587"),//25  //465
    MAIL_PASSWORD("Inspectoria2022"),
    MAIL_GENERIC("comprobantes2@salesianos.org.ec"),
    PAGINA_CONSULTA("http://comprobantes.salesianos.org.ec"),
    CPANEL_WEB_COMPROBANTE("http://192.168.1.205/framework/servicios/ServicioNube.php/subirComprobante"),
    CPANEL_WEB_REENVIAR("http://192.168.1.205/framework/servicios/ServicioNube.php/reenviarComprobante");

    private final String codigo;

    private ParametrosSistemaEnum(final String codigo) {
        this.codigo = codigo;
    }

    /**
     * obtiene el codigo de la numeracion
     *
     * @return
     */
    public String getCodigo() {
        return codigo;
    }
}
