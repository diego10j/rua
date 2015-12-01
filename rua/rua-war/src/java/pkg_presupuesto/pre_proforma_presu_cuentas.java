/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg_presupuesto;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Boton;
import framework.componentes.Combo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionCalendario;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import framework.componentes.Tabulador;
import framework.componentes.VisualizarPDF;
import framework.reportes.ReporteDataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.primefaces.event.SelectEvent;
import pkg_contabilidad.cls_contabilidad;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author Diego P
 */
public class pre_proforma_presu_cuentas extends Pantalla {

    Tabla tab_tabla1 = new Tabla();
    Tabla tab_tabla2 = new Tabla();
    Tabla tab_tabla3 = new Tabla();
    private Division div_division = new Division();
    private Reporte rep_reporte = new Reporte();
    private SeleccionFormatoReporte sel_rep = new SeleccionFormatoReporte();
    private SeleccionTabla sel_tab_periodo = new SeleccionTabla();
    private SeleccionTabla sel_tab = new SeleccionTabla();
    private SeleccionTabla sel_tab_periodo_cabecera = new SeleccionTabla();
    private Combo com_periodo_presupuesto = new Combo();
    private Tabulador tab_tabulador = new Tabulador();
    private SeleccionCalendario sec_calendario = new SeleccionCalendario();
    private SeleccionCalendario sec_rango_reporte = new SeleccionCalendario();
    private Boton bot_ver_asiento = new Boton();
    private VisualizarPDF vpdf_ver = new VisualizarPDF();
    private SeleccionCalendario sec_rango_reporte_eje = new SeleccionCalendario();
    //Parametros del sistema    
    //Objetos 
    cls_contabilidad conta = new cls_contabilidad();

    public pre_proforma_presu_cuentas() {
        List lis_plan = utilitario.getConexion().consultar("select ide_CNCPC from con_cab_plan_cuen where activo_cncpc=true");
        if (lis_plan != null && !lis_plan.isEmpty()) {
            bar_botones.agregarReporte();
            tab_tabulador.setId("tab_tabulador");
            com_periodo_presupuesto.setId("com_periodo_presupuesto");
            Etiqueta eti1 = new Etiqueta();
            eti1.setValue("Periodo Presupuesto :");
            bar_botones.agregarComponente(eti1);
            com_periodo_presupuesto.setId("com_periodo_presupuesto");
            com_periodo_presupuesto.setCombo("select ide_prppr,nombre_prppr from pre_periodo_presu where ide_empr=" + utilitario.getVariable("ide_empr"));
            com_periodo_presupuesto.setMetodo("seleccionar_periodo_presupuesto");
            bar_botones.agregarComponente(com_periodo_presupuesto);
            bar_botones.agregarCalendario();

            rep_reporte.setId("rep_reporte");
            rep_reporte.getBot_aceptar().setMetodo("aceptarReporte");
            sel_rep.setId("sel_rep");


            tab_tabla1.setId("tab_tabla1");
            tab_tabla1.setTabla("pre_cab_presu_cuentas", "ide_prcpc", 1);
            tab_tabla1.onSelect("seleccionar_tabla1");
            tab_tabla1.agregarRelacion(tab_tabla2);
            tab_tabla1.agregarRelacion(tab_tabla3);
            tab_tabla1.getColumna("ide_prppr").setCombo("pre_periodo_presu", "ide_prppr", "nombre_prppr", "");
            tab_tabla1.getColumna("ide_prppr").setVisible(false);
            tab_tabla1.getColumna("ide_usua").setCombo("sis_usuario", "ide_usua", "nom_usua", "");
            tab_tabla1.getColumna("ide_usua").setValorDefecto(utilitario.getVariable("ide_usua"));
            tab_tabla1.getColumna("ide_usua").setLectura(true);
            tab_tabla1.getColumna("ide_prepp").setCombo("pre_estado_plan_presu", "ide_prepp", "nombre_prepp", "");
            tab_tabla1.getColumna("ide_prepp").setVisible(false);
            tab_tabla1.getColumna("ide_prepp").setValorDefecto(utilitario.getVariable("ide_prepp"));
            tab_tabla1.getColumna("ide_prepp").setLectura(true);
            tab_tabla1.getColumna("fecha_trans_prcpc").setValorDefecto(utilitario.getFechaActual());
            tab_tabla1.getColumna("fecha_prcpc").setValorDefecto(utilitario.getFechaActual());
            tab_tabla1.getColumna("hora_prcpc").setValorDefecto(utilitario.getHoraActual());
            tab_tabla1.getColumna("fecha_prcpc").setVisible(false);
            tab_tabla1.getColumna("hora_prcpc").setVisible(false);
            tab_tabla1.setCondicion("ide_prcpc=-1");

            tab_tabla1.dibujar();
            PanelTabla pat_panel = new PanelTabla();
            pat_panel.setPanelTabla(tab_tabla1);

            tab_tabla2.setId("tab_tabla2");
            tab_tabla2.setTabla("pre_deta_presu_cuentas", "ide_prdpc", 2);
            tab_tabla2.getColumna("ide_prtpr").setValorDefecto(utilitario.getVariable("p_pre_tipo_presu_ingreso"));
            tab_tabla2.getColumna("ide_cndpc").setCombo("con_det_plan_cuen", "ide_cndpc", "codig_recur_cndpc,nombre_cndpc", "ide_cncpc=" + lis_plan.get(0) + " and ide_cntcu in(" + utilitario.getVariable("p_con_tipo_cuenta_ingresos") + ")");
            tab_tabla2.getColumna("ide_cndpc").setAutoCompletar();
            tab_tabla2.getColumna("variacion_prdpc").setEstilo("font-size: 13px;color: black;font-weight: bold");
            tab_tabla2.setCondicion("ide_prtpr=" + utilitario.getVariable("p_pre_tipo_presu_ingreso"));
            tab_tabla2.getColumna("ide_prtpr").setVisible(false);
            tab_tabla2.setIdCompleto("tab_tabulador:tab_tabla2");
            tab_tabla2.dibujar();
            PanelTabla pat_panel2 = new PanelTabla();
            pat_panel2.setPanelTabla(tab_tabla2);

            //tabla de gastos de cuentas
            tab_tabla3.setId("tab_tabla3");
            tab_tabla3.setSql("select ide_prdpc as ide_gasto,ide_prcpc,ide_cndpc,nombre_prdpc,valor_prdpc,observacion_prdpc,ejecutado_prdpc,porcentaje_prdpc,variacion_prdpc "
                    + "from pre_deta_presu_cuentas WHERE ide_empr=" + utilitario.getVariable("ide_empr") + " and ide_sucu=" + utilitario.getVariable("ide_sucu") + " and ide_prdpc=-1 and ide_prtpr=" + utilitario.getVariable("p_pre_tipo_presu_gasto"));
            tab_tabla3.setCampoPrimaria("ide_gasto");
            tab_tabla3.getColumna("ide_cndpc").setCombo("con_det_plan_cuen", "ide_cndpc", "codig_recur_cndpc,nombre_cndpc", "ide_cncpc=" + lis_plan.get(0) + " and ide_cntcu in(" + utilitario.getVariable("p_con_tipo_cuenta_gastos") + ")");
            tab_tabla3.getColumna("ide_cndpc").setAutoCompletar();
            tab_tabla3.getColumna("variacion_prdpc").setEstilo("font-size: 13px;color: black;font-weight: bold");
            tab_tabla3.setIdCompleto("tab_tabulador:tab_tabla3");
            tab_tabla3.dibujar();
            PanelTabla pat_panel3 = new PanelTabla();
            pat_panel3.setPanelTabla(tab_tabla3);

            tab_tabulador.agregarTab("INGRESOS", pat_panel2);
            tab_tabulador.agregarTab("GASTOS", pat_panel3);
            div_division.setId("div_division");



            sec_calendario.setId("sec_calendario");
            //por defecto friltra un mes
            sec_calendario.setMultiple(false);
            sec_calendario.setFecha1(utilitario.getDate());
            //sec_calendario.setFecha1(utilitario.sumarDiasFecha(utilitario.getDate(), -31));
            //sec_calendario.setFecha2(utilitario.getDate());
            gru_pantalla.getChildren().add(sec_calendario);
            sec_calendario.getBot_aceptar().setMetodo("aceptarRango");


            sel_tab_periodo.setId("sel_tab_periodo");
            sel_tab_periodo.setSeleccionTabla("SELECT ide_prppr,nombre_prppr FROM pre_periodo_presu WHERE ide_empr=" + utilitario.getVariable("ide_empr"), "ide_prppr");
            sel_tab_periodo.getTab_seleccion().getColumna("nombre_prppr").setFiltro(true);
            gru_pantalla.getChildren().add(sel_tab_periodo);


            sel_tab_periodo.getBot_aceptar().setMetodo("aceptarReporte");


            sel_tab_periodo_cabecera.setId("sel_tab_periodo_cabecera");
            sel_tab_periodo_cabecera.setSeleccionTabla("SELECT ide_prppr,nombre_prppr FROM pre_periodo_presu WHERE ide_empr=" + utilitario.getVariable("ide_empr"), "ide_prppr");
            gru_pantalla.getChildren().add(sel_tab_periodo_cabecera);
            sel_tab_periodo_cabecera.setRadio();
            sel_tab_periodo_cabecera.getBot_aceptar().setMetodo("aceptarReporte");

            bot_ver_asiento.setValue("Ver Ejecución Presupuestaria");
            bot_ver_asiento.setMetodo("verEjecucionPresupuestaria");
            bar_botones.agregarBoton(bot_ver_asiento);

            vpdf_ver.setId("vpdf_ver");
            vpdf_ver.setTitle("Ejecución Presupuestaria");
            agregarComponente(vpdf_ver);


            tab_tabla1.setCondicionSucursal(true);
            div_division.dividir2(pat_panel, tab_tabulador, "50%", "H");
            agregarComponente(rep_reporte);
            agregarComponente(sel_rep);
            agregarComponente(div_division);
            agregarComponente(sel_tab_periodo_cabecera);

            sec_rango_reporte.setId("sec_rango_reporte");
            //por defecto friltra un mes
            sec_rango_reporte.setMultiple(false);
            gru_pantalla.getChildren().add(sec_rango_reporte);
            sec_rango_reporte.getBot_aceptar().setMetodo("aceptarReporte");

            sec_rango_reporte_eje.setId("sec_rango_reporte_eje");
            //por defecto friltra un mes
            sec_rango_reporte_eje.setMultiple(false);
            gru_pantalla.getChildren().add(sec_rango_reporte_eje);
            sec_rango_reporte_eje.getBot_aceptar().setMetodo("verEjecucionPresupuestaria");

        }
    }

    
    public void verEjecucionPresupuestaria() {
        if (tab_tabla1.getTotalFilas() > 0) {
            if (sec_rango_reporte_eje.isVisible()) {
                if (sec_rango_reporte_eje.isFechasValidas()) {
                    parametro = new HashMap();
                    TablaGenerica tab_datos = utilitario.consultar("SELECT * FROM sis_empresa e, sis_sucursal s where s.ide_empr=e.ide_empr and s.ide_empr=" + utilitario.getVariable("ide_empr") + " and s.ide_sucu=" + utilitario.getVariable("ide_sucu"));
                    if (tab_datos.getTotalFilas() > 0) {
                        parametro.put("logo", tab_datos.getValor(0, "logo_empr"));
                        parametro.put("empresa", tab_datos.getValor(0, "nom_empr"));
                        parametro.put("sucursal", tab_datos.getValor(0, "nom_sucu"));
                        parametro.put("direccion", tab_datos.getValor(0, "direccion_sucu"));
                        parametro.put("telefono", tab_datos.getValor(0, "telefonos_sucu"));
                        parametro.put("ruc", tab_datos.getValor(0, "identificacion_empr"));
                    }
                    parametro.put("fecha_inicio", getFormatoFecha(conta.getFechaInicialPeriodo(sec_rango_reporte_eje.getFecha1String())));
                    parametro.put("fecha_fin", getFormatoFecha(sec_rango_reporte_eje.getFecha1String()));
                    cls_presupuesto presu = new cls_presupuesto();
                    System.out.println("sel tab seleccionado: " + tab_tabla1.getValor("ide_prppr"));
                    TablaGenerica tab_detalle_presu = presu.getTablaEjecucionPresupuestaria(tab_tabla1.getValor("ide_prppr"), sec_rango_reporte_eje.getFecha1String(), true);
                    List lis_totales_ingreso = presu.calcularTotalesDetallePresupuesto(tab_detalle_presu, utilitario.getVariable("p_pre_tipo_presu_ingreso"));
                    List lis_totales_gastos = presu.calcularTotalesDetallePresupuesto(tab_detalle_presu, utilitario.getVariable("p_pre_tipo_presu_gasto"));
                    double dou_total_valor_presu = Double.parseDouble(lis_totales_ingreso.get(0) + "");
                    double dou_total_valor_ejecutado = Double.parseDouble(lis_totales_ingreso.get(1) + "");
                    double dou_total_valor_variacion = Double.parseDouble(lis_totales_ingreso.get(2) + "");
                    double dou_total_valor_presu_gastos = Double.parseDouble(lis_totales_gastos.get(0) + "");
                    double dou_total_valor_ejecutado_gastos = Double.parseDouble(lis_totales_gastos.get(1) + "");
                    double dou_total_valor_variacion_gastos = Double.parseDouble(lis_totales_gastos.get(2) + "");
                    parametro.put("tot_valor_presupuesto", utilitario.getFormatoNumero(dou_total_valor_presu, 2));
                    parametro.put("tot_valor_ejecutado", utilitario.getFormatoNumero(dou_total_valor_ejecutado, 2));
                    parametro.put("tot_variacion", utilitario.getFormatoNumero(dou_total_valor_variacion, 2));
                    parametro.put("tot_valor_presupuesto_gastos", utilitario.getFormatoNumero(dou_total_valor_presu_gastos, 2));
                    parametro.put("tot_valor_ejecutado_gastos", utilitario.getFormatoNumero(dou_total_valor_ejecutado_gastos, 2));
                    parametro.put("tot_variacion_gastos", utilitario.getFormatoNumero(dou_total_valor_variacion_gastos, 2));
                    ReporteDataSource data = new ReporteDataSource(tab_detalle_presu);
                    System.out.println("total filas detalle presu " + tab_detalle_presu.getTotalFilas());
                    sec_rango_reporte_eje.cerrar();
                    vpdf_ver.setVisualizarPDF("rep_presupuesto/rep_ejecucion_presupuesto.jasper", parametro, data);
                    vpdf_ver.dibujar();
                    utilitario.addUpdate("vpdf_ver,sec_rango_reporte_eje");
                } else {
                    utilitario.agregarMensajeInfo("No se puede generar el reporte", "La fecha seleccecionada no es valida");
                }
            } else {
                sec_rango_reporte_eje.setFecha1(null);
                sec_rango_reporte_eje.dibujar();
            }
        } else {
            utilitario.agregarMensajeInfo("No hay ningun comprobante seleccionado", "");
        }
    }

    public SeleccionCalendario getSec_calendario() {
        return sec_calendario;
    }

    public void setSec_calendario(SeleccionCalendario sec_calendario) {
        this.sec_calendario = sec_calendario;
    }

    public void seleccionar_tabla1(SelectEvent evt) {
        tab_tabla1.seleccionarFila(evt);
        tab_tabla3.setSql("select ide_prdpc as ide_gasto,ide_prcpc,ide_cndpc,nombre_prdpc,valor_prdpc,observacion_prdpc,ejecutado_prdpc,porcentaje_prdpc,variacion_prdpc "
                + "from pre_deta_presu_cuentas WHERE ide_empr=" + utilitario.getVariable("ide_empr") + " and ide_sucu=" + utilitario.getVariable("ide_sucu") + " and ide_prcpc=" + tab_tabla1.getValor("ide_prcpc") + " and ide_prtpr=" + utilitario.getVariable("p_pre_tipo_presu_gasto"));
        tab_tabla3.ejecutarSql();
    }

    public void seleccionar_periodo_presupuesto() {
        System.out.print("Resultado: " + com_periodo_presupuesto.getValue());
        if (com_periodo_presupuesto.getValue() != null) {
            tab_tabla1.setCondicion("ide_prppr=" + com_periodo_presupuesto.getValue().toString());
            tab_tabla1.ejecutarSql();
            tab_tabla2.ejecutarValorForanea(tab_tabla1.getValor("ide_prcpc"));
            tab_tabla3.setSql("select ide_prdpc as ide_gasto, ide_prcpc,ide_cndpc,nombre_prdpc,valor_prdpc,observacion_prdpc,ejecutado_prdpc,porcentaje_prdpc,variacion_prdpc "
                    + "from pre_deta_presu_cuentas WHERE ide_empr=" + utilitario.getVariable("ide_empr") + " and ide_sucu=" + utilitario.getVariable("ide_sucu") + " and ide_prcpc=" + tab_tabla1.getValor("ide_prcpc") + " and ide_prtpr=" + utilitario.getVariable("p_pre_tipo_presu_gasto"));
            System.out.println("valor de ide_prcpc: " + tab_tabla1.getValor("ide_prcpc"));
            tab_tabla3.ejecutarSql();
            utilitario.addUpdate("tab_tabla1,tab_tabla2,tab_tabla3");
        } else {
            tab_tabla1.setCondicion("ide_prcpc=-1");
            tab_tabla1.ejecutarSql();
            tab_tabla2.ejecutarValorForanea(tab_tabla1.getValor("ide_prcpc"));
            tab_tabla3.ejecutarValorForanea(tab_tabla2.getValor("ide_prcpc"));
            tab_tabla3.setSql("select ide_prdpc as ide_gasto, ide_prcpc,ide_cndpc,nombre_prdpc,valor_prdpc,observacion_prdpc,ejecutado_prdpc,porcentaje_prdpc,variacion_prdpc "
                    + "from pre_deta_presu_cuentas WHERE ide_empr=" + utilitario.getVariable("ide_empr") + " and ide_sucu=" + utilitario.getVariable("ide_sucu") + " and ide_prcpc=" + tab_tabla1.getValor("ide_prcpc") + " and ide_prtpr=" + utilitario.getVariable("p_pre_tipo_presu_gasto"));
            tab_tabla3.ejecutarSql();
            utilitario.addUpdate("tab_tabla1,tab_tabla2,tab_tabla3");
        }

    }

    public void aceptarRango() {
        if (sec_calendario.isFechasValidas()) {
            sec_calendario.cerrar();

            utilitario.addUpdate("sec_calendario");


            //Tabla2
            System.out.println("total de filas+ tabla2: " + tab_tabla2.getTotalFilas());
            for (int i = 0; i < tab_tabla2.getTotalFilas(); i++) {
                double dou_aux = conta.obtenerSaldoCuenta(false, tab_tabla2.getValor(i, "ide_cndpc"), sec_calendario.getFecha1String());
                double dou_valor = Double.parseDouble(tab_tabla2.getValor(i, "valor_prdpc"));
                tab_tabla2.setValor(i, "porcentaje_prdpc", "" + utilitario.getFormatoNumero(((dou_aux * 100) / dou_valor), 2));
                tab_tabla2.setValor(i, "variacion_prdpc", "" + utilitario.getFormatoNumero((dou_valor - dou_aux), 2));
                tab_tabla2.setValor(i, "ejecutado_prdpc", "" + dou_aux);
                tab_tabla2.modificar(i);
            }
            utilitario.addUpdate("tab_tabulador:tab_tabla2");

            //Tabla 3
            System.out.println("total de filas+ tabla3: " + tab_tabla2.getTotalFilas());

            for (int i = 0; i < tab_tabla3.getTotalFilas(); i++) {
                double dou_aux = conta.obtenerSaldoCuenta(false, tab_tabla3.getValor(i, "ide_cndpc"), sec_calendario.getFecha1String());
                double dou_valor = Double.parseDouble(tab_tabla3.getValor(i, "valor_prdpc"));
                tab_tabla3.setValor(i, "porcentaje_prdpc", "" + utilitario.getFormatoNumero(((dou_aux * 100) / dou_valor), 2));
                tab_tabla3.setValor(i, "variacion_prdpc", "" + utilitario.getFormatoNumero((dou_valor - dou_aux), 2));
                tab_tabla3.setValor(i, "ejecutado_prdpc", "" + dou_aux);
                tab_tabla3.modificar(i);
            }
            utilitario.addUpdate("tab_tabulador:tab_tabla3");

        } else {
            utilitario.agregarMensajeInfo("Fechas no válidas", "");
        }
    }

    @Override
    public void insertar() {
        if (com_periodo_presupuesto.getValue() != null) {
            if (tab_tabla1.isFocus()) {
                tab_tabla1.insertar();
                tab_tabla1.setValor("ide_prppr", com_periodo_presupuesto.getValue().toString());
            } else if (tab_tabla2.isFocus()) {
                if (!tab_tabla1.isFilaInsertada()) {
                    tab_tabla2.insertar();
                } else {
                    utilitario.agregarMensajeInfo("No se puede insertar", "Debe guardar primero la transacción que esta tabajando");
                }
            } else if (tab_tabla3.isFocus()) {
                if (!tab_tabla1.isFilaInsertada()) {
                    if (!tab_tabla3.isFilaInsertada()) {
                        tab_tabla3.insertar();
                    } else {
                        utilitario.agregarMensajeInfo("No se puede insertar", "Debe guardar primero la transacción que esta tabajando");
                    }
                } else {
                    utilitario.agregarMensajeInfo("No se puede insertar", "Debe guardar primero la transacción que esta tabajando");
                }
            }

        } else {
            utilitario.agregarMensajeInfo("Periodo", "Debe seleccionar un Periodo");
        }
    }

    public void guardarTablaGatos() {
        Tabla tab_gastos = new Tabla();
        tab_gastos.setTabla("pre_deta_presu_cuentas", "ide_prdpc", -1);
        tab_gastos.setCondicion("ide_prdpc=-1");
        tab_gastos.ejecutarSql();
        for (int i = 0; i < tab_tabla3.getTotalFilas(); i++) {
            if (tab_tabla3.isFilaInsertada(i)) {
                tab_gastos.insertar();
                tab_gastos.setValor("ide_prtpr", utilitario.getVariable("p_pre_tipo_presu_gasto"));
                tab_gastos.setValor("ide_empr", utilitario.getVariable("ide_empr"));
                tab_gastos.setValor("ide_sucu", utilitario.getVariable("ide_sucu"));
                tab_gastos.setValor("ide_prcpc", tab_tabla3.getValor(i, "ide_prcpc"));
                tab_gastos.setValor("ide_cndpc", tab_tabla3.getValor(i, "ide_cndpc"));
                tab_gastos.setValor("nombre_prdpc", tab_tabla3.getValor(i, "nombre_prdpc"));
                tab_gastos.setValor("valor_prdpc", tab_tabla3.getValor(i, "valor_prdpc"));
                tab_gastos.setValor("observacion_prdpc", tab_tabla3.getValor(i, "observacion_prdpc"));
                tab_gastos.setValor("ejecutado_prdpc", tab_tabla3.getValor(i, "ejecutado_prdpc"));
                tab_gastos.setValor("porcentaje_prdpc", tab_tabla3.getValor(i, "porcentaje_prdpc"));
                tab_gastos.setValor("variacion_prdpc", tab_tabla3.getValor(i, "variacion_prdpc"));
            } else if (tab_tabla3.isFilaModificada(i)) {
                System.out.println("total de filas tabla 3:  " + tab_tabla3.getTotalFilas());
                for (int j = 0; j < tab_tabla3.getTotalFilas(); j++) {
//                    utilitario.getConexion().agregarSqlPantalla("update pre_deta_presu_cuentas set ide_cndpc=" + tab_tabla3.getValor(i, "ide_cndpc") + ", nombre_prdpc='" + tab_tabla3.getValor(i, "nombre_prdpc") + "',valor_prdpc=" + tab_tabla3.getValor(i, "valor_prdpc") + ", observacion_prdpc='" + tab_tabla3.getValor(i, "observacion_prdpc") + "',ejecutado_prdpc=" + tab_tabla3.getValor(i, "ejecutado_prdpc") + ",porcentaje_prdpc=" + tab_tabla3.getValor(i, "porcentaje_prdpc") + ",variacion_prdpc=" + tab_tabla3.getValor(i, "variacion_prdpc") + "  where ide_prdpc=" + tab_tabla3.getValor(i, "ide_gasto") + " and ide_sucu=" + utilitario.getVariable("ide_sucu") + " and ide_empr=" + utilitario.getVariable("ide_empr") + " ");
                    utilitario.getConexion().agregarSqlPantalla("update pre_deta_presu_cuentas set ide_cndpc=" + tab_tabla3.getValor(i, "ide_cndpc") + ", nombre_prdpc='" + tab_tabla3.getValor(i, "nombre_prdpc") + "',valor_prdpc=" + tab_tabla3.getValor(i, "valor_prdpc") + ", observacion_prdpc='" + tab_tabla3.getValor(i, "observacion_prdpc") + "'  where ide_prdpc=" + tab_tabla3.getValor(i, "ide_gasto") + " and ide_sucu=" + utilitario.getVariable("ide_sucu") + " and ide_empr=" + utilitario.getVariable("ide_empr") + " ");
                    utilitario.addUpdate("tab_tabla3");
                }
//                tab_gastos.setValor("ide_prtpr", utilitario.getVariable("p_pre_tipo_presu_gasto"));
//                tab_gastos.setValor("ide_empr", utilitario.getVariable("ide_empr"));
//                tab_gastos.setValor("ide_sucu", utilitario.getVariable("ide_sucu"));
//                tab_gastos.setValor("ide_prcpc", tab_tabla3.getValor(i, "ide_prcpc"));
//                tab_gastos.setValor("ide_cndpc", tab_tabla3.getValor(i, "ide_cndpc"));
//                tab_gastos.setValor("nombre_prdpc", tab_tabla3.getValor(i, "nombre_prdpc"));
//                tab_gastos.setValor("valor_prdpc", tab_tabla3.getValor(i, "valor_prdpc"));
//                tab_gastos.setValor("observacion_prdpc", tab_tabla3.getValor(i, "observacion_prdpc"));
//                tab_gastos.setValor("ejecutado_prdpc", tab_tabla3.getValor(i, "ejecutado_prdpc"));
//                tab_gastos.setValor("porcentaje_prdpc", tab_tabla3.getValor(i, "porcentaje_prdpc"));
//                tab_gastos.setValor("variacion_prdpc", tab_tabla3.getValor(i,"variacion_prdpc")); 
//                
            }
        }
        tab_gastos.guardar();
    }

    @Override
    public void abrirListaReportes() {
//Se ejecuta cuando da click en el boton de Reportes de la Barra 
        rep_reporte.dibujar();

    }
    Map parametro = new HashMap();

    @Override
    public void aceptarReporte() {
        if (rep_reporte.getReporteSelecionado().equals("Histórico Presupuestario")) { //si
            if (rep_reporte.isVisible()) {//si
                parametro = new HashMap();
                rep_reporte.cerrar();//cierra
                sel_tab_periodo.dibujar();
                utilitario.addUpdate("rep_reporte,sel_tab_periodo");
            } else if (sel_tab_periodo.isVisible()) {
                System.out.println("si es visible");
                if (sel_tab_periodo.getSeleccionados() != null && !sel_tab_periodo.getSeleccionados().isEmpty()) {
                    parametro.put("ide_prppr", sel_tab_periodo.getSeleccionados());//lista sel   
                    System.out.println("seleccionados" + sel_tab_periodo.getSeleccionados());
                    sel_tab_periodo.cerrar();
                    sel_rep.setSeleccionFormatoReporte(parametro, rep_reporte.getPath());
                    sel_rep.dibujar();
                    utilitario.addUpdate("sel_tab_periodo,sel_rep");
                } else {
                    utilitario.agregarMensajeError("Error de Selección ", "Debe seleccionar al menos una Opción");
                }
            }
        } else if (rep_reporte.getReporteSelecionado().equals("Reporte Presupuesto Ejecutado")) {
            if (rep_reporte.isVisible()) {
                parametro = new HashMap();
                rep_reporte.cerrar();
                sec_rango_reporte.setFecha1(null);
                sec_rango_reporte.dibujar();
                utilitario.addUpdate("sec_rango_reporte,rep_reporte");
            } else if (sec_rango_reporte.isVisible()) {
                if (sec_rango_reporte.isFechasValidas()) {
                    if (conta.getFechaInicialPeriodo(sec_rango_reporte.getFecha1String()) != null) {
                        sec_rango_reporte.cerrar();
                        sel_tab_periodo_cabecera.dibujar();
                        TablaGenerica tab_datos = utilitario.consultar("SELECT * FROM sis_empresa e, sis_sucursal s where s.ide_empr=e.ide_empr and s.ide_empr=" + utilitario.getVariable("ide_empr") + " and s.ide_sucu=" + utilitario.getVariable("ide_sucu"));
                        if (tab_datos.getTotalFilas() > 0) {
                            parametro.put("logo", tab_datos.getValor(0, "logo_empr"));
                            parametro.put("empresa", tab_datos.getValor(0, "nom_empr"));
                            parametro.put("sucursal", tab_datos.getValor(0, "nom_sucu"));
                            parametro.put("direccion", tab_datos.getValor(0, "direccion_sucu"));
                            parametro.put("telefono", tab_datos.getValor(0, "telefonos_sucu"));
                            parametro.put("ruc", tab_datos.getValor(0, "identificacion_empr"));
                        }
                        parametro.put("fecha_inicio", getFormatoFecha(conta.getFechaInicialPeriodo(sec_rango_reporte.getFecha1String())));
                        parametro.put("fecha_fin", getFormatoFecha(sec_rango_reporte.getFecha1String()));
                        utilitario.addUpdate("sec_rango_reporte,sel_tab_periodo_cabecera");
                    } else {
                        utilitario.agregarMensajeError("Atención", "El rango de fechas seleccionado no se encuentra en ningun Periodo Contable");
                    }
                }

            } else if (sel_tab_periodo_cabecera.isVisible()) {
                System.out.println("valores de sel tab periodo: " + sel_tab_periodo_cabecera.getSeleccionados());
                if (sel_tab_periodo_cabecera.getValorSeleccionado() != null) {
                    System.out.println("valores de sel tab periodo: " + sel_tab_periodo_cabecera.getSeleccionados());
                    cls_presupuesto presu = new cls_presupuesto();
                    TablaGenerica tab_detalle_presu = presu.getTablaEjecucionPresupuestaria(sel_tab_periodo_cabecera.getValorSeleccionado(), sec_rango_reporte.getFecha1String(), true);
                    List lis_totales_ingreso = presu.calcularTotalesDetallePresupuesto(tab_detalle_presu, utilitario.getVariable("p_pre_tipo_presu_ingreso"));
                    List lis_totales_gastos = presu.calcularTotalesDetallePresupuesto(tab_detalle_presu, utilitario.getVariable("p_pre_tipo_presu_gasto"));
                    double dou_total_valor_presu = Double.parseDouble(lis_totales_ingreso.get(0) + "");
                    double dou_total_valor_ejecutado = Double.parseDouble(lis_totales_ingreso.get(1) + "");
                    double dou_total_valor_variacion = Double.parseDouble(lis_totales_ingreso.get(2) + "");
                    double dou_total_valor_presu_gastos = Double.parseDouble(lis_totales_gastos.get(0) + "");
                    double dou_total_valor_ejecutado_gastos = Double.parseDouble(lis_totales_gastos.get(1) + "");
                    double dou_total_valor_variacion_gastos = Double.parseDouble(lis_totales_gastos.get(2) + "");
                    parametro.put("tot_valor_presupuesto", utilitario.getFormatoNumero(dou_total_valor_presu, 2));
                    parametro.put("tot_valor_ejecutado", utilitario.getFormatoNumero(dou_total_valor_ejecutado, 2));
                    parametro.put("tot_variacion", utilitario.getFormatoNumero(dou_total_valor_variacion, 2));
                    parametro.put("tot_valor_presupuesto_gastos", utilitario.getFormatoNumero(dou_total_valor_presu_gastos, 2));
                    parametro.put("tot_valor_ejecutado_gastos", utilitario.getFormatoNumero(dou_total_valor_ejecutado_gastos, 2));
                    parametro.put("tot_variacion_gastos", utilitario.getFormatoNumero(dou_total_valor_variacion_gastos, 2));
                    ReporteDataSource data = new ReporteDataSource(tab_detalle_presu);
                    sel_rep.setSeleccionFormatoReporte(parametro, rep_reporte.getPath(), data);
                    sel_tab_periodo_cabecera.cerrar();
                    sel_rep.dibujar();
                    utilitario.addUpdate("sel_tab_periodo_cabecera,sel_rep");
                } else {
                    utilitario.agregarMensajeInfo("No se puede generar el reporte", "Debe seleccionar una cabecera");
                }
            }
        }
    }

    public String retornar_mes_letras(int mes) {
        String mes1 = "";
        if (mes == 1) {
            mes1 = "Enero";
        }
        if (mes == 2) {
            mes1 = "Febrero";
        }
        if (mes == 3) {
            mes1 = "Marzo";
        }
        if (mes == 4) {
            mes1 = "Abril";
        }
        if (mes == 5) {
            mes1 = "Mayo";
        }
        if (mes == 6) {
            mes1 = "Junio";
        }
        if (mes == 7) {
            mes1 = "Julio";
        }
        if (mes == 8) {
            mes1 = "Agosto";
        }
        if (mes == 9) {
            mes1 = "Septiembre";
        }
        if (mes == 10) {
            mes1 = "Octubre";
        }
        if (mes == 11) {
            mes1 = "Noviembre";
        }
        if (mes == 12) {
            mes1 = "Diciembre";
        }
        return mes1;
    }

    public String getFormatoFecha(String fecha) {
        String mes = retornar_mes_letras(utilitario.getMes(fecha));
        String dia = utilitario.getDia(fecha) + "";
        String anio = utilitario.getAnio(fecha) + "";
        String fecha_formato = dia + " de " + mes + " del " + anio;
        return fecha_formato;
    }

    public Reporte getRep_reporte() {
        return rep_reporte;
    }

    public void setRep_reporte(Reporte rep_reporte) {
        this.rep_reporte = rep_reporte;
    }

    public SeleccionFormatoReporte getSel_rep() {
        return sel_rep;
    }

    public void setSel_rep(SeleccionFormatoReporte sel_rep) {
        this.sel_rep = sel_rep;
    }

    @Override
    public void abrirRangoFecha() {
        if (com_periodo_presupuesto.getValue() != null) {
            sec_calendario.dibujar();
        } else {
            utilitario.agregarMensajeInfo("Debe seleccionar un tipo de comprobante", "");
        }
    }

    @Override
    public void guardar() {
        if (tab_tabla1.isFocus()) {
            tab_tabla1.guardar();
        } else if (tab_tabla2.isFocus()) {
            tab_tabla2.guardar();
        } else if (tab_tabla3.isFocus()) {
            guardarTablaGatos();
        }
        utilitario.getConexion().guardarPantalla();
        tab_tabla3.setSql("select ide_prdpc as ide_gasto, ide_prcpc,ide_cndpc,nombre_prdpc,valor_prdpc,observacion_prdpc,ejecutado_prdpc,porcentaje_prdpc,variacion_prdpc "
                + "from pre_deta_presu_cuentas WHERE ide_empr=" + utilitario.getVariable("ide_empr") + " and ide_sucu=" + utilitario.getVariable("ide_sucu") + " and ide_prcpc=" + tab_tabla1.getValor("ide_prcpc") + " and ide_prtpr=" + utilitario.getVariable("p_pre_tipo_presu_gasto"));
        tab_tabla3.ejecutarSql();
        utilitario.addUpdate("tab_tabla3");
    }

    public void eliminarTablaGastos() {
        if (!tab_tabla3.isFilaInsertada()) {
            utilitario.getConexion().agregarSqlPantalla("DELETE FROM pre_deta_presu_cuentas WHERE ide_prdpc=" + tab_tabla3.getValor("ide_gasto"));
        } else {
            tab_tabla3.eliminar();
        }
        utilitario.getConexion().guardarPantalla();
        tab_tabla3.setSql("select ide_prdpc as ide_gasto, ide_prcpc,ide_cndpc,nombre_prdpc,valor_prdpc,observacion_prdpc,ejecutado_prdpc,porcentaje_prdpc,variacion_prdpc "
                + "from pre_deta_presu_cuentas WHERE ide_empr=" + utilitario.getVariable("ide_empr") + " and ide_sucu=" + utilitario.getVariable("ide_sucu") + " and ide_prcpc=" + tab_tabla1.getValor("ide_prcpc") + " and ide_prtpr=" + utilitario.getVariable("p_pre_tipo_presu_gasto"));
        tab_tabla3.ejecutarSql();
        utilitario.addUpdate("tab_tabla3");
    }

    @Override
    public void eliminar() {
        if (tab_tabla1.isFocus()) {
            tab_tabla1.eliminar();
        } else if (tab_tabla2.isFocus()) {
            tab_tabla2.eliminar();
        } else if (tab_tabla3.isFocus()) {
            eliminarTablaGastos();
        }
    }

    public Tabla getTab_tabla1() {
        return tab_tabla1;
    }

    public void setTab_tabla1(Tabla tab_tabla1) {
        this.tab_tabla1 = tab_tabla1;
    }

    public Tabla getTab_tabla2() {
        return tab_tabla2;
    }

    public void setTab_tabla2(Tabla tab_tabla2) {
        this.tab_tabla2 = tab_tabla2;
    }

    public Tabla getTab_tabla3() {
        return tab_tabla3;
    }

    public void setTab_tabla3(Tabla tab_tabla3) {
        this.tab_tabla3 = tab_tabla3;
    }

    public Tabulador getTab_tabulador() {
        return tab_tabulador;
    }

    public void setTab_tabulador(Tabulador tab_tabulador) {
        this.tab_tabulador = tab_tabulador;
    }

    public SeleccionTabla getSel_tab_periodo() {
        return sel_tab_periodo;
    }

    public void setSel_tab_periodo(SeleccionTabla sel_tab_periodo) {
        this.sel_tab_periodo = sel_tab_periodo;
    }

    public SeleccionCalendario getSec_rango_reporte() {
        return sec_rango_reporte;
    }

    public void setSec_rango_reporte(SeleccionCalendario sec_rango_reporte) {
        this.sec_rango_reporte = sec_rango_reporte;
    }

    public SeleccionTabla getSel_tab_periodo_cabecera() {
        return sel_tab_periodo_cabecera;
    }

    public void setSel_tab_periodo_cabecera(SeleccionTabla sel_tab_periodo_cabecera) {
        this.sel_tab_periodo_cabecera = sel_tab_periodo_cabecera;
    }
}
