/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_cuentas_x_pagar;

import componentes.DocumentoCxP;
import framework.componentes.Boton;
import framework.componentes.Calendario;
import framework.componentes.Combo;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.Grupo;
import framework.componentes.MenuPanel;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import framework.componentes.graficos.GraficoCartesiano;
import framework.componentes.graficos.GraficoPastel;
import javax.ejb.EJB;
import servicios.cuentas_x_pagar.ServicioCuentasCxP;
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

    private Tabla tab_tabla1 = new Tabla();
    private Combo com_periodo;
    private GraficoCartesiano gca_facturas;
    private GraficoPastel gpa_facturas;

    public pre_documentosCxP() {
        bar_botones.quitarBotonsNavegacion();
        bar_botones.quitarBotonGuardar();
        bar_botones.quitarBotonEliminar();

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
        dcp_documento.getBot_aceptar().setMetodo("guardar");
        agregarComponente(dcp_documento);

        mep_menu.setMenuPanel("OPCIONES DOCUMENTOS POR PAGAR", "20%");
        mep_menu.agregarItem("Listado de Documentos CxP ", "dibujarDocumentos", "ui-icon-note");
        mep_menu.agregarItem("Generar Comprobante Contabilidad", "dibujarDocumentosNoContabilizadas", "ui-icon-notice");
        mep_menu.agregarItem("Generar Comprobante Retención", "dibujarDocumentosNoRetencion", "ui-icon-notice");
        mep_menu.agregarItem("Documentos Anulados", "dibujarDocumentosAnulados", "ui-icon-cancel");
        mep_menu.agregarItem("Documentos Por Pagar", "dibujarDocumentosPorPagar", "ui-icon-calculator");
        mep_menu.agregarSubMenu("INFORMES");
        mep_menu.agregarItem("Grafico de Compras", "dibujarGraficoCompras", "ui-icon-clock");
        // mep_menu.agregarItem("Estadística de Ventas", "dibujarEstadisticas", "ui-icon-bookmark");        
        mep_menu.agregarSubMenu("FACTURAS ELECTRONICAS");
        mep_menu.agregarItem("Ingresar Factura ", "dibujarFacturaElectronica", "ui-icon-signal-diag");
        agregarComponente(mep_menu);
        dibujarDocumentos();
    }

    public void dibujarDocumentos() {
        tab_tabla1 = new Tabla();
        tab_tabla1.setId("tab_tabla1");
        tab_tabla1.setSql(ser_cuentas_cxp.getSqlDocumentos(cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha(), String.valueOf(com_tipo_documento.getValue())));
        tab_tabla1.setCampoPrimaria("ide_cpcfa");
        tab_tabla1.getColumna("ide_cpcfa").setVisible(false);
        tab_tabla1.getColumna("ide_cpefa").setVisible(false);
        tab_tabla1.getColumna("nombre_cpefa").setFiltroContenido();
        tab_tabla1.getColumna("numero_cpcfa").setFiltroContenido();
        tab_tabla1.getColumna("nom_geper").setFiltroContenido();
        tab_tabla1.getColumna("identificac_geper").setFiltroContenido();
        tab_tabla1.getColumna("ide_cnccc").setFiltroContenido();
        tab_tabla1.getColumna("ventas0").alinearDerecha();
        tab_tabla1.getColumna("ventas12").alinearDerecha();
        tab_tabla1.getColumna("valor_iva_cpcfa").alinearDerecha();
        tab_tabla1.getColumna("total_cpcfa").alinearDerecha();
        tab_tabla1.getColumna("total_cpcfa").setEstilo("font-size: 12px;font-weight: bold;");
        tab_tabla1.setRows(20);
        tab_tabla1.setLectura(true);
        //COLOR VERDE FACTURAS NO CONTABILIZADAS
        //COLOR ROJO FACTURAS ANULADAS
        tab_tabla1.setValueExpression("rowStyleClass", "fila.campos[5] eq '" + utilitario.getVariable("p_cxp_estado_factura_anulada") + "' ? 'text-red' : fila.campos[18] eq null  ? 'text-green' : null");
        tab_tabla1.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_tabla1);
        Grupo gru = new Grupo();
        gru.getChildren().add(pat_panel);
        mep_menu.dibujar(1, "LISTADO DE DOCUMENTOS POR PAGAR", gru);
    }

    public void dibujarDocumentosNoContabilizadas() {
        tab_tabla1 = new Tabla();
        tab_tabla1.setId("tab_tabla1");
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
        tab_tabla1.getColumna("total_cpcfa").alinearDerecha();
        tab_tabla1.getColumna("total_cpcfa").setEstilo("font-size: 12px;font-weight: bold;");
        tab_tabla1.setRows(20);
        tab_tabla1.setLectura(true);
        tab_tabla1.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_tabla1);
        Grupo gru = new Grupo();
        gru.getChildren().add(pat_panel);
        mep_menu.dibujar(2, "DOCUMENTOS POR PAGAR SIN COMPROBANTE CONTABLE", gru);
    }

    public void dibujarDocumentosNoRetencion() {
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

    @Override
    public void guardar() {

    }

    @Override
    public void eliminar() {

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

}
