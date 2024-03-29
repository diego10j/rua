/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_cuentas_x_cobrar;

import componentes.AsientoContable;
import componentes.FacturaCxC;
import componentes.Retencion;
import dj.comprobantes.offline.enums.EstadoComprobanteEnum;
import dj.comprobantes.offline.enums.TipoComprobanteEnum;
import framework.aplicacion.Fila;
import framework.aplicacion.TablaGenerica;
import framework.componentes.Barra;
import framework.componentes.Boton;
import framework.componentes.BotonesCombo;
import framework.componentes.Calendario;
import framework.componentes.Combo;
import framework.componentes.Confirmar;
import framework.componentes.Dialogo;
import framework.componentes.Espacio;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.Grupo;
import framework.componentes.ItemMenu;
import framework.componentes.Link;
import framework.componentes.Mascara;
import framework.componentes.Mensaje;

import framework.componentes.MenuPanel;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionCalendario;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import framework.componentes.Texto;
import framework.componentes.VisualizarPDF;
import framework.componentes.graficos.GraficoCartesiano;
import framework.componentes.graficos.GraficoPastel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.faces.event.ActionEvent;
import org.primefaces.component.fieldset.Fieldset;
import org.primefaces.component.panel.Panel;
import org.primefaces.component.separator.Separator;
import servicios.ceo.ServicioComprobanteElectronico;
import servicios.cuentas_x_cobrar.ServicioCuentasCxC;

import sistema.aplicacion.Pantalla;

/**
 *
 * @author dfjacome
 */
public class pre_facturasCxCPVenta extends Pantalla {

    private final MenuPanel mep_menu = new MenuPanel();

    @EJB
    private final ServicioCuentasCxC ser_factura = (ServicioCuentasCxC) utilitario.instanciarEJB(ServicioCuentasCxC.class);

    private final Combo com_pto_emision = new Combo();
    private final Calendario cal_fecha_inicio = new Calendario();
    private final Calendario cal_fecha_fin = new Calendario();
    private FacturaCxC fcc_factura = new FacturaCxC();

    private Tabla tab_tabla;
    private Tabla tab_tabla1;

    private GraficoCartesiano gca_facturas;
    private GraficoPastel gpa_facturas;
    private Combo com_periodo;
    private Combo com_mes;

    private VisualizarPDF vipdf_comprobante = new VisualizarPDF();

    private Reporte rep_reporte = new Reporte();
    private SeleccionFormatoReporte sel_rep = new SeleccionFormatoReporte();

    private AsientoContable asc_asiento = new AsientoContable();
    private Mascara mas_secuencial;

    private Confirmar con_confirma = new Confirmar();

    private Etiqueta eti1 = new Etiqueta();
    private Retencion ret_retencion = new Retencion();

    @EJB
    private final ServicioComprobanteElectronico ser_facElect = (ServicioComprobanteElectronico) utilitario.instanciarEJB(ServicioComprobanteElectronico.class);

    //private Radio rad_facelectronica = new Radio();
    private final Mensaje men_factura = new Mensaje();

    private final Grid gri_dashboard = new Grid();

    private final Dialogo dia_correo = new Dialogo();
    private final Texto tex_correo = new Texto();

    private Combo com_vendedor;

    //Modifica vendedor
    private Dialogo dia_vendedor = new Dialogo();
    private SeleccionCalendario sec_rango_reporte = new SeleccionCalendario();
    private SeleccionTabla sel_sucursales = new SeleccionTabla();

    public pre_facturasCxCPVenta() {

        bar_botones.quitarBotonsNavegacion();
        bar_botones.quitarBotonGuardar();
        bar_botones.quitarBotonEliminar();
        bar_botones.agregarReporte();

        sec_rango_reporte.setId("sec_rango_reporte");
        sec_rango_reporte.getBot_aceptar().setMetodo("aceptarReporte");
        sec_rango_reporte.setMultiple(true);
        agregarComponente(sec_rango_reporte);

        com_pto_emision.setId("com_pto_emision");
        com_pto_emision.setCombo(ser_factura.getSqlPuntosEmisionFacturas());
        com_pto_emision.setMetodo("actualizarFacturas");
        com_pto_emision.eliminarVacio();
        bar_botones.agregarComponente(new Etiqueta("PUNTO DE EMISIÓN:"));
        bar_botones.agregarComponente(com_pto_emision);
        bar_botones.agregarSeparador();
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

        //mep_menu.agregarSubMenu("FACTURACIÓN ELECTRÓNICA");
        //mep_menu.agregarItem("Configuración", "dibujarConfiguraFE", "ui-icon-wrench");
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
        ret_retencion.setId("ret_retencion");
        ret_retencion.getBot_aceptar().setMetodo("guardar");
        agregarComponente(ret_retencion);
        sel_sucursales.setId("sel_sucursales");
        sel_sucursales.setTitle("SUCURSALES");
        sel_sucursales.setSeleccionTabla("select ide_sucu,nom_sucu from sis_sucursal ORDER BY nom_sucu", "ide_sucu");
        sel_sucursales.getBot_aceptar().setMetodo("aceptarReporte");
        agregarComponente(sel_sucursales);
        men_factura.setId("men_factura");
        utilitario.getPantalla().getChildren().add(men_factura);

        dia_correo.setId("dia_correo");
        dia_correo.setTitle("REENVIAR FACTURA ELECTRONICA AL CLIENTE");
        dia_correo.setWidth("35%");
        dia_correo.setHeight("17%");
        dia_correo.getBot_aceptar().setMetodo("aceptarReenviar");
        tex_correo.setStyle("width:" + (dia_correo.getAnchoPanel() - 35) + "px");
        Grid gri = new Grid();
        gri.setStyle("width:" + (dia_correo.getAnchoPanel() - 5) + "px; height:" + dia_correo.getAltoPanel() + "px;overflow:auto;display:block;vertical-align:middle;");
        gri.getChildren().add(new Etiqueta("<strong> CORREO ELECTRÓNICO: </strong>"));
        gri.getChildren().add(tex_correo);
        dia_correo.setDialogo(gri);
        agregarComponente(dia_correo);

        dia_vendedor.setId("dia_vendedor");
        dia_vendedor.setTitle("MODIFICAR VENDEDOR");
        dia_vendedor.setWidth("35%");
        dia_vendedor.setHeight("40%");
        dia_vendedor.getBot_aceptar().setMetodo("aceptarModificarVendedor");
        agregarComponente(dia_vendedor);

    }

    public void actualizarUtilidad() {
//        tab_tabla.setSql(ser_integra.getSqlUtilidadVentas(String.valueOf(com_vendedor.getValue()), String.valueOf(com_periodo.getValue()), String.valueOf(com_mes.getValue())));
//        tab_tabla.ejecutarSql();
    }

    private void dibujarDashboard() {
        int num_pendientes = 0;
        int num_recibidas = 0;
        int num_rechazadas = 0;
        int num_devueltas = 0;
        int num_autorizadas = 0;
        int num_no_autorizadas = 0;
        TablaGenerica tg = utilitario.consultar(ser_facElect.getSqlTotalComprobantesPorEstado(cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha(), TipoComprobanteEnum.FACTURA, com_pto_emision.getValue() + ""));
        if (tg.isEmpty() == false) {
            for (int i = 0; i < tg.getTotalFilas(); i++) {
                if (tg.getValor(i, "ide_sresc").equals(String.valueOf(EstadoComprobanteEnum.PENDIENTE.getCodigo()))) {
                    num_pendientes = Integer.parseInt(tg.getValor(i, "contador"));
                } else if (tg.getValor(i, "ide_sresc").equals(String.valueOf(EstadoComprobanteEnum.RECIBIDA.getCodigo()))) {
                    num_recibidas = Integer.parseInt(tg.getValor(i, "contador"));
                } else if (tg.getValor(i, "ide_sresc").equals(String.valueOf(EstadoComprobanteEnum.DEVUELTA.getCodigo()))) {
                    num_devueltas = Integer.parseInt(tg.getValor(i, "contador"));
                } else if (tg.getValor(i, "ide_sresc").equals(String.valueOf(EstadoComprobanteEnum.RECHAZADO.getCodigo()))) {
                    num_rechazadas = Integer.parseInt(tg.getValor(i, "contador"));
                } else if (tg.getValor(i, "ide_sresc").equals(String.valueOf(EstadoComprobanteEnum.AUTORIZADO.getCodigo()))) {
                    num_autorizadas = Integer.parseInt(tg.getValor(i, "contador"));
                } else if (tg.getValor(i, "ide_sresc").equals(String.valueOf(EstadoComprobanteEnum.NOAUTORIZADO.getCodigo()))) {
                    num_no_autorizadas = Integer.parseInt(tg.getValor(i, "contador"));
                }
            }
        }

        gri_dashboard.getChildren().clear();
        Panel p1 = new Panel();
        p1.setStyle("margin-left: 2px;");
        Grid g1 = new Grid();
        g1.setWidth("100%");
        g1.setColumns(2);
        g1.setHeader(new Etiqueta("<span style='font-size:11px;' class='text-navy'>PENDIENTES </span>"));
        Link l1 = new Link();
        l1.setMetodo("filtrarPendientes");
        l1.getChildren().add(new Etiqueta("<i class='fa fa-clock-o fa-4x text-navy'></i>"));
        g1.getChildren().add(l1);
        g1.getChildren().add(new Etiqueta("<span style='font-size:20px; text-align: left;'>" + num_pendientes + "</span>"));
        p1.getChildren().add(g1);
        gri_dashboard.getChildren().add(p1);

        Panel p2 = new Panel();
        p2.setStyle("margin-left: 2px;");
        Grid g2 = new Grid();
        g2.setColumns(2);
        g2.setWidth("100%");
        g2.setHeader(new Etiqueta("<span style='font-size:11px;' class='text-blue'>RECIBIDAS </span>"));
        Link l2 = new Link();
        l2.setMetodo("filtrarRecibidas");
        l2.getChildren().add(new Etiqueta("<i class='fa fa-cloud-upload fa-4x text-blue'></i>"));
        g2.getChildren().add(l2);
        g2.getChildren().add(new Etiqueta("<span style='font-size:20px; text-align: left;'>" + num_recibidas + "</span>"));
        p2.getChildren().add(g2);
        gri_dashboard.getChildren().add(p2);

        Panel p3 = new Panel();
        p3.setStyle("margin-left: 2px;");
        Grid g3 = new Grid();
        g3.setWidth("100%");
        g3.setColumns(2);
        g3.setHeader(new Etiqueta("<span style='font-size:11px;' class='text-orange'>DEVUELTAS </span>"));
        Link l3 = new Link();
        l3.setMetodo("filtrarDevueltas");
        l3.getChildren().add(new Etiqueta("<i class='fa fa-arrow-circle-left fa-4x text-orange'></i>"));
        g3.getChildren().add(l3);
        g3.getChildren().add(new Etiqueta("<span style='font-size:20px; text-align: left;'>" + num_devueltas + "</span>"));
        p3.getChildren().add(g3);

        gri_dashboard.getChildren().add(p3);
        Panel p4 = new Panel();
        p4.setStyle("margin-left: 2px;");
        Grid g4 = new Grid();
        g4.setColumns(2);
        g4.setWidth("100%");
        g4.setHeader(new Etiqueta("<span style='font-size:11px;' class='text-red'>RECHAZADAS </span>"));
        Link l4 = new Link();
        l4.setMetodo("filtrarRechazadas");
        l4.getChildren().add(new Etiqueta("<i class='fa fa-times-circle fa-4x text-red'></i>"));
        g4.getChildren().add(l4);
        g4.getChildren().add(new Etiqueta("<span style='font-size:20px; text-align: left;'>" + num_rechazadas + "</span>"));
        p4.getChildren().add(g4);
        gri_dashboard.getChildren().add(p4);

        Panel p5 = new Panel();
        p5.setStyle("margin-left: 2px;");
        Grid g5 = new Grid();
        g5.setColumns(2);
        g5.setWidth("100%");
        g5.setHeader(new Etiqueta("<span style='font-size:11px;' class='text-green'>AUTORIZADAS </span>"));
        Link l5 = new Link();
        l5.setMetodo("filtrarAutorizadas");
        l5.getChildren().add(new Etiqueta("<i class='fa fa-check-circle fa-4x text-green'></i>"));
        g5.getChildren().add(l5);
        g5.getChildren().add(new Etiqueta("<span style='font-size:20px; text-align: left;'>" + num_autorizadas + "</span>"));
        p5.getChildren().add(g5);
        gri_dashboard.getChildren().add(p5);
        Panel p6 = new Panel();
        p6.setStyle("margin-left: 2px;");
        Grid g6 = new Grid();
        g6.setWidth("100%");
        g6.setColumns(2);
        g6.setHeader(new Etiqueta("<span style='font-size:11px;' class='text-red'> NO AUTORIZADAS </span>"));
        Link l6 = new Link();
        l6.setMetodo("filtrarNoAutorizadas");
        l6.getChildren().add(new Etiqueta("<i class='fa fa-minus-circle fa-4x text-red'></i>"));
        g6.getChildren().add(l6);

        g6.getChildren().add(new Etiqueta("<span style='font-size:20px; text-align: left;'>" + num_no_autorizadas + "</span>"));
        p6.getChildren().add(g6);
        gri_dashboard.getChildren().add(p6);

        if (ser_factura.isFacturaElectronica(String.valueOf(com_pto_emision.getValue()))) {
            gri_dashboard.setRendered(true);
        } else {
            gri_dashboard.setRendered(false);
        }
    }

    public void filtrarPendientes() {
        tab_tabla.setSql(ser_factura.getSqlFacturasElectronicasPorEstado(com_pto_emision.getValue() + "", cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha(), EstadoComprobanteEnum.PENDIENTE));
        tab_tabla.ejecutarSql();
    }

    public void filtrarRecibidas() {
        tab_tabla.setSql(ser_factura.getSqlFacturasElectronicasPorEstado(com_pto_emision.getValue() + "", cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha(), EstadoComprobanteEnum.RECIBIDA));
        tab_tabla.ejecutarSql();
    }

    public void filtrarDevueltas() {
        tab_tabla.setSql(ser_factura.getSqlFacturasElectronicasPorEstado(com_pto_emision.getValue() + "", cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha(), EstadoComprobanteEnum.DEVUELTA));
        tab_tabla.ejecutarSql();
    }

    public void filtrarRechazadas() {
        tab_tabla.setSql(ser_factura.getSqlFacturasElectronicasPorEstado(com_pto_emision.getValue() + "", cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha(), EstadoComprobanteEnum.RECHAZADO));
        tab_tabla.ejecutarSql();
    }

    public void filtrarAutorizadas() {
        tab_tabla.setSql(ser_factura.getSqlFacturasElectronicasPorEstado(com_pto_emision.getValue() + "", cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha(), EstadoComprobanteEnum.AUTORIZADO));
        tab_tabla.ejecutarSql();
    }

    public void filtrarNoAutorizadas() {
        tab_tabla.setSql(ser_factura.getSqlFacturasElectronicasPorEstado(com_pto_emision.getValue() + "", cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha(), EstadoComprobanteEnum.NOAUTORIZADO));
        tab_tabla.ejecutarSql();
    }

    public void dibujarFacturas() {

        Barra bar_menu = new Barra();
        bar_menu.setId("bar_menu");
        bar_menu.limpiar();
        Boton bot_ver = new Boton();
        bot_ver.setValue("Ver");
        bot_ver.setTitle("Ver Factura");
        bot_ver.setMetodo("abrirVerFactura");
        bot_ver.setIcon("ui-icon-search");
        bar_menu.agregarComponente(bot_ver);

        Boton bot_anular = new Boton();
        bot_anular.setValue("Anular");
        bot_anular.setTitle("Anular Factura");
        bot_anular.setMetodo("abrirAnularFactura");
        bot_anular.setIcon("ui-icon-cancel");
        bar_menu.agregarComponente(bot_anular);
        Boton bot_retencion = new Boton();
        bot_retencion.setValue("Retención");
        bot_retencion.setTitle("Ingresar Comprobante Retención");
        bot_retencion.setMetodo("dibujarRetencion");
        bot_retencion.setIcon("ui-icon-note");
        bar_menu.agregarBoton(bot_retencion);

        if (ser_factura.isFacturaElectronica(String.valueOf(com_pto_emision.getValue()))) {
            bar_menu.agregarSeparador();

            Boton bot_enviar = new Boton();
            bot_enviar.setValue("Enviar al SRI");
            bot_enviar.setMetodo("enviarSRI");
            bot_enviar.setIcon("ui-icon-signal-diag");
            bar_menu.agregarBoton(bot_enviar);

            Boton bot_ride = new Boton();
            bot_ride.setValue("Imprimir");
            bot_ride.setTitle("Imprimir RIDE");
            bot_ride.setMetodo("abrirRIDE");
            bot_ride.setIcon("ui-icon-print");
            bar_menu.agregarBoton(bot_ride);

            Boton bot_reenviar = new Boton();
            bot_reenviar.setValue("Reenviar");
            bot_reenviar.setTitle("Enviar nuevamente al correo del cliente");
            bot_reenviar.setMetodo("reenviarFactura");
            bot_reenviar.setIcon("ui-icon-mail-closed");
            bar_menu.agregarBoton(bot_reenviar);
         
        

        }

        tab_tabla = new Tabla();
        tab_tabla.setId("tab_tabla");
        tab_tabla.setSql(ser_factura.getSqlFacturas(com_pto_emision.getValue() + "", cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha()));
        tab_tabla.setCampoPrimaria("ide_cccfa");
        tab_tabla.getColumna("ide_cccfa").setVisible(false);
        tab_tabla.getColumna("ide_cncre").setVisible(false);
        tab_tabla.getColumna("ide_geper").setVisible(false);
        tab_tabla.getColumna("direccion_cccfa").setVisible(false);
        tab_tabla.getColumna("fecha_trans_cccfa").setVisible(false);
        tab_tabla.getColumna("ide_ccefa").setVisible(false);
        tab_tabla.getColumna("nombre_ccefa").setFiltroContenido();
        tab_tabla.getColumna("fecha_emisi_cccfa").setNombreVisual("FECHA");
        tab_tabla.getColumna("nombre_ccefa").setVisible(true);
        tab_tabla.getColumna("nombre_ccefa").setNombreVisual("ESTADO");
        tab_tabla.getColumna("secuencial_cccfa").setFiltroContenido();
        tab_tabla.getColumna("secuencial_cccfa").setNombreVisual("SECUENCIAL");
        tab_tabla.getColumna("nom_geper").setFiltroContenido();
        tab_tabla.getColumna("nom_geper").setNombreVisual("CLIENTE");
        tab_tabla.getColumna("identificac_geper").setFiltroContenido();
        tab_tabla.getColumna("identificac_geper").setNombreVisual("IDENTIFICACIÓN");
        tab_tabla.getColumna("ide_cnccc").setFiltroContenido();
        tab_tabla.getColumna("ide_cnccc").setNombreVisual("N. ASIENTO");
        tab_tabla.getColumna("IDE_CNCCC").setLink();
        tab_tabla.getColumna("IDE_CNCCC").setMetodoChange("abrirAsiento");
        tab_tabla.getColumna("IDE_CNCCC").alinearCentro();
        tab_tabla.getColumna("ventas0").alinearDerecha();
        tab_tabla.getColumna("ventas0").setNombreVisual("VENTAS 0");
        tab_tabla.getColumna("ventas12").alinearDerecha();
        tab_tabla.getColumna("ventas12").setNombreVisual("VENTAS IVA");
        tab_tabla.getColumna("valor_iva_cccfa").alinearDerecha();
        tab_tabla.getColumna("valor_iva_cccfa").setNombreVisual("IVA");
        tab_tabla.getColumna("total_cccfa").alinearDerecha();
        tab_tabla.getColumna("total_cccfa").setEstilo("font-size: 12px;font-weight: bold;");
        tab_tabla.getColumna("total_cccfa").setNombreVisual("TOTAL");
        if (ser_factura.isFacturaElectronica(String.valueOf(com_pto_emision.getValue()))) {
            tab_tabla.getColumna("ide_srcom").setVisible(false);
        }
        tab_tabla.setRows(15);
        tab_tabla.setLectura(true);
        //COLOR VERDE FACTURAS NO CONTABILIZADAS
        //COLOR ROJO FACTURAS ANULADAS
        /// ok tab_tabla.setValueExpression("rowStyleClass", "fila.campos[3] eq '" + utilitario.getVariable("p_cxc_estado_factura_anulada") + "' ? 'text-red' : fila.campos[2] eq null  ? 'text-green' : null");
        tab_tabla.setValueExpression("rowStyleClass", "fila.campos[3] eq '" + utilitario.getVariable("p_cxc_estado_factura_anulada") + "' ? 'text-red' : null");
        tab_tabla.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_tabla);

        ItemMenu itemedita = new ItemMenu();
        itemedita.setValue("Modificar Vendedor");
        itemedita.setIcon("ui-icon-pencil");
        itemedita.setMetodo("abrirModificarVendedor");
        pat_panel.getMenuTabla().getChildren().add(itemedita);

        Grupo gru = new Grupo();

        dibujarDashboard();
        Grupo gr = new Grupo();
        gr.getChildren().add(new Etiqueta("<div align='center'>"));
        gri_dashboard.setId("gri_dashboard");
        gri_dashboard.setWidth("100%");
        gri_dashboard.setColumns(6);
        gr.getChildren().add(gri_dashboard);
        gr.getChildren().add(new Etiqueta("</div>"));
        gru.getChildren().add(gr);

        gru.getChildren().add(bar_menu);
        gru.getChildren().add(pat_panel);

        mep_menu.dibujar(1, "LISTADO DE FACTURAS", gru);
        //metodo of mauricio
        utilitario.buscarPermisosObjetos();
    }

    public void abrirModificarVendedor() {

        if (tab_tabla.getValor("ide_cccfa") != null) {
            com_vendedor = new Combo();
            com_vendedor.setCombo(ser_factura.getSqlComboVendedores());
            Grid gri = new Grid();

            //  gri1.getChildren().add(new Etiqueta("<strong>CLIENTE : </strong><span style='color:red;font-weight: bold;'>*</span>"));
            //    gri1.getChildren().add(new Etiqueta("<strong>&nbsp;&nbsp;&nbsp;FECHA : </strong><span style='color:red;font-weight: bold;'>*</span>"));
            gri.setColumns(2);
            gri.getChildren().add(new Etiqueta("<strong>FACTURA N. : </strong>"));
            gri.getChildren().add(new Etiqueta(tab_tabla.getValor("secuencial_cccfa")));
            gri.getChildren().add(new Etiqueta("<strong>CLIENTE : </strong>"));
            gri.getChildren().add(new Etiqueta(tab_tabla.getValor("nom_geper")));
            gri.getChildren().add(new Etiqueta("<strong>FECHA : </strong>"));
            gri.getChildren().add(new Etiqueta(tab_tabla.getValor("fecha_emisi_cccfa")));
            gri.getChildren().add(new Etiqueta("<strong>VENDEDOR : </strong>"));
            gri.getChildren().add(new Etiqueta(ser_factura.getNombreVendedorFactura(tab_tabla.getValor("ide_cccfa"))));

            Grid gri1 = new Grid();
            gri1.setColumns(2);
            gri1.getChildren().add(new Etiqueta("<strong>NUEVO VENDEDOR : </strong>"));
            gri1.getChildren().add(com_vendedor);

            Grid g = new Grid();
            g.getChildren().add(gri);
            g.getChildren().add(new Separator());
            g.getChildren().add(gri1);

            dia_vendedor.setTitle("MODIFICAR VENDEDOR - FACTURA N. " + tab_tabla.getValor("secuencial_cccfa"));
            dia_vendedor.getGri_cuerpo().getChildren().clear();
            dia_vendedor.setDialogo(g);
            dia_vendedor.dibujar();
        } else {
            utilitario.agregarMensajeInfo("Seleccione una factura", "");
        }

    }

    public void aceptarModificarVendedor() {
        if (com_vendedor.getValue() != null) {
            ser_factura.actualizarVendedorFactura(tab_tabla.getValor("ide_cccfa"), String.valueOf(com_vendedor.getValue()));
            dia_vendedor.cerrar();
            utilitario.agregarMensaje("Se guardo correctamente", "");
        } else {
            utilitario.agregarMensajeInfo("Seleccione un vendedor", "");
        }
    }

    public void reenviarFactura() {
        //Valida que la factura este AUTORIZADA
        if (tab_tabla.getValor("ide_cccfa") != null) {
            if (tab_tabla.getValor("ide_srcom") != null) {
                if (tab_tabla.getValor("nombre_ccefa") != null && tab_tabla.getValor("nombre_ccefa").equals(EstadoComprobanteEnum.AUTORIZADO.getDescripcion())) {
                    dia_correo.dibujar();
                } else {
                    utilitario.agregarMensajeError("No se puede reenviar la factura", "La factura seleccionada debe estar en estado AUTORIZADO");
                }
            } else {
                utilitario.agregarMensajeInfo("La factura seleccionada no es electrónica", "");
            }
        } else {
            utilitario.agregarMensajeInfo("Seleccione una factura", "");
        }
    }

    public void aceptarReenviar() {
        if (utilitario.isCorreoValido(String.valueOf(tex_correo.getValue()))) {
            ser_facElect.reenviarComprobante(String.valueOf(tex_correo.getValue()), tab_tabla.getValor("ide_srcom"));
            dia_correo.cerrar();
            tex_correo.setValue(null);
        } else {
            utilitario.agregarMensajeError("Correo electrónico no válido", "");
        }
    }

    public void abrirRIDE() {
        if (tab_tabla.getValor("ide_cccfa") != null) {
            if (tab_tabla.getValor("ide_srcom") != null) {
                if (tab_tabla.getValor("nombre_ccefa") != null && !tab_tabla.getValor("nombre_ccefa").equals(EstadoComprobanteEnum.ANULADO.getDescripcion())) {
                    fcc_factura.visualizarRide(tab_tabla.getValor("ide_srcom"));
                } else {
                    utilitario.agregarMensajeError("No se puede Visualizar el Comprobate", "La Factura seleccionada se encuentara ANULADA");
                }
            } else {
                utilitario.agregarMensajeInfo("La factura seleccionada no es electrónica", "");
            }
        } else {
            utilitario.agregarMensajeInfo("Seleccione una factura", "");
        }
    }

    public void enviarSRI() {
        if (tab_tabla.getValor("ide_cccfa") != null) {
            //Valida que se encuentre en estado PENDIENTE o RECIBIDA
            if ((tab_tabla.getValor("nombre_ccefa")) != null && (tab_tabla.getValor("nombre_ccefa").equals(EstadoComprobanteEnum.PENDIENTE.getDescripcion())) || tab_tabla.getValor("nombre_ccefa").equals(EstadoComprobanteEnum.RECIBIDA.getDescripcion())) {
                String mensaje = ser_facElect.enviarComprobante(tab_tabla.getValor("clave_acceso"));

                String aux = tab_tabla.getValorSeleccionado();
                dibujarFacturas();
                tab_tabla.setFilaActual(aux);
                tab_tabla.calcularPaginaActual();

                if (mensaje.isEmpty()) {
                    String mensje = "<p> FACTURA NRO. " + tab_tabla.getValor("secuencial_cccfa") + " ";
                    mensje += "</br>AMBIENTE : <strong>" + (utilitario.getVariable("p_sri_ambiente_comp_elect").equals("1") ? "PRUEBAS" : "PRODUCCIÓN") + "</strong></p>";  //********variable ambiente facturacion electronica                    
                    mensje += "<p>ESTADO : <strong>" + tab_tabla.getValor("nombre_ccefa") + "</strong></p>";
                    mensje += "<p>NÚMERO DE AUTORIZACION : <span style='font-size:12px;font-weight: bold;'>" + tab_tabla.getValor("CLAVE_ACCESO") + "</span> </p>";
                    men_factura.setMensajeExito("FACTURACIÓN ELECTRÓNICA AUTORIZADA", mensje);
                    men_factura.dibujar();
                } else {
                    utilitario.agregarMensajeError(mensaje, "");
                }

            } else {
                utilitario.agregarMensajeInfo("La Factura seleccionada no se encuentra en estado PENDIENTE o RECIBIDA", "");
            }
        } else {
            utilitario.agregarMensajeInfo("Seleccione una factura", "");
        }
    }

    public void dibujarRetencion() {
        if (tab_tabla.getValor("ide_cccfa") != null) {
            if (tab_tabla.getValor("ide_cncre") == null) {
                ret_retencion.nuevaRetencionVenta(tab_tabla.getValor("ide_cccfa"));
                ret_retencion.dibujar();
            } else {
                utilitario.agregarMensajeInfo("La Factura seleccionada ya tiene registrado un Comprobante de Retención", "");
            }

        } else {
            utilitario.agregarMensajeInfo("Seleccione una factura", "");

        }
    }

    public void abrirAnularFactura() {
        if (tab_tabla.getValor("ide_cccfa") != null) {
            if (fcc_factura.isFacturaElectronica()) {
                //valida que este en estado PENDIENTE o AUTORIZADA            
                if (tab_tabla.getValor("nombre_ccefa") != null) {
                    if (!tab_tabla.getValor("nombre_ccefa").equals(EstadoComprobanteEnum.PENDIENTE.getDescripcion())) {
                        if (!tab_tabla.getValor("nombre_ccefa").equals(EstadoComprobanteEnum.AUTORIZADO.getDescripcion())) {
                            utilitario.agregarMensajeError("No se puede anular la Factura Electrónica seleccionada", "Solo se pueden anular FacturaS en estado PENDIENTE y AUTORIZADO");
                            return;
                        }
                    }
                }
            }
            con_confirma.getBot_aceptar().setMetodo("anularFactura");
            con_confirma.dibujar();
        } else {
            utilitario.agregarMensajeError("Debe seleccionar una Factura", "");
        }

    }

    
public void seleccionarTod(){
    tab_tabla.setSeleccionados(null);
    Fila seleccionados[]= new Fila[tab_tabla.getTotalFilas()];
    for(int i=0; i< tab_tabla.getFilas().size();i++){
        seleccionados[i]=tab_tabla.getFilas().get(i);
    }
    tab_tabla.setSeleccionados(seleccionados);
    utilitario.addUpdate("tab_seleccion");
}
    public void sumarSeleccionNoConta() {
        double dou_suma = 0;
        for (Fila actual : tab_tabla.getSeleccionados()) {
            double valor = 0;
            try {
                valor = Double.parseDouble(String.valueOf(actual.getCampos()[tab_tabla.getNumeroColumna("total_cccfa")]));
            } catch (Exception e) {
            }
            dou_suma += valor;
        }
        eti1.setValue("NUM. SELECCIONADOS : " + tab_tabla.getSeleccionados().length + "  -  VALOR : " + utilitario.getFormatoNumero(dou_suma));
        utilitario.addUpdate("eti1");
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

    public void dibujarConfiguraFE() {

        tab_tabla = new Tabla();
        tab_tabla.setId("tab_tabla");
        tab_tabla.setTabla("sri_firma_digital", "ide_srfid", 9);
        tab_tabla.getColumna("ide_srfid").setVisible(false);
        tab_tabla.setTipoFormulario(true);
        tab_tabla.setCondicionSucursal(true);
        tab_tabla.setCampoOrden("disponible_srfid");
        tab_tabla.getColumna("password_srfid").setClave();
        tab_tabla.getColumna("disponible_srfid").setCheck();
        tab_tabla.getColumna("ruta_srfid").setControl("Texto");
        tab_tabla.setMostrarNumeroRegistros(false);
        tab_tabla.dibujar();
        if (tab_tabla.isEmpty()) {
            tab_tabla.insertar();
        }

        PanelTabla pat_panel = new PanelTabla();

        //Grid g1 = new Grid();
        //g1.setColumns(2);
        //g1.getChildren().add(new Etiqueta("<strong> HACE FACTURACIÓN ELECTRONICA ? </strong>"));
        //rad_facelectronica = new Radio();
        //rad_facelectronica.setRadio(utilitario.getListaPregunta());
        //rad_facelectronica.setValue(String.valueOf(com_pto_emision.getValue()));
        //g1.getChildren().add(rad_facelectronica);
        pat_panel.setPanelTabla(tab_tabla);
        //pat_panel.getChildren().add(g1);
        pat_panel.getMenuTabla().getItem_guardar().setRendered(true);
        pat_panel.getMenuTabla().getItem_insertar().setRendered(false);
        pat_panel.getMenuTabla().getItem_eliminar().setRendered(false);
        pat_panel.getMenuTabla().getItem_actualizar().setRendered(false);
        pat_panel.getMenuTabla().getItem_buscar().setRendered(false);

        List lista = new ArrayList();
        Object fila1[] = {
            "1", "PRUEBAS"
        };
        Object fila2[] = {
            "2", "PRODUCCIÓN"
        };
        lista.add(fila1);
        lista.add(fila2);

        List lista1 = new ArrayList();
        Object fila11[] = {
            "1", "NORMAL"
        };

        lista1.add(fila11);
        tab_tabla1 = new Tabla();
        tab_tabla1.setId("tab_tabla1");
        tab_tabla1.setTabla("sri_emisor", "ide_sremi", 10);
        tab_tabla1.getColumna("ide_sremi").setVisible(false);
        tab_tabla1.setTipoFormulario(true);
        tab_tabla1.setCondicionSucursal(true);
        tab_tabla1.getColumna("wsdl_recep_offline_sremi").setControl("Texto");
        tab_tabla1.getColumna("wsdl_autori_offline_sremi").setControl("Texto");
        tab_tabla1.setMostrarNumeroRegistros(false);
        tab_tabla1.getColumna("ambiente_sremi").setCombo(lista);
        tab_tabla1.getColumna("ambiente_sremi").setPermitirNullCombo(false);
        tab_tabla1.getColumna("tipoemision_sremi").setCombo(lista1);
        tab_tabla1.getColumna("tipoemision_sremi").setPermitirNullCombo(false);

        tab_tabla1.dibujar();
        if (tab_tabla1.isEmpty()) {
            tab_tabla1.insertar();
        }
        PanelTabla pat_panel1 = new PanelTabla();
        pat_panel1.setPanelTabla(tab_tabla1);
        pat_panel1.getMenuTabla().getItem_guardar().setRendered(true);
        pat_panel1.getMenuTabla().getItem_insertar().setRendered(false);
        pat_panel1.getMenuTabla().getItem_eliminar().setRendered(false);
        pat_panel1.getMenuTabla().getItem_actualizar().setRendered(false);
        pat_panel1.getMenuTabla().getItem_buscar().setRendered(false);

        Grupo gru = new Grupo();
        gru.getChildren().add(pat_panel);
        gru.getChildren().add(pat_panel1);

        Boton bot_aceptar = new Boton();
        bot_aceptar.setValue("Aceptar");
        bot_aceptar.setIcon("ui-icon-check");
        bot_aceptar.setMetodo("guardar");
        gru.getChildren().add(new Separator());
        gru.getChildren().add(bot_aceptar);
        mep_menu.dibujar(9, "fa fa-cogs", "Configuración para generar comprobantes electrónicos.", gru, false);

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
            fcc_factura.setPuntodeEmision(String.valueOf(com_pto_emision.getValue()));
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
        } else if (rep_reporte.getReporteSelecionado().equals("Iva en Ventas") || rep_reporte.getReporteSelecionado().equals("Reporte de Ventas")) {
            if (rep_reporte.isVisible()) {
                rep_reporte.cerrar();
                sel_sucursales.dibujar();
            } else if (sel_sucursales.isVisible()) {
                if (sel_sucursales.getSeleccionados() != null && sel_sucursales.getSeleccionados().isEmpty() == false) {
                    parametro = new HashMap();
                    parametro.put("sucursales", sel_sucursales.getSeleccionados());
                    sel_sucursales.cerrar();
                    sec_rango_reporte.dibujar();
                } else {
                    utilitario.agregarMensaje("Seleccione una sucursal", "");
                }
            } else if (sec_rango_reporte.isVisible()) {
                if (sec_rango_reporte.isFechasValidas()) {
                    parametro.put("fecha_inicio", sec_rango_reporte.getFecha1());
                    parametro.put("fecha_fin", sec_rango_reporte.getFecha2());
                    sec_rango_reporte.cerrar();
                    sel_rep.setSeleccionFormatoReporte(parametro, rep_reporte.getPath());
                    sel_rep.dibujar();
                } else {
                    utilitario.agregarMensaje("Rango de Fechas no válido", "");
                }
            }

        } else if (rep_reporte.getReporteSelecionado().equals("Comprobante de Inventario")) {
            if (rep_reporte.isVisible()) {
                List sql_cab_fac_inv = utilitario.getConexion().consultar("SELECT ide_incci  FROM inv_det_comp_inve WHERE ide_cccfa=" + tab_tabla.getValor("ide_cccfa"));
                if (sql_cab_fac_inv != null && !sql_cab_fac_inv.isEmpty()) {
                    parametro = new HashMap();
                    rep_reporte.cerrar();
                    parametro.put("ide_incci", Long.parseLong(sql_cab_fac_inv.get(0).toString()));
                    sel_rep.setSeleccionFormatoReporte(parametro, rep_reporte.getPath());
                    sel_rep.dibujar();
                    utilitario.addUpdate("rep_reporte,sel_rep");
                } else {
                    utilitario.agregarMensajeInfo("No se puede generar el reporte", "El factura seleccionada no tiene comprobante de inventario");
                }
            }
        }
    }

    public void actualizarFacturas() {
        if (mep_menu.getOpcion() == 1) {
            dibujarFacturas();
        } else if (mep_menu.getOpcion() == 2) {
            tab_tabla.setSql(ser_factura.getSqlFacturasNoContabilizadas(com_pto_emision.getValue() + "", cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha()));
            tab_tabla.ejecutarSql();
        } else if (mep_menu.getOpcion() == 3) {
            tab_tabla.setSql(ser_factura.getSqlFacturasAnuladas(com_pto_emision.getValue() + "", cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha()));
            tab_tabla.ejecutarSql();
        } else if (mep_menu.getOpcion() == 4) {
            tab_tabla.setSql(ser_factura.getSqlFacturasPorCobrar(cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha()));
            tab_tabla.ejecutarSql();
        } else if (mep_menu.getOpcion() == 5) {
            tab_tabla.setSql(ser_factura.getSqlTotalVentasMensuales(String.valueOf(com_periodo.getValue()), String.valueOf(com_vendedor.getValue())));
            tab_tabla.ejecutarSql();
            gca_facturas.limpiar();
            gca_facturas.agregarSerie(tab_tabla, "nombre_gemes", "total", "VENTAS " + String.valueOf(com_periodo.getValue()));
            gpa_facturas.limpiar();
            gpa_facturas.agregarSerie(tab_tabla, "nombre_gemes", "num_facturas");
            utilitario.addUpdate("gca_facturas,gpa_facturas");
        } else if (mep_menu.getOpcion() == 7) {
            tab_tabla.setSql(ser_factura.getSqlVentasMensuales(com_mes.getValue() + "", com_periodo.getValue() + ""));
            tab_tabla.ejecutarSql();
            tab_tabla1.setSql(ser_factura.getSqlNotasCreditoMensuales(com_mes.getValue() + "", com_periodo.getValue() + ""));
            tab_tabla1.ejecutarSql();
        }

    }

    @Override
    public void insertar() {
        fcc_factura.setPuntodeEmision(String.valueOf(com_pto_emision.getValue()));
        fcc_factura.nuevaFactura();
        fcc_factura.dibujar();
    }

    @Override
    public void guardar() {
        if (mep_menu.getOpcion() == 9) { //CONF FE
            //Actualiza parametro 
            // ser_factura.setHaceFacturaElectronica(rad_facelectronica.getValue().toString());
            if (tab_tabla.guardar()) {
                if (tab_tabla1.guardar()) {
                    guardarPantalla();
                }
            }
            return;
        }
        if (fcc_factura.isVisible()) {
            fcc_factura.guardar();
            if (fcc_factura.isVisible() == false) {
                //actualiza el punto de emision seleccionado y la tabla
                com_pto_emision.setValue(fcc_factura.getComboPuntoEmision().getValue());
                dibujarFacturas();
                tab_tabla.setFilaActual(fcc_factura.getTab_cab_factura().getValor("ide_cccfa"));
                tab_tabla.calcularPaginaActual();
                utilitario.addUpdate("com_pto_emision,tab_tabla");
            }
        } else if (ret_retencion.isVisible()) {
            ret_retencion.guardar();
            if (ret_retencion.isVisible() == false) {
                dibujarFacturas();
                tab_tabla.setFilaActual(ret_retencion.getIde_cccfa());
                tab_tabla.calcularPaginaActual();
                utilitario.addUpdate("tab_tabla");
            }
        } else if (asc_asiento.isVisible()) {
            asc_asiento.guardar();
            if (asc_asiento.isVisible() == false) {
             // dibujarFacturasNoContabilizadas();
            }
        }
    }

    public void anularFactura() {
        if (tab_tabla.getValor("ide_cccfa") != null) {
            ser_factura.anularFactura(tab_tabla.getValor("ide_cccfa"));
            if (guardarPantalla().isEmpty()) {
                con_confirma.cerrar();
                String aux = tab_tabla.getValorSeleccionado();
                tab_tabla.actualizar();
                tab_tabla.setFilaActual(aux);
                tab_tabla.calcularPaginaActual();
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
public void seleccionarTodas() {
        tab_tabla.setSeleccionados(null);
        Fila seleccionados[] = new Fila[tab_tabla.getTotalFilas()];
        for (int i = 0; i < tab_tabla.getFilas().size(); i++) {
            seleccionados[i] = tab_tabla.getFilas().get(i);
        }
        tab_tabla.setSeleccionados(seleccionados);
        //calculoTotal();
        sumarSeleccionNoConta();
    }
public void seleccinarInversa() {
        if (tab_tabla.getSeleccionados() == null) {
            seleccionarTodas();
        } else if (tab_tabla.getSeleccionados().length == tab_tabla.getTotalFilas()) {
            seleccionarNinguna();
        } else {
            Fila seleccionados[] = new Fila[tab_tabla.getTotalFilas() - tab_tabla.getSeleccionados().length];
            int cont = 0;
            for (int i = 0; i < tab_tabla.getFilas().size(); i++) {
                boolean boo_selecionado = false;
                for (int j = 0; j < tab_tabla.getSeleccionados().length; j++) {
                    if (tab_tabla.getSeleccionados()[j].equals(tab_tabla.getFilas().get(i))) {
                        boo_selecionado = true;
                        break;
                    }
                }
                if (boo_selecionado == false) {
                    seleccionados[cont] = tab_tabla.getFilas().get(i);
                    cont++;
                }
            }
            tab_tabla.setSeleccionados(seleccionados);
        }
        //calculoTotal();
        //sumarSeleccionNoConta();

    }

    public void seleccionarNinguna() {
        tab_tabla.setSeleccionados(null);
        //txt_total.setValue(utilitario.getFormatoNumero(0,2));
        //utilitario.addUpdate("txt_total");
        
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

    public Retencion getRet_retencion() {
        return ret_retencion;
    }

    public void setRet_retencion(Retencion ret_retencion) {
        this.ret_retencion = ret_retencion;
    }

    public Tabla getTab_tabla1() {
        return tab_tabla1;
    }

    public void setTab_tabla1(Tabla tab_tabla1) {
        this.tab_tabla1 = tab_tabla1;
    }

    public SeleccionCalendario getSec_rango_reporte() {
        return sec_rango_reporte;
    }

    public void setSec_rango_reporte(SeleccionCalendario sec_rango_reporte) {
        this.sec_rango_reporte = sec_rango_reporte;
    }

    public SeleccionTabla getSel_sucursales() {
        return sel_sucursales;
    }

    public void setSel_sucursales(SeleccionTabla sel_sucursales) {
        this.sel_sucursales = sel_sucursales;
    }

}
