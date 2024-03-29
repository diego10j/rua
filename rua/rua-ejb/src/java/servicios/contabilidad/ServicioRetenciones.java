/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servicios.contabilidad;

import dj.comprobantes.offline.enums.EstadoComprobanteEnum;
import framework.aplicacion.TablaGenerica;
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
        return "Select autorizacion_cncre,autorizacion_cncre from con_cabece_retenc   WHERE ide_empr=" + utilitario.getVariable("ide_empr") + " and es_venta_cncre = false group by autorizacion_cncre ORDER BY autorizacion_cncre";
    }

    /**
     * Retenciones realizadas
     *
     * @param ide_ccdaf
     * @param fechaInicio
     * @param fechaFin
     * @return
     */
    public String getSqlRetenciones(String ide_ccdaf, String fechaInicio, String fechaFin) {
        if (ide_ccdaf == null) {
            ide_ccdaf = "";
        }
        ide_ccdaf = ide_ccdaf.replace("null", "").trim();
        if (ide_ccdaf.isEmpty() == false) {
            ide_ccdaf = " and ide_ccdaf='" + ide_ccdaf + "' ";
        }
        return "SELECT a.ide_cncre,ide_cnere,fecha_emisi_cncre,a.ide_cnccc,observacion_cncre,numero_cncre AS NUMERO,autorizacion_cncre AS AUTORIZACION,"
                + "(select sum(base_cndre) from con_detall_retenc where ide_cncre=a.ide_cncre)AS BASE_IMPONIBLE,"
                + "(select sum(valor_cndre) from con_detall_retenc where ide_cncre=a.ide_cncre)AS VALOR,ide_cpcfa,numero_cpcfa as NUM_FACTURA,nom_geper AS PROVEEDOR\n"
                + "FROM con_cabece_retenc a\n"
                + "left join cxp_cabece_factur b on a.ide_cncre=b.ide_cncre\n"
                + "left join gen_persona c on b.ide_geper=c.ide_geper\n"
                + "WHERE a.ide_empr=" + utilitario.getVariable("ide_empr") + "\n"
                + "and fecha_emisi_cncre BETWEEN '" + fechaInicio + "' and '" + fechaFin + "' \n"
                + "and es_venta_cncre = false\n"
                + ide_ccdaf
                + "ORDER BY ide_cncre desc";
    }

    public String getSqlRetencionesElectronicas(String ide_ccdaf, String fechaInicio, String fechaFin) {
        if (ide_ccdaf == null) {
            ide_ccdaf = "-1";
        }
        ide_ccdaf = ide_ccdaf.replace("null", "-1").trim();
        if (ide_ccdaf.isEmpty() == false) {
            ide_ccdaf = " and a.ide_ccdaf='" + ide_ccdaf + "' ";
        }
        return "SELECT a.ide_cncre,ide_cnere,fecha_emisi_cncre,a.ide_cnccc,nombre_sresc as ESTADO,observacion_cncre as OBSERVACION,numero_cncre AS NUMERO,autorizacion_cncre AS AUTORIZACION,"
                + "(select sum(base_cndre) from con_detall_retenc where ide_cncre=a.ide_cncre)AS BASE_IMPONIBLE,"
                + "(select sum(valor_cndre) from con_detall_retenc where ide_cncre=a.ide_cncre)AS VALOR,ide_cpcfa,numero_cpcfa as NUM_FACTURA,nom_geper AS PROVEEDOR, correo_cncre as CORREO,a.ide_srcom\n"
                + "FROM con_cabece_retenc a\n"
                + "left join cxp_cabece_factur b on a.ide_cncre=b.ide_cncre\n"
                + "left join gen_persona c on b.ide_geper=c.ide_geper\n"
                + "left join sri_comprobante d on a.ide_srcom=d.ide_srcom "
                + "left join sri_estado_comprobante f on d.ide_sresc=f.ide_sresc "
                + "WHERE a.ide_empr=" + utilitario.getVariable("ide_empr") + "\n"
                + "and fecha_emisi_cncre BETWEEN '" + fechaInicio + "' and '" + fechaFin + "' \n"
                + "and es_venta_cncre = false\n"
                + ide_ccdaf
                + "ORDER BY numero_cncre desc,ide_cncre desc";
    }

    public String getSqlRetencionesElectronicasPorEstado(String ide_ccdaf, String fechaInicio, String fechaFin, EstadoComprobanteEnum estado) {
        if (ide_ccdaf == null) {
            ide_ccdaf = "-1";
        }
        ide_ccdaf = ide_ccdaf.replace("null", "-1").trim();
        if (ide_ccdaf.isEmpty() == false) {
            ide_ccdaf = " and a.ide_ccdaf='" + ide_ccdaf + "' "; // 8/1/2020 agregue alias a
        }
        return "SELECT a.ide_cncre,ide_cnere,fecha_emisi_cncre,a.ide_cnccc,nombre_sresc as ESTADO,observacion_cncre as OBSERVACION,numero_cncre AS NUMERO,autorizacion_cncre AS AUTORIZACION,"
                + "(select sum(base_cndre) from con_detall_retenc where ide_cncre=a.ide_cncre)AS BASE_IMPONIBLE,"
                + "(select sum(valor_cndre) from con_detall_retenc where ide_cncre=a.ide_cncre)AS VALOR,ide_cpcfa,numero_cpcfa as NUM_FACTURA,nom_geper AS PROVEEDOR,a.ide_srcom\n"
                + "FROM con_cabece_retenc a\n"
                + "left join cxp_cabece_factur b on a.ide_cncre=b.ide_cncre\n"
                + "left join gen_persona c on b.ide_geper=c.ide_geper\n"
                + "left join sri_comprobante d on a.ide_srcom=d.ide_srcom "
                + "left join sri_estado_comprobante f on d.ide_sresc=f.ide_sresc "
                + "WHERE a.ide_empr=" + utilitario.getVariable("ide_empr") + "\n"
                + "and fecha_emisi_cncre BETWEEN '" + fechaInicio + "' and '" + fechaFin + "' \n"
                + "and es_venta_cncre = false\n"
                + " and d.ide_sresc =" + estado.getCodigo() + " "
                + ide_ccdaf
                + "ORDER BY numero_cncre desc,ide_cncre desc";
    }

    public String getSqlRetencionesProveedor(String ide_geper) {
        String fechaFin = utilitario.getFechaActual();
        String fechaInicio = utilitario.getFormatoFecha(utilitario.sumarDiasFecha(new Date(), -90));

        return "SELECT a.ide_cncre,fecha_emisi_cncre AS FECHA_EMISION,numero_cncre AS NUMERO,autorizacion_cncre AS NUM_AUTORIZACION,observacion_cncre AS OBSERVACION,numero_cpcfa as NUM_FACTURA,ide_cpcfa\n"
                + "FROM con_cabece_retenc a\n"
                + "left join cxp_cabece_factur b on a.ide_cncre=b.ide_cncre\n"
                + "WHERE a.ide_empr=" + utilitario.getVariable("ide_empr") + "\n"
                + "and fecha_emisi_cncre BETWEEN '" + fechaInicio + "' and '" + fechaFin + "' \n"
                + "and b.ide_geper=" + ide_geper + " "
                + "and es_venta_cncre = false\n"
                + "and a.ide_sucu=" + utilitario.getVariable("IDE_SUCU") + " "
                + "and ide_cnere!=" + utilitario.getVariable("p_con_estado_comprobante_rete_anulado") + " "
                + "ORDER BY fecha_emisi_cncre desc";
    }

    public String getSqlRetencionesCliente(String ide_geper) {
        String fechaFin = utilitario.getFechaActual();
        String fechaInicio = utilitario.getFormatoFecha(utilitario.sumarDiasFecha(new Date(), -90));

        return "SELECT a.ide_cncre,fecha_emisi_cncre AS FECHA_EMISION,numero_cncre AS NUMERO,autorizacion_cncre AS NUM_AUTORIZACION,observacion_cncre AS OBSERVACION,secuencial_cccfa as NUM_FACTURA,ide_cccfa\n"
                + "FROM con_cabece_retenc a\n"
                + "left join cxc_cabece_factura b on a.ide_cncre=b.ide_cncre\n"
                + "WHERE a.ide_empr=" + utilitario.getVariable("ide_empr") + "\n"
                + "and fecha_emisi_cncre BETWEEN '" + fechaInicio + "' and '" + fechaFin + "' \n"
                + "and b.ide_geper=" + ide_geper + " "
                + "and es_venta_cncre = true\n"
                + "and a.ide_sucu=" + utilitario.getVariable("IDE_SUCU") + " "
                + "and ide_cnere!=" + utilitario.getVariable("p_con_estado_comprobante_rete_anulado") + " "
                + "ORDER BY fecha_emisi_cncre desc";
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

        return "select a.ide_cndre, fecha_emisi_cncre,nombre_cncim,numero_cncre AS NUMERO,nom_geper AS PROVEEDOR,numero_cpcfa AS NUM_FACTURA,base_cndre,porcentaje_cndre AS PORCENTAJE,valor_cndre from con_detall_retenc a\n"
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

    public String getSqlRetencionesAnuladas(String ide_ccdaf, String fechaInicio, String fechaFin) {
        if (ide_ccdaf == null) {
            ide_ccdaf = "-1";
        }
        ide_ccdaf = ide_ccdaf.replace("null", "-1").trim();
        if (ide_ccdaf.isEmpty() == false) {
            ide_ccdaf = " and a.ide_ccdaf='" + ide_ccdaf + "' ";// 8/1/2020 agrege alias a
        }
        return "SELECT a.ide_cncre,fecha_emisi_cncre,observacion_cncre AS OBSERVACION,numero_cncre AS NUMERO,autorizacion_cncre AS AUTORIZACION,numero_cpcfa AS NUM_FACTURA,nom_geper AS PROVEEDOR\n"
                + "FROM con_cabece_retenc a\n"
                + "left join cxp_cabece_factur b on a.ide_cncre=b.ide_cncre\n"
                + "left join gen_persona c on b.ide_geper=c.ide_geper\n"
                + "WHERE a.ide_empr=" + utilitario.getVariable("ide_empr") + "\n"
                + "and fecha_emisi_cncre BETWEEN '" + fechaInicio + "' and '" + fechaFin + "' \n"
                + "and es_venta_cncre = false\n"
                + "and ide_cnere=" + utilitario.getVariable("p_con_estado_comprobante_rete_anulado") + " "
                + ide_ccdaf
                + "ORDER BY ide_cncre desc";
    }

    /**
     * Cambia a estado anulado un comprobante de retencion
     *
     * @param ide_cncre
     */
    public void anularComprobanteRetencion(String ide_cncre) {
        utilitario.getConexion().agregarSqlPantalla("delete from cxp_detall_transa where  ide_cpdtr in (\n"
                + "SELECT ide_cpdtr FROM cxp_detall_transa a \n"
                + "inner join cxp_cabece_factur b on a.ide_cpcfa=b.ide_cpcfa  \n"
                + "where ide_cpttr=21 \n"
                + "and ide_cncre=" + ide_cncre
                + " )");
        utilitario.getConexion().agregarSqlPantalla("UPDATE con_cabece_retenc set ide_cnere=" + utilitario.getVariable("p_con_estado_comprobante_rete_anulado") + " where ide_cncre=" + ide_cncre);
        utilitario.getConexion().agregarSqlPantalla("UPDATE cxp_cabece_factur set ide_cncre=null where ide_cncre=" + ide_cncre);

        if (isElectronica()) {
            TablaGenerica tab_cab = utilitario.consultar("SELECT a.ide_srcom,a.ide_cncre,ide_sresc from con_cabece_retenc a inner join sri_comprobante b on a.ide_srcom=b.ide_srcom  where a.ide_cncre=" + ide_cncre);
            //cambia de estado el compobante pendiente
            if (tab_cab.getValor("ide_srcom") != null) {
                if (tab_cab.getValor("ide_sresc") != null && tab_cab.getValor("ide_sresc").equals(EstadoComprobanteEnum.AUTORIZADO.getCodigo() + "")) {
                    utilitario.getConexion().agregarSql("update sri_comprobante set ide_sresc=" + EstadoComprobanteEnum.ANULADO.getCodigo() + " where ide_srcom=" + tab_cab.getValor("ide_srcom"));
                } else {
                    utilitario.getConexion().agregarSql("update sri_comprobante set ide_sresc=" + EstadoComprobanteEnum.ANULADO.getCodigo() + ",reutiliza_srcom=true  where ide_srcom=" + tab_cab.getValor("ide_srcom") + " and ide_sresc=" + EstadoComprobanteEnum.PENDIENTE.getCodigo());
                }
            }
        }

    }

    /**
     * Cambia a estado anulado un comprobante de retencion
     *
     * @param ide_cncre
     */
    public void anularComprobanteRetencionVenta(String ide_cncre) {
        utilitario.getConexion().agregarSqlPantalla("delete from cxc_detall_transa where  ide_ccdtr in (\n"
                + "SELECT ide_ccdtr FROM cxc_detall_transa a \n"
                + "inner join cxc_cabece_factura b on a.ide_cccfa=b.ide_cccfa  \n"
                + "where ide_ccttr=" + utilitario.getVariable("p_cxc_tipo_trans_retencion") + " "
                + "and ide_cncre=" + ide_cncre
                + " )");
        utilitario.getConexion().agregarSqlPantalla("UPDATE con_cabece_retenc set ide_cnere=" + utilitario.getVariable("p_con_estado_comprobante_rete_anulado") + " where ide_cncre=" + ide_cncre);
        utilitario.getConexion().agregarSqlPantalla("UPDATE cxc_cabece_factura set ide_cncre=null where ide_cncre=" + ide_cncre);

    }

    public String getNumeroRetencion(String autorizacion) {
        String str_sql = "select  max(CAST(coalesce(numero_cncre,'0') AS BIGINT))  as num_retencion from con_cabece_retenc where ide_sucu=" + utilitario.getVariable("ide_sucu") + " "
                + "and autorizacion_cncre ='" + autorizacion + "' "
                + "and es_venta_cncre is FALSE ";

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

    public boolean isImpuestoRenta(String ide_cncim) {
        TablaGenerica tb = utilitario.consultar("select ide_cncim,ide_cnimp,* from con_cabece_impues where ide_cnimp =1 and ide_cncim=" + ide_cncim);
        return !tb.isEmpty();
    }

    public String getValorDefectoImpuesto(String ide_cncim) {
        TablaGenerica tb = utilitario.consultar("select ide_cncim,valor_defecto_cncim from con_cabece_impues where ide_cncim=" + ide_cncim);
        return tb.getValor("valor_defecto_cncim");
    }

    public String getSqlRetencionesVentas(String fechaInicio, String fechaFin) {

        return "SELECT a.ide_cncre,ide_cnere,fecha_emisi_cncre,nom_geper AS CLIENTE,a.ide_cnccc,numero_cncre AS NUMERO,autorizacion_cncre AS AUTORIZACION,"
                + "(select sum(base_cndre) from con_detall_retenc d INNER JOIN con_cabece_impues f on d.ide_cncim=f.ide_cncim  where d.ide_cncre=a.ide_cncre and f.ide_cnimp=1 )AS BASE_RENTA,"
                + "(select sum(valor_cndre) from con_detall_retenc d INNER JOIN con_cabece_impues f on d.ide_cncim=f.ide_cncim  where d.ide_cncre=a.ide_cncre and f.ide_cnimp=1 )AS RET_RENTA,"
                + "(select sum(base_cndre) from con_detall_retenc d INNER JOIN con_cabece_impues f on d.ide_cncim=f.ide_cncim  where d.ide_cncre=a.ide_cncre and f.ide_cnimp=0 )AS BASE_IVA,"
                + "(select sum(valor_cndre) from con_detall_retenc d INNER JOIN con_cabece_impues f on d.ide_cncim=f.ide_cncim  where d.ide_cncre=a.ide_cncre and f.ide_cnimp=0 )AS RET_IVA,"
                + "ide_cccfa,secuencial_cccfa as NUM_FACTURA,observacion_cncre\n"
                + "FROM con_cabece_retenc a\n"
                + "left join cxc_cabece_factura b on a.ide_cncre=b.ide_cncre\n"
                + "left join gen_persona c on b.ide_geper=c.ide_geper\n"
                + "WHERE a.ide_sucu=" + utilitario.getVariable("ide_sucu") + "\n"
                + "and fecha_emisi_cncre BETWEEN '" + fechaInicio + "' and '" + fechaFin + "' \n"
                + "and es_venta_cncre = true AND ide_cnere=0\n"
                + "ORDER BY ide_cncre desc";
    }

    /**
     * Retorna la sentencia SQL con los puntos de emision de retenciones
     *
     * @return
     */
    public String getSqlPuntosEmision() {
        return "select ide_ccdaf,serie_ccdaf, coalesce(autorizacion_ccdaf,''),observacion_ccdaf from cxc_datos_fac where ide_cntdoc=8  and activo_ccdaf=true and  ide_sucu=" + utilitario.getVariable("IDE_SUCU");
    }

    public boolean isElectronica() {
        return true;
    }

    public String getSqlComboImpuestos() {
        return "SELECT ide_cncim, nombre_cncim,casillero_cncim FROM con_cabece_impues order by ide_cnimp,casillero_cncim";
    }

    //Retorna el último correo de las retenciones realizadas
    public String getCorreoRetencion(String ide_geper) {
        return utilitario.consultar("select ide_cpcfa,correo_cncre from con_cabece_retenc a\n"
                + "inner join cxp_cabece_factur b on a.ide_cncre=b.ide_cncre\n"
                + "where ide_geper=" + ide_geper + "\n"
                + "order by ide_cpcfa desc limit 1").getValor("correo_cncre");
    }

    /**
     * Retorna consolidado de retenciones realizadas en un rango de fechas
     *
     * @param fechaInicio
     * @param fechaFin
     * @return
     */
    public String getSqlConsolidadoRenta(String fechaInicio, String fechaFin) {

        return "SELECT  casillero_cncim AS CASILLERO,nombre_cncim as IMPUESTO ,SUM(dr.base_cndre) as BASE_IMPONIBLE,SUM(dr.valor_cndre) as valor_retenido\n"
                + "FROM con_cabece_retenc cr \n"
                + "LEFT JOIN con_detall_retenc dr ON (dr.ide_cncre = cr.ide_cncre) \n"
                + "LEFT JOIN con_cabece_impues ci ON (ci.ide_cncim = dr.ide_cncim) \n"
                + "\n"
                + "WHERE cr.fecha_emisi_cncre BETWEEN '" + fechaInicio + "' and '" + fechaFin + "'\n"
                + "AND ide_cnere= 0\n"
                + "AND es_venta_cncre=false\n"
                + "AND ide_cnimp=1 --renta\n"
                + "GROUP BY nombre_cncim,casillero_cncim, ci.ide_cnimp\n"
                + "ORDER BY 1";
    }

    /**
     * Retorna consolidado de retenciones realizadas en un rango de fechas
     *
     * @param fechaInicio
     * @param fechaFin
     * @return
     */
    public String getSqlConsolidadoIva(String fechaInicio, String fechaFin) {

        return "SELECT casillero_cncim AS CASILLERO, nombre_cncim as IMPUESTO ,SUM(dr.valor_cndre) as valor_retenido\n"
                + "FROM con_cabece_retenc cr \n"
                + "LEFT JOIN con_detall_retenc dr ON (dr.ide_cncre = cr.ide_cncre) \n"
                + "LEFT JOIN con_cabece_impues ci ON (ci.ide_cncim = dr.ide_cncim) \n"
                + "WHERE cr.fecha_emisi_cncre BETWEEN '" + fechaInicio + "' and '" + fechaFin + "'\n"
                + "AND ide_cnere= 0 and ide_cnimp=0\n"
                + "AND es_venta_cncre=false\n"
                + "GROUP BY nombre_cncim,casillero_cncim, ci.ide_cnimp\n"
                + "HAVING SUM(dr.valor_cndre) >0\n"
                + "ORDER BY 1";
    }

    /**
     * Verifica si existe una clave de acceso de un comprobante electronico
     *
     * @param autorizacion_cncre
     * @return
     */
    public boolean isExisteRetencionElectronicaVenta(String autorizacion_cncre) {
        TablaGenerica tag = utilitario.consultar("select ide_cncre,autorizacion_cncre from con_cabece_retenc where autorizacion_cncre='" + autorizacion_cncre + "'");
        return !tag.isEmpty();
    }

    /**
     *
     * @param casillero_cncim
     * @return
     */
    public String getIdeCabeceraImpuesto(String casillero_cncim) {
        return utilitario.consultar("select ide_cncim,casillero_cncim from con_cabece_impues where casillero_cncim='" + casillero_cncim + "' or codigo_fe_retencion_cncim='" + casillero_cncim + "'").getValor("ide_cncim");
    }

    /**
     * Retorna consolidado de retenciones realizadas en un rango de fechas
     *
     * @param fechaInicio
     * @param fechaFin
     * @return
     */
    public String getSqlConsolidadoRentaInspectoria(String fechaInicio, String fechaFin) {

        return "SELECT  casillero_cncim AS CASILLERO,nombre_cncim as IMPUESTO ,SUM(dr.base_cndre) as BASE_IMPONIBLE,SUM(dr.valor_cndre) as valor_retenido,\n"
                + "(\n"
                + "SELECT  SUM(base_cndre)\n"
                + "FROM con_cabece_retenc a \n"
                + "LEFT JOIN con_detall_retenc d ON d.ide_cncre = a.ide_cncre\n"
                + "JOIN con_cabece_impues c ON c.ide_cncim = d.ide_cncim\n"
                + "WHERE fecha_emisi_cncre BETWEEN '" + fechaInicio + "' and '" + fechaFin + "'\n"
                + "AND casillero_cncim = ci.casillero_cncim\n"
                + "AND ide_cnere=0 \n"
                + "AND es_venta_cncre=false\n"
                + "and a.ide_sucu=0\n"
                + ") as ECONOMATO_BASE_IMPONIBLE,\n"
                + "(\n"
                + "SELECT  SUM(valor_cndre)\n"
                + "FROM con_cabece_retenc a \n"
                + "LEFT JOIN con_detall_retenc d ON d.ide_cncre = a.ide_cncre\n"
                + "JOIN con_cabece_impues c ON c.ide_cncim = d.ide_cncim\n"
                + "WHERE fecha_emisi_cncre BETWEEN '" + fechaInicio + "' and '" + fechaFin + "'\n"
                + "AND casillero_cncim = ci.casillero_cncim\n"
                + "AND ide_cnere=0 \n"
                + "AND es_venta_cncre=false\n"
                + "and a.ide_sucu=0\n"
                + ") as ECONOMATO_VALOR_RETENIDO,\n"
                + "\n"
                + "(\n"
                + "SELECT  SUM(base_cndre)\n"
                + "FROM con_cabece_retenc a \n"
                + "LEFT JOIN con_detall_retenc d ON d.ide_cncre = a.ide_cncre\n"
                + "JOIN con_cabece_impues c ON c.ide_cncim = d.ide_cncim\n"
                + "WHERE fecha_emisi_cncre BETWEEN '" + fechaInicio + "' and '" + fechaFin + "'\n"
                + "AND casillero_cncim = ci.casillero_cncim\n"
                + "AND ide_cnere=0 \n"
                + "AND es_venta_cncre=false\n"
                + "and a.ide_sucu=1\n"
                + ") as CASA_INSPECTORIAL_base_imponible,\n"
                + "(\n"
                + "SELECT  SUM(valor_cndre)\n"
                + "FROM con_cabece_retenc a \n"
                + "LEFT JOIN con_detall_retenc d ON d.ide_cncre = a.ide_cncre\n"
                + "JOIN con_cabece_impues c ON c.ide_cncim = d.ide_cncim\n"
                + "WHERE fecha_emisi_cncre BETWEEN '" + fechaInicio + "' and '" + fechaFin + "'\n"
                + "AND casillero_cncim = ci.casillero_cncim\n"
                + "AND ide_cnere=0 \n"
                + "AND es_venta_cncre=false\n"
                + "and a.ide_sucu=1\n"
                + ") as CASA_INSPECTORIAL_valor_retenido,\n"
                + "\n"
                + "(\n"
                + "SELECT  SUM(base_cndre)\n"
                + "FROM con_cabece_retenc a \n"
                + "LEFT JOIN con_detall_retenc d ON d.ide_cncre = a.ide_cncre\n"
                + "JOIN con_cabece_impues c ON c.ide_cncim = d.ide_cncim\n"
                + "WHERE fecha_emisi_cncre BETWEEN '" + fechaInicio + "' and '" + fechaFin + "'\n"
                + "AND casillero_cncim = ci.casillero_cncim\n"
                + "AND ide_cnere=0 \n"
                + "AND es_venta_cncre=false\n"
                + "and a.ide_sucu=3\n"
                + ") as OPLADI_base_imponible,\n"
                + "(\n"
                + "SELECT  SUM(valor_cndre)\n"
                + "FROM con_cabece_retenc a \n"
                + "LEFT JOIN con_detall_retenc d ON d.ide_cncre = a.ide_cncre\n"
                + "JOIN con_cabece_impues c ON c.ide_cncim = d.ide_cncim\n"
                + "WHERE fecha_emisi_cncre BETWEEN '" + fechaInicio + "' and '" + fechaFin + "'\n"
                + "AND casillero_cncim = ci.casillero_cncim\n"
                + "AND ide_cnere=0 \n"
                + "AND es_venta_cncre=false\n"
                + "and a.ide_sucu=3\n"
                + ") as OPLADI_valor_retenido,\n"
                //  mision don bosco
                  + "(\n"
                + "SELECT  SUM(base_cndre)\n"
                + "FROM con_cabece_retenc a \n"
                + "LEFT JOIN con_detall_retenc d ON d.ide_cncre = a.ide_cncre\n"
                + "JOIN con_cabece_impues c ON c.ide_cncim = d.ide_cncim\n"
                + "WHERE fecha_emisi_cncre BETWEEN '" + fechaInicio + "' and '" + fechaFin + "'\n"
                + "AND casillero_cncim = ci.casillero_cncim\n"
                + "AND ide_cnere=0 \n"
                + "AND es_venta_cncre=false\n"
                + "and a.ide_sucu=5\n"
                + ") as MisionDB_base_imponible,\n"
                + "(\n"
                + "SELECT  SUM(valor_cndre)\n"
                + "FROM con_cabece_retenc a \n"
                + "LEFT JOIN con_detall_retenc d ON d.ide_cncre = a.ide_cncre\n"
                + "JOIN con_cabece_impues c ON c.ide_cncim = d.ide_cncim\n"
                + "WHERE fecha_emisi_cncre BETWEEN '" + fechaInicio + "' and '" + fechaFin + "'\n"
                + "AND casillero_cncim = ci.casillero_cncim\n"
                + "AND ide_cnere=0 \n"
                + "AND es_venta_cncre=false\n"
                + "and a.ide_sucu=5\n"
                + ") as MisionDB_valor_retenido\n"
                
                + "FROM con_cabece_retenc cr \n"
                + "LEFT JOIN con_detall_retenc dr ON (dr.ide_cncre = cr.ide_cncre) \n"
                + "LEFT JOIN con_cabece_impues ci ON (ci.ide_cncim = dr.ide_cncim) \n"
                + "\n"
                + "WHERE cr.fecha_emisi_cncre BETWEEN '" + fechaInicio + "' and '" + fechaFin + "'\n"
                + "AND ide_cnere= 0\n"
                + "AND es_venta_cncre=false\n"
                + "AND ide_cnimp=1 --renta\n"
                + "GROUP BY nombre_cncim,casillero_cncim, ci.ide_cnimp\n"
                + "ORDER BY 1";
    }

    /**
     * Retorna consolidado de retenciones realizadas en un rango de fechas
     *
     * @param fechaInicio
     * @param fechaFin
     * @return
     */
    public String getSqlConsolidadoIvaInspectoria(String fechaInicio, String fechaFin) {

        return "SELECT casillero_cncim AS CASILLERO, nombre_cncim as IMPUESTO ,SUM(dr.valor_cndre) as valor_retenido,\n"
                + "(\n"
                + "SELECT  SUM(valor_cndre)\n"
                + "FROM con_cabece_retenc a \n"
                + "LEFT JOIN con_detall_retenc d ON d.ide_cncre = a.ide_cncre\n"
                + "JOIN con_cabece_impues c ON c.ide_cncim = d.ide_cncim\n"
                + "WHERE fecha_emisi_cncre BETWEEN '" + fechaInicio + "' and '" + fechaFin + "'\n"
                + "AND casillero_cncim = ci.casillero_cncim\n"
                + "AND ide_cnere=0 \n"
                + "AND es_venta_cncre=false\n"
                + "and a.ide_sucu=0\n"
                + ") as ECONOMATO,\n"
                + "(\n"
                + "SELECT  SUM(valor_cndre)\n"
                + "FROM con_cabece_retenc a \n"
                + "LEFT JOIN con_detall_retenc d ON d.ide_cncre = a.ide_cncre\n"
                + "JOIN con_cabece_impues c ON c.ide_cncim = d.ide_cncim\n"
                + "WHERE fecha_emisi_cncre BETWEEN '" + fechaInicio + "' and '" + fechaFin + "'\n"
                + "AND casillero_cncim = ci.casillero_cncim\n"
                + "AND ide_cnere=0 \n"
                + "AND es_venta_cncre=false\n"
                + "and a.ide_sucu=1\n"
                + ") as CASA_INSPECTORIAL,\n"
                + "(\n"
                + "SELECT  SUM(valor_cndre)\n"
                + "FROM con_cabece_retenc a \n"
                + "LEFT JOIN con_detall_retenc d ON d.ide_cncre = a.ide_cncre\n"
                + "JOIN con_cabece_impues c ON c.ide_cncim = d.ide_cncim\n"
                + "WHERE fecha_emisi_cncre BETWEEN '" + fechaInicio + "' and '" + fechaFin + "'\n"
                + "AND casillero_cncim = ci.casillero_cncim\n"
                + "AND ide_cnere=0 \n"
                + "AND es_venta_cncre=false\n"
                + "and a.ide_sucu=3\n"
                + ") as OPLADI,\n"
                //MISION DB
                  + "(\n"
                + "SELECT  SUM(valor_cndre)\n"
                + "FROM con_cabece_retenc a \n"
                + "LEFT JOIN con_detall_retenc d ON d.ide_cncre = a.ide_cncre\n"
                + "JOIN con_cabece_impues c ON c.ide_cncim = d.ide_cncim\n"
                + "WHERE fecha_emisi_cncre BETWEEN '" + fechaInicio + "' and '" + fechaFin + "'\n"
                + "AND casillero_cncim = ci.casillero_cncim\n"
                + "AND ide_cnere=0 \n"
                + "AND es_venta_cncre=false\n"
                + "and a.ide_sucu=5\n"
                + ") as MISIONDB\n"
                + "FROM con_cabece_retenc cr \n"
                + "LEFT JOIN con_detall_retenc dr ON (dr.ide_cncre = cr.ide_cncre) \n"
                + "LEFT JOIN con_cabece_impues ci ON (ci.ide_cncim = dr.ide_cncim) \n"
                + "WHERE cr.fecha_emisi_cncre BETWEEN '" + fechaInicio + "' and '" + fechaFin + "'\n"
                + "AND ide_cnere= 0 and ide_cnimp=0\n"
                + "AND es_venta_cncre=false\n"
                + "GROUP BY nombre_cncim,casillero_cncim, ci.ide_cnimp\n"
                + "HAVING SUM(dr.valor_cndre) >0\n"
                + "ORDER BY 1";
    }

    public String getSqlRetencionesComprasNoContabilizadas(String ide_ccdaf, String fechaInicio, String fechaFin) {
        if (ide_ccdaf == null) {
            ide_ccdaf = "-1";
        }
        ide_ccdaf = ide_ccdaf.replace("null", "-1").trim();
        if (ide_ccdaf.isEmpty() == false) {
            ide_ccdaf = " and a.ide_ccdaf='" + ide_ccdaf + "' "; //8/1/2020 agrege el alias a
        }
        return "SELECT b.ide_cpcfa,fecha_emisi_cncre AS FECHA_EMISION,nombre_sresc as ESTADO,observacion_cncre as OBSERVACION,numero_cncre AS NUMERO,autorizacion_cncre AS AUTORIZACION,"
                + "(select sum(base_cndre) from con_detall_retenc where ide_cncre=a.ide_cncre)AS BASE_IMPONIBLE,"
                + "(select sum(valor_cndre) from con_detall_retenc where ide_cncre=a.ide_cncre)AS VALOR,numero_cpcfa as FACTURA,nom_geper AS PROVEEDOR\n"
                + "FROM con_cabece_retenc a\n"
                + "left join cxp_cabece_factur b on a.ide_cncre=b.ide_cncre\n"
                + "left join gen_persona c on b.ide_geper=c.ide_geper\n"
                + "left join sri_comprobante d on a.ide_srcom=d.ide_srcom "
                + "left join sri_estado_comprobante f on d.ide_sresc=f.ide_sresc "
                + "WHERE a.ide_empr=" + utilitario.getVariable("ide_empr") + "\n"
                + "and fecha_emisi_cncre BETWEEN '" + fechaInicio + "' and '" + fechaFin + "' \n"
                + "and es_venta_cncre = false and a.ide_cnccc is null \n"
                + ide_ccdaf
                + "ORDER BY numero_cncre desc,a.ide_cncre desc"; 
    }

    public String getSqlRetencionesVentasNoContabilizadas( String fechaInicio, String fechaFin) {
 
        return "SELECT  b.ide_cccfa as pk ,b.ide_cccfa,fecha_emisi_cncre,nombre_sresc as ESTADO,observacion_cncre as OBSERVACION,numero_cncre AS NUMERO,autorizacion_cncre AS AUTORIZACION,"
                + "(select sum(base_cndre) from con_detall_retenc where ide_cncre=a.ide_cncre)AS BASE_IMPONIBLE,"
                + "(select sum(valor_cndre) from con_detall_retenc where ide_cncre=a.ide_cncre)AS VALOR,secuencial_cccfa as NUM_FACTURA,nom_geper AS CLIENTE \n"
                + "FROM con_cabece_retenc a\n"
                + "left join cxc_cabece_factura b on a.ide_cncre=b.ide_cncre\n"
                + "left join gen_persona c on b.ide_geper=c.ide_geper\n"
                + "left join sri_comprobante d on a.ide_srcom=d.ide_srcom "
                + "left join sri_estado_comprobante f on d.ide_sresc=f.ide_sresc "
                + "WHERE a.ide_empr=" + utilitario.getVariable("ide_empr") + "\n"
                + "and fecha_emisi_cncre BETWEEN '" + fechaInicio + "' and '" + fechaFin + "' \n"
                + "and es_venta_cncre = true and a.ide_cnccc is null \n"
                + "ORDER BY numero_cncre desc,a.ide_cncre desc"; 
    }

}
