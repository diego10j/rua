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
 * @author dfjacome
 */
@Stateless
public class ServicioContabilidad {
    
    public final static String P_TIPO_CUENTA_ACTIVO = "0";
    private final Utilitario utilitario = new Utilitario();
    
    public TablaGenerica getCuenta(String ide_cndpc) {
        return utilitario.consultar("SELECT * FROM con_det_plan_cuen where ide_cndpc=" + ide_cndpc);
    }

    /**
     * Retorna la sentencia SQL para obtener cuentas hijas de tipo ACTIVOS
     *
     * @return
     */
    public String getSqlCuentasActivos() {
        return "select ide_cndpc,codig_recur_cndpc,nombre_cndpc "
                + "from con_det_plan_cuen "
                + "where ide_cntcu = " + P_TIPO_CUENTA_ACTIVO + " "
                + "ORDER BY codig_recur_cndpc";
    }

    /**
     * Retorna los movimientos de una cuenta de la sucursal actual en un rango
     * de fechas
     *
     * @param ide_cndpc Codigo de la cuenta
     * @param fechaInicio Fecha Inicio
     * @param fechaFin Fecha Fin
     * @return
     */
    public String getSqlMovimientosCuenta(String ide_cndpc, String fechaInicio, String fechaFin) {
        return "SELECT CAB.fecha_trans_cnccc as FECHA,CAB.ide_cnccc as ASIENTO,CAB.observacion_cnccc as OBSERVACION, "
                + "LUGAR.ide_cnlap,'' as DEBE, '' as HABER, "
                + "(DETA.valor_cndcc * sc.signo_cnscu) as valor_cndcc,'' as SALDO,PERSO.nom_geper as BENEFICIARIO, "
                + "CUENTA.nombre_cndpc as NOMBRE_CUENTA "
                + "from con_cab_comp_cont CAB "
                + "left join gen_persona PERSO on CAB.ide_geper=PERSO.ide_geper "
                + "inner join  con_det_comp_cont DETA on CAB.ide_cnccc=DETA.ide_cnccc "
                + "inner join con_det_plan_cuen CUENTA on  CUENTA.ide_cndpc = DETA.ide_cndpc "
                + "inner join con_lugar_aplicac LUGAR on  LUGAR.ide_cnlap=DETA.ide_cnlap "
                + "inner join con_tipo_cuenta tc on CUENTA.ide_cntcu=tc.ide_cntcu "
                + "inner join con_signo_cuenta sc on tc.ide_cntcu=sc.ide_cntcu and DETA.ide_cnlap=sc.ide_cnlap "
                + "WHERE CUENTA.ide_cndpc=" + ide_cndpc + " and fecha_trans_cnccc BETWEEN '" + fechaInicio + "' and '" + fechaFin + "' "
                + "and ide_cneco in (" + utilitario.getVariable("p_con_estado_comp_inicial") + "," + utilitario.getVariable("p_con_estado_comprobante_normal") + "," + utilitario.getVariable("p_con_estado_comp_final") + ") "
                + "and cab.ide_sucu=" + utilitario.getVariable("ide_sucu") + " ORDER BY CAB.fecha_trans_cnccc,cab.ide_cnccc asc";
    }

    /**
     * Retorna los ultimos N movimientos de una cuenta de la sucursal actual
     *
     * @param ide_cndpc
     * @param numMovimientos N movimientos
     * @return
     */
    public String getSqlMovimientosCuenta(String ide_cndpc, int numMovimientos) {
        return "SELECT CAB.fecha_trans_cnccc as FECHA,CAB.ide_cnccc as ASIENTO,CAB.observacion_cnccc as OBSERVACION, "
                + "LUGAR.ide_cnlap,'' as DEBE, '' as HABER, "
                + "(DETA.valor_cndcc * sc.signo_cnscu) as valor_cndcc,'' as SALDO,PERSO.nom_geper as BENEFICIARIO, "
                + "CUENTA.nombre_cndpc as NOMBRE_CUENTA "
                + "from con_cab_comp_cont CAB "
                + "left join gen_persona PERSO on CAB.ide_geper=PERSO.ide_geper "
                + "inner join  con_det_comp_cont DETA on CAB.ide_cnccc=DETA.ide_cnccc "
                + "inner join con_det_plan_cuen CUENTA on  CUENTA.ide_cndpc = DETA.ide_cndpc "
                + "inner join con_lugar_aplicac LUGAR on  LUGAR.ide_cnlap=DETA.ide_cnlap "
                + "inner join con_tipo_cuenta tc on CUENTA.ide_cntcu=tc.ide_cntcu "
                + "inner join con_signo_cuenta sc on tc.ide_cntcu=sc.ide_cntcu and DETA.ide_cnlap=sc.ide_cnlap "
                + "WHERE CUENTA.ide_cndpc=" + ide_cndpc + "  "
                + "and ide_cneco in (" + utilitario.getVariable("p_con_estado_comp_inicial") + "," + utilitario.getVariable("p_con_estado_comprobante_normal") + "," + utilitario.getVariable("p_con_estado_comp_final") + ") "
                + "and cab.ide_sucu=" + utilitario.getVariable("ide_sucu") + " ORDER BY CAB.fecha_trans_cnccc,cab.ide_cnccc asc LIMIT " + numMovimientos;
    }

    /**
     * Retorna el saldo contable inicial de una cuenta a una determindada fecha
     *
     * @param ide_cndpc
     * @param fecha
     * @return
     */
    public double getSaldoInicialCuenta(String ide_cndpc, String fecha) {
        //Retorna el saldo inicial de una cuenta segun el periodo activo
        double saldo = 0;
        String sql = "Select dpc.ide_cndpc,sum(dcc.valor_cndcc*sc.signo_cnscu) as valor "
                + "from con_cab_comp_cont ccc "
                + "inner join  con_det_comp_cont dcc on ccc.ide_cnccc=dcc.ide_cnccc "
                + "inner join con_det_plan_cuen dpc on  dpc.ide_cndpc = dcc.ide_cndpc "
                + "inner join con_tipo_cuenta tc on dpc.ide_cntcu=tc.ide_cntcu "
                + "inner  join con_signo_cuenta sc on tc.ide_cntcu=sc.ide_cntcu and dcc.ide_cnlap=sc.ide_cnlap "
                + "WHERE  ccc.fecha_trans_cnccc< '" + fecha + "' "
                + "and ccc.ide_cneco in (" + utilitario.getVariable("p_con_estado_comp_inicial") + "," + utilitario.getVariable("p_con_estado_comprobante_normal") + "," + utilitario.getVariable("p_con_estado_comp_final") + ") "
                + "and ccc.ide_sucu=" + utilitario.getVariable("IDE_SUCU") + " "
                + "and dpc.ide_cndpc=" + ide_cndpc + " "
                + "GROUP BY dpc.ide_cndpc ";
        TablaGenerica tab_saldo = utilitario.consultar(sql);
        if (tab_saldo.getTotalFilas() > 0) {
            if (tab_saldo.getValor(0, "valor") != null) {
                try {
                    saldo = Double.parseDouble(tab_saldo.getValor(0, "valor"));
                } catch (Exception e) {
                }
            }
        }
        return saldo;
    }
    
}
