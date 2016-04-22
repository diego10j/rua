/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servicios.contabilidad;

import framework.aplicacion.TablaGenerica;
import javax.ejb.Stateless;
import sistema.aplicacion.Utilitario;

/**
 *
 * @author diego.jacome
 */
@Stateless
public class ServicioComprobanteContabilidad {

    private final Utilitario utilitario = new Utilitario();

    public String getSqlTiposComprobante() {
        return "select ide_cntcm,nombre_cntcm from con_tipo_comproba where ide_empr=" + utilitario.getVariable("ide_empr");
    }

    public String getSqlEstadosComprobante() {
        return "select ide_cneco,nombre_cneco from con_estado_compro where ide_empr=" + utilitario.getVariable("ide_empr");
    }

    public String getSqlLugarAplica() {
        return "select ide_cnlap,nombre_cnlap from con_lugar_aplicac where ide_empr=" + utilitario.getVariable("ide_empr");

    }

    public String getSecuencial(String fecha_trans_cnccc) {
        //GENERA el número secuencial de la cabecera del comprobante
        String str_numero = null;
        String str_ano = utilitario.getAnio(fecha_trans_cnccc) + "";
        String str_mes = utilitario.getMes(fecha_trans_cnccc) + "";
        String str_ide_sucu = utilitario.getVariable("ide_sucu");
        //SELECCIONA EL MAXIMO SEGUN EL MES Y EL AÑO 
        TablaGenerica tab_max = utilitario.consultar("SELECT count(NUMERO_CNCCC) as cod,max(cast( substr(NUMERO_CNCCC,8) as NUMERIC)) AS MAXIMO FROM CON_CAB_COMP_CONT WHERE extract(year from FECHA_TRANS_CNCCC) ='" + str_ano + "' AND extract(month from FECHA_TRANS_CNCCC) ='" + str_mes + "' AND IDE_SUCU=" + str_ide_sucu);
        String str_maximo = "0";
        if (tab_max.getTotalFilas() > 0) {
            str_maximo = tab_max.getValor("MAXIMO");
            if (str_maximo == null || str_maximo.isEmpty()) {
                str_maximo = "0";
            }
            long lon_siguiente = 0;
            try {
                lon_siguiente = Long.parseLong(str_maximo) + 1;
            } catch (Exception e) {
            }
            str_maximo = lon_siguiente + "";
        }
        str_maximo = utilitario.generarCero(8 - str_maximo.length()) + str_maximo;
        str_numero = str_ano + str_mes + str_ide_sucu + str_maximo;
        return str_numero;
    }
}
