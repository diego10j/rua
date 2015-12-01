/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg_presupuesto;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Boton;
import framework.componentes.Check;
import framework.componentes.Combo;
import framework.componentes.Dialogo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionCalendario;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import framework.componentes.Tabulador;
import framework.reportes.ReporteDataSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.faces.event.AjaxBehaviorEvent;
import org.primefaces.event.SelectEvent;
import pkg_contabilidad.cls_contabilidad;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author user
 */
public class pre_proforma_presupuesto extends Pantalla {

    private Tabla tab_tabla1 = new Tabla();
    private Tabla tab_tabla2 = new Tabla();
    private Tabla tab_tabla3 = new Tabla();
    private Tabla tab_tabla4 = new Tabla();
    private Division div_division = new Division();
    private Combo com_periodo_presupuesto = new Combo();
    private Combo com_organigrama = new Combo();
    private Reporte rep_reporte = new Reporte();
    private SeleccionFormatoReporte sel_rep = new SeleccionFormatoReporte();
    private SeleccionTabla sel_tab = new SeleccionTabla();
    private SeleccionTabla sel_tab_periodo = new SeleccionTabla();
    private SeleccionTabla sel_tab_mes = new SeleccionTabla();
    private SeleccionTabla sel_tab_modificacion = new SeleccionTabla();
    private Tabulador tab_tabulador = new Tabulador();
    private Grid gri_totales = new Grid();
    private Check chk_aprobado = new Check();
    private Dialogo dia_cambiar_estado = new Dialogo();
    private Boton bot_cambiar_presupuesto = new Boton();
    private SeleccionTabla sel_tab_periodo_cabecera = new SeleccionTabla();
    private SeleccionCalendario sec_rango_reporte = new SeleccionCalendario();
    cls_contabilidad conta = new cls_contabilidad();

    public pre_proforma_presupuesto() {
        List lis_plan = utilitario.getConexion().consultar("select ide_CNCPC from con_cab_plan_cuen where activo_cncpc=true");
        if (lis_plan != null && !lis_plan.isEmpty()) {
            tab_tabulador.setId("tab_tabulador");
            bar_botones.agregarReporte();

            com_periodo_presupuesto.setId("com_periodo_presupuesto");
            Etiqueta eti1 = new Etiqueta();
            eti1.setValue("Periodo Presupuesto :");
            bar_botones.agregarComponente(eti1);
            com_periodo_presupuesto.setCombo("select ide_prppr,nombre_prppr from pre_periodo_presu where ide_empr=" + utilitario.getVariable("ide_empr"));
            com_periodo_presupuesto.setMetodo("seleccionar_periodo_presupuesto");
            bar_botones.agregarComponente(com_periodo_presupuesto);
            com_organigrama.setMetodo("seleccionarOrganigrama");

            com_organigrama.setId("com_organigrama");
            Etiqueta eti_organigrama = new Etiqueta();
            eti_organigrama.setValue("Organigrama");
            bar_botones.agregarComponente(eti_organigrama);
            bar_botones.agregarComponente(com_organigrama);
            com_organigrama.setCombo("SELECT ide_georg,nombre_georg FROM gen_organigrama WHERE ide_empr=" + utilitario.getVariable("ide_empr"));

            tab_tabla1.setId("tab_tabla1");
            tab_tabla1.setTabla("pre_cab_plan_presu", "ide_prcppr", 1);
            tab_tabla1.onSelect("seleccionar_tabla1");
            tab_tabla1.agregarRelacion(tab_tabla2);
            tab_tabla1.getColumna("ide_prppr").setCombo("pre_periodo_presu", "ide_prppr", "nombre_prppr", "");
            tab_tabla1.getColumna("ide_cndpc").setCombo("con_det_plan_cuen", "ide_cndpc", "codig_recur_cndpc,nombre_cndpc", "nivel_cndpc='HIJO' and ide_cncpc=" + lis_plan.get(0));
            tab_tabla1.getColumna("ide_cndpc").setAutoCompletar();
            tab_tabla1.getColumna("ide_prepp").setCombo("pre_estado_plan_presu", "ide_prepp", "nombre_prepp", "");
            tab_tabla1.getColumna("ide_usua").setCombo("sis_usuario", "ide_usua", "nom_usua", "");
            tab_tabla1.getColumna("ide_usua").setValorDefecto(utilitario.getVariable("ide_usua"));
            tab_tabla1.getColumna("ide_gemes").setCombo("gen_mes", "ide_gemes", "nombre_gemes", "");
            tab_tabla1.getColumna("ide_usua").setLectura(true);
            tab_tabla1.getColumna("ide_prppr").setVisible(false);
            tab_tabla1.getColumna("ide_georg").setCombo("gen_organigrama", "ide_georg", "nombre_georg", "");
            tab_tabla1.getColumna("fecha_sistema_prcppr").setValorDefecto(utilitario.getFechaActual());
            tab_tabla1.getColumna("hora_sistema_prcppr").setValorDefecto(utilitario.getHoraActual());
            tab_tabla1.getColumna("fecha_sistema_prcppr").setVisible(false);
            tab_tabla1.getColumna("hora_sistema_prcppr").setVisible(false);
            tab_tabla1.getColumna("fecha_trans_prcppr").setValorDefecto(utilitario.getFechaActual());
            tab_tabla1.getColumna("ide_georg").setVisible(false);
            tab_tabla1.getColumna("es_activo_prcppr").setValorDefecto("true");
            tab_tabla1.getColumna("ide_prepp").setValorDefecto(utilitario.getVariable("p_pre_estado_en_tramite"));
            //tab_tabla1.getColumna("ide_cndpc").setRequerida(true);
            tab_tabla1.getColumna("ide_prepp").setRequerida(true);
            tab_tabla1.getColumna("nombre_prcppr").setRequerida(true);
            tab_tabla1.getColumna("observacion_prcppr").setRequerida(true);
            tab_tabla1.setCondicion("ide_prcppr=-1"); //para mostrar en blanco
            tab_tabla1.dibujar();
            PanelTabla pat_panel1 = new PanelTabla();
            pat_panel1.setPanelTabla(tab_tabla1);

            tab_tabla2.setId("tab_tabla2");
            tab_tabla2.setTabla("pre_descripcion_presu", "ide_prdpr", 2);
            tab_tabla2.onSelect("seleccionar_tabla2");
            tab_tabla2.agregarRelacion(tab_tabla3);
            tab_tabla2.agregarRelacion(tab_tabla4);
            tab_tabla2.getColumna("ide_prtrp").setCombo("pre_tipo_rubro_presu", "ide_prtrp", "nombre_prtrp", "nivel_prtrp='HIJO'");
            tab_tabla2.getColumna("ide_prtrp").setAutoCompletar();
            tab_tabla2.getColumna("ide_prcppr").setCombo("pre_cab_plan_presu", "ide_prcppr", "nombre_prcppr", "");
            tab_tabla2.getColumna("ide_geubi").setCombo("gen_ubicacion", "ide_geubi", "nombre_geubi", "nivel_geubi='HIJO'");
            tab_tabla2.getColumna("ide_geubi").setAutoCompletar();
            tab_tabla2.getColumna("ide_geubi").setRequerida(true);
            tab_tabla2.getColumna("ide_prtrp").setRequerida(true);
            tab_tabla2.dibujar();

            PanelTabla pat_panel2 = new PanelTabla();
            pat_panel2.setPanelTabla(tab_tabla2);

            //Tabla de INGRESOS
            tab_tabla3.setId("tab_tabla3");
            tab_tabla3.setTabla("pre_detalle_presu", "ide_prdep", 3);

            tab_tabla3.getColumna("ide_cndpc").setCombo("con_det_plan_cuen", "ide_cndpc", "codig_recur_cndpc,nombre_cndpc", "nivel_cndpc='HIJO' and ide_cncpc=" + lis_plan.get(0) + " and ide_cntcu in(" + utilitario.getVariable("p_con_tipo_cuenta_ingresos") + ")");
            tab_tabla3.getColumna("ide_cndpc").setAutoCompletar();
            tab_tabla3.getColumna("ide_prtap").setCombo("pre_tipo_aporte_presu", "ide_prtap", "nombre_prtap", "");
            tab_tabla3.getColumna("ide_prtap").setMetodoChange("calcula_total_ingresos");
            tab_tabla3.getColumna("ide_prtpa").setVisible(false);
            tab_tabla3.getColumna("num_prdep").setVisible(false);
            tab_tabla3.getColumna("veces_prdep").setVisible(false);
            tab_tabla3.getColumna("costo_unitario_prdep").setVisible(false);
            tab_tabla3.getColumna("ide_prtpr").setVisible(false);
            tab_tabla3.getColumna("ide_prtap").setRequerida(true);
            //tab_tabla3.getColumna("ide_cndpc").setRequerida(true);
            tab_tabla3.getColumna("nombre_prdep").setRequerida(true);
            //tab_tabla3.getColumna("total_prdep").setRequerida(true);
            tab_tabla3.getColumna("total_prdep").setMetodoChange("calcula_total_ingresos");
            tab_tabla3.getColumna("ide_prtpr").setValorDefecto(utilitario.getVariable("p_pre_tipo_presu_ingreso"));
            //  tab_tabla3.setCondicion("ide_prdep=-1");
            tab_tabla3.setIdCompleto("tab_tabulador:tab_tabla3");
            tab_tabla3.setCondicion("ide_prtpr=" + utilitario.getVariable("p_pre_tipo_presu_ingreso"));
            tab_tabla3.dibujar();

            PanelTabla pat_panel3 = new PanelTabla();
            pat_panel3.setPanelTabla(tab_tabla3);

            //tabla de gastos
            tab_tabla4.setId("tab_tabla4");
            tab_tabla4.setSql("SELECT ide_prdep as ide_gasto,ide_prdpr,ide_prtpa,ide_cndpc,nombre_prdep,num_prdep,veces_prdep,costo_unitario_prdep,total_prdep,observacion_prdep  "
                    + "FROM  pre_detalle_presu WHERE ide_empr=" + utilitario.getVariable("ide_empr") + " and ide_sucu=" + utilitario.getVariable("ide_sucu") + " and ide_prdep=-1 and ide_prtpr=" + utilitario.getVariable("p_pre_tipo_presu_gasto"));
            //tab_tabla4.setTabla("pre_detalle_presu", "ide_prdep", 3);
            tab_tabla4.setCampoPrimaria("ide_gasto");
            tab_tabla4.getColumna("ide_cndpc").setCombo("con_det_plan_cuen", "ide_cndpc", "codig_recur_cndpc,nombre_cndpc", "nivel_cndpc='HIJO' and ide_cncpc=" + lis_plan.get(0) + " and ide_cntcu in(" + utilitario.getVariable("p_con_tipo_cuenta_gastos") + ")");
            tab_tabla4.getColumna("ide_cndpc").setAutoCompletar();
            tab_tabla4.getColumna("ide_prtpa").setCombo("pre_tipo_participantes", "ide_prtpa", "nombre_prtpa", "");
            tab_tabla4.getColumna("num_prdep").setMetodoChange("obtener_total_detalle_presu");
            tab_tabla4.getColumna("veces_prdep").setMetodoChange("obtener_total_detalle_presu");
            tab_tabla4.getColumna("costo_unitario_prdep").setMetodoChange("obtener_total_detalle_presu");
            tab_tabla4.getColumna("total_prdep").setEtiqueta();
            tab_tabla4.getColumna("total_prdep").setEstilo("font-size:13px;font-weight: bold;");
            tab_tabla4.getColumna("total_prdep").setMetodoChange("calcula_total_ingresos");
//            tab_tabla4.getColumna("ide_prtpa").setRequerida(true);
//            //tab_tabla4.getColumna("ide_cndpc").setRequerida(true);
//            tab_tabla4.getColumna("nombre_prdep").setRequerida(true);
//            tab_tabla4.getColumna("num_prdep").setRequerida(true);
//            tab_tabla4.getColumna("veces_prdep").setRequerida(true);
//            tab_tabla4.getColumna("costo_unitario_prdep").setRequerida(true);
//            //  tab_tabla4.getColumna("total_prdep").setRequerida(true);
            tab_tabla4.setIdCompleto("tab_tabulador:tab_tabla4");
            tab_tabla4.dibujar();

            PanelTabla pat_panel4 = new PanelTabla();
            pat_panel4.setPanelTabla(tab_tabla4);
            gri_totales.setId("gri_totales");

            List lis_tipo_aporte = utilitario.getConexion().consultar("SELECT nombre_prtap FROM pre_tipo_aporte_presu WHERE ide_empr=" + utilitario.getVariable("ide_empr"));

            if (lis_tipo_aporte != null) {
                for (int i = 0; i < lis_tipo_aporte.size(); i++) {
                    Etiqueta eti_aporte = new Etiqueta();
                    eti_aporte.setValue(lis_tipo_aporte.get(i).toString());
                    System.out.println("Aporte: " + lis_tipo_aporte.get(i).toString());
                    gri_totales.getChildren().add(eti_aporte);
                    gri_totales.getChildren().add(new Etiqueta("0.00"));
                }
            }
            gri_totales.setColumns(2);
            gri_totales.getChildren().add(new Etiqueta("Total Gastos"));
            gri_totales.getChildren().add(new Etiqueta("0.00"));

            Grid gri_cambiar_estado = new Grid();
            gri_cambiar_estado.setColumns(1);
            gri_cambiar_estado.getChildren().add(new Etiqueta("Esta seguro  que desea cambiar el estado del plan presupuestado a APROBADO,"));
            gri_cambiar_estado.getChildren().add(new Etiqueta("Una vez guardado el estado en aprobado NO podra mofificar el plan"));

            dia_cambiar_estado.setId("dia_cambiar_estado");
            dia_cambiar_estado.setWidth("40%");
            dia_cambiar_estado.setHeight("20%");
            dia_cambiar_estado.setHeader("Cambiar Estado Presupuesto");
            dia_cambiar_estado.setDialogo(gri_cambiar_estado);
            dia_cambiar_estado.getBot_aceptar().setMetodo("cambiarEstadoPresupuesto");
            //dia_cambiar_estado.getBot_aceptar().setMetodo("validarCambiarPlanPresupuesto");

            gri_cambiar_estado.setStyle("width:" + (dia_cambiar_estado.getAnchoPanel() - 5) + "px;height:" + dia_cambiar_estado.getAltoPanel() + "px;overflow: auto;display: block;");

            //dia_pago_prestamo.setDynamic(false);
            bot_cambiar_presupuesto.setId("bot_cambiar_presupuesto");
            bot_cambiar_presupuesto.setValue("Modificar Presupuesto");
            bot_cambiar_presupuesto.setMetodo("modificarPresupuesto");
            bot_cambiar_presupuesto.setDisabled(true);

            Division div_aux_cab = new Division();
            div_aux_cab.setId("div_aux_cab");
            div_aux_cab.dividir2(pat_panel1, gri_totales, "80%", "V");

            tab_tabulador.agregarTab("INGRESOS", pat_panel3);
            tab_tabulador.agregarTab("GASTOS", pat_panel4);

            div_division.setId("div_division");
            div_division.dividir3(div_aux_cab, pat_panel2, tab_tabulador, "25%", "35%", "H");

            agregarComponente(div_division);
            agregarComponente(sel_rep);
            agregarComponente(dia_cambiar_estado);

            sel_tab.setId("sel_tab");
            sel_tab.setSeleccionTabla("SELECT ide_georg,nombre_georg FROM gen_organigrama WHERE ide_empr=" + utilitario.getVariable("ide_empr"), "ide_georg");
            sel_tab.getTab_seleccion().getColumna("nombre_georg").setFiltro(true);
            gru_pantalla.getChildren().add(sel_tab);

            sel_tab_periodo.setId("sel_tab_periodo");
            sel_tab_periodo.setSeleccionTabla("SELECT ide_prppr,nombre_prppr FROM pre_periodo_presu WHERE ide_empr=" + utilitario.getVariable("ide_empr"), "ide_prppr");
            sel_tab_periodo.getTab_seleccion().getColumna("nombre_prppr").setFiltro(true);
            gru_pantalla.getChildren().add(sel_tab_periodo);

            sel_tab_mes.setId("sel_tab_mes");
            sel_tab_mes.setSeleccionTabla("SELECT ide_gemes,nombre_gemes FROM gen_mes WHERE ide_empr=" + utilitario.getVariable("ide_empr") + " ORDER BY ide_gemes ASC", "ide_gemes");
            sel_tab_mes.getTab_seleccion().getColumna("nombre_gemes").setFiltro(true);
            gru_pantalla.getChildren().add(sel_tab_mes);

            sel_tab_modificacion.setId("sel_tab_modificacion");
            sel_tab_modificacion.setSeleccionTabla("SELECT num_modificacion_prcppr as primaria,num_modificacion_prcppr FROM pre_cab_plan_presu WHERE ide_empr=" + utilitario.getVariable("ide_empr") + " AND ide_sucu=" + utilitario.getVariable("ide_sucu") + " GROUP BY num_modificacion_prcppr ORDER BY num_modificacion_prcppr ", "primaria");
            sel_tab_modificacion.getTab_seleccion().getColumna("num_modificacion_prcppr").setFiltro(true);
            gru_pantalla.getChildren().add(sel_tab_modificacion);

            rep_reporte.setId("rep_reporte");
            rep_reporte.getBot_aceptar().setMetodo("aceptarReporte");

            sel_tab.getBot_aceptar().setMetodo("aceptarReporte");
            sel_tab_periodo.getBot_aceptar().setMetodo("aceptarReporte");
            sel_tab_mes.getBot_aceptar().setMetodo("aceptarReporte");
            sel_tab_modificacion.getBot_aceptar().setMetodo("aceptarReporte");

            sel_rep.setId("sel_rep");
            gru_pantalla.getChildren().add(rep_reporte);

            chk_aprobado.setId("chk_aprobado");
            bar_botones.agregarComponente(new Etiqueta("Aprobado"));
            bar_botones.agregarComponente(chk_aprobado);
            chk_aprobado.setMetodoChange("cambiarEstadoPresupuesto");
            chk_aprobado.setDisabled(true);
            bar_botones.agregarBoton(bot_cambiar_presupuesto);
            //System.out.println("Aprobado valor del check:  "+chk_aprobado.getValue());

            sel_tab_periodo_cabecera.setId("sel_tab_periodo_cabecera");
            sel_tab_periodo_cabecera.setSeleccionTabla("SELECT ide_prppr,nombre_prppr FROM pre_periodo_presu WHERE ide_empr=" + utilitario.getVariable("ide_empr"), "ide_prppr");
            gru_pantalla.getChildren().add(sel_tab_periodo_cabecera);
            sel_tab_periodo_cabecera.setRadio();
            sel_tab_periodo_cabecera.getBot_aceptar().setMetodo("aceptarReporte");

            sec_rango_reporte.setId("sec_rango_reporte");
            //por defecto friltra un mes
            sec_rango_reporte.setMultiple(false);
            gru_pantalla.getChildren().add(sec_rango_reporte);
            sec_rango_reporte.getBot_aceptar().setMetodo("aceptarReporte");

            seleccionar_periodo_presupuesto();
        } else {
            utilitario.agregarNotificacionInfo("No existe un plan de cuentas activo", "Para poder ingresar a esta pantalla debe estar activo un plan de cuentas, contactese con el administrador del sistema");
        }

        tab_tabla1.setCondicionSucursal(true);
    }

    public void modificarPresupuesto() {
        System.out.println("si ingresa al metodo del boton ");
        if (com_periodo_presupuesto.getValue() != null && com_organigrama.getValue() != null) {
            if (chk_aprobado.getValue().equals("true")) {
                if (validarModificacion()) {
                    System.out.println("pase validacion  ");
                    utilitario.getConexion().agregarSqlPantalla("UPDATE pre_cab_plan_presu SET es_activo_prcppr=FALSE WHERE ide_prppr=" + com_periodo_presupuesto.getValue() + " AND es_activo_prcppr=TRUE");
                    utilitario.getConexion().agregarSqlPantalla("UPDATE pre_periodo_presu SET aprobado_prppr=FALSE WHERE ide_prppr=" + com_periodo_presupuesto.getValue());
                    insertarDatosCabecera();
                    tab_tabla1.setCondicion("ide_prppr=" + com_periodo_presupuesto.getValue().toString() + " and ide_georg=" + com_organigrama.getValue() + " and es_activo_prcppr=true");
                    chk_aprobado.setValue(false);
                    chk_aprobado.setDisabled(false);
                    utilitario.addUpdate("chk_aprobado");
                    tab_tabla1.getColumna("ide_gemes").setLectura(false);
                    tab_tabla1.getColumna("ide_cndpc").setLectura(false);
                    tab_tabla1.getColumna("ide_usua").setLectura(false);
                    tab_tabla1.getColumna("ide_prepp").setLectura(false);
                    tab_tabla1.getColumna("nombre_prcppr").setLectura(false);
                    tab_tabla1.getColumna("observacion_prcppr").setLectura(false);
                    tab_tabla1.getColumna("fecha_trans_prcppr").setLectura(false);
                    tab_tabla2.getColumna("ide_prtrp").setLectura(false);
                    tab_tabla2.getColumna("ide_geubi").setLectura(false);
                    tab_tabla2.getColumna("observacion_prdpr").setLectura(false);
                    tab_tabla3.getColumna("ide_prtap").setLectura(false);
                    tab_tabla3.getColumna("ide_cndpc").setLectura(false);
                    tab_tabla3.getColumna("nombre_prdep").setLectura(false);
                    tab_tabla3.getColumna("observacion_prdep").setLectura(false);
                    tab_tabla3.getColumna("total_prdep").setLectura(false);
                    tab_tabla4.getColumna("ide_prtpa").setLectura(false);
                    tab_tabla4.getColumna("ide_cndpc").setLectura(false);
                    tab_tabla4.getColumna("nombre_prdep").setLectura(false);
                    tab_tabla4.getColumna("observacion_prdep").setLectura(false);
                    tab_tabla4.getColumna("num_prdep").setLectura(false);
                    tab_tabla4.getColumna("veces_prdep").setLectura(false);
                    tab_tabla4.getColumna("costo_unitario_prdep").setLectura(false);
                    tab_tabla4.getColumna("total_prdep").setLectura(false);
                    tab_tabla1.ejecutarSql();
                    tab_tabla2.ejecutarValorForanea(tab_tabla1.getValor("ide_prcppr"));
                    tab_tabla3.ejecutarValorForanea(tab_tabla2.getValor("ide_prdpr"));
                    tab_tabla4.setSql("SELECT ide_prdep as ide_gasto,ide_prdpr,ide_prtpa,ide_cndpc,nombre_prdep,num_prdep,veces_prdep,costo_unitario_prdep,total_prdep,observacion_prdep  "
                            + "FROM  pre_detalle_presu WHERE ide_prdep=" + tab_tabla2.getValor("ide_prdpr"));
                    utilitario.addUpdate("tab_tabla1,tab_tabla2,tab_tabla3,tab_tabla4");

                }
            }
        }
    }
    int nummax;

    public boolean validarModificacion() {
        List lis_num_max_mod = utilitario.getConexion().consultar("SELECT MAX (num_modificacion_prcppr) FROM pre_cab_plan_presu WHERE ide_prppr=" + com_periodo_presupuesto.getValue() + " ");
        if (lis_num_max_mod.size() > 0) {
            if (lis_num_max_mod.get(0) != null && !lis_num_max_mod.get(0).toString().isEmpty()) {
                nummax = Integer.parseInt(lis_num_max_mod.get(0).toString());
                nummax = nummax + 1;
                if (nummax <= Integer.parseInt(utilitario.getVariable("p_pre_num_modificacion_presu"))) {
                    return true;
                } else {
                    utilitario.agregarNotificacionInfo("Atención ", "No se Puede Realizar la modificación Presupuestara porque no puede sobre pasar las modificaciones");
                    return false;
                }
            } else {
                utilitario.agregarNotificacionInfo("Atención 2", "No se Puede Realizar la modificación Presupuestaria");
                return false;
            }
        } else {
            utilitario.agregarNotificacionInfo("Atenciónn 1 ", "No se Puede Realizar la modificación Presupuestaria");
            return false;
        }
    }

    public void insertarDatosCabecera() {
        TablaGenerica tab_cabecera_presupuesto_anterior = utilitario.consultar("SELECT * FROM pre_cab_plan_presu WHERE ide_prppr=2 and es_activo_prcppr is TRUE ORDER BY ide_prcppr ASC");
        TablaGenerica tab_cabecera_presupuesto = new TablaGenerica();
        tab_cabecera_presupuesto.setTabla("pre_cab_plan_presu", "ide_prcppr", -1);
        tab_cabecera_presupuesto.setCondicion("ide_prcppr=-1");
        tab_cabecera_presupuesto.ejecutarSql();
        for (int i = 0; i < tab_cabecera_presupuesto_anterior.getTotalFilas(); i++) {
            tab_cabecera_presupuesto.insertar();
            tab_cabecera_presupuesto.setValor("ide_prppr", tab_cabecera_presupuesto_anterior.getValor(i, "ide_prppr"));
            tab_cabecera_presupuesto.setValor("ide_cndpc", tab_cabecera_presupuesto_anterior.getValor(i, "ide_cndpc"));
            tab_cabecera_presupuesto.setValor("ide_sucu", utilitario.getVariable("ide_sucu"));
            tab_cabecera_presupuesto.setValor("ide_usua", utilitario.getVariable("ide_usua"));
            tab_cabecera_presupuesto.setValor("ide_empr", utilitario.getVariable("ide_empr"));
            tab_cabecera_presupuesto.setValor("ide_prepp", utilitario.getVariable("p_pre_estado_en_tramite"));
            tab_cabecera_presupuesto.setValor("nombre_prcppr", tab_cabecera_presupuesto_anterior.getValor(i, "nombre_prcppr"));
            tab_cabecera_presupuesto.setValor("observacion_prcppr", tab_cabecera_presupuesto_anterior.getValor(i, "observacion_prcppr"));
            tab_cabecera_presupuesto.setValor("fecha_trans_prcppr", tab_cabecera_presupuesto_anterior.getValor(i, "fecha_trans_prcppr"));
            tab_cabecera_presupuesto.setValor("fecha_sistema_prcppr", utilitario.getFechaActual());
            tab_cabecera_presupuesto.setValor("hora_sistema_prcppr", utilitario.getHoraActual());
            tab_cabecera_presupuesto.setValor("ide_georg", tab_cabecera_presupuesto_anterior.getValor(i, "ide_georg"));
            tab_cabecera_presupuesto.setValor("ide_gemes", tab_cabecera_presupuesto_anterior.getValor(i, "ide_gemes"));
            tab_cabecera_presupuesto.setValor("num_modificacion_prcppr", nummax + "");
            tab_cabecera_presupuesto.setValor("ide_anterior_prcppr", tab_cabecera_presupuesto_anterior.getValor(i, "ide_prcppr"));
            tab_cabecera_presupuesto.setValor("es_activo_prcppr", "true");
        }
        tab_cabecera_presupuesto.guardar();
        utilitario.getConexion().guardarPantalla();
        insertarDatosDescripcion();
        insertarDatosDetalle();
    }

    public void insertarDatosDescripcion() {

        TablaGenerica tab_descripcion_presupuesto_anterior = utilitario.consultar("SELECT * FROM pre_descripcion_presu WHERE ide_prcppr in(SELECT ide_prcppr FROM pre_cab_plan_presu WHERE ide_prppr=" + com_periodo_presupuesto.getValue() + " and num_modificacion_prcppr=" + (nummax - 1) + ")");
        TablaGenerica tab_descripcion_presupuesto = new TablaGenerica();
        tab_descripcion_presupuesto.setTabla("pre_descripcion_presu", "ide_prdpr", -1);
        tab_descripcion_presupuesto.setCondicion("ide_prdpr=-1");
        tab_descripcion_presupuesto.ejecutarSql();
        for (int i = 0; i < tab_descripcion_presupuesto_anterior.getTotalFilas(); i++) {
            tab_descripcion_presupuesto.insertar();
            tab_descripcion_presupuesto.setValor("ide_prtrp", tab_descripcion_presupuesto_anterior.getValor(i, "ide_prtrp"));
            tab_descripcion_presupuesto.setValor("ide_sucu", utilitario.getVariable("ide_sucu"));
            tab_descripcion_presupuesto.setValor("ide_empr", utilitario.getVariable("ide_empr"));
            tab_descripcion_presupuesto.setValor("ide_geubi", tab_descripcion_presupuesto_anterior.getValor(i, "ide_geubi"));
            tab_descripcion_presupuesto.setValor("observacion_prdpr", tab_descripcion_presupuesto_anterior.getValor(i, "observacion_prdpr"));
            tab_descripcion_presupuesto.setValor("ide_anterior_prdpr", tab_descripcion_presupuesto_anterior.getValor(i, "ide_prdpr"));
//            tab_descripcion_presupuesto.setValor("ide_prcppr", tab_descripcion_presupuesto_anterior.getValor(0, "ide_prcppr"));
//            Tabla tab_ide_prcppr = utilitario.consultar("SELECT * FROM pre_cab_plan_presu WHERE  ide_anterior_prcppr=" + tab_descripcion_presupuesto_anterior.getValor(i, "ide_prcppr"));
            TablaGenerica tab_ide_prcppr = utilitario.consultar("SELECT * FROM pre_cab_plan_presu WHERE  ide_anterior_prcppr=" + tab_descripcion_presupuesto_anterior.getValor(i, "ide_prcppr"));
            if (tab_ide_prcppr.getTotalFilas() > 0) {
                tab_descripcion_presupuesto.setValor("ide_prcppr", tab_ide_prcppr.getValor(0, "ide_prcppr"));
            }
        }
        tab_descripcion_presupuesto.guardar();
        utilitario.getConexion().guardarPantalla();
    }

    public void insertarDatosDetalle() {
        TablaGenerica tab_detalle_presupuesto_anterior = utilitario.consultar("SELECT * FROM pre_detalle_presu WHERE ide_prdpr IN ( SELECT ide_prdpr FROM pre_descripcion_presu WHERE ide_prcppr in(SELECT ide_prcppr FROM pre_cab_plan_presu WHERE ide_prppr=" + com_periodo_presupuesto.getValue() + " and num_modificacion_prcppr=" + (nummax - 1) + "))");
        Tabla tab_detalle_presupuesto = new Tabla();
        tab_detalle_presupuesto.setTabla("pre_detalle_presu", "ide_prdep", -1);
        tab_detalle_presupuesto.setCondicion("ide_prdep=-1");
        tab_detalle_presupuesto.ejecutarSql();
        for (int i = 0; i < tab_detalle_presupuesto_anterior.getTotalFilas(); i++) {
            tab_detalle_presupuesto.insertar();
            tab_detalle_presupuesto.setValor("ide_prtap", tab_detalle_presupuesto_anterior.getValor(i, "ide_prtap"));
            tab_detalle_presupuesto.setValor("ide_prtpa", tab_detalle_presupuesto_anterior.getValor(i, "ide_prtpa"));
            tab_detalle_presupuesto.setValor("ide_sucu", utilitario.getVariable("ide_sucu"));
            tab_detalle_presupuesto.setValor("ide_cndpc", tab_detalle_presupuesto_anterior.getValor(i, "ide_cndpc"));
            tab_detalle_presupuesto.setValor("ide_empr", utilitario.getVariable("ide_empr"));
            tab_detalle_presupuesto.setValor("nombre_prdep", tab_detalle_presupuesto_anterior.getValor(i, "nombre_prdep"));
            tab_detalle_presupuesto.setValor("num_prdep", tab_detalle_presupuesto_anterior.getValor(i, "num_prdep"));
            tab_detalle_presupuesto.setValor("veces_prdep", tab_detalle_presupuesto_anterior.getValor(i, "veces_prdep"));
            tab_detalle_presupuesto.setValor("costo_unitario_prdep", tab_detalle_presupuesto_anterior.getValor(i, "costo_unitario_prdep"));
            tab_detalle_presupuesto.setValor("total_prdep", tab_detalle_presupuesto_anterior.getValor(i, "total_prdep"));
            tab_detalle_presupuesto.setValor("observacion_prdep", tab_detalle_presupuesto_anterior.getValor(i, "observacion_prdep"));
            tab_detalle_presupuesto.setValor("ide_prtpr", tab_detalle_presupuesto_anterior.getValor(i, "ide_prtpr"));
            //tab_detalle_presupuesto.setValor("ide_anterior_prdep", tab_detalle_presupuesto_anterior.getValor(i, "ide_prdep"));
            TablaGenerica tab_ide_prdpr = utilitario.consultar("SELECT * FROM pre_descripcion_presu dp left join pre_cab_plan_presu cpp on cpp.ide_prcppr=dp.ide_prcppr WHERE cpp.num_modificacion_prcppr=" + nummax + " and ide_anterior_prdpr=" + tab_detalle_presupuesto_anterior.getValor(i, "ide_prdpr"));
            if (tab_ide_prdpr.getTotalFilas() > 0) {
                tab_detalle_presupuesto.setValor("ide_prdpr", tab_ide_prdpr.getValor(0, "ide_prdpr"));
            }
        }
        tab_detalle_presupuesto.guardar();
        utilitario.getConexion().guardarPantalla();
    }

    public void cambiarEstadoPresupuesto() {

        System.out.print("Si ingresa al metodo cambiarEstadoPresupuesto");
        if (dia_cambiar_estado.isVisible()) {
            if (validarCambiarPlanPresupuesto()) {
                System.out.print("actualizar la cabecera del periodo presupuesto aprobado");
                getConexion().agregarSqlPantalla("UPDATE pre_periodo_presu SET aprobado_prppr=TRUE where ide_prppr=" + com_periodo_presupuesto.getValue() + " and ide_empr=" + utilitario.getVariable("ide_empr"));
                getConexion().guardarPantalla();
                tab_tabla1.setCondicion("ide_prppr=" + com_periodo_presupuesto.getValue().toString() + " and ide_georg=" + com_organigrama.getValue() + " and es_activo_prcppr=true");
                chk_aprobado.setValue(true);
                chk_aprobado.setDisabled(true);
                bot_cambiar_presupuesto.setDisabled(false);
                utilitario.addUpdate("chk_aprobado");
                tab_tabla1.getColumna("ide_gemes").setLectura(true);
                tab_tabla1.getColumna("ide_cndpc").setLectura(true);
                tab_tabla1.getColumna("ide_usua").setLectura(true);
                tab_tabla1.getColumna("ide_prepp").setLectura(true);
                tab_tabla1.getColumna("nombre_prcppr").setLectura(true);
                tab_tabla1.getColumna("observacion_prcppr").setLectura(true);
                tab_tabla1.getColumna("fecha_trans_prcppr").setLectura(true);
                tab_tabla2.getColumna("ide_prtrp").setLectura(true);
                tab_tabla2.getColumna("ide_geubi").setLectura(true);
                tab_tabla2.getColumna("observacion_prdpr").setLectura(true);
                tab_tabla3.getColumna("ide_prtap").setLectura(true);
                tab_tabla3.getColumna("ide_cndpc").setLectura(true);
                tab_tabla3.getColumna("nombre_prdep").setLectura(true);
                tab_tabla3.getColumna("observacion_prdep").setLectura(true);
                tab_tabla3.getColumna("total_prdep").setLectura(true);
                tab_tabla4.getColumna("ide_prtpa").setLectura(true);
                tab_tabla4.getColumna("ide_cndpc").setLectura(true);
                tab_tabla4.getColumna("nombre_prdep").setLectura(true);
                tab_tabla4.getColumna("observacion_prdep").setLectura(true);
                tab_tabla4.getColumna("num_prdep").setLectura(true);
                tab_tabla4.getColumna("veces_prdep").setLectura(true);
                tab_tabla4.getColumna("costo_unitario_prdep").setLectura(true);
                tab_tabla4.getColumna("total_prdep").setLectura(true);
                tab_tabla1.ejecutarSql();
                tab_tabla2.ejecutarValorForanea(tab_tabla1.getValor("ide_prcppr"));
                tab_tabla3.ejecutarValorForanea(tab_tabla2.getValor("ide_prdpr"));
                tab_tabla4.setSql("SELECT ide_prdep as ide_gasto,ide_prdpr,ide_prtpa,ide_cndpc,nombre_prdep,num_prdep,veces_prdep,costo_unitario_prdep,total_prdep,observacion_prdep  "
                        + "FROM  pre_detalle_presu WHERE ide_prdep=" + tab_tabla2.getValor("ide_prdpr"));
                utilitario.addUpdate("chk_aprobado,tab_tabla1,tab_tabla2,tab_tabla3,tab_tabla4,bot_cambiar_presupuesto");
            } else {
                chk_aprobado.setValue(false);
                chk_aprobado.setDisabled(false);
                bot_cambiar_presupuesto.setDisabled(true);
                utilitario.addUpdate("chk_aprobado,bot_cambiar_presupuesto");
            }
        } else {
//                if (com_periodo_presupuesto.getValue() != null && !com_periodo_presupuesto.getValue().toString().isEmpty()) {
            if (tab_tabla1.getTotalFilas() > 0) {
                dia_cambiar_estado.dibujar();
            } else {
                utilitario.agregarMensajeInfo("No se  puede cambiar el estado", "Debe seleccionar un periodo y un organigrama");
                chk_aprobado.setValue(false);
                utilitario.addUpdate("chk_aprobado");
            }
        }
    }

    public boolean validarCambiarPlanPresupuesto() {
        System.out.print("Si ingresa validar cambio presupuesto");
        TablaGenerica tab_estado = utilitario.consultar("SELECT ide_prcppr,estado.nombre_prepp FROM pre_cab_plan_presu cab "
                + "LEFT JOIN pre_estado_plan_presu estado ON cab.ide_prepp=estado.ide_prepp "
                + "WHERE estado.ide_prepp=" + utilitario.getVariable("p_pre_estado_en_tramite") + ""
                + "AND cab.ide_empr=" + utilitario.getVariable("ide_empr") + ""
                + "AND cab.ide_sucu=" + utilitario.getVariable("ide_sucu") + "");
        if (tab_estado.getTotalFilas() == 0) {
            dia_cambiar_estado.cerrar();
            return true;
        } else {
            utilitario.agregarMensajeInfo("No puede aprobar el presupuesto", "Tiene estado en tramite en la cabecera");
        }
        return false;
    }

//    public boolean validar() {
//        if (tab_tabla1.getValor("ide_cndpc") == null || tab_tabla1.getValor("ide_cndpc").isEmpty()) {
//            utilitario.agregarMensajeError("No se pudo guardar", "Debe ingresar una cuenta asociada en la cabecera");
//            return false;
//        }
//        if (tab_tabla1.getValor("ide_prepp") == null || tab_tabla1.getValor("ide_prepp").isEmpty()) {
//            utilitario.agregarMensajeError("No se pudo guardar", "Debe seleccionar el Estado del Presupuesto");
//            return false;--
//        }
//        if (tab_tabla1.getValor("nombre_prcppr") == null || tab_tabla1.getValor("nombre_prcppr").isEmpty()) {
//            utilitario.agregarMensajeError("No se pudo guardar", "Debe ingresar el nombre en la cabecera");
//            return false;
//        }
//        if (tab_tabla1.getValor("observacion_prcppr") == null || tab_tabla1.getValor("observacion_prcppr").isEmpty()) {
//            utilitario.agregarMensajeError("No se pudo guardar", "Debe ingresar una observación en la cabecera");
//            return false;
//        }
//        if (tab_tabla2.getValor("ide_prtrp") == null || tab_tabla2.getValor("ide_prtrp").isEmpty()) {
//            utilitario.agregarMensajeError("No se pudo guardar", "Debe seleccionar el tipo de rubro en la descripción");
//            return false;
//        }
//        if (tab_tabla2.getValor("ide_geubi") == null || tab_tabla2.getValor("ide_geubi").isEmpty()) {
//            utilitario.agregarMensajeError("No se pudo guardar", "Debe seleccionar la ubicacion  en la descripción");
//            return false;
//        }
//        if (tab_tabla2.getValor("ide_geubi") == null || tab_tabla2.getValor("ide_geubi").isEmpty()) {
//            utilitario.agregarMensajeError("No se pudo guardar", "Debe seleccionar la ubicacion  en la descripción");
//            return false;
//        }
//        if (tab_tabla2.getValor("ide_prtpr") == null || tab_tabla2.getValor("ide_prtpr").isEmpty()) {
//            utilitario.agregarMensajeError("No se pudo guardar", "Debe seleccionar el tipo de presupuesto  en la descripción");
//            return false;
//        }
//        if (tab_tabla2.getValor("ide_georg") == null || tab_tabla2.getValor("ide_georg").isEmpty()) {
//            utilitario.agregarMensajeError("No se pudo guardar", "Debe seleccionar el organigrama  en la descripción");
//            return false;
//        }
//        if (tab_tabla2.getValor("observacion_prdpr") == null || tab_tabla2.getValor("observacion_prdpr").isEmpty()) {
//            utilitario.agregarMensajeError("No se pudo guardar", "Debe ingresar una observación  en la descripción");
//            return false;
//        }
//        if (tab_tabla3.getValor("ide_prtap") == null || tab_tabla3.getValor("ide_prtap").isEmpty()) {
//            utilitario.agregarMensajeError("No se pudo guardar", "Debe seleccionar el tipo de aporte");
//            return false;
//        }
//        if (tab_tabla3.getValor("ide_prtpa") == null || tab_tabla3.getValor("ide_prtpa").isEmpty()) {
//            utilitario.agregarMensajeError("No se pudo guardar", "Debe seleccionar el tipo de partcicipante");
//            return false;
//        }
//        if (tab_tabla3.getValor("ide_cndpc") == null || tab_tabla3.getValor("ide_cndpc").isEmpty()) {
//            utilitario.agregarMensajeError("No se pudo guardar", "Debe ingresar una cuenta asociada al gasto o al ingreso");
//            return false;
//        }
//        if (tab_tabla3.getValor("nombre_prdep") == null || tab_tabla3.getValor("nombre_prdep").isEmpty()) {
//            utilitario.agregarMensajeError("No se pudo guardar", "Debe ingresar un nombre en el detalle");
//            return false;
//        }
//        if (tab_tabla3.getValor("num_prdep") == null || tab_tabla3.getValor("num_prdep").isEmpty()) {
//            utilitario.agregarMensajeError("No se pudo guardar", "Debe ingresar el numero de participantes en el detalle");
//            return false;
//        }
//        if (tab_tabla3.getValor("veces_prdep") == null || tab_tabla3.getValor("veces_prdep").isEmpty()) {
//            utilitario.agregarMensajeError("No se pudo guardar", "Debe ingresar el numero de veces en el detalle");
//            return false;
//        }
//        if (tab_tabla3.getValor("costo_unitario_prdep") == null || tab_tabla3.getValor("costo_unitario_prdep").isEmpty()) {
//            utilitario.agregarMensajeError("No se pudo guardar", "Debe ingresar el costo unitario  en el detalle");
//            return false;
//        }
//        if (tab_tabla3.getValor("observacion_prdep") == null || tab_tabla3.getValor("observacion_prdep").isEmpty()) {
//            utilitario.agregarMensajeError("No se pudo guardar", "Debe ingresar una observación en el detalle");
//            return false;
//        }
//        return true;
//    }
    @Override
    public void insertar() {
        if (com_periodo_presupuesto.getValue() != null) {
            if (com_organigrama.getValue() != null) {
                if (tab_tabla1.isFocus()) {
                    tab_tabla1.insertar();
                    tab_tabla1.setValor("ide_prppr", com_periodo_presupuesto.getValue().toString());
                    tab_tabla1.setValor("ide_georg", com_organigrama.getValue().toString());
                    tab_tabla1.setValor("num_modificacion_prcppr", nummax + "");
                } else if (tab_tabla2.isFocus()) {
                    if (!tab_tabla1.isFilaInsertada()) {
                        tab_tabla2.insertar();
                    } else {
                        utilitario.agregarMensajeInfo("No se puede Insertar", "Debe guardar primero la transacción que esta tabajando");
                    }
                } else if (tab_tabla3.isFocus()) {
                    if (!tab_tabla2.isFilaInsertada()) {
                        if (!tab_tabla3.isFilaInsertada()) {
                            tab_tabla3.insertar();
                            calcula_total_ingresos();
                        } else {
                            utilitario.agregarMensajeInfo("No se puede Insertar", "Debe guardar primero la transacción que esta tabajando");
                        }
                    } else {
                        utilitario.agregarMensajeInfo("No se puede Insertar", "Debe guardar primero la transacción que esta tabajando");
                    }
                } else if (tab_tabla4.isFocus()) {
                    if (!tab_tabla2.isFilaInsertada()) {
                        if (!tab_tabla4.isFilaInsertada()) {
                            tab_tabla4.insertar();
                            calcula_total_ingresos();
                        } else {
                            utilitario.agregarMensajeInfo("No se puede Insertar", "Debe guardar primero la transacción que esta tabajando");
                        }
                    } else {
                        utilitario.agregarMensajeInfo("No se puede Insertar", "Debe guardar primero la transacción que esta tabajando");
                    }
                }
            } else {
                utilitario.agregarMensajeInfo("Organigrama", "Debe escoger un Organigrama");
            }
        } else {
            utilitario.agregarMensajeInfo("Presupuesto", "Debe escoger un Periodo Presupuestario");
        }
    }

    public void guardarTablaGatos() {
        Tabla tab_gastos = new Tabla();
        tab_gastos.setTabla("pre_detalle_presu", "ide_prdep", -1);
        tab_gastos.setCondicion("ide_prdep=-1");
        tab_gastos.ejecutarSql();
        for (int i = 0; i < tab_tabla4.getTotalFilas(); i++) {
            if (tab_tabla4.isFilaInsertada(i)) {
                tab_gastos.insertar();
                tab_gastos.setValor("ide_prtpr", utilitario.getVariable("p_pre_tipo_presu_gasto"));
                tab_gastos.setValor("ide_empr", utilitario.getVariable("ide_empr"));
                tab_gastos.setValor("ide_sucu", utilitario.getVariable("ide_sucu"));
                tab_gastos.setValor("ide_prdpr", tab_tabla4.getValor(i, "ide_prdpr"));
                tab_gastos.setValor("ide_prtpa", tab_tabla4.getValor(i, "ide_prtpa"));
                tab_gastos.setValor("ide_cndpc", tab_tabla4.getValor(i, "ide_cndpc"));
                tab_gastos.setValor("nombre_prdep", tab_tabla4.getValor(i, "nombre_prdep"));
                tab_gastos.setValor("num_prdep", tab_tabla4.getValor(i, "num_prdep"));
                tab_gastos.setValor("veces_prdep", tab_tabla4.getValor(i, "veces_prdep"));
                tab_gastos.setValor("costo_unitario_prdep", tab_tabla4.getValor(i, "costo_unitario_prdep"));
                tab_gastos.setValor("total_prdep", tab_tabla4.getValor(i, "total_prdep"));
                tab_gastos.setValor("observacion_prdep", tab_tabla4.getValor(i, "observacion_prdep"));
            } else if (tab_tabla4.isFilaModificada(i)) {
                utilitario.getConexion().agregarSqlPantalla("UPDATE pre_detalle_presu "
                        + "SET ide_prtpa=" + tab_tabla4.getValor(i, "ide_prtpa") + " ,"
                        + "ide_cndpc=" + tab_tabla4.getValor(i, "ide_cndpc") + ", "
                        + "nombre_prdep='" + tab_tabla4.getValor(i, "nombre_prdep") + "', "
                        + "num_prdep=" + tab_tabla4.getValor(i, "num_prdep") + ","
                        + "veces_prdep=" + tab_tabla4.getValor(i, "veces_prdep") + ","
                        + "costo_unitario_prdep=" + tab_tabla4.getValor(i, "costo_unitario_prdep") + ","
                        + "total_prdep=" + tab_tabla4.getValor(i, "total_prdep") + ","
                        + "observacion_prdep='" + tab_tabla4.getValor(i, "observacion_prdep") + "' "
                        + "WHERE ide_prdep=" + tab_tabla4.getValor(i, "ide_prdep") + "");
            }
        }
        tab_gastos.guardar();
    }

    @Override
    public void guardar() {

        //if (validar()) {
        if (tab_tabla1.isFocus()) {
            tab_tabla1.guardar();
        } else if (tab_tabla2.isFocus()) {
            tab_tabla2.guardar();
        } else if (tab_tabla3.isFocus()) {
            tab_tabla3.guardar();
        } else if (tab_tabla4.isFocus()) {
            guardarTablaGatos();
        }
        utilitario.getConexion().guardarPantalla();
        tab_tabla4.setSql("SELECT ide_prdep as ide_gasto,ide_prdpr,ide_prtpa,ide_cndpc,nombre_prdep,num_prdep,veces_prdep,costo_unitario_prdep,total_prdep,observacion_prdep  "
                + "FROM  pre_detalle_presu WHERE ide_empr=" + utilitario.getVariable("ide_empr") + " and ide_sucu=" + utilitario.getVariable("ide_sucu") + " and ide_prdpr=" + tab_tabla2.getValor("ide_prdpr") + " and ide_prtpr=" + utilitario.getVariable("p_pre_tipo_presu_gasto"));
        tab_tabla4.ejecutarSql();
        utilitario.addUpdate("tab_tabla4");
//}
    }

    public void eliminarTablaGastos() {
        if (!tab_tabla4.isFilaInsertada()) {
            utilitario.getConexion().agregarSqlPantalla("DELETE FROM pre_detalle_presu WHERE ide_prdep=" + tab_tabla4.getValor("ide_gasto"));
        } else {
            tab_tabla4.eliminar();
        }
    }

    @Override
    public void eliminar() {
        if (tab_tabla1.isFocus()) {
            tab_tabla1.eliminar();
        } else if (tab_tabla2.isFocus()) {
            tab_tabla2.eliminar();
        } else if (tab_tabla3.isFocus()) {
            tab_tabla3.eliminar();
        } else if (tab_tabla4.isFocus()) {
            eliminarTablaGastos();
        }
        utilitario.getConexion().guardarPantalla();
        tab_tabla4.setSql("SELECT ide_prdep as ide_gasto,ide_prdpr,ide_prtpa,ide_cndpc,nombre_prdep,num_prdep,veces_prdep,costo_unitario_prdep,total_prdep,observacion_prdep  "
                + "FROM  pre_detalle_presu WHERE ide_empr=" + utilitario.getVariable("ide_empr") + " and ide_sucu=" + utilitario.getVariable("ide_sucu") + " and ide_prdpr=" + tab_tabla2.getValor("ide_prdpr") + " and ide_prtpr=" + utilitario.getVariable("p_pre_tipo_presu_gasto"));
        tab_tabla4.ejecutarSql();
        utilitario.addUpdate("tab_tabla4");
    }

    public void obtener_total_detalle_presu(AjaxBehaviorEvent evt) {
        System.out.print("Si ingresa al metodo obtener detalle de presupuesto");
        tab_tabla4.modificar(evt);
        System.out.print("Sii ingresa al calcular");
        double num_prdep = 0;
        double veces = 0;
        double costo_unitario = 0;
        double total = 0;
        if (tab_tabla4.getValor(tab_tabla4.getFilaActual(), "num_prdep") != null && !tab_tabla4.getValor(tab_tabla4.getFilaActual(), "num_prdep").isEmpty()) {
            try {
                num_prdep = Double.parseDouble(tab_tabla4.getValor(tab_tabla4.getFilaActual(), "num_prdep"));
            } catch (Exception e) {
                num_prdep = 0;
            }
        }
        if (tab_tabla4.getValor(tab_tabla4.getFilaActual(), "veces_prdep") != null && !tab_tabla4.getValor(tab_tabla4.getFilaActual(), "veces_prdep").isEmpty()) {
            try {
                veces = Double.parseDouble(tab_tabla4.getValor(tab_tabla4.getFilaActual(), "veces_prdep"));
            } catch (Exception e) {
                veces = 0;
            }
        }
        if (tab_tabla4.getValor(tab_tabla4.getFilaActual(), "costo_unitario_prdep") != null && !tab_tabla4.getValor(tab_tabla4.getFilaActual(), "costo_unitario_prdep").isEmpty()) {
            try {
                costo_unitario = Double.parseDouble(tab_tabla4.getValor(tab_tabla4.getFilaActual(), "costo_unitario_prdep"));
            } catch (Exception e) {
                costo_unitario = 0;
            }
        }
        total = num_prdep * veces * costo_unitario;
        tab_tabla4.setValor(tab_tabla4.getFilaActual(), "total_prdep", utilitario.getFormatoNumero(total, 2));
        utilitario.addUpdateTabla(tab_tabla4, "total_prdep", "");
        calcula_total_ingresos();
    }

    public void seleccionarOrganigrama() {
        if (com_periodo_presupuesto.getValue() != null) {
            if (com_organigrama.getValue() != null) {
                tab_tabla1.setCondicion("ide_prppr=" + com_periodo_presupuesto.getValue().toString() + " and ide_georg=" + com_organigrama.getValue() + " and es_activo_prcppr=true");
                if (chk_aprobado.getValue().equals("false")) {
                    chk_aprobado.setDisabled(false);
                    utilitario.addUpdate("chk_aprobado");
                    tab_tabla1.getColumna("ide_gemes").setLectura(false);
                    tab_tabla1.getColumna("ide_cndpc").setLectura(false);
                    tab_tabla1.getColumna("ide_usua").setLectura(false);
                    tab_tabla1.getColumna("ide_prepp").setLectura(false);
                    tab_tabla1.getColumna("nombre_prcppr").setLectura(false);
                    tab_tabla1.getColumna("observacion_prcppr").setLectura(false);
                    tab_tabla1.getColumna("fecha_trans_prcppr").setLectura(false);
                    tab_tabla2.getColumna("ide_prtrp").setLectura(false);
                    tab_tabla2.getColumna("ide_geubi").setLectura(false);
                    tab_tabla2.getColumna("observacion_prdpr").setLectura(false);
                    tab_tabla3.getColumna("ide_prtap").setLectura(false);
                    tab_tabla3.getColumna("ide_cndpc").setLectura(false);
                    tab_tabla3.getColumna("nombre_prdep").setLectura(false);
                    tab_tabla3.getColumna("observacion_prdep").setLectura(false);
                    tab_tabla3.getColumna("total_prdep").setLectura(false);
                    tab_tabla4.getColumna("ide_prtpa").setLectura(false);
                    tab_tabla4.getColumna("ide_cndpc").setLectura(false);
                    tab_tabla4.getColumna("nombre_prdep").setLectura(false);
                    tab_tabla4.getColumna("observacion_prdep").setLectura(false);
                    tab_tabla4.getColumna("num_prdep").setLectura(false);
                    tab_tabla4.getColumna("veces_prdep").setLectura(false);
                    tab_tabla4.getColumna("costo_unitario_prdep").setLectura(false);
                    tab_tabla4.getColumna("total_prdep").setLectura(false);
                } else {
                    bot_cambiar_presupuesto.setDisabled(false);
                    utilitario.addUpdate("bot_cambiar_presupuesto");
                    tab_tabla1.getColumna("ide_gemes").setLectura(true);
                    tab_tabla1.getColumna("ide_cndpc").setLectura(true);
                    tab_tabla1.getColumna("ide_usua").setLectura(true);
                    tab_tabla1.getColumna("ide_prepp").setLectura(true);
                    tab_tabla1.getColumna("nombre_prcppr").setLectura(true);
                    tab_tabla1.getColumna("observacion_prcppr").setLectura(true);
                    tab_tabla1.getColumna("fecha_trans_prcppr").setLectura(true);
                    tab_tabla2.getColumna("ide_prtrp").setLectura(true);
                    tab_tabla2.getColumna("ide_geubi").setLectura(true);
                    tab_tabla2.getColumna("observacion_prdpr").setLectura(true);
                    tab_tabla3.getColumna("ide_prtap").setLectura(true);
                    tab_tabla3.getColumna("ide_cndpc").setLectura(true);
                    tab_tabla3.getColumna("nombre_prdep").setLectura(true);
                    tab_tabla3.getColumna("observacion_prdep").setLectura(true);
                    tab_tabla3.getColumna("total_prdep").setLectura(true);
                    tab_tabla4.getColumna("ide_prtpa").setLectura(true);
                    tab_tabla4.getColumna("ide_cndpc").setLectura(true);
                    tab_tabla4.getColumna("nombre_prdep").setLectura(true);
                    tab_tabla4.getColumna("observacion_prdep").setLectura(true);
                    tab_tabla4.getColumna("num_prdep").setLectura(true);
                    tab_tabla4.getColumna("veces_prdep").setLectura(true);
                    tab_tabla4.getColumna("costo_unitario_prdep").setLectura(true);
                    tab_tabla4.getColumna("total_prdep").setLectura(true);
                }
                tab_tabla1.ejecutarSql();
                tab_tabla2.ejecutarValorForanea(tab_tabla1.getValor("ide_prcppr"));
                tab_tabla3.ejecutarValorForanea(tab_tabla2.getValor("ide_prdpr"));
                tab_tabla4.setSql("SELECT ide_prdep as ide_gasto,ide_prdpr,ide_prtpa,ide_cndpc,nombre_prdep,num_prdep,veces_prdep,costo_unitario_prdep,total_prdep,observacion_prdep  "
                        + "FROM  pre_detalle_presu WHERE ide_empr=" + utilitario.getVariable("ide_empr") + " and ide_sucu=" + utilitario.getVariable("ide_sucu") + " and ide_prdpr=" + tab_tabla2.getValor("ide_prdpr") + " and ide_prtpr=" + utilitario.getVariable("p_pre_tipo_presu_gasto"));
                tab_tabla4.ejecutarSql();
                utilitario.addUpdate("tab_tabla1,tab_tabla2,tab_tabla3,tab_tabla4");

                calcula_totales_por_plan_presupuestario();
                calcula_totales();
                calcula_total_organigrama();
            } else {
                tab_tabla1.setCondicion("ide_prcppr=-1");
                tab_tabla1.ejecutarSql();
                tab_tabla2.ejecutarValorForanea(tab_tabla1.getValor("ide_prcppr"));
                tab_tabla3.ejecutarValorForanea(tab_tabla2.getValor("ide_prdpr"));
                tab_tabla4.setSql("SELECT ide_prdep as ide_gasto,ide_prdpr,ide_prtpa,ide_cndpc,nombre_prdep,num_prdep,veces_prdep,costo_unitario_prdep,total_prdep,observacion_prdep  "
                        + "FROM  pre_detalle_presu WHERE ide_empr=" + utilitario.getVariable("ide_empr") + " and ide_sucu=" + utilitario.getVariable("ide_sucu") + " and ide_prdpr=" + tab_tabla2.getValor("ide_prdpr") + " and ide_prtpr=" + utilitario.getVariable("p_pre_tipo_presu_gasto"));
                tab_tabla4.ejecutarSql();
                utilitario.addUpdate("tab_tabla1,tab_tabla2,tab_tabla3,tab_tabla4");
                calcula_totales_por_plan_presupuestario();
                calcula_totales();
                calcula_total_organigrama();
            }
        } else {
            tab_tabla1.setCondicion("ide_prcppr=-1");
            tab_tabla1.ejecutarSql();
            tab_tabla2.ejecutarValorForanea(tab_tabla1.getValor("ide_prcppr"));
            tab_tabla3.ejecutarValorForanea(tab_tabla2.getValor("ide_prdpr"));
            tab_tabla4.setSql("SELECT ide_prdep as ide_gasto,ide_prdpr,ide_prtpa,ide_cndpc,nombre_prdep,num_prdep,veces_prdep,costo_unitario_prdep,total_prdep,observacion_prdep  "
                    + "FROM  pre_detalle_presu WHERE ide_empr=" + utilitario.getVariable("ide_empr") + " and ide_sucu=" + utilitario.getVariable("ide_sucu") + " and ide_prdpr=" + tab_tabla2.getValor("ide_prdpr") + " and ide_prtpr=" + utilitario.getVariable("p_pre_tipo_presu_gasto"));
            tab_tabla4.ejecutarSql();
            utilitario.addUpdate("tab_tabla1,tab_tabla2,tab_tabla3,tab_tabla4");
            calcula_totales_por_plan_presupuestario();
            calcula_totales();
            calcula_total_organigrama();
        }
        calcula_total_organigrama();
    }

    public void seleccionar_periodo_presupuesto() {
        System.out.print("Resultado: " + com_periodo_presupuesto.getValue());
        if (com_periodo_presupuesto.getValue() != null) {
            System.out.println("combo de periodo.....");
            TablaGenerica tab_periodo_presupuesto = utilitario.consultar("SELECT * FROM pre_periodo_presu  where ide_prppr=" + com_periodo_presupuesto.getValue() + " and ide_empr=" + utilitario.getVariable("ide_empr"));
            System.out.println("total de filas.." + tab_periodo_presupuesto.getTotalFilas());
            if (tab_periodo_presupuesto.getTotalFilas() > 0) {
                System.out.println("valor del checkkk..." + tab_periodo_presupuesto.getValor("aprobado_prppr"));
                chk_aprobado.setValue(tab_periodo_presupuesto.getValor("aprobado_prppr"));
                if (tab_periodo_presupuesto.getValor("aprobado_prppr").equals("true")) {
                    bot_cambiar_presupuesto.setDisabled(false);
                    utilitario.addUpdate("bot_cambiar_presupuesto");
                }
                utilitario.addUpdate("chk_aprobado");
            }
            if (com_organigrama.getValue() != null) {
                tab_tabla1.setCondicion("ide_prppr=" + com_periodo_presupuesto.getValue().toString() + " and ide_georg=" + com_organigrama.getValue() + " and es_activo_prcppr=true");
                tab_tabla1.ejecutarSql();
                tab_tabla2.ejecutarValorForanea(tab_tabla1.getValor("ide_prcppr"));
                tab_tabla3.ejecutarValorForanea(tab_tabla2.getValor("ide_prdpr"));
                tab_tabla4.setSql("SELECT ide_prdep as ide_gasto,ide_prdpr,ide_prtpa,ide_cndpc,nombre_prdep,num_prdep,veces_prdep,costo_unitario_prdep,total_prdep,observacion_prdep  "
                        + "FROM  pre_detalle_presu WHERE ide_empr=" + utilitario.getVariable("ide_empr") + " and ide_sucu=" + utilitario.getVariable("ide_sucu") + " and ide_prdpr=" + tab_tabla2.getValor("ide_prdpr") + " and ide_prtpr=" + utilitario.getVariable("p_pre_tipo_presu_gasto"));
                tab_tabla4.ejecutarSql();
                utilitario.addUpdate("tab_tabla1,tab_tabla2,tab_tabla3,tab_tabla4");
            } else {
                tab_tabla1.setCondicion("ide_prcppr=-1");
                tab_tabla1.ejecutarSql();
                tab_tabla2.ejecutarValorForanea(tab_tabla1.getValor("ide_prcppr"));
                tab_tabla3.ejecutarValorForanea(tab_tabla2.getValor("ide_prdpr"));
                tab_tabla4.setSql("SELECT ide_prdep as ide_gasto,ide_prdpr,ide_prtpa,ide_cndpc,nombre_prdep,num_prdep,veces_prdep,costo_unitario_prdep,total_prdep,observacion_prdep  "
                        + "FROM  pre_detalle_presu WHERE ide_empr=" + utilitario.getVariable("ide_empr") + " and ide_sucu=" + utilitario.getVariable("ide_sucu") + " and ide_prdpr=" + tab_tabla2.getValor("ide_prdpr") + " and ide_prtpr=" + utilitario.getVariable("p_pre_tipo_presu_gasto"));
                tab_tabla4.ejecutarSql();
                utilitario.addUpdate("tab_tabla1,tab_tabla2,tab_tabla3,tab_tabla4");
            }
        } else {
            tab_tabla1.setCondicion("ide_prcppr=-1");
            tab_tabla1.ejecutarSql();
            tab_tabla2.ejecutarValorForanea(tab_tabla1.getValor("ide_prcppr"));
            tab_tabla3.ejecutarValorForanea(tab_tabla2.getValor("ide_prdpr"));
            tab_tabla4.setSql("SELECT ide_prdep as ide_gasto,ide_prdpr,ide_prtpa,ide_cndpc,nombre_prdep,num_prdep,veces_prdep,costo_unitario_prdep,total_prdep,observacion_prdep  "
                    + "FROM  pre_detalle_presu WHERE ide_empr=" + utilitario.getVariable("ide_empr") + " and ide_sucu=" + utilitario.getVariable("ide_sucu") + " and ide_prdpr=" + tab_tabla2.getValor("ide_prdpr") + " and ide_prtpr=" + utilitario.getVariable("p_pre_tipo_presu_gasto"));
            tab_tabla4.ejecutarSql();
            utilitario.addUpdate("tab_tabla1,tab_tabla2,tab_tabla3,tab_tabla4");
        }
        calcula_total_organigrama();
    }

    public double obtenerTotalDetallePresupuesto(String ide_prdpr) {
        TablaGenerica tab_detalle_presupuesto = utilitario.consultar("SELECT * from pre_detalle_presu WHERE ide_prdpr=" + ide_prdpr);
        if (tab_detalle_presupuesto.getTotalFilas() > 0) {
            return tab_detalle_presupuesto.getSumaColumna("total_prdep");
        } else {
            return 0;
        }
    }

    public int obtenerSignoTipoPresupuesto(String ide_prtpr) {
        List lis_signo = utilitario.getConexion().consultar("SELECT signo_prtpr FROM pre_tipo_presupuesto WHERE ide_empr=" + utilitario.getVariable("ide_empr") + " AND ide_prtpr=" + ide_prtpr);
        if (lis_signo != null) {
            return Integer.parseInt(lis_signo.get(0).toString());
        } else {
            return 0;
        }
    }
    Map parametro = new HashMap();

    @Override
    public void aceptarReporte() {
        System.out.println("si funciona metodo");
        if (rep_reporte.getReporteSelecionado().equals("Cartera Presupuestaria")) { //si
            if (rep_reporte.isVisible()) {//si
                System.out.println("si es visible");
                parametro = new HashMap();
                rep_reporte.cerrar();//cierra
                sel_tab.dibujar();
                utilitario.addUpdate("rep_reporte,sel_tab");
            } else if (sel_tab.isVisible()) {
                if (sel_tab.getSeleccionados() != null && !sel_tab.getSeleccionados().isEmpty()) {
                    parametro.put("ide_georg", sel_tab.getSeleccionados());
                    sel_tab.cerrar();
                    parametro.put("ide_prtpr_ingreso", Long.parseLong(utilitario.getVariable("p_pre_tipo_presu_ingreso")));
                    parametro.put("ide_prtpr_gasto", Long.parseLong(utilitario.getVariable("p_pre_tipo_presu_gasto")));
                    sel_tab_periodo.dibujar();
                    utilitario.addUpdate("sel_tab,sel_tab_periodo");
                    System.out.println("carteras:  " + sel_tab.getSeleccionados());
                    System.out.println("tipo ingreso:  " + utilitario.getVariable("p_pre_tipo_presu_ingreso"));
                    System.out.println("tipo gasto:  " + utilitario.getVariable("p_pre_tipo_presu_gasto"));
                } else {
                    utilitario.agregarMensajeError("Error de Selección ", "Debe seleccionar al menos una Opción");
                }
            } else if (sel_tab_periodo.isVisible()) {
                if (sel_tab_periodo.getSeleccionados() != null && !sel_tab_periodo.getSeleccionados().isEmpty()) {
                    parametro.put("ide_prppr", sel_tab_periodo.getSeleccionados());//lista sel                     
                    sel_tab_periodo.cerrar();
                    sel_tab_modificacion.dibujar();
                    utilitario.addUpdate("sel_tab_periodo,sel_tab_modificacion");
                    System.out.println("periodo:  " + sel_tab_periodo.getSeleccionados());
                } else {
                    utilitario.agregarMensajeError("Error de Selección ", "Debe seleccionar al menos un Periodo");
                }
            } else if (sel_tab_modificacion.isVisible()) {
                if (sel_tab_modificacion.isVisible()) {
                    parametro.put("num_modificacion_prcppr", sel_tab_modificacion.getSeleccionados());//lista sel                     
                    sel_tab_modificacion.cerrar();
                    sel_tab_mes.dibujar();
                    utilitario.addUpdate("sel_tab_modificacion,sel_tab_mes");
                    System.out.println("modificacion:  " + sel_tab_modificacion.getSeleccionados());
                } else {
                    utilitario.agregarMensajeError("Error de Selección ", "Debe seleccionar al menos un Periodo");
                }
            } else if (sel_tab_mes.isVisible()) {
                if (sel_tab_mes.getSeleccionados() != null) {
                    if (sel_tab_mes.getSeleccionados() != null && !sel_tab_mes.getSeleccionados().isEmpty()) {
                        sel_tab_mes.cerrar();
                        parametro.put("ide_gemes", sel_tab_mes.getSeleccionados());//lista sel                     
                        System.out.println("mes:  " + sel_tab_mes.getSeleccionados());
                        sel_rep.setSeleccionFormatoReporte(parametro, rep_reporte.getPath());
                        sel_rep.dibujar();
                        utilitario.addUpdate("sel_tab_mes,sel_rep");
                    } else {
                        utilitario.agregarMensajeError("Error de Selección ", "Debe seleccionar al menos un Mes");
                    }
                }
            }

        } else if (rep_reporte.getReporteSelecionado().equals("Resumen Presupuestario Anual")) {//anual
            if (rep_reporte.isVisible()) {
                System.out.println("si ingresa al a la cartera y al presupuesto anual");
                parametro = new HashMap();
                rep_reporte.cerrar();
                sel_tab.dibujar();
                utilitario.addUpdate("rep_reporte,sel_tab");
            } else if (sel_tab.isVisible()) {
                if (sel_tab.getSeleccionados() != null && !sel_tab.getSeleccionados().isEmpty()) {
                    parametro.put("ide_georg", sel_tab.getSeleccionados());//lista sel              
                    System.out.println("ide_georgxxx  " + sel_tab.getSeleccionados());
                    sel_tab.cerrar();
                    sel_tab_periodo.dibujar();
                    utilitario.addUpdate("sel_tab,sel_tab_periodo");
                } else {
                    utilitario.agregarMensajeError("Error de Selección ", "Debe seleccionar al menos una Opción");
                }
            } else if (sel_tab_periodo.isVisible()) {
                if (sel_tab_periodo.getSeleccionados() != null && !sel_tab_periodo.getSeleccionados().isEmpty()) {
                    parametro.put("ide_prppr", sel_tab_periodo.getSeleccionados());
                    System.out.println("ide_prppr++   " + sel_tab_periodo.getSeleccionados());
                    sel_tab_periodo.cerrar();
                    sel_rep.setSeleccionFormatoReporte(parametro, rep_reporte.getPath());
                    sel_rep.dibujar();
                    utilitario.addUpdate("sel_rep,sel_tab_periodo");
                } else {
                    utilitario.agregarMensajeError("Error de Selección ", "Debe seleccionar al menos un Periodo");
                }
            }
        } else if (rep_reporte.getReporteSelecionado().equals("Resumen Presupuestario Mensual")) {//Mensual
            if (rep_reporte.isVisible()) {
                System.out.println("si ingresa al a la cartera y al presupuesto Mensual");
                parametro = new HashMap();
                rep_reporte.cerrar();
                sel_tab.dibujar();
                utilitario.addUpdate("rep_reporte,sel_tab");
            } else if (sel_tab.isVisible()) {
                if (sel_tab.getSeleccionados() != null && !sel_tab.getSeleccionados().isEmpty()) {
                    parametro.put("ide_georg", sel_tab.getSeleccionados());//lista sel      
                    sel_tab.cerrar();
                    System.out.println("geor:xxxxxxxx  " + sel_tab.getSeleccionados());
                    sel_tab_mes.dibujar();
                    utilitario.addUpdate("sel_tab,sel_tab_mes");
                } else {
                    utilitario.agregarMensajeError("Error de Selección ", "Debe seleccionar al menos una Opción");
                }
            } else if (sel_tab_mes.isVisible()) {
                if (sel_tab_mes.getSeleccionados() != null && !sel_tab_mes.getSeleccionados().isEmpty()) {
                    parametro.put("ide_gemes", sel_tab_mes.getSeleccionados());//lista sel   
                    sel_tab_mes.cerrar();
                    System.out.println("mes:xxxxxxxx  " + sel_tab_mes.getSeleccionados());
                    sel_tab_periodo.dibujar();
                    utilitario.addUpdate("sel_tab_mes,sel_tab_periodo");
                } else {
                    utilitario.agregarMensajeError("Error de Selección ", "Debe seleccionar al menos un Mes");
                }
            } else if (sel_tab_periodo.isVisible()) {
                if (sel_tab_periodo.getSeleccionados() != null && !sel_tab_periodo.getSeleccionados().isEmpty()) {
                    parametro.put("ide_prppr", sel_tab_periodo.getSeleccionados());//lista sel   
                    sel_tab_periodo.cerrar();
                    System.out.println("Periodo:xxxxxxxx  " + sel_tab_periodo.getSeleccionados());
                    sel_rep.setSeleccionFormatoReporte(parametro, rep_reporte.getPath());
                    sel_rep.dibujar();
                    utilitario.addUpdate("sel_tab_periodo,sel_rep");
                } else {
                    utilitario.agregarMensajeError("Error de Selección ", "Debe seleccionar al menos un Periodo");
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
                    TablaGenerica tab_detalle_presu = presu.getTablaEjecucionPresupuestaria(sel_tab_periodo_cabecera.getValorSeleccionado(), sec_rango_reporte.getFecha1String(), false);
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

    @Override
    public void abrirListaReportes() {
//Se ejecuta cuando da click en el boton de Reportes de la Barra 
        rep_reporte.dibujar();
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

    public void seleccionar_tabla1(SelectEvent evt) {
        tab_tabla1.seleccionarFila(evt);
        tab_tabla4.setSql("SELECT ide_prdep as ide_gasto,ide_prdpr,ide_prtpa,ide_cndpc,nombre_prdep,num_prdep,veces_prdep,costo_unitario_prdep,total_prdep,observacion_prdep  "
                + "FROM  pre_detalle_presu WHERE ide_empr=" + utilitario.getVariable("ide_empr") + " and ide_sucu=" + utilitario.getVariable("ide_sucu") + " and ide_prdpr=" + tab_tabla2.getValor("ide_prdpr") + " and ide_prtpr=" + utilitario.getVariable("p_pre_tipo_presu_gasto"));
        tab_tabla4.ejecutarSql();
        calcula_totales_por_plan_presupuestario();
    }

    public void calcula_total_organigrama() {
        if (com_periodo_presupuesto.getValue() != null) {
            if (com_organigrama.getValue() != null) {
                TablaGenerica tab_ingreso = utilitario.consultar("SELECT dp.ide_prtap,tap.nombre_prtap,sum(total_prdep) as total FROM pre_cab_plan_presu cpp "
                        + " left join pre_descripcion_presu dpr on dpr.ide_prcppr=cpp.ide_prcppr "
                        + "left join pre_detalle_presu dp on dp.ide_prdpr=dpr.ide_prdpr "
                        + "left join pre_tipo_aporte_presu tap on tap.ide_prtap=dp.ide_prtap "
                        + "left join pre_periodo_presu per on cpp.ide_prppr=per.ide_prppr "
                        + "WHERE cpp.ide_georg=" + com_organigrama.getValue() + " "
                        + "and per.ide_prppr=" + com_periodo_presupuesto.getValue() + " and dp.ide_prtpr=" + utilitario.getVariable("p_pre_tipo_presu_ingreso") + " "
                        + "GROUP BY cpp.ide_prcppr,dp.ide_prtap,dp.ide_prtpr,tap.nombre_prtap ORDER BY nombre_prtap ");
                gri_totales.getChildren().clear();
                List lis_total_org = new ArrayList();
                List lis_ide_prtap = new ArrayList();
                List lis_nombre_prtap = new ArrayList();
                int int_bandera = 0;
                for (int i = 0; i < tab_ingreso.getTotalFilas(); i++) {
                    for (int j = 0; j < lis_ide_prtap.size(); j++) {
                        if (tab_ingreso.getValor(i, "ide_prtap").equals(lis_ide_prtap.get(j))) {
                            int_bandera = 1;
                            break;
                        }
                    }
                    if (int_bandera == 0) {
                        lis_ide_prtap.add(tab_ingreso.getValor(i, "ide_prtap"));
                        lis_nombre_prtap.add(tab_ingreso.getValor(i, "nombre_prtap"));
                    }
                    int_bandera = 0;
                }
                double sum_acumulada = 0;
                for (int i = 0; i < lis_ide_prtap.size(); i++) {
                    for (int j = 0; j < tab_ingreso.getTotalFilas(); j++) {
                        if (lis_ide_prtap.get(i).equals(tab_ingreso.getValor(j, "ide_prtap"))) {
                            sum_acumulada = sum_acumulada + Double.parseDouble(tab_ingreso.getValor(j, "total"));
                        }
                    }
                    lis_total_org.add(sum_acumulada);
                    sum_acumulada = 0;
                }
                for (int i = 0; i < lis_ide_prtap.size(); i++) {
                    System.out.print("lista ide_prat " + lis_ide_prtap.get(i) + " TOTAL " + lis_total_org.get(i));
                    gri_totales.getChildren().add(new Etiqueta(lis_nombre_prtap.get(i) + ""));
                    gri_totales.getChildren().add(new Etiqueta(utilitario.getFormatoNumero(lis_total_org.get(i))));
                }
                if (tab_ingreso.getTotalFilas() > 0) {
                    gri_totales.getChildren().add(new Etiqueta("TOTAL INGRESOS:  "));
                    gri_totales.getChildren().add(new Etiqueta(utilitario.getFormatoNumero(tab_ingreso.getSumaColumna("total"))));
                } else {
                    gri_totales.getChildren().add(new Etiqueta("TOTAL INGRESOS:  "));
                    gri_totales.getChildren().add(new Etiqueta("0.00"));
                }

                TablaGenerica tab_gastos = utilitario.consultar("SELECT dp.ide_prtap,tap.nombre_prtap,sum(total_prdep) as total FROM pre_cab_plan_presu cpp "
                        + "left join pre_descripcion_presu dpr on dpr.ide_prcppr=cpp.ide_prcppr "
                        + "left join pre_detalle_presu dp on dp.ide_prdpr=dpr.ide_prdpr "
                        + "left join pre_tipo_aporte_presu tap on tap.ide_prtap=dp.ide_prtap "
                        + "left join pre_periodo_presu per on cpp.ide_prppr=per.ide_prppr "
                        + "WHERE cpp.ide_georg=" + com_organigrama.getValue() + " "
                        + "and per.ide_prppr=" + com_periodo_presupuesto.getValue() + " and dp.ide_prtpr=" + utilitario.getVariable("p_pre_tipo_presu_gasto") + " "
                        + "GROUP BY cpp.ide_prcppr,dp.ide_prtap,dp.ide_prtpr,tap.nombre_prtap "
                        + "ORDER BY nombre_prtap ");
                if (tab_gastos.getTotalFilas() > 0) {
                    gri_totales.getChildren().add(new Etiqueta("TOTAL GASTOS:  "));
                    gri_totales.getChildren().add(new Etiqueta(utilitario.getFormatoNumero(tab_gastos.getSumaColumna("total"))));
                } else {
                    gri_totales.getChildren().add(new Etiqueta("TOTAL GASTOS:  "));
                    gri_totales.getChildren().add(new Etiqueta("0.00"));
                }
                utilitario.addUpdate("gri_totales");
            } else {
                gri_totales.getChildren().clear();
                TablaGenerica tab_tipo_aporte = utilitario.consultar("SELECT * FROM pre_tipo_aporte_presu WHERE ide_empr=" + utilitario.getVariable("ide_empr"));
                for (int i = 0; i < tab_tipo_aporte.getTotalFilas(); i++) {
                    gri_totales.getChildren().add(new Etiqueta(tab_tipo_aporte.getValor(i, "nombre_prtap")));
                    gri_totales.getChildren().add(new Etiqueta("0.0"));
                }
                utilitario.addUpdate("gri_totales");

            }
        } else {
            gri_totales.getChildren().clear();
            TablaGenerica tab_tipo_aporte = utilitario.consultar("SELECT * FROM pre_tipo_aporte_presu WHERE ide_empr=" + utilitario.getVariable("ide_empr"));
            for (int i = 0; i < tab_tipo_aporte.getTotalFilas(); i++) {
                gri_totales.getChildren().add(new Etiqueta(tab_tipo_aporte.getValor(i, "nombre_prtap")));
                gri_totales.getChildren().add(new Etiqueta("0.0"));
            }
            utilitario.addUpdate("gri_totales");

        }
    }

    public void calcula_totales_por_plan_presupuestario() {
        if (tab_tabla2.getTotalFilas() > 0) {
            TablaGenerica tab_ingreso = utilitario.consultar("SELECT dp.ide_prtap,tap.nombre_prtap,sum(total_prdep) as total FROM pre_cab_plan_presu cpp "
                    + "left join pre_descripcion_presu dpr on dpr.ide_prcppr=cpp.ide_prcppr "
                    + "left join pre_detalle_presu dp on dp.ide_prdpr=dpr.ide_prdpr "
                    + "left join pre_tipo_aporte_presu tap on tap.ide_prtap=dp.ide_prtap "
                    + "WHERE cpp.ide_prcppr=" + tab_tabla1.getValor("ide_prcppr") + " "
                    + "and dp.ide_prtpr=" + utilitario.getVariable("p_pre_tipo_presu_ingreso") + " "
                    + "GROUP BY cpp.ide_prcppr,dp.ide_prtap,dp.ide_prtpr,tap.nombre_prtap");
            gri_totales.getChildren().clear();
            for (int i = 0; i < tab_ingreso.getTotalFilas(); i++) {
                gri_totales.getChildren().add(new Etiqueta(tab_ingreso.getValor(i, "nombre_prtap") + "  "));
                gri_totales.getChildren().add(new Etiqueta(utilitario.getFormatoNumero(tab_ingreso.getValor(i, "total"))));
                //gri_totales.getChildren().add(new Etiqueta(tab_ingreso.getValor(i, "total")));
            }
            gri_totales.getChildren().add(new Etiqueta("TOTAL INGRESOS:  "));
            gri_totales.getChildren().add(new Etiqueta(utilitario.getFormatoNumero(tab_ingreso.getSumaColumna("total"))));

            TablaGenerica tab_gastos = utilitario.consultar("SELECT dp.ide_prtap,tap.nombre_prtap,sum(total_prdep) as total FROM pre_cab_plan_presu cpp "
                    + "left join pre_descripcion_presu dpr on dpr.ide_prcppr=cpp.ide_prcppr "
                    + "left join pre_detalle_presu dp on dp.ide_prdpr=dpr.ide_prdpr "
                    + "left join pre_tipo_aporte_presu tap on tap.ide_prtap=dp.ide_prtap "
                    + "WHERE cpp.ide_prcppr=" + tab_tabla1.getValor("ide_prcppr") + " "
                    + "and dp.ide_prtpr=" + utilitario.getVariable("p_pre_tipo_presu_gasto") + " "
                    + "GROUP BY cpp.ide_prcppr,dp.ide_prtap,dp.ide_prtpr,tap.nombre_prtap");
            if (tab_gastos.getTotalFilas() > 0) {
                gri_totales.getChildren().add(new Etiqueta("TOTAL GASTOS:  "));
                gri_totales.getChildren().add(new Etiqueta(utilitario.getFormatoNumero(tab_gastos.getValor(0, "total"))));
            } else {
                gri_totales.getChildren().add(new Etiqueta("TOTAL GASTOS:  "));
                gri_totales.getChildren().add(new Etiqueta("0.00"));
            }
            utilitario.addUpdate("gri_totales");
        }

    }

    public void calcula_totales() {
        if (tab_tabla2.getTotalFilas() > 0) {
            TablaGenerica tab_ingreso = utilitario.consultar("SELECT dp.ide_prtap,tap.nombre_prtap,sum(total_prdep) as total FROM pre_detalle_presu dp "
                    + "left join pre_tipo_aporte_presu tap on tap.ide_prtap=dp.ide_prtap "
                    + "WHERE dp.ide_prdpr=" + tab_tabla2.getValor(tab_tabla2.getFilaActual(), "ide_prdpr") + " "
                    + "AND dp.ide_prtpr =" + utilitario.getVariable("p_pre_tipo_presu_ingreso") + " "
                    + "GROUP BY dp.ide_prtap,tap.nombre_prtap");

            gri_totales.getChildren().clear();
            for (int i = 0; i < tab_ingreso.getTotalFilas(); i++) {
                gri_totales.getChildren().add(new Etiqueta(tab_ingreso.getValor(i, "nombre_prtap") + "  "));
                gri_totales.getChildren().add(new Etiqueta(utilitario.getFormatoNumero(tab_ingreso.getValor(i, "total"))));
            }
            gri_totales.getChildren().add(new Etiqueta("TOTAL INGRESOS:  "));
            gri_totales.getChildren().add(new Etiqueta(utilitario.getFormatoNumero(tab_ingreso.getSumaColumna("total"))));
            //gri_totales.getChildren().add(new Etiqueta(tab_ingreso.getSumaColumna("total") + "  "));

            TablaGenerica tab_gastos = utilitario.consultar("SELECT dp.ide_prtap,tap.nombre_prtap,sum(total_prdep) as total FROM pre_detalle_presu dp "
                    + "left join pre_tipo_aporte_presu tap on tap.ide_prtap=dp.ide_prtap "
                    + "WHERE dp.ide_prdpr=" + tab_tabla2.getValor(tab_tabla2.getFilaActual(), "ide_prdpr") + " "
                    + "AND dp.ide_prtpr =" + utilitario.getVariable("p_pre_tipo_presu_gasto") + " "
                    + "GROUP BY dp.ide_prtap,tap.nombre_prtap");
            if (tab_gastos.getTotalFilas() > 0) {
                gri_totales.getChildren().add(new Etiqueta("TOTAL GASTOS:  "));
                gri_totales.getChildren().add(new Etiqueta(utilitario.getFormatoNumero(tab_gastos.getValor(0, "total"))));
            } else {
                gri_totales.getChildren().add(new Etiqueta("TOTAL GASTOS:  "));
                gri_totales.getChildren().add(new Etiqueta("0.00"));
            }
            utilitario.addUpdate("gri_totales");
        }
    }

    public void calcula_totales_deta() {
        String ide_prtap = "";
        String total = "";
        System.out.println("ide prtap " + tab_tabla3.getValor("ide_prtap"));
        System.out.println("total " + tab_tabla3.getValor("total_prdep"));
        TablaGenerica tab_total_ingresos = utilitario.consultar("SELECT ide_prtap,sum(total_prdep) as total FROM pre_detalle_presu  "
                + "WHERE ide_prdpr=" + tab_tabla2.getValor("ide_prdpr") + " "
                + "AND ide_prtpr=" + utilitario.getVariable("p_pre_tipo_presu_ingreso") + " "
                + "GROUP BY ide_prtap");
        int band = 0;
        if (tab_tabla3.getValor("ide_prtap") != null && !tab_tabla3.getValor("ide_prtap").isEmpty()) {
            // carga 
            ide_prtap = tab_tabla3.getValor("ide_prtap");
            if (tab_tabla3.getValor("total_prdep") != null && !tab_tabla3.getValor("total_prdep").isEmpty()) {
                total = tab_tabla3.getValor("total_prdep");
            } else {
                total = "0";
            }
            band = 1;
            for (int j = 0; j < tab_total_ingresos.getTotalFilas(); j++) {
                if (tab_total_ingresos.getValor(j, "ide_prtap").equals(ide_prtap)) {
                    band = 2;
                    break;
                }
            }
        }

        gri_totales.getChildren().clear();
        if (band == 1) {
            TablaGenerica tab_tipo_aporte = utilitario.consultar("SELECT * FROM pre_tipo_aporte_presu WHERE ide_prtap=" + ide_prtap);
            gri_totales.getChildren().add(new Etiqueta(tab_tipo_aporte.getValor(0, "nombre_prtap")));
            gri_totales.getChildren().add(new Etiqueta(total));
        }
        for (int i = 0; i < tab_total_ingresos.getTotalFilas(); i++) {
            TablaGenerica tab_tipo_aporte = utilitario.consultar("SELECT * FROM pre_tipo_aporte_presu WHERE ide_prtap=" + tab_total_ingresos.getValor(i, "ide_prtap"));
            if (tab_tipo_aporte.getTotalFilas() >= 0) {
                if (band == 2) {
                    if (tab_tipo_aporte.getValor(0, "ide_prtap").equals(ide_prtap)) {
                        double tot = Double.parseDouble(total) + Double.parseDouble(tab_total_ingresos.getValor(i, "total"));

                        gri_totales.getChildren().add(new Etiqueta(tab_tipo_aporte.getValor(0, "nombre_prtap")));
                        gri_totales.getChildren().add(new Etiqueta(utilitario.getFormatoNumero(tot)));
                    } else {
                        gri_totales.getChildren().add(new Etiqueta(tab_tipo_aporte.getValor(0, "nombre_prtap")));
                        gri_totales.getChildren().add(new Etiqueta(tab_total_ingresos.getValor(i, "total")));
                    }
                } else {
                    gri_totales.getChildren().add(new Etiqueta(tab_tipo_aporte.getValor(0, "nombre_prtap")));
                    gri_totales.getChildren().add(new Etiqueta(tab_total_ingresos.getValor(i, "total")));
                }
            }
        }
        //TOTAL DE INGRESOS
        gri_totales.getChildren().add(new Etiqueta("TOTAL INGRESOS: "));
        if (band > 0) {
            double tot = Double.parseDouble(total) + tab_total_ingresos.getSumaColumna("total");
            gri_totales.getChildren().add(new Etiqueta(tot + ""));
        } else {
            double tot = tab_total_ingresos.getSumaColumna("total");
            gri_totales.getChildren().add(new Etiqueta(tot + ""));
        }
        System.out.println("total " + tab_tabla4.getValor("total_prdep"));
        TablaGenerica tab_total_gastos = utilitario.consultar("select ide_prdpr,sum(total_prdep) as total FROM pre_detalle_presu "
                + "WHERE ide_prdpr=" + tab_tabla2.getValor("ide_prdpr") + " "
                + "AND ide_prtpr=" + utilitario.getVariable("p_pre_tipo_presu_gasto") + " group by ide_prdpr ");
        if (tab_tabla4.isFilaInsertada()) {
            if (tab_total_gastos.getTotalFilas() > 0) {
                System.out.println("total " + tab_tabla4.getValor("total_prdep"));
                if (tab_tabla4.getValor("total_prdep") != null && !tab_tabla4.getValor("total_prdep").isEmpty()) {
                    gri_totales.getChildren().add(new Etiqueta("TOTAL GASTOS: "));
                    double tot = Double.parseDouble(tab_total_gastos.getValor(0, "total")) + Double.parseDouble(tab_tabla4.getValor("total_prdep"));
                    gri_totales.getChildren().add(new Etiqueta(tot + ""));
                } else {
                    gri_totales.getChildren().add(new Etiqueta("TOTAL GASTOS: "));
                    gri_totales.getChildren().add(new Etiqueta(tab_total_gastos.getValor(0, "total")));
                }
            } else {
                if (tab_tabla4.getValor("total_prdep") != null && !tab_tabla4.getValor("total_prdep").isEmpty()) {
                    gri_totales.getChildren().add(new Etiqueta("TOTAL GASTOS: "));
                    gri_totales.getChildren().add(new Etiqueta(tab_tabla4.getValor("total_prdep")));
                } else {
                    gri_totales.getChildren().add(new Etiqueta("TOTAL GASTOS: "));
                    gri_totales.getChildren().add(new Etiqueta("0.0"));
                }
            }
        } else {
            if (tab_total_gastos.getTotalFilas() > 0) {
                gri_totales.getChildren().add(new Etiqueta("TOTAL GASTOS: "));
                gri_totales.getChildren().add(new Etiqueta(tab_total_gastos.getValor(0, "total")));
            } else {
                gri_totales.getChildren().add(new Etiqueta("TOTAL GASTOS: "));
                gri_totales.getChildren().add(new Etiqueta("0.0"));
            }
        }
        utilitario.addUpdate("gri_totales");
    }

    public void calcula_total_ingresos() {
        if (tab_tabla3.getTotalFilas() > 0) {
            if (tab_tabla3.isFilaInsertada()) {
                calcula_totales_deta();
            } else if (tab_tabla4.isFilaInsertada()) {

                calcula_totales_deta();
            }
        }
    }

    public void seleccionar_tabla2(SelectEvent evt) {
        tab_tabla2.seleccionarFila(evt);
        tab_tabla4.setSql("SELECT ide_prdep as ide_gasto,ide_prdpr,ide_prtpa,ide_cndpc,nombre_prdep,num_prdep,veces_prdep,costo_unitario_prdep,total_prdep,observacion_prdep  "
                + "FROM  pre_detalle_presu WHERE ide_empr=" + utilitario.getVariable("ide_empr") + " and ide_sucu=" + utilitario.getVariable("ide_sucu") + " and ide_prdpr=" + tab_tabla2.getValor("ide_prdpr") + " and ide_prtpr=" + utilitario.getVariable("p_pre_tipo_presu_gasto"));
        tab_tabla4.ejecutarSql();
        calcula_totales();
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

    public Tabla getTab_tabla4() {
        return tab_tabla4;
    }

    public void setTab_tabla4(Tabla tab_tabla4) {
        this.tab_tabla4 = tab_tabla4;
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

    public SeleccionTabla getSel_tab() {
        return sel_tab;
    }

    public void setSel_tab(SeleccionTabla sel_tab) {
        this.sel_tab = sel_tab;
    }

    public SeleccionTabla getSel_tab_mes() {
        return sel_tab_mes;
    }

    public void setSel_tab_mes(SeleccionTabla sel_tab_mes) {
        this.sel_tab_mes = sel_tab_mes;
    }

    public SeleccionTabla getSel_tab_periodo() {
        return sel_tab_periodo;
    }

    public void setSel_tab_periodo(SeleccionTabla sel_tab_periodo) {
        this.sel_tab_periodo = sel_tab_periodo;
    }

    public Tabulador getTab_tabulador() {
        return tab_tabulador;
    }

    public void setTab_tabulador(Tabulador tab_tabulador) {
        this.tab_tabulador = tab_tabulador;
    }

    public Check getChk_aprobado() {
        return chk_aprobado;
    }

    public void setChk_aprobado(Check chk_aprobado) {
        this.chk_aprobado = chk_aprobado;
    }

    public Dialogo getDia_cambiar_estado() {
        return dia_cambiar_estado;
    }

    public void setDia_cambiar_estado(Dialogo dia_cambiar_estado) {
        this.dia_cambiar_estado = dia_cambiar_estado;
    }

    public Boton getBot_cambiar_presupuesto() {
        return bot_cambiar_presupuesto;
    }

    public void setBot_cambiar_presupuesto(Boton bot_cambiar_presupuesto) {
        this.bot_cambiar_presupuesto = bot_cambiar_presupuesto;
    }

    public SeleccionTabla getSel_tab_modificacion() {
        return sel_tab_modificacion;
    }

    public void setSel_tab_modificacion(SeleccionTabla sel_tab_modificacion) {
        this.sel_tab_modificacion = sel_tab_modificacion;
    }

    public SeleccionTabla getSel_tab_periodo_cabecera() {
        return sel_tab_periodo_cabecera;
    }

    public void setSel_tab_periodo_cabecera(SeleccionTabla sel_tab_periodo_cabecera) {
        this.sel_tab_periodo_cabecera = sel_tab_periodo_cabecera;
    }

    public SeleccionCalendario getSec_rango_reporte() {
        return sec_rango_reporte;
    }

    public void setSec_rango_reporte(SeleccionCalendario sec_rango_reporte) {
        this.sec_rango_reporte = sec_rango_reporte;
    }
}
