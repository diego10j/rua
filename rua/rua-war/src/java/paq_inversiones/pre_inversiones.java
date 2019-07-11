/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_inversiones;

import componentes.AsientoContable;
import framework.componentes.AutoCompletar;
import framework.componentes.Barra;
import framework.componentes.Boton;
import framework.componentes.Combo;
import framework.componentes.Confirmar;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.Grupo;
import framework.componentes.ItemMenu;
import framework.componentes.Link;
import framework.componentes.MenuPanel;
import framework.componentes.PanelTabla;
import framework.componentes.Radio;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.Tabla;
import java.util.HashMap;
import java.util.Map;
import javax.ejb.EJB;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
import org.primefaces.component.separator.Separator;
import org.primefaces.event.DateSelectEvent;
import org.primefaces.event.SelectEvent;
import servicios.inversiones.ServicioInversiones;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author DIEGOFERNANDOJACOMEG
 */
public class pre_inversiones extends Pantalla {

    private final MenuPanel mep_menu = new MenuPanel();
    private Radio rad_hace_asiento;
    private Tabla tab_tabla1;
    private Tabla tab_tabla2;
    @EJB
    private final ServicioInversiones ser_inversion = (ServicioInversiones) utilitario.instanciarEJB(ServicioInversiones.class);
    private AsientoContable asc_asiento = new AsientoContable();
    private AutoCompletar aut_inversion;
    private Grid gri = new Grid();
    private String ide_ipcai = "-1";
    private String iyp_ide_ipcer = null;
    private String ide_teban = "-1";
    private Reporte rep_reporte = new Reporte();
    private SeleccionFormatoReporte sel_formato = new SeleccionFormatoReporte();
    private double dou_capital_renova;
    private double dou_interes_renova;
    private Combo com_bancos;

    private Confirmar con_confirma_anular = new Confirmar();

    public pre_inversiones() {
        bar_botones.quitarBotonsNavegacion();
        bar_botones.agregarReporte();

        bar_botones.quitarBotonInsertar();
        bar_botones.quitarBotonEliminar();
        mep_menu.setMenuPanel("INVERSIONES BANCARIAS", "20%");
        mep_menu.agregarItem("Listado de Inversiones Bancarias", "dibujarListadoB", "ui-icon-note");//2
        mep_menu.agregarItem("Nuevo Certificado Bancario", "dibujarCertificadoB", "ui-icon-contact"); //1
        mep_menu.agregarItem("Pago de Interes", "dibujarPagoB", "ui-icon-contact"); //4
        mep_menu.agregarItem("Renovaciones", "dibujarRenovacionesBanco", "ui-icon-calculator"); //21
        mep_menu.agregarItem("Generar Asiento de Terminación", "dibujarAsientoTerminaB", "ui-icon-notice");//12
        mep_menu.agregarSubMenu("INVERSIONES SALESIANAS");
        mep_menu.agregarItem("Listado de Inversiones Casas - Obras", "dibujarListadoCasas", "ui-icon-note");//5
        mep_menu.agregarItem("Nuevo Certificado Casas", "dibujarCertificadoCasa", "ui-icon-contact"); //6
        mep_menu.agregarItem("Renovaciones", "dibujarRenovaciones", "ui-icon-calculator"); //7
        mep_menu.agregarItem("Pago de Interes", "dibujarPagoC", "ui-icon-contact"); //4
        mep_menu.agregarItem("Generar Asiento de Terminación", "dibujarAsientoTerminaCasas", "ui-icon-notice");//17
        mep_menu.agregarSubMenu("INVERSIONES FONDO DE DESVINCULACIÓN");
        mep_menu.agregarItem("Listado de Inversiones de Desvinculación", "dibujarListadoFondo", "ui-icon-note");//8
        mep_menu.agregarItem("Nuevo Certificado de Desvinculación", "dibujarCertificadoFondo", "ui-icon-contact"); //9               
        mep_menu.agregarItem("Generar Asiento de Terminación", "dibujarAsientoTerminaFondo", "ui-icon-notice");//19
        mep_menu.agregarSubMenu("INVERSIONES VENCIDAS");
        mep_menu.agregarItem("Inversiones Vencidas", "dibujarVencidas", "ui-icon-calculator");//13
        mep_menu.agregarSubMenu("HISTORICO");
        mep_menu.agregarItem("Inversiones Canceladas", "dibujarCanceladas", "ui-icon-star");//20
        agregarComponente(mep_menu);

        asc_asiento.setId("asc_asiento");
        asc_asiento.getBot_cancelar().setMetodo("cerrarAsiento");
        agregarComponente(asc_asiento);

        rep_reporte.setId("rep_reporte");
        rep_reporte.getBot_aceptar().setMetodo("aceptarReporte");
        sel_formato.setId("sel_formato");
        agregarComponente(rep_reporte);
        agregarComponente(sel_formato);

        con_confirma_anular.setId("con_confirma_anular");
        con_confirma_anular.setMessage("Está seguro de Anular el Certificado Seleccionado ?");
        con_confirma_anular.setTitle("ANULAR CERTIFICADO");
        con_confirma_anular.getBot_aceptar().setValue("Si");
        con_confirma_anular.getBot_cancelar().setValue("No");
        agregarComponente(con_confirma_anular);
    }

    @Override
    public void abrirListaReportes() {
        rep_reporte.dibujar();
    }

    @Override
    public void aceptarReporte() {

        if (rep_reporte.getReporteSelecionado().equals("Certificado de Inversión") || rep_reporte.getReporteSelecionado().equals("Certificado de Inversión Fondo")) {
            if (rep_reporte.isVisible()) {
                if (tab_tabla1.getValor("ide_ipcer") != null && !tab_tabla1.getValor("ide_ipcer").isEmpty()) {
                    Map parametro = new HashMap();
                    rep_reporte.cerrar();
                    if (tab_tabla1.getValor("ide_cnccc") != null && !tab_tabla1.getValor("ide_cnccc").isEmpty()) {
                        parametro.put("ide_cnccc", tab_tabla1.getValor("ide_cnccc") + "");
                    } else {
                        parametro.put("ide_cnccc", "-1");
                    }
                    parametro.put("ide_ipcer", Long.parseLong(tab_tabla1.getValor("ide_ipcer")));
                    sel_formato.setSeleccionFormatoReporte(parametro, rep_reporte.getPath());
                    sel_formato.dibujar();

                } else {
                    utilitario.agregarMensajeInfo("No se puede generar el reporte", "La fila seleccionada no tiene Certificados de Inversion");
                }
            }
        } else if (rep_reporte.getReporteSelecionado().equals("Resumen Inversiones Casas - Obras")) {
            Map parametro = new HashMap();
            rep_reporte.cerrar();
            sel_formato.setSeleccionFormatoReporte(parametro, rep_reporte.getPath());
            sel_formato.dibujar();
        } else if (rep_reporte.getReporteSelecionado().equals("Resumen Inversiones Bancarias")) {
            Map parametro = new HashMap();
            rep_reporte.cerrar();
            sel_formato.setSeleccionFormatoReporte(parametro, rep_reporte.getPath());
            sel_formato.dibujar();
        } else if (rep_reporte.getReporteSelecionado().equals("Inversiones Bancarias no Canceladas")) {
            Map parametro = new HashMap();
            rep_reporte.cerrar();
            sel_formato.setSeleccionFormatoReporte(parametro, rep_reporte.getPath());
            sel_formato.dibujar();
        }

    }

    public void cerrarAsiento() {
        //limpia sql guardados
        utilitario.getConexion().getSqlPantalla().clear();
        asc_asiento.cerrar();
    }

    public void abrirGeneraAsiento() {
        if (tab_tabla1.getValorSeleccionado() != null) {
            asc_asiento.nuevoAsiento();
            asc_asiento.dibujar();
            asc_asiento.getTab_cabe_asiento().setValor("ide_geper", tab_tabla1.getValor("ide_geper"));
            asc_asiento.getBot_aceptar().setMetodo("aceptarTerminarAsiento");
        } else {
            utilitario.agregarMensajeInfo("Debe seleccionar un certificado", "");
        }
    }

    public void aceptarTerminarAsiento() {
        if (asc_asiento.isVisible()) {
            asc_asiento.guardar();
            if (asc_asiento.isVisible() == false) {
                utilitario.getConexion().agregarSqlPantalla("UPDATE iyp_certificado SET ide_cnccc_terminacion=" + asc_asiento.getIde_cnccc() + ", ide_ipein=1 where ide_ipcer=" + tab_tabla1.getValor("ide_ipcer"));
                guardarPantalla();
                if (mep_menu.getOpcion() == 12) {
                    dibujarListadoB();
                }
                if (mep_menu.getOpcion() == 17) {
                    dibujarListadoCasas();
                }
                if (mep_menu.getOpcion() == 19) {
                    dibujarListadoFondo();
                }
            }
        }
    }

    public void dibujarVencidas() {
        tab_tabla1 = new Tabla();
        tab_tabla1.setId("tab_tabla1");
        tab_tabla1.setSql(ser_inversion.getSqlListaCertificadosVencidosNuevas());
        tab_tabla1.setNumeroTabla(13);
        tab_tabla1.setLectura(true);
        tab_tabla1.setCampoPrimaria("ide_ipcer");
        tab_tabla1.getColumna("ide_ipcer").setVisible(false);
        tab_tabla1.getColumna("ide_iptin").setVisible(false);
        tab_tabla1.getColumna("nombre_iptin").setNombreVisual("TIPO");
        tab_tabla1.getColumna("nombre_iptin").setFiltroContenido();
        tab_tabla1.getColumna("nombre_ipein").setNombreVisual("ESTADO");
        tab_tabla1.getColumna("nombre_ipein").setFiltroContenido();
        tab_tabla1.getColumna("num_certificado_ipcer").setNombreVisual("NUM. CERTIFICADO");
        tab_tabla1.getColumna("num_certificado_ipcer").setFiltroContenido();
        tab_tabla1.getColumna("num_certificado_ipcer").alinearCentro();
        tab_tabla1.getColumna("nombre_ipcin").setNombreVisual("CLASE");
        tab_tabla1.getColumna("nombre_ipcin").setFiltroContenido();
        tab_tabla1.getColumna("nombre_ipcin").setLongitud(-1);
        tab_tabla1.getColumna("observacion_ipcer").setNombreVisual("OBSERVACIÓN");
        tab_tabla1.getColumna("fecha_emision_ipcer").setNombreVisual("FECHA EMISIÓN");
        tab_tabla1.getColumna("plazo_ipcer").setNombreVisual("PLAZO");
        tab_tabla1.getColumna("capital_ipcer").setNombreVisual("CAPITAL");
        tab_tabla1.getColumna("interes_ipcer").setNombreVisual("INTERES");
        tab_tabla1.getColumna("valor_a_pagar_ipcer").setNombreVisual("VALOR A PAGAR");
        tab_tabla1.getColumna("plazo_ipcer").alinearDerecha();
        tab_tabla1.getColumna("capital_ipcer").alinearDerecha();
        tab_tabla1.getColumna("interes_ipcer").alinearDerecha();
        tab_tabla1.getColumna("valor_a_pagar_ipcer").alinearDerecha();
        tab_tabla1.getColumna("fecha_vence_ipcer").setNombreVisual("FECHA VENCE");
        tab_tabla1.getColumna("ide_cnccc").setNombreVisual("N. ASIENTO");
        tab_tabla1.getColumna("IDE_CNCCC").setLink();
        tab_tabla1.getColumna("IDE_CNCCC").setMetodoChange("abrirAsiento");
        tab_tabla1.getColumna("IDE_CNCCC").alinearCentro();

        tab_tabla1.setRows(25);
        tab_tabla1.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_tabla1);
        pat_panel.getMenuTabla().getItem_buscar().setRendered(false);
        mep_menu.dibujar(13, "INVERSIONES VENCIDAS AL " + utilitario.getFechaLarga(utilitario.getFechaActual()).toUpperCase(), pat_panel);
    }

    public void dibujarCanceladas() {
        tab_tabla1 = new Tabla();
        tab_tabla1.setId("tab_tabla1");
        tab_tabla1.setSql(ser_inversion.getSqlListaCertificadosCanceladosNuevas());
        tab_tabla1.setNumeroTabla(20);
        tab_tabla1.setLectura(true);
        tab_tabla1.setCampoPrimaria("ide_ipcer");
        tab_tabla1.getColumna("ide_ipcer").setVisible(false);
        tab_tabla1.getColumna("ide_iptin").setVisible(false);
        tab_tabla1.getColumna("nombre_iptin").setNombreVisual("TIPO");
        tab_tabla1.getColumna("nombre_iptin").setFiltroContenido();
        tab_tabla1.getColumna("nombre_ipein").setFiltroContenido();
        tab_tabla1.getColumna("num_certificado_ipcer").setNombreVisual("NUM. CERTIFICADO");
        tab_tabla1.getColumna("num_certificado_ipcer").setFiltroContenido();
        tab_tabla1.getColumna("num_certificado_ipcer").alinearCentro();
        tab_tabla1.getColumna("nombre_ipcin").setNombreVisual("CLASE");
        tab_tabla1.getColumna("nombre_ipcin").setFiltroContenido();
        tab_tabla1.getColumna("nombre_ipcin").setLongitud(-1);
        tab_tabla1.getColumna("observacion_ipcer").setNombreVisual("OBSERVACIÓN");
        tab_tabla1.getColumna("fecha_emision_ipcer").setNombreVisual("FECHA EMISIÓN");
        tab_tabla1.getColumna("plazo_ipcer").setNombreVisual("PLAZO");
        tab_tabla1.getColumna("capital_ipcer").setNombreVisual("CAPITAL");
        tab_tabla1.getColumna("interes_ipcer").setNombreVisual("INTERES");
        tab_tabla1.getColumna("valor_a_pagar_ipcer").setNombreVisual("VALOR A PAGAR");
        tab_tabla1.getColumna("plazo_ipcer").alinearDerecha();
        tab_tabla1.getColumna("capital_ipcer").alinearDerecha();
        tab_tabla1.getColumna("interes_ipcer").alinearDerecha();
        tab_tabla1.getColumna("valor_a_pagar_ipcer").alinearDerecha();
        tab_tabla1.getColumna("fecha_vence_ipcer").setNombreVisual("FECHA VENCE");
        tab_tabla1.getColumna("ide_cnccc").setNombreVisual("N. ASIENTO");
        tab_tabla1.getColumna("IDE_CNCCC").setLink();
        tab_tabla1.getColumna("IDE_CNCCC").setMetodoChange("abrirAsiento");
        tab_tabla1.getColumna("IDE_CNCCC").alinearCentro();

        tab_tabla1.setRows(25);
        tab_tabla1.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_tabla1);
        pat_panel.getMenuTabla().getItem_buscar().setRendered(false);
        mep_menu.dibujar(20, "INVERSIONES CANCELADAS", pat_panel);
    }

    public void dibujarAsientoTerminaB() {
        Barra bar_menu = new Barra();
        bar_menu.setId("bar_menu");
        bar_menu.limpiar();
        Boton bot_asi = new Boton();
        bot_asi.setValue("Generar Asiento Contable de Termincación");
        bot_asi.setMetodo("abrirGeneraAsiento");
        bar_menu.agregarComponente(bot_asi);

        tab_tabla1 = new Tabla();
        tab_tabla1.setId("tab_tabla1");
        tab_tabla1.setSql(ser_inversion.getSqlListaInversionesBancariasSinTerminacion());
        tab_tabla1.setLectura(true);
        tab_tabla1.setNumeroTabla(12);
        tab_tabla1.setCampoPrimaria("ide_ipcer");
        tab_tabla1.getColumna("ide_ipcer").setVisible(false);
        tab_tabla1.getColumna("ide_geper").setVisible(false);
        tab_tabla1.getColumna("CAPITAL").alinearDerecha();
        tab_tabla1.getColumna("INTERES").alinearDerecha();
        tab_tabla1.getColumna("CAPITAL_MAS_INTERES").alinearDerecha();
        tab_tabla1.getColumna("CAPITAL_MAS_INTERES").setLongitud(35);
        tab_tabla1.getColumna("FECHA_VENCIMIENTO").setLongitud(35);
        tab_tabla1.getColumna("FECHA_VENCIMIENTO").setEstilo("font-size:13px;font-weight: bold;");
        tab_tabla1.getColumna("FECHA_VENCIMIENTO").alinearCentro();
        tab_tabla1.getColumna("CAPITAL_MAS_INTERES").setEstilo("font-size:13px;font-weight: bold;");
        tab_tabla1.getColumna("ide_cnccc").setNombreVisual("N. ASIENTO");
        tab_tabla1.getColumna("IDE_CNCCC").setLink();
        tab_tabla1.getColumna("IDE_CNCCC").setMetodoChange("abrirAsiento");
        tab_tabla1.getColumna("IDE_CNCCC").alinearCentro();
        tab_tabla1.getColumna("BANCO").setFiltroContenido();
        tab_tabla1.getColumna("NUM_CERTIFICADO").setFiltroContenido();
        tab_tabla1.getColumna("ESTADO").setFiltroContenido();
        tab_tabla1.setRows(15);
        tab_tabla1.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_tabla1);
        pat_panel.getMenuTabla().getItem_buscar().setRendered(false);

        Grupo gru = new Grupo();
        gru.getChildren().add(bar_menu);
        gru.getChildren().add(pat_panel);
        mep_menu.dibujar(12, "GENERAR ASIENTO CONTABLE DE TERMINACIÓN INVERSIONES BANCARIAS", gru);
        // metodo of mauriicio
        utilitario.buscarPermisosObjetos();

    }

    public void dibujarAsientoTerminaCasas() {
        Barra bar_menu = new Barra();
        bar_menu.setId("bar_menu");
        bar_menu.limpiar();
        Boton bot_asi = new Boton();
        bot_asi.setValue("Generar Asiento Contable de Termincación");
        bot_asi.setMetodo("abrirGeneraAsiento");
        bar_menu.agregarComponente(bot_asi);

        tab_tabla1 = new Tabla();
        tab_tabla1.setId("tab_tabla1");
        tab_tabla1.setSql(ser_inversion.getSqlListaInversionesCasasSinTerminacion());
        tab_tabla1.setLectura(true);
        tab_tabla1.setCampoPrimaria("ide_ipcer");
        tab_tabla1.setNumeroTabla(17);
        tab_tabla1.getColumna("ide_ipcer").setVisible(false);
        tab_tabla1.getColumna("ide_ipcai").setVisible(false);
        tab_tabla1.getColumna("ide_geper").setVisible(false);
        tab_tabla1.setColumnaSuma("CAPITAL,INTERES,CAPITAL_MAS_INTERES");
        tab_tabla1.getColumna("CAPITAL").alinearDerecha();
        tab_tabla1.getColumna("INTERES").alinearDerecha();
        tab_tabla1.getColumna("CAPITAL_MAS_INTERES").alinearDerecha();
        tab_tabla1.getColumna("CAPITAL_MAS_INTERES").setLongitud(35);
        tab_tabla1.getColumna("FECHA_VENCIMIENTO").setLongitud(35);
        tab_tabla1.getColumna("FECHA_VENCIMIENTO").setEstilo("font-size:13px;font-weight: bold;");
        tab_tabla1.getColumna("FECHA_VENCIMIENTO").alinearCentro();
        tab_tabla1.getColumna("CAPITAL_MAS_INTERES").setEstilo("font-size:13px;font-weight: bold;");
        tab_tabla1.getColumna("ide_cnccc").setNombreVisual("N. ASIENTO");
        tab_tabla1.getColumna("IDE_CNCCC").setLink();
        tab_tabla1.getColumna("IDE_CNCCC").setMetodoChange("abrirAsiento");
        tab_tabla1.getColumna("IDE_CNCCC").alinearCentro();
        tab_tabla1.getColumna("CASAS_OBRAS").setFiltroContenido();
        tab_tabla1.getColumna("GRUPO").setFiltroContenido();
        tab_tabla1.getColumna("NUM_CERTIFICADO").setFiltroContenido();
        tab_tabla1.getColumna("ESTADO").setFiltroContenido();
        tab_tabla1.setRows(15);
        tab_tabla1.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_tabla1);
        pat_panel.getMenuTabla().getItem_buscar().setRendered(false);

        Grupo gru = new Grupo();
        gru.getChildren().add(bar_menu);
        gru.getChildren().add(pat_panel);
        mep_menu.dibujar(17, "GENERAR ASIENTO CONTABLE DE TERMINACIÓN INVERSIONES CASAS - OBRAS", gru);
        // metodo of mauriicio
        utilitario.buscarPermisosObjetos();

    }

    public void dibujarAsientoTerminaFondo() {
        Barra bar_menu = new Barra();
        bar_menu.setId("bar_menu");
        bar_menu.limpiar();
        Boton bot_asi = new Boton();
        bot_asi.setValue("Generar Asiento Contable de Termincación");
        bot_asi.setMetodo("abrirGeneraAsiento");
        bar_menu.agregarComponente(bot_asi);

        tab_tabla1 = new Tabla();
        tab_tabla1.setId("tab_tabla1");
        tab_tabla1.setSql(ser_inversion.getSqlListaInversionesFondoSinTerminacion());
        tab_tabla1.setLectura(true);
        tab_tabla1.setCampoPrimaria("ide_ipcer");
        tab_tabla1.setNumeroTabla(19);
        tab_tabla1.setCampoPrimaria("ide_ipcer");
        tab_tabla1.getColumna("ide_ipcer").setVisible(false);
        tab_tabla1.getColumna("ide_geper").setVisible(false);
        tab_tabla1.setColumnaSuma("CAPITAL,INTERES,CAPITAL_MAS_INTERES");
        tab_tabla1.getColumna("CAPITAL").alinearDerecha();
        tab_tabla1.getColumna("INTERES").alinearDerecha();
        tab_tabla1.getColumna("CAPITAL_MAS_INTERES").alinearDerecha();
        tab_tabla1.getColumna("CAPITAL_MAS_INTERES").setLongitud(35);
        tab_tabla1.getColumna("FECHA_VENCIMIENTO").setLongitud(35);
        tab_tabla1.getColumna("FECHA_VENCIMIENTO").setEstilo("font-size:13px;font-weight: bold;");
        tab_tabla1.getColumna("FECHA_VENCIMIENTO").alinearCentro();
        tab_tabla1.getColumna("CAPITAL_MAS_INTERES").setEstilo("font-size:13px;font-weight: bold;");
        tab_tabla1.getColumna("ide_cnccc").setNombreVisual("N. ASIENTO");
        tab_tabla1.getColumna("IDE_CNCCC").setLink();
        tab_tabla1.getColumna("IDE_CNCCC").setMetodoChange("abrirAsiento");
        tab_tabla1.getColumna("IDE_CNCCC").alinearCentro();
        tab_tabla1.setRows(15);
        tab_tabla1.getColumna("NUM_CERTIFICADO").setFiltroContenido();
        tab_tabla1.getColumna("ESTADO").setFiltroContenido();
        tab_tabla1.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_tabla1);
        pat_panel.getMenuTabla().getItem_buscar().setRendered(false);

        Grupo gru = new Grupo();
        gru.getChildren().add(bar_menu);
        gru.getChildren().add(pat_panel);
        mep_menu.dibujar(19, "GENERAR ASIENTO CONTABLE DE TERMINACIÓN INVERSIONES FONDO DE DESVINCULACIÓN", gru);
        // metodo of mauriicio
        utilitario.buscarPermisosObjetos();

    }

    public void dibujarRenovaciones() {
        Grupo grupo = new Grupo();
        ide_ipcai = "-1";
        iyp_ide_ipcer = null;
        aut_inversion = new AutoCompletar();
        aut_inversion.setId("aut_inversion");
        aut_inversion.setAutoCompletar(ser_inversion.getSqlComboGrupoBeneficiarios());
        aut_inversion.setMaxResults(20);
        aut_inversion.setAutocompletarContenido();
        aut_inversion.setMetodoChange("cargarInversionesGrupo");
        Grid gr = new Grid();
        gr.setColumns(3);
        gr.getChildren().add(new Etiqueta("<strong>GRUPO INVERSION CASAS-OBRAS : </strong>"));
        gr.getChildren().add(aut_inversion);
        Boton bot_clean = new Boton();
        bot_clean.setIcon("ui-icon-cancel");
        bot_clean.setTitle("Limpiar");
        bot_clean.setMetodo("limpiarRenovacion");
        gr.getChildren().add(bot_clean);
        grupo.getChildren().add(gr);

        Barra bar_menu = new Barra();
        bar_menu.setId("bar_menu");
        bar_menu.limpiar();
        Boton bot_asi = new Boton();
        bot_asi.setValue("Generar Renovación");
        bot_asi.setMetodo("abrirRenovacion");
        bar_menu.agregarComponente(bot_asi);
        grupo.getChildren().add(bar_menu);

        tab_tabla1 = new Tabla();
        tab_tabla1.setId("tab_tabla1");
        tab_tabla1.setSql(ser_inversion.getSqlListaInversionesCasasGrupo("-1"));
        tab_tabla1.setNumeroTabla(7);
        tab_tabla1.setLectura(true);
        tab_tabla1.setCampoPrimaria("ide_ipcer");
        tab_tabla1.getColumna("ide_ipcer").setVisible(false);
        tab_tabla1.getColumna("ide_ipcai").setVisible(false);
        tab_tabla1.getColumna("CAPITAL").alinearDerecha();
        tab_tabla1.getColumna("INTERES").alinearDerecha();
        tab_tabla1.getColumna("CAPITAL_MAS_INTERES").alinearDerecha();
        tab_tabla1.getColumna("CAPITAL_MAS_INTERES").setLongitud(35);
        tab_tabla1.getColumna("FECHA_VENCIMIENTO").setLongitud(35);
        tab_tabla1.getColumna("FECHA_VENCIMIENTO").setEstilo("font-size:13px;font-weight: bold;");
        tab_tabla1.getColumna("FECHA_VENCIMIENTO").alinearCentro();
        tab_tabla1.getColumna("CAPITAL_MAS_INTERES").setEstilo("font-size:13px;font-weight: bold;");
        tab_tabla1.getColumna("ide_cnccc").setNombreVisual("N. ASIENTO");
        tab_tabla1.getColumna("IDE_CNCCC").setLink();
        tab_tabla1.getColumna("IDE_CNCCC").setMetodoChange("abrirAsiento");
        tab_tabla1.getColumna("IDE_CNCCC").alinearCentro();
        tab_tabla1.getColumna("CASAS_OBRAS").setFiltroContenido();
        tab_tabla1.getColumna("NUM_CERTIFICADO").setFiltroContenido();
        tab_tabla1.getColumna("ESTADO").setFiltroContenido();
        tab_tabla1.setScrollable(true);
        tab_tabla1.setScrollHeight(170);
        tab_tabla1.onSelect("seleccionarCertifcadoRenova");
        tab_tabla1.dibujar();

        PanelTabla pat_panel = new PanelTabla();
        ItemMenu itemedita = new ItemMenu();
        itemedita.setValue("Generar Renovación");
        itemedita.setIcon("ui-icon-wrench");
        itemedita.setMetodo("abrirRenovacion");
        pat_panel.getMenuTabla().getChildren().add(itemedita);

        pat_panel.setPanelTabla(tab_tabla1);
        grupo.getChildren().add(pat_panel);

        tab_tabla2 = new Tabla();
        tab_tabla2.setId("tab_tabla2");
        tab_tabla2.setSql(ser_inversion.getSqlListaRenovaciones("-1"));
        tab_tabla2.setNumeroTabla(7);
        tab_tabla2.setLectura(true);
        tab_tabla2.setCampoPrimaria("ide_ipcer");
        tab_tabla2.getColumna("ide_ipcer").setVisible(false);
        tab_tabla2.getColumna("CAPITAL").alinearDerecha();
        tab_tabla2.getColumna("INTERES").alinearDerecha();
        tab_tabla2.getColumna("CAPITAL_MAS_INTERES").alinearDerecha();
        tab_tabla2.getColumna("CAPITAL_MAS_INTERES").setLongitud(35);
        tab_tabla2.getColumna("FECHA_VENCIMIENTO").setLongitud(35);
        tab_tabla2.getColumna("FECHA_VENCIMIENTO").setEstilo("font-size:13px;font-weight: bold;");
        tab_tabla2.getColumna("FECHA_VENCIMIENTO").alinearCentro();
        tab_tabla2.getColumna("CAPITAL_MAS_INTERES").setEstilo("font-size:13px;font-weight: bold;");
        tab_tabla2.getColumna("ide_cnccc").setNombreVisual("N. ASIENTO");
        tab_tabla2.getColumna("IDE_CNCCC").setLink();
        tab_tabla2.getColumna("IDE_CNCCC").setMetodoChange("abrirAsiento");
        tab_tabla2.getColumna("IDE_CNCCC").alinearCentro();
        tab_tabla2.setScrollable(true);
        tab_tabla2.setScrollHeight(110);
        tab_tabla2.setHeader("RENOVACIONES REALIZADAS");

        tab_tabla2.dibujar();
        PanelTabla pat_panel1 = new PanelTabla();
        pat_panel1.setPanelTabla(tab_tabla2);
        grupo.getChildren().add(pat_panel1);

        mep_menu.dibujar(7, "RENOVACIONES DE INVERSIONES CASAS - OBRAS", grupo);
        // metodo of mauriicio
        utilitario.buscarPermisosObjetos();
    }

    public void dibujarRenovacionesBanco() {
        Grupo grupo = new Grupo();
        ide_teban = "-1";
        iyp_ide_ipcer = null;
        com_bancos = new Combo();
        com_bancos.setId("com_bancos");
        com_bancos.setCombo(ser_inversion.getSqlComboBancos());
        com_bancos.setMetodo("cargarInversionesPorBanco");

        Grid gr = new Grid();
        gr.setColumns(3);
        gr.getChildren().add(new Etiqueta("<strong>BANCO : </strong>"));
        gr.getChildren().add(com_bancos);
        grupo.getChildren().add(gr);

        Barra bar_menu = new Barra();
        bar_menu.setId("bar_menu");
        bar_menu.limpiar();
        Boton bot_asi = new Boton();
        bot_asi.setValue("Generar Renovación");
        bot_asi.setMetodo("abrirRenovacionBanco");
        bar_menu.agregarComponente(bot_asi);
        grupo.getChildren().add(bar_menu);

        tab_tabla1 = new Tabla();
        tab_tabla1.setId("tab_tabla1");
        tab_tabla1.setSql(ser_inversion.getSqlListaInversionesPorBanco("-1"));
        tab_tabla1.setNumeroTabla(21);
        tab_tabla1.setLectura(true);
        tab_tabla1.setCampoPrimaria("ide_ipcer");
        tab_tabla1.getColumna("ide_ipcer").setVisible(false);
        tab_tabla1.getColumna("ide_teban").setVisible(false);
        tab_tabla1.getColumna("CAPITAL").alinearDerecha();
        tab_tabla1.getColumna("INTERES").alinearDerecha();
        tab_tabla1.getColumna("CAPITAL_MAS_INTERES").alinearDerecha();
        tab_tabla1.getColumna("CAPITAL_MAS_INTERES").setLongitud(35);
        tab_tabla1.getColumna("FECHA_VENCIMIENTO").setLongitud(35);
        tab_tabla1.getColumna("FECHA_VENCIMIENTO").setEstilo("font-size:13px;font-weight: bold;");
        tab_tabla1.getColumna("FECHA_VENCIMIENTO").alinearCentro();
        tab_tabla1.getColumna("CAPITAL_MAS_INTERES").setEstilo("font-size:13px;font-weight: bold;");
        tab_tabla1.getColumna("ide_cnccc").setNombreVisual("N. ASIENTO");
        tab_tabla1.getColumna("IDE_CNCCC").setLink();
        tab_tabla1.getColumna("IDE_CNCCC").setMetodoChange("abrirAsiento");
        tab_tabla1.getColumna("IDE_CNCCC").alinearCentro();
        tab_tabla1.getColumna("NUM_CERTIFICADO").setFiltroContenido();
        tab_tabla1.getColumna("ESTADO").setFiltroContenido();
        tab_tabla1.setScrollable(true);
        tab_tabla1.setScrollHeight(170);
        tab_tabla1.onSelect("seleccionarCertifcadoBancarioRenova");
        tab_tabla1.dibujar();

        PanelTabla pat_panel = new PanelTabla();
        ItemMenu itemedita = new ItemMenu();
        itemedita.setValue("Generar Renovación");
        itemedita.setIcon("ui-icon-wrench");
        itemedita.setMetodo("abrirRenovacion");
        pat_panel.getMenuTabla().getChildren().add(itemedita);

        pat_panel.setPanelTabla(tab_tabla1);
        grupo.getChildren().add(pat_panel);

        tab_tabla2 = new Tabla();
        tab_tabla2.setId("tab_tabla2");
        tab_tabla2.setSql(ser_inversion.getSqlListaRenovacionesBanco("-1"));
        tab_tabla2.setNumeroTabla(22);
        tab_tabla2.setLectura(true);
        tab_tabla2.setCampoPrimaria("ide_ipcer");
        tab_tabla2.getColumna("ide_ipcer").setVisible(false);
        tab_tabla2.getColumna("CAPITAL").alinearDerecha();
        tab_tabla2.getColumna("INTERES").alinearDerecha();
        tab_tabla2.getColumna("CAPITAL_MAS_INTERES").alinearDerecha();
        tab_tabla2.getColumna("CAPITAL_MAS_INTERES").setLongitud(35);
        tab_tabla2.getColumna("FECHA_VENCIMIENTO").setLongitud(35);
        tab_tabla2.getColumna("FECHA_VENCIMIENTO").setEstilo("font-size:13px;font-weight: bold;");
        tab_tabla2.getColumna("FECHA_VENCIMIENTO").alinearCentro();
        tab_tabla2.getColumna("CAPITAL_MAS_INTERES").setEstilo("font-size:13px;font-weight: bold;");
        tab_tabla2.getColumna("ide_cnccc").setNombreVisual("N. ASIENTO");
        tab_tabla2.getColumna("IDE_CNCCC").setLink();
        tab_tabla2.getColumna("IDE_CNCCC").setMetodoChange("abrirAsiento");
        tab_tabla2.getColumna("IDE_CNCCC").alinearCentro();
        tab_tabla2.setScrollable(true);
        tab_tabla2.setScrollHeight(110);
        tab_tabla2.setHeader("RENOVACIONES REALIZADAS");

        tab_tabla2.dibujar();
        PanelTabla pat_panel1 = new PanelTabla();
        pat_panel1.setPanelTabla(tab_tabla2);
        grupo.getChildren().add(pat_panel1);

        mep_menu.dibujar(21, "RENOVACIONES DE INVERSIONES BANCOS", grupo);
        // metodo of mauriicio
        utilitario.buscarPermisosObjetos();
    }

    public void seleccionarCertifcadoRenova(SelectEvent evt) {
        tab_tabla1.seleccionarFila(evt);
        tab_tabla2.setSql(ser_inversion.getSqlListaRenovaciones(tab_tabla1.getValorSeleccionado()));
        tab_tabla2.ejecutarSql();
    }

    public void seleccionarCertifcadoBancarioRenova(SelectEvent evt) {
        tab_tabla1.seleccionarFila(evt);
        tab_tabla2.setSql(ser_inversion.getSqlListaRenovacionesBanco(tab_tabla1.getValorSeleccionado()));
        tab_tabla2.ejecutarSql();
    }

    public void cargarInversionesGrupo(SelectEvent evt) {
        aut_inversion.onSelect(evt);
        tab_tabla1.setSql(ser_inversion.getSqlListaInversionesCasasGrupo(aut_inversion.getValor()));
        tab_tabla1.ejecutarSql();
        tab_tabla2.setSql(ser_inversion.getSqlListaRenovaciones(tab_tabla1.getValorSeleccionado()));
        tab_tabla2.ejecutarSql();
    }

    public void cargarInversionesPorBanco() {
        if (com_bancos.getValue() != null) {
            tab_tabla1.setSql(ser_inversion.getSqlListaInversionesPorBanco(com_bancos.getValue().toString()));
            tab_tabla1.ejecutarSql();
            tab_tabla2.setSql(ser_inversion.getSqlListaRenovacionesBanco(tab_tabla1.getValorSeleccionado()));
            tab_tabla2.ejecutarSql();
        } else {
            tab_tabla1.limpiar();
            tab_tabla2.limpiar();
        }

    }

    public void limpiarRenovacion() {
        aut_inversion.limpiar();
        tab_tabla1.limpiar();
        tab_tabla2.limpiar();
    }

    public void dibujarCertificadoFondo() {
        tab_tabla1 = new Tabla();
        tab_tabla1.setId("tab_tabla1");
        tab_tabla1.setTabla("iyp_certificado", "ide_ipcer", 9);
        tab_tabla1.setCondicion("ide_ipcer=-1");
        //oculta todas las columnas        
        for (int i = 0; i < tab_tabla1.getTotalColumnas(); i++) {
            tab_tabla1.getColumnas()[i].setVisible(false);
        }
        tab_tabla1.getColumna("num_certificado_ipcer").setVisible(true);
        tab_tabla1.getColumna("ide_cnccc_terminacion").setVisible(true);
        tab_tabla1.getColumna("fecha_emision_ipcer").setVisible(true);
        tab_tabla1.getColumna("fecha_vence_ipcer").setVisible(true);
        tab_tabla1.getColumna("tasa_ipcer").setVisible(true);
        tab_tabla1.getColumna("plazo_ipcer").setVisible(true);
        tab_tabla1.getColumna("capital_ipcer").setVisible(true);
        tab_tabla1.getColumna("interes_ipcer").setVisible(true);
        tab_tabla1.getColumna("valor_a_pagar_ipcer").setVisible(true);
        tab_tabla1.getColumna("observacion_ipcer").setVisible(true);
        tab_tabla1.getColumna("fecha_sistema_ipcer").setValorDefecto(utilitario.getFechaActual());
        tab_tabla1.getColumna("activo_ipcer").setValorDefecto("true");
        tab_tabla1.getColumna("ide_cnmod").setVisible(true);
        tab_tabla1.getColumna("ide_cnccc").setVisible(true);
        tab_tabla1.getColumna("ide_cnmod").setCombo("select ide_cnmod,nombre_cnmod from con_moneda where ide_empr=" + utilitario.getVariable("ide_empr") + "  order by ide_cnmod ");
        tab_tabla1.getColumna("ide_cnmod").setPermitirNullCombo(false);
        tab_tabla1.getColumna("capital_ipcer").setMetodoChange("calcularInteres");
        tab_tabla1.getColumna("tasa_ipcer").setMetodoChange("calcularInteres");
        tab_tabla1.getColumna("plazo_ipcer").setMetodoChange("calcularInteres");
        tab_tabla1.getColumna("interes_ipcer").setEstilo("font-size:13px;font-weight: bold;");
        tab_tabla1.getColumna("valor_a_pagar_ipcer").setEstilo("font-size:12px;font-weight: bold;");
        tab_tabla1.getColumna("fecha_emision_ipcer").setMetodoChange("obtenetFechaVencimiento");
        tab_tabla1.getColumna("fecha_vence_ipcer").setLectura(true);
        tab_tabla1.getColumna("ide_iptin").setValorDefecto("2"); //FONDO
        tab_tabla1.getColumna("es_inver_banco_ipcer").setValorDefecto("false");
        tab_tabla1.getColumna("ide_geper").setVisible(true);
        tab_tabla1.getColumna("ide_geper").setRequerida(true);
        tab_tabla1.getColumna("ide_geper").setCombo(ser_inversion.getSqlComboClientes());
        tab_tabla1.getColumna("ide_geper").setAutoCompletar();
        tab_tabla1.getColumna("ide_ipein").setVisible(true);
        tab_tabla1.getColumna("ide_ipein").setCombo("iyp_estado_inversion", "ide_ipein", "nombre_ipein", "");
        tab_tabla1.getColumna("ide_ipcin").setValorDefecto("1");
        tab_tabla1.getColumna("ide_usua").setValorDefecto(utilitario.getVariable("ide_usua"));
        tab_tabla1.getColumna("nuevo").setValorDefecto("true");
        tab_tabla1.getColumna("cancelado").setVisible(true);
        tab_tabla1.setTipoFormulario(true);
        tab_tabla1.getGrid().setColumns(4);
        tab_tabla1.setMostrarNumeroRegistros(false);
        tab_tabla1.dibujar();
        if (tab_tabla1.isEmpty()) {
            tab_tabla1.insertar();
            tab_tabla1.setValor("num_certificado_ipcer", ser_inversion.getSecuencialNuevos("2"));
        }
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_tabla1);
        pat_panel.getMenuTabla().getItem_buscar().setRendered(false);
        pat_panel.getMenuTabla().getItem_eliminar().setRendered(false);
        pat_panel.getMenuTabla().getItem_actualizar().setRendered(false);
        pat_panel.getMenuTabla().getItem_importar().setRendered(false);

        Grid gri1 = new Grid();
        gri1.setColumns(2);
        gri1.getChildren().add(new Etiqueta("<div style='font-size:12px;font-weight: bold;'> <img src='imagenes/im_pregunta.gif' />  GENERAR NUEVO ASIENTO CONTABLE ? </div>"));
        rad_hace_asiento = new Radio();
        rad_hace_asiento.setRadio(utilitario.getListaPregunta());
        rad_hace_asiento.setValue(true);
        gri1.getChildren().add(rad_hace_asiento);
        pat_panel.getChildren().add(gri1);

        mep_menu.dibujar(9, "CERTIFICADO DE INVERSIÓN FONDO DE DESVINCULACIÓN", pat_panel);
    }

    public void dibujarCertificadoCasa() {
        Grupo grupo = new Grupo();
        tab_tabla2 = new Tabla();
        tab_tabla2.setId("tab_tabla2");
        tab_tabla2.setTabla("iyp_cab_inversion", "ide_ipcai", 6);
        tab_tabla2.setCondicion("ide_ipcai=" + ide_ipcai);
        tab_tabla2.setTipoFormulario(true);
        tab_tabla2.getColumna("ide_ipcai").setVisible(false);
        tab_tabla2.getColumna("ide_usua").setVisible(true);
        tab_tabla2.getColumna("ide_usua").setCombo("sis_usuario", "ide_usua", "nom_usua", "ide_usua=" + utilitario.getVariable("ide_usua"));
        tab_tabla2.getColumna("ide_usua").setValorDefecto(utilitario.getVariable("ide_usua"));
        tab_tabla2.getColumna("ide_usua").setLectura(true);
        tab_tabla2.getColumna("ide_geper").setCombo(ser_inversion.getSqlComboClientes());
        tab_tabla2.getColumna("ide_geper").setRequerida(true);
        tab_tabla2.getColumna("ide_geper").setAutoCompletar();
        tab_tabla2.getColumna("ide_geper_ben").setCombo(tab_tabla2.getColumna("ide_geper").getListaCombo());
        tab_tabla2.getColumna("ide_geper_ben").setAutoCompletar();
        tab_tabla2.getColumna("ide_geper_ben").setRequerida(true);
        tab_tabla2.getColumna("beneficiario_ipcai").setRequerida(true);
        tab_tabla2.getColumna("observacion_ipcai").setRequerida(true);
        tab_tabla2.getColumna("observacion_ipcai").setMetodoChange("cargarObservacionCertificado");
        tab_tabla2.getColumna("ide_geper_ben").setMetodoChange("selecionarCasa");
        tab_tabla2.getColumna("fecha_inicio_ipcai").setValorDefecto(utilitario.getFechaActual());
        tab_tabla2.getColumna("ide_iptin").setValorDefecto("1");
        tab_tabla2.getColumna("ide_iptin").setVisible(false);
        tab_tabla2.getColumna("activo_ipcai").setValorDefecto("true");
        tab_tabla2.getGrid().setColumns(4);
        tab_tabla2.setMostrarNumeroRegistros(false);
        tab_tabla2.dibujar();
        PanelTabla pat_panel1 = new PanelTabla();
        pat_panel1.setPanelTabla(tab_tabla2);
        pat_panel1.getMenuTabla().getItem_actualizar().setRendered(false);
        pat_panel1.getMenuTabla().getItem_buscar().setRendered(false);
        pat_panel1.getMenuTabla().getItem_importar().setRendered(false);
        pat_panel1.getMenuTabla().getItem_eliminar().setRendered(false);
        pat_panel1.getMenuTabla().getItem_insertar().setRendered(false);
        if (tab_tabla2.isEmpty()) {
            tab_tabla2.insertar();
        }
        grupo.getChildren().add(pat_panel1);

        tab_tabla1 = new Tabla();
        tab_tabla1.setId("tab_tabla1");
        tab_tabla1.setTabla("iyp_certificado", "ide_ipcer", 16);
        tab_tabla1.setCondicion("ide_ipcer=-1");
        //oculta todas las columnas        
        for (int i = 0; i < tab_tabla1.getTotalColumnas(); i++) {
            tab_tabla1.getColumnas()[i].setVisible(false);
        }
        tab_tabla1.getColumna("num_certificado_ipcer").setVisible(true);
        tab_tabla1.getColumna("ide_cnccc_terminacion").setVisible(true);
        tab_tabla1.getColumna("fecha_emision_ipcer").setVisible(true);
        tab_tabla1.getColumna("fecha_vence_ipcer").setVisible(true);
        tab_tabla1.getColumna("tasa_ipcer").setVisible(true);
        tab_tabla1.getColumna("plazo_ipcer").setVisible(true);
        tab_tabla1.getColumna("capital_ipcer").setVisible(true);
        tab_tabla1.getColumna("interes_ipcer").setVisible(true);
        tab_tabla1.getColumna("valor_a_pagar_ipcer").setVisible(true);
        tab_tabla1.getColumna("observacion_ipcer").setVisible(true);
        tab_tabla1.getColumna("fecha_sistema_ipcer").setValorDefecto(utilitario.getFechaActual());
        tab_tabla1.getColumna("activo_ipcer").setValorDefecto("true");
        tab_tabla1.getColumna("ide_cnmod").setVisible(true);
        tab_tabla1.getColumna("ide_cnccc").setVisible(true);
        tab_tabla1.getColumna("ide_cnmod").setCombo("select ide_cnmod,nombre_cnmod from con_moneda where ide_empr=" + utilitario.getVariable("ide_empr") + "  order by ide_cnmod ");
        tab_tabla1.getColumna("ide_cnmod").setPermitirNullCombo(false);
        tab_tabla1.getColumna("capital_ipcer").setMetodoChange("calcularInteres");
        tab_tabla1.getColumna("tasa_ipcer").setMetodoChange("calcularInteres");
        tab_tabla1.getColumna("plazo_ipcer").setMetodoChange("calcularInteres");
        tab_tabla1.getColumna("interes_ipcer").setEstilo("font-size:13px;font-weight: bold;");
        tab_tabla1.getColumna("valor_a_pagar_ipcer").setEstilo("font-size:12px;font-weight: bold;");
        tab_tabla1.getColumna("fecha_emision_ipcer").setMetodoChange("obtenetFechaVencimiento");
        tab_tabla1.getColumna("fecha_vence_ipcer").setLectura(true);
        tab_tabla1.getColumna("ide_iptin").setValorDefecto("1"); //CASAS OBRAS
        tab_tabla1.getColumna("es_inver_banco_ipcer").setValorDefecto("false"); //BANCOS        
        tab_tabla1.getColumna("ide_ipein").setVisible(true);
        tab_tabla1.getColumna("ide_ipein").setCombo("iyp_estado_inversion", "ide_ipein", "nombre_ipein", "");
        tab_tabla1.getColumna("ide_ipein").setMetodoChange("cambioEstado");
        tab_tabla1.getColumna("ide_ipcin").setVisible(true);
        tab_tabla1.getColumna("ide_ipcin").setCombo("iyp_clase_inversion", "ide_ipcin", "nombre_ipcin", "");
        tab_tabla1.getColumna("ide_usua").setValorDefecto(utilitario.getVariable("ide_usua"));
        tab_tabla1.getColumna("nuevo").setValorDefecto("true");
        tab_tabla1.getColumna("cancelado").setVisible(true);
        tab_tabla1.setTipoFormulario(true);
        tab_tabla1.getGrid().setColumns(4);
        tab_tabla1.setMostrarNumeroRegistros(false);
        tab_tabla1.setHeader("DATOS DEL CERTIFICADO");
        tab_tabla1.dibujar();
        tab_tabla1.insertar();
        tab_tabla1.setValor("num_certificado_ipcer", ser_inversion.getSecuencialNuevos("1"));
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_tabla1);
        pat_panel.getMenuTabla().getItem_buscar().setRendered(false);
        pat_panel.getMenuTabla().getItem_eliminar().setRendered(false);
        pat_panel.getMenuTabla().getItem_actualizar().setRendered(false);
        pat_panel.getMenuTabla().getItem_importar().setRendered(false);
        pat_panel.getMenuTabla().getItem_insertar().setRendered(false);
        grupo.getChildren().add(pat_panel);

        gri = new Grid();
        gri.setId("gri");
        // gri.setRendered(false); //solo cuando se inseta se hace visible
        gri.setColumns(2);
        gri.getChildren().add(new Etiqueta("<div style='font-size:12px;font-weight: bold;'> <img src='imagenes/im_pregunta.gif' />  GENERAR NUEVO ASIENTO CONTABLE ? </div>"));
        rad_hace_asiento = new Radio();
        rad_hace_asiento.setRadio(utilitario.getListaPregunta());
        rad_hace_asiento.setValue(true);
        gri.getChildren().add(rad_hace_asiento);
        pat_panel.getChildren().add(gri);
        if (ide_ipcai.equals("-1")) {
            mep_menu.dibujar(6, "LISTADO DE INVERSIONES CASAS - OBRAS", grupo);
        } else {
            tab_tabla1.setValor("iyp_ide_ipcer", iyp_ide_ipcer);
            tab_tabla1.setValor("ide_geper", tab_tabla2.getValor("ide_geper_ben"));
            tab_tabla1.setValor("es_renovacion_ipcer", "true");
            mep_menu.dibujar(6, "RENOVACIÓN INVERSION CASAS - OBRAS", grupo);
        }

    }

    public void cambioEstado(AjaxBehaviorEvent evt) {
        tab_tabla1.modificar(evt);
        if (tab_tabla1.getValor("ide_ipein") != null) {
            switch (tab_tabla1.getValor("ide_ipein")) {
                case "8":
                case "6":
                    //renovacion x capital
                    tab_tabla1.setValor("capital_ipcer", utilitario.getFormatoNumero(dou_capital_renova));
                    break;
                case "7":
                    //renovacion x interes
                    tab_tabla1.setValor("capital_ipcer", utilitario.getFormatoNumero(dou_interes_renova));
                    break;
                case "10":
                    //renovacion x capital e interes
                    tab_tabla1.setValor("capital_ipcer", utilitario.getFormatoNumero(dou_interes_renova + dou_capital_renova));
                    break;
            }
        }
        utilitario.addUpdateTabla(tab_tabla1, "capital_ipcer", "");

    }

    public void cargarObservacionCertificado() {
        tab_tabla1.setValor("observacion_ipcer", tab_tabla2.getValor("observacion_ipcai"));
        utilitario.addUpdateTabla(tab_tabla1, "observacion_ipcer", "");
    }

    public void selecionarCasa(SelectEvent evt) {
        tab_tabla1.setValor("ide_geper", tab_tabla2.getValor("ide_geper_ben"));
        tab_tabla2.setValor("beneficiario_ipcai", tab_tabla2.getValorArreglo("ide_geper_ben", 2));
        utilitario.addUpdateTabla(tab_tabla2, "beneficiario_ipcai", "");
    }

    public void cancelarInversion() {
        if (tab_tabla1.getValor("ide_ipcer") != null) {
            if (tab_tabla1.getValor("cancelado") != null && tab_tabla1.getValor("cancelado").equals("true")) {
                utilitario.agregarMensajeInfo("La inversión seleccionada ya se encuentra Cancelada", "");
                return;
            }
            ser_inversion.cancelarInversion(tab_tabla1.getValor("ide_ipcer"));
            if (guardarPantalla().isEmpty()) {
                String aux = tab_tabla1.getValorSeleccionado();
                tab_tabla1.actualizar();
                tab_tabla1.setFilaActual(aux);
                tab_tabla1.calcularPaginaActual();
            }
        }
    }

    public void abrirAnularIversion() {
        if (tab_tabla1.getValor("ide_ipcer") != null) {
            con_confirma_anular.getBot_aceptar().setMetodo("anularInversion");
            con_confirma_anular.dibujar();
        }
    }

    public void anularInversion() {
        if (tab_tabla1.getValor("ide_ipcer") != null) {
            ser_inversion.anularInversion(tab_tabla1.getValor("ide_ipcer"));
            if (guardarPantalla().isEmpty()) {
                con_confirma_anular.cerrar();
                tab_tabla1.actualizar();
            }
        }
    }

    public void dibujarListadoFondo() {
        tab_tabla1 = new Tabla();
        tab_tabla1.setId("tab_tabla1");
        tab_tabla1.setSql(ser_inversion.getSqlListaInversionesFondo());
        tab_tabla1.setNumeroTabla(5);
        tab_tabla1.setLectura(true);
        tab_tabla1.setCampoPrimaria("ide_ipcer");
        tab_tabla1.getColumna("ide_ipcer").setVisible(false);
        tab_tabla1.setColumnaSuma("CAPITAL,INTERES,CAPITAL_MAS_INTERES");
        tab_tabla1.getColumna("CAPITAL").alinearDerecha();
        tab_tabla1.getColumna("INTERES").alinearDerecha();
        tab_tabla1.getColumna("CAPITAL_MAS_INTERES").alinearDerecha();
        tab_tabla1.getColumna("CAPITAL_MAS_INTERES").setLongitud(35);
        tab_tabla1.getColumna("FECHA_VENCIMIENTO").setLongitud(35);
        tab_tabla1.getColumna("FECHA_VENCIMIENTO").setEstilo("font-size:13px;font-weight: bold;");
        tab_tabla1.getColumna("FECHA_VENCIMIENTO").alinearCentro();
        tab_tabla1.getColumna("CAPITAL_MAS_INTERES").setEstilo("font-size:13px;font-weight: bold;");
        tab_tabla1.getColumna("ide_cnccc").setNombreVisual("N. ASIENTO");
        tab_tabla1.getColumna("IDE_CNCCC").setLink();
        tab_tabla1.getColumna("IDE_CNCCC").setMetodoChange("abrirAsiento");
        tab_tabla1.getColumna("IDE_CNCCC").alinearCentro();

        tab_tabla1.getColumna("ide_cnccc_terminacion").setNombreVisual("N. ASIENTO C.");
        tab_tabla1.getColumna("ide_cnccc_terminacion").setLink();
        tab_tabla1.getColumna("ide_cnccc_terminacion").setMetodoChange("abrirAsiento");
        tab_tabla1.getColumna("ide_cnccc_terminacion").alinearCentro();

        tab_tabla1.getColumna("NUM_CERTIFICADO").setFiltroContenido();
        tab_tabla1.getColumna("ESTADO").setFiltroContenido();
        tab_tabla1.setRows(15);
        tab_tabla1.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        ItemMenu itemedita = new ItemMenu();
        itemedita.setValue("Modificar");
        itemedita.setIcon("ui-icon-pencil");
        itemedita.setMetodo("abrirModificarF");
        pat_panel.getMenuTabla().getChildren().add(itemedita);
        ItemMenu itemcancela = new ItemMenu();
        itemcancela.setValue("Cancelar Inversión");
        itemcancela.setIcon("ui-icon-check");
        itemcancela.setMetodo("cancelarInversion");
        pat_panel.getMenuTabla().getChildren().add(itemcancela);
        
             ItemMenu itemanula = new ItemMenu();
        itemanula.setValue("Anular Inversión");
        itemanula.setIcon("ui-icon-close");
        itemanula.setMetodo("abrirAnularIversion");
        pat_panel.getMenuTabla().getChildren().add(itemanula);
        pat_panel.setPanelTabla(tab_tabla1);
        pat_panel.getMenuTabla().getItem_buscar().setRendered(false);

        mep_menu.dibujar(8, "LISTADO DE INVERSIONES FONDO DE DESVINCULACIÓN", pat_panel);
    }

    public void abrirModificarF() {
        String ide_aux = tab_tabla1.getValor("ide_ipcer");
        if (ide_aux != null) {
            //oculta todas las columnas        
            tab_tabla1 = new Tabla();
            tab_tabla1.setId("tab_tabla1");
            tab_tabla1.setTabla("iyp_certificado", "ide_ipcer", 9);
            tab_tabla1.setCondicion("ide_ipcer=" + ide_aux);
            for (int i = 0; i < tab_tabla1.getTotalColumnas(); i++) {
                tab_tabla1.getColumnas()[i].setVisible(false);
            }
            tab_tabla1.getColumna("num_certificado_ipcer").setVisible(true);
            tab_tabla1.getColumna("fecha_emision_ipcer").setVisible(true);
            tab_tabla1.getColumna("fecha_vence_ipcer").setVisible(true);
            tab_tabla1.getColumna("tasa_ipcer").setVisible(true);
            tab_tabla1.getColumna("plazo_ipcer").setVisible(true);
            tab_tabla1.getColumna("capital_ipcer").setVisible(true);
            tab_tabla1.getColumna("interes_ipcer").setVisible(true);
            tab_tabla1.getColumna("valor_a_pagar_ipcer").setVisible(true);
            tab_tabla1.getColumna("observacion_ipcer").setVisible(true);
            tab_tabla1.getColumna("fecha_sistema_ipcer").setValorDefecto(utilitario.getFechaActual());
            tab_tabla1.getColumna("activo_ipcer").setValorDefecto("true");
            tab_tabla1.getColumna("ide_cnmod").setVisible(true);
            tab_tabla1.getColumna("ide_cnccc").setVisible(true);
            tab_tabla1.getColumna("ide_cnmod").setCombo("select ide_cnmod,nombre_cnmod from con_moneda where ide_empr=" + utilitario.getVariable("ide_empr") + "  order by ide_cnmod ");
            tab_tabla1.getColumna("ide_cnmod").setPermitirNullCombo(false);
            tab_tabla1.getColumna("capital_ipcer").setMetodoChange("calcularInteres");
            tab_tabla1.getColumna("tasa_ipcer").setMetodoChange("calcularInteres");
            tab_tabla1.getColumna("plazo_ipcer").setMetodoChange("calcularInteres");
            tab_tabla1.getColumna("interes_ipcer").setEstilo("font-size:13px;font-weight: bold;");
            tab_tabla1.getColumna("valor_a_pagar_ipcer").setEstilo("font-size:12px;font-weight: bold;");
            tab_tabla1.getColumna("fecha_emision_ipcer").setMetodoChange("obtenetFechaVencimiento");
            tab_tabla1.getColumna("fecha_vence_ipcer").setLectura(true);
            tab_tabla1.getColumna("ide_iptin").setValorDefecto("2"); //FONDO
            tab_tabla1.getColumna("es_inver_banco_ipcer").setValorDefecto("false");
            tab_tabla1.getColumna("ide_geper").setValorDefecto("1294"); //SOCIEDAD SALESIANA EN EL ECUADOR
            tab_tabla1.getColumna("ide_ipein").setVisible(true);
            tab_tabla1.getColumna("ide_ipein").setCombo("iyp_estado_inversion", "ide_ipein", "nombre_ipein", "");
            tab_tabla1.getColumna("ide_ipcin").setValorDefecto("1");
            tab_tabla1.getColumna("ide_geper").setVisible(true);
            tab_tabla1.getColumna("ide_geper").setRequerida(true);
            tab_tabla1.getColumna("ide_geper").setCombo(ser_inversion.getSqlComboClientes());
            tab_tabla1.getColumna("ide_geper").setAutoCompletar();
            tab_tabla1.getColumna("ide_usua").setValorDefecto(utilitario.getVariable("ide_usua"));
            tab_tabla1.getColumna("nuevo").setValorDefecto("true");
            tab_tabla1.getColumna("cancelado").setVisible(true);
            tab_tabla1.setTipoFormulario(true);
            tab_tabla1.getGrid().setColumns(4);
            tab_tabla1.setMostrarNumeroRegistros(false);
            tab_tabla1.dibujar();
            PanelTabla pat_panel = new PanelTabla();
            pat_panel.setPanelTabla(tab_tabla1);
            pat_panel.getMenuTabla().getItem_buscar().setRendered(false);
            pat_panel.getMenuTabla().getItem_eliminar().setRendered(false);
            pat_panel.getMenuTabla().getItem_actualizar().setRendered(false);
            pat_panel.getMenuTabla().getItem_importar().setRendered(false);

            mep_menu.dibujar(10, "MODIFICAR CERTIFICADO DE INVERSIÓN FONDO DE DESVINCULACIÓN", pat_panel);
        } else {
            utilitario.agregarMensajeInfo("Seleccione un certificado", "");
        }

    }

    public void dibujarListadoCasas() {
        tab_tabla1 = new Tabla();
        tab_tabla1.setId("tab_tabla1");
        tab_tabla1.setSql(ser_inversion.getSqlListaInversionesCasas());
        tab_tabla1.setNumeroTabla(5);
        tab_tabla1.setLectura(true);
        tab_tabla1.setCampoPrimaria("ide_ipcer");
        tab_tabla1.getColumna("ide_ipcer").setVisible(false);
        tab_tabla1.getColumna("ide_ipcai").setVisible(false);
        tab_tabla1.setColumnaSuma("CAPITAL,INTERES,CAPITAL_MAS_INTERES");
        tab_tabla1.getColumna("CAPITAL").alinearDerecha();
        tab_tabla1.getColumna("INTERES").alinearDerecha();
        tab_tabla1.getColumna("CAPITAL_MAS_INTERES").alinearDerecha();
        tab_tabla1.getColumna("CAPITAL_MAS_INTERES").setLongitud(35);
        tab_tabla1.getColumna("FECHA_VENCIMIENTO").setLongitud(35);
        tab_tabla1.getColumna("FECHA_VENCIMIENTO").setEstilo("font-size:13px;font-weight: bold;");
        tab_tabla1.getColumna("FECHA_VENCIMIENTO").alinearCentro();
        tab_tabla1.getColumna("CAPITAL_MAS_INTERES").setEstilo("font-size:13px;font-weight: bold;");
        tab_tabla1.getColumna("ide_cnccc").setNombreVisual("N. ASIENTO");
        tab_tabla1.getColumna("IDE_CNCCC").setLink();
        tab_tabla1.getColumna("IDE_CNCCC").setMetodoChange("abrirAsiento");
        tab_tabla1.getColumna("IDE_CNCCC").alinearCentro();
        tab_tabla1.getColumna("CASAS_OBRAS").setFiltroContenido();
        tab_tabla1.getColumna("GRUPO").setFiltroContenido();
        tab_tabla1.getColumna("NUM_CERTIFICADO").setFiltroContenido();
        tab_tabla1.getColumna("ESTADO").setFiltroContenido();
        tab_tabla1.setRows(15);
        tab_tabla1.getColumna("ide_cnccc_terminacion").setNombreVisual("N. ASIENTO T.");
        tab_tabla1.getColumna("ide_cnccc_terminacion").setLink();
        tab_tabla1.getColumna("ide_cnccc_terminacion").setMetodoChange("abrirAsiento");
        tab_tabla1.getColumna("ide_cnccc_terminacion").alinearCentro();
        tab_tabla1.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        ItemMenu itemedita = new ItemMenu();
        itemedita.setValue("Modificar");
        itemedita.setIcon("ui-icon-pencil");
        itemedita.setMetodo("abrirModificarC");
        pat_panel.getMenuTabla().getChildren().add(itemedita);

        ItemMenu itemcancela = new ItemMenu();
        itemcancela.setValue("Cancelar Inversión");
        itemcancela.setIcon("ui-icon-check");
        itemcancela.setMetodo("cancelarInversion");
        pat_panel.getMenuTabla().getChildren().add(itemcancela);

             ItemMenu itemanula = new ItemMenu();
        itemanula.setValue("Anular Inversión");
        itemanula.setIcon("ui-icon-close");
        itemanula.setMetodo("abrirAnularIversion");
        pat_panel.getMenuTabla().getChildren().add(itemanula);
        
        pat_panel.setPanelTabla(tab_tabla1);
        pat_panel.getMenuTabla().getItem_buscar().setRendered(false);

        mep_menu.dibujar(5, "LISTADO DE INVERSIONES CASAS - OBRAS", pat_panel);
    }

    public void abrirModificarC() {
        String ide_aux = tab_tabla1.getValor("ide_ipcer");
        String ide_aux1 = tab_tabla1.getValor("ide_ipcai");
        if (ide_aux != null) {
            Grupo grupo = new Grupo();
            tab_tabla2 = new Tabla();
            tab_tabla2.setId("tab_tabla2");
            tab_tabla2.setTabla("iyp_cab_inversion", "ide_ipcai", 6);
            tab_tabla2.setCondicion("ide_ipcai=" + ide_aux1);
            tab_tabla2.setTipoFormulario(true);
            tab_tabla2.getColumna("ide_ipcai").setVisible(false);
            tab_tabla2.getColumna("ide_usua").setVisible(true);
            tab_tabla2.getColumna("ide_usua").setCombo("sis_usuario", "ide_usua", "nom_usua", "ide_usua=" + utilitario.getVariable("ide_usua"));
            tab_tabla2.getColumna("ide_usua").setValorDefecto(utilitario.getVariable("ide_usua"));
            tab_tabla2.getColumna("ide_usua").setLectura(true);
            tab_tabla2.getColumna("ide_geper").setCombo(ser_inversion.getSqlComboClientes());
            tab_tabla2.getColumna("ide_geper").setRequerida(true);
            tab_tabla2.getColumna("ide_geper").setAutoCompletar();
            tab_tabla2.getColumna("ide_geper_ben").setCombo(tab_tabla2.getColumna("ide_geper").getListaCombo());
            tab_tabla2.getColumna("ide_geper_ben").setAutoCompletar();
            tab_tabla2.getColumna("ide_geper_ben").setRequerida(true);
            tab_tabla2.getColumna("beneficiario_ipcai").setRequerida(true);
            tab_tabla2.getColumna("observacion_ipcai").setRequerida(true);
            tab_tabla2.getColumna("observacion_ipcai").setMetodoChange("cargarObservacionCertificado");
            tab_tabla2.getColumna("ide_geper_ben").setMetodoChange("selecionarCasa");
            tab_tabla2.getColumna("fecha_inicio_ipcai").setValorDefecto(utilitario.getFechaActual());
            tab_tabla2.getColumna("ide_iptin").setValorDefecto("1");
            tab_tabla2.getColumna("ide_iptin").setVisible(false);
            tab_tabla2.getColumna("activo_ipcai").setValorDefecto("true");
            tab_tabla2.getGrid().setColumns(4);
            tab_tabla2.setMostrarNumeroRegistros(false);
            tab_tabla2.dibujar();
            PanelTabla pat_panel1 = new PanelTabla();
            pat_panel1.setPanelTabla(tab_tabla2);
            pat_panel1.getMenuTabla().getItem_actualizar().setRendered(false);
            pat_panel1.getMenuTabla().getItem_buscar().setRendered(false);
            pat_panel1.getMenuTabla().getItem_importar().setRendered(false);
            pat_panel1.getMenuTabla().getItem_eliminar().setRendered(false);
            pat_panel1.getMenuTabla().getItem_insertar().setRendered(false);
            grupo.getChildren().add(pat_panel1);

            tab_tabla1 = new Tabla();
            tab_tabla1.setId("tab_tabla1");
            tab_tabla1.setTabla("iyp_certificado", "ide_ipcer", 16);
            tab_tabla1.setCondicion("ide_ipcer=" + ide_aux);
            //oculta todas las columnas        
            for (int i = 0; i < tab_tabla1.getTotalColumnas(); i++) {
                tab_tabla1.getColumnas()[i].setVisible(false);
            }
            tab_tabla1.getColumna("num_certificado_ipcer").setVisible(true);
            tab_tabla1.getColumna("fecha_emision_ipcer").setVisible(true);
            tab_tabla1.getColumna("fecha_vence_ipcer").setVisible(true);
            tab_tabla1.getColumna("tasa_ipcer").setVisible(true);
            tab_tabla1.getColumna("plazo_ipcer").setVisible(true);
            tab_tabla1.getColumna("capital_ipcer").setVisible(true);
            tab_tabla1.getColumna("interes_ipcer").setVisible(true);
            tab_tabla1.getColumna("valor_a_pagar_ipcer").setVisible(true);
            tab_tabla1.getColumna("observacion_ipcer").setVisible(true);
            tab_tabla1.getColumna("cancelado").setVisible(true);
            tab_tabla1.getColumna("fecha_sistema_ipcer").setValorDefecto(utilitario.getFechaActual());
            tab_tabla1.getColumna("activo_ipcer").setValorDefecto("true");
            tab_tabla1.getColumna("ide_cnmod").setVisible(true);
            tab_tabla1.getColumna("ide_cnccc").setVisible(true);
            tab_tabla1.getColumna("ide_cnmod").setCombo("select ide_cnmod,nombre_cnmod from con_moneda where ide_empr=" + utilitario.getVariable("ide_empr") + "  order by ide_cnmod ");
            tab_tabla1.getColumna("ide_cnmod").setPermitirNullCombo(false);
            tab_tabla1.getColumna("capital_ipcer").setMetodoChange("calcularInteres");
            tab_tabla1.getColumna("tasa_ipcer").setMetodoChange("calcularInteres");
            tab_tabla1.getColumna("plazo_ipcer").setMetodoChange("calcularInteres");
            tab_tabla1.getColumna("interes_ipcer").setEstilo("font-size:13px;font-weight: bold;");
            tab_tabla1.getColumna("valor_a_pagar_ipcer").setEstilo("font-size:12px;font-weight: bold;");
            tab_tabla1.getColumna("fecha_emision_ipcer").setMetodoChange("obtenetFechaVencimiento");
            tab_tabla1.getColumna("fecha_vence_ipcer").setLectura(true);
            tab_tabla1.getColumna("ide_iptin").setValorDefecto("1"); //CASAS OBRAS
            tab_tabla1.getColumna("es_inver_banco_ipcer").setValorDefecto("false"); //BANCOS        
            tab_tabla1.getColumna("ide_ipein").setVisible(true);
            tab_tabla1.getColumna("ide_ipein").setCombo("iyp_estado_inversion", "ide_ipein", "nombre_ipein", "");
            tab_tabla1.getColumna("ide_ipcin").setVisible(true);
            tab_tabla1.getColumna("ide_ipcin").setCombo("iyp_clase_inversion", "ide_ipcin", "nombre_ipcin", "");
            tab_tabla1.getColumna("ide_usua").setValorDefecto(utilitario.getVariable("ide_usua"));
            tab_tabla1.getColumna("nuevo").setValorDefecto("true");
            tab_tabla1.setTipoFormulario(true);
            tab_tabla1.getGrid().setColumns(4);
            tab_tabla1.setMostrarNumeroRegistros(false);
            tab_tabla1.setHeader("DATOS DEL CERTIFICADO");
            tab_tabla1.dibujar();
            //tab_tabla1.setValor("num_certificado_ipcer", ser_inversion.getSecuencialNuevos("1"));
            PanelTabla pat_panel = new PanelTabla();
            pat_panel.setPanelTabla(tab_tabla1);
            pat_panel.getMenuTabla().getItem_buscar().setRendered(false);
            pat_panel.getMenuTabla().getItem_eliminar().setRendered(false);
            pat_panel.getMenuTabla().getItem_actualizar().setRendered(false);
            pat_panel.getMenuTabla().getItem_importar().setRendered(false);
            pat_panel.getMenuTabla().getItem_insertar().setRendered(false);
            grupo.getChildren().add(pat_panel);
            mep_menu.dibujar(11, "MODIFICAR CERTIFICADO DE INVERSIÓN CASAS - OBRAS", grupo);
        } else {
            utilitario.agregarMensajeInfo("Seleccione un certificado", "");
        }

    }

    public void abrirRenovacionBanco() {
        if (tab_tabla1.getValor("ide_teban") != null) {
            ide_teban = tab_tabla1.getValor("ide_teban");
            iyp_ide_ipcer = tab_tabla1.getValor("ide_ipcer");
            dou_capital_renova = 0;
            dou_interes_renova = 0;
            try {
                dou_capital_renova = Double.parseDouble(tab_tabla1.getValor("CAPITAL"));
            } catch (Exception e) {
            }
            try {
                dou_interes_renova = Double.parseDouble(tab_tabla1.getValor("INTERES"));
            } catch (Exception e) {
            }

            dibujarCertificadoB();
        } else {
            ide_teban = "-1";
            iyp_ide_ipcer = null;
            utilitario.agregarMensajeInfo("Seleccione un certificado de inversión", "");
        }
    }

    public void abrirRenovacion() {
        if (tab_tabla1.getValor("ide_ipcai") != null) {
            ide_ipcai = tab_tabla1.getValor("ide_ipcai");
            iyp_ide_ipcer = tab_tabla1.getValor("ide_ipcer");

            dou_capital_renova = 0;
            dou_interes_renova = 0;
            try {
                dou_capital_renova = Double.parseDouble(tab_tabla1.getValor("CAPITAL"));
            } catch (Exception e) {
            }
            try {
                dou_interes_renova = Double.parseDouble(tab_tabla1.getValor("INTERES"));
            } catch (Exception e) {
            }

            dibujarCertificadoCasa();
        } else {
            ide_ipcai = "-1";
            iyp_ide_ipcer = null;
            utilitario.agregarMensajeInfo("Seleccione un certificado de inversión", "");
        }

    }

    public void dibujarPagoC() {
        Grupo gru = new Grupo();
        aut_inversion = new AutoCompletar();
        aut_inversion.setId("aut_inversion");
        aut_inversion.setAutoCompletar(ser_inversion.getSqlComboListaInversionesCasas());
        aut_inversion.setMaxResults(25);
        aut_inversion.setAutocompletarContenido();
        aut_inversion.setMetodoChange("cargarPagosInteres");
        Grid gr = new Grid();
        gr.setColumns(3);
        gr.getChildren().add(new Etiqueta("<strong>INVERSIÓN CASA/OBRA : </strong>"));
        gr.getChildren().add(aut_inversion);
        Boton bot_clean = new Boton();
        bot_clean.setIcon("ui-icon-cancel");
        bot_clean.setTitle("Limpiar");
        bot_clean.setMetodo("limpiarPagoB");
        gr.getChildren().add(bot_clean);
        gru.getChildren().add(gr);

        tab_tabla2 = new Tabla();
        tab_tabla2.setId("tab_tabla2");
        tab_tabla2.setHeader("NUEVO PAGO DE INTERESES");
        tab_tabla2.setTabla("iyp_pago_interes", "ide_ippin", 4);
        tab_tabla2.setCondicion("ide_ippin=-1");
        tab_tabla2.setTipoFormulario(true);
        tab_tabla2.setValidarInsertar(true);
        tab_tabla2.getColumna("ide_ippin").setVisible(false);
        tab_tabla2.getColumna("ide_ipcer").setVisible(false);
        tab_tabla2.getColumna("fecha_sistema_ippin").setVisible(false);
        tab_tabla2.getColumna("valor_ippin").alinearDerecha();
        tab_tabla2.getColumna("valor_ippin").setRequerida(true);
        tab_tabla2.getColumna("fecha_pago_ippin").setRequerida(true);
        tab_tabla2.getColumna("fecha_pago_ippin").setValorDefecto(utilitario.getFechaActual());
        tab_tabla2.getColumna("observacion_ippin").setRequerida(true);
        tab_tabla2.getColumna("pagado_ippin").setValorDefecto("true");
        tab_tabla2.getColumna("num_pago_ippin").setRequerida(true);
        tab_tabla2.getColumna("capital_ippin").setVisible(false);
        tab_tabla2.getColumna("interes_ippin").setVisible(false);
        tab_tabla2.getColumna("ide_cnccc").setNombreVisual("N. ASIENTO");
        tab_tabla2.getGrid().setColumns(4);
        tab_tabla2.setMostrarNumeroRegistros(false);
        tab_tabla2.dibujar();
        PanelTabla pat_panel2 = new PanelTabla();
        pat_panel2.setPanelTabla(tab_tabla2);
        pat_panel2.getMenuTabla().getItem_buscar().setRendered(false);
        pat_panel2.getMenuTabla().getItem_insertar().setRendered(false);
        pat_panel2.getMenuTabla().getItem_eliminar().setRendered(false);
        pat_panel2.getMenuTabla().getItem_importar().setRendered(false);
        pat_panel2.getMenuTabla().getItem_actualizar().setRendered(false);
        tab_tabla2.insertar();

        gri = new Grid();
        gri.setId("gri");
        // gri.setRendered(false); //solo cuando se inseta se hace visible
        gri.setColumns(2);
        gri.getChildren().add(new Etiqueta("<div style='font-size:12px;font-weight: bold;'> <img src='imagenes/im_pregunta.gif' />  GENERAR NUEVO ASIENTO CONTABLE ? </div>"));
        rad_hace_asiento = new Radio();
        rad_hace_asiento.setRadio(utilitario.getListaPregunta());
        rad_hace_asiento.setValue(true);
        gri.getChildren().add(rad_hace_asiento);

        gru.getChildren().add(pat_panel2);
        Boton bt = new Boton();
        bt.setMetodo("guardar");
        bt.setValue("Aceptar");
        gru.getChildren().add(gri);
        gru.getChildren().add(bt);
        gru.getChildren().add(new Separator());

        tab_tabla1 = new Tabla();
        tab_tabla1.setId("tab_tabla1");
        tab_tabla1.setHeader("DETALLE DE PAGOS INTERESES");
        tab_tabla1.setTabla("iyp_pago_interes", "ide_ippin", 14);
        tab_tabla1.setCondicion("ide_ippin=-1");
        tab_tabla1.setLectura(true);
        tab_tabla1.setColumnaSuma("valor_ippin");
        tab_tabla1.setValidarInsertar(true);
        tab_tabla1.getColumna("ide_ippin").setVisible(false);
        tab_tabla1.getColumna("ide_ipcer").setVisible(false);
        tab_tabla1.getColumna("capital_ippin").setVisible(false);
        tab_tabla1.getColumna("interes_ippin").setVisible(false);
        tab_tabla1.getColumna("fecha_sistema_ippin").setVisible(false);
        tab_tabla1.getColumna("valor_ippin").alinearDerecha();
        tab_tabla1.getColumna("ide_cnccc").setNombreVisual("N. ASIENTO");
        tab_tabla1.getColumna("IDE_CNCCC").setLink();
        tab_tabla1.getColumna("IDE_CNCCC").setMetodoChange("abrirAsiento");
        tab_tabla1.getColumna("IDE_CNCCC").alinearCentro();
        tab_tabla1.setCampoOrden("num_pago_ippin");
        tab_tabla1.setRows(5);
        tab_tabla1.dibujar();

        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_tabla1);
        pat_panel.getMenuTabla().getItem_buscar().setRendered(false);
        pat_panel.getMenuTabla().getItem_insertar().setRendered(false);
        gru.getChildren().add(pat_panel);
        mep_menu.dibujar(4, "PAGO DE INTERESES INVERSIONES CASAS - OBRAS", gru);

    }

    public void dibujarPagoB() {
        Grupo gru = new Grupo();
        aut_inversion = new AutoCompletar();
        aut_inversion.setId("aut_inversion");
        aut_inversion.setAutoCompletar(ser_inversion.getSqlListaInversionesBancarias());
        aut_inversion.setMaxResults(25);
        aut_inversion.setAutocompletarContenido();
        aut_inversion.setMetodoChange("cargarPagosInteres");
        Grid gr = new Grid();
        gr.setColumns(3);
        gr.getChildren().add(new Etiqueta("<strong>INVERSIÓN BANCARIA : </strong>"));
        gr.getChildren().add(aut_inversion);
        Boton bot_clean = new Boton();
        bot_clean.setIcon("ui-icon-cancel");
        bot_clean.setTitle("Limpiar");
        bot_clean.setMetodo("limpiarPagoB");
        gr.getChildren().add(bot_clean);
        gru.getChildren().add(gr);

        tab_tabla2 = new Tabla();
        tab_tabla2.setId("tab_tabla2");
        tab_tabla2.setHeader("NUEVO PAGO DE INTERESES");
        tab_tabla2.setTabla("iyp_pago_interes", "ide_ippin", 4);
        tab_tabla2.setCondicion("ide_ippin=-1");
        tab_tabla2.setTipoFormulario(true);
        tab_tabla2.setValidarInsertar(true);
        tab_tabla2.getColumna("ide_ippin").setVisible(false);
        tab_tabla2.getColumna("ide_ipcer").setVisible(false);
        tab_tabla2.getColumna("fecha_sistema_ippin").setVisible(false);
        tab_tabla2.getColumna("valor_ippin").alinearDerecha();
        tab_tabla2.getColumna("valor_ippin").setRequerida(true);
        tab_tabla2.getColumna("fecha_pago_ippin").setRequerida(true);
        tab_tabla2.getColumna("fecha_pago_ippin").setValorDefecto(utilitario.getFechaActual());
        tab_tabla2.getColumna("observacion_ippin").setRequerida(true);
        tab_tabla2.getColumna("pagado_ippin").setValorDefecto("true");
        tab_tabla2.getColumna("num_pago_ippin").setRequerida(true);
        tab_tabla2.getColumna("capital_ippin").setVisible(false);
        tab_tabla2.getColumna("interes_ippin").setVisible(false);
        tab_tabla2.getColumna("ide_cnccc").setNombreVisual("N. ASIENTO");
        tab_tabla2.getGrid().setColumns(4);
        tab_tabla2.setMostrarNumeroRegistros(false);
        tab_tabla2.dibujar();
        PanelTabla pat_panel2 = new PanelTabla();
        pat_panel2.setPanelTabla(tab_tabla2);
        pat_panel2.getMenuTabla().getItem_buscar().setRendered(false);
        pat_panel2.getMenuTabla().getItem_insertar().setRendered(false);
        pat_panel2.getMenuTabla().getItem_eliminar().setRendered(false);
        pat_panel2.getMenuTabla().getItem_importar().setRendered(false);
        pat_panel2.getMenuTabla().getItem_actualizar().setRendered(false);
        tab_tabla2.insertar();

        gri = new Grid();
        gri.setId("gri");
        // gri.setRendered(false); //solo cuando se inseta se hace visible
        gri.setColumns(2);
        gri.getChildren().add(new Etiqueta("<div style='font-size:12px;font-weight: bold;'> <img src='imagenes/im_pregunta.gif' />  GENERAR NUEVO ASIENTO CONTABLE ? </div>"));
        rad_hace_asiento = new Radio();
        rad_hace_asiento.setRadio(utilitario.getListaPregunta());
        rad_hace_asiento.setValue(true);
        gri.getChildren().add(rad_hace_asiento);

        gru.getChildren().add(pat_panel2);
        Boton bt = new Boton();
        bt.setMetodo("guardar");
        bt.setValue("Aceptar");
        gru.getChildren().add(gri);
        gru.getChildren().add(bt);
        gru.getChildren().add(new Separator());

        tab_tabla1 = new Tabla();
        tab_tabla1.setId("tab_tabla1");
        tab_tabla1.setHeader("DETALLE DE PAGOS INTERESES");
        tab_tabla1.setTabla("iyp_pago_interes", "ide_ippin", 14);
        tab_tabla1.setCondicion("ide_ippin=-1");
        tab_tabla1.setLectura(true);
        tab_tabla1.setColumnaSuma("valor_ippin");
        tab_tabla1.setValidarInsertar(true);
        tab_tabla1.getColumna("ide_ippin").setVisible(false);
        tab_tabla1.getColumna("ide_ipcer").setVisible(false);
        tab_tabla1.getColumna("capital_ippin").setVisible(true);
        tab_tabla1.getColumna("interes_ippin").setVisible(true);
        tab_tabla1.getColumna("fecha_sistema_ippin").setVisible(false);
        tab_tabla1.getColumna("valor_ippin").alinearDerecha();
        tab_tabla1.getColumna("ide_cnccc").setNombreVisual("N. ASIENTO");
        tab_tabla1.getColumna("IDE_CNCCC").setLink();
        tab_tabla1.getColumna("IDE_CNCCC").setMetodoChange("abrirAsiento");
        tab_tabla1.getColumna("IDE_CNCCC").alinearCentro();
        tab_tabla1.setCampoOrden("num_pago_ippin");
        tab_tabla1.setRows(5);
        tab_tabla1.dibujar();

        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_tabla1);
        pat_panel.getMenuTabla().getItem_buscar().setRendered(false);
        pat_panel.getMenuTabla().getItem_insertar().setRendered(false);
        gru.getChildren().add(pat_panel);
        mep_menu.dibujar(4, "PAGO DE INTERESES INVERSIONES BANCARIAS", gru);

    }

    public void limpiarPagoB() {
        aut_inversion.limpiar();
        tab_tabla2.limpiar();
        tab_tabla2.insertar();
        tab_tabla1.limpiar();
        rad_hace_asiento.setValue(true);
        utilitario.addUpdate("gri");
    }

    public void dibujarListadoB() {
        tab_tabla1 = new Tabla();
        tab_tabla1.setId("tab_tabla1");
        tab_tabla1.setSql(ser_inversion.getSqlListaInversionesBancarias());
        tab_tabla1.setLectura(true);
        tab_tabla1.setCampoPrimaria("ide_ipcer");
        tab_tabla1.getColumna("ide_ipcer").setVisible(false);
        tab_tabla1.setColumnaSuma("CAPITAL,INTERES,CAPITAL_MAS_INTERES");
        tab_tabla1.getColumna("CAPITAL").alinearDerecha();
        tab_tabla1.getColumna("INTERES").alinearDerecha();
        tab_tabla1.getColumna("CAPITAL_MAS_INTERES").alinearDerecha();
        tab_tabla1.getColumna("CAPITAL_MAS_INTERES").setLongitud(35);
        tab_tabla1.getColumna("FECHA_VENCIMIENTO").setLongitud(35);
        tab_tabla1.getColumna("FECHA_VENCIMIENTO").setEstilo("font-size:13px;font-weight: bold;");
        tab_tabla1.getColumna("FECHA_VENCIMIENTO").alinearCentro();
        tab_tabla1.getColumna("CAPITAL_MAS_INTERES").setEstilo("font-size:13px;font-weight: bold;");
        tab_tabla1.getColumna("ide_cnccc").setNombreVisual("N. ASIENTO");
        tab_tabla1.getColumna("IDE_CNCCC").setLink();
        tab_tabla1.getColumna("IDE_CNCCC").setMetodoChange("abrirAsiento");
        tab_tabla1.getColumna("IDE_CNCCC").alinearCentro();
        tab_tabla1.getColumna("ide_cnccc_terminacion").setNombreVisual("N. ASIENTO T.");
        tab_tabla1.getColumna("ide_cnccc_terminacion").setLink();
        tab_tabla1.getColumna("ide_cnccc_terminacion").setMetodoChange("abrirAsiento");
        tab_tabla1.getColumna("ide_cnccc_terminacion").alinearCentro();

        tab_tabla1.getColumna("BANCO").setFiltroContenido();
        tab_tabla1.getColumna("NUM_CERTIFICADO").setFiltroContenido();
        tab_tabla1.getColumna("ESTADO").setFiltroContenido();
        tab_tabla1.setRows(15);
        tab_tabla1.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        ItemMenu itemedita = new ItemMenu();
        itemedita.setValue("Modificar");
        itemedita.setIcon("ui-icon-pencil");
        itemedita.setMetodo("abrirModificarB");
        pat_panel.getMenuTabla().getChildren().add(itemedita);
        ItemMenu itemcancela = new ItemMenu();
        itemcancela.setValue("Cancelar Inversión");
        itemcancela.setIcon("ui-icon-check");
        itemcancela.setMetodo("cancelarInversion");
        pat_panel.getMenuTabla().getChildren().add(itemcancela);

        ItemMenu itemanula = new ItemMenu();
        itemanula.setValue("Anular Inversión");
        itemanula.setIcon("ui-icon-close");
        itemanula.setMetodo("abrirAnularIversion");
        pat_panel.getMenuTabla().getChildren().add(itemanula);

        pat_panel.setPanelTabla(tab_tabla1);
        pat_panel.getMenuTabla().getItem_buscar().setRendered(false);
        mep_menu.dibujar(2, "LISTADO DE INVERSIONES BANCARIAS", pat_panel);

    }

    public void cargarPagosInteres(SelectEvent evt) {
        aut_inversion.onSelect(evt);
        if (aut_inversion.getValor() != null) {
            tab_tabla1.setCondicion("ide_ipcer=" + aut_inversion.getValor());
            tab_tabla1.ejecutarSql();
            //gri.setRendered(false);
            utilitario.addUpdate("gri");
            tab_tabla2.setValor("valor_ippin", aut_inversion.getValorArreglo(5));
            tab_tabla2.setValor("num_pago_ippin", String.valueOf(tab_tabla1.getTotalFilas() + 1));

            tab_tabla2.setValor("capital_ippin", aut_inversion.getValorArreglo(5));
            tab_tabla2.setValor("interes_ippin", aut_inversion.getValorArreglo(6));

            utilitario.addUpdateTabla(tab_tabla2, "valor_ippin,num_pago_ippin", null);
        } else {
            tab_tabla1.limpiar();
            utilitario.agregarMensajeInfo("Seleccione una inversión Bancaria", "");
        }
    }

    public void dibujarCertificadoB() {
        tab_tabla1 = new Tabla();
        tab_tabla1.setId("tab_tabla1");
        tab_tabla1.setTabla("iyp_certificado", "ide_ipcer", 1);
        tab_tabla1.setCondicion("ide_ipcer=-1");
        //oculta todas las columnas        
        for (int i = 0; i < tab_tabla1.getTotalColumnas(); i++) {
            tab_tabla1.getColumnas()[i].setVisible(false);
        }
        tab_tabla1.getColumna("num_certificado_ipcer").setVisible(true);
        tab_tabla1.getColumna("ide_cnccc_terminacion").setVisible(true);
        tab_tabla1.getColumna("ide_tecba").setVisible(true);
        tab_tabla1.getColumna("ide_tecba").setCombo("select tes_cuenta_banco.ide_tecba,tes_banco.nombre_teban,tes_cuenta_banco.nombre_tecba from  tes_banco,tes_cuenta_banco,sis_empresa where tes_banco.ide_teban=tes_cuenta_banco.ide_teban and sis_empresa.ide_empr=" + utilitario.getVariable("ide_empr") + " and es_caja_teban=false and tes_cuenta_banco.ide_sucu=" + utilitario.getVariable("ide_sucu"));
        tab_tabla1.getColumna("ide_teban").setCombo(ser_inversion.getSqlComboBancos());
        tab_tabla1.getColumna("ide_teban").setNombreVisual("BANCO");
        tab_tabla1.getColumna("ide_teban").setRequerida(true);
        tab_tabla1.getColumna("ide_teban").setVisible(true);
        tab_tabla1.getColumna("ide_tecba").setRequerida(false); //CAMBIA A BANCO         
        tab_tabla1.getColumna("fecha_emision_ipcer").setVisible(true);
        tab_tabla1.getColumna("fecha_vence_ipcer").setVisible(true);
        tab_tabla1.getColumna("tasa_ipcer").setVisible(true);
        tab_tabla1.getColumna("plazo_ipcer").setVisible(true);
        tab_tabla1.getColumna("capital_ipcer").setVisible(true);
        tab_tabla1.getColumna("interes_ipcer").setVisible(true);
        tab_tabla1.getColumna("valor_a_pagar_ipcer").setVisible(true);
        tab_tabla1.getColumna("observacion_ipcer").setVisible(true);
        tab_tabla1.getColumna("fecha_sistema_ipcer").setValorDefecto(utilitario.getFechaActual());
        tab_tabla1.getColumna("activo_ipcer").setValorDefecto("true");
        tab_tabla1.getColumna("ide_cnmod").setVisible(true);
        tab_tabla1.getColumna("ide_cnccc").setVisible(true);
        tab_tabla1.getColumna("ide_cnmod").setCombo("select ide_cnmod,nombre_cnmod from con_moneda where ide_empr=" + utilitario.getVariable("ide_empr") + "  order by ide_cnmod ");
        tab_tabla1.getColumna("ide_cnmod").setPermitirNullCombo(false);
        tab_tabla1.getColumna("capital_ipcer").setMetodoChange("calcularInteres");
        tab_tabla1.getColumna("tasa_ipcer").setMetodoChange("calcularInteres");
        tab_tabla1.getColumna("plazo_ipcer").setMetodoChange("calcularInteres");
        tab_tabla1.getColumna("interes_ipcer").setEstilo("font-size:13px;font-weight: bold;");
        tab_tabla1.getColumna("valor_a_pagar_ipcer").setEstilo("font-size:12px;font-weight: bold;");
        tab_tabla1.getColumna("fecha_emision_ipcer").setMetodoChange("obtenetFechaVencimiento");
        tab_tabla1.getColumna("fecha_vence_ipcer").setLectura(true);
        tab_tabla1.getColumna("ide_iptin").setValorDefecto("0"); //BANCOS
        tab_tabla1.getColumna("es_inver_banco_ipcer").setValorDefecto("true"); //BANCOS
        tab_tabla1.getColumna("ide_geper").setValorDefecto("1294"); //SOCIEDAD SALESIANA EN EL ECUADOR        
        tab_tabla1.getColumna("ide_ipcin").setValorDefecto("1");
        tab_tabla1.getColumna("ide_usua").setValorDefecto(utilitario.getVariable("ide_usua"));
        tab_tabla1.getColumna("nuevo").setValorDefecto("true");
        tab_tabla1.getColumna("cancelado").setVisible(true);
        tab_tabla1.getColumna("ide_ipein").setVisible(true);
        tab_tabla1.getColumna("ide_ipein").setCombo("iyp_estado_inversion", "ide_ipein", "nombre_ipein", "");
        tab_tabla1.getColumna("ide_ipein").setMetodoChange("cambioEstado");
        tab_tabla1.setTipoFormulario(true);
        tab_tabla1.getGrid().setColumns(4);
        tab_tabla1.setMostrarNumeroRegistros(false);
        tab_tabla1.dibujar();
        if (tab_tabla1.isEmpty()) {
            tab_tabla1.insertar();
        }
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_tabla1);
        pat_panel.getMenuTabla().getItem_buscar().setRendered(false);
        pat_panel.getMenuTabla().getItem_eliminar().setRendered(false);
        pat_panel.getMenuTabla().getItem_actualizar().setRendered(false);
        pat_panel.getMenuTabla().getItem_importar().setRendered(false);

        Grid gri1 = new Grid();
        gri1.setColumns(2);
        gri1.getChildren().add(new Etiqueta("<div style='font-size:12px;font-weight: bold;'> <img src='imagenes/im_pregunta.gif' />  GENERAR NUEVO ASIENTO CONTABLE ? </div>"));
        rad_hace_asiento = new Radio();
        rad_hace_asiento.setRadio(utilitario.getListaPregunta());
        rad_hace_asiento.setValue(true);
        gri1.getChildren().add(rad_hace_asiento);
        pat_panel.getChildren().add(gri1);
        if (ide_teban.equals("-1")) {
            mep_menu.dibujar(1, "CERTIFICADO DE INVERSIÓN BANCARIA", pat_panel);
        } else {
            tab_tabla1.setValor("iyp_ide_ipcer", iyp_ide_ipcer);
            tab_tabla1.setValor("ide_teban", ide_teban);
            tab_tabla1.setValor("es_renovacion_ipcer", "true");
            mep_menu.dibujar(1, "RENOVACIÓN INVERSIÓN BANCARIA", pat_panel);
        }

    }

    public void abrirModificarB() {
        String ide_aux = tab_tabla1.getValor("ide_ipcer");
        if (ide_aux != null) {
            tab_tabla1 = new Tabla();
            tab_tabla1.setId("tab_tabla1");
            tab_tabla1.setTabla("iyp_certificado", "ide_ipcer", 1);
            tab_tabla1.setCondicion("ide_ipcer=" + ide_aux);
            //oculta todas las columnas        
            for (int i = 0; i < tab_tabla1.getTotalColumnas(); i++) {
                tab_tabla1.getColumnas()[i].setVisible(false);
            }
            tab_tabla1.getColumna("num_certificado_ipcer").setVisible(true);
            tab_tabla1.getColumna("ide_tecba").setVisible(true);
            tab_tabla1.getColumna("ide_tecba").setCombo("select tes_cuenta_banco.ide_tecba,tes_banco.nombre_teban,tes_cuenta_banco.nombre_tecba from  tes_banco,tes_cuenta_banco,sis_empresa where tes_banco.ide_teban=tes_cuenta_banco.ide_teban and sis_empresa.ide_empr=" + utilitario.getVariable("ide_empr") + " and es_caja_teban=false and tes_cuenta_banco.ide_sucu=" + utilitario.getVariable("ide_sucu"));
            tab_tabla1.getColumna("ide_tecba").setRequerida(true);
            tab_tabla1.getColumna("ide_teban").setVisible(true);
            tab_tabla1.getColumna("cancelado").setVisible(true);
            tab_tabla1.getColumna("ide_teban").setCombo(ser_inversion.getSqlComboBancos());
            tab_tabla1.getColumna("ide_teban").setNombreVisual("BANCO");
            tab_tabla1.getColumna("fecha_emision_ipcer").setVisible(true);
            tab_tabla1.getColumna("fecha_vence_ipcer").setVisible(true);
            tab_tabla1.getColumna("tasa_ipcer").setVisible(true);
            tab_tabla1.getColumna("plazo_ipcer").setVisible(true);
            tab_tabla1.getColumna("capital_ipcer").setVisible(true);
            tab_tabla1.getColumna("interes_ipcer").setVisible(true);
            tab_tabla1.getColumna("valor_a_pagar_ipcer").setVisible(true);
            tab_tabla1.getColumna("observacion_ipcer").setVisible(true);
            tab_tabla1.getColumna("fecha_sistema_ipcer").setValorDefecto(utilitario.getFechaActual());
            tab_tabla1.getColumna("activo_ipcer").setValorDefecto("true");
            tab_tabla1.getColumna("ide_cnmod").setVisible(true);
            tab_tabla1.getColumna("ide_cnccc").setVisible(true);
            tab_tabla1.getColumna("ide_cnmod").setCombo("select ide_cnmod,nombre_cnmod from con_moneda where ide_empr=" + utilitario.getVariable("ide_empr") + "  order by ide_cnmod ");
            tab_tabla1.getColumna("ide_cnmod").setPermitirNullCombo(false);
            tab_tabla1.getColumna("capital_ipcer").setMetodoChange("calcularInteres");
            tab_tabla1.getColumna("tasa_ipcer").setMetodoChange("calcularInteres");
            tab_tabla1.getColumna("plazo_ipcer").setMetodoChange("calcularInteres");
            tab_tabla1.getColumna("interes_ipcer").setEstilo("font-size:13px;font-weight: bold;");
            tab_tabla1.getColumna("valor_a_pagar_ipcer").setEstilo("font-size:12px;font-weight: bold;");
            tab_tabla1.getColumna("fecha_emision_ipcer").setMetodoChange("obtenetFechaVencimiento");
            tab_tabla1.getColumna("fecha_vence_ipcer").setLectura(true);
            tab_tabla1.getColumna("ide_iptin").setValorDefecto("0"); //BANCOS
            tab_tabla1.getColumna("es_inver_banco_ipcer").setValorDefecto("true"); //BANCOS
            tab_tabla1.getColumna("ide_geper").setValorDefecto("1294"); //SOCIEDAD SALESIANA EN EL ECUADOR
            tab_tabla1.getColumna("ide_ipein").setValorDefecto("0"); //NO CANCELADO
            tab_tabla1.getColumna("ide_ipcin").setValorDefecto("1");
            tab_tabla1.getColumna("ide_usua").setValorDefecto(utilitario.getVariable("ide_usua"));
            tab_tabla1.getColumna("nuevo").setValorDefecto("true");
            tab_tabla1.setTipoFormulario(true);
            tab_tabla1.getGrid().setColumns(4);
            tab_tabla1.setMostrarNumeroRegistros(false);
            tab_tabla1.dibujar();
            if (tab_tabla1.isEmpty()) {
                tab_tabla1.insertar();
            }
            PanelTabla pat_panel = new PanelTabla();
            pat_panel.setPanelTabla(tab_tabla1);
            pat_panel.getMenuTabla().getItem_buscar().setRendered(false);
            pat_panel.getMenuTabla().getItem_eliminar().setRendered(false);
            pat_panel.getMenuTabla().getItem_actualizar().setRendered(false);
            pat_panel.getMenuTabla().getItem_importar().setRendered(false);

            mep_menu.dibujar(3, "MODIFICAR CERTIFICADO DE INVERSIÓN BANCARIA", pat_panel);
        } else {
            utilitario.agregarMensajeInfo("Seleccione un certificado", "");
        }

    }

    public boolean obtenetFechaVencimiento() {
        try {
            if (mep_menu.getOpcion() == 1 || mep_menu.getOpcion() == 3 || mep_menu.getOpcion() == 6 || mep_menu.getOpcion() == 9 || mep_menu.getOpcion() == 10 || mep_menu.getOpcion() == 11) {
                if (tab_tabla1.getValor("plazo_ipcer") != null && !tab_tabla1.getValor("plazo_ipcer").isEmpty()) {
                    tab_tabla1.setValor("fecha_vence_ipcer", utilitario.getFormatoFecha(utilitario.sumarDiasFecha(utilitario.getFecha(tab_tabla1.getValor("fecha_emision_ipcer")), Integer.parseInt(tab_tabla1.getValor("plazo_ipcer")))));
                    utilitario.addUpdateTabla(tab_tabla1, "fecha_vence_ipcer", "");
                    return true;
                } else {
                    tab_tabla1.setValor("fecha_vence_ipcer", "");
                    utilitario.addUpdateTabla(tab_tabla1, "fecha_vence_ipcer", "");
                    return false;
                }
            }
        } catch (Exception e) {
        }
        return false;
    }

    public void obtenetFechaVencimiento(DateSelectEvent evt) {
//        tab_renovacion_inversion.modificar(evt);
        if (obtenetFechaVencimiento()) {
            calculainteres();
        }
    }

    public void calculainteres() {
        double capital = 0;
        double tasa = 0;
        double plazo = 0;
        double interes = 0;
        double valortotal = 0;
        if (tab_tabla1.getValor("capital_ipcer") != null && !tab_tabla1.getValor("capital_ipcer").isEmpty()) {
            try {
                capital = Double.parseDouble(tab_tabla1.getValor(tab_tabla1.getFilaActual(), "capital_ipcer"));
            } catch (Exception e) {
                capital = 0;
            }
        }
        if (tab_tabla1.getValor("tasa_ipcer") != null && !tab_tabla1.getValor("tasa_ipcer").isEmpty()) {
            try {
                tasa = Double.parseDouble(tab_tabla1.getValor("tasa_ipcer"));

            } catch (Exception e) {
                tasa = 0;
            }
        }
        if (tab_tabla1.getValor("plazo_ipcer") != null && !tab_tabla1.getValor("plazo_ipcer").isEmpty()) {
            plazo = Double.parseDouble(tab_tabla1.getValor("plazo_ipcer"));
            obtenetFechaVencimiento();
        } else {
            plazo = 0;
            obtenetFechaVencimiento();
        }
        interes = (capital * tasa * plazo) / 36000;
        valortotal = capital + interes;
        tab_tabla1.setValor("interes_ipcer", utilitario.getFormatoNumero(interes));
        tab_tabla1.setValor("valor_a_pagar_ipcer", utilitario.getFormatoNumero(valortotal));
        utilitario.addUpdateTabla(tab_tabla1, "interes_ipcer,valor_a_pagar_ipcer", "");
    }

    public void calcularInteres(AjaxBehaviorEvent evt) {
        tab_tabla1.modificar(evt);
        calculainteres();
    }

    @Override
    public void insertar() {

    }

    @Override
    public void guardar() {
        if (mep_menu.getOpcion() == 1) {
            if (validarCertificado()) {
                if (String.valueOf(rad_hace_asiento.getValue()).equals("true")) {
                    asc_asiento.nuevoAsiento();
                    asc_asiento.dibujar();
                    asc_asiento.getTab_cabe_asiento().setValor("ide_geper", tab_tabla1.getValor("ide_geper"));
                    asc_asiento.getTab_cabe_asiento().setValor("fecha_trans_cnccc", tab_tabla1.getValor("fecha_emision_ipcer"));
                    asc_asiento.getTab_cabe_asiento().setValor("observacion_cnccc", tab_tabla1.getValor("observacion_ipcer"));
                    asc_asiento.getBot_aceptar().setMetodo("aceptarCrearCertificadoB");
                } else {
                    tab_tabla1.setValor("hora_sistema_ipcer", utilitario.getHoraActual());
                    if (tab_tabla1.guardar()) {
                        guardarPantalla();
                        dibujarListadoB();
                    }
                }
            }
        } else if (mep_menu.getOpcion() == 3) {  //MODIFICAR CERT BANCO
            if (tab_tabla1.guardar()) {
                guardarPantalla();
                dibujarListadoB();
            }
        } else if (mep_menu.getOpcion() == 4) {  //PAGO INTERESES
            if (aut_inversion.getValor() != null) {
                tab_tabla2.setValor("fecha_sistema_ippin", utilitario.getFechaActual());
                tab_tabla2.setValor("ide_ipcer", aut_inversion.getValor());
            } else {
                utilitario.agregarMensajeInfo("Seleccione una inversión Bancaria", "");
                return;
            }
            if (validarPagoInteres()) {
                if (String.valueOf(rad_hace_asiento.getValue()).equals("true")) {
                    asc_asiento.nuevoAsiento();
                    asc_asiento.dibujar();
                    asc_asiento.getTab_cabe_asiento().setValor("fecha_trans_cnccc", tab_tabla2.getValor("fecha_pago_ippin"));
                    asc_asiento.getTab_cabe_asiento().setValor("observacion_cnccc", tab_tabla2.getValor("observacion_ippin"));

                    asc_asiento.getBot_aceptar().setMetodo("aceptarPagoInteresB");
                } else {
                    if (tab_tabla2.guardar()) {
                        guardarPantalla();
                        tab_tabla1.actualizar();
                        tab_tabla2.limpiar();
                        tab_tabla2.insertar();
                    }
                }
            }
        } else if (mep_menu.getOpcion() == 6) { //NUEVO CERT CASAS
            if (validarCertificado()) {
                if (String.valueOf(rad_hace_asiento.getValue()).equals("true")) {
                    asc_asiento.nuevoAsiento();
                    asc_asiento.dibujar();
                    asc_asiento.getTab_cabe_asiento().setValor("ide_geper", tab_tabla1.getValor("ide_geper"));
                    asc_asiento.getTab_cabe_asiento().setValor("fecha_trans_cnccc", tab_tabla1.getValor("fecha_emision_ipcer"));
                    asc_asiento.getTab_cabe_asiento().setValor("observacion_cnccc", tab_tabla1.getValor("observacion_ipcer"));
                    asc_asiento.getBot_aceptar().setMetodo("aceptarCrearCertificadoCasas");
                } else {
                    if (tab_tabla2.guardar()) {
                        tab_tabla1.setValor("hora_sistema_ipcer", utilitario.getHoraActual());
                        tab_tabla1.setValor("ide_ipcai", tab_tabla2.getValor("ide_ipcai"));
                        if (tab_tabla1.guardar()) {
                            guardarPantalla();
                            dibujarListadoCasas();
                        }
                    }
                }
            }
        }
        if (mep_menu.getOpcion() == 9) {   //certificado fondo
            if (validarCertificado()) {
                if (String.valueOf(rad_hace_asiento.getValue()).equals("true")) {
                    asc_asiento.nuevoAsiento();
                    asc_asiento.dibujar();
                    asc_asiento.getTab_cabe_asiento().setValor("ide_geper", tab_tabla1.getValor("ide_geper"));
                    asc_asiento.getTab_cabe_asiento().setValor("fecha_trans_cnccc", tab_tabla1.getValor("fecha_emision_ipcer"));
                    asc_asiento.getTab_cabe_asiento().setValor("observacion_cnccc", tab_tabla1.getValor("observacion_ipcer"));
                    asc_asiento.getBot_aceptar().setMetodo("aceptarCrearCertificadoFondo");
                } else {
                    tab_tabla1.setValor("hora_sistema_ipcer", utilitario.getHoraActual());
                    if (tab_tabla1.guardar()) {
                        guardarPantalla();
                        dibujarListadoFondo();
                    }
                }
            }
        } else if (mep_menu.getOpcion() == 10) {  //MODIFICAR CERT FONDO
            if (tab_tabla1.guardar()) {
                guardarPantalla();
                dibujarListadoFondo();
            }
        } else if (mep_menu.getOpcion() == 11) {
            if (tab_tabla2.guardar()) {
                if (tab_tabla1.guardar()) {
                    guardarPantalla();
                    dibujarListadoCasas();
                }
            }
        }
    }

    public void aceptarPagoInteresB() {
        if (asc_asiento.isVisible()) {
            asc_asiento.guardar();
            if (asc_asiento.isVisible() == false) {
                tab_tabla2.setValor("ide_cnccc", asc_asiento.getIde_cnccc());
                tab_tabla2.guardar();
                guardarPantalla();
                tab_tabla1.actualizar();
                tab_tabla2.limpiar();
                tab_tabla2.insertar();
            }
        }
    }

    public void aceptarCrearCertificadoFondo() {
        if (asc_asiento.isVisible()) {
            asc_asiento.guardar();
            if (asc_asiento.isVisible() == false) {
                tab_tabla1.setValor("ide_cnccc", asc_asiento.getIde_cnccc());
                tab_tabla1.setValor("hora_sistema_ipcer", utilitario.getHoraActual());
                tab_tabla1.guardar();
                guardarPantalla();
                dibujarListadoFondo();
            }
        }
    }

    public void aceptarCrearCertificadoCasas() {
        if (asc_asiento.isVisible()) {
            asc_asiento.guardar();
            if (asc_asiento.isVisible() == false) {
                if (tab_tabla2.guardar()) {
                    tab_tabla1.setValor("ide_cnccc", asc_asiento.getIde_cnccc());
                    tab_tabla1.setValor("hora_sistema_ipcer", utilitario.getHoraActual());
                    tab_tabla1.setValor("ide_ipcai", tab_tabla2.getValor("ide_ipcai"));
                    tab_tabla1.guardar();
                    guardarPantalla();
                    dibujarListadoCasas();
                }
            }
        }
    }

    public void aceptarCrearCertificadoB() {

        if (asc_asiento.isVisible()) {
            asc_asiento.guardar();
            if (asc_asiento.isVisible() == false) {
                tab_tabla1.setValor("ide_cnccc", asc_asiento.getIde_cnccc());
                tab_tabla1.setValor("hora_sistema_ipcer", utilitario.getHoraActual());
                tab_tabla1.guardar();
                guardarPantalla();
                dibujarListadoB();

            }
        }
    }

    public boolean validarPagoInteres() {
        if (tab_tabla2.getValor("fecha_pago_ippin") == null || tab_tabla2.getValor("fecha_pago_ippin").isEmpty()) {
            utilitario.agregarMensajeError("No se pudo guardar", "Debe seleccionar la fecha de pago");
            return false;
        }
        if (tab_tabla2.getValor("valor_ippin") == null || tab_tabla2.getValor("valor_ippin").isEmpty()) {
            utilitario.agregarMensajeError("No se pudo guardar", "Debe ingresar el valor a pagar");
            return false;
        }
        if (tab_tabla2.getValor("num_pago_ippin") == null || tab_tabla2.getValor("num_pago_ippin").isEmpty()) {
            utilitario.agregarMensajeError("No se pudo guardar", "Debe ingresar el número de pago");
            return false;
        }
        if (tab_tabla2.getValor("observacion_ippin") == null || tab_tabla2.getValor("observacion_ippin").isEmpty()) {
            utilitario.agregarMensajeError("No se pudo guardar", "Debe ingresar una observación");
            return false;
        }

        return true;
    }

    public boolean validarCertificado() {
        if (tab_tabla1.getValor("capital_ipcer") == null || tab_tabla1.getValor("capital_ipcer").isEmpty()) {
            utilitario.agregarMensajeError("No se pudo guardar", "Debe ingresar el capital");
            return false;
        }
        if (tab_tabla1.getValor("plazo_ipcer") == null || tab_tabla1.getValor("plazo_ipcer").isEmpty()) {
            utilitario.agregarMensajeError("No se pudo guardar", "Debe ingresar el plazo");
            return false;
        }

        if (tab_tabla1.getValor("tasa_ipcer") == null || tab_tabla1.getValor("tasa_ipcer").isEmpty()) {
            utilitario.agregarMensajeError("No se pudo guardar", "Debe ingresar la tasa de interes");
            return false;
        }

        if (tab_tabla1.getValor("fecha_emision_ipcer") == null || tab_tabla1.getValor("fecha_emision_ipcer").isEmpty()) {
            utilitario.agregarMensajeError("No se pudo guardar", "Debe ingresar la fecha de emisión");
            return false;
        }
        if (tab_tabla1.getValor("observacion_ipcer") == null || tab_tabla1.getValor("observacion_ipcer").isEmpty()) {
            utilitario.agregarMensajeError("No se pudo guardar", "Debe ingresar la observación");
            return false;
        }
        if (tab_tabla1.getValor("ide_geper") == null || tab_tabla1.getValor("ide_geper").isEmpty()) {
            utilitario.agregarMensajeError("No se pudo guardar", "Debe seleccionar el Beneficiario");
            return false;
        }
        if (tab_tabla1.getValor("ide_iptin") == null || tab_tabla1.getValor("ide_iptin").isEmpty()) {
            utilitario.agregarMensajeError("No se pudo guardar", "Debe seleccionar el Tipo de Inversión");
            return false;
        }
        if (tab_tabla1.getValor("ide_cnmod") == null || tab_tabla1.getValor("ide_cnmod").isEmpty()) {
            utilitario.agregarMensajeError("No se pudo guardar", "Debe seleccionar la Moneda");
            return false;
        }
        if (tab_tabla1.getValor("ide_ipein") == null || tab_tabla1.getValor("ide_ipein").isEmpty()) {
            utilitario.agregarMensajeError("No se pudo guardar", "Debe seleccionar el estado");
            return false;
        }
        if (tab_tabla1.getValor("ide_ipcin") == null || tab_tabla1.getValor("ide_ipcin").isEmpty()) {
            utilitario.agregarMensajeError("No se pudo guardar", "Debe seleccionar la Clase de Inversión");
            return false;
        }

        if (mep_menu.getOpcion() == 6) {
            //Valida cabecera de prestamo
            if (tab_tabla2.getValor("ide_geper") == null || tab_tabla2.getValor("ide_geper").isEmpty()) {
                utilitario.agregarMensajeError("No se pudo guardar", "Debe seleccionar el" + tab_tabla2.getColumna("ide_geper").getNombreVisual());
                return false;
            }
            if (tab_tabla2.getValor("ide_geper_ben") == null || tab_tabla2.getValor("ide_geper_ben").isEmpty()) {
                utilitario.agregarMensajeError("No se pudo guardar", "Debe seleccionar el" + tab_tabla2.getColumna("ide_geper_ben").getNombreVisual());
                return false;
            }
            if (tab_tabla2.getValor("observacion_ipcai") == null || tab_tabla2.getValor("observacion_ipcai").isEmpty()) {
                utilitario.agregarMensajeError("No se pudo guardar", "Debe ingresar la observación");
                return false;
            }
        }

        return true;
    }

    /**
     * Abre el asiento contable seleccionado
     *
     * @param evt
     */
    public void abrirAsiento(ActionEvent evt) {
        Link lin_ide_cnccc = (Link) evt.getComponent();
        asc_asiento.setAsientoContable(lin_ide_cnccc.getValue().toString());
        tab_tabla1.setFilaActual(lin_ide_cnccc.getDir());
        asc_asiento.dibujar();
    }

    @Override
    public void eliminar() {

    }

    public Tabla getTab_tabla1() {
        return tab_tabla1;
    }

    public void setTab_tabla1(Tabla tab_tabla1) {
        this.tab_tabla1 = tab_tabla1;
    }

    public AsientoContable getAsc_asiento() {
        return asc_asiento;
    }

    public void setAsc_asiento(AsientoContable asc_asiento) {
        this.asc_asiento = asc_asiento;
    }

    public AutoCompletar getAut_inversion() {
        return aut_inversion;
    }

    public void setAut_inversion(AutoCompletar aut_inversion) {
        this.aut_inversion = aut_inversion;
    }

    public Tabla getTab_tabla2() {
        return tab_tabla2;
    }

    public void setTab_tabla2(Tabla tab_tabla2) {
        this.tab_tabla2 = tab_tabla2;
    }

    public Reporte getRep_reporte() {
        return rep_reporte;
    }

    public void setRep_reporte(Reporte rep_reporte) {
        this.rep_reporte = rep_reporte;
    }

    public SeleccionFormatoReporte getSel_formato() {
        return sel_formato;
    }

    public void setSel_formato(SeleccionFormatoReporte sel_formato) {
        this.sel_formato = sel_formato;
    }

    public Confirmar getCon_confirma_anular() {
        return con_confirma_anular;
    }

    public void setCon_confirma_anular(Confirmar con_confirma_anular) {
        this.con_confirma_anular = con_confirma_anular;
    }

}
