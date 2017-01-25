/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_cuentas_x_pagar;

import componentes.AsientoContable;
import componentes.DocumentoCxP;
import componentes.Retencion;
import framework.aplicacion.Fila;
import framework.componentes.AutoCompletar;
import framework.componentes.Barra;
import framework.componentes.Boton;
import framework.componentes.Calendario;
import framework.componentes.Combo;
import framework.componentes.Confirmar;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.Grupo;
import framework.componentes.Link;
import framework.componentes.MenuPanel;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.Tabla;
import framework.componentes.graficos.GraficoCartesiano;
import framework.componentes.graficos.GraficoPastel;
import java.util.HashMap;
import java.util.Map;
import javax.ejb.EJB;
import javax.faces.event.ActionEvent;
import org.primefaces.event.SelectEvent;
import servicios.cuentas_x_pagar.ServicioCuentasCxP;
import servicios.cuentas_x_pagar.ServicioProveedor;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author DIEGOFERNANDOJACOMEG
 */
public class pre_documentosCxP extends Pantalla {

    private final MenuPanel mep_menu = new MenuPanel();
    private final Combo com_tipo_documento = new Combo();
    private final Calendario cal_fecha_inicio = new Calendario();
    private final Calendario cal_fecha_fin = new Calendario();
    private DocumentoCxP dcp_documento = new DocumentoCxP();
    @EJB
    private final ServicioCuentasCxP ser_cuentas_cxp = (ServicioCuentasCxP) utilitario.instanciarEJB(ServicioCuentasCxP.class);
    @EJB
    private final ServicioProveedor ser_proveedor = (ServicioProveedor) utilitario.instanciarEJB(ServicioProveedor.class);

    private Tabla tab_tabla1 = new Tabla();
    private Combo com_periodo;
    private GraficoCartesiano gca_facturas;
    private GraficoPastel gpa_facturas;

    private Retencion ret_retencion = new Retencion();

    private Reporte rep_reporte = new Reporte();
    private SeleccionFormatoReporte sel_rep = new SeleccionFormatoReporte();

    private AsientoContable asc_asiento = new AsientoContable();

    private Confirmar con_confirmar = new Confirmar();
    private AutoCompletar aut_proveedor;
    private Tabla tab_tabla2;
    private Etiqueta eti1 = new Etiqueta();

    public pre_documentosCxP() {
        bar_botones.quitarBotonsNavegacion();
        bar_botones.quitarBotonGuardar();
        bar_botones.quitarBotonEliminar();
        bar_botones.agregarReporte();

        com_tipo_documento.setCombo(ser_cuentas_cxp.getSqlTipoDocumentosCxP());
        com_tipo_documento.setMetodo("actualizarFiltros");

        bar_botones.agregarComponente(new Etiqueta("TIPO DE DOCUMENTO :"));
        bar_botones.agregarComponente(com_tipo_documento);

        bar_botones.agregarComponente(new Etiqueta("FECHA DESDE :"));
        cal_fecha_inicio.setValue(utilitario.getFecha(utilitario.getAnio(utilitario.getFechaActual()) + "-01-01"));
        bar_botones.agregarComponente(cal_fecha_inicio);

        bar_botones.agregarComponente(new Etiqueta("FECHA HASTA :"));
        cal_fecha_fin.setFechaActual();
        bar_botones.agregarComponente(cal_fecha_fin);

        Boton bot_consultar = new Boton();
        bot_consultar.setTitle("Buscar");
        bot_consultar.setMetodo("actualizarFiltros");
        bot_consultar.setIcon("ui-icon-search");
        bar_botones.agregarComponente(bot_consultar);

        dcp_documento.setId("dcp_documento");
        dcp_documento.setDocumentoCxP("INGRESAR DOCUMENTO POR PAGAR");
        dcp_documento.getBot_aceptar().setMetodo("guardar");
        agregarComponente(dcp_documento);

        mep_menu.setMenuPanel("OPCIONES DOCUMENTOS POR PAGAR", "20%");
        mep_menu.agregarItem("Listado de Documentos CxP ", "dibujarDocumentos", "ui-icon-note");
        mep_menu.agregarItem("Generar Asiento Contable", "dibujarDocumentosNoContabilizadas", "ui-icon-notice");
        mep_menu.agregarItem("Generar Comprobante Retención", "dibujarDocumentosNoRetencion", "ui-icon-notice");
        mep_menu.agregarItem("Documentos Anulados", "dibujarDocumentosAnulados", "ui-icon-cancel");
        mep_menu.agregarItem("Documentos Por Pagar", "dibujarDocumentosPorPagar", "ui-icon-calculator");
        mep_menu.agregarSubMenu("INFORMES");
        mep_menu.agregarItem("Grafico de Compras", "dibujarGraficoCompras", "ui-icon-clock");
        // mep_menu.agregarItem("Estadística de Ventas", "dibujarEstadisticas", "ui-icon-bookmark");        
        mep_menu.agregarSubMenu("PRESUPUESTO");
        mep_menu.agregarItem("Compromiso Documentos CxP", "dibujarCompromiso", "ui-icon-calculator");
        mep_menu.agregarSubMenu("FACTURAS ELECTRONICAS");
        mep_menu.agregarItem("Seleccionar Factura XML", "dibujarFacturaElectronica", "ui-icon-signal-diag");
        agregarComponente(mep_menu);
        dibujarDocumentos();

        ret_retencion.setId("ret_retencion");
        ret_retencion.getBot_aceptar().setMetodo("guardar");
        agregarComponente(ret_retencion);

        rep_reporte.setId("rep_reporte");
        rep_reporte.getBot_aceptar().setMetodo("aceptarReporte");
        sel_rep.setId("sel_rep");
        agregarComponente(rep_reporte);
        agregarComponente(sel_rep);

        asc_asiento.setId("asc_asiento");
        asc_asiento.getBot_aceptar().setMetodo("guardar");
        agregarComponente(asc_asiento);

        con_confirmar.setId("con_confirmar");
        con_confirmar.setWidgetVar("con_confirmar");
        con_confirmar.setMessage("Desea Generar el Comprobante de Retención?");
        con_confirmar.getBot_aceptar().setOncomplete("con_confirmar.hide();");
        con_confirmar.getBot_aceptar().setMetodo("abrirRetencion");
        con_confirmar.getBot_cancelar().setOnclick("con_confirmar.hide();");
        agregarComponente(con_confirmar);
    }

    public void dibujarCompromiso() {
        Grid gri = new Grid();
        Grid g1 = new Grid();
        g1.setColumns(2);
        g1.getChildren().add(new Etiqueta("<strong>PROVEEDOR : </strong>"));
        aut_proveedor = new AutoCompletar();
        aut_proveedor.setId("aut_proveedor");
        aut_proveedor.setAutoCompletar(ser_proveedor.getSqlComboProveedor());
        aut_proveedor.setAutocompletarContenido();
        aut_proveedor.setMetodoChange("cargarDocumetosProveedor");
        aut_proveedor.setSize(70);
        g1.getChildren().add(aut_proveedor);
        gri.getChildren().add(g1);

        tab_tabla1 = new Tabla();
        tab_tabla1.setId("tab_tabla1");
        tab_tabla1.setSql(ser_cuentas_cxp.getSqlDocumentosProveedor(cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha(), "-1"));
        tab_tabla1.setCampoPrimaria("ide_cpcfa");
        tab_tabla1.getColumna("ide_cpcfa").setVisible(false);
        tab_tabla1.getColumna("ide_cpefa").setVisible(false);
        tab_tabla1.getColumna("ide_cncre").setVisible(false);
        tab_tabla1.getColumna("nombre_cpefa").setVisible(false);
        tab_tabla1.getColumna("numero_cpcfa").setFiltroContenido();
        tab_tabla1.getColumna("ide_cnccc").setFiltroContenido();
        tab_tabla1.getColumna("ide_cnccc").setNombreVisual("N. ASIENTO");
        tab_tabla1.getColumna("IDE_CNCCC").setLink();
        tab_tabla1.getColumna("IDE_CNCCC").setMetodoChange("abrirAsiento");
        tab_tabla1.getColumna("IDE_CNCCC").alinearCentro();
        tab_tabla1.getColumna("ventas0").alinearDerecha();
        tab_tabla1.getColumna("ventas12").alinearDerecha();
        tab_tabla1.getColumna("valor_iva_cpcfa").alinearDerecha();
        tab_tabla1.getColumna("total_cpcfa").alinearDerecha();
        tab_tabla1.getColumna("total_cpcfa").setEstilo("font-size: 12px;font-weight: bold;");
        tab_tabla1.setScrollable(true);
        tab_tabla1.setScrollHeight(utilitario.getAltoPantalla() - 350);
        tab_tabla1.setLectura(true);
        //COLOR VERDE FACTURAS NO CONTABILIZADAS
        //COLOR ROJO FACTURAS ANULADAS
        tab_tabla1.setValueExpression("rowStyleClass", "fila.campos[5] eq '" + utilitario.getVariable("p_cxp_estado_factura_anulada") + "' ? 'text-red' : fila.campos[2] eq null  ? 'text-green' : null");
        tab_tabla1.onSelect("seleccionarDocumento");
        tab_tabla1.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_tabla1);

        gri.getChildren().add(pat_panel);

        //tabla asociación
        tab_tabla2 = new Tabla();
        tab_tabla2.setId("tab_tabla2");
        tab_tabla2.setTabla("pre_compromiso_factura", "ide_prcof", 7);
        tab_tabla2.setCondicion("ide_cpcfa=-1");
        tab_tabla2.getColumna("ide_cpcfa").setVisible(false);
        //tab_tabla2.getColumna("ide_prpot").setCombo("", "", "", "");
        tab_tabla2.setScrollable(true);
        tab_tabla2.setScrollHeight(utilitario.getAltoPantalla() - 450);
        tab_tabla2.dibujar();

        PanelTabla pat_panel2 = new PanelTabla();
        pat_panel2.setPanelTabla(tab_tabla2);

        pat_panel2.getMenuTabla().getItem_insertar().setMetodo("insertar7");
        pat_panel2.getMenuTabla().getItem_guardar().setMetodo("guardar7");
        pat_panel2.getMenuTabla().getItem_guardar().setRendered(true);
        pat_panel2.getMenuTabla().getItem_eliminar().setMetodo("eliminar7");
        pat_panel2.getMenuTabla().getItem_eliminar().setRendered(true);

        gri.getChildren().add(pat_panel2);
        mep_menu.dibujar(7, "COMPROMISO PRESUPUESTARIO DOCUMENTOS CXP", gri);
    }

    public void seleccionarDocumento(SelectEvent evt) {
        tab_tabla1.seleccionarFila(evt);
        tab_tabla2.setCondicion("ide_cpcfa=" + tab_tabla1.getValorSeleccionado());
        tab_tabla2.ejecutarSql();

    }

    public void cargarDocumetosProveedor(SelectEvent evt) {
        aut_proveedor.onSelect(evt);
        if (aut_proveedor.getValor() != null) {
            tab_tabla1.setSql(ser_cuentas_cxp.getSqlDocumentosProveedor(cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha(), aut_proveedor.getValor()));
            tab_tabla1.ejecutarSql();
            tab_tabla2.setCondicion("ide_cpcfa=" + tab_tabla1.getValorSeleccionado());
            tab_tabla2.ejecutarSql();
        } else {
            tab_tabla1.limpiar();
            tab_tabla2.limpiar();
        }
    }

    @Override
    public void abrirListaReportes() {
        rep_reporte.dibujar();
    }
    Map parametro = new HashMap();

    @Override
    public void aceptarReporte() {
        if (rep_reporte.getReporteSelecionado().equals("Comprobante de Retención")) {
            if (rep_reporte.isVisible()) {
                if (tab_tabla1.getValor("ide_cncre") != null) {
                    parametro = new HashMap();
                    rep_reporte.cerrar();
                    parametro.put("ide_cncre", Long.parseLong(tab_tabla1.getValor("ide_cncre")));
                    sel_rep.setSeleccionFormatoReporte(parametro, rep_reporte.getPath());
                    sel_rep.dibujar();
                    utilitario.addUpdate("rep_reporte,sel_rep");
                } else {
                    utilitario.agregarMensajeInfo("Seleccione un Comprobante de Retención", "");
                }

            }
        }
    }

    public void dibujarDocumentos() {

        Barra bar_menu = new Barra();
        bar_menu.setId("bar_menu");
        bar_menu.limpiar();
        Boton bot_ver = new Boton();
        bot_ver.setValue("Ver Documento");
        bot_ver.setMetodo("verDocumento");
        bar_menu.agregarComponente(bot_ver);

        Boton bot_anular = new Boton();
        bot_anular.setValue("Anular Documento");
        bot_anular.setMetodo("anularDocumento");
        bar_menu.agregarComponente(bot_anular);

        tab_tabla1 = new Tabla();
        tab_tabla1.setId("tab_tabla1");
        tab_tabla1.setSql(ser_cuentas_cxp.getSqlDocumentos(cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha(), String.valueOf(com_tipo_documento.getValue())));
        tab_tabla1.setCampoPrimaria("ide_cpcfa");
        tab_tabla1.getColumna("ide_cpcfa").setVisible(false);
        tab_tabla1.getColumna("ide_cpefa").setVisible(false);
        tab_tabla1.getColumna("ide_cncre").setVisible(false);
        tab_tabla1.getColumna("nombre_cpefa").setVisible(false);
        tab_tabla1.getColumna("numero_cpcfa").setFiltroContenido();
        tab_tabla1.getColumna("nom_geper").setFiltroContenido();
        tab_tabla1.getColumna("identificac_geper").setFiltroContenido();
        tab_tabla1.getColumna("ide_cnccc").setFiltroContenido();
        tab_tabla1.getColumna("ide_cnccc").setNombreVisual("N. ASIENTO");
        tab_tabla1.getColumna("IDE_CNCCC").setLink();
        tab_tabla1.getColumna("IDE_CNCCC").setMetodoChange("abrirAsiento");
        tab_tabla1.getColumna("IDE_CNCCC").alinearCentro();
        tab_tabla1.getColumna("ventas0").alinearDerecha();
        tab_tabla1.getColumna("ventas12").alinearDerecha();
        tab_tabla1.getColumna("valor_iva_cpcfa").alinearDerecha();
        tab_tabla1.getColumna("total_cpcfa").alinearDerecha();
        tab_tabla1.getColumna("total_cpcfa").setEstilo("font-size: 12px;font-weight: bold;");
        tab_tabla1.setRows(15);
        tab_tabla1.setLectura(true);
        //COLOR VERDE FACTURAS NO CONTABILIZADAS
        //COLOR ROJO FACTURAS ANULADAS
        tab_tabla1.setValueExpression("rowStyleClass", "fila.campos[5] eq '" + utilitario.getVariable("p_cxp_estado_factura_anulada") + "' ? 'text-red' : fila.campos[2] eq null  ? 'text-green' : null");
        tab_tabla1.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_tabla1);
        Grupo gru = new Grupo();
        gru.getChildren().add(bar_menu);
        gru.getChildren().add(pat_panel);
        mep_menu.dibujar(1, "LISTADO DE DOCUMENTOS POR PAGAR", gru);
    }

    public void dibujarDocumentosNoContabilizadas() {
        Barra bar_menu = new Barra();
        bar_menu.setId("bar_menu");
        bar_menu.limpiar();
        Boton bot_asi = new Boton();
        bot_asi.setValue("Generar Asiento Contable");
        bot_asi.setMetodo("abrirGeneraAsiento");
        bar_menu.agregarComponente(bot_asi);
        bar_menu.agregarSeparador();
        Boton bot_ver = new Boton();
        bot_ver.setValue("Ver Factura");
        bot_ver.setMetodo("abrirVerFactura");
        bar_menu.agregarComponente(bot_ver);
        eti1.setId("eti1");
        eti1.setValue("NUM. SELECCIONADOS : 0  -  VALOR : 0.00");
        eti1.setEstiloCabecera("");
        bar_menu.agregarComponente(eti1);

        tab_tabla1 = new Tabla();
        tab_tabla1.setId("tab_seleccion");
        tab_tabla1.setSql(ser_cuentas_cxp.getSqlDocumentosNoContabilidad(cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha(), String.valueOf(com_tipo_documento.getValue())));
        tab_tabla1.setCampoPrimaria("ide_cpcfa");
        tab_tabla1.getColumna("ide_cpcfa").setVisible(false);
        tab_tabla1.getColumna("ide_cpefa").setVisible(false);
        tab_tabla1.getColumna("nombre_cpefa").setFiltroContenido();
        tab_tabla1.getColumna("numero_cpcfa").setFiltroContenido();
        tab_tabla1.getColumna("nom_geper").setFiltroContenido();
        tab_tabla1.getColumna("identificac_geper").setFiltroContenido();
        tab_tabla1.getColumna("ventas0").alinearDerecha();
        tab_tabla1.getColumna("ventas12").alinearDerecha();
        tab_tabla1.getColumna("valor_iva_cpcfa").alinearDerecha();
        tab_tabla1.onSelectCheck("sumarSeleccionNoConta");
        tab_tabla1.onUnselectCheck("sumarSeleccionNoConta");
        tab_tabla1.getColumna("total_cpcfa").alinearDerecha();
        tab_tabla1.getColumna("total_cpcfa").setEstilo("font-size: 12px;font-weight: bold;");
        tab_tabla1.setRows(20);
        //tab_tabla1.setLectura(true);
        tab_tabla1.setTipoSeleccion(true);
        tab_tabla1.setSeleccionTabla("multiple");
        tab_tabla1.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_tabla1);
        Grupo gru = new Grupo();
        gru.getChildren().add(bar_menu);
        gru.getChildren().add(pat_panel);

        mep_menu.dibujar(2, "DOCUMENTOS POR PAGAR SIN COMPROBANTE CONTABLE", gru);
    }

    public void sumarSeleccionNoConta() {
        double dou_suma = 0;
        for (Fila actual : tab_tabla1.getSeleccionados()) {
            double valor = 0;
            try {
                valor = Double.parseDouble(String.valueOf(actual.getCampos()[tab_tabla1.getNumeroColumna("total_cpcfa")]));
            } catch (Exception e) {
            }
            dou_suma += valor;
        }
        eti1.setValue("NUM. SELECCIONADOS : " + tab_tabla1.getSeleccionados().length + "  -  VALOR : " + utilitario.getFormatoNumero(dou_suma));
        utilitario.addUpdate("eti1");
    }

    public void abrirGeneraAsiento() {
        if (tab_tabla1.getFilasSeleccionadas() != null && tab_tabla1.getFilasSeleccionadas().length() > 0) {
            asc_asiento.nuevoAsiento();
            asc_asiento.dibujar();
            asc_asiento.setAsientoDocumentosCxP(tab_tabla1.getFilasSeleccionadas());
            asc_asiento.getBot_aceptar().setMetodo("guardar");
        } else {
            utilitario.agregarMensajeInfo("Debe seleccionar almenos una Factura", "");
        }
    }

    public void dibujarDocumentosNoRetencion() {
        Barra bar_menu = new Barra();
        bar_menu.setId("bar_menu");
        bar_menu.limpiar();
        Boton bot_ver = new Boton();
        bot_ver.setValue("Generar Comprobante de Retención");
        bot_ver.setMetodo("abrirRetencion");
        bar_menu.agregarComponente(bot_ver);

        tab_tabla1 = new Tabla();
        tab_tabla1.setId("tab_tabla1");
        tab_tabla1.setSql(ser_cuentas_cxp.getSqlDocumentosNoRetencion(cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha(), String.valueOf(com_tipo_documento.getValue())));
        tab_tabla1.setCampoPrimaria("ide_cpcfa");
        tab_tabla1.getColumna("ide_cpcfa").setVisible(false);
        tab_tabla1.getColumna("ide_cpefa").setVisible(false);
        tab_tabla1.getColumna("nombre_cpefa").setFiltroContenido();
        tab_tabla1.getColumna("numero_cpcfa").setFiltroContenido();
        tab_tabla1.getColumna("nom_geper").setFiltroContenido();
        tab_tabla1.getColumna("identificac_geper").setFiltroContenido();
        tab_tabla1.getColumna("ide_cnccc").setFiltroContenido();
        tab_tabla1.getColumna("ide_cnccc").setNombreVisual("N. ASIENTO");
        tab_tabla1.getColumna("IDE_CNCCC").setLink();
        tab_tabla1.getColumna("IDE_CNCCC").setMetodoChange("abrirAsiento");
        tab_tabla1.getColumna("IDE_CNCCC").alinearCentro();
        tab_tabla1.getColumna("ventas0").alinearDerecha();
        tab_tabla1.getColumna("ventas12").alinearDerecha();
        tab_tabla1.getColumna("valor_iva_cpcfa").alinearDerecha();
        tab_tabla1.getColumna("total_cpcfa").alinearDerecha();
        tab_tabla1.getColumna("total_cpcfa").setEstilo("font-size: 12px;font-weight: bold;");
        tab_tabla1.setRows(20);
        tab_tabla1.setLectura(true);
        tab_tabla1.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_tabla1);
        Grupo gru = new Grupo();
        gru.getChildren().add(bar_menu);
        gru.getChildren().add(pat_panel);
        mep_menu.dibujar(3, "DOCUMENTOS POR PAGAR SIN COMPROBANTE DE RETENCION", gru);
    }

    public void dibujarDocumentosAnulados() {
        tab_tabla1 = new Tabla();
        tab_tabla1.setId("tab_tabla1");
        tab_tabla1.setSql(ser_cuentas_cxp.getSqlDocumentosAnulados(cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha(), String.valueOf(com_tipo_documento.getValue())));
        tab_tabla1.setCampoPrimaria("ide_cpcfa");
        tab_tabla1.getColumna("ide_cpcfa").setVisible(false);
        tab_tabla1.getColumna("ide_cpefa").setVisible(false);
        tab_tabla1.getColumna("nombre_cpefa").setFiltroContenido();
        tab_tabla1.getColumna("numero_cpcfa").setFiltroContenido();
        tab_tabla1.getColumna("nom_geper").setFiltroContenido();
        tab_tabla1.getColumna("identificac_geper").setFiltroContenido();
        tab_tabla1.getColumna("ide_cnccc").setFiltroContenido();
        tab_tabla1.getColumna("ide_cnccc").setNombreVisual("N. ASIENTO");
        tab_tabla1.getColumna("IDE_CNCCC").setLink();
        tab_tabla1.getColumna("IDE_CNCCC").setMetodoChange("abrirAsiento");
        tab_tabla1.getColumna("IDE_CNCCC").alinearCentro();
        tab_tabla1.getColumna("ventas0").alinearDerecha();
        tab_tabla1.getColumna("ventas12").alinearDerecha();
        tab_tabla1.getColumna("valor_iva_cpcfa").alinearDerecha();
        tab_tabla1.getColumna("total_cpcfa").alinearDerecha();
        tab_tabla1.getColumna("total_cpcfa").setEstilo("font-size: 12px;font-weight: bold;");
        tab_tabla1.setRows(20);
        tab_tabla1.setLectura(true);
        tab_tabla1.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_tabla1);
        Grupo gru = new Grupo();
        gru.getChildren().add(pat_panel);
        mep_menu.dibujar(4, "DOCUMENTOS POR PAGAR ANULADOS", gru);
    }

    public void dibujarDocumentosPorPagar() {
        tab_tabla1 = new Tabla();
        tab_tabla1.setId("tab_tabla1");
        tab_tabla1.setSql(ser_cuentas_cxp.getSqlDocumentosPorPagar(cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha(), String.valueOf(com_tipo_documento.getValue())));
        tab_tabla1.getColumna("saldo_x_pagar").setEstilo("font-size: 13px;font-weight: bold");
        tab_tabla1.getColumna("saldo_x_pagar").alinearDerecha();
        tab_tabla1.setCampoPrimaria("ide_cpctr");
        tab_tabla1.getColumna("ide_cpctr").setVisible(false);
        tab_tabla1.getColumna("ide_cpcfa").setVisible(false);
        tab_tabla1.getColumna("nom_geper").setFiltroContenido();
        tab_tabla1.getColumna("nom_geper").setNombreVisual("PROVEEDOR");
        tab_tabla1.getColumna("identificac_geper").setFiltroContenido();
        tab_tabla1.getColumna("identificac_geper").setNombreVisual("IDENTIFICACIÓN");
        tab_tabla1.getColumna("fecha").setVisible(true);
        tab_tabla1.getColumna("numero_cpcfa").setNombreVisual("N. FACTURA");
        tab_tabla1.getColumna("numero_cpcfa").setFiltroContenido();
        tab_tabla1.getColumna("saldo_x_pagar").setNombreVisual("SALDO");
        tab_tabla1.getColumna("total_cpcfa").setNombreVisual("TOTAL");
        tab_tabla1.getColumna("total_cpcfa").setEstilo("font-size: 13px;");
        tab_tabla1.getColumna("total_cpcfa").alinearDerecha();
        tab_tabla1.setLectura(true);
        tab_tabla1.setColumnaSuma("saldo_x_pagar");
        tab_tabla1.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_tabla1);
        Grupo gru = new Grupo();
        gru.getChildren().add(pat_panel);
        mep_menu.dibujar(5, "DOCUMENTOS PENDIENTES DE PAGO", gru);
    }

    public void dibujarGraficoCompras() {
        Grupo grupo = new Grupo();
        gca_facturas = new GraficoCartesiano();
        gca_facturas.setId("gca_facturas");

        gpa_facturas = new GraficoPastel();
        gpa_facturas.setId("gpa_facturas");
        gpa_facturas.setShowDataLabels(true);
        gpa_facturas.setStyle("width:300px;");

        com_periodo = new Combo();
        com_periodo.setMetodo("actualizarFiltros");
        com_periodo.setCombo(ser_cuentas_cxp.getSqlAniosFacturacion());
        com_periodo.eliminarVacio();
        com_periodo.setValue(utilitario.getAnio(utilitario.getFechaActual()));

        tab_tabla1 = new Tabla();
        tab_tabla1.setId("tab_tabla1");
        tab_tabla1.setSql(ser_cuentas_cxp.getSqlTotalComprasMensuales(String.valueOf(com_periodo.getValue())));
        tab_tabla1.setLectura(true);
        tab_tabla1.setColumnaSuma("num_documentos,compras12,compras0,iva,total");
        tab_tabla1.getColumna("num_documentos").alinearDerecha();
        tab_tabla1.getColumna("compras12").alinearDerecha();
        tab_tabla1.getColumna("compras0").alinearDerecha();
        tab_tabla1.getColumna("iva").alinearDerecha();
        tab_tabla1.getColumna("total").alinearDerecha();
        tab_tabla1.dibujar();
        Grid gri_opciones = new Grid();
        gri_opciones.setColumns(2);
        gri_opciones.getChildren().add(new Etiqueta("<strong>PERÍODO :</strong>"));
        gri_opciones.getChildren().add(com_periodo);
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.getChildren().add(gri_opciones);
        pat_panel.setPanelTabla(tab_tabla1);
        Grid gri = new Grid();
        gri.setWidth("100%");
        gri.setColumns(2);
        gpa_facturas.agregarSerie(tab_tabla1, "nombre_gemes", "num_documentos");
        gri.getChildren().add(pat_panel);
        gri.getChildren().add(gpa_facturas);
        grupo.getChildren().add(gri);

        gca_facturas.setTitulo("COMPRAS MENSUALES");
        gca_facturas.agregarSerie(tab_tabla1, "nombre_gemes", "total", "COMPRAS " + String.valueOf(com_periodo.getValue()));
        grupo.getChildren().add(gca_facturas);
        mep_menu.dibujar(6, "GRAFICOS DE COMPRAS", grupo);
    }

    public void dibujarFacturaElectronica() {

    }

    public void anularDocumento() {

    }

    public void verDocumento() {
        if (mep_menu.getOpcion() == 2) {
            if (tab_tabla1.getSeleccionados() != null && tab_tabla1.getSeleccionados().length > 0) {
                dcp_documento.verDocumento(tab_tabla1.getSeleccionados()[0].getRowKey());
                dcp_documento.dibujar();
            } else {
                utilitario.agregarMensajeInfo("Debe seleccionar una Factura", "");
            }
        } else if (tab_tabla1.getValorSeleccionado() != null) {
            dcp_documento.verDocumento(tab_tabla1.getValorSeleccionado());
            dcp_documento.dibujar();
        } else {
            utilitario.agregarMensajeInfo("Seleccione un Documento", "");
        }
    }

    public void actualizarFiltros() {
        switch (mep_menu.getOpcion()) {
            case 1:
                tab_tabla1.setSql(ser_cuentas_cxp.getSqlDocumentos(cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha(), String.valueOf(com_tipo_documento.getValue())));
                tab_tabla1.ejecutarSql();
                break;
            case 2:
                tab_tabla1.setSql(ser_cuentas_cxp.getSqlDocumentosNoContabilidad(cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha(), String.valueOf(com_tipo_documento.getValue())));
                tab_tabla1.ejecutarSql();
                break;
            case 3:
                tab_tabla1.setSql(ser_cuentas_cxp.getSqlDocumentosNoRetencion(cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha(), String.valueOf(com_tipo_documento.getValue())));
                tab_tabla1.ejecutarSql();
                break;
            case 4:
                tab_tabla1.setSql(ser_cuentas_cxp.getSqlDocumentosAnulados(cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha(), String.valueOf(com_tipo_documento.getValue())));
                tab_tabla1.ejecutarSql();
                break;
            case 5:
                tab_tabla1.setSql(ser_cuentas_cxp.getSqlDocumentosPorPagar(cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha(), String.valueOf(com_tipo_documento.getValue())));
                tab_tabla1.ejecutarSql();
                break;
            case 6:
                tab_tabla1.setSql(ser_cuentas_cxp.getSqlTotalComprasMensuales(String.valueOf(com_periodo.getValue())));
                tab_tabla1.ejecutarSql();
                gca_facturas.limpiar();
                gca_facturas.agregarSerie(tab_tabla1, "nombre_gemes", "total", "COMPRAS " + String.valueOf(com_periodo.getValue()));
                gpa_facturas.limpiar();
                gpa_facturas.agregarSerie(tab_tabla1, "nombre_gemes", "num_documentos");
                utilitario.addUpdate("gca_facturas,gpa_facturas");
                break;
            default:
                break;
        }

    }

    @Override
    public void insertar() {

        dcp_documento.nuevoDocumento();
        dcp_documento.dibujar();
    }

    public void eliminar7() {
        if (mep_menu.getOpcion() == 7) {
            tab_tabla2.eliminar();
        }
    }

    public void insertar7() {
        if (mep_menu.getOpcion() == 7) {
            if (tab_tabla2.isFocus()) {
                if (tab_tabla1.isEmpty() == false) {
                    tab_tabla2.insertar();
                    tab_tabla2.setValor("ide_cpcfa", tab_tabla1.getValorSeleccionado());
                } else {
                    utilitario.agregarMensajeInfo("Seleccione un Documento por pagar", "");
                }
            }
        }
    }

    public void guardar7() {
        if (mep_menu.getOpcion() == 7) {
            tab_tabla2.guardar();
            guardarPantalla();
        }
    }

    @Override
    public void guardar() {
        if (dcp_documento.isVisible()) {
            dcp_documento.guardar();
            if (dcp_documento.isVisible() == false) {
                //actualiza el punto de emision seleccionado y la tabla                
                dibujarDocumentos();
                tab_tabla1.setFilaActual(dcp_documento.getTab_cab_documento().getValor("ide_cpcfa"));
                utilitario.addUpdate("tab_tabla1");
                con_confirmar.dibujar();
            }
        } else if (ret_retencion.isVisible()) {
            ret_retencion.guardar();
            if (ret_retencion.isVisible() == false) {
                dibujarDocumentos();
                tab_tabla1.setFilaActual(ret_retencion.getIde_cpcfa());
                utilitario.addUpdate("tab_tabla1");
            }
        } else if (asc_asiento.isVisible()) {
            asc_asiento.guardar();
            if (asc_asiento.isVisible() == false) {
                dibujarDocumentosNoContabilizadas();
            }
        }
    }

    public void abrirRetencion() {
        ret_retencion.nuevaRetencionCompra(tab_tabla1.getValor("ide_cpcfa"));
        ret_retencion.dibujar();
    }

    @Override
    public void eliminar() {

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

    public DocumentoCxP getDcp_documento() {
        return dcp_documento;
    }

    public void setDcp_documento(DocumentoCxP dcp_documento) {
        this.dcp_documento = dcp_documento;
    }

    public Tabla getTab_tabla1() {
        return tab_tabla1;
    }

    public void setTab_tabla1(Tabla tab_tabla1) {
        this.tab_tabla1 = tab_tabla1;
    }

    public Retencion getRet_retencion() {
        return ret_retencion;
    }

    public void setRet_retencion(Retencion ret_retencion) {
        this.ret_retencion = ret_retencion;
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

    public Tabla getTab_seleccion() {
        return tab_tabla1;
    }

    public void setTab_seleccion(Tabla tab_tabla) {
        this.tab_tabla1 = tab_tabla;
    }

    public AsientoContable getAsc_asiento() {
        return asc_asiento;
    }

    public void setAsc_asiento(AsientoContable asc_asiento) {
        this.asc_asiento = asc_asiento;
    }

    public GraficoCartesiano getGca_facturas() {
        return gca_facturas;
    }

    public void setGca_facturas(GraficoCartesiano gca_facturas) {
        this.gca_facturas = gca_facturas;
    }

    public Confirmar getCon_confirmar() {
        return con_confirmar;
    }

    public void setCon_confirmar(Confirmar con_confirmar) {
        this.con_confirmar = con_confirmar;
    }

    public AutoCompletar getAut_proveedor() {
        return aut_proveedor;
    }

    public void setAut_proveedor(AutoCompletar aut_proveedor) {
        this.aut_proveedor = aut_proveedor;
    }

    public Tabla getTab_tabla2() {
        return tab_tabla2;
    }

    public void setTab_tabla2(Tabla tab_tabla2) {
        this.tab_tabla2 = tab_tabla2;
    }

}
