/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servicios.contabilidad;

import framework.aplicacion.TablaGenerica;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import sistema.aplicacion.Utilitario;

/**
 *
 * @author dfjacome
 */
@Stateless
public class ServicioContabilidadGeneral {

    public static String P_TIPO_CUENTA_ACTIVO = "0";
    public static String P_TIPO_CUENTA_PASIVO = "4";
    private final Utilitario utilitario = new Utilitario();

    @PostConstruct
    public void init() {
        P_TIPO_CUENTA_ACTIVO = "0"; ///******PONER EN VARIABLE
        P_TIPO_CUENTA_PASIVO = "4"; ///******PONER EN VARIABLE
    }

    public TablaGenerica getCuenta(String ide_cndpc) {
        return utilitario.consultar("SELECT * FROM con_det_plan_cuen where ide_cndpc=" + ide_cndpc);
    }

    public TablaGenerica getCuentaporCodigo(String codig_recur_cndpc) {
        return utilitario.consultar("SELECT * FROM con_det_plan_cuen where codig_recur_cndpc in (" + codig_recur_cndpc + ")");
    }

    public String getSqlPeridos() {
        return "select * from con_periodo where ide_sucu=" + utilitario.getVariable("ide_sucu") + " order by ide_cnper desc";
    }

    /**
     * Retorna la sentencia SQL para obtener cuentas de tipo ACTIVOS de la
     * empresa
     *
     * @return
     */
    public String getSqlCuentasActivos() {
        return "select ide_cndpc,codig_recur_cndpc,nombre_cndpc "
                + "from con_det_plan_cuen "
                + "where ide_cntcu = " + P_TIPO_CUENTA_ACTIVO + " "
                + " and ide_cncpc=" + getPlandeCuentasActivo()
                + " and ide_empr=" + utilitario.getVariable("ide_empr")
                + " ORDER BY codig_recur_cndpc";
    }

    /**
     * Retorna la sentencia SQL para obtener cuentas de tipo PASIVO de la
     * empresa
     *
     * @return
     */
    public String getSqlCuentasPasivos() {
        return "select ide_cndpc,codig_recur_cndpc,nombre_cndpc "
                + "from con_det_plan_cuen "
                + "where ide_cntcu = " + P_TIPO_CUENTA_PASIVO + " "
                + " and ide_cncpc=" + getPlandeCuentasActivo()
                + " and ide_empr=" + utilitario.getVariable("ide_empr")
                + " ORDER BY codig_recur_cndpc";
    }

    /**
     * Retorna la sentencia SQL para obtener el plan de cuentas de la empresa
     *
     * @return
     */
    public String getSqlCuentas() {
        return "select ide_cndpc,codig_recur_cndpc,nombre_cndpc "
                + "from con_det_plan_cuen "
                + " WHERE ide_cncpc=" + getPlandeCuentasActivo()
                + " and ide_empr=" + utilitario.getVariable("ide_empr")
                + " ORDER BY codig_recur_cndpc";
    }

    /**
     * Retorna la sentencia SQL para obtener el plan de cuentas HIJO de la
     * empresa
     *
     * @return
     */
    public String getSqlCuentasHijas() {
        return "select ide_cndpc,codig_recur_cndpc,nombre_cndpc "
                + "from con_det_plan_cuen where nivel_cndpc='HIJO' "
                + " and ide_cncpc=" + getPlandeCuentasActivo()
                + " and ide_empr=" + utilitario.getVariable("ide_empr")
                + " ORDER BY codig_recur_cndpc";
    }

    /**
     * Retorna los movimientos de una cuenta de la sucursal actual en un rango
     * de fechas
     *
     * @param ide_cndpc Codigo de la cuenta
     * @param fechaInicio Fecha Inicio
     * @param fechaFin Fecha Fin
     * @param ide_geper filtar por beneficiario
     * @return
     */
    public String getSqlMovimientosCuenta(String ide_cndpc, String fechaInicio, String fechaFin, String ide_geper) {
        if (ide_geper == null) {
            ide_geper = "";
        }
        ide_geper = ide_geper.trim();
        if (!ide_geper.isEmpty()) {
            ide_geper = " AND CAB.ide_geper IN (" + ide_geper + ") ";
        }

        return "SELECT CAB.fecha_trans_cnccc,CAB.ide_cnccc ,PERSO.nom_geper as BENEFICIARIO, "
                + "DETA.ide_cnlap,'' as DEBE, '' as HABER, "
                + "(DETA.valor_cndcc * sc.signo_cnscu) as valor_cndcc,'' as SALDO, CAB.observacion_cnccc as OBSERVACION,CAB.ide_geper "
                + "from con_cab_comp_cont CAB "
                + "left join gen_persona PERSO on CAB.ide_geper=PERSO.ide_geper "
                + "inner join  con_det_comp_cont DETA on CAB.ide_cnccc=DETA.ide_cnccc "
                + "inner join con_det_plan_cuen CUENTA on  CUENTA.ide_cndpc = DETA.ide_cndpc "
                + "inner join con_tipo_cuenta tc on CUENTA.ide_cntcu=tc.ide_cntcu "
                + "inner join con_signo_cuenta sc on tc.ide_cntcu=sc.ide_cntcu and DETA.ide_cnlap=sc.ide_cnlap "
                + "WHERE CUENTA.ide_cndpc=" + ide_cndpc + " and fecha_trans_cnccc BETWEEN '" + fechaInicio + "' and '" + fechaFin + "' "
                + "and ide_cneco in (" + utilitario.getVariable("p_con_estado_comp_inicial") + "," + utilitario.getVariable("p_con_estado_comprobante_normal") + "," + utilitario.getVariable("p_con_estado_comp_final") + ") "
                + ide_geper
                + "and cab.ide_sucu=" + utilitario.getVariable("ide_sucu") + " ORDER BY CAB.fecha_trans_cnccc,cab.ide_cnccc asc";
    }

    public String getSqlMovimientosCuentaPersona(String ide_cndpc, String fechaInicio, String fechaFin, String ide_geper) {
        return "SELECT CAB.fecha_trans_cnccc,CAB.ide_cnccc ,PERSO.nom_geper as BENEFICIARIO, "
                + "DETA.ide_cnlap,'' as DEBE, '' as HABER, "
                + "(DETA.valor_cndcc * sc.signo_cnscu) as valor_cndcc,'' as SALDO, CAB.observacion_cnccc as OBSERVACION "
                + "from con_cab_comp_cont CAB "
                + "left join gen_persona PERSO on CAB.ide_geper=PERSO.ide_geper "
                + "inner join  con_det_comp_cont DETA on CAB.ide_cnccc=DETA.ide_cnccc "
                + "inner join con_det_plan_cuen CUENTA on  CUENTA.ide_cndpc = DETA.ide_cndpc "
                + "inner join con_tipo_cuenta tc on CUENTA.ide_cntcu=tc.ide_cntcu "
                + "inner join con_signo_cuenta sc on tc.ide_cntcu=sc.ide_cntcu and DETA.ide_cnlap=sc.ide_cnlap "
                + "WHERE CUENTA.ide_cndpc=" + ide_cndpc + " and fecha_trans_cnccc BETWEEN '" + fechaInicio + "' and '" + fechaFin + "' "
                + "and ide_cneco in (" + utilitario.getVariable("p_con_estado_comp_inicial") + "," + utilitario.getVariable("p_con_estado_comprobante_normal") + "," + utilitario.getVariable("p_con_estado_comp_final") + ") "
                + "and cab.ide_sucu=" + utilitario.getVariable("ide_sucu") + " "
                + "and cab.ide_geper=" + ide_geper + " "
                + "ORDER BY CAB.fecha_trans_cnccc,cab.ide_cnccc asc";
    }

    /**
     * Retorna el saldo inicial de una cuenta a una determindada fecha
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

    /**
     * Retorna el saldo de una cuenta a una determindada fecha
     *
     * @param ide_cndpc
     * @param fecha
     * @return
     */
    public double getSaldoCuenta(String ide_cndpc, String fecha) {
        //Retorna el saldo inicial de una cuenta segun el periodo activo
        double saldo = 0;
        String sql = "Select dpc.ide_cndpc,sum(dcc.valor_cndcc*sc.signo_cnscu) as valor "
                + "from con_cab_comp_cont ccc "
                + "inner join  con_det_comp_cont dcc on ccc.ide_cnccc=dcc.ide_cnccc "
                + "inner join con_det_plan_cuen dpc on  dpc.ide_cndpc = dcc.ide_cndpc "
                + "inner join con_tipo_cuenta tc on dpc.ide_cntcu=tc.ide_cntcu "
                + "inner  join con_signo_cuenta sc on tc.ide_cntcu=sc.ide_cntcu and dcc.ide_cnlap=sc.ide_cnlap "
                + "WHERE  ccc.fecha_trans_cnccc<= '" + fecha + "' "
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

    /**
     * Retorna el saldo de la cuenta contable
     *
     * @param ide_cndpc
     * @return
     */
    public double getSaldoCuenta(String ide_cndpc) {
        return getSaldoCuenta(ide_cndpc, utilitario.getFechaActual());
    }

    /**
     * Retorna el número de dias de credito de una forma de pago
     *
     * @param ide_cndfp
     * @return 0==contado !=0 Credito
     */
    public int getDiasFormaPago(String ide_cndfp) {
        int dias = 0;
        TablaGenerica tab_dias = utilitario.consultar("select ide_cndfp,dias_cndfp from con_deta_forma_pago where ide_cndfp=" + ide_cndfp);
        if (tab_dias.isEmpty() == false) {
            try {
                dias = Integer.parseInt(tab_dias.getValor("dias_cndfp"));
            } catch (Exception e) {
            }
        }
        return dias;
    }

    /**
     * Retorna sentencia SQL para obtener el libro diario en un determinado
     * rango de fechas
     *
     * @param fechaInicio
     * @param fechaFin
     * @return
     */
    public String getSqlLibroDiario(String fechaInicio, String fechaFin) {
        String estadosComprobantes = "" + utilitario.getVariable("p_con_estado_comprobante_normal") + "," + utilitario.getVariable("p_con_estado_comp_inicial") + "," + utilitario.getVariable("p_con_estado_comp_final");
        return "SELECT DCC.ide_cndcc,CCC.ide_cnccc,CCC.fecha_trans_cnccc,CCC.numero_cnccc,\n"
                + "DPC.codig_recur_cndpc,DPC.nombre_cndpc,\n"
                + "case when DCC.ide_cnlap = 1 THEN DCC.valor_cndcc  end as DEBE,case when DCC.ide_cnlap = 0 THEN DCC.valor_cndcc end as HABER\n"
                + ",CCC.observacion_cnccc,TC.nombre_cntcm\n"
                + "FROM con_cab_comp_cont CCC\n"
                + "LEFT JOIN sis_modulo SM ON CCC.ide_modu=SM.ide_modu\n"
                + "INNER JOIN con_tipo_comproba TC ON CCC.ide_cntcm=TC.ide_cntcm\n"
                + "inner JOIN  con_det_comp_cont DCC ON  CCC.ide_cnccc=DCC.ide_cnccc\n"
                + "inner JOIN con_det_plan_cuen DPC ON DCC.ide_cndpc=DPC.ide_cndpc\n"
                + "left join con_signo_cuenta sc on DPC.ide_cntcu=sc.ide_cntcu and DCC.ide_cnlap=sc.ide_cnlap  "
                + "WHERE  CCC.ide_sucu=" + utilitario.getVariable("ide_sucu") + "\n"
                + "and CCC.ide_cneco IN (" + estadosComprobantes + ")\n"
                + "AND CCC.fecha_trans_cnccc BETWEEN '" + fechaInicio + "' AND '" + fechaFin + "'\n"
                + "ORDER BY CCC.fecha_trans_cnccc DESC,numero_cnccc DESC,DCC.ide_cnlap desc";
    }

    /**
     * Sql que genera el Balance General
     *
     * @param fecha_fin
     * @return
     */
    public String getSqlBalanceGeneral(String fecha_fin) {
        String p_activo = utilitario.getVariable("p_con_tipo_cuenta_activo");
        String p_pasivo = utilitario.getVariable("p_con_tipo_cuenta_pasivo");
        String p_patrimonio = utilitario.getVariable("p_con_tipo_cuenta_patrimonio");
        String p_tipo_cuentas = p_activo + "," + p_pasivo + "," + p_patrimonio;
        String sql = getSqlBalances(fecha_fin, p_tipo_cuentas);
        return sql;
    }

    /**
     * Sql genera estado de resultados
     *
     * @param fecha_fin
     * @return
     */
    public String getSqlEstadoResultados(String fecha_fin) {
        String p_ingresos = utilitario.getVariable("p_con_tipo_cuenta_ingresos");
        String p_gastos = utilitario.getVariable("p_con_tipo_cuenta_gastos");
        String p_costos = utilitario.getVariable("p_con_tipo_cuenta_costos");
        String p_tipo_cuentas = p_ingresos + "," + p_costos + "," + p_gastos;
        String sql = getSqlBalances(fecha_fin, p_tipo_cuentas);
        return sql;
    }

    /**
     * Sql con saldo de las cuentas
     *
     * @param fecha_fin
     * @param p_tipo_cuentas
     * @return
     */
    private String getSqlBalances(String fecha_fin, String p_tipo_cuentas) {

        String estado_normal = utilitario.getVariable("p_con_estado_comprobante_normal");
        String estado_inicial = utilitario.getVariable("p_con_estado_comp_inicial");
        String estado_final = utilitario.getVariable("p_con_estado_comp_final");

        return "(SELECT * FROM (select dpc.ide_cndpc,dpc.codig_recur_cndpc,repeat('  ', ide_cnncu::int ) || dpc.nombre_cndpc as nombre_cndpc,dpc.ide_cnncu, "
                + "sum(dcc.valor_cndcc*sc.signo_cnscu) as valor,"
                + "con_ide_cndpc,dpc.ide_cntcu from  "
                + "con_cab_comp_cont ccc "
                + "inner join  con_det_comp_cont dcc on ccc.ide_cnccc=dcc.ide_cnccc "
                + "inner join con_det_plan_cuen dpc on  dpc.ide_cndpc = dcc.ide_cndpc "
                + "inner join con_tipo_cuenta tc on dpc.ide_cntcu=tc.ide_cntcu "
                + "inner  join con_signo_cuenta sc on tc.ide_cntcu=sc.ide_cntcu and dcc.ide_cnlap=sc.ide_cnlap "
                + "WHERE (ccc.fecha_trans_cnccc <='" + fecha_fin + "') "
                + "and ccc.ide_cneco IN (" + estado_normal + "," + estado_inicial + "," + estado_final + ") "
                + "and ccc.ide_sucu=" + utilitario.getVariable("IDE_SUCU") + " " ///solo la sucursal 
                + "and dpc.ide_cntcu in (" + p_tipo_cuentas + ") "
                + " GROUP BY dpc.ide_cndpc,codig_recur_cndpc, nombre_cndpc,con_ide_cndpc "
                + "HAVING (sum(dcc.valor_cndcc*sc.signo_cnscu) <>0) ) as C1 "
                + "UNION SELECT * FROM(Select ide_cndpc,codig_recur_cndpc,repeat('  ', ide_cnncu::int ) || nombre_cndpc,ide_cnncu , '0' as valor, "
                + "con_ide_cndpc,ide_cntcu "
                + "from  con_det_plan_cuen where ide_cntcu in (" + p_tipo_cuentas + ") "
                + "and ide_cndpc not in(select ide_cndpc from con_det_comp_cont where "
                + "ide_sucu=" + utilitario.getVariable("IDE_SUCU") + ") "
                + "and nivel_cndpc='PADRE' ) AS C2) "
                + "order by codig_recur_cndpc ";
    }

    /**
     * Retorna los totales del Estado de Resultados
     *
     * @param fecha_final
     * @return INGRESOS, GASTOS, COSTOS
     */
    public Map<String, Double> getTotalesEstadoResultados(String fecha_final) {
        Map<String, Double> resultado = new HashMap();
        String p_ingresos = utilitario.getVariable("p_con_tipo_cuenta_ingresos");
        String p_gastos = utilitario.getVariable("p_con_tipo_cuenta_gastos");
        String p_costos = utilitario.getVariable("p_con_tipo_cuenta_costos");
        String p_tipo_cuentas = p_ingresos + "," + p_costos + "," + p_gastos;

        String sql = getSqlTotalesBalances(fecha_final, p_tipo_cuentas);

        TablaGenerica tab_estado = utilitario.consultar(sql);
        double tot_ingresos = 0;
        double tot_gastos = 0;
        double tot_costos = 0;
        for (int i = 0; i < tab_estado.getTotalFilas(); i++) {
            if (tab_estado.getValor(i, "ide_cntcu").equals(p_ingresos)) {
                tot_ingresos += Double.parseDouble(tab_estado.getValor(i, "valor"));
            } else if (tab_estado.getValor(i, "ide_cntcu").equals(p_gastos)) {
                tot_gastos += Double.parseDouble(tab_estado.getValor(i, "valor"));
            } else if (tab_estado.getValor(i, "ide_cntcu").equals(p_costos)) {
                tot_costos += Double.parseDouble(tab_estado.getValor(i, "valor"));
            }
        }
        resultado.put("INGRESOS", Double.parseDouble(utilitario.getFormatoNumero(tot_ingresos)));
        resultado.put("GASTOS", Double.parseDouble(utilitario.getFormatoNumero(tot_gastos)));
        resultado.put("COSTOS", Double.parseDouble(utilitario.getFormatoNumero(tot_costos)));
        return resultado;
    }

    /**
     * Retorna los totales del balance general a una fecha determinada
     *
     * @param fecha_final
     * @return ACTIVO, PASIVO, PATRIMONIO
     */
    public Map<String, Double> getTotalesBalanceGeneral(String fecha_final) {
        Map<String, Double> resultado = new HashMap();
        String p_activo = utilitario.getVariable("p_con_tipo_cuenta_activo");
        String p_pasivo = utilitario.getVariable("p_con_tipo_cuenta_pasivo");
        String p_patrimonio = utilitario.getVariable("p_con_tipo_cuenta_patrimonio");

        String p_tipo_cuentas = p_activo + "," + p_pasivo + "," + p_patrimonio;
        String sql = getSqlTotalesBalances(fecha_final, p_tipo_cuentas);

        TablaGenerica tab_balance = utilitario.consultar(sql);
        double tot_activo = 0;
        double tot_pasivo = 0;
        double tot_patrimonio = 0;
        for (int i = 0; i < tab_balance.getTotalFilas(); i++) {
            if (tab_balance.getValor(i, "ide_cntcu").equals(p_activo)) {
                tot_activo += Double.parseDouble(tab_balance.getValor(i, "valor"));
            } else if (tab_balance.getValor(i, "ide_cntcu").equals(p_pasivo)) {
                tot_pasivo += Double.parseDouble(tab_balance.getValor(i, "valor"));
            } else if (tab_balance.getValor(i, "ide_cntcu").equals(p_patrimonio)) {
                tot_patrimonio += Double.parseDouble(tab_balance.getValor(i, "valor"));
            }
        }
        resultado.put("ACTIVO", Double.parseDouble(utilitario.getFormatoNumero(tot_activo)));
        resultado.put("PASIVO", Double.parseDouble(utilitario.getFormatoNumero(tot_pasivo)));
        resultado.put("PATRIMONIO", Double.parseDouble(utilitario.getFormatoNumero(tot_patrimonio)));
        return resultado;
    }

    /**
     * Sql que retorna el saldo de las cuentas a una determinada fecha
     *
     * @param fecha_fin
     * @param p_tipo_cuentas
     * @return
     */
    private String getSqlTotalesBalances(String fecha_fin, String p_tipo_cuentas) {
        String estado_normal = utilitario.getVariable("p_con_estado_comprobante_normal");
        String estado_inicial = utilitario.getVariable("p_con_estado_comp_inicial");
        String estado_final = utilitario.getVariable("p_con_estado_comp_final");
        return "select dpc.ide_cndpc,dpc.nombre_cndpc,dpc.ide_cnncu , "
                + "sum(dcc.valor_cndcc*sc.signo_cnscu) as valor, "
                + "dpc.codig_recur_cndpc,con_ide_cndpc,dpc.ide_cntcu from "
                + "con_cab_comp_cont ccc "
                + "inner join  con_det_comp_cont dcc on ccc.ide_cnccc=dcc.ide_cnccc "
                + "inner join con_det_plan_cuen dpc on  dpc.ide_cndpc = dcc.ide_cndpc "
                + "inner join con_tipo_cuenta tc on dpc.ide_cntcu=tc.ide_cntcu "
                + "inner  join con_signo_cuenta sc on tc.ide_cntcu=sc.ide_cntcu and dcc.ide_cnlap=sc.ide_cnlap "
                + "WHERE (ccc.fecha_trans_cnccc <='" + fecha_fin + "') "
                + "and ccc.ide_sucu=" + utilitario.getVariable("ide_sucu") + " " ///solo la sucursal 
                + "and ccc.ide_cneco in (" + estado_normal + "," + estado_inicial + "," + estado_final + ") "
                + "and dpc.ide_cntcu in (" + p_tipo_cuentas + ") "
                + "GROUP BY dpc.ide_cndpc,codig_recur_cndpc, nombre_cndpc,con_ide_cndpc "
                + "HAVING (sum(dcc.valor_cndcc*sc.signo_cnscu) <>0) ";
    }

    /**
     * Retorna el ultimo nivel del plan de cuentas
     *
     * @return
     */
    public int getUltimoNivelCuentas() {
        List lis_nivel_max = utilitario.getConexion().consultar("select max (ide_cnncu) from con_det_plan_cuen dpc where ide_empr=" + utilitario.getVariable("ide_empr") + " and ide_cncpc=" + getPlandeCuentasActivo());
        if (lis_nivel_max != null) {
            return Integer.parseInt(lis_nivel_max.get(0).toString());
        } else {
            return -1;
        }
    }

    /**
     * Retorna el ide_cncpc del plan de cuentas activo
     *
     * @return
     */
    public String getPlandeCuentasActivo() {
        TablaGenerica tab_plan_activo = utilitario.consultar("select * from con_cab_plan_cuen where ide_empr=" + utilitario.getVariable("ide_empr") + " and activo_cncpc is TRUE");
        if (tab_plan_activo.getTotalFilas() > 0) {
            return tab_plan_activo.getValor(0, "ide_cncpc");
        } else {
            return null;
        }
    }

    /**
     * Sql que retorna los niveles del Plan de cuentas
     *
     * @return
     */
    public String getSqlNivelesPlandeCuentas() {
        return "select ide_cnncu,nombre_cnncu from con_nivel_cuenta where ide_empr=" + utilitario.getVariable("ide_empr") + "order by ide_cnncu";
    }

    /**
     * Retorna si una cuenta es nivel HIJO
     *
     * @param ide_cndpc
     * @return
     */
    public boolean isCuentaContableHija(String ide_cndpc) {
        if (ide_cndpc != null && !ide_cndpc.isEmpty()) {
            TablaGenerica tab_cuenta = getCuenta(ide_cndpc);
            String str_nivel_cndpc = tab_cuenta.getValor("nivel_cndpc");
            if (str_nivel_cndpc != null) {
                if (str_nivel_cndpc.equalsIgnoreCase("HIJO")) {
                    return true;
                }
            }
        }
        return false;
    }

}
