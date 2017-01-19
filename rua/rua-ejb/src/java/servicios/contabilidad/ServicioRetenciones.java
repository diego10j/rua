/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servicios.contabilidad;

import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import servicios.ServicioBase;

/**
 *
 * @author djacome
 */
@Stateless
public class ServicioRetenciones extends ServicioBase {

    public String getSqlComboAutorizaciones() {
        return "Select autorizacion_cncre,autorizacion_cncre from con_cabece_retenc   WHERE ide_empr=" + utilitario.getVariable("ide_empr") + "  group by autorizacion_cncre ORDER BY autorizacion_cncre";
    }

    /**
     * Retenciones realizadas
     *
     * @param autorizacion_cncre
     * @param fechaInicio
     * @param fechaFin
     * @return
     */
    public String getSqlRetenciones(String autorizacion_cncre, String fechaInicio, String fechaFin) {
        if (autorizacion_cncre == null) {
            autorizacion_cncre = "";
        }
        autorizacion_cncre = autorizacion_cncre.replace("null", "").trim();
        if (autorizacion_cncre.isEmpty() == false) {
            autorizacion_cncre = " and autorizacion_cncre='" + autorizacion_cncre + "' ";
        }
        return "SELECT a.ide_cncre,ide_cnere,a.ide_cnccc,fecha_emisi_cncre,observacion_cncre,numero_cncre,autorizacion_cncre,ide_cpcfa,numero_cpcfa,nom_geper\n"
                + "FROM con_cabece_retenc a\n"
                + "left join cxp_cabece_factur b on a.ide_cncre=b.ide_cncre\n"
                + "left join gen_persona c on b.ide_geper=c.ide_geper\n"
                + "WHERE a.ide_empr=" + utilitario.getVariable("ide_empr") + "\n"
                + "and fecha_emisi_cncre BETWEEN '" + fechaInicio + "' and '" + fechaFin + "' \n"
                + "and es_venta_cncre = false\n"
                + autorizacion_cncre
                + "ORDER BY ide_cncre desc";
    }

    public String getSqlRetencionesProveedor(String ide_geper) {
        String fechaFin = utilitario.getFechaActual();
        String fechaInicio = utilitario.getFormatoFecha(utilitario.sumarDiasFecha(new Date(), -45));

        return "SELECT a.ide_cncre,fecha_emisi_cncre AS FECHA,numero_cncre AS NUMERO,autorizacion_cncre AS NUM_AUTORIZACION,observacion_cncre AS OBSERVACION,numero_cpcfa as NUM_FACTURA,ide_cpcfa\n"
                + "FROM con_cabece_retenc a\n"
                + "left join cxp_cabece_factur b on a.ide_cncre=b.ide_cncre\n"
                + "WHERE a.ide_empr=" + utilitario.getVariable("ide_empr") + "\n"
                + "and fecha_emisi_cncre BETWEEN '" + fechaInicio + "' and '" + fechaFin + "' \n"
                + "and b.ide_geper=" + ide_geper + " "
                + "and es_venta_cncre = false\n"
                + "and a.ide_sucu=" + utilitario.getVariable("IDE_SUCU") + " "
                + "and ide_cnere!=" + utilitario.getVariable("p_con_estado_comprobante_rete_anulado") + " "
                + "ORDER BY numero_cncre desc";
    }

    public String getSqlConsultaImpuestos(String autorizacion_cncre, String fechaInicio, String fechaFin, String ide_cncim) {
        if (autorizacion_cncre == null) {
            autorizacion_cncre = "";
        }
        autorizacion_cncre = autorizacion_cncre.replace("null", "").trim();
        if (autorizacion_cncre.isEmpty() == false) {
            autorizacion_cncre = " and autorizacion_cncre='" + autorizacion_cncre + "' ";
        }

        if (ide_cncim == null) {
            ide_cncim = "";
        }
        ide_cncim = ide_cncim.replace("null", "").trim();
        if (ide_cncim.isEmpty() == false) {
            ide_cncim = " and a.ide_cncim=" + ide_cncim + " ";
        }

        return "select a.ide_cndre, fecha_emisi_cncre,nombre_cncim,numero_cncre,nom_geper,numero_cpcfa,base_cndre,porcentaje_cndre,valor_cndre from con_detall_retenc a\n"
                + "inner join con_cabece_retenc b  on a.ide_cncre=b.ide_cncre\n"
                + "left join cxp_cabece_factur c on b.ide_cncre=c.ide_cncre\n"
                + "left join gen_persona d on c.ide_geper=d.ide_geper\n"
                + "left join con_cabece_impues e on a.ide_cncim=e.ide_cncim "
                + "where a.ide_empr=" + utilitario.getVariable("ide_empr") + "\n"
                + "and ide_cnere!=" + utilitario.getVariable("p_con_estado_comprobante_rete_anulado") + " "
                + "and fecha_emisi_cncre BETWEEN '" + fechaInicio + "' and '" + fechaFin + "' \n"
                + "and es_venta_cncre = false\n"
                + autorizacion_cncre
                + ide_cncim
                + "order by fecha_emisi_cncre";

    }

    public String getSqlRetencionesAnuladas(String autorizacion_cncre, String fechaInicio, String fechaFin) {
        if (autorizacion_cncre == null) {
            autorizacion_cncre = "";
        }
        autorizacion_cncre = autorizacion_cncre.replace("null", "").trim();
        if (autorizacion_cncre.isEmpty() == false) {
            autorizacion_cncre = " and autorizacion_cncre='" + autorizacion_cncre + "' ";
        }
        return "SELECT a.ide_cncre,ide_cnere,a.ide_cnccc,fecha_emisi_cncre,observacion_cncre,numero_cncre,autorizacion_cncre,ide_cpcfa,numero_cpcfa,nom_geper\n"
                + "FROM con_cabece_retenc a\n"
                + "left join cxp_cabece_factur b on a.ide_cncre=b.ide_cncre\n"
                + "left join gen_persona c on b.ide_geper=c.ide_geper\n"
                + "WHERE a.ide_empr=" + utilitario.getVariable("ide_empr") + "\n"
                + "and fecha_emisi_cncre BETWEEN '" + fechaInicio + "' and '" + fechaFin + "' \n"
                + "and es_venta_cncre = false\n"
                + "and ide_cnere=" + utilitario.getVariable("p_con_estado_comprobante_rete_anulado") + " "
                + autorizacion_cncre
                + "ORDER BY ide_cncre desc";
    }

    /**
     * Cambia a estado anulado un comprobante de retencion
     *
     * @param ide_cncre
     */
    public void anularComprobanteRetencion(String ide_cncre) {
        utilitario.getConexion().agregarSqlPantalla("UPDATE con_cabece_retenc set ide_cnere=" + utilitario.getVariable("p_con_estado_comprobante_rete_anulado") + " where ide_cncre=" + ide_cncre);
        utilitario.getConexion().agregarSqlPantalla("UPDATE cxp_cabece_factur set ide_cncre=null where ide_cncre=" + ide_cncre);
    }

    public String getNumeroRetencion(String autorizacion) {
        String str_sql = "select  max(CAST(coalesce(numero_cncre,'0') AS BIGINT))  as num_retencion from con_cabece_retenc where ide_sucu=" + utilitario.getVariable("ide_sucu") + " "
                + "and autorizacion_cncre ='" + autorizacion + "' "
                + "and es_venta_cncre is FALSE ";
        System.out.println(str_sql);
        //System.out.println("sql num retencion " + str_sql);
        List lis_cabecera_retencion = utilitario.getConexion().consultar(str_sql);
        if (lis_cabecera_retencion.size() > 0) {
            if (lis_cabecera_retencion.get(0) != null && !lis_cabecera_retencion.get(0).toString().isEmpty()) {
                String num_max = lis_cabecera_retencion.get(0) + "";
                num_max = utilitario.generarCero(14 - num_max.length()) + num_max;
                String num_max_retencion = num_max.substring(0, 6);
                String aux_num = num_max.substring(6, num_max.length());
                try {
                    int num = Integer.parseInt(aux_num);
                    num = num + 1;
                    aux_num = num + "";
                    String ceros = utilitario.generarCero(8 - aux_num.length());
                    num_max_retencion = num_max_retencion.concat(ceros).concat(aux_num);
                    return num_max_retencion;
                } catch (Exception e) {
                    return null;
                }
            } else {
                return null;
            }

        } else {
            return null;
        }
    }
}
