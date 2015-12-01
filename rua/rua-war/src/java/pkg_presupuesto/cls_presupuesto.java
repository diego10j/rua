/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg_presupuesto;

import framework.aplicacion.TablaGenerica;
import java.util.ArrayList;
import java.util.List;
import pkg_contabilidad.cls_contabilidad;
import sistema.aplicacion.Utilitario;

/**
 *
 * @author Edwin
 */
public class cls_presupuesto {

    private Utilitario utilitario = new Utilitario();

    private String getSqlTablaejecucionSoloCuentas(String ide_prppr) {
        String sql = "select "
                + "dprc.ide_prdpc,"
                + "dprc.ide_prtpr,"
                + "tpr.nombre_prtpr,"
                + "dpc.ide_cndpc, "
                + "dpc.codig_recur_cndpc,"
                + "dpc.nombre_cndpc, "
                + "dprc.valor_prdpc as total,"
                + "'0' as valor_ejecutado,"
                + "'0' as porcentaje_ejecutado,"
                + "'0' as variacion "
                + "from pre_deta_presu_cuentas dprc "
                + "left join con_det_plan_cuen dpc on dpc.ide_cndpc=dprc.ide_cndpc "
                + "left join pre_tipo_presupuesto tpr on tpr.ide_prtpr=dprc.ide_prtpr "
                + "left join pre_cab_presu_cuentas cpc on cpc.ide_prcpc=dprc.ide_prcpc "
                + "where cpc.ide_prppr=" + ide_prppr + " "
                + "and dprc.ide_sucu=" + utilitario.getVariable("ide_sucu") + " "
                + "order by dprc.ide_prtpr desc";
        return sql;
    }

    private String getSqlTablaejecucionEventos(String ide_prppr) {
        String sql = "select "
                + "dprc.ide_prdep,"
                + "dprc.ide_prtpr,"
                + "tpr.nombre_prtpr,"
                + "dpc.ide_cndpc, "
                + "dpc.codig_recur_cndpc,"
                + "dpc.nombre_cndpc, "
                + "dprc.total_prdep as total,"
                + "'0' as valor_ejecutado,"
                + "'0' as porcentaje_ejecutado,"
                + "'0' as variacion "
                + "from pre_detalle_presu dprc "
                + "left join con_det_plan_cuen dpc on dpc.ide_cndpc=dprc.ide_cndpc "
                + "left join pre_tipo_presupuesto tpr on tpr.ide_prtpr=dprc.ide_prtpr "
                + "left join pre_descripcion_presu dpr on dpr.ide_prdpr=dprc.ide_prdpr "
                + "left join pre_cab_plan_presu cpp on cpp.ide_prcppr=dpr.ide_prcppr "
                + "where cpp.ide_sucu=" + utilitario.getVariable("ide_sucu") + " "
                + "and cpp.ide_prppr=" + ide_prppr + " "
//                + "and cpp.ide_georg in (47) "
//                + "and cpp.ide_gemes in (0,1,2,3,4,5) "
                + "order by dprc.ide_prtpr desc";
        return sql;
    }

    public TablaGenerica getTablaEjecucionPresupuestaria(String ide_prppr, String fecha_fin, boolean solo_cuentas) {
        TablaGenerica tab_ejecucion_presupuestaria;
        if (solo_cuentas) {
            tab_ejecucion_presupuestaria = getTablaPresupuestoEjecutado(getSqlTablaejecucionSoloCuentas(ide_prppr), fecha_fin);
        } else {
            tab_ejecucion_presupuestaria = getTablaPresupuestoEjecutado(getSqlTablaejecucionEventos(ide_prppr), fecha_fin);
        }
        return tab_ejecucion_presupuestaria;
    }

    private double obtenerSaldoCuenta(String ide_cndpc) {
        double saldo_cuenta = 0;
        for (int i = 0; i < tab_estado_resultados.getTotalFilas(); i++) {
            if (ide_cndpc.equals(tab_estado_resultados.getValor(i,"ide_cndpc"))){
                saldo_cuenta=Double.parseDouble(tab_estado_resultados.getValor(i,"valor"));
                break;
            }
        }
        return saldo_cuenta;
    }
    
    TablaGenerica tab_estado_resultados = new TablaGenerica();

    private TablaGenerica getTablaPresupuestoEjecutado(String sql, String fecha_fin) {
        TablaGenerica tab_det_presu_cuentas = utilitario.consultar(sql);

        cls_contabilidad con = new cls_contabilidad();
        tab_estado_resultados = con.generarEstadoResultados(false, con.getFechaInicialPeriodo(fecha_fin), fecha_fin, con.obtenerUltimoNivel());
        for (int i = 0; i < tab_det_presu_cuentas.getTotalFilas(); i++) {
            if (tab_det_presu_cuentas.getValor(i, "ide_cndpc") != null && !tab_det_presu_cuentas.getValor(i, "ide_cndpc").isEmpty()) {
                double val_ejecutado = obtenerSaldoCuenta(tab_det_presu_cuentas.getValor(i, "ide_cndpc"));
                double val_presupuestado = 0;
                try {
                    val_presupuestado = Double.parseDouble(tab_det_presu_cuentas.getValor(i, "total"));
                } catch (Exception e) {
                }
                double por_ejecutado = (val_ejecutado * 100) / val_presupuestado;
                double variacion = val_presupuestado - val_ejecutado;
                tab_det_presu_cuentas.setValor(i, "valor_ejecutado", utilitario.getFormatoNumero(val_ejecutado, 2));
                tab_det_presu_cuentas.setValor(i, "porcentaje_ejecutado", utilitario.getFormatoNumero(por_ejecutado, 2) + "");
                tab_det_presu_cuentas.setValor(i, "variacion", utilitario.getFormatoNumero(variacion) + "");
            }
        }

        return tab_det_presu_cuentas;
    }

    public List calcularTotalesDetallePresupuesto(TablaGenerica tab_detalle_presu, String ide_prtpr) {
        List lis_totales = new ArrayList();
        double tot_presupuestado = 0;
        double tot_ejecutado = 0;
        double tot_variacion = 0;

        for (int i = 0; i < tab_detalle_presu.getTotalFilas(); i++) {
            if (tab_detalle_presu.getValor(i, "ide_prtpr") != null && !tab_detalle_presu.getValor(i, "ide_prtpr").isEmpty()) {
                if (tab_detalle_presu.getValor(i, "ide_prtpr").equals(ide_prtpr)) {
                    try {
                        tot_presupuestado = tot_presupuestado + Double.parseDouble(tab_detalle_presu.getValor(i, "total"));
                    } catch (Exception e) {
                    }
                    try {
                        tot_ejecutado = tot_ejecutado + Double.parseDouble(tab_detalle_presu.getValor(i, "valor_ejecutado"));
                    } catch (Exception e) {
                    }
                    try {
                        tot_variacion = tot_variacion + Double.parseDouble(tab_detalle_presu.getValor(i, "variacion"));
                    } catch (Exception e) {
                    }
                }
            }
        }
        lis_totales.add(tot_presupuestado);
        lis_totales.add(tot_ejecutado);
        lis_totales.add(tot_variacion);
        return lis_totales;
    }
}
