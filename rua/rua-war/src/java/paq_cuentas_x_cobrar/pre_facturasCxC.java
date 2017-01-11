/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_cuentas_x_cobrar;

import componentes.AsientoContable;
import componentes.FacturaCxC;
import framework.componentes.Barra;
import framework.componentes.Boton;
import framework.componentes.Calendario;
import framework.componentes.Combo;
import framework.componentes.Confirmar;
import framework.componentes.Espacio;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.Grupo;
import framework.componentes.Link;
import framework.componentes.Mascara;

import framework.componentes.MenuPanel;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.Tabla;
import framework.componentes.VisualizarPDF;
import framework.componentes.graficos.GraficoCartesiano;
import framework.componentes.graficos.GraficoPastel;
import java.util.HashMap;
import java.util.Map;
import javax.ejb.EJB;
import javax.faces.event.ActionEvent;
import org.primefaces.component.fieldset.Fieldset;
import servicios.cuentas_x_cobrar.ServicioFacturaCxC;
import servicios.sri.ServicioComprobatesElectronicos;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author dfjacome
 */
public class pre_facturasCxC extends Pantalla {

    private final MenuPanel mep_menu = new MenuPanel();

    @EJB
    private final ServicioFacturaCxC ser_factura = (ServicioFacturaCxC) utilitario.instanciarEJB(ServicioFacturaCxC.class);

    private final Combo com_pto_emision = new Combo();
    private final Calendario cal_fecha_inicio = new Calendario();
    private final Calendario cal_fecha_fin = new Calendario();

    private FacturaCxC fcc_factura = new FacturaCxC();

    private Tabla tab_tabla;

    private GraficoCartesiano gca_facturas;
    private GraficoPastel gpa_facturas;
    private Combo com_periodo;
    private Combo com_mes;

    //Facturacion Electrónica
    @EJB
    private final ServicioComprobatesElectronicos ser_comprobante = (ServicioComprobatesElectronicos) utilitario.instanciarEJB(ServicioComprobatesElectronicos.class);
    private Combo com_estados_fe;
    private VisualizarPDF vipdf_comprobante = new VisualizarPDF();

    private Reporte rep_reporte = new Reporte();
    private SeleccionFormatoReporte sel_rep = new SeleccionFormatoReporte();

    private AsientoContable asc_asiento = new AsientoContable();
    private Mascara mas_secuencial;

    private Confirmar con_confirma = new Confirmar();

    public pre_facturasCxC() {

        bar_botones.quitarBotonsNavegacion();
        bar_botones.quitarBotonGuardar();
        bar_botones.quitarBotonEliminar();
        bar_botones.agregarReporte();

        com_pto_emision.setId("com_pto_emision");
        com_pto_emision.setCombo(ser_factura.getSqlPuntosEmision());
        com_pto_emision.setMetodo("actualizarFacturas");
        com_pto_emision.eliminarVacio();
        bar_botones.agregarComponente(new Etiqueta("FACTURACIÓN:"));
        bar_botones.agregarComponente(com_pto_emision);

        bar_botones.agregarComponente(new Etiqueta("FECHA DESDE :"));
        cal_fecha_inicio.setValue(utilitario.getFecha(utilitario.getAnio(utilitario.getFechaActual()) + "-01-01"));
        bar_botones.agregarComponente(cal_fecha_inicio);

        bar_botones.agregarComponente(new Etiqueta("FECHA HASTA :"));
        cal_fecha_fin.setFechaActual();
        bar_botones.agregarComponente(cal_fecha_fin);

        Boton bot_consultar = new Boton();
        bot_consultar.setTitle("Buscar");
        bot_consultar.setMetodo("actualizarFacturas");
        bot_consultar.setIcon("ui-icon-search");
        bar_botones.agregarComponente(bot_consultar);

        fcc_factura.setId("fcc_factura");
        fcc_factura.getBot_aceptar().setMetodo("guardar");
        agregarComponente(fcc_factura);

        mep_menu.setMenuPanel("OPCIONES FACTURA", "20%");
        mep_menu.agregarItem("Listado de Facturas", "dibujarFacturas", "ui-icon-note");
        mep_menu.agregarItem("Generar Asiento Contable", "dibujarFacturasNoContabilizadas", "ui-icon-notice");
        mep_menu.agregarItem("Facturas Anuladas", "dibujarFacturasAnuladas", "ui-icon-cancel");
        mep_menu.agregarItem("Facturas Por Cobrar", "dibujarFacturasPorCobrar", "ui-icon-calculator");
        mep_menu.agregarSubMenu("INFORMES");
        mep_menu.agregarItem("Grafico de Ventas", "dibujarGraficoVentas", "ui-icon-clock");
        // mep_menu.agregarItem("Estadística de Ventas", "dibujarEstadisticas", "ui-icon-bookmark");
        mep_menu.agregarItem("Reporte de Ventas", "dibujarReporteVentas", "ui-icon-calendar");
        mep_menu.agregarSubMenu("FACTURACIÓN ELECTRÓNICA");
        mep_menu.agregarItem("Facturas Eléctrónicas", "dibujarFacturaElectronica", "ui-icon-signal-diag");
        agregarComponente(mep_menu);
        dibujarFacturas();

        vipdf_comprobante.setId("vipdf_comprobante");
        agregarComponente(vipdf_comprobante);

        rep_reporte.setId("rep_reporte");
        rep_reporte.getBot_aceptar().setMetodo("aceptarReporte");
        sel_rep.setId("sel_rep");
        agregarComponente(rep_reporte);
        agregarComponente(sel_rep);

        asc_asiento.setId("asc_asiento");
        asc_asiento.getBot_aceptar().setMetodo("guardar");
        agregarComponente(asc_asiento);

        con_confirma.setId("con_confirma");
        con_confirma.setMessage("Está seguro de Anular la Factura Seleccionada ?");
        con_confirma.setTitle("ANULAR FACTURA");
        con_confirma.getBot_aceptar().setValue("Si");
        con_confirma.getBot_cancelar().setValue("No");
        agregarComponente(con_confirma);
    }

    public void dibujarFacturas() {
        Barra bar_menu = new Barra();
        bar_menu.setId("bar_menu");
        bar_menu.limpiar();
        Boton bot_ver = new Boton();
        bot_ver.setValue("Ver Factura");
        bot_ver.setMetodo("abrirVerFactura");
        bar_menu.agregarComponente(bot_ver);

        Boton bot_anular = new Boton();
        bot_anular.setValue("Anular Factura");
        bot_anular.setMetodo("abrirAnularFactura");
        bar_menu.agregarComponente(bot_anular);

        Boton bot_retención = new Boton();
        bot_retención.setValue("Generar Retención");
        bar_menu.agregarBoton(bot_retención);

        tab_tabla = new Tabla();
        tab_tabla.setId("tab_tabla");
        tab_tabla.setSql(ser_factura.getSqlFacturas(com_pto_emision.getValue() + "", cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha()));
        tab_tabla.setCampoPrimaria("ide_cccfa");
        tab_tabla.getColumna("ide_cccfa").setVisible(false);
        tab_tabla.getColumna("ide_ccefa").setVisible(false);
        //tab_tabla.getColumna("nombre_ccefa").setFiltroContenido();
        tab_tabla.getColumna("nombre_ccefa").setVisible(false);
        tab_tabla.getColumna("secuencial_cccfa").setFiltroContenido();
        tab_tabla.getColumna("nom_geper").setFiltroContenido();
        tab_tabla.getColumna("identificac_geper").setFiltroContenido();
        tab_tabla.getColumna("ide_cnccc").setFiltroContenido();
        tab_tabla.getColumna("ide_cnccc").setNombreVisual("N. ASIENTO");
        tab_tabla.getColumna("IDE_CNCCC").setLink();
        tab_tabla.getColumna("IDE_CNCCC").setMetodoChange("abrirAsiento");
        tab_tabla.getColumna("IDE_CNCCC").alinearCentro();
        tab_tabla.getColumna("ventas0").alinearDerecha();
        tab_tabla.getColumna("ventas12").alinearDerecha();
        tab_tabla.getColumna("valor_iva_cccfa").alinearDerecha();
        tab_tabla.getColumna("total_cccfa").alinearDerecha();
        tab_tabla.getColumna("total_cccfa").setEstilo("font-size: 12px;font-weight: bold;");
        tab_tabla.setRows(15);
        tab_tabla.setLectura(true);
        //COLOR VERDE FACTURAS NO CONTABILIZADAS
        //COLOR ROJO FACTURAS ANULADAS
        tab_tabla.setValueExpression("rowStyleClass", "fila.campos[3] eq '" + utilitario.getVariable("p_cxc_estado_factura_anulada") + "' ? 'text-red' : fila.campos[2] eq null  ? 'text-green' : null");
        tab_tabla.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_tabla);
        Grupo gru = new Grupo();
        gru.getChildren().add(bar_menu);
        gru.getChildren().add(pat_panel);
        mep_menu.dibujar(1, "LISTADO DE FACTURAS", gru);
    }

    public void abrirAnularFactura() {
        if (tab_tabla.getValor("ide_cccfa") != null) {
            con_confirma.getBot_aceptar().setMetodo("anularFactura");
            con_confirma.dibujar();
        } else {
            utilitario.agregarMensajeError("Debe seleccionar una Factura", "");
        }

    }

    public void dibujarFacturasNoContabilizadas() {
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

        tab_tabla = new Tabla();
        tab_tabla.setId("tab_seleccion");
        tab_tabla.setSql(ser_factura.getSqlFacturasNoContabilizadas(com_pto_emision.getValue() + "", cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha()));
        tab_tabla.setCampoPrimaria("ide_cccfa");
        tab_tabla.getColumna("ide_cccfa").setVisible(false);
        tab_tabla.getColumna("ide_geper").setVisible(false);
        tab_tabla.getColumna("secuencial_cccfa").setFiltroContenido();
        tab_tabla.getColumna("nom_geper").setFiltroContenido();
        tab_tabla.getColumna("identificac_geper").setFiltroContenido();
        tab_tabla.getColumna("ventas0").alinearDerecha();
        tab_tabla.getColumna("ventas12").alinearDerecha();
        tab_tabla.getColumna("valor_iva_cccfa").alinearDerecha();
        tab_tabla.getColumna("total_cccfa").alinearDerecha();
        tab_tabla.getColumna("total_cccfa").setEstilo("font-size: 12px;font-weight: bold;");
        tab_tabla.setRows(15);
        //tab_tabla.setLectura(true);   
        tab_tabla.setTipoSeleccion(true);
        tab_tabla.setSeleccionTabla("multiple");

        tab_tabla.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_tabla);
        Grupo gru = new Grupo();
        gru.getChildren().add(bar_menu);
        gru.getChildren().add(pat_panel);
        mep_menu.dibujar(2, "FACTURAS NO CONTABILIZADAS", gru);
    }

    public void abrirGeneraAsiento() {
        if (tab_tabla.getFilasSeleccionadas() != null && tab_tabla.getFilasSeleccionadas().length() > 0) {
            asc_asiento.nuevoAsiento();
            asc_asiento.dibujar();
            asc_asiento.setAsientoFacturasCxC(tab_tabla.getFilasSeleccionadas());
            asc_asiento.getBot_aceptar().setMetodo("guardar");
        } else {
            utilitario.agregarMensajeInfo("Debe seleccionar almenos una Factura", "");
        }
    }

    /**
     * Abre el asiento contable seleccionado
     *
     * @param evt
     */
    public void abrirAsiento(ActionEvent evt) {
        Link lin_ide_cnccc = (Link) evt.getComponent();
        asc_asiento.setAsientoContable(lin_ide_cnccc.getValue().toString());
        tab_tabla.setFilaActual(lin_ide_cnccc.getDir());
        asc_asiento.dibujar();
    }

    public void dibujarFacturasAnuladas() {
        tab_tabla = new Tabla();
        tab_tabla.setId("tab_tabla");
        tab_tabla.setSql(ser_factura.getSqlFacturasAnuladas(com_pto_emision.getValue() + "", cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha()));
        tab_tabla.setCampoPrimaria("ide_cccfa");
        tab_tabla.getColumna("ide_cccfa").setVisible(false);
        tab_tabla.getColumna("secuencial_cccfa").setFiltroContenido();
        tab_tabla.getColumna("nom_geper").setFiltroContenido();
        tab_tabla.getColumna("identificac_geper").setFiltroContenido();
        tab_tabla.getColumna("ventas0").alinearDerecha();
        tab_tabla.getColumna("ventas12").alinearDerecha();
        tab_tabla.getColumna("valor_iva_cccfa").alinearDerecha();
        tab_tabla.getColumna("total_cccfa").alinearDerecha();
        tab_tabla.getColumna("total_cccfa").setEstilo("font-size: 12px;font-weight: bold;");
        tab_tabla.getColumna("ide_cnccc").setFiltroContenido();
        tab_tabla.getColumna("ide_cnccc").setNombreVisual("N. ASIENTO");
        tab_tabla.getColumna("IDE_CNCCC").setLink();
        tab_tabla.getColumna("IDE_CNCCC").setMetodoChange("abrirAsiento");
        tab_tabla.getColumna("IDE_CNCCC").alinearCentro();
        tab_tabla.setRows(15);
        tab_tabla.setLectura(true);
        tab_tabla.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_tabla);

        mas_secuencial = new Mascara();
        mas_secuencial.setId("mas_secuencial");
        mas_secuencial.setMask("999999999");

        Fieldset fie_anula = new Fieldset();
        fie_anula.setLegend("Ingresar Factura Anulada");

        Grid gri = new Grid();
        gri.setColumns(4);
        gri.getChildren().add(new Etiqueta("<strong>NUM. SECUENCIAL :</strong> <span style='color:red;font-weight: bold;'> *</span>"));
        gri.getChildren().add(mas_secuencial);
        gri.getChildren().add(new Espacio("5", "5"));
        Boton bot_anula = new Boton();
        bot_anula.setValue("Anular");
        bot_anula.setMetodo("ingresarAnulada");
        gri.getChildren().add(bot_anula);

        fie_anula.getChildren().add(gri);

        Grupo gru = new Grupo();
        gru.getChildren().add(fie_anula);
        gru.getChildren().add(pat_panel);
        mep_menu.dibujar(3, "FACTURAS ANULADAS", gru);
    }

    public void dibujarFacturasPorCobrar() {
        tab_tabla = new Tabla();
        tab_tabla.setId("tab_tabla");
        tab_tabla.setSql(ser_factura.getSqlFacturasPorCobrar(com_pto_emision.getValue() + "", cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha()));
        tab_tabla.setCampoPrimaria("ide_cccfa");
        tab_tabla.getColumna("ide_cccfa").setVisible(false);
        tab_tabla.getColumna("secuencial_cccfa").setFiltroContenido();
        tab_tabla.getColumna("nom_geper").setFiltroContenido();
        tab_tabla.getColumna("identificac_geper").setFiltroContenido();
        tab_tabla.getColumna("ventas0").alinearDerecha();
        tab_tabla.getColumna("ventas12").alinearDerecha();
        tab_tabla.getColumna("valor_iva_cccfa").alinearDerecha();
        tab_tabla.getColumna("total_cccfa").alinearDerecha();
        tab_tabla.getColumna("total_cccfa").setEstilo("font-size: 12px;font-weight: bold;");
        tab_tabla.getColumna("saldo_x_pagar").alinearDerecha();
        tab_tabla.getColumna("saldo_x_pagar").setEstilo("font-size: 12px;font-weight: bold;color:red");
        tab_tabla.getColumna("ide_cnccc").setFiltroContenido();
        tab_tabla.getColumna("ide_cnccc").setNombreVisual("N. ASIENTO");
        tab_tabla.getColumna("IDE_CNCCC").setLink();
        tab_tabla.getColumna("IDE_CNCCC").setMetodoChange("abrirAsiento");
        tab_tabla.getColumna("IDE_CNCCC").alinearCentro();
        tab_tabla.setLectura(true);
        tab_tabla.setRows(15);
        tab_tabla.setColumnaSuma("total_cccfa,saldo_x_pagar");
        tab_tabla.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_tabla);

        mep_menu.dibujar(4, "FACTURAS POR COBRAR", pat_panel);
    }

    public void dibujarGraficoVentas() {
        Grupo grupo = new Grupo();
        gca_facturas = new GraficoCartesiano();
        gca_facturas.setId("gca_facturas");

        gpa_facturas = new GraficoPastel();
        gpa_facturas.setId("gpa_facturas");
        gpa_facturas.setShowDataLabels(true);
        gpa_facturas.setStyle("width:300px;");

        com_periodo = new Combo();
        com_periodo.setMetodo("actualizarFacturas");
        com_periodo.setCombo(ser_factura.getSqlAniosFacturacion());
        com_periodo.eliminarVacio();
        com_periodo.setValue(utilitario.getAnio(utilitario.getFechaActual()));

        tab_tabla = new Tabla();
        tab_tabla.setId("tab_tabla");
        tab_tabla.setSql(ser_factura.getSqlTotalVentasMensuales(String.valueOf(com_pto_emision.getValue()), String.valueOf(com_periodo.getValue())));
        tab_tabla.setLectura(true);
        tab_tabla.setColumnaSuma("num_facturas,ventas12,ventas0,iva,total");
        tab_tabla.getColumna("num_facturas").alinearDerecha();
        tab_tabla.getColumna("ventas12").alinearDerecha();
        tab_tabla.getColumna("ventas0").alinearDerecha();
        tab_tabla.getColumna("iva").alinearDerecha();
        tab_tabla.getColumna("total").alinearDerecha();
        tab_tabla.dibujar();

        Grid gri_opciones = new Grid();
        gri_opciones.setColumns(2);
        gri_opciones.getChildren().add(new Etiqueta("<strong>PERÍODO :</strong>"));
        gri_opciones.getChildren().add(com_periodo);
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.getChildren().add(gri_opciones);
        pat_panel.setPanelTabla(tab_tabla);

        Grid gri = new Grid();
        gri.setWidth("100%");
        gri.setColumns(2);
        gpa_facturas.agregarSerie(tab_tabla, "nombre_gemes", "num_facturas");
        gri.getChildren().add(pat_panel);
        gri.getChildren().add(gpa_facturas);
        grupo.getChildren().add(gri);

        gca_facturas.setTitulo("VENTAS MENSUALES");
        gca_facturas.agregarSerie(tab_tabla, "nombre_gemes", "total", "VENTAS " + String.valueOf(com_periodo.getValue()));
        grupo.getChildren().add(gca_facturas);
        mep_menu.dibujar(5, "GRAFICOS DE VENTAS", grupo);
    }

    public void dibujarReporteVentas() {

        Barra bar_menu = new Barra();
        bar_menu.setId("bar_menu");
        bar_menu.limpiar();

        com_mes = new Combo();
        com_mes.setMetodo("actualizarFacturas");
        com_mes.setCombo(ser_factura.getSqlMeses());
        com_mes.eliminarVacio();
        com_mes.setValue(String.valueOf(utilitario.getMes(utilitario.getFechaActual())));

        com_periodo = new Combo();
        com_periodo.setMetodo("actualizarFacturas");
        com_periodo.setCombo(ser_factura.getSqlAniosFacturacion());
        com_periodo.eliminarVacio();
        com_periodo.setValue(utilitario.getAnio(utilitario.getFechaActual()));

        bar_menu.agregarComponente(new Etiqueta("<strong>PERÍODO :</strong>"));
        bar_menu.agregarComponente(com_periodo);
        bar_menu.agregarComponente(new Etiqueta("<strong>MES :</strong>"));
        bar_menu.agregarComponente(com_mes);

        tab_tabla = new Tabla();
        tab_tabla.setId("tab_tabla");
        tab_tabla.setSql(ser_factura.getSqlVentasMensuales(com_pto_emision.getValue() + "", com_mes.getValue() + "", com_periodo.getValue() + ""));
        tab_tabla.getColumna("ide_cccfa").setVisible(false);
        tab_tabla.getColumna("observacion_cccfa").setVisible(false);
        tab_tabla.setRows(15);

        tab_tabla.setLectura(true);
        tab_tabla.getColumna("NOM_GEPER").setLongitud(100);
        tab_tabla.setColumnaSuma("ventas12,ventas0,valor_iva_cccfa,total_cccfa");
        tab_tabla.getColumna("ventas12").alinearDerecha();
        tab_tabla.getColumna("ventas0").alinearDerecha();
        tab_tabla.getColumna("valor_iva_cccfa").alinearDerecha();
        tab_tabla.getColumna("total_cccfa").alinearDerecha();
        tab_tabla.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_tabla);

        Grupo grupo = new Grupo();
        grupo.getChildren().add(bar_menu);
        grupo.getChildren().add(pat_panel);

        mep_menu.dibujar(7, "REPORTE DE VENTAS POR MES Y PERÍODO", grupo);

    }

    public void dibujarFacturaElectronica() {
        Grupo grupo = new Grupo();

        Barra bar_menu = new Barra();
        bar_menu.setId("bar_menu");
        bar_menu.limpiar();

        com_estados_fe = new Combo();
        com_estados_fe.setCombo("SELECT * FROM sri_estado_comprobante order by nombre_sresc");
        com_estados_fe.setMetodo("actualizarFacturas");

        bar_menu.agregarComponente(new Etiqueta("ESTADOS COMPROBANTES ELECTRÓNICOS :"));
        bar_menu.agregarComponente(com_estados_fe);
        bar_menu.agregarSeparador();
        Boton bot_pdf = new Boton();
        bot_pdf.setValue("Ver PDF");
        bot_pdf.setMetodo("generarPDF");
        bar_menu.agregarComponente(bot_pdf);

        Boton bot_xml = new Boton();
        bot_xml.setValue("Descargar XML");
        bot_xml.setMetodo("descargarXML");
        bot_xml.setAjax(false);
        bar_menu.agregarComponente(bot_xml);
        tab_tabla = new Tabla();
        tab_tabla.setId("tab_tabla");

        tab_tabla.setSql(ser_comprobante.getSqlFacturasElectronicas(cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha(), String.valueOf(com_estados_fe.getValue())));

        tab_tabla.getColumna("ide_srcom").setVisible(false);
        tab_tabla.getColumna("ide_cccfa").setVisible(false);
        tab_tabla.getColumna("SECUENCIAL_SRCOM").setFiltroContenido();

        tab_tabla.setLectura(true);
        tab_tabla.setRows(15);
        tab_tabla.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_tabla);

        grupo.getChildren().add(bar_menu);
        grupo.getChildren().add(pat_panel);

        mep_menu.dibujar(8, "FACTURAS ELECTRÓNICAS", grupo);
    }

    public void abrirVerFactura() {
        if (mep_menu.getOpcion() == 2) {
            if (tab_tabla.getSeleccionados() != null && tab_tabla.getSeleccionados().length > 0) {
                fcc_factura.verFactura(tab_tabla.getSeleccionados()[0].getRowKey());
                fcc_factura.dibujar();
            } else {
                utilitario.agregarMensajeInfo("Debe seleccionar una Factura", "");
            }
        } else if (tab_tabla.getValorSeleccionado() != null) {
            fcc_factura.verFactura(tab_tabla.getValor("ide_cccfa"));
            fcc_factura.dibujar();
        } else {
            utilitario.agregarMensajeInfo("Debe seleccionar una Factura", "");
        }

    }

    @Override
    public void abrirListaReportes() {
//Se ejecuta cuando da click en el boton de Reportes de la Barra    
        rep_reporte.dibujar();
    }

    Map parametro = new HashMap();

    @Override
    public void aceptarReporte() {
//Se ejecuta cuando se selecciona un reporte de la lista
        if (rep_reporte.getReporteSelecionado().equals("Facturas") || rep_reporte.getReporteSelecionado().equals("Facturas Eventos")
                || rep_reporte.getReporteSelecionado().equals("Facturas Nueva") || rep_reporte.getReporteSelecionado().equals("Facturas con Formato")) {
            if (rep_reporte.isVisible()) {
                parametro = new HashMap();
                rep_reporte.cerrar();
                parametro.put("ide_cccfa", Long.parseLong(tab_tabla.getValorSeleccionado()));
                sel_rep.setSeleccionFormatoReporte(parametro, rep_reporte.getPath());
                sel_rep.dibujar();
            }
        } else if (rep_reporte.getReporteSelecionado().equals("Comprobante Contabilidad")) {
            if (rep_reporte.isVisible()) {
                if (tab_tabla.getValor("ide_cnccc") != null && !tab_tabla.getValor("ide_cnccc").isEmpty()) {
                    parametro = new HashMap();
                    rep_reporte.cerrar();
                    parametro.put("ide_cnccc", Long.parseLong(tab_tabla.getValor("ide_cnccc")));
                    parametro.put("ide_cnlap_haber", utilitario.getVariable("p_con_lugar_haber"));
                    parametro.put("ide_cnlap_debe", utilitario.getVariable("p_con_lugar_debe"));
                    sel_rep.setSeleccionFormatoReporte(parametro, rep_reporte.getPath());
                    sel_rep.dibujar();
                } else {
                    utilitario.agregarMensajeInfo("Comprobante de Contabilidad", "La factura seleccionada no tiene Comprobante de Contabilidad");
                }
            }
        }
    }

    public void actualizarFacturas() {
        if (mep_menu.getOpcion() == 1) {
            tab_tabla.setSql(ser_factura.getSqlFacturas(com_pto_emision.getValue() + "", cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha()));
            tab_tabla.ejecutarSql();
        } else if (mep_menu.getOpcion() == 2) {
            tab_tabla.setSql(ser_factura.getSqlFacturasNoContabilizadas(com_pto_emision.getValue() + "", cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha()));
            tab_tabla.ejecutarSql();
        } else if (mep_menu.getOpcion() == 3) {
            tab_tabla.setSql(ser_factura.getSqlFacturasAnuladas(com_pto_emision.getValue() + "", cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha()));
            tab_tabla.ejecutarSql();
        } else if (mep_menu.getOpcion() == 4) {
            tab_tabla.setSql(ser_factura.getSqlFacturasPorCobrar(com_pto_emision.getValue() + "", cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha()));
            tab_tabla.ejecutarSql();
        } else if (mep_menu.getOpcion() == 5) {
            tab_tabla.setSql(ser_factura.getSqlTotalVentasMensuales(com_pto_emision.getValue() + "", String.valueOf(com_periodo.getValue())));
            tab_tabla.ejecutarSql();
            gca_facturas.limpiar();
            gca_facturas.agregarSerie(tab_tabla, "nombre_gemes", "total", "VENTAS " + String.valueOf(com_periodo.getValue()));
            gpa_facturas.limpiar();
            gpa_facturas.agregarSerie(tab_tabla, "nombre_gemes", "num_facturas");
            utilitario.addUpdate("gca_facturas,gpa_facturas");
        } else if (mep_menu.getOpcion() == 7) {
            tab_tabla.setSql(ser_factura.getSqlVentasMensuales(com_pto_emision.getValue() + "", com_mes.getValue() + "", com_periodo.getValue() + ""));
            tab_tabla.ejecutarSql();
        } else if (mep_menu.getOpcion() == 8) {
            tab_tabla.setSql(ser_comprobante.getSqlFacturasElectronicas(cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha(), String.valueOf(com_estados_fe.getValue())));
            tab_tabla.ejecutarSql();
        }

    }

    public void generarPDF() {
        if (tab_tabla.getValorSeleccionado() != null) {
            ser_comprobante.generarPDF(tab_tabla.getValorSeleccionado());
            vipdf_comprobante.setVisualizarPDFUsuario();
            vipdf_comprobante.dibujar();
        } else {
            utilitario.agregarMensajeInfo("Seleccione una Factura Electrónica", "");
        }
    }

    public void descargarXML() {
        if (tab_tabla.getValorSeleccionado() != null) {
            ser_comprobante.generarXML(tab_tabla.getValorSeleccionado());
        } else {
            utilitario.agregarMensajeInfo("Seleccione una Factura Electrónica", "");
        }
    }

    @Override
    public void insertar() {
        fcc_factura.nuevaFactura();
        fcc_factura.dibujar();
    }

    @Override
    public void guardar() {
        if (fcc_factura.isVisible()) {
            fcc_factura.guardar();
            if (fcc_factura.isVisible() == false) {
                //actualiza el punto de emision seleccionado y la tabla
                com_pto_emision.setValue(fcc_factura.getComboPuntoEmision().getValue());
                dibujarFacturas();
                tab_tabla.setFilaActual(fcc_factura.getTab_cab_factura().getValor("ide_cccfa"));
                utilitario.addUpdate("com_pto_emision,tab_tabla");
            }
        } else if (asc_asiento.isVisible()) {
            asc_asiento.guardar();
            if (asc_asiento.isVisible() == false) {
                dibujarFacturasNoContabilizadas();
            }
        }
    }

    public void anularFactura() {
        if (tab_tabla.getValor("ide_cccfa") != null) {
            ser_factura.anularFactura(tab_tabla.getValor("ide_cccfa"));
            if (guardarPantalla().isEmpty()) {
                con_confirma.cerrar();
                tab_tabla.actualizar();
            }
        } else {
            utilitario.agregarMensajeError("Debe seleccionar una Factura", "");
        }
    }

    public void ingresarAnulada() {
        if (mas_secuencial.getValue() != null) {
            ser_factura.anularSecuencial(String.valueOf(mas_secuencial.getValue()), String.valueOf(com_pto_emision.getValue()));
            //utilitario.getConexion().setImprimirSqlConsola(true);
            if (guardarPantalla().isEmpty()) {
                tab_tabla.actualizar();
                mas_secuencial.limpiar();
                utilitario.addUpdate("mas_secuencial");
            }
        } else {
            utilitario.agregarMensajeError("Debe ingresar el Número Secuencial de la Factura", "");
        }
    }

    @Override
    public void eliminar() {

    }

    public FacturaCxC getFcc_factura() {
        return fcc_factura;
    }

    public void setFcc_factura(FacturaCxC fcc_factura) {
        this.fcc_factura = fcc_factura;
    }

    public GraficoCartesiano getGca_facturas() {
        return gca_facturas;
    }

    public void setGca_facturas(GraficoCartesiano gca_facturas) {
        this.gca_facturas = gca_facturas;
    }

    public VisualizarPDF getVipdf_comprobante() {
        return vipdf_comprobante;
    }

    public void setVipdf_comprobante(VisualizarPDF vipdf_comprobante) {
        this.vipdf_comprobante = vipdf_comprobante;
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

    public AsientoContable getAsc_asiento() {
        return asc_asiento;
    }

    public void setAsc_asiento(AsientoContable asc_asiento) {
        this.asc_asiento = asc_asiento;
    }

    public Tabla getTab_tabla() {
        return tab_tabla;
    }

    public void setTab_tabla(Tabla tab_tabla) {
        this.tab_tabla = tab_tabla;
    }

    public Tabla getTab_seleccion() {
        return tab_tabla;
    }

    public void setTab_seleccion(Tabla tab_tabla) {
        this.tab_tabla = tab_tabla;
    }

    public Confirmar getCon_confirma() {
        return con_confirma;
    }

    public void setCon_confirma(Confirmar con_confirma) {
        this.con_confirma = con_confirma;
    }

}
