/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package componentes;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Tabla;
import java.util.ArrayList;
import java.util.List;
import sistema.aplicacion.Utilitario;

/**
 *
 * @author Diego
 */
public class cls_contabilidad {

    private Utilitario utilitario = new Utilitario();
    private Tabla tab_cabecera = new Tabla();
    private Tabla tab_detalle = new Tabla();
    private long ide_cnccc = -1;
    private long ide_cndcc = -1;

    public cls_contabilidad() {
        tab_cabecera.setTabla("con_cab_comp_cont", "ide_cnccc", -1);
        tab_cabecera.setCondicion("ide_cnccc=-1");
        tab_cabecera.setGenerarPrimaria(false);
        tab_cabecera.getColumna("ide_cnccc").setExterna(false);
        tab_cabecera.ejecutarSql();

        tab_detalle.setTabla("con_det_comp_cont", "ide_cndcc", -1);
        tab_detalle.setCondicion("ide_cnccc=-1");
        tab_detalle.ejecutarSql();
        tab_detalle.setGenerarPrimaria(false);
        tab_detalle.getColumna("ide_cndcc").setExterna(false);

    }

    public boolean esCuentaContableHija(String ide_cndpc) {
        if (ide_cndpc != null && !ide_cndpc.isEmpty()) {
            String str_nivel_cndpc = getParametroPlanCuentas(ide_cndpc, "nivel_cndpc");
            if (str_nivel_cndpc != null) {
                if (str_nivel_cndpc.equalsIgnoreCase("HIJO")) {
                    return true;
                }
            }
        }
        return false;
    }

    private String getParametroPlanCuentas(String variable_busqueda, String parametro_retorno) {
        if (variable_busqueda != null && !variable_busqueda.isEmpty() && parametro_retorno != null && !parametro_retorno.isEmpty()) {
            TablaGenerica tab_plan_cuentas = utilitario.consultar("select * from con_det_plan_cuen where ide_cndpc=" + variable_busqueda);
            if (tab_plan_cuentas.getTotalFilas() > 0) {
                if (tab_plan_cuentas.getValor(0, parametro_retorno) != null && !tab_plan_cuentas.getValor(0, parametro_retorno).isEmpty()) {
                    return tab_plan_cuentas.getValor(0, parametro_retorno);
                }
            }
        }
        return null;
    }

    public String generarAsientoContable(cls_cab_comp_cont comprobante) {
        if (validarPeriodo(comprobante.getFecha_trans_cnccc())) {
            comprobante.resumirComprobane();
            tab_cabecera.getColumna("ide_usua").setValorDefecto(utilitario.getVariable("ide_usua"));
            tab_cabecera.getColumna("fecha_siste_cnccc").setValorDefecto(utilitario.getFechaActual());

            if (comprobante.validarComprobane()) {
                if (ide_cnccc == -1) {
                    ide_cnccc = utilitario.getConexion().getMaximo("con_cab_comp_cont", "ide_cnccc",1);
                } else {
                    ide_cnccc++;
                }
                tab_cabecera.insertar();
                tab_cabecera.setValor("ide_cnccc", ide_cnccc + "");
                tab_cabecera.setValor("hora_sistem_cnccc", utilitario.getHoraActual());
                tab_cabecera.setValor("ide_cntcm", comprobante.getIde_cntcm());
                tab_cabecera.setValor("ide_cneco", comprobante.getIde_cneco());
                tab_cabecera.setValor("ide_modu", comprobante.getIde_modu());
                tab_cabecera.setValor("ide_geper", comprobante.getIde_geper());
                tab_cabecera.setValor("fecha_trans_cnccc", comprobante.getFecha_trans_cnccc());
                tab_cabecera.setValor("observacion_cnccc", comprobante.getObservacion_cnccc());
                tab_cabecera.setValor("numero_cnccc", generarSecuencial(comprobante.getFecha_trans_cnccc()));
                tab_cabecera.guardar();
                tab_detalle.getColumna("ide_cnccc").setValorDefecto(tab_cabecera.getValor("ide_cnccc"));
                for (int i = 0; i < comprobante.getDetalles().size(); i++) {
                    if (ide_cndcc == -1) {
                        ide_cndcc = utilitario.getConexion().getMaximo("con_det_comp_cont", "ide_cndcc",comprobante.getDetalles().size());
                    } else {
                        ide_cndcc++;
                    }
                    tab_detalle.insertar();
                    tab_detalle.setValor("ide_cndcc", ide_cndcc + "");
                    tab_detalle.setValor("ide_cnlap", comprobante.getDetalles().get(i).getIde_cnlap());
                    tab_detalle.setValor("ide_cndpc", comprobante.getDetalles().get(i).getIde_cndpc());
                    tab_detalle.setValor("valor_cndcc", comprobante.getDetalles().get(i).getValor_cndcc() + "");
                    tab_detalle.setValor("observacion_cndcc", comprobante.getDetalles().get(i).getObservacion_cndcc());
                }
                tab_detalle.guardar();
                System.out.println("Asiento contable generado correctamente: ide_cnccc " + tab_cabecera.getValor("ide_cnccc"));
                return tab_cabecera.getValor("ide_cnccc");

            } else {
                return null;
            }
        } else {
//            utilitario.agregarMensajeError("No se puede generar el comprobante de Contabilidad", "El périodo de la fecha del comprobante ya fue cerrado");
            return null;
        }
    }

    public boolean validarPeriodo(String fecha) {
        if (fecha != null && !fecha.isEmpty()) {
            TablaGenerica tab_periodo = utilitario.consultar("SELECT  * FROM con_periodo WHERE ide_sucu=" + utilitario.getVariable("ide_sucu") + " and estado_cnper=true");
            if (tab_periodo.getTotalFilas() > 0) {
                if (tab_periodo.getValor("fecha_fin_cnper") == null || tab_periodo.getValor("fecha_fin_cnper").isEmpty()) {
                    utilitario.agregarMensajeError("No se puede generar el comprobante de Contabilidad", "No existe fecha de cierre de Periodo");
                    return false;
                }
            }
            tab_periodo = utilitario.consultar("SELECT  * FROM con_periodo WHERE ide_sucu=" + utilitario.getVariable("ide_sucu") + " and estado_cnper=true AND '" + fecha + "' BETWEEN fecha_inicio_cnper and fecha_fin_cnper ");
            if (tab_periodo.getTotalFilas() > 0) {
                return true;
            } else {
                utilitario.agregarMensajeError("No se puede generar el comprobante de Contabilidad", "El périodo de la fecha del comprobante ya fue cerrado");
                return false;
            }
        } else {
            return false;
        }
    }

    public String obtenerPeriodoActivo() {
        return obtenerParametroPeriodo("ide_cnper");
    }

    public String obtenerFechaInicialPeriodoActivo() {
        return obtenerParametroPeriodo("fecha_inicio_cnper");
    }

    public String obtenerFechaFinalPeriodoActivo() {
        return obtenerParametroPeriodo("fecha_fin_cnper");
    }

    public String obtenerParametroPeriodo(String parametro) {
        TablaGenerica tab_periodo = utilitario.consultar("SELECT  * FROM con_periodo WHERE estado_cnper=true and ide_sucu=" + utilitario.getVariable("ide_sucu"));
        if (tab_periodo.getTotalFilas() > 0) {
            return tab_periodo.getValor(0, parametro);
        } else {
            return null;
        }
    }

    public double obtenerSaldoInicialCuenta(String ideCuenta, String fecha) {
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
                + "and dpc.ide_cndpc=" + ideCuenta + " "
                + "GROUP BY dpc.ide_cndpc ";
        TablaGenerica tab_saldo = utilitario.consultar(sql);
        if (tab_saldo.getTotalFilas() > 0) {
            if (tab_saldo.getValor(0, "valor") != null) {
                saldo = Double.parseDouble(tab_saldo.getValor(0, "valor"));
            }
        }
        return saldo;
    }

    public double obtenerSaldoCuenta(boolean es_consolidado,String ideCuenta, String fecha) {
        //Retorna el saldo inicial de una cuenta segun el periodo activo
        String ide_cntcu = getParametroPlanCuentas(ideCuenta, "ide_cntcu");
        double saldo = 0;
        String sql_saldo_cuentas = getSqlSaldoCuentas(es_consolidado, obtenerFechaInicialPeriodoActivo(), fecha, ide_cntcu, false);
        TablaGenerica tab_salso_cuentas = llenarTablaResultadosGenerales(sql_saldo_cuentas, obtenerUltimoNivel());
        for (int i = 0; i < tab_salso_cuentas.getTotalFilas(); i++) {
            if (ideCuenta.equals(tab_salso_cuentas.getValor(i, "ide_cndpc"))) {
                if (tab_salso_cuentas.getValor(i, "valor") != null && !tab_salso_cuentas.getValor(i, "valor").isEmpty()) {
                    saldo = Double.parseDouble(tab_salso_cuentas.getValor(i, "valor"));
                    break;
                }
            }
        }
        System.out.println("ide_cndpc " + ideCuenta + " " + " saldo " + saldo);
        return saldo;
    }

    public String obtenerNivelTipoCuenta(String ideCuenta) {
        //  Retorna si una cuenta es de tipo PADRE O HIJO
        List lis_nivel = utilitario.getConexion().consultar("SELECT nivel_cndpc FROM con_det_plan_cuen where ide_cndpc=" + ideCuenta);
        if (lis_nivel != null && !lis_nivel.isEmpty()) {
            return lis_nivel.get(0) + "";
        }
        return "HIJO";
    }

    public String buscarIdeIdentificador(String identificador) {
        List lis_identificador = utilitario.getConexion().consultar("SELECT IDE_CNCCA FROM con_cab_conf_asie WHERE UPPER(nombre_cncca) LIKE '" + identificador.toUpperCase() + "'");
        if (lis_identificador != null && !lis_identificador.isEmpty()) {
            return lis_identificador.get(0) + "";
        }
        return null;
    }

    public String buscarCuenta(String identificador, String persona, String articulo, String impuesto, String porcentajeImpuesto, String transaccionInventario, String transaccionActivo) {
        String str_sql = "";
        String ide_identificador = buscarIdeIdentificador(identificador);
        if (ide_identificador != null) {
            str_sql = "select cn_d.ide_cndpc "
                    + "from con_vig_conf_asie cn_v, con_det_conf_asie cn_d "
                    + "where cn_v.ide_cnvca = cn_d.ide_cnvca "
                    + "and cn_v.ide_cncca =" + ide_identificador + " "
                    + "and cn_v.estado_cnvca=true ";
            if (persona != null) {
                str_sql += "and ide_geper=" + persona + " ";
            }
            if (articulo != null) {
                str_sql += "and ide_inarti=" + articulo + " ";
            }
            if (impuesto != null) {
                str_sql += "and ide_cncim=" + impuesto + " ";
            }
            if (porcentajeImpuesto != null) {
                str_sql += "and ide_cnpim=" + porcentajeImpuesto + " ";
            }
            if (transaccionInventario != null) {
                str_sql += "and ide_intti=" + transaccionInventario + " ";
            }
            if (transaccionActivo != null) {
                str_sql += "and ide_acttr=" + transaccionActivo + " ";
            }

            List lis_configura = utilitario.getConexion().consultar(str_sql);
            if (lis_configura != null && !lis_configura.isEmpty()) {
                //  return ltab_identificador.obtener_texto(0, "ide_cndpc");
                return lis_configura.get(0) + "";
            }
        } else {
            System.out.println("no existe identificador");
            utilitario.agregarMensajeError("No existe el identificador : " + identificador, " No existe un identificador con ese nombre para obtener la configuración para el asiento contable");
        }
        return null;
    }

    public String buscarParametro(String parametro, String identificador, String persona, String articulo, String impuesto, String porcentajeImpuesto, String transaccionInventario) {
        String str_sql = "";
        String ide_identificador = buscarIdeIdentificador(identificador);
        if (ide_identificador != null) {
            str_sql = "select " + parametro + " "
                    + "from con_vig_conf_asie , con_det_conf_asie  "
                    + "where con_vig_conf_asie.ide_cnvca = con_det_conf_asie.ide_cnvca "
                    + "and con_vig_conf_asie.ide_cncca =" + ide_identificador + " "
                    + "and con_vig_conf_asie.estado_cnvca=true ";
            if (persona != null) {
                str_sql += "and ide_geper=" + persona + " ";
            }
            if (articulo != null) {
                str_sql += "and ide_inarti=" + articulo + " ";
            }
            if (impuesto != null) {
                str_sql += "and ide_cncim=" + impuesto + " ";
            }
            if (porcentajeImpuesto != null) {
                str_sql += "and ide_cnpim=" + porcentajeImpuesto + " ";
            }
            if (transaccionInventario != null) {
                str_sql += "and ide_intti=" + transaccionInventario + " ";
            }

            List lis_configura = utilitario.getConexion().consultar(str_sql);
            if (lis_configura != null && !lis_configura.isEmpty()) {
                //  return ltab_identificador.obtener_texto(0, "ide_cndpc");
                return lis_configura.get(0) + "";
            }
        } else {
            utilitario.agregarMensajeError("No existe el identificador : " + identificador, " No existe un identificador con ese nombre para obtener la configuración para el asiento contable");
        }
        return null;
    }

    public String buscarParametroProducto(String parametro, String identificador, String producto) {
        String str_sql = "";
        String str_padre = null, str_cuenta = null;
        str_sql = "select ide_inarti, inv_ide_inarti from inv_articulo where ide_inarti=" + producto;
        TablaGenerica tab_identificador = utilitario.consultar(str_sql);
        if (tab_identificador.getTotalFilas() > 0) {
            str_cuenta = buscarParametro(parametro, identificador, null, producto, null, null, null);
            str_padre = tab_identificador.getValor(0, "inv_ide_inarti");
            if (str_cuenta == null) {
                if (str_padre == null) {
                    return null;
                } else {
                    return buscarParametroProducto(parametro, identificador, str_padre);
                }
            } else {
                return str_cuenta;
            }
        }
        return null;
    }

    public String buscarCuentaProducto(String identificador, String producto) {
        String str_sql = "";
        String str_padre = null, str_cuenta = null;
        str_sql = "select ide_inarti, inv_ide_inarti from inv_articulo where ide_inarti=" + producto;
        TablaGenerica tab_identificador = utilitario.consultar(str_sql);
        if (tab_identificador.getTotalFilas() > 0) {
            str_cuenta = buscarCuenta(identificador, null, producto, null, null, null, null);
            System.out.println("str_cuenta " + str_cuenta);
            str_padre = tab_identificador.getValor(0, "inv_ide_inarti");
            if (str_cuenta == null) {
                if (str_padre == null) {
                    return null;
                } else {
                    return buscarCuentaProducto(identificador, str_padre);
                }
            } else {
                return str_cuenta;
            }
        }
        return null;
    }

    public String buscarCuentaActivo(String identificador, String producto, String tipo_transaccion) {
        String str_sql = "";
        String str_padre = null, str_cuenta = null;
        str_sql = "select ide_inarti, inv_ide_inarti from inv_articulo where ide_inarti=" + producto;
        TablaGenerica tab_identificador = utilitario.consultar(str_sql);
        if (tab_identificador.getTotalFilas() > 0) {
            str_cuenta = buscarCuenta(identificador, null, producto, null, null, null, tipo_transaccion);
            str_padre = tab_identificador.getValor(0, "inv_ide_inarti");
            if (str_cuenta == null) {
                if (str_padre == null) {
                    return null;
                } else {
                    return buscarCuentaActivo(identificador, str_padre, tipo_transaccion);
                }
            } else {
                return str_cuenta;
            }
        }
        return null;
    }

    public String buscarCuentaPersona(String identificador, String persona) {
        String str_sql = "";
        String str_padre = null, str_cuenta = null;
        str_sql = "select ide_geper,gen_ide_geper from gen_persona where ide_geper=" + persona;
        TablaGenerica tab_identificador = utilitario.consultar(str_sql);
        if (tab_identificador.getTotalFilas() > 0) {
            str_cuenta = buscarCuenta(identificador, persona, null, null, null, null, null);
            str_padre = tab_identificador.getValor(0, "gen_ide_geper");
            if (str_cuenta == null) {
                if (str_padre == null) {
                    return null;
                } else {
                    return buscarCuentaPersona(identificador, str_padre);
                }

            } else {
                return str_cuenta;
            }
        }
        return null;
    }

    public void reversar(String ide_cnccc, String observacion, cls_contabilidad comprobante) {
        System.out.println("reversar asiento " + ide_cnccc);
        TablaGenerica tab_busca = utilitario.consultar("SELECT * FROM con_cab_comp_cont where ide_cnccc=" + ide_cnccc);
        TablaGenerica tab_detalles = utilitario.consultar("SELECT * FROM con_det_comp_cont where ide_cnccc=" + ide_cnccc);
        if (tab_busca.getTotalFilas() > 0) {
            utilitario.getConexion().agregarSqlPantalla("update con_cab_comp_cont set ide_cneco=" + utilitario.getVariable("p_con_estado_comprobante_anulado") + " where ide_cnccc=" + ide_cnccc);
            cls_cab_comp_cont cab_com_con;
            if (observacion != null) {
                cab_com_con = new cls_cab_comp_cont(tab_busca.getValor("ide_cntcm"), utilitario.getVariable("p_con_estado_comprobante_normal"), tab_busca.getValor("ide_modu"), tab_busca.getValor("ide_geper"), utilitario.getFechaActual(), "Reversa transaccion num: " + tab_busca.getValor("ide_cnccc") + "   obs. (" + tab_busca.getValor("observacion_cnccc") + " )");
            } else {
                cab_com_con = new cls_cab_comp_cont(tab_busca.getValor("ide_cntcm"), utilitario.getVariable("p_con_estado_comprobante_normal"), tab_busca.getValor("ide_modu"), tab_busca.getValor("ide_geper"), utilitario.getFechaActual(), "Reversa transaccion num: " + tab_busca.getValor("ide_cnccc") + "   obs. (" + tab_busca.getValor("observacion_cnccc") + " )");
            }
            List<cls_det_comp_cont> lista_detalles = new ArrayList();
            String lugar_aplica = "";
            for (int i = 0; i < tab_detalles.getTotalFilas(); i++) {
                if (tab_detalles.getValor(i, "ide_cnlap").equals(utilitario.getVariable("p_con_lugar_debe"))) {
                    lugar_aplica = utilitario.getVariable("p_con_lugar_haber");
                } else {
                    lugar_aplica = utilitario.getVariable("p_con_lugar_debe");
                }
                lista_detalles.add(new cls_det_comp_cont(lugar_aplica, tab_detalles.getValor(i, "ide_cndpc"), Double.parseDouble(tab_detalles.getValor(i, "valor_cndcc")), ""));
            }
            cab_com_con.setDetalles(lista_detalles);
            comprobante.generarAsientoContable(cab_com_con);
            String ide_cnccc1 = comprobante.getTab_cabecera().getValor("ide_cnccc");
            System.out.println("asiento rversa generado " + ide_cnccc1);
        }
    }

    public boolean anular(String ide_cnccc) {
        System.out.println("anular asiento " + ide_cnccc);
        TablaGenerica tab_busca = utilitario.consultar("SELECT * FROM con_cab_comp_cont where ide_cnccc=" + ide_cnccc);
        TablaGenerica tab_detalles = utilitario.consultar("SELECT * FROM con_det_comp_cont where ide_cnccc=" + ide_cnccc);
        if (tab_busca.getTotalFilas() > 0) {
            utilitario.getConexion().agregarSqlPantalla("update con_cab_comp_cont set ide_cneco=" + utilitario.getVariable("p_con_estado_comprobante_anulado") + " where ide_cnccc=" + ide_cnccc);
            utilitario.getConexion().agregarSqlPantalla("UPDATE con_det_comp_cont set valor_cndcc=0 where ide_cnccc=" + ide_cnccc);
            return true;
        }
        return false;
    }

    public void limpiar() {
        tab_cabecera.limpiar();
        tab_detalle.limpiar();
        ide_cnccc = -1;
        ide_cndcc = -1;
    }

    private String generarSecuencial(String fecha) {
        //GENERA el número secuencial de la cabecera del comprobante   
        String str_numero = null;
        String str_fecha = fecha;
        String str_ano = utilitario.getAnio(str_fecha) + "";
        String str_mes = utilitario.getMes(str_fecha) + "";
        String str_ide_sucu = utilitario.getVariable("ide_sucu");
        //SELECCIONA EL MAXIMO SEGUN EL MES Y EL AÑO 
        TablaGenerica tab_max = utilitario.consultar("SELECT count(NUMERO_CNCCC) as cod,max(cast( substr(NUMERO_CNCCC,8) as NUMERIC)) AS MAXIMO  FROM CON_CAB_COMP_CONT WHERE extract(year from FECHA_TRANS_CNCCC) ='" + str_ano + "' AND extract(month from FECHA_TRANS_CNCCC) ='" + str_mes + "' AND IDE_SUCU=" + str_ide_sucu+"");
       
        String str_maximo = "0";
        if (tab_max.getTotalFilas()>0) {
            str_maximo = tab_max.getValor("MAXIMO");
            if (str_maximo == null || str_maximo.isEmpty()) {
                str_maximo = "0";
            }
            long lon_siguiente=0;
            try {
             lon_siguiente= Long.parseLong(str_maximo) + 1;   
            } catch (Exception e) {
            }             
            str_maximo = lon_siguiente + "";
        }
        str_maximo = utilitario.generarCero(8 - str_maximo.length()) + str_maximo;
        str_numero = str_ano + str_mes + str_ide_sucu + str_maximo;
        System.out.println("num com contaxxxxx: " + str_numero);
        return str_numero;
    }

    private String obtenerPlanActivo(String parametro) {
        TablaGenerica tab_plan_activo = utilitario.consultar("select * from con_cab_plan_cuen where ide_empr=" + utilitario.getVariable("ide_empr") + " and activo_cncpc is TRUE");
        if (tab_plan_activo.getTotalFilas() > 0) {
            return tab_plan_activo.getValor(0, parametro);
        } else {
            return null;
        }
    }

    public String cerrarPeriodoContable(String ide_cnper, String fecha_inicio, String fecha_fin) {
        if (ide_cnper != null && !ide_cnper.isEmpty()
                && fecha_inicio !=null && !fecha_inicio.isEmpty()
                && fecha_fin !=null && !fecha_fin.isEmpty()) {
        int signo = 0;
        String fecha_inicio_nuevo_periodo = "";
        String fecha_fin_nuevo_periodo = "";
        fecha_inicio_nuevo_periodo = utilitario.getFormatoFecha(utilitario.sumarDiasFecha(utilitario.getFecha(fecha_fin), 1));
        fecha_fin_nuevo_periodo = utilitario.getFormatoFecha(utilitario.sumarDiasFecha(utilitario.getFecha(fecha_fin), 60));
        long ide_cnper_nuevo = utilitario.getConexion().getMaximo("con_periodo", "ide_cnper",1);
        if (utilitario.getConexion().ejecutarSql("insert into con_periodo values (" + ide_cnper_nuevo + "," + utilitario.getVariable("ide_empr") + "," + utilitario.getVariable("ide_sucu") + ",'Periodo Creado Automaticamente ','" + fecha_inicio_nuevo_periodo + "','" + fecha_fin_nuevo_periodo + "',true,false)").isEmpty()) {
            utilitario.getConexion().ejecutarSql("update con_periodo set estado_cnper=false where ide_cnper=" + ide_cnper);
        }
        return String.valueOf(ide_cnper_nuevo);
        } else {
            return null;
        }

    }

    public String generarAsientoAperturaCuentas(String fecha_inicio, String fecha_fin) {
        return generarEstadoSituacionInicial(fecha_inicio, fecha_fin);
    }

    private String generarEstadoSituacionInicial(String fecha_inicio, String fecha_fin) {
        //if (tab_periodo != null && tab_periodo.getTotalFilas() > 0) {
        List<cls_det_comp_cont> lista_detalles = new ArrayList();
        String str_valor_utilidad_perdida_anterior = "";
        String str_valor_patrimonio_anterior = "";
        double utilidad_perdida = 0;
        TablaGenerica tab_balance_general = new TablaGenerica();
        tab_balance_general = utilitario.consultar(getSqlBalanceGeneralHijos(false, fecha_inicio, fecha_fin));
//            Tabla tab_patrimonio_resultados = utilitario.consultar(getSqlParametrosCierrePeriodo(fecha_inicio, fecha_fin));
        for (int j = 0; j < tab_balance_general.getTotalFilas(); j++) {
            if (utilitario.getVariable("p_con_superavit_ejercicio_anterior").equals(tab_balance_general.getValor(j, "ide_cndpc"))) {
                str_valor_utilidad_perdida_anterior = tab_balance_general.getValor(j, "valor");
            }
            if (utilitario.getVariable("p_con_deficit_ejercicio_anterior").equals(tab_balance_general.getValor(j, "ide_cndpc"))) {
                str_valor_utilidad_perdida_anterior = tab_balance_general.getValor(j, "valor");
            }
            if (utilitario.getVariable("p_con_patrimonio").equals(tab_balance_general.getValor(j, "ide_cndpc"))) {
                str_valor_patrimonio_anterior = tab_balance_general.getValor(j, "valor");
            }
        }
        cls_cab_comp_cont cab_com_con = new cls_cab_comp_cont(utilitario.getVariable("p_con_tipo_comprobante_diario"), utilitario.getVariable("p_con_estado_comprobante_normal"), "0", "", obtenerFechaInicialPeriodoActivo(), "Asiento de Apertura de Cuentas");
        lista_detalles.clear();
        for (int i = 0; i < tab_balance_general.getTotalFilas(); i++) {
            if (!utilitario.getVariable("p_con_deficit_ejercicio_anterior").equals(tab_balance_general.getValor(i, "ide_cndpc"))) {
                if (!utilitario.getVariable("p_con_superavit_ejercicio_anterior").equals(tab_balance_general.getValor(i, "ide_cndpc"))) {
                    if (!utilitario.getVariable("p_con_patrimonio").equals(tab_balance_general.getValor(i, "ide_cndpc"))) {
                        if (tab_balance_general.getValor(i, "ide_cntcu").equalsIgnoreCase(utilitario.getVariable("p_con_tipo_cuenta_activo"))) {
                            lista_detalles.add(new cls_det_comp_cont(utilitario.getVariable("p_con_lugar_debe"), tab_balance_general.getValor(i, "ide_cndpc"), Double.parseDouble(tab_balance_general.getValor(i, "valor")), ""));
                        } else {
                            lista_detalles.add(new cls_det_comp_cont(utilitario.getVariable("p_con_lugar_haber"), tab_balance_general.getValor(i, "ide_cndpc"), Double.parseDouble(tab_balance_general.getValor(i, "valor")), ""));
                        }
                    }
                }
            }
        }
        double nuevo_patrimonio = 0;
        try {
            nuevo_patrimonio = Double.parseDouble(str_valor_patrimonio_anterior) + (Double.parseDouble(str_valor_utilidad_perdida_anterior));

        } catch (Exception e) {
        }
        List lis_totales = obtenerTotalesBalanceGeneral(false, fecha_inicio, fecha_fin);
        double tot_activo = Double.parseDouble(lis_totales.get(0) + "");
        double tot_pasivo = Double.parseDouble(lis_totales.get(1) + "");
        double tot_patrimonio = Double.parseDouble(lis_totales.get(2) + "");
        utilidad_perdida = tot_activo - tot_pasivo - tot_patrimonio;

        System.out.println("utilidad/perdida ejercicio anterior " + str_valor_utilidad_perdida_anterior);
        System.out.println("valor patrimonio anterior " + str_valor_patrimonio_anterior);
        System.out.println("utilidad/perdida " + utilidad_perdida);
        System.out.println("nuevo_patrimonio " + nuevo_patrimonio);
        if (utilidad_perdida > 0) {
            lista_detalles.add(new cls_det_comp_cont(utilitario.getVariable("p_con_lugar_haber"), utilitario.getVariable("p_con_superavit_ejercicio_anterior"), utilidad_perdida, ""));
        } else {
            lista_detalles.add(new cls_det_comp_cont(utilitario.getVariable("p_con_lugar_haber"), utilitario.getVariable("p_con_deficit_ejercicio_anterior"), utilidad_perdida, ""));
        }
        lista_detalles.add(new cls_det_comp_cont(utilitario.getVariable("p_con_lugar_haber"), utilitario.getVariable("p_con_patrimonio"), nuevo_patrimonio, ""));

        cab_com_con.setDetalles(lista_detalles);
        return generarAsientoContable(cab_com_con);
//        } else {
//            return null;
//        }

    }

    private String getSqlBalanceGeneralHijos(boolean es_consolidado, String fecha_inicio, String fecha_fin) {
        String p_activo = utilitario.getVariable("p_con_tipo_cuenta_activo");
        String p_pasivo = utilitario.getVariable("p_con_tipo_cuenta_pasivo");
        String p_patrimonio = utilitario.getVariable("p_con_tipo_cuenta_patrimonio");
        String p_tipo_cuentas = p_activo + "," + p_pasivo + "," + p_patrimonio;
        return getSqlSaldoCuentas(es_consolidado, fecha_inicio, fecha_fin, p_tipo_cuentas, true);
    }

    private String getSqlBalanceGeneral(boolean es_consolidado, String fecha_inicio, String fecha_fin) {
        String p_activo = utilitario.getVariable("p_con_tipo_cuenta_activo");
        String p_pasivo = utilitario.getVariable("p_con_tipo_cuenta_pasivo");
        String p_patrimonio = utilitario.getVariable("p_con_tipo_cuenta_patrimonio");
        String p_tipo_cuentas = p_activo + "," + p_pasivo + "," + p_patrimonio;
        return getSqlSaldoCuentas(es_consolidado, fecha_inicio, fecha_fin, p_tipo_cuentas, false);
    }

    private String getSqlEstadoResultados(boolean es_consolidado, String fecha_inicio, String fecha_fin) {
        String p_ingresos = utilitario.getVariable("p_con_tipo_cuenta_ingresos");
        String p_gastos = utilitario.getVariable("p_con_tipo_cuenta_gastos");
        String p_costos = utilitario.getVariable("p_con_tipo_cuenta_costos");
        String p_tipo_cuentas = p_ingresos + "," + p_costos + "," + p_gastos;
        return getSqlSaldoCuentas(es_consolidado, fecha_inicio, fecha_fin, p_tipo_cuentas, false);
    }

    private String getSqlSaldoCuentas(boolean es_consolidado, String fecha_inicio, String fecha_fin, String ide_cntcu, boolean solo_hijos) {
        String estado_normal = utilitario.getVariable("p_con_estado_comprobante_normal");
        String estado_inicial = utilitario.getVariable("p_con_estado_comp_inicial");
        String estado_final = utilitario.getVariable("p_con_estado_comp_final");
        String p_tipo_cuentas = ide_cntcu;
        String sql = "";
        if (solo_hijos) {
            sql = "select dpc.ide_cndpc,dpc.nombre_cndpc,dpc.ide_cnncu, "
                    + "sum(dcc.valor_cndcc*sc.signo_cnscu) as valor,"
                    + "dpc.codig_recur_cndpc,con_ide_cndpc,dpc.ide_cntcu from  "
                    + "con_cab_comp_cont ccc "
                    + "inner join  con_det_comp_cont dcc on ccc.ide_cnccc=dcc.ide_cnccc "
                    + "inner join con_det_plan_cuen dpc on  dpc.ide_cndpc = dcc.ide_cndpc "
                    + "inner join con_tipo_cuenta tc on dpc.ide_cntcu=tc.ide_cntcu "
                    + "inner  join con_signo_cuenta sc on tc.ide_cntcu=sc.ide_cntcu and dcc.ide_cnlap=sc.ide_cnlap "
                    + "WHERE (ccc.fecha_trans_cnccc BETWEEN '" + fecha_inicio + "' and '" + fecha_fin + "') "
                    + "and ccc.ide_cneco IN (" + estado_normal + "," + estado_inicial + "," + estado_final + ") ";
            if (!es_consolidado) {
                sql += "and ccc.ide_sucu=" + utilitario.getVariable("IDE_SUCU") + " ";
            }
            sql += "and dpc.ide_cntcu in (" + p_tipo_cuentas + ") "
                    + "GROUP BY dpc.ide_cndpc,codig_recur_cndpc, nombre_cndpc,con_ide_cndpc "
                    + "HAVING (sum(dcc.valor_cndcc*sc.signo_cnscu) <>0)";
        } else {
            sql = "(SELECT * FROM (select dpc.ide_cndpc,dpc.nombre_cndpc,dpc.ide_cnncu, "
                    + "sum(dcc.valor_cndcc*sc.signo_cnscu) as valor,"
                    + "dpc.codig_recur_cndpc,con_ide_cndpc,dpc.ide_cntcu from  "
                    + "con_cab_comp_cont ccc "
                    + "inner join  con_det_comp_cont dcc on ccc.ide_cnccc=dcc.ide_cnccc "
                    + "inner join con_det_plan_cuen dpc on  dpc.ide_cndpc = dcc.ide_cndpc "
                    + "inner join con_tipo_cuenta tc on dpc.ide_cntcu=tc.ide_cntcu "
                    + "inner  join con_signo_cuenta sc on tc.ide_cntcu=sc.ide_cntcu and dcc.ide_cnlap=sc.ide_cnlap "
                    + "WHERE (ccc.fecha_trans_cnccc BETWEEN '" + fecha_inicio + "' and '" + fecha_fin + "') "
                    + "and ccc.ide_cneco IN (" + estado_normal + "," + estado_inicial + "," + estado_final + ") ";
            if (!es_consolidado) {
                sql += "and ccc.ide_sucu=" + utilitario.getVariable("IDE_SUCU") + " ";
            }
            sql += "and dpc.ide_cntcu in (" + p_tipo_cuentas + ") "
                    + " GROUP BY dpc.ide_cndpc,codig_recur_cndpc, nombre_cndpc,con_ide_cndpc "
                    + "HAVING (sum(dcc.valor_cndcc*sc.signo_cnscu) <>0) ) as C1 "
                    + "UNION SELECT * FROM(Select ide_cndpc,nombre_cndpc,ide_cnncu , '0' as valor, "
                    + "codig_recur_cndpc,con_ide_cndpc,ide_cntcu "
                    + "from  con_det_plan_cuen where ide_cntcu in (" + p_tipo_cuentas + ") ";
            if (!es_consolidado) {
                sql += "and ide_cndpc not in(select ide_cndpc from con_det_comp_cont where "
                        + "ide_sucu=" + utilitario.getVariable("IDE_SUCU") + ") ";
            }
            sql += "and nivel_cndpc='PADRE' ) AS C2) "
                    + "order by codig_recur_cndpc ";
        }
        System.out.println("sql balance general "+sql);
        return sql;
        
    }

    private String getSqlEstadoResultadosHijos(boolean es_consolidado, String fecha_inicio, String fecha_fin) {
        String p_ingresos = utilitario.getVariable("p_con_tipo_cuenta_ingresos");
        String p_gastos = utilitario.getVariable("p_con_tipo_cuenta_gastos");
        String p_costos = utilitario.getVariable("p_con_tipo_cuenta_costos");
        String p_tipo_cuentas = p_ingresos + "," + p_gastos + "," + p_costos;
        return getSqlSaldoCuentas(es_consolidado, fecha_inicio, fecha_fin, p_tipo_cuentas, true);
    }

    private TablaGenerica getTablaResultadosGenerales(boolean es_balance, boolean es_consolidado, String fecha_inicio, String fecha_fin, int nivel_tope) {
        String sql;
        if (es_balance) {
            sql = getSqlBalanceGeneral(es_consolidado, fecha_inicio, fecha_fin);
        } else {
            sql = getSqlEstadoResultados(es_consolidado, fecha_inicio, fecha_fin);
        }
        return llenarTablaResultadosGenerales(sql, nivel_tope);
    }

    private TablaGenerica llenarTablaResultadosGenerales(String sql, int nivel_tope) {
        TablaGenerica tab_saldo = utilitario.consultar(sql);
        List lis_padres = new ArrayList();
        List lis_valor_padre = new ArrayList();
        double valor_acu = 0;
        int nivel = obtenerUltimoNivel();
        String padre;
        int band = 0;
        do {
            for (int i = 0; i < tab_saldo.getTotalFilas(); i++) {
                if (tab_saldo.getValor(i, "ide_cnncu").equals(String.valueOf(nivel))) {
                    padre = tab_saldo.getValor(i, "con_ide_cndpc");
                    for (int k = 0; k < lis_padres.size(); k++) {
                        if (padre != null && !padre.isEmpty()) {
                            if (lis_padres.get(k).equals(padre)) {
                                band = 1;
                                break;
                            }
                        }
                    }
                    if (band == 0) {
                        if (padre != null && !padre.isEmpty()) {
                            lis_padres.add(padre);
                        }
                        for (int j = 0; j < tab_saldo.getTotalFilas(); j++) {
                            if (padre != null && !padre.isEmpty() && tab_saldo.getValor(j, "con_ide_cndpc") != null && !tab_saldo.getValor(j, "con_ide_cndpc").isEmpty()) {
                                if (padre.equals(tab_saldo.getValor(j, "con_ide_cndpc"))) {
                                    try {
                                        valor_acu = valor_acu + Double.parseDouble(tab_saldo.getValor(j, "valor"));
                                    } catch (Exception e) {
                                    }
                                }
                            }
                        }
                        lis_valor_padre.add(valor_acu);
                        valor_acu = 0;
                    } else {
                        band = 0;
                    }
                }
            }

            for (int i = 0; i < lis_padres.size(); i++) {
                padre = lis_padres.get(i).toString();
                for (int j = 0; j < tab_saldo.getTotalFilas(); j++) {
                    if (tab_saldo.getValor(j, "ide_cndpc").equals(padre)) {
                        try {
                            tab_saldo.setValor(j, "valor", lis_valor_padre.get(i).toString());
                        } catch (Exception e) {
                        }
                    }
                }
            }
            nivel = nivel - 1;
        } while (nivel >= 2);
        // se llena la tabla con los valores hasta el nivel tope enviado 
        String espacio = "";
        for (int i = 0; i < tab_saldo.getTotalFilas(); i++) {
            try {
                if (Integer.parseInt(tab_saldo.getValor(i, "ide_cnncu")) > nivel_tope) {
                    tab_saldo.setValor(i, "valor", "0");
                } else {
                    if (Integer.parseInt(tab_saldo.getValor(i, "ide_cnncu")) > 1) {
                        for (int j = 0; j < (Integer.parseInt(tab_saldo.getValor(i, "ide_cnncu")) - 1) * 6; j++) {
                            espacio += " ";
                        }
                        tab_saldo.setValor(i, "nombre_cndpc", espacio + tab_saldo.getValor(i, "nombre_cndpc"));
                    } else {
                        tab_saldo.setValor(i, "nombre_cndpc", espacio + tab_saldo.getValor(i, "nombre_cndpc"));
                    }
                }
                espacio = "";
            } catch (Exception e) {
            }

        }
        return tab_saldo;
    }

    public TablaGenerica generarBalanceGeneral(boolean es_consolidado, String fecha_inicio, String fecha_final, int nivel_tope) {
        if (fecha_inicio != null) {
            if (fecha_final != null) {
                return getTablaResultadosGenerales(true, es_consolidado, fecha_inicio, fecha_final, nivel_tope);
            } else {
                utilitario.agregarMensajeInfo("No se puede generar el Reporte", "La fecha fin es nulla");
                return null;
            }
        } else {
            utilitario.agregarMensajeInfo("No se puede generar el Reporte", "La fecha del Periodo Activo ya termino");
            return null;
        }
    }

    public List obtenerTotalesEstadoResultados(boolean es_consolidado, String fecha_inicial, String fecha_final) {
        String p_ingresos = utilitario.getVariable("p_con_tipo_cuenta_ingresos");
        String p_gastos = utilitario.getVariable("p_con_tipo_cuenta_gastos");
        String p_costos = utilitario.getVariable("p_con_tipo_cuenta_costos");
        String estado_normal = utilitario.getVariable("p_con_estado_comprobante_normal");
        String estado_inicial = utilitario.getVariable("p_con_estado_comp_inicial");
        String estado_final = utilitario.getVariable("p_con_estado_comp_final");

        List lis_totales_est_resul = new ArrayList();
        String sql = "select dpc.ide_cndpc,dpc.nombre_cndpc,dpc.ide_cnncu , "
                + "sum(dcc.valor_cndcc*sc.signo_cnscu) as valor, "
                + "dpc.codig_recur_cndpc,con_ide_cndpc,dpc.ide_cntcu from  "
                + "con_cab_comp_cont ccc "
                + "inner join  con_det_comp_cont dcc on ccc.ide_cnccc=dcc.ide_cnccc "
                + "inner join con_det_plan_cuen dpc on  dpc.ide_cndpc = dcc.ide_cndpc "
                + "inner join con_tipo_cuenta tc on dpc.ide_cntcu=tc.ide_cntcu "
                + "inner  join con_signo_cuenta sc on tc.ide_cntcu=sc.ide_cntcu and dcc.ide_cnlap=sc.ide_cnlap "
                + "WHERE (ccc.fecha_trans_cnccc BETWEEN '" + fecha_inicial + "' and '" + fecha_final + "') "
                + "and ccc.ide_cneco IN (" + estado_normal + "," + estado_inicial + "," + estado_final + ") ";
        if (!es_consolidado) {
            sql += "and ccc.ide_sucu=" + utilitario.getVariable("ide_sucu") + " ";
        }
        sql += "and tc.ide_cntcu in (" + p_gastos + "," + p_ingresos + "," + p_costos + ") "
                + "GROUP BY dpc.ide_cndpc,codig_recur_cndpc, nombre_cndpc,con_ide_cndpc "
                + "HAVING (sum(dcc.valor_cndcc*sc.signo_cnscu) <>0)";
        TablaGenerica tab_estado = utilitario.consultar(sql);
        double tot_ingresos = 0;
        double tot_gastos = 0;
        double tot_costos = 0;
        for (int i = 0; i < tab_estado.getTotalFilas(); i++) {
            if (tab_estado.getValor(i, "ide_cntcu").equals(p_ingresos)) {
                tot_ingresos = tot_ingresos + Double.parseDouble(tab_estado.getValor(i, "valor"));
            }
            if (tab_estado.getValor(i, "ide_cntcu").equals(p_gastos)) {
                tot_gastos = tot_gastos + Double.parseDouble(tab_estado.getValor(i, "valor"));
            }
            if (tab_estado.getValor(i, "ide_cntcu").equals(p_costos)) {
                tot_costos = tot_costos + Double.parseDouble(tab_estado.getValor(i, "valor"));
            }
        }
        // en el indice 0 contiene el total de ingresos
        // en el indice 1 contiene el total de gastos
        // en el indice 2 contiene el total de costos
        lis_totales_est_resul.add(tot_ingresos);
        lis_totales_est_resul.add(tot_gastos);
        lis_totales_est_resul.add(tot_costos);
        System.out.println("ingresos " + tot_ingresos + " gastos " + tot_gastos + " costos " + tot_costos);
        return lis_totales_est_resul;

    }

    public List obtenerTotalesBalanceGeneral(boolean es_consolidado, String fecha_inicial, String fecha_final) {
        List lis_totales_bal_general = new ArrayList();
        String p_activo = utilitario.getVariable("p_con_tipo_cuenta_activo");
        String p_pasivo = utilitario.getVariable("p_con_tipo_cuenta_pasivo");
        String p_patrimonio = utilitario.getVariable("p_con_tipo_cuenta_patrimonio");
        String estado_normal = utilitario.getVariable("p_con_estado_comprobante_normal");
        String estado_inicial = utilitario.getVariable("p_con_estado_comp_inicial");
        String estado_final = utilitario.getVariable("p_con_estado_comp_final");
        String sql = "select dpc.ide_cndpc,dpc.nombre_cndpc,dpc.ide_cnncu , "
                + "sum(dcc.valor_cndcc*sc.signo_cnscu) as valor, "
                + "dpc.codig_recur_cndpc,con_ide_cndpc,dpc.ide_cntcu from "
                + "con_cab_comp_cont ccc "
                + "inner join  con_det_comp_cont dcc on ccc.ide_cnccc=dcc.ide_cnccc "
                + "inner join con_det_plan_cuen dpc on  dpc.ide_cndpc = dcc.ide_cndpc "
                + "inner join con_tipo_cuenta tc on dpc.ide_cntcu=tc.ide_cntcu "
                + "inner  join con_signo_cuenta sc on tc.ide_cntcu=sc.ide_cntcu and dcc.ide_cnlap=sc.ide_cnlap "
                + "WHERE (ccc.fecha_trans_cnccc BETWEEN '" + fecha_inicial + "' and '" + fecha_final + "') ";
        if (!es_consolidado) {
            sql += "and ccc.ide_sucu=" + utilitario.getVariable("ide_sucu") + " ";
        }
        sql += "and ccc.ide_cneco in (" + estado_normal + "," + estado_inicial + "," + estado_final + ") "
                //+ "and dcc.ide_cndpc!= 1497 "
                + "and dpc.ide_cntcu in (" + p_activo + "," + p_pasivo + "," + p_patrimonio + ") "
                + "GROUP BY dpc.ide_cndpc,codig_recur_cndpc, nombre_cndpc,con_ide_cndpc "
                + "HAVING (sum(dcc.valor_cndcc*sc.signo_cnscu) <>0) ";
        TablaGenerica tab_balance = utilitario.consultar(sql);
        double tot_activo = 0;
        double tot_pasivo = 0;
        double tot_patrimonio = 0;
        for (int i = 0; i < tab_balance.getTotalFilas(); i++) {

            if (tab_balance.getValor(i, "ide_cntcu").equals(p_activo)) {
                tot_activo = tot_activo + Double.parseDouble(tab_balance.getValor(i, "valor"));
            }
            if (tab_balance.getValor(i, "ide_cntcu").equals(p_pasivo)) {
                tot_pasivo = tot_pasivo + Double.parseDouble(tab_balance.getValor(i, "valor"));
            }
            if (tab_balance.getValor(i, "ide_cntcu").equals(p_patrimonio)) {
                tot_patrimonio = tot_patrimonio + Double.parseDouble(tab_balance.getValor(i, "valor"));
            }
        }

        // en el indice 0 contiene el total de activo
        // en el indice 1 contiene el total de pasivo
        // en el indice 2 contiene el total de patrimonio
        tot_activo = Double.parseDouble(utilitario.getFormatoNumero(tot_activo));
        tot_pasivo = Double.parseDouble(utilitario.getFormatoNumero(tot_pasivo));
        tot_patrimonio = Double.parseDouble(utilitario.getFormatoNumero(tot_patrimonio));
        lis_totales_bal_general.add(tot_activo);
        lis_totales_bal_general.add(tot_pasivo);
        lis_totales_bal_general.add(tot_patrimonio);
        return lis_totales_bal_general;
    }

    public TablaGenerica generarEstadoResultados(boolean es_consolidado, String fecha_inicio, String fecha_final, int nivel_tope) {
        if (fecha_inicio != null) {
            if (fecha_final != null) {
                return getTablaResultadosGenerales(false, es_consolidado, fecha_inicio, fecha_final, nivel_tope);
            } else {
                utilitario.agregarMensajeInfo("No se puede generar el Reporte", "La fecha fin no existe");
                return null;
            }
        } else {
            utilitario.agregarMensajeInfo("No se puede generar el Reporte", "La fecha del Periodo Activo ya termino");
            return null;
        }
    }

    public TablaGenerica generarBalanceComprobacion(String fecha_inicial, String fecha_final) {
        String estado_normal = utilitario.getVariable("p_con_estado_comprobante_normal");
        String estado_inicial = utilitario.getVariable("p_con_estado_comp_inicial");
        String estado_final = utilitario.getVariable("p_con_estado_comp_final");
        String sql = "SELECT CUENTA.nombre_cndpc,CUENTA.codig_recur_cndpc,LUGAR.nombre_cnlap, "
                + "CUENTA.ide_cndpc,LUGAR.ide_cnlap,sum(DETA.valor_cndcc) as valor "
                + "FROM con_cab_comp_cont CAB, "
                + "con_det_plan_cuen CUENTA, "
                + "con_det_comp_cont DETA, "
                + "con_lugar_aplicac LUGAR, "
                + "con_tipo_comproba TIPOCOMPRO "
                + "WHERE  DETA.ide_cndpc=CUENTA.ide_cndpc and  "
                + "DETA.ide_cnccc=CAB.ide_cnccc and  "
                + "LUGAR.ide_cnlap=DETA.ide_cnlap and "
                + "TIPOCOMPRO.ide_cntcm=CAB.ide_cntcm  and "
                + "CAB.fecha_trans_cnccc BETWEEN '" + fecha_inicial + "' and '" + fecha_final + "' and "
                + "CAB.ide_cneco IN (" + estado_normal + "," + estado_inicial + "," + estado_final + ") and "
                + "CAB.ide_sucu=" + utilitario.getVariable("ide_sucu") + ""
                + "GROUP BY DETA.ide_cndpc,CUENTA.nombre_cndpc,CUENTA.codig_recur_cndpc,LUGAR.nombre_cnlap,CUENTA.ide_cndpc,LUGAR.ide_cnlap "
                + "ORDER BY CUENTA.codig_recur_cndpc desc";
        TablaGenerica tab_total = utilitario.consultar(sql);


        String sql1 = "SELECT '' as cuenta, '' as codigo,'' as debe, ''as haber,'' as deudor, ''as acreedor";
        TablaGenerica tab_balance = utilitario.consultar(sql1);
        String cuenta_anterior = "";

        List lis_cuenta_repe = new ArrayList();
        double debe = 0;
        double haber = 0;
        int band = 0;
        for (int i = 0; i < tab_total.getTotalFilas(); i++) {
            if (i == (tab_total.getTotalFilas() - 1)) {
                cuenta_anterior = "";
            } else {
                cuenta_anterior = tab_total.getValor(i + 1, "ide_cndpc");
            }
            if (tab_total.getValor(i, "ide_cndpc").equals(cuenta_anterior)) {
                for (int j = 0; j < lis_cuenta_repe.size(); j++) {
                    if (lis_cuenta_repe.get(j).equals(tab_total.getValor(i, "ide_cndpc"))) {
                        band = 1;
                        break;
                    }
                }
                lis_cuenta_repe.add(tab_total.getValor(i, "ide_cndpc"));
                if (band == 0) {
                    tab_balance.insertar();
                    tab_balance.setValor("cuenta", tab_total.getValor(i, "nombre_cndpc"));
                    tab_balance.setValor("codigo", tab_total.getValor(i, "codig_recur_cndpc"));
                    if (tab_total.getValor(i, "ide_cnlap").equals(utilitario.getVariable("p_con_lugar_debe"))) {
                        tab_balance.setValor("debe", utilitario.getFormatoNumero(tab_total.getValor(i, "valor")));
                        debe = Double.parseDouble(tab_total.getValor(i, "valor"));
                        tab_balance.setValor("haber", utilitario.getFormatoNumero(tab_total.getValor(i + 1, "valor")));
                        haber = Double.parseDouble(tab_total.getValor(i + 1, "valor"));
                    }
                    if (tab_total.getValor(i, "ide_cnlap").equals(utilitario.getVariable("p_con_lugar_haber"))) {
                        tab_balance.setValor("debe", utilitario.getFormatoNumero(tab_total.getValor(i + 1, "valor")));
                        debe = Double.parseDouble(tab_total.getValor(i + 1, "valor"));
                        tab_balance.setValor("haber", utilitario.getFormatoNumero(tab_total.getValor(i, "valor")));
                        haber = Double.parseDouble(tab_total.getValor(i, "valor"));
                    }
                    if (debe >= haber) {
                        tab_balance.setValor("deudor", utilitario.getFormatoNumero((debe - haber)) + "");
                        tab_balance.setValor("acreedor", "0");
                    } else {
                        tab_balance.setValor("deudor", "0");
                        tab_balance.setValor("acreedor", utilitario.getFormatoNumero((haber - debe)) + "");
                    }
                } else {
                    band = 0;
                }
            } else {
                for (int j = 0; j < lis_cuenta_repe.size(); j++) {
                    if (lis_cuenta_repe.get(j).equals(tab_total.getValor(i, "ide_cndpc"))) {
                        band = 1;
                    }
                }
                if (band == 0) {
                    tab_balance.insertar();
                    tab_balance.setValor("cuenta", tab_total.getValor(i, "nombre_cndpc"));
                    tab_balance.setValor("codigo", tab_total.getValor(i, "codig_recur_cndpc"));
                    if (tab_total.getValor(i, "ide_cnlap").equals(utilitario.getVariable("p_con_lugar_debe"))) {
                        tab_balance.setValor("debe",utilitario.getFormatoNumero( tab_total.getValor(i, "valor")));
                        tab_balance.setValor("haber", utilitario.getFormatoNumero("0"));
                        debe = Double.parseDouble(tab_total.getValor(i, "valor"));
                        haber = 0;
                    }
                    if (tab_total.getValor(i, "ide_cnlap").equals(utilitario.getVariable("p_con_lugar_haber"))) {
                        tab_balance.setValor("haber", utilitario.getFormatoNumero(tab_total.getValor(i, "valor")));
                        tab_balance.setValor("debe", utilitario.getFormatoNumero("0"));
                        debe = 0;
                        haber = Double.parseDouble(tab_total.getValor(i, "valor"));
                    }
                    if (debe >= haber) {
                        tab_balance.setValor("deudor", utilitario.getFormatoNumero((debe - haber)) + "");
                        tab_balance.setValor("acreedor", utilitario.getFormatoNumero("0"));
                    } else {
                        tab_balance.setValor("deudor", utilitario.getFormatoNumero("0"));
                        tab_balance.setValor("acreedor", utilitario.getFormatoNumero((haber - debe)) + "");
                    }
                } else {
                    band = 0;
                }
            }
        }
        return tab_balance;
    }

    public String obtenerParametroPersona(String parametro, String ide_geper) {
        if (ide_geper != null && !ide_geper.isEmpty()) {
            TablaGenerica tab_persona = utilitario.consultar("select * from gen_persona where ide_empr=" + utilitario.getVariable("ide_empr") + " and ide_geper=" + ide_geper);
            if (tab_persona.getTotalFilas() > 0) {
                return tab_persona.getValor(parametro);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public int obtenerUltimoNivel() {
        String ide_cncpc = obtenerPlanActivo("ide_cncpc");
        List lis_nivel_max = utilitario.getConexion().consultar("select max (ide_cnncu) from con_det_plan_cuen dpc where ide_empr=" + utilitario.getVariable("ide_empr") + " and ide_cncpc=" + ide_cncpc);
        if (lis_nivel_max != null) {
            return Integer.parseInt(lis_nivel_max.get(0).toString());
        } else {
            return -1;
        }
    }

    public String obtenerNumDiasFormaPago(String ide_cndfp) {
        List sql_forma_pago = utilitario.getConexion().consultar("select dias_cndfp from con_deta_forma_pago where ide_cndfp=" + ide_cndfp);
        if (sql_forma_pago != null && !sql_forma_pago.isEmpty()) {
            return sql_forma_pago.get(0).toString();
        } else {
            return null;
        }
    }

    public String getFechaInicialPeriodo(String fecha) {
        if (fecha != null && !fecha.isEmpty()) {
            TablaGenerica tab_periodo = utilitario.consultar("select * from con_periodo "
                    + "where '" + fecha + "' BETWEEN fecha_inicio_cnper and fecha_fin_cnper "
                    + "and ide_sucu=" + utilitario.getVariable("ide_sucu") + " ");
            if (tab_periodo.getTotalFilas() > 0) {
                return tab_periodo.getValor(0, "fecha_inicio_cnper");
            } else {
                utilitario.agregarMensajeError("No se puede generar el Reporte", "No existe fecha de cierre de Periodo");
                return null;
            }
        } else {
            return null;
        }
    }

    public Tabla getTab_cabecera() {
        return tab_cabecera;
    }

    public void setTab_cabecera(Tabla tab_cabecera) {
        this.tab_cabecera = tab_cabecera;
    }

    public Tabla getTab_detalle() {
        return tab_detalle;
    }

    public void setTab_detalle(Tabla tab_detalle) {
        this.tab_detalle = tab_detalle;
    }
}
