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

    RUTA_SISTEMA("C:\\ComprobantesElectronicosSRIOffline"), //Ruta del sistema 
    //RUTA_SISTEMA("/opt/rua"), //Ruta del sistema RUA
    PROXY_HOST(""),
    PROXY_PORT(""),
    MAIL_SMTP_HOST("smtp.office365.com"),
    MAIL_SMTP_PORT("587"),//25  //465
    MAIL_PASSWORD("Inspectoria2018"),
    MAIL_GENERIC("comprobantes@salesianos.org.ec"),
    PAGINA_CONSULTA("http://comprobates.salesianos.org.ec"),
    CPANEL_WEB_COMPROBANTE("http://comprobates.salesianos.org.ec/framework/servicios/ServicioNube.php/subirComprobante"),
    CPANEL_WEB_REENVIAR("http://comprobates.salesianos.org.ec/framework/servicios/ServicioNube.php/reenviarComprobante"),
    CPANEL_WEB_DETALLE_COMPROBANTE("http://comprobates.salesianos.org.ec/framework/servicios/ServicioNube.php/subirDetalleComprobante");
    ///

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
