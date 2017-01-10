/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_contabilidad;

import componentes.AsientoContable;
import framework.aplicacion.Fila;
import framework.aplicacion.TablaGenerica;
import framework.componentes.AutoCompletar;
import framework.componentes.Barra;
import framework.componentes.Boton;
import framework.componentes.Calendario;
import framework.componentes.Dialogo;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.Grupo;
import framework.componentes.Link;
import framework.componentes.MenuPanel;
import framework.componentes.PanelTabla;
import framework.componentes.Radio;
import framework.componentes.Reporte;
import framework.componentes.SeleccionCalendario;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import framework.reportes.ReporteDataSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.faces.event.ActionEvent;
import org.primefaces.component.fieldset.Fieldset;
import org.primefaces.component.separator.Separator;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import pkg_contabilidad.cls_contabilidad;
import servicios.contabilidad.ServicioContabilidadGeneral;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author diego.jacome
 */
public class pre_contabilidad extends Pantalla {

    @EJB
    private final ServicioContabilidadGeneral ser_contabilidad = (ServicioContabilidadGeneral) utilitario.instanciarEJB(ServicioContabilidadGeneral.class);

    private final MenuPanel mep_menu = new MenuPanel();
    private AutoCompletar aut_cuenta;
    private AutoCompletar aut_persona;

    private Tabla tab_consulta;

    //Consultas
    private Calendario cal_fecha_inicio;
    private Calendario cal_fecha_fin;
    private Radio rad_niveles;

    //Reportes
    private final String p_con_lugar_debe = utilitario.getVariable("p_con_lugar_debe");
    private final String p_con_lugar_haber = utilitario.getVariable("p_con_lugar_haber");
    private Reporte rep_reporte = new Reporte();
    private SeleccionFormatoReporte sel_rep = new SeleccionFormatoReporte();
    private SeleccionTabla sel_tab = new SeleccionTabla();
    private SeleccionTabla sel_tab_nivel = new SeleccionTabla();
    private SeleccionCalendario sec_rango_reporte = new SeleccionCalendario();
    private cls_contabilidad con = new cls_contabilidad();
    private List lis_ide_cndpc_deseleccionados = new ArrayList();
    private int int_count_deseleccion = 0;
    private Map parametro = new HashMap();
    private String fecha_fin;
    private String fecha_inicio;

    private AsientoContable asc_asiento = new AsientoContable();
    private Dialogo dia_cerrar_periodo = new Dialogo();

    public pre_contabilidad() {
        bar_botones.limpiar();
        bar_botones.agregarReporte();

        mep_menu.setMenuPanel("INFORMES CONTABLIDAD", "20%");

        mep_menu.agregarItem("Plan de Cuentas", "dibujarPlan", "ui-icon-bookmark"); //5
        mep_menu.agregarItem("Períodos Contales", "dibujarPeriodo", "ui-icon-bookmark"); //6

        mep_menu.agregarItem("Libro Mayor", "dibujarLibroMayor", "ui-icon-bookmark"); //1
        mep_menu.agregarItem("Libro Diario", "dibujarLibroDiario", "ui-icon-bookmark");//2
        mep_menu.agregarItem("Balance General", "dibujarBalanceGeneral", "ui-icon-bookmark");//3
        mep_menu.agregarItem("Estado de Resultados", "dibujarEstadoResultados", "ui-icon-bookmark");//4
        mep_menu.agregarSubMenu("GRAFICOS");
        mep_menu.agregarItem("Gráfico Balance", "dibujarGrafico", "ui-icon-bookmark");
        agregarComponente(mep_menu);

        //Reportes
        //seleccion de las cuentas para reporte libro mayor
        sel_tab.setId("sel_tab");
        sel_tab.setSeleccionTabla(ser_contabilidad.getSqlCuentasHijas(), "ide_cndpc");
        sel_tab.getTab_seleccion().getColumna("nombre_cndpc").setFiltro(true);
        sel_tab.getTab_seleccion().getColumna("codig_recur_cndpc").setFiltro(true);
        sel_tab.getTab_seleccion().onSelectCheck("seleccionaCuentaContable");
        sel_tab.getTab_seleccion().onUnselectCheck("deseleccionaCuentaContable");
        sel_tab.setDynamic(false);
        agregarComponente(sel_tab);

        rep_reporte.setId("rep_reporte");
        rep_reporte.getBot_aceptar().setMetodo("aceptarReporte");
        sel_tab.getBot_aceptar().setMetodo("aceptarReporte");
        sel_tab.getBot_aceptar().setUpdate("sel_tab ");

        sel_tab_nivel.setId("sel_tab_nivel");
        sel_tab_nivel.setTitle("Seleccione El Nivel");
        sel_tab_nivel.setSeleccionTabla(ser_contabilidad.getSqlNivelesPlandeCuentas(), "ide_cnncu");
        sel_tab_nivel.setRadio();
        agregarComponente(sel_tab_nivel);

        sel_tab_nivel.getBot_aceptar().setMetodo("aceptarReporte");

        agregarComponente(rep_reporte);

        sec_rango_reporte.setId("sec_rango_reporte");
        sec_rango_reporte.getBot_aceptar().setMetodo("aceptarReporte");
        sec_rango_reporte.setMultiple(false);
        agregarComponente(sec_rango_reporte);

        sel_rep.setId("sel_rep");
        agregarComponente(sel_rep);

        asc_asiento.setId("asc_asiento");
        agregarComponente(asc_asiento);

        dia_cerrar_periodo.setId("dia_cerrar_periodo");
        dia_cerrar_periodo.setTitle("CONFIRMACION CIERRE DE PERIODO");
        dia_cerrar_periodo.setWidth("45%");
        dia_cerrar_periodo.setHeight("30%");

        Grid gri = new Grid();
        gri.setColumns(1);
        Etiqueta eti_1 = new Etiqueta("ATENCION SE REALIZARA LAS SIGUIENTES ACCIONES: ");
        eti_1.setStyle("font-size: 13px;font-weight:bold");
        Etiqueta eti_2 = new Etiqueta("1.- SE CERRARA EL PERIODO ACTUAL Y QUEDARA INACTIVO ");
        eti_2.setStyle("font-size: 11px;font-weight:text-decoration: underline");
        Etiqueta eti_3 = new Etiqueta("2.- SE VA A GENERAR UN NUEVO PERIODO AUTOMATICAMENTE SIN FECHA DE CIERRE ");
        eti_3.setStyle("font-size: 11px;font-weight:text-decoration: underline");
        Etiqueta eti_4 = new Etiqueta("3.- SE GENERARA UN ASIENTO DE APERTURA DE CUENTAS ");
        eti_4.setStyle("font-size: 11px;font-weight:text-decoration: underline");
        Etiqueta eti_5 = new Etiqueta("ESTA SEGURO DE CONTINUAR CON EL CIERRE DE PERIODO: ");
        eti_5.setStyle("font-size: 13px;font-weight:bold");
        gri.getChildren().add(eti_1);
        gri.getChildren().add(new Etiqueta(""));
        gri.getChildren().add(new Etiqueta(""));
        gri.getChildren().add(eti_2);
        gri.getChildren().add(eti_3);
        gri.getChildren().add(eti_4);
        gri.getChildren().add(new Etiqueta(""));
        gri.getChildren().add(new Etiqueta(""));
        gri.getChildren().add(eti_5);
        gri.setStyle("width:" + (dia_cerrar_periodo.getAnchoPanel() - 5) + "px; height:" + dia_cerrar_periodo.getAltoPanel() + "px;overflow:auto;display:block;");
        dia_cerrar_periodo.setDialogo(gri);
        dia_cerrar_periodo.getBot_aceptar().setMetodo("aceptarCierrePeriodo");

        agregarComponente(dia_cerrar_periodo);

    }

    /**
     * Selecciona una cuenta en el autocompletar para el libro mayor
     *
     * @param evt
     */
    public void seleccionarCuenta(SelectEvent evt) {
        aut_cuenta.onSelect(evt);
        aut_persona.limpiar();
        actualizarLibroMayor();
    }

    public void dibujarLibroMayor() {
        Grupo gru_grupo = new Grupo();

        Fieldset fis_consulta = new Fieldset();
        Grid gp = new Grid();
        gp.setColumns(3);
        gp.getChildren().add(new Etiqueta("<strong>CUENTA CONTABLE : </strong>"));

        aut_cuenta = new AutoCompletar();
        aut_cuenta.setId("aut_cuenta");
        aut_cuenta.setAutoCompletar(ser_contabilidad.getSqlCuentasHijas());
        aut_cuenta.setSize(75);        
        aut_cuenta.setAutocompletarContenido(); // no startWith para la busqueda
        aut_cuenta.setMetodoChange("seleccionarCuenta");
        aut_cuenta.setGlobal(true);
        gp.getChildren().add(aut_cuenta);
        Boton bot_clean = new Boton();
        bot_clean.setIcon("ui-icon-cancel");
        bot_clean.setTitle("Limpiar");
        bot_clean.setMetodo("limpiar");
        gp.getChildren().add(bot_clean);

        gp.getChildren().add(new Etiqueta("<strong>BENEFICIARIO : </strong>"));
        aut_persona = new AutoCompletar();
        aut_persona.setId("aut_persona");        
        aut_persona.setSize(75);
        aut_persona.setAutocompletarContenido(); // no startWith para la busqueda
        aut_persona.setMetodoChange("seleccionarPersona");
        aut_persona.setGlobal(true);
        gp.getChildren().add(aut_persona);

        fis_consulta.getChildren().add(gp);

        Grid gri_fechas = new Grid();
        gri_fechas.setColumns(5);
        gri_fechas.getChildren().add(new Etiqueta("<strong>FECHA DESDE :</strong>"));
        cal_fecha_inicio = new Calendario();
        cal_fecha_inicio.setValue(utilitario.getFecha(utilitario.getAnio(utilitario.getFechaActual()) + "-01-01"));
        gri_fechas.getChildren().add(cal_fecha_inicio);
        gri_fechas.getChildren().add(new Etiqueta("<strong>FECHA HASTA :</strong>"));
        cal_fecha_fin = new Calendario();
        cal_fecha_fin.setFechaActual();
        gri_fechas.getChildren().add(cal_fecha_fin);
        fis_consulta.getChildren().add(gri_fechas);

        Boton bot_consultar = new Boton();
        bot_consultar.setValue("Consultar");
        bot_consultar.setMetodo("actualizarLibroMayor");
        bot_consultar.setIcon("ui-icon-search");

        gri_fechas.getChildren().add(bot_consultar);

        Separator separar = new Separator();
        fis_consulta.getChildren().add(separar);

        gru_grupo.getChildren().add(fis_consulta);

        tab_consulta = new Tabla();
        tab_consulta.setNumeroTabla(-1);
        tab_consulta.setId("tab_consulta");
        tab_consulta.setSql(ser_contabilidad.getSqlMovimientosCuenta(aut_cuenta.getValor(), cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha(), ""));
        tab_consulta.setLectura(true);
        tab_consulta.getColumna("ide_cnccc").setNombreVisual("N. ASIENTO");
        tab_consulta.getColumna("ide_cnccc").setFiltro(true);
        tab_consulta.getColumna("IDE_CNCCC").setLink();
        tab_consulta.getColumna("IDE_CNCCC").setMetodoChange("abrirAsiento");
        tab_consulta.getColumna("IDE_CNCCC").alinearCentro();
        tab_consulta.getColumna("fecha_trans_cnccc").setNombreVisual("FECHA");
        tab_consulta.getColumna("ide_cnlap").setVisible(false);
        tab_consulta.getColumna("debe").setLongitud(20);
        tab_consulta.getColumna("haber").setLongitud(20);
        tab_consulta.getColumna("saldo").setLongitud(25);
        tab_consulta.getColumna("debe").alinearDerecha();
        tab_consulta.getColumna("haber").alinearDerecha();
        tab_consulta.getColumna("saldo").alinearDerecha();
        tab_consulta.getColumna("debe").setSuma(false);
        tab_consulta.getColumna("haber").setSuma(false);
        tab_consulta.getColumna("saldo").setSuma(false);
        tab_consulta.getColumna("saldo").setEstilo("font-weight: bold;");
        tab_consulta.getColumna("valor_cndcc").setVisible(false);
        tab_consulta.setColumnaSuma("debe,haber,saldo");
        tab_consulta.setRows(20);
        tab_consulta.setOrdenar(false);
        tab_consulta.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_consulta);
        gru_grupo.getChildren().add(pat_panel);
        actualizarSaldosLibroMayor();
        mep_menu.dibujar(1, "LIBRO MAYOR", gru_grupo);
    }

    public void seleccionarPersona(SelectEvent evt) {
        aut_persona.onSelect(evt);
        actualizarLibroMayor();
    }

    /**
     * Actualiza libro mayor segun las fechas selecionadas
     */
    public void actualizarLibroMayor() {
        if (isCuentaSeleccionada()) {
            tab_consulta.setSql(ser_contabilidad.getSqlMovimientosCuenta(aut_cuenta.getValor(), cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha(), aut_persona.getValor()));
            tab_consulta.ejecutarSql();

            if (aut_persona.getValor() == null) {
                String ide_geper = tab_consulta.getStringColumna("ide_geper");
                if (ide_geper != null) {
                    if (ide_geper.isEmpty() == false) {
                        aut_persona.setAutoCompletar("select ide_geper,identificac_geper,nom_geper from gen_persona where ide_geper in (" + ide_geper + ")");
                    }
                }
            }

            actualizarSaldosLibroMayor();
        }
    }

    /**
     * Actualiza los solados libro mayor
     */
    private void actualizarSaldosLibroMayor() {
        double saldo_anterior = ser_contabilidad.getSaldoInicialCuenta(aut_cuenta.getValor(), cal_fecha_inicio.getFecha());
        double dou_saldo_inicial = saldo_anterior;
        double dou_saldo_actual = 0;
        double dou_debe = 0;
        double dou_haber = 0;
        for (int i = 0; i < tab_consulta.getTotalFilas(); i++) {
            if (tab_consulta.getValor(i, "ide_cnlap").equals(p_con_lugar_debe)) {
                tab_consulta.setValor(i, "debe", utilitario.getFormatoNumero(Math.abs(Double.parseDouble(tab_consulta.getValor(i, "valor_cndcc")))));
                dou_debe += Double.parseDouble(tab_consulta.getValor(i, "debe"));
            } else {
                tab_consulta.setValor(i, "haber", utilitario.getFormatoNumero(Math.abs(Double.parseDouble(tab_consulta.getValor(i, "valor_cndcc")))));
                dou_haber += Double.parseDouble(tab_consulta.getValor(i, "haber"));
            }
            dou_saldo_actual = saldo_anterior + Double.parseDouble(tab_consulta.getValor(i, "valor_cndcc"));
            tab_consulta.setValor(i, "saldo", utilitario.getFormatoNumero(dou_saldo_actual));
            saldo_anterior = dou_saldo_actual;
        }
        if (tab_consulta.isEmpty()) {
            dou_saldo_actual = dou_saldo_inicial;
            tab_consulta.setEmptyMessage("No existen Movimientos Contables en el rango de fechas seleccionado");
        }
        //INSERTA PRIMERA FILA SALDO INICIAL
        if (isCuentaSeleccionada()) {
            tab_consulta.setLectura(false);
            tab_consulta.insertar();
            tab_consulta.setValor("saldo", utilitario.getFormatoNumero(dou_saldo_inicial));
            tab_consulta.setValor("OBSERVACION", "SALDO INICIAL AL " + cal_fecha_inicio.getFecha());
            tab_consulta.setValor("fecha_trans_cnccc", cal_fecha_inicio.getFecha());
            tab_consulta.setLectura(true);
        }
        //ASIGNA SALDOS FINALES
        tab_consulta.getColumna("saldo").setTotal(dou_saldo_actual);
        tab_consulta.getColumna("debe").setTotal(dou_debe);
        tab_consulta.getColumna("haber").setTotal(dou_haber);
    }

    public void dibujarLibroDiario() {
        Grupo gru_grupo = new Grupo();

        Fieldset fis_consulta = new Fieldset();
        Grid gri_fechas = new Grid();
        gri_fechas.setColumns(5);
        gri_fechas.getChildren().add(new Etiqueta("<strong>FECHA DESDE :</strong>"));
        cal_fecha_inicio = new Calendario();
        cal_fecha_inicio.setValue(utilitario.getFecha(utilitario.getAnio(utilitario.getFechaActual()) + "-01-01"));
        gri_fechas.getChildren().add(cal_fecha_inicio);
        gri_fechas.getChildren().add(new Etiqueta("<strong>FECHA HASTA :</strong>"));
        cal_fecha_fin = new Calendario();
        cal_fecha_fin.setFechaActual();
        gri_fechas.getChildren().add(cal_fecha_fin);
        fis_consulta.getChildren().add(gri_fechas);

        Boton bot_consultar = new Boton();
        bot_consultar.setValue("Consultar");
        bot_consultar.setMetodo("actualizarLibroDiario");
        bot_consultar.setIcon("ui-icon-search");

        gri_fechas.getChildren().add(bot_consultar);

        Separator separar = new Separator();
        fis_consulta.getChildren().add(separar);
        gru_grupo.getChildren().add(fis_consulta);

        tab_consulta = new Tabla();
        tab_consulta.setNumeroTabla(-1);
        tab_consulta.setId("tab_consulta");
        tab_consulta.setSql(ser_contabilidad.getSqlLibroDiario(cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha()));
        tab_consulta.getColumna("ide_cndcc").setVisible(false);
        tab_consulta.setLectura(true);
        tab_consulta.getColumna("ide_cnccc").setNombreVisual("N. ASIENTO");
        tab_consulta.getColumna("ide_cnccc").setFiltro(true);
        tab_consulta.getColumna("fecha_trans_cnccc").setNombreVisual("FECHA");
        tab_consulta.getColumna("codig_recur_cndpc").setNombreVisual("CODIGO CUENTA CONTABLE");
        tab_consulta.getColumna("nombre_cndpc").setNombreVisual("CUENTA CONTABLE");
        tab_consulta.getColumna("observacion_cnccc").setNombreVisual("OBSERVACION");
        tab_consulta.getColumna("nombre_cntcm").setNombreVisual("TIPO DE COMPROBANTE");
        tab_consulta.getColumna("numero_cnccc").setFiltro(true);
        tab_consulta.getColumna("numero_cnccc").setNombreVisual("SECUENCIAL");
        tab_consulta.getColumna("numero_cnccc").setLongitud(25);
        tab_consulta.getColumna("debe").setLongitud(20);
        tab_consulta.getColumna("haber").setLongitud(20);
        tab_consulta.getColumna("debe").alinearDerecha();
        tab_consulta.getColumna("haber").alinearDerecha();
        tab_consulta.setColumnaSuma("debe,haber");
        tab_consulta.setRows(20);
        tab_consulta.setOrdenar(false);
        tab_consulta.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_consulta);
        gru_grupo.getChildren().add(pat_panel);
        mep_menu.dibujar(2, "LIBRO DIARIO", gru_grupo);
    }

    public void dibujarPlan() {

        Grupo gru_grupo = new Grupo();

        tab_consulta = new Tabla();
        tab_consulta.setNumeroTabla(-1);
        tab_consulta.setId("tab_consulta");
        tab_consulta.setSql(ser_contabilidad.getSqlCuentas());
        tab_consulta.getColumna("ide_cndpc").setVisible(false);
        tab_consulta.setLectura(true);
        tab_consulta.getColumna("codig_recur_cndpc").setNombreVisual("CÓDIGO DE LA CUENTA");
        tab_consulta.getColumna("codig_recur_cndpc").setFiltro(true);
        tab_consulta.getColumna("nombre_cndpc").setNombreVisual("NOMBRE DE LA CUENTA");
        tab_consulta.getColumna("nombre_cndpc").setFiltroContenido();
        tab_consulta.setRows(30);
        tab_consulta.setOrdenar(false);
        tab_consulta.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_consulta);
        gru_grupo.getChildren().add(pat_panel);
        mep_menu.dibujar(5, "PLAN DE CUENTAS", gru_grupo);

    }

    public void confirmarCerrar() {
        dia_cerrar_periodo.dibujar();
    }

    public void dibujarPeriodo() {

        Grupo gru_grupo = new Grupo();

        Barra bar_menu = new Barra();
        bar_menu.setId("bar_menu");
        bar_menu.limpiar();
        Boton bot_ver = new Boton();
        bot_ver.setValue("Cerrar Periodo Contable");
        bot_ver.setMetodo("confirmarCerrar");
        bar_menu.agregarComponente(bot_ver);

        gru_grupo.getChildren().add(bar_menu);

        tab_consulta = new Tabla();
        tab_consulta.setId("tab_consulta");
        tab_consulta.setTabla("con_periodo", "ide_cnper", 999);
        tab_consulta.getColumna("ide_cnper").setVisible(false);
        tab_consulta.setCondicionSucursal(true);

        tab_consulta.getColumna("nombre_cnper").setNombreVisual("NOMBRE");
        tab_consulta.getColumna("fecha_inicio_cnper").setNombreVisual("FECHA INICIO");
        tab_consulta.getColumna("fecha_fin_cnper").setNombreVisual("FECHA FIN");
        tab_consulta.getColumna("estado_cnper").setNombreVisual("ACTIVO");
        tab_consulta.getColumna("estado_cnper").setLectura(true);
        tab_consulta.getColumna("cerrado_cnper").setNombreVisual("CERRADO");
        tab_consulta.getColumna("cerrado_cnper").setLectura(true);

        tab_consulta.getColumna("ide_cnccc_i").setNombreVisual("NUM. ASIENTO INICIAL");
        tab_consulta.getColumna("ide_cnccc_i").setLink();
        tab_consulta.getColumna("ide_cnccc_i").setMetodoChange("abrirAsiento");
        tab_consulta.getColumna("ide_cnccc_c").setNombreVisual("NUM. ASIENTO CIERRE");
        tab_consulta.getColumna("ide_cnccc_c").setLink();
        tab_consulta.getColumna("ide_cnccc_c").setMetodoChange("abrirAsiento");

        tab_consulta.setRows(20);

        tab_consulta.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_consulta);
        gru_grupo.getChildren().add(pat_panel);
        mep_menu.dibujar(6, "PERÍODOS CONTABLES", gru_grupo);

    }

    public void actualizarLibroDiario() {
        tab_consulta.setSql(ser_contabilidad.getSqlLibroDiario(cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha()));
        tab_consulta.ejecutarSql();
    }

    public void dibujarBalanceGeneral() {
        Grupo gru_grupo = new Grupo();

        Fieldset fis_consulta = new Fieldset();
        Grid gr_nivel = new Grid();
        gr_nivel.setColumns(2);
        gr_nivel.getChildren().add(new Etiqueta("<strong>NIVEL PLAN DE CUENTAS :</strong> "));
        rad_niveles = new Radio();
        rad_niveles.setRadio(utilitario.getConexion().consultar(ser_contabilidad.getSqlNivelesPlandeCuentas()));
        gr_nivel.getChildren().add(rad_niveles);

        fis_consulta.getChildren().add(gr_nivel);

        Grid gri_fechas = new Grid();
        gri_fechas.setColumns(3);
        gri_fechas.getChildren().add(new Etiqueta("<strong>FECHA :</strong>"));
        cal_fecha_fin = new Calendario();
        cal_fecha_fin.setFechaActual();
        gri_fechas.getChildren().add(cal_fecha_fin);
        fis_consulta.getChildren().add(gri_fechas);

        Boton bot_consultar = new Boton();
        bot_consultar.setValue("Consultar");
        bot_consultar.setMetodo("actualizarBalanceGeneral");
        bot_consultar.setIcon("ui-icon-search");

        gri_fechas.getChildren().add(bot_consultar);

        Separator separar = new Separator();
        fis_consulta.getChildren().add(separar);
        gru_grupo.getChildren().add(fis_consulta);

        tab_consulta = new Tabla();
        tab_consulta.setNumeroTabla(-1);
        tab_consulta.setId("tab_consulta");
        tab_consulta.setSql(ser_contabilidad.getSqlBalanceGeneral(cal_fecha_fin.getFecha()));
        tab_consulta.setScrollable(true);
        tab_consulta.setScrollHeight(280);
        tab_consulta.setLectura(true);
        tab_consulta.setOrdenar(false);
        tab_consulta.getColumna("CODIG_RECUR_CNDPC").setNombreVisual("CODIGO CUENTA CONTABLE");
        tab_consulta.getColumna("nombre_cndpc").setNombreVisual("CUENTA CONTABLE");
        tab_consulta.getColumna("ide_cndpc").setVisible(false);
        tab_consulta.getColumna("ide_cnncu").setVisible(false);
        tab_consulta.getColumna("ide_cntcu").setVisible(false);
        tab_consulta.getColumna("con_ide_cndpc").setVisible(false);
        tab_consulta.getColumna("valor").setLongitud(30);
        tab_consulta.getColumna("valor").alinearDerecha();
        tab_consulta.getColumna("valor").setNombreVisual("SALDO");
        tab_consulta.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_consulta);
        gru_grupo.getChildren().add(pat_panel);
        calcularBalance();
        mep_menu.dibujar(3, "BALANCE GENERAL", gru_grupo);

    }

    public void actualizarBalanceGeneral() {
        tab_consulta.setSql(ser_contabilidad.getSqlBalanceGeneral(cal_fecha_fin.getFecha()));
        tab_consulta.ejecutarSql();
        calcularBalance();
        System.out.println(ser_contabilidad.getTotalesBalanceGeneral(cal_fecha_fin.getFecha()));
    }

    /**
     * Calcula los totales de las cuentas padre
     */
    private void calcularBalance() {
        int nivel_tope = 0;
        if (rad_niveles.getValue() != null) {
            try {
                nivel_tope = Integer.parseInt(String.valueOf(rad_niveles.getValue()));
            } catch (Exception e) {
            }
        }
        if (nivel_tope == 0) {
            tab_consulta.limpiar();
            return;
        }
        List lis_padres = new ArrayList();
        List lis_valor_padre = new ArrayList();
        double valor_acu = 0;
        int nivel = ser_contabilidad.getUltimoNivelCuentas();
        String padre;
        int band = 0;
        do {
            for (int i = 0; i < tab_consulta.getTotalFilas(); i++) {
                if (tab_consulta.getValor(i, "ide_cnncu").equals(String.valueOf(nivel))) {
                    padre = tab_consulta.getValor(i, "con_ide_cndpc");
                    for (Object lis_padre : lis_padres) {
                        if (padre != null && !padre.isEmpty()) {
                            if (lis_padre.equals(padre)) {
                                band = 1;
                                break;
                            }
                        }
                    }
                    if (band == 0) {
                        if (padre != null && !padre.isEmpty()) {
                            lis_padres.add(padre);
                        }
                        int intOptimiza = 0;
                        for (int j = 0; j < tab_consulta.getTotalFilas(); j++) {
                            if (padre != null && !padre.isEmpty() && tab_consulta.getValor(j, "con_ide_cndpc") != null && !tab_consulta.getValor(j, "con_ide_cndpc").isEmpty()) {
                                if (padre.equals(tab_consulta.getValor(j, "con_ide_cndpc"))) {
                                    intOptimiza = 1;
                                    try {
                                        valor_acu = valor_acu + Double.parseDouble(tab_consulta.getValor(j, "valor"));
                                    } catch (Exception e) {
                                    }
                                }
                            } else {
                                //DFJ
                                if (intOptimiza == 1) {
                                    break;
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
            //Asigna valor acumulado al padre
            for (int i = 0; i < lis_padres.size(); i++) {
                padre = lis_padres.get(i).toString();
                for (int j = 0; j < tab_consulta.getTotalFilas(); j++) {
                    if (tab_consulta.getValor(j, "ide_cndpc").equals(padre)) {
                        try {
                            tab_consulta.setValor(j, "valor", utilitario.getFormatoNumero(lis_valor_padre.get(i).toString()));
                        } catch (Exception e) {
                        }
                        break;
                    }
                }
            }
            nivel = nivel - 1;
        } while (nivel >= 2);

        //elimina cuentas con saldo 0 y las cuentas mayores que el nivel tope
        Iterator<Fila> it = tab_consulta.getFilas().iterator();
        int numColumnaNivel = tab_consulta.getNumeroColumna("ide_cnncu");
        int numColumnaValor = tab_consulta.getNumeroColumna("valor");
        while (it.hasNext()) {
            Fila filaActual = it.next();
            //Elimina cuentas mayores al nivel seleccionado
            if (Integer.parseInt(String.valueOf(filaActual.getCampos()[numColumnaNivel])) > nivel_tope) {
                it.remove();
            } //  Elimina cuentas con valor 0 
            else if (utilitario.getFormatoNumero(String.valueOf(filaActual.getCampos()[numColumnaValor])).equals("0.00")) {
                it.remove();
            }
        }
    }

    public void dibujarEstadoResultados() {
        Grupo gru_grupo = new Grupo();

        Fieldset fis_consulta = new Fieldset();
        //fis_consulta.setLegend("Detalle de la Consulta");

        Grid gr_nivel = new Grid();
        gr_nivel.setColumns(2);
        gr_nivel.getChildren().add(new Etiqueta("<strong>NIVEL PLAN DE CUENTAS :</strong> "));
        rad_niveles = new Radio();
        rad_niveles.setRadio(utilitario.getConexion().consultar(ser_contabilidad.getSqlNivelesPlandeCuentas()));
        gr_nivel.getChildren().add(rad_niveles);

        fis_consulta.getChildren().add(gr_nivel);

        Grid gri_fechas = new Grid();
        gri_fechas.setColumns(5);
        gri_fechas.getChildren().add(new Etiqueta("<strong>FECHA :</strong>"));
        cal_fecha_fin = new Calendario();
        cal_fecha_fin.setFechaActual();
        gri_fechas.getChildren().add(cal_fecha_fin);
        fis_consulta.getChildren().add(gri_fechas);

        Boton bot_consultar = new Boton();
        bot_consultar.setValue("Consultar");
        bot_consultar.setMetodo("actualizarEstadoResultados");
        bot_consultar.setIcon("ui-icon-search");

        gri_fechas.getChildren().add(bot_consultar);

        Separator separar = new Separator();
        fis_consulta.getChildren().add(separar);
        gru_grupo.getChildren().add(fis_consulta);

        tab_consulta = new Tabla();
        tab_consulta.setNumeroTabla(-1);
        tab_consulta.setId("tab_consulta");
        tab_consulta.setSql(ser_contabilidad.getSqlEstadoResultados(cal_fecha_fin.getFecha()));
        tab_consulta.setScrollable(true);
        tab_consulta.setScrollHeight(280);
        tab_consulta.setLectura(true);
        tab_consulta.setOrdenar(false);
        tab_consulta.getColumna("CODIG_RECUR_CNDPC").setNombreVisual("CODIGO CUENTA CONTABLE");
        tab_consulta.getColumna("nombre_cndpc").setNombreVisual("CUENTA CONTABLE");
        tab_consulta.getColumna("ide_cndpc").setVisible(false);
        tab_consulta.getColumna("ide_cnncu").setVisible(false);
        tab_consulta.getColumna("ide_cntcu").setVisible(false);
        tab_consulta.getColumna("con_ide_cndpc").setVisible(false);
        tab_consulta.getColumna("valor").setLongitud(30);
        tab_consulta.getColumna("valor").alinearDerecha();
        tab_consulta.getColumna("valor").setNombreVisual("SALDO");
        tab_consulta.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_consulta);
        gru_grupo.getChildren().add(pat_panel);
        calcularBalance();
        mep_menu.dibujar(4, "ESTADO DE RESULTADOS", gru_grupo);

    }

    public void actualizarEstadoResultados() {
        tab_consulta.setSql(ser_contabilidad.getSqlEstadoResultados(cal_fecha_fin.getFecha()));
        tab_consulta.ejecutarSql();
        calcularBalance();
        System.out.println(ser_contabilidad.getTotalesEstadoResultados(cal_fecha_fin.getFecha()));
    }

    /**
     * Validacion para que se seleccione un Proveedor del Autocompletar
     *
     * @return
     */
    private boolean isCuentaSeleccionada() {
        if (aut_cuenta.getValor() == null) {
            utilitario.agregarMensajeInfo("Debe seleccionar una Cuenta Contable", "Seleccione una cuenta contable de la lista del Autocompletar");
            return false;
        }
        return true;
    }

    /**
     * Limpia el autocompletar y la tabla de consulta
     */
    public void limpiar() {
        aut_cuenta.limpiar();
        if (aut_persona != null) {
            aut_persona.limpiar();
        }
        tab_consulta.limpiar();
    }

    @Override
    public void insertar() {
    }

    @Override
    public void guardar() {
    }

    @Override
    public void eliminar() {
    }

    public void aceptarCierrePeriodo() {
        dia_cerrar_periodo.cerrar();
    }

    @Override
    public void aceptarReporte() {
//Se ejecuta cuando se selecciona un reporte de la lista        
        if (rep_reporte.getReporteSelecionado().equals("Libro Diario")) {
            if (rep_reporte.isVisible()) {
                parametro = new HashMap();
                rep_reporte.cerrar();
                sec_rango_reporte.setMultiple(true);
                sec_rango_reporte.dibujar();
                utilitario.addUpdate("rep_reporte,sec_rango_reporte");

            } else if (sec_rango_reporte.isVisible()) {
                String estado = "" + utilitario.getVariable("p_con_estado_comprobante_normal") + "," + utilitario.getVariable("p_con_estado_comp_inicial") + "," + utilitario.getVariable("p_con_estado_comp_final");
                parametro.put("fecha_inicio", sec_rango_reporte.getFecha1());
                parametro.put("fecha_fin", sec_rango_reporte.getFecha2());

                parametro.put("ide_cneco", estado);
                parametro.put("ide_cnlap_haber", p_con_lugar_haber);
                parametro.put("ide_cnlap_debe", p_con_lugar_debe);
                sec_rango_reporte.cerrar();
                sel_rep.setSeleccionFormatoReporte(parametro, rep_reporte.getPath());
                sel_rep.dibujar();
                utilitario.addUpdate("sel_rep,sec_rango_reporte");
            }
        } else if (rep_reporte.getReporteSelecionado().equals("Balance General Consolidado")) {
            if (rep_reporte.isVisible()) {
                parametro = new HashMap();
                rep_reporte.cerrar();
                sec_rango_reporte.setMultiple(true);
                sec_rango_reporte.dibujar();
                utilitario.addUpdate("rep_reporte,sec_rango_reporte");
            } else if (sec_rango_reporte.isVisible()) {
                if (sec_rango_reporte.getFecha1String() != null && !sec_rango_reporte.getFecha1String().isEmpty()) {
                    if (sec_rango_reporte.getFecha2String() != null && !sec_rango_reporte.getFecha2String().isEmpty()) {
                        fecha_fin = sec_rango_reporte.getFecha2String();
                        fecha_inicio = con.getFechaInicialPeriodo(fecha_fin);
                        if (fecha_inicio != null && !fecha_inicio.isEmpty()) {
                            sec_rango_reporte.cerrar();
                            sel_tab_nivel.dibujar();
                            utilitario.addUpdate("sec_rango_reporte,sel_tab_nivel");
                        } else {
                            utilitario.agregarMensajeError("Atencion", "El rango de fechas seleccionado no se encuentra en ningun Periodo Contable");
                        }
                    } else {
                        utilitario.agregarMensajeError("Atencion", "No ha seleccionado la fecha final");
                    }
                } else {
                    utilitario.agregarMensajeError("Atencion", "No ha seleccionado la fecha inicial");
                }
            } else if (sel_tab_nivel.isVisible()) {
                if (sel_tab_nivel.getValorSeleccionado() != null) {
                    System.out.println("fecha fin " + fecha_fin);
                    parametro.put("p_activo", utilitario.getVariable("p_con_tipo_cuenta_activo"));
                    parametro.put("p_pasivo", utilitario.getVariable("p_con_tipo_cuenta_pasivo"));
                    parametro.put("p_patrimonio", utilitario.getVariable("p_con_tipo_cuenta_patrimonio"));
                    TablaGenerica tab_datos = utilitario.consultar("SELECT * FROM sis_empresa e, sis_sucursal s where s.ide_empr=e.ide_empr and s.ide_empr=" + utilitario.getVariable("ide_empr") + " and s.ide_sucu=" + utilitario.getVariable("ide_sucu"));
                    if (tab_datos.getTotalFilas() > 0) {
                        parametro.put("logo", "upload/logos/logo_reporte.png");
                        parametro.put("empresa", tab_datos.getValor(0, "nom_empr"));
                        parametro.put("sucursal", tab_datos.getValor(0, "nom_sucu"));
                        parametro.put("direccion", tab_datos.getValor(0, "direccion_sucu"));
                        parametro.put("telefono", tab_datos.getValor(0, "telefonos_sucu"));
                        parametro.put("ruc", tab_datos.getValor(0, "identificacion_empr"));

                    }
                    parametro.put("fecha_inicio", getFormatoFecha(fecha_inicio));
                    parametro.put("fecha_fin", getFormatoFecha(fecha_fin));
                    TablaGenerica tab_balance = con.generarBalanceGeneral(true, fecha_inicio, fecha_fin, Integer.parseInt(sel_tab_nivel.getValorSeleccionado()));
                    parametro.put("titulo", "BALANCE GENERAL CONSOLIDADO");
                    if (tab_balance.getTotalFilas() > 0) {
                        List lis_totales = con.obtenerTotalesBalanceGeneral(true, fecha_inicio, fecha_fin);
                        double tot_activo = Double.parseDouble(lis_totales.get(0) + "");
                        double tot_pasivo = Double.parseDouble(lis_totales.get(1) + "");
                        double tot_patrimonio = Double.parseDouble(lis_totales.get(2) + "");
                        double utilidad_perdida = tot_activo - tot_pasivo - tot_patrimonio;
                        double total = tot_pasivo + tot_patrimonio + utilidad_perdida;
                        parametro.put("p_tot_activo", tot_activo);
                        parametro.put("p_total", total);
                        parametro.put("p_utilidad_perdida", utilidad_perdida);
                        parametro.put("p_tot_pasivo", tot_pasivo);
                        parametro.put("p_tot_patrimonio", (tot_patrimonio));
                    }
                    sel_tab_nivel.cerrar();
                    ReporteDataSource data = new ReporteDataSource(tab_balance);
                    sel_rep.setSeleccionFormatoReporte(parametro, rep_reporte.getPath(), data);
                    sel_rep.dibujar();

                    utilitario.addUpdate("sel_rep,sel_tab_nivel");
                }
            }
        } else if (rep_reporte.getReporteSelecionado().equals("Balance General")) {
            if (rep_reporte.isVisible()) {
                parametro = new HashMap();
                rep_reporte.cerrar();
                sec_rango_reporte.setMultiple(true);
                sec_rango_reporte.dibujar();
                utilitario.addUpdate("rep_reporte,sec_rango_reporte");
            } else if (sec_rango_reporte.isVisible()) {
                if (sec_rango_reporte.getFecha1String() != null && !sec_rango_reporte.getFecha1String().isEmpty()) {
                    if (sec_rango_reporte.getFecha2String() != null && !sec_rango_reporte.getFecha2String().isEmpty()) {
                        fecha_fin = sec_rango_reporte.getFecha2String();
                        fecha_inicio = con.getFechaInicialPeriodo(fecha_fin);
                        if (fecha_inicio != null && !fecha_inicio.isEmpty()) {
                            sec_rango_reporte.cerrar();
                            sel_tab_nivel.dibujar();
                            utilitario.addUpdate("sec_rango_reporte,sel_tab_nivel");
                        } else {
                            utilitario.agregarMensajeError("Atencion", "El rango de fechas seleccionado no se encuentra en ningun Periodo Contable");
                        }

                    } else {
                        utilitario.agregarMensajeError("Atencion", "No ha seleccionado la fecha final");
                    }
                } else {
                    utilitario.agregarMensajeError("Atencion", "No ha seleccionado la fecha inicial");
                }
            } else if (sel_tab_nivel.isVisible()) {
                if (sel_tab_nivel.getValorSeleccionado() != null) {
                    System.out.println("fecha fin " + fecha_fin);
                    parametro.put("p_activo", utilitario.getVariable("p_con_tipo_cuenta_activo"));
                    parametro.put("p_pasivo", utilitario.getVariable("p_con_tipo_cuenta_pasivo"));
                    parametro.put("p_patrimonio", utilitario.getVariable("p_con_tipo_cuenta_patrimonio"));
                    TablaGenerica tab_datos = utilitario.consultar("SELECT * FROM sis_empresa e, sis_sucursal s where s.ide_empr=e.ide_empr and s.ide_empr=" + utilitario.getVariable("ide_empr") + " and s.ide_sucu=" + utilitario.getVariable("ide_sucu"));
                    if (tab_datos.getTotalFilas() > 0) {
                        parametro.put("logo", "upload/logos/logo_reporte.png");
                        parametro.put("empresa", tab_datos.getValor(0, "nom_empr"));
                        parametro.put("sucursal", tab_datos.getValor(0, "nom_sucu"));
                        parametro.put("direccion", tab_datos.getValor(0, "direccion_sucu"));
                        parametro.put("telefono", tab_datos.getValor(0, "telefonos_sucu"));
                        parametro.put("ruc", tab_datos.getValor(0, "identificacion_empr"));

                    }
                    parametro.put("fecha_inicio", getFormatoFecha(fecha_inicio));
                    parametro.put("fecha_fin", getFormatoFecha(fecha_fin));
                    TablaGenerica tab_balance = con.generarBalanceGeneral(false, fecha_inicio, fecha_fin, Integer.parseInt(sel_tab_nivel.getValorSeleccionado()));
                    parametro.put("titulo", "BALANCE GENERAL");
                    if (tab_balance.getTotalFilas() > 0) {
                        List lis_totales = con.obtenerTotalesBalanceGeneral(false, fecha_inicio, fecha_fin);
                        double tot_activo = Double.parseDouble(lis_totales.get(0) + "");
                        double tot_pasivo = Double.parseDouble(lis_totales.get(1) + "");
                        double tot_patrimonio = Double.parseDouble(lis_totales.get(2) + "");
                        double utilidad_perdida = tot_activo - tot_pasivo - tot_patrimonio;
                        double total = tot_pasivo + tot_patrimonio + utilidad_perdida;
                        parametro.put("p_tot_activo", tot_activo);
                        parametro.put("p_total", total);
                        parametro.put("p_utilidad_perdida", utilidad_perdida);
                        parametro.put("p_tot_pasivo", tot_pasivo);
                        parametro.put("p_tot_patrimonio", (tot_patrimonio));
                    }
                    sel_tab_nivel.cerrar();
                    ReporteDataSource data = new ReporteDataSource(tab_balance);
                    sel_rep.setSeleccionFormatoReporte(parametro, rep_reporte.getPath(), data);
                    sel_rep.dibujar();

                    utilitario.addUpdate("sel_rep,sel_tab_nivel");
                }
            }
        } else if (rep_reporte.getReporteSelecionado().equals("Estado de Resultados Consolidado")) {
            if (rep_reporte.isVisible()) {
                parametro = new HashMap();
                rep_reporte.cerrar();
                sec_rango_reporte.setMultiple(true);
                sec_rango_reporte.dibujar();
                utilitario.addUpdate("rep_reporte,sec_rango_reporte");
            } else if (sec_rango_reporte.isVisible()) {
                if (sec_rango_reporte.getFecha1String() != null && !sec_rango_reporte.getFecha1String().isEmpty()) {
                    if (sec_rango_reporte.getFecha2String() != null && !sec_rango_reporte.getFecha2String().isEmpty()) {
                        fecha_fin = sec_rango_reporte.getFecha2String();
                        fecha_inicio = con.getFechaInicialPeriodo(fecha_fin);
                        if (fecha_inicio != null && !fecha_inicio.isEmpty()) {
                            sec_rango_reporte.cerrar();
                            sel_tab_nivel.dibujar();
                            utilitario.addUpdate("sec_rango_reporte,sel_tab_nivel");
                        }
                    } else {
                        utilitario.agregarMensajeError("Atencion", "No ha seleccionado la fecha fin");
                    }
                } else {
                    utilitario.agregarMensajeError("Atencion", "No ha seleccionado la fecha inicio");
                }
            } else if (sel_tab_nivel.isVisible()) {
                if (sel_tab_nivel.getValorSeleccionado() != null) {
                    parametro.put("p_ingresos", utilitario.getVariable("p_con_tipo_cuenta_ingresos"));
                    parametro.put("p_gastos", utilitario.getVariable("p_con_tipo_cuenta_gastos"));
                    parametro.put("p_costos", utilitario.getVariable("p_con_tipo_cuenta_costos"));
                    TablaGenerica tab_datos = utilitario.consultar("SELECT * FROM sis_empresa e, sis_sucursal s where s.ide_empr=e.ide_empr and s.ide_empr=" + utilitario.getVariable("ide_empr") + " and s.ide_sucu=" + utilitario.getVariable("ide_sucu"));
                    if (tab_datos.getTotalFilas() > 0) {
                        parametro.put("logo", "upload/logos/logo_reporte.png");
                        parametro.put("empresa", tab_datos.getValor(0, "nom_empr"));
                        parametro.put("sucursal", tab_datos.getValor(0, "nom_sucu"));
                        parametro.put("direccion", tab_datos.getValor(0, "direccion_sucu"));
                        parametro.put("telefono", tab_datos.getValor(0, "telefonos_sucu"));
                        parametro.put("ruc", tab_datos.getValor(0, "identificacion_empr"));
                    }

                    parametro.put("fecha_inicio", getFormatoFecha(fecha_inicio));
                    parametro.put("fecha_fin", getFormatoFecha(fecha_fin));
                    TablaGenerica tab_estado = con.generarEstadoResultados(true, fecha_inicio, fecha_fin, Integer.parseInt(sel_tab_nivel.getValorSeleccionado()));
                    if (tab_estado.getTotalFilas() > 0) {
                        List lis_totales = con.obtenerTotalesEstadoResultados(true, fecha_inicio, fecha_fin);
                        double tot_ingresos = Double.parseDouble(lis_totales.get(0) + "");
                        double tot_gastos = Double.parseDouble(lis_totales.get(1) + "");
                        double tot_costos = Double.parseDouble(lis_totales.get(2) + "");
                        double utilidad_perdida = tot_ingresos - (tot_gastos + tot_costos);
                        parametro.put("p_tot_ingresos", tot_ingresos);
                        parametro.put("p_tot_gastos", tot_gastos);
                        parametro.put("p_tot_costos", tot_costos);
                        parametro.put("p_utilidad", utilidad_perdida);
                    }
                    parametro.put("titulo", "ESTADO DE RESULTADOS CONSOLIDADO");
                    ReporteDataSource data = new ReporteDataSource(tab_estado);
                    sel_tab_nivel.cerrar();
                    sel_rep.setSeleccionFormatoReporte(parametro, rep_reporte.getPath(), data);
                    sel_rep.dibujar();
                    utilitario.addUpdate("sel_rep,sel_tab_nivel");
                }
            }

        } else if (rep_reporte.getReporteSelecionado().equals("Estado de Resultados")) {
            if (rep_reporte.isVisible()) {
                parametro = new HashMap();
                rep_reporte.cerrar();
                sec_rango_reporte.setMultiple(true);
                sec_rango_reporte.dibujar();
                utilitario.addUpdate("rep_reporte,sec_rango_reporte");
            } else if (sec_rango_reporte.isVisible()) {
                if (sec_rango_reporte.getFecha1String() != null && !sec_rango_reporte.getFecha1String().isEmpty()) {
                    if (sec_rango_reporte.getFecha2String() != null && !sec_rango_reporte.getFecha2String().isEmpty()) {
                        fecha_fin = sec_rango_reporte.getFecha2String();
                        fecha_inicio = con.getFechaInicialPeriodo(fecha_fin);
                        if (fecha_inicio != null && !fecha_inicio.isEmpty()) {
                            sec_rango_reporte.cerrar();
                            sel_tab_nivel.dibujar();
                            utilitario.addUpdate("sec_rango_reporte,sel_tab_nivel");
                        }
                    } else {
                        utilitario.agregarMensajeError("Atencion", "No ha seleccionado la fecha fin");
                    }
                } else {
                    utilitario.agregarMensajeError("Atencion", "No ha seleccionado la fecha inicio");
                }
            } else if (sel_tab_nivel.isVisible()) {
                if (sel_tab_nivel.getValorSeleccionado() != null) {
                    parametro.put("p_ingresos", utilitario.getVariable("p_con_tipo_cuenta_ingresos"));
                    parametro.put("p_gastos", utilitario.getVariable("p_con_tipo_cuenta_gastos"));
                    parametro.put("p_costos", utilitario.getVariable("p_con_tipo_cuenta_costos"));
                    TablaGenerica tab_datos = utilitario.consultar("SELECT * FROM sis_empresa e, sis_sucursal s where s.ide_empr=e.ide_empr and s.ide_empr=" + utilitario.getVariable("ide_empr") + " and s.ide_sucu=" + utilitario.getVariable("ide_sucu"));
                    if (tab_datos.getTotalFilas() > 0) {
                        parametro.put("logo", "upload/logos/logo_reporte.png");
                        parametro.put("empresa", tab_datos.getValor(0, "nom_empr"));
                        parametro.put("sucursal", tab_datos.getValor(0, "nom_sucu"));
                        parametro.put("direccion", tab_datos.getValor(0, "direccion_sucu"));
                        parametro.put("telefono", tab_datos.getValor(0, "telefonos_sucu"));
                        parametro.put("ruc", tab_datos.getValor(0, "identificacion_empr"));
                    }

                    parametro.put("fecha_inicio", getFormatoFecha(fecha_inicio));
                    parametro.put("fecha_fin", getFormatoFecha(fecha_fin));
                    TablaGenerica tab_estado = con.generarEstadoResultados(false, fecha_inicio, fecha_fin, Integer.parseInt(sel_tab_nivel.getValorSeleccionado()));
                    if (tab_estado.getTotalFilas() > 0) {
                        List lis_totales = con.obtenerTotalesEstadoResultados(false, fecha_inicio, fecha_fin);
                        double tot_ingresos = Double.parseDouble(lis_totales.get(0) + "");
                        double tot_gastos = Double.parseDouble(lis_totales.get(1) + "");
                        double tot_costos = Double.parseDouble(lis_totales.get(2) + "");
                        double utilidad_perdida = tot_ingresos - (tot_gastos + tot_costos);
                        parametro.put("p_tot_ingresos", tot_ingresos);
                        parametro.put("p_tot_gastos", tot_gastos);
                        parametro.put("p_tot_costos", tot_costos);
                        parametro.put("p_utilidad", utilidad_perdida);
                    }
                    ReporteDataSource data = new ReporteDataSource(tab_estado);
                    parametro.put("titulo", "ESTADO DE RESULTADOS");
                    sel_tab_nivel.cerrar();
                    sel_rep.setSeleccionFormatoReporte(parametro, rep_reporte.getPath(), data);
                    sel_rep.dibujar();
                    utilitario.addUpdate("sel_rep,sel_tab_nivel");
                }
            }

        } else if (rep_reporte.getReporteSelecionado().equals("Libro Mayor")) {
            if (rep_reporte.isVisible()) {
                parametro = new HashMap();
                rep_reporte.cerrar();
                lis_ide_cndpc_sel.clear();
                lis_ide_cndpc_deseleccionados.clear();
                int_count_deseleccion = 0;
                int_count_seleccion = 0;
                sel_tab.getTab_seleccion().setSeleccionados(null);
//                utilitario.ejecutarJavaScript(sel_tab.getTab_seleccion().getId() + ".clearFilters();");
                sel_tab.dibujar();
                utilitario.addUpdate("rep_reporte,sel_tab");
            } else {
                if (sel_tab.isVisible()) {

                    if (sel_tab.getSeleccionados() != null && !sel_tab.getSeleccionados().isEmpty()) {
                        System.out.println("nn " + sel_tab.getSeleccionados());
                        parametro.put("ide_cndpc", sel_tab.getSeleccionados());//lista sel                     
                        sel_tab.cerrar();
                        String estado = "" + utilitario.getVariable("p_con_estado_comprobante_normal") + "," + utilitario.getVariable("p_con_estado_comp_inicial") + "," + utilitario.getVariable("p_con_estado_comp_final");
                        parametro.put("ide_cneco", estado);
                        sec_rango_reporte.setMultiple(true);
                        sec_rango_reporte.dibujar();
                        utilitario.addUpdate("sel_tab,sec_rango_reporte");

                    } else {
                        utilitario.agregarMensajeInfo("Debe seleccionar al menos una cuenta contable", "");
                    }
//                    if (lis_ide_cndpc_deseleccionados.size() == 0) {
//                        System.out.println("sel tab lis " + sel_tab.getSeleccionados());
//                        parametro.put("ide_cndpc", sel_tab.getSeleccionados());//lista sel                     
//                    } else {
//                        System.out.println("sel tab " + utilitario.generarComillasLista(lis_ide_cndpc_deseleccionados));
//                        parametro.put("ide_cndpc", utilitario.generarComillasLista(lis_ide_cndpc_deseleccionados));//lista sel                     
//                    }
                } else if (sec_rango_reporte.isVisible()) {
                    if (sec_rango_reporte.isFechasValidas()) {
                        parametro.put("fecha_inicio", sec_rango_reporte.getFecha1());
                        parametro.put("fecha_fin", sec_rango_reporte.getFecha2());
                        parametro.put("ide_cnlap_haber", p_con_lugar_haber);
                        parametro.put("ide_cnlap_debe", p_con_lugar_debe);
                        sec_rango_reporte.cerrar();
                        sel_rep.setSeleccionFormatoReporte(parametro, rep_reporte.getPath());
                        sel_rep.dibujar();
                        utilitario.addUpdate("sel_rep,sec_rango_reporte");
                    } else {
                        utilitario.agregarMensajeInfo("Las fechas seleccionadas no son correctas", "");
                    }
                }
            }
        } else if (rep_reporte.getReporteSelecionado().equals("Balance de Comprobacion")) {
            if (rep_reporte.isVisible()) {
                parametro = new HashMap();
                rep_reporte.cerrar();
                sec_rango_reporte.setMultiple(true);
                sec_rango_reporte.dibujar();
                utilitario.addUpdate("rep_reporte,sec_rango_reporte");

            } else {
                if (sec_rango_reporte.isVisible()) {
                    if (sec_rango_reporte.getFecha1String() != null && !sec_rango_reporte.getFecha1String().isEmpty()) {
                        if (sec_rango_reporte.getFecha2String() != null && !sec_rango_reporte.getFecha2String().isEmpty()) {
                            String fecha_fin1 = sec_rango_reporte.getFecha2String();
                            String fecha_inicio1 = sec_rango_reporte.getFecha1String();
                            System.out.println("fecha fin " + fecha_fin1);
                            sec_rango_reporte.cerrar();

                            TablaGenerica tab_datos = utilitario.consultar("SELECT * FROM sis_empresa e, sis_sucursal s where s.ide_empr=e.ide_empr and s.ide_empr=" + utilitario.getVariable("ide_empr") + " and s.ide_sucu=" + utilitario.getVariable("ide_sucu"));
                            if (tab_datos.getTotalFilas() > 0) {
                                parametro.put("logo", "upload/logos/logo_reporte.png");
                                parametro.put("empresa", tab_datos.getValor(0, "nom_empr"));
                                parametro.put("sucursal", tab_datos.getValor(0, "nom_sucu"));
                                parametro.put("direccion", tab_datos.getValor(0, "direccion_sucu"));
                                parametro.put("telefono", tab_datos.getValor(0, "telefonos_sucu"));
                                parametro.put("ruc", tab_datos.getValor(0, "identificacion_empr"));
                            }
                            String fechaPeriodoActivo = con.obtenerFechaInicialPeriodoActivo();
//                        if (fechaPeriodoActivo != null && !fechaPeriodoActivo.isEmpty()) {
                            parametro.put("fecha_inicio", getFormatoFecha(fecha_inicio1));
                            parametro.put("fecha_fin", getFormatoFecha(fecha_fin1));
                            TablaGenerica tab_bal = con.generarBalanceComprobacion(fechaPeriodoActivo, fecha_fin1);
                            double suma_debe = 0;
                            double suma_haber = 0;
                            double suma_deudor = 0;
                            double suma_acreedor = 0;
                            for (int i = 0; i < tab_bal.getTotalFilas() - 1; i++) {
                                suma_debe = Double.parseDouble(tab_bal.getValor(i, "debe")) + suma_debe;
                                suma_haber = Double.parseDouble(tab_bal.getValor(i, "haber")) + suma_haber;
                                suma_deudor = Double.parseDouble(tab_bal.getValor(i, "deudor")) + suma_deudor;
                                suma_acreedor = Double.parseDouble(tab_bal.getValor(i, "acreedor")) + suma_acreedor;
                            }
                            parametro.put("tot_debe", suma_debe);
                            parametro.put("tot_haber", suma_haber);
                            parametro.put("tot_deudor", suma_deudor);
                            parametro.put("tot_acreedor", suma_acreedor);
                            ReporteDataSource data = new ReporteDataSource(tab_bal);
                            sel_rep.setSeleccionFormatoReporte(parametro, rep_reporte.getPath(), data);
                            sel_rep.dibujar();
                            utilitario.addUpdate("sel_rep,sec_rango_reporte");
                        }
//                    }
                    } else {
                        utilitario.agregarMensajeError("Atencion", "No ha seleccionado la fecha fin");
                    }
                } else {
                    utilitario.agregarMensajeError("Atencion", "No ha seleccionado la fecha inicio");
                }
            }

        }
    }
    private List lis_ide_cndpc_sel = new ArrayList();
    private int int_count_seleccion = 0;

    public void seleccionaCuentaContable(SelectEvent evt) {
        sel_tab.getTab_seleccion().seleccionarFila(evt);
        for (Fila actual : sel_tab.getTab_seleccion().getSeleccionados()) {
            int band = 0;
            for (int i = 0; i < lis_ide_cndpc_sel.size(); i++) {
                if (actual.getRowKey().equals(lis_ide_cndpc_sel.get(i))) {
                    band = 1;
                    break;
                }
            }
            if (band == 0) {
                lis_ide_cndpc_sel.add(actual.getRowKey());
            }
        }
        if (int_count_seleccion == 0) {
            lis_ide_cndpc_deseleccionados = lis_ide_cndpc_sel;
        }
        int_count_seleccion += 1;

    }

    public void deseleccionaCuentaContable(UnselectEvent evt) {
        //tab_tabla2.modificar(evt);
        for (Fila actual : sel_tab.getTab_seleccion().getSeleccionados()) {
            int band = 0;
            for (int i = 0; i < lis_ide_cndpc_deseleccionados.size(); i++) {
                if (actual.getRowKey().equals(lis_ide_cndpc_deseleccionados.get(i))) {
                    band = 1;
                    break;
                }
            }
            if (band == 0) {
                lis_ide_cndpc_deseleccionados.add(actual.getRowKey());
            }
        }
        int_count_deseleccion += 1;
    }

    /**
     * Abre el asiento contable seleccionado
     *
     * @param evt
     */
    public void abrirAsiento(ActionEvent evt) {
        Link lin_ide_cnccc = (Link) evt.getComponent();
        asc_asiento.setAsientoContable(lin_ide_cnccc.getValue().toString());
        tab_consulta.setFilaActual(lin_ide_cnccc.getDir());
        asc_asiento.dibujar();
    }

    public String getFormatoFecha(String fecha) {
        String mes = utilitario.getNombreMes(utilitario.getMes(fecha));
        String dia = utilitario.getDia(fecha) + "";
        String anio = utilitario.getAnio(fecha) + "";
        String fecha_formato = dia + " DE " + mes + " DEL " + anio;
        return fecha_formato;
    }

    @Override
    public void abrirListaReportes() {
//Se ejecuta cuando da click en el boton de Reportes de la Barra 
        sec_rango_reporte.getCal_fecha1().setValue(null);
        sec_rango_reporte.getCal_fecha2().setValue(null);
        rep_reporte.dibujar();

    }

    public AutoCompletar getAut_cuenta() {
        return aut_cuenta;
    }

    public void setAut_cuenta(AutoCompletar aut_cuenta) {
        this.aut_cuenta = aut_cuenta;
    }

    public Tabla getTab_consulta() {
        return tab_consulta;
    }

    public void setTab_consulta(Tabla tab_consulta) {
        this.tab_consulta = tab_consulta;
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

    public SeleccionTabla getSel_tab_nivel() {
        return sel_tab_nivel;
    }

    public void setSel_tab_nivel(SeleccionTabla sel_tab_nivel) {
        this.sel_tab_nivel = sel_tab_nivel;
    }

    public SeleccionCalendario getSec_rango_reporte() {
        return sec_rango_reporte;
    }

    public void setSec_rango_reporte(SeleccionCalendario sec_rango_reporte) {
        this.sec_rango_reporte = sec_rango_reporte;
    }

    public AsientoContable getAsc_asiento() {
        return asc_asiento;
    }

    public void setAsc_asiento(AsientoContable asc_asiento) {
        this.asc_asiento = asc_asiento;
    }

    public Dialogo getDia_cerrar_periodo() {
        return dia_cerrar_periodo;
    }

    public void setDia_cerrar_periodo(Dialogo dia_cerrar_periodo) {
        this.dia_cerrar_periodo = dia_cerrar_periodo;
    }

    public AutoCompletar getAut_persona() {
        return aut_persona;
    }

    public void setAut_persona(AutoCompletar aut_persona) {
        this.aut_persona = aut_persona;
    }

}
