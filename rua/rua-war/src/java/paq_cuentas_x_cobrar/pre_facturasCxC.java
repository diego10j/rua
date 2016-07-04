/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_cuentas_x_cobrar;

import componentes.FacturaCxC;
import framework.componentes.Barra;
import framework.componentes.Boton;
import framework.componentes.Calendario;
import framework.componentes.Combo;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.Grupo;

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

    private Tabla tab_facturas;
    private Tabla tab_facturas_no_conta;
    private Tabla tab_facturas_anuladas;
    private Tabla tab_facturas_x_cobrar;

    private GraficoCartesiano gca_facturas;
    private GraficoPastel gpa_facturas;
    private Tabla tab_datos_grafico;
    private Combo com_periodo;

    private Tabla tab_rep_ventas;
    private Combo com_mes;

    //Facturacion Electrónica
    @EJB
    private final ServicioComprobatesElectronicos ser_comprobante = (ServicioComprobatesElectronicos) utilitario.instanciarEJB(ServicioComprobatesElectronicos.class);
    private Combo com_estados_fe;
    private Tabla tab_facturas_fe;
    private VisualizarPDF vipdf_comprobante = new VisualizarPDF();

    private Reporte rep_reporte = new Reporte();
    private SeleccionFormatoReporte sel_rep = new SeleccionFormatoReporte();

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
        mep_menu.agregarItem("Generar Comprobante Contabilidad", "dibujarFacturasNoContabilizadas", "ui-icon-notice");
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
        bar_menu.agregarComponente(bot_anular);

        Boton bot_asiento = new Boton();
        bot_asiento.setValue("Generar Asiento");
        bar_menu.agregarBoton(bot_asiento);

        Boton bot_retención = new Boton();
        bot_retención.setValue("Generar Retención");
        bar_menu.agregarBoton(bot_retención);

        tab_facturas = new Tabla();
        tab_facturas.setId("tab_facturas");
        tab_facturas.setSql(ser_factura.getSqlFacturas(com_pto_emision.getValue() + "", cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha()));
        tab_facturas.setCampoPrimaria("ide_cccfa");
        tab_facturas.getColumna("ide_cccfa").setVisible(false);
        tab_facturas.getColumna("ide_ccefa").setVisible(false);
        tab_facturas.getColumna("nombre_ccefa").setFiltroContenido();
        tab_facturas.getColumna("secuencial_cccfa").setFiltroContenido();
        tab_facturas.getColumna("nom_geper").setFiltroContenido();
        tab_facturas.getColumna("identificac_geper").setFiltroContenido();
        tab_facturas.getColumna("ide_cnccc").setFiltroContenido();
        tab_facturas.getColumna("ventas0").alinearDerecha();
        tab_facturas.getColumna("ventas12").alinearDerecha();
        tab_facturas.getColumna("valor_iva_cccfa").alinearDerecha();
        tab_facturas.getColumna("total_cccfa").alinearDerecha();
        tab_facturas.getColumna("total_cccfa").setEstilo("font-size: 12px;font-weight: bold;");
        tab_facturas.setRows(15);
        tab_facturas.setLectura(true);
        //COLOR VERDE FACTURAS NO CONTABILIZADAS
        //COLOR ROJO FACTURAS ANULADAS
        tab_facturas.setValueExpression("rowStyleClass", "fila.campos[2] eq '" + utilitario.getVariable("p_cxc_estado_factura_anulada") + "' ? 'text-red' : fila.campos[13] eq null  ? 'text-green' : null");
        tab_facturas.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_facturas);
        Grupo gru = new Grupo();
        gru.getChildren().add(bar_menu);
        gru.getChildren().add(pat_panel);
        mep_menu.dibujar(1, "LISTADO DE FACTURAS", gru);
    }

    public void dibujarFacturasNoContabilizadas() {
        tab_facturas_no_conta = new Tabla();
        tab_facturas_no_conta.setId("tab_facturas_no_conta");
        tab_facturas_no_conta.setSql(ser_factura.getSqlFacturasNoContabilizadas(com_pto_emision.getValue() + "", cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha()));
        tab_facturas_no_conta.setCampoPrimaria("ide_cccfa");
        tab_facturas_no_conta.getColumna("ide_cccfa").setVisible(false);
        tab_facturas_no_conta.getColumna("secuencial_cccfa").setFiltroContenido();
        tab_facturas_no_conta.getColumna("nom_geper").setFiltroContenido();
        tab_facturas_no_conta.getColumna("identificac_geper").setFiltroContenido();
        tab_facturas_no_conta.getColumna("ventas0").alinearDerecha();
        tab_facturas_no_conta.getColumna("ventas12").alinearDerecha();
        tab_facturas_no_conta.getColumna("valor_iva_cccfa").alinearDerecha();
        tab_facturas_no_conta.getColumna("total_cccfa").alinearDerecha();
        tab_facturas_no_conta.getColumna("total_cccfa").setEstilo("font-size: 12px;font-weight: bold;");
        tab_facturas_no_conta.setRows(15);
        tab_facturas_no_conta.setLectura(true);
        tab_facturas_no_conta.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_facturas_no_conta);

        mep_menu.dibujar(2, "FACTURAS NO CONTABILIZADAS", pat_panel);
    }

    public void dibujarFacturasAnuladas() {
        tab_facturas_anuladas = new Tabla();
        tab_facturas_anuladas.setId("tab_facturas_anuladas");
        tab_facturas_anuladas.setSql(ser_factura.getSqlFacturasAnuladas(com_pto_emision.getValue() + "", cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha()));
        tab_facturas_anuladas.setCampoPrimaria("ide_cccfa");
        tab_facturas_anuladas.getColumna("ide_cccfa").setVisible(false);
        tab_facturas_anuladas.getColumna("secuencial_cccfa").setFiltroContenido();
        tab_facturas_anuladas.getColumna("nom_geper").setFiltroContenido();
        tab_facturas_anuladas.getColumna("identificac_geper").setFiltroContenido();
        tab_facturas_anuladas.getColumna("ventas0").alinearDerecha();
        tab_facturas_anuladas.getColumna("ventas12").alinearDerecha();
        tab_facturas_anuladas.getColumna("valor_iva_cccfa").alinearDerecha();
        tab_facturas_anuladas.getColumna("total_cccfa").alinearDerecha();
        tab_facturas_anuladas.getColumna("total_cccfa").setEstilo("font-size: 12px;font-weight: bold;");
        tab_facturas_anuladas.getColumna("ide_cnccc").setFiltroContenido();
        tab_facturas_anuladas.setRows(15);
        tab_facturas_anuladas.setLectura(true);
        tab_facturas_anuladas.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_facturas_anuladas);

        mep_menu.dibujar(3, "FACTURAS ANULADAS", pat_panel);
    }

    public void dibujarFacturasPorCobrar() {
        tab_facturas_x_cobrar = new Tabla();
        tab_facturas_x_cobrar.setId("tab_facturas_x_cobrar");
        tab_facturas_x_cobrar.setSql(ser_factura.getSqlFacturasPorCobrar(com_pto_emision.getValue() + "", cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha()));
        tab_facturas_x_cobrar.setCampoPrimaria("ide_cccfa");
        tab_facturas_x_cobrar.getColumna("ide_cccfa").setVisible(false);
        tab_facturas_x_cobrar.getColumna("secuencial_cccfa").setFiltroContenido();
        tab_facturas_x_cobrar.getColumna("nom_geper").setFiltroContenido();
        tab_facturas_x_cobrar.getColumna("identificac_geper").setFiltroContenido();
        tab_facturas_x_cobrar.getColumna("ventas0").alinearDerecha();
        tab_facturas_x_cobrar.getColumna("ventas12").alinearDerecha();
        tab_facturas_x_cobrar.getColumna("valor_iva_cccfa").alinearDerecha();
        tab_facturas_x_cobrar.getColumna("total_cccfa").alinearDerecha();
        tab_facturas_x_cobrar.getColumna("total_cccfa").setEstilo("font-size: 12px;font-weight: bold;");
        tab_facturas_x_cobrar.getColumna("saldo_x_pagar").alinearDerecha();
        tab_facturas_x_cobrar.getColumna("saldo_x_pagar").setEstilo("font-size: 12px;font-weight: bold;color:red");
        tab_facturas_x_cobrar.getColumna("ide_cnccc").setFiltroContenido();
        tab_facturas_x_cobrar.setLectura(true);
        tab_facturas_x_cobrar.setRows(15);
        tab_facturas_x_cobrar.setColumnaSuma("total_cccfa,saldo_x_pagar");
        tab_facturas_x_cobrar.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_facturas_x_cobrar);

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

        tab_datos_grafico = new Tabla();
        tab_datos_grafico.setId("tab_datos_grafico");
        tab_datos_grafico.setSql(ser_factura.getSqlTotalVentasMensuales(String.valueOf(com_pto_emision.getValue()), String.valueOf(com_periodo.getValue())));
        tab_datos_grafico.setLectura(true);
        tab_datos_grafico.setColumnaSuma("num_facturas,ventas12,ventas0,iva,total");
        tab_datos_grafico.getColumna("num_facturas").alinearDerecha();
        tab_datos_grafico.getColumna("ventas12").alinearDerecha();
        tab_datos_grafico.getColumna("ventas0").alinearDerecha();
        tab_datos_grafico.getColumna("iva").alinearDerecha();
        tab_datos_grafico.getColumna("total").alinearDerecha();
        tab_datos_grafico.dibujar();

        Grid gri_opciones = new Grid();
        gri_opciones.setColumns(2);
        gri_opciones.getChildren().add(new Etiqueta("<strong>PERÍODO :</strong>"));
        gri_opciones.getChildren().add(com_periodo);
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.getChildren().add(gri_opciones);
        pat_panel.setPanelTabla(tab_datos_grafico);

        Grid gri = new Grid();
        gri.setWidth("100%");
        gri.setColumns(2);
        gpa_facturas.agregarSerie(tab_datos_grafico, "nombre_gemes", "num_facturas");
        gri.getChildren().add(pat_panel);
        gri.getChildren().add(gpa_facturas);
        grupo.getChildren().add(gri);

        gca_facturas.setTitulo("VENTAS MENSUALES");
        gca_facturas.agregarSerie(tab_datos_grafico, "nombre_gemes", "total", "VENTAS " + String.valueOf(com_periodo.getValue()));
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

        tab_rep_ventas = new Tabla();
        tab_rep_ventas.setId("tab_rep_ventas");
        tab_rep_ventas.setSql(ser_factura.getSqlVentasMensuales(com_pto_emision.getValue() + "", com_mes.getValue() + "", com_periodo.getValue() + ""));
        tab_rep_ventas.getColumna("ide_cccfa").setVisible(false);
        tab_rep_ventas.getColumna("observacion_cccfa").setVisible(false);
        tab_rep_ventas.setRows(15);

        tab_rep_ventas.setLectura(true);
        tab_rep_ventas.getColumna("NOM_GEPER").setLongitud(100);
        tab_rep_ventas.setColumnaSuma("ventas12,ventas0,valor_iva_cccfa,total_cccfa");
        tab_rep_ventas.getColumna("ventas12").alinearDerecha();
        tab_rep_ventas.getColumna("ventas0").alinearDerecha();
        tab_rep_ventas.getColumna("valor_iva_cccfa").alinearDerecha();
        tab_rep_ventas.getColumna("total_cccfa").alinearDerecha();
        tab_rep_ventas.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_rep_ventas);

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
        tab_facturas_fe = new Tabla();
        tab_facturas_fe.setId("tab_facturas_fe");

        tab_facturas_fe.setSql(ser_comprobante.getSqlFacturasElectronicas(cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha(), String.valueOf(com_estados_fe.getValue())));

        tab_facturas_fe.getColumna("ide_srcom").setVisible(false);
        tab_facturas_fe.getColumna("ide_cccfa").setVisible(false);
        tab_facturas_fe.getColumna("SECUENCIAL_SRCOM").setFiltroContenido();

        tab_facturas_fe.setLectura(true);
        tab_facturas_fe.setRows(15);
        tab_facturas_fe.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_facturas_fe);

        grupo.getChildren().add(bar_menu);
        grupo.getChildren().add(pat_panel);

        mep_menu.dibujar(8, "FACTURAS ELECTRÓNICAS", grupo);
    }

    public void abrirVerFactura() {
        fcc_factura.verFactura(tab_facturas.getValorSeleccionado());
        fcc_factura.dibujar();
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
        if (rep_reporte.getReporteSelecionado().equals("Facturas")) {
            if (rep_reporte.isVisible()) {
                parametro = new HashMap();
                rep_reporte.cerrar();
                parametro.put("ide_cccfa", Long.parseLong(tab_facturas.getValorSeleccionado()));
                sel_rep.setSeleccionFormatoReporte(parametro, rep_reporte.getPath());
                sel_rep.dibujar();
                utilitario.addUpdate("rep_reporte,sel_rep");
            }
        } else if (rep_reporte.getReporteSelecionado().equals("Comprobante Contabilidad")) {
            if (rep_reporte.isVisible()) {
                if (tab_facturas.getValor("ide_cnccc") != null && !tab_facturas.getValor("ide_cnccc").isEmpty()) {
                    parametro = new HashMap();
                    rep_reporte.cerrar();
                    parametro.put("ide_cnccc", Long.parseLong(tab_facturas.getValor("ide_cnccc")));
                    parametro.put("ide_cnlap_haber", utilitario.getVariable("p_con_lugar_haber"));
                    parametro.put("ide_cnlap_debe", utilitario.getVariable("p_con_lugar_debe"));
                    sel_rep.setSeleccionFormatoReporte(parametro, rep_reporte.getPath());
                    sel_rep.dibujar();
                    utilitario.addUpdate("rep_reporte,sel_rep");
                } else {
                    utilitario.agregarMensajeInfo("Comprobante de Contabilidad", "La factura seleccionada no tiene Comprobante de Contabilidad");
                }
            }
        } else if (rep_reporte.getReporteSelecionado().equals("Facturas A6")) {
            if (rep_reporte.isVisible()) {
                parametro = new HashMap();
                rep_reporte.cerrar();
                parametro.put("ide_cccfa", Long.parseLong(tab_facturas.getValorSeleccionado()));
                sel_rep.setSeleccionFormatoReporte(parametro, rep_reporte.getPath());
                sel_rep.dibujar();
                utilitario.addUpdate("rep_reporte,sel_rep");
            }
        }
    }

    public void actualizarFacturas() {
        if (mep_menu.getOpcion() == 1) {
            tab_facturas.setSql(ser_factura.getSqlFacturas(com_pto_emision.getValue() + "", cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha()));
            tab_facturas.ejecutarSql();
        } else if (mep_menu.getOpcion() == 2) {
            tab_facturas_no_conta.setSql(ser_factura.getSqlFacturasNoContabilizadas(com_pto_emision.getValue() + "", cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha()));
            tab_facturas_no_conta.ejecutarSql();
        } else if (mep_menu.getOpcion() == 3) {
            tab_facturas_anuladas.setSql(ser_factura.getSqlFacturasAnuladas(com_pto_emision.getValue() + "", cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha()));
            tab_facturas_anuladas.ejecutarSql();
        } else if (mep_menu.getOpcion() == 4) {
            tab_facturas_x_cobrar.setSql(ser_factura.getSqlFacturasPorCobrar(com_pto_emision.getValue() + "", cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha()));
            tab_facturas_x_cobrar.ejecutarSql();
        } else if (mep_menu.getOpcion() == 5) {
            tab_datos_grafico.setSql(ser_factura.getSqlTotalVentasMensuales(com_pto_emision.getValue() + "", String.valueOf(com_periodo.getValue())));
            tab_datos_grafico.ejecutarSql();
            gca_facturas.limpiar();
            gca_facturas.agregarSerie(tab_datos_grafico, "nombre_gemes", "total", "VENTAS " + String.valueOf(com_periodo.getValue()));
            gpa_facturas.limpiar();
            gpa_facturas.agregarSerie(tab_datos_grafico, "nombre_gemes", "num_facturas");
            utilitario.addUpdate("gca_facturas,gpa_facturas");
        } else if (mep_menu.getOpcion() == 7) {
            tab_rep_ventas.setSql(ser_factura.getSqlVentasMensuales(com_pto_emision.getValue() + "", com_mes.getValue() + "", com_periodo.getValue() + ""));
            tab_rep_ventas.ejecutarSql();
        } else if (mep_menu.getOpcion() == 8) {
            tab_facturas_fe.setSql(ser_comprobante.getSqlFacturasElectronicas(cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha(), String.valueOf(com_estados_fe.getValue())));
            tab_facturas_fe.ejecutarSql();
        }

    }

    public void generarPDF() {
        if (tab_facturas_fe.getValorSeleccionado() != null) {
            ser_comprobante.generarPDF(tab_facturas_fe.getValorSeleccionado());
            vipdf_comprobante.setVisualizarPDFUsuario();
            vipdf_comprobante.dibujar();
        } else {
            utilitario.agregarMensajeInfo("Seleccione una Factura Electrónica", "");
        }
    }

    public void descargarXML() {
        if (tab_facturas_fe.getValorSeleccionado() != null) {
            ser_comprobante.generarXML(tab_facturas_fe.getValorSeleccionado());
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
                tab_facturas.setFilaActual(fcc_factura.getTab_cab_factura().getValor("ide_cccfa"));
                utilitario.addUpdate("com_pto_emision,tab_facturas");
            }
        }
    }

    @Override
    public void eliminar() {

    }

    public Tabla getTab_facturas() {
        return tab_facturas;
    }

    public void setTab_facturas(Tabla tab_facturas) {
        this.tab_facturas = tab_facturas;
    }

    public FacturaCxC getFcc_factura() {
        return fcc_factura;
    }

    public void setFcc_factura(FacturaCxC fcc_factura) {
        this.fcc_factura = fcc_factura;
    }

    public Tabla getTab_facturas_no_conta() {
        return tab_facturas_no_conta;
    }

    public void setTab_facturas_no_conta(Tabla tab_facturas_no_conta) {
        this.tab_facturas_no_conta = tab_facturas_no_conta;
    }

    public Tabla getTab_facturas_anuladas() {
        return tab_facturas_anuladas;
    }

    public void setTab_facturas_anuladas(Tabla tab_facturas_anuladas) {
        this.tab_facturas_anuladas = tab_facturas_anuladas;
    }

    public Tabla getTab_facturas_x_cobrar() {
        return tab_facturas_x_cobrar;
    }

    public void setTab_facturas_x_cobrar(Tabla tab_facturas_x_cobrar) {
        this.tab_facturas_x_cobrar = tab_facturas_x_cobrar;
    }

    public GraficoCartesiano getGca_facturas() {
        return gca_facturas;
    }

    public void setGca_facturas(GraficoCartesiano gca_facturas) {
        this.gca_facturas = gca_facturas;
    }

    public Tabla getTab_datos_grafico() {
        return tab_datos_grafico;
    }

    public void setTab_datos_grafico(Tabla tab_datos_grafico) {
        this.tab_datos_grafico = tab_datos_grafico;
    }

    public Tabla getTab_rep_ventas() {
        return tab_rep_ventas;
    }

    public void setTab_rep_ventas(Tabla tab_rep_ventas) {
        this.tab_rep_ventas = tab_rep_ventas;
    }

    public Tabla getTab_facturas_fe() {
        return tab_facturas_fe;
    }

    public void setTab_facturas_fe(Tabla tab_facturas_fe) {
        this.tab_facturas_fe = tab_facturas_fe;
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

}
