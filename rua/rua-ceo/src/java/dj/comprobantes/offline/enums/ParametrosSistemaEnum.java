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

    ///GLOBALQUIM
    RUTA_SISTEMA("C:\\ComprobantesElectronicosSRIOffline"), //Ruta del sistema
    //RUTA_SISTEMA("/opt/rua"), //Ruta del sistema RUA
    PROXY_HOST(""),
    PROXY_PORT(""),
    MAIL_SMTP_HOST("mail.globalquim.com.ec"),
    MAIL_SMTP_PORT(""),//25  //465
    MAIL_PASSWORD(""),
    MAIL_GENERIC("comprobantes@globalquim.com.ec"),
    PAGINA_CONSULTA("http://www.comprobantes.globalquim.com.ec"),
    CPANEL_WEB_COMPROBANTE("http://comprobantes.globalquim.com.ec/framework/servicios/ServicioNube.php/subirComprobante"),
    CPANEL_WEB_REENVIAR("http://comprobantes.globalquim.com.ec/framework/servicios/ServicioNube.php/reenviarComprobante"),
    CPANEL_WEB_DETALLE_COMPROBANTE("http://comprobantes.globalquim.com.ec/framework/servicios/ServicioNube.php/subirDetalleComprobante");
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
