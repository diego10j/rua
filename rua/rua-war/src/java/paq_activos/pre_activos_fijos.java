/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_activos;

import framework.componentes.AutoCompletar;
import framework.componentes.Barra;
import framework.componentes.Boton;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.Grupo;
import framework.componentes.MenuPanel;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionArbol;
import framework.componentes.SeleccionCalendario;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.Tabla;
import java.util.HashMap;
import java.util.Map;
import javax.ejb.EJB;
import org.primefaces.component.separator.Separator;
import org.primefaces.event.SelectEvent;
import servicios.activos.ServicioActivosFijos;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author DIEGOFERNANDOJACOMEG
 */
public class pre_activos_fijos extends Pantalla {

    private final MenuPanel mep_menu = new MenuPanel();

    @EJB
    private final ServicioActivosFijos ser_activos = (ServicioActivosFijos) utilitario.instanciarEJB(ServicioActivosFijos.class);

    private Tabla tab_tabla;
    private Tabla tab_tabla2;
    private Tabla tab_tabla3;
    private AutoCompletar aut_custodio;

    private Reporte rep_reporte = new Reporte();
    private SeleccionFormatoReporte sel_rep = new SeleccionFormatoReporte();
    private SeleccionArbol sel_arb = new SeleccionArbol();
    private SeleccionCalendario sec_rango_reporte = new SeleccionCalendario();

    public pre_activos_fijos() {
        bar_botones.quitarBotonsNavegacion();
        bar_botones.agregarReporte();

        mep_menu.setMenuPanel("OPCIONES ACTIVOS FIJOS", "21%");
        mep_menu.agregarItem("Listado de Activos", "dibujarListadosActivos", "ui-icon-note");//1
        mep_menu.agregarItem("Asignar Activos", "dibujarAsignarActivos", "ui-icon-cart");//5
        mep_menu.agregarItem("Depreciar Activos", "dibujarDepreciar", "ui-icon-clock");//6
        mep_menu.agregarItem("Dar de Baja Activos", "dibujarDardeBaja", "ui-icon-cancel");//7
        mep_menu.agregarSubMenu("INFORMES");
        mep_menu.agregarItem("Consultar Activos Asignados", "dibujarConsultarAsignados", "ui-icon-contact");//3
        mep_menu.agregarItem("Activos Dados de Baja", "dibujarActDadosBaja", "ui-icon-note");//8
        agregarComponente(mep_menu);
        dibujarListadosActivos();

        rep_reporte.setId("rep_reporte");
        rep_reporte.getBot_aceptar().setMetodo("aceptarReporte");
        agregarComponente(rep_reporte);

        sec_rango_reporte.setId("sec_rango_reporte");
        sec_rango_reporte.getBot_aceptar().setMetodo("aceptarReporte");
        sec_rango_reporte.setMultiple(false);
        agregarComponente(sec_rango_reporte);

        sel_rep.setId("sel_rep");
        agregarComponente(sel_rep);
        sel_arb.setId("sel_arb");
        sel_arb.setSeleccionArbol("inv_articulo", "ide_inarti", "nombre_inarti", "inv_ide_inarti");
        sel_arb.getArb_seleccion().setCondicion("ide_inarti=" + utilitario.getVariable("p_inv_articulo_activo_fijo"));
        sel_arb.getBot_aceptar().setMetodo("aceptarReporte");
        agregarComponente(sel_arb);

    }

    @Override
    public void abrirListaReportes() {
        rep_reporte.dibujar();
    }
    Map parametro = new HashMap();

    @Override
    public void aceptarReporte() {
        if (rep_reporte.getReporteSelecionado().equals("Activos Fijos")) {
            if (rep_reporte.isVisible()) {
                parametro = new HashMap();
                rep_reporte.cerrar();
                sel_arb.dibujar();
                utilitario.addUpdate("rep_reporte,sel_arb");
            } else if (sel_arb.isVisible()) {
                System.out.println("ides " + sel_arb.getSeleccionados());
                parametro.put("ide_inarti", sel_arb.getSeleccionados());
                sel_arb.cerrar();
                sel_rep.setSeleccionFormatoReporte(parametro, rep_reporte.getPath());
                sel_rep.dibujar();
                utilitario.addUpdate("sel_arb,sel_rep");
            }

        } else if (rep_reporte.getReporteSelecionado().equals("Depreciacion Activos Fijos")) {
            if (rep_reporte.isVisible()) {
                parametro = new HashMap();
                rep_reporte.cerrar();
                sec_rango_reporte.setMultiple(false);
                sec_rango_reporte.dibujar();
                utilitario.addUpdate("rep_reporte,sec_rango_reporte");
            } else if (sec_rango_reporte.isVisible()) {
                parametro.put("fecha_fin", sec_rango_reporte.getFecha1());
                sec_rango_reporte.cerrar();
                sel_arb.dibujar();
                utilitario.addUpdate("rep_reporte,sel_arb");
            } else if (sel_arb.isVisible()) {
                System.out.println("ides " + sel_arb.getSeleccionados());
                parametro.put("ide_inarti", sel_arb.getSeleccionados());
                sel_arb.cerrar();
                sel_rep.setSeleccionFormatoReporte(parametro, rep_reporte.getPath());
                sel_rep.dibujar();
                utilitario.addUpdate("sel_arb,sel_rep");
            }

        } else if (rep_reporte.getReporteSelecionado().equals("Detalle Depreciacion Activos")) {
            if (rep_reporte.isVisible()) {
                parametro = new HashMap();
                rep_reporte.cerrar();
                sec_rango_reporte.setMultiple(true);
                sec_rango_reporte.dibujar();
                utilitario.addUpdate("rep_reporte,sec_rango_reporte");
            } else if (sec_rango_reporte.isVisible()) {
                parametro.put("fecha_ini", sec_rango_reporte.getFecha1());
                parametro.put("fecha_fin", sec_rango_reporte.getFecha2());
                sec_rango_reporte.cerrar();
                sel_arb.dibujar();
                utilitario.addUpdate("rep_reporte,sel_arb");
            } else if (sel_arb.isVisible()) {
                System.out.println("ides " + sel_arb.getSeleccionados());
                parametro.put("ide_inarti", sel_arb.getSeleccionados());
                sel_arb.cerrar();
                sel_rep.setSeleccionFormatoReporte(parametro, rep_reporte.getPath());
                sel_rep.dibujar();
                utilitario.addUpdate("sel_arb,sel_rep");
            }
        }
    }

    public void dibujarDepreciar() {
        Grid grm = new Grid();
        grm.setMensajeError("Verificar los grupos de Activos Fijos");
        mep_menu.dibujar(6, "DEPRECIAR ACTIVOS FIJOS", grm);
    }

    public void dibujarDardeBaja() {
        Grupo gru = new Grupo();

        tab_tabla = new Tabla();
        tab_tabla.setId("tab_tabla");
        tab_tabla.setTabla("act_transaccion", "ide_actra", -1);
        tab_tabla.getColumna("ide_actra").setVisible(false);
        tab_tabla.getColumna("ide_acafi").setVisible(false);
        tab_tabla.getColumna("ide_usua").setVisible(false);
        tab_tabla.getColumna("ide_cnccc").setVisible(false);
        tab_tabla.getColumna("acumulado_actra").setVisible(false);
        tab_tabla.getColumna("valor_actra").setVisible(false);
        tab_tabla.getColumna("valor_activo_actra").setVisible(false);
        tab_tabla.getColumna("cantidad_actra").setVisible(false);
        tab_tabla.getColumna("ide_acttr").setCombo("act_tipo_transaccion", "ide_acttr", "nombre_acttr", "ide_acttr=1");
        tab_tabla.getColumna("ide_acttr").setValorDefecto("1"); //Dar de baja
        tab_tabla.getColumna("ide_acttr").setLectura(true);
        tab_tabla.getColumna("ide_acttr").setNombreVisual("TIPO TRANSACCION");
        tab_tabla.getColumna("fecha_actra").setNombreVisual("FECHA");
        tab_tabla.getColumna("fecha_actra").setValorDefecto(utilitario.getFechaActual());
        tab_tabla.getColumna("observacion_actra").setNombreVisual("OBSERVACION");
        //tab_tabla.getColumna("valor_actra").setNombreVisual("VALOR TRANSACCION");
        tab_tabla.setTipoFormulario(true);
        tab_tabla.getGrid().setColumns(4);
        tab_tabla.setMostrarNumeroRegistros(false);
        tab_tabla.dibujar();
        tab_tabla.insertar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_tabla);
        pat_panel.getMenuTabla().setRendered(false);
        gru.getChildren().add(pat_panel);
        gru.getChildren().add(new Separator());

        tab_tabla2 = new Tabla();

        tab_tabla2.setId("tab_seleccion");
        tab_tabla2.setSql(ser_activos.getSqlListaActivosFijos());
        tab_tabla2.setCampoPrimaria("ide_acafi");
        tab_tabla2.getColumna("ide_acafi").setVisible(false);
        tab_tabla2.getColumna("serie_acafi").setFiltroContenido();
        tab_tabla2.getColumna("nombre_acafi").setFiltroContenido();
        tab_tabla2.getColumna("descripcion1_acafi").setFiltroContenido();
        tab_tabla2.getColumna("nom_geper").setFiltroContenido();
        tab_tabla2.getColumna("nom_geper").setNombreVisual("CUSTODIO");
        tab_tabla2.getColumna("nombre_aceaf").setFiltroContenido();
        tab_tabla2.getColumna("nombre_inarti").setFiltroContenido();
        tab_tabla2.getColumna("anos_uso_acafi").alinearDerecha();
        tab_tabla2.getColumna("vida_util_acafi").alinearDerecha();
        tab_tabla2.getColumna("valor_compra_acafi").alinearDerecha();
        tab_tabla2.getColumna("valor_comercial_acafi").alinearDerecha();
        tab_tabla2.getColumna("valor_remate_acafi").alinearDerecha();
        tab_tabla2.setTipoSeleccion(true);
        tab_tabla2.setSeleccionTabla("multiple");
        tab_tabla2.setRows(10);
        tab_tabla2.dibujar();
        PanelTabla pat_panel2 = new PanelTabla();
        pat_panel2.setPanelTabla(tab_tabla2);

        Grid grm = new Grid();
        grm.setMensajeInfo("Seleccionar los activos que se van a dar de baja");
        gru.getChildren().add(grm);

        gru.getChildren().add(pat_panel2);

        mep_menu.dibujar(7, "DAR DE BAJA ACTIVOS FIJOS", gru);
    }

    public void dibujarActDadosBaja() {
        //8
        tab_tabla = new Tabla();
        tab_tabla.setId("tab_tabla");
        tab_tabla.setSql(ser_activos.getSqlActivosDadosdeBaja());
        tab_tabla.setCampoPrimaria("baja_ide_acafi");
        tab_tabla.getColumna("baja_ide_acafi").setVisible(false);
        tab_tabla.getColumna("descripcion").setFiltroContenido();
        tab_tabla.getColumna("serie").setFiltroContenido();
        tab_tabla.setRows(20);
        tab_tabla.setLectura(true);
        tab_tabla.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_tabla);
        Grupo gru = new Grupo();

        gru.getChildren().add(pat_panel);
        mep_menu.dibujar(8, "ACTIVOS FIJOS DADOS DE BAJA", gru);
    }

    public void dibujarConsultarAsignados() {
        //3        
        tab_tabla = new Tabla();
        tab_tabla.setId("tab_tabla");
        tab_tabla.setSql(ser_activos.getSqlActivosAsignados("-1"));
        tab_tabla.setCampoPrimaria("baja_ide_acafi");
        tab_tabla.getColumna("baja_ide_acafi").setVisible(false);
        tab_tabla.getColumna("descripcion").setFiltroContenido();
        tab_tabla.getColumna("serie").setFiltroContenido();
        tab_tabla.setRows(20);
        tab_tabla.setLectura(true);
        tab_tabla.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_tabla);
        Grupo gru = new Grupo();

        Grid gr = new Grid();
        gr.setColumns(2);
        gr.getChildren().add(new Etiqueta("<strong>CUSTODIO :</strong>"));
        aut_custodio = new AutoCompletar();
        aut_custodio.setId("aut_custodio");
        aut_custodio.setAutoCompletar("select ide_geper, nom_geper,identificac_geper from gen_persona where es_empleado_geper=true and ide_empr=" + utilitario.getVariable("ide_empr"));
        aut_custodio.setAutocompletarContenido();
        aut_custodio.setMetodoChange("cargarActivosAsignados");
        gr.getChildren().add(aut_custodio);
        gru.getChildren().add(gr);
        gru.getChildren().add(new Separator());
        gru.getChildren().add(pat_panel);
        mep_menu.dibujar(3, "CONSULTAR ACTIVOS ASIGNADOS", gru);

    }

    public void cargarActivosAsignados(SelectEvent evt) {
        aut_custodio.onSelect(evt);
        if (aut_custodio.getValor() != null) {
            tab_tabla.setSql(ser_activos.getSqlActivosAsignados(aut_custodio.getValor()));
            tab_tabla.ejecutarSql();

        }

    }

    public void dibujarAsignarActivos() {
        Grupo gru = new Grupo();

        tab_tabla = new Tabla();
        tab_tabla.setId("tab_tabla");
        tab_tabla.setTabla("act_asignacion_activo", "ide_acaaf", -1);
        tab_tabla.getColumna("ide_acaaf").setVisible(false);
        tab_tabla.getColumna("ide_acafi").setVisible(false);
        tab_tabla.getColumna("ide_usua").setVisible(false);
        tab_tabla.getColumna("ide_geper").setCombo("gen_persona", "ide_geper", "nom_geper,identificac_geper", "es_empleado_geper=true");
        tab_tabla.getColumna("ide_geper").setAutoCompletar();
        tab_tabla.getColumna("ide_geper").setNombreVisual("CUSTODIO");
        tab_tabla.getColumna("ide_geper").setRequerida(true);
        tab_tabla.getColumna("fecha_acaaf").setValorDefecto(utilitario.getFechaActual());
        tab_tabla.getColumna("fecha_acaaf").setRequerida(true);
        tab_tabla.getColumna("fecha_acaaf").setNombreVisual("FECHA DE ASIGNACIÓN");
        tab_tabla.getColumna("observacion_acaaf").setControl("Texto");
        tab_tabla.getColumna("observacion_acaaf").setNombreVisual("OBSERVACIÓN");
        tab_tabla.getColumna("observacion_acaaf").setRequerida(true);
        tab_tabla.setCondicion("ide_acaaf=-1");
        tab_tabla.setTipoFormulario(true);
        tab_tabla.getGrid().setColumns(4);
        tab_tabla.setMostrarNumeroRegistros(false);
        tab_tabla.dibujar();
        tab_tabla.insertar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_tabla);
        pat_panel.getMenuTabla().setRendered(false);
        gru.getChildren().add(pat_panel);
        gru.getChildren().add(new Separator());

        tab_tabla2 = new Tabla();

        tab_tabla2.setId("tab_seleccion");
        tab_tabla2.setSql(ser_activos.getSqlListaActivosFijos());
        tab_tabla2.setCampoPrimaria("ide_acafi");
        tab_tabla2.getColumna("ide_acafi").setVisible(false);
        tab_tabla2.getColumna("serie_acafi").setFiltroContenido();
        tab_tabla2.getColumna("nombre_acafi").setFiltroContenido();
        tab_tabla2.getColumna("descripcion1_acafi").setFiltroContenido();
        tab_tabla2.getColumna("nom_geper").setFiltroContenido();
        tab_tabla2.getColumna("nombre_aceaf").setFiltroContenido();
        tab_tabla2.getColumna("nom_geper").setNombreVisual("CUSTODIO");
        tab_tabla2.getColumna("anos_uso_acafi").alinearDerecha();
        tab_tabla2.getColumna("vida_util_acafi").alinearDerecha();
        tab_tabla2.getColumna("valor_compra_acafi").alinearDerecha();
        tab_tabla2.getColumna("valor_comercial_acafi").alinearDerecha();
        tab_tabla2.getColumna("valor_remate_acafi").alinearDerecha();
        tab_tabla2.setTipoSeleccion(true);
        tab_tabla2.setSeleccionTabla("multiple");
        tab_tabla2.setRows(10);
        tab_tabla2.dibujar();
        PanelTabla pat_panel2 = new PanelTabla();
        pat_panel2.setPanelTabla(tab_tabla2);

        Grid grm = new Grid();
        grm.setMensajeInfo("Seleccionar los activos a ser asignados");
        gru.getChildren().add(grm);
        gru.getChildren().add(pat_panel2);

        mep_menu.dibujar(5, "ASIGNAR ACTIVOS FIJOS", gru);
    }

    public void dibujarListadosActivos() {
        Barra bar_menu = new Barra();
        bar_menu.setId("bar_menu");
        bar_menu.limpiar();

        Boton bot_ver = new Boton();
        bot_ver.setValue("Datos Activo Fijo");
        bot_ver.setMetodo("cargarActivoFijo");
        bar_menu.agregarComponente(bot_ver);

        tab_tabla = new Tabla();
        tab_tabla.setId("tab_tabla");
        tab_tabla.setSql(ser_activos.getSqlListaActivosFijos());
        tab_tabla.setCampoPrimaria("ide_acafi");
        tab_tabla.getColumna("ide_acafi").setVisible(false);
        tab_tabla.getColumna("serie_acafi").setFiltroContenido();
        tab_tabla.getColumna("nombre_acafi").setFiltroContenido();
        tab_tabla.getColumna("descripcion1_acafi").setFiltroContenido();
        tab_tabla.getColumna("nom_geper").setFiltroContenido();
        tab_tabla.getColumna("nom_geper").setNombreVisual("CUSTODIO");
        tab_tabla.getColumna("nombre_inarti").setFiltroContenido();
        tab_tabla.getColumna("nombre_aceaf").setFiltroContenido();
        tab_tabla.getColumna("anos_uso_acafi").alinearDerecha();
        tab_tabla.getColumna("vida_util_acafi").alinearDerecha();
        tab_tabla.getColumna("valor_compra_acafi").alinearDerecha();
        tab_tabla.getColumna("valor_comercial_acafi").alinearDerecha();
        tab_tabla.getColumna("valor_remate_acafi").alinearDerecha();
        tab_tabla.setLectura(true);
        tab_tabla.setRows(20);

        //COLOR ROJO NO TIENE GRUPO 
        tab_tabla.setValueExpression("rowStyleClass", "fila.campos[2] eq null ? 'text-red' : null");
        tab_tabla.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_tabla);

        Grupo gru = new Grupo();
        gru.getChildren().add(bar_menu);
        gru.getChildren().add(pat_panel);
        mep_menu.dibujar(4, "LISTADO DE ACTIVOS FIJOS", gru);
    }

    public void dibujarActivoFijo() {

        tab_tabla = new Tabla();
        tab_tabla.setId("tab_tabla");
        tab_tabla.setHeader("DATOS DEL ACTIVO");
        tab_tabla.setTabla("act_activo_fijo", "ide_acafi", 1);
        tab_tabla.getColumna("ide_inarti").setCombo("inv_articulo", "ide_inarti", "nombre_inarti", " ide_intpr=0"); //SOLO ACTIVOS FIJOS
        tab_tabla.getColumna("gen_ide_geper").setVisible(false);
        tab_tabla.getColumna("ide_rheor").setVisible(false);
        tab_tabla.getColumna("alterno_acafi").setVisible(false);
        tab_tabla.getColumna("fo_acafi").setVisible(false);
        tab_tabla.getColumna("ide_geubi").setCombo("gen_ubicacion", "ide_geubi", "nombre_geubi", "nivel_geubi='HIJO'");
        tab_tabla.getColumna("ide_usua").setVisible(false);
        tab_tabla.getColumna("foto_acafi").setUpload();
        tab_tabla.getColumna("foto_acafi").setImagen("120", "120");
        tab_tabla.getColumna("ide_aceaf").setCombo("act_estado_activo_fijo", "ide_aceaf", "nombre_aceaf", "");
        tab_tabla.getColumna("ide_inmar").setCombo("inv_marca", "ide_inmar", "nombre_invmar", "");
        // tab_tabla.getColumna("ide_geper").setCombo("gen_persona", "ide_geper", "nom_geper,identificac_geper", "es_empleado_geper=true");
        //tab_tabla.getColumna("ide_geper").setLectura(true);
        //tab_tabla.getColumna("ide_geper").setAutoCompletar();
        tab_tabla.getColumna("ide_geper").setVisible(false);
        tab_tabla.getColumna("ide_acuba").setCombo("act_ubicacion_activo", "ide_acuba", "nombre_acuba", "");
        tab_tabla.getColumna("ide_acuba").setAutoCompletar();
        tab_tabla.getColumna("serie_acafi").setRequerida(true);
        tab_tabla.getColumna("nombre_acafi").setRequerida(true);
        tab_tabla.getColumna("ide_inarti").setRequerida(true);
        tab_tabla.getColumna("vida_util_acafi").setLectura(true);
        tab_tabla.getColumna("valor_compra_acafi").setLectura(true);
        tab_tabla.getColumna("recidual_acafi").setVisible(true);
        tab_tabla.getColumna("credi_tribu_acafi").setVisible(false);
        tab_tabla.getColumna("fecha_compra_acafi").setLectura(true);
        tab_tabla.getColumna("fecha_acafi").setLectura(true);
        tab_tabla.getColumna("fecha_acafi").setValorDefecto(utilitario.getFechaActual());
        tab_tabla.getGrid().setColumns(4);
        tab_tabla.setTipoFormulario(true);
        tab_tabla.setCondicion("ide_acafi=-1");
        tab_tabla.setMostrarNumeroRegistros(false);
        tab_tabla.dibujar();
        tab_tabla.setValidarInsertar(false);
        PanelTabla pat_panel1 = new PanelTabla();
        pat_panel1.setPanelTabla(tab_tabla);

        tab_tabla2 = new Tabla();
        tab_tabla2.setId("tab_tabla2");
        tab_tabla2.setHeader("HISTORIAL ASIGNACIONES");
        tab_tabla2.setSql(ser_activos.getSqlHistoriaAsignacionActivo("-1"));
        tab_tabla2.setCampoPrimaria("ide_acaaf");
        tab_tabla2.getColumna("ide_acaaf").setVisible(false);
        tab_tabla2.setLectura(true);
        tab_tabla2.setRows(10);
        tab_tabla2.dibujar();
        tab_tabla2.setRendered(false);
        PanelTabla pat_panel2 = new PanelTabla();
        pat_panel2.setPanelTabla(tab_tabla2);

        tab_tabla3 = new Tabla();
        tab_tabla3.setId("tab_tabla3");
        tab_tabla3.setHeader("TRANSACCIONES");
        tab_tabla3.setSql(ser_activos.getSqlTransaccionesActivo("-1"));
        tab_tabla3.setCampoPrimaria("ide_actra");
        tab_tabla3.getColumna("ide_actra").setVisible(false);
        tab_tabla3.setLectura(true);
        tab_tabla3.setRows(10);
        tab_tabla3.setRendered(false);
        tab_tabla3.dibujar();

        PanelTabla pat_panel5 = new PanelTabla();
        pat_panel5.setPanelTabla(tab_tabla3);

        Grupo gr = new Grupo();
        gr.getChildren().add(pat_panel1);
        gr.getChildren().add(new Separator());
        gr.getChildren().add(pat_panel2);
        gr.getChildren().add(new Separator());
        gr.getChildren().add(pat_panel5);

        mep_menu.dibujar(2, "FICHA ACTIVO FIJO", gr);
    }

    public void reasignarActivo() {

    }

    public void cargarActivoFijo() {
        String ide_acafi = tab_tabla.getValor("ide_acafi");
        if (ide_acafi != null) {
            dibujarActivoFijo();
            tab_tabla.setCondicion("ide_acafi=" + ide_acafi);
            tab_tabla.ejecutarSql();
            tab_tabla2.setSql(ser_activos.getSqlHistoriaAsignacionActivo(tab_tabla.getValor("ide_acafi")));
            tab_tabla2.ejecutarSql();
            tab_tabla3.setSql(ser_activos.getSqlTransaccionesActivo(tab_tabla.getValor("ide_acafi")));
            tab_tabla3.ejecutarSql();
            tab_tabla2.setRendered(true);
            tab_tabla3.setRendered(true);
        } else {
            utilitario.agregarMensaje("Debe seleccionar un Activo", "");
        }
    }

    @Override
    public void insertar() {
        dibujarActivoFijo();
        tab_tabla.insertar();
    }

    @Override
    public void guardar() {
        if (mep_menu.getOpcion() == 2) {
            if (tab_tabla.guardar()) {
                guardarPantalla();
            }
        } else if (mep_menu.getOpcion() == 5) {
            String seleccionadas = tab_tabla2.getFilasSeleccionadas();
            if (seleccionadas != null) {
                String ide_geper = tab_tabla.getValor("ide_geper");
                String fecha_acaaf = tab_tabla.getValor("fecha_acaaf");
                String observacion_acaaf = tab_tabla.getValor("observacion_acaaf");
                if (ide_geper == null) {
                    utilitario.agregarMensajeError("Debe seleccionar un Custodio", "");
                    return;
                }
                if (fecha_acaaf == null) {
                    utilitario.agregarMensajeError("Debe seleccionar una Fecha de asignación", "");
                    return;
                }
                if (observacion_acaaf == null) {
                    utilitario.agregarMensajeError("Debe ingresar una Observación", "");
                    return;
                }
                String[] act = seleccionadas.split(",");
                tab_tabla.limpiar();
                tab_tabla.setCampoForanea("");
                for (int i = 0; i < act.length; i++) {
                    String ide_acafi = act[i];
                    tab_tabla.setValidarInsertar(false);
                    tab_tabla.insertar();
                    tab_tabla.setValor("ide_acafi", ide_acafi);
                    tab_tabla.setValor("ide_usua", utilitario.getVariable("ide_usua"));
                    tab_tabla.setValor("ide_geper", ide_geper);
                    tab_tabla.setValor("observacion_acaaf", observacion_acaaf);
                }
                utilitario.getConexion().agregarSqlPantalla("update act_activo_fijo set ide_geper=" + ide_geper + " where ide_acafi in (" + seleccionadas + ")");
                if (tab_tabla.guardar()) {
                    if (guardarPantalla().isEmpty()) {
                        tab_tabla.limpiar();
                        tab_tabla.insertar();
                        tab_tabla2.actualizar();
                    }
                }
            } else {
                utilitario.agregarMensajeError("Seleccione activos para asignar", "");
            }
        }
    }

    @Override
    public void eliminar() {

    }

    public Tabla getTab_tabla() {
        return tab_tabla;
    }

    public void setTab_tabla(Tabla tab_tabla) {
        this.tab_tabla = tab_tabla;
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

    public Tabla getTab_seleccion() {
        return tab_tabla2;
    }

    public void setTab_seleccion(Tabla tab_tabla) {
        this.tab_tabla2 = tab_tabla;
    }

    public AutoCompletar getAut_custodio() {
        return aut_custodio;
    }

    public void setAut_custodio(AutoCompletar aut_custodio) {
        this.aut_custodio = aut_custodio;
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

    public SeleccionArbol getSel_arb() {
        return sel_arb;
    }

    public void setSel_arb(SeleccionArbol sel_arb) {
        this.sel_arb = sel_arb;
    }

    public SeleccionCalendario getSec_rango_reporte() {
        return sec_rango_reporte;
    }

    public void setSec_rango_reporte(SeleccionCalendario sec_rango_reporte) {
        this.sec_rango_reporte = sec_rango_reporte;
    }

}
